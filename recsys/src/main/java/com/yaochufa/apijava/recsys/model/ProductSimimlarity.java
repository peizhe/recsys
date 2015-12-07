package com.yaochufa.apijava.recsys.model;

import java.io.Serializable;
import java.util.Date;

public class ProductSimimlarity implements Serializable {
    private Integer productIdA;

    private Integer productIdB;

    private Float similarity;

    private Date createdAt;

    public Integer getProductIdA() {
        return productIdA;
    }

    public void setProductIdA(Integer productIdA) {
        this.productIdA = productIdA;
    }

    public Integer getProductIdB() {
        return productIdB;
    }

    public void setProductIdB(Integer productIdB) {
        this.productIdB = productIdB;
    }

    public Float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Float similarity) {
        this.similarity = similarity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}