package br.com.ibm.analytics.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sell {

    public final String identifier = "003";
    private String id;
    private List<Item> items;
    private String salesman;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(String inputItems) {
        String regex = "(\\w{1,9}-\\d{1,9}-\\d{1,9}[.]{0,1}\\d{0,4})";
        Pattern pattern = Pattern.compile("[\\[]" + regex + "[,]" + regex + "[,]" + regex + "[\\]]");
        Matcher matcher = pattern.matcher(inputItems);
        matcher.find();

        List<Item> items = new ArrayList<>();

        for (int idx = 1; idx <= matcher.groupCount(); idx++) {

            String[] item = matcher.group(idx).split("-");
            Item insert = new Item();
            insert.setId(item[0]);
            insert.setQuantity(Float.parseFloat(item[1]));
            insert.setPrice(Float.parseFloat(item[2]));

            items.add(insert);
        }

        this.items = items;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }
}
