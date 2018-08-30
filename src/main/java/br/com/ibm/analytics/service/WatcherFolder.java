package br.com.ibm.analytics.service;

import br.com.ibm.analytics.entity.*;
import com.esotericsoftware.minlog.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class WatcherFolder {

    private static final Logger log = LoggerFactory.getLogger(WatcherFolder.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private String folderInput;
    private String folderOutput;


    public WatcherFolder(String folder) throws IOException {

        this.folderInput = new StringBuilder(folder).append("\\data\\in").toString();
        this.folderOutput = new StringBuilder(folder).append("\\data\\out").toString();

        this.Init();
        this.Start();
    }

    private void Init() throws IOException {

        log.info("  >> Now {} - Path: {}", dateFormat.format(new Date()), this.folderInput);

        File files[];
        File path = new File(this.folderInput);
        files = path.listFiles(file -> file.getName().endsWith(".bat"));

        for(int i = 0; i < files.length; i++) {
            this.ReadFile(files[0]);
        }

    }

    public void Start() {

        log.info("  >> {}", this.folderInput);

        Path dir = Paths.get(this.folderInput);

        try {
            WatchService watcher = dir.getFileSystem().newWatchService();
            dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);

            for (;;) {

                WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException x) {
                    return;
                }

                for (WatchEvent<?> event: key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == OVERFLOW) {
                        continue;
                    }

                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        log.info("  >> Created: {}", event.context().toString());

                        this.ReadFile((File) event.context());

                    }

                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }

        } catch (Exception e) {
            Log.error("  >> {}", e.toString());
        }

    }

    private void ReadFile(File file) throws IOException {

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line = "";

        List<Seller> sellers = new ArrayList<>();
        List<Client> clients = new ArrayList<>();
        List<Sell> sells = new ArrayList<>();

        while ((line = bufferedReader.readLine()) != null) {
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

        /// TODO: Inserir dados
        this.WriteOutput(file);
    }

    private Object prepareLineToObject(String line) {

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

    private void WriteOutput(File file) {
        /// TODO
    }

}
