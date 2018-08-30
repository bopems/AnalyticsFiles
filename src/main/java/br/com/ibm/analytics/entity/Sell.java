package br.com.ibm.analytics.entity;

import java.util.List;

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

    //[1-34-10,2-33-1.50,3-40-0.10]
    public void setItems(String items) {
        String[] listItems = items.split("\\s[\\[]([0-9]{1,9}-[0-9]{1,9}-[0-9]{1,9}[.]*[0-9]{0,2}),([0-9]{1,9}-[0-9]{1,9}-[0-9]{1,9}[.]*[0-9]{0,2}),([0-9]{1,9}-[0-9]{1,9}-[0-9]{1,9}[.]*[0-9]{0,2})[\\]]]");

        for (String _item : listItems) {

            String[] item = _item.split("-");
            Item insert = new Item();
            insert.setId(item[0]);
            insert.setQuantity(Integer.parseInt(item[1]));
            insert.setPrice(Double.parseDouble(item[2]));

            this.items.add(insert);
        }
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }
}
