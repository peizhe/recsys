package com.yaochufa.apijava.recsys.model;

import java.util.Date;

public class Search {
    private Integer id;

    private String product;

    private Integer category;

    private String categoryName;

    private String address;

    private String pcmaintitle;

    private String pcsubtitle;

    private String appmaintitle;

    private String appsubtitle;

    private String tags;

    private String properties;

    private String scenicspots;

    private String themes;

    private String fiterThemes;

    private String longitude;

    private String latitude;

    private String geohash;

    private String region;

    private String province;

    private String provincealias;

    private String city;

    private String cityalias;

    private String district;

    private Float price;

    private Float retailprice;

    private String pcimageurl;

    private String appimageurl;

    private Integer channellinkid;

    private Integer salesvolume;

    private Float grade;

    private Float rank;

    private Date createddate;

    private Date syncTime;

    private Integer commentcount;

    private Boolean isReduce;

    private Boolean isOrderd;

    private Boolean couponedIos;

    private Boolean couponedAndroid;

    private String productCityAlias;

    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product == null ? null : product.trim();
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getPcmaintitle() {
        return pcmaintitle;
    }

    public void setPcmaintitle(String pcmaintitle) {
        this.pcmaintitle = pcmaintitle == null ? null : pcmaintitle.trim();
    }

    public String getPcsubtitle() {
        return pcsubtitle;
    }

    public void setPcsubtitle(String pcsubtitle) {
        this.pcsubtitle = pcsubtitle == null ? null : pcsubtitle.trim();
    }

    public String getAppmaintitle() {
        return appmaintitle;
    }

    public void setAppmaintitle(String appmaintitle) {
        this.appmaintitle = appmaintitle == null ? null : appmaintitle.trim();
    }

    public String getAppsubtitle() {
        return appsubtitle;
    }

    public void setAppsubtitle(String appsubtitle) {
        this.appsubtitle = appsubtitle == null ? null : appsubtitle.trim();
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties == null ? null : properties.trim();
    }

    public String getScenicspots() {
        return scenicspots;
    }

    public void setScenicspots(String scenicspots) {
        this.scenicspots = scenicspots == null ? null : scenicspots.trim();
    }

    public String getThemes() {
        return themes;
    }

    public void setThemes(String themes) {
        this.themes = themes == null ? null : themes.trim();
    }

    public String getFiterThemes() {
        return fiterThemes;
    }

    public void setFiterThemes(String fiterThemes) {
        this.fiterThemes = fiterThemes == null ? null : fiterThemes.trim();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash == null ? null : geohash.trim();
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getProvincealias() {
        return provincealias;
    }

    public void setProvincealias(String provincealias) {
        this.provincealias = provincealias == null ? null : provincealias.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getCityalias() {
        return cityalias;
    }

    public void setCityalias(String cityalias) {
        this.cityalias = cityalias == null ? null : cityalias.trim();
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getRetailprice() {
        return retailprice;
    }

    public void setRetailprice(Float retailprice) {
        this.retailprice = retailprice;
    }

    public String getPcimageurl() {
        return pcimageurl;
    }

    public void setPcimageurl(String pcimageurl) {
        this.pcimageurl = pcimageurl == null ? null : pcimageurl.trim();
    }

    public String getAppimageurl() {
        return appimageurl;
    }

    public void setAppimageurl(String appimageurl) {
        this.appimageurl = appimageurl == null ? null : appimageurl.trim();
    }

    public Integer getChannellinkid() {
        return channellinkid;
    }

    public void setChannellinkid(Integer channellinkid) {
        this.channellinkid = channellinkid;
    }

    public Integer getSalesvolume() {
        return salesvolume;
    }

    public void setSalesvolume(Integer salesvolume) {
        this.salesvolume = salesvolume;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public Float getRank() {
        return rank;
    }

    public void setRank(Float rank) {
        this.rank = rank;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }

    public Date getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Date syncTime) {
        this.syncTime = syncTime;
    }

    public Integer getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(Integer commentcount) {
        this.commentcount = commentcount;
    }

    public Boolean getIsReduce() {
        return isReduce;
    }

    public void setIsReduce(Boolean isReduce) {
        this.isReduce = isReduce;
    }

    public Boolean getIsOrderd() {
        return isOrderd;
    }

    public void setIsOrderd(Boolean isOrderd) {
        this.isOrderd = isOrderd;
    }

    public Boolean getCouponedIos() {
        return couponedIos;
    }

    public void setCouponedIos(Boolean couponedIos) {
        this.couponedIos = couponedIos;
    }

    public Boolean getCouponedAndroid() {
        return couponedAndroid;
    }

    public void setCouponedAndroid(Boolean couponedAndroid) {
        this.couponedAndroid = couponedAndroid;
    }

    public String getProductCityAlias() {
        return productCityAlias;
    }

    public void setProductCityAlias(String productCityAlias) {
        this.productCityAlias = productCityAlias == null ? null : productCityAlias.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}