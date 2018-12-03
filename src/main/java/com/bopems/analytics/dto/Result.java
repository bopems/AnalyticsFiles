package com.bopems.analytics.dto;

import java.util.List;

public class Result {

    private List<String> idSaleHighest;
    private int sellerCount;
    private int clientCount;
    private List<String> sellerWorsest;

    public List<String> getIdSaleHighest() {
        return idSaleHighest;
    }

    public void setIdSaleHighest( List<String> idSaleHighest) {
        this.idSaleHighest = idSaleHighest;
    }

    public int getSellerCount() {
        return sellerCount;
    }

    public void setSellerCount(int sellerCount) {
        this.sellerCount = sellerCount;
    }

    public int getClientCount() {
        return clientCount;
    }

    public void setClientCount(int clientCount) {
        this.clientCount = clientCount;
    }

    public List<String> getSellerWorsest() {
        return sellerWorsest;
    }

    public void setSellerWorsest(List<String> sellerWorsest) {
        this.sellerWorsest = sellerWorsest;
    }
}
