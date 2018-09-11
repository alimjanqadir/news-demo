package com.example.alimjan.news.api.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class NewsResponse {

    @SerializedName("hits")
    private List<Hit> hits = new ArrayList<>();

    @SerializedName("page")
    private int page;

    @SerializedName("params")
    private String params;

    @SerializedName("nbPages")
    private int nbPages;


    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    public List<Hit> getHits() {
        return hits;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getParams() {
        return params;
    }

    public void setNbPages(int nbPages) {
        this.nbPages = nbPages;
    }

    public int getNbPages() {
        return nbPages;
    }

    @Override
    public String toString() {
        return
                "NewsResponse{" +
                        "hits = '" + hits + '\'' +
                        ",page = '" + page + '\'' +
                        ",params = '" + params + '\'' +
                        ",nbPages = '" + nbPages + '\'' +
                        "}";
    }
}