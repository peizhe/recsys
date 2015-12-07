package com.yaochufa.apijava.recsys.model;

import java.io.Serializable;

public class UserProductRecommend implements Serializable {
    private Integer userId;

    private String productIds;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds == null ? null : productIds.trim();
    }
}