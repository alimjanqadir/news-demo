package com.example.alimjan.news.network.pojo;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class News {

    @SerializedName("hits")
    private List<HitsItem> hits;

    @SerializedName("page")
    private int page;

    @SerializedName("params")
    private String params;

    @SerializedName("nbPages")
    private int nbPages;


    public void setHits(List<HitsItem> hits) {
        this.hits = hits;
    }

    public List<HitsItem> getHits() {
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
                "News{" +
                        "hits = '" + hits + '\'' +
                        ",page = '" + page + '\'' +
                        ",params = '" + params + '\'' +
                        ",nbPages = '" + nbPages + '\'' +
                        "}";
    }
}