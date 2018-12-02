package com.jeisonmp.archib.analytics.service;

import com.jeisonmp.archib.analytics.dto.Result;
import br.com.ibm.analytics.entity.*;
import com.esotericsoftware.minlog.Log;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class WatcherFiles {

    private static final Logger log = LoggerFactory.getLogger(WatcherFiles.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private String folderInput;
    private String folderOutput;

    public WatcherFiles(String folder) throws IOException {

        this.folderInput = new StringBuilder(folder).append("\\data\\in\\").toString();
        this.folderOutput = new StringBuilder(folder).append("\\data\\out\\").toString();

        this.init();
        this.startWacther();
    }

    private void init() throws IOException {

        log.info("  >> Now {} - Path: {}", dateFormat.format(new Date()), this.folderInput);

        File files[];
        File path = new File(this.folderInput);
        files = path.listFiles(file -> file.getName().endsWith(".bat"));

        for(int i = 0; i < files.length; i++) {
            try {
                this.readFile(files[0]);
                files[0].delete();
            }
            catch (Exception e) {
                Log.error("  >> {}", e.toString());
            }
        }

    }

    private void startWacther() throws IOException {

        log.info("  >> {}", this.folderInput);

        Path dir = Paths.get(this.folderInput);

        WatchService watcher = dir.getFileSystem().newWatchService();
        dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);

        for (;;) {

            try {
                WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException e) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == OVERFLOW) {
                        continue;
                    }

                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        log.info("  >> Created: {}", event.context().toString());

                        String fread = new StringBuilder(this.folderInput).append(event.context().toString()).toString();
                        File file = new File(fread);
                        this.readFile(file);
                        file.delete();
                    }

                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
            catch (Exception e) {
                Log.error("  >> {}", e.toString());
            }
        }

    }

    public void readFile(File file) throws IOException {

        List<String> lines = FileUtils.readLines(file, "UTF-8");

        List<Seller> sellers = new ArrayList<>();
        List<Client> clients = new ArrayList<>();
        List<Sell> sells = new ArrayList<>();

        for (String line : lines) {

            Log.info("  >> " + line);

            Object obj = this.prepareLineToObject(line);

            if (obj instanceof Seller) {
                sellers.add((Seller)obj);
            }
            else if (obj instanceof Client) {
                clients.add((Client)obj);
            }
            else if (obj instanceof Sell) {
                sells.add((Sell)obj);
            }
        }

        try {
            this.writeOutput(file.getName(), this.writeResult(sellers, clients, sells));
        }
        catch (Exception e) {
            Log.error(" >> {}", e.toString());
        }
    }

    public String writeResult(List<Seller> sellers, List<Client> clients, List<Sell> sells) {

        Result obj = new Result();
        obj.setClientCount(clients.size());
        obj.setSellerCount(clients.size());
        SellerWorsestIdSaleHighest(sells, obj);

        String result = new Gson().toJson(obj);
        return result;
    }

    private void SellerWorsestIdSaleHighest(List<Sell> sells, Result result) {
        Map<String, Float> sumSells = new HashMap<>();
        AtomicReference<Float> total = new AtomicReference<>((float) 0);

        Iterator<Sell> it = sells.iterator();
        while(it.hasNext()) {
            total.set((float)0);
            Sell sell = it.next();
            sell.getItems().forEach(item -> total.updateAndGet(v -> new Float((float) (v + item.getPrice()))));
            sumSells.put(sell.getSalesman(), total.get());
        }

        List<String> names = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        Float sellWorsest = Float.MAX_VALUE;
        Float sellHighest = Float.MIN_VALUE;
        for (Map.Entry<String, Float> entry : sumSells.entrySet()) {
            if (entry.getValue() <= sellWorsest) {
                if (entry.getValue().equals(sellWorsest)) {
                    names.add(entry.getKey());
                }
                else {
                    names.clear();
                    names.add(entry.getKey());
                }
                sellWorsest = entry.getValue();
            }
            if (entry.getValue() >= sellHighest) {
                if (entry.getValue().equals(sellHighest)) {
                    ids.add(entry.getKey());
                }
                else {
                    ids.clear();
                    ids.add(entry.getKey());
                }
                sellHighest = entry.getValue();
            }
        }

        result.setIdSaleHighest(ids);
        result.setSellerWorsest(names);
    }

    public Object prepareLineToObject(String line) {

        try {
            String[] data = line.split("ç");

            if (!data[0].isEmpty() && Integer.compare(data[0].length(), 3) == 0) {

                if (data[0].equals("001")) {
                    Seller seller = new Seller();
                    seller.setCpf(data[1]);
                    seller.setName(data[2]);
                    seller.setSalary(Double.parseDouble(data[3]));

                    return seller;
                }
                else if (data[0].equals("002")) {
                    Client client = new Client();
                    client.setCnpj(data[1]);
                    client.setName(data[2]);
                    client.setBusiness(data[3]);

                    return client;
                }
                else if (data[0].equals("003")) {
                    Sell sell = new Sell();
                    sell.setId(data[1]);
                    sell.setItems(data[2]);
                    sell.setSalesman(data[3]);

                    return sell;
                }

            } else {
                Log.error(" >> {}", "'Id' - Identificador invalido");
            }
        }
        catch (Exception e) {
            Log.error(" >> {}", e.toString());
        }

        return null;
    }

    private void writeOutput(String fileName, String data) throws IOException {
        /// TODO: Preparar os resultados

        String fileOut = new StringBuilder(this.folderOutput).append(fileName.replace(".bat", ".done.bat")).toString();
        FileWriter arq = new FileWriter(fileOut);
        PrintWriter saveArq = new PrintWriter(arq);
        saveArq.print(data);
        arq.close();

    }

}
