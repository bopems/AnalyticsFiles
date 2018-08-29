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

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }
}
