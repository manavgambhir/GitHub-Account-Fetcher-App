package com.example.apicalljson;

import java.util.ArrayList;

public class apiResult {
    private Integer total_count;
    private Boolean incomplete_results;
    ArrayList<Users> items;

    public apiResult() {

    }

    public apiResult(Integer total_count, Boolean incomplete_results, ArrayList<Users> items) {
        this.total_count = total_count;
        this.incomplete_results = incomplete_results;
        this.items = items;
    }

    public Integer getTotal_count() {
        return total_count;
    }

    public Boolean getIncomplete_results() {
        return incomplete_results;
    }

    public ArrayList<Users> getItems() {
        return items;
    }
}
