package com.yaochufa.apijava.lang.common.bean;

/**
 * 产品推荐请求bean
 * 
 * @author xuht
 *
 */
public class ItemRecSysRequestBean
{
	private Integer userId;
	
	private Integer productId;
	
	private String province;

	private String city;
	/**
	 * 经度
	 */
	private Double lng;
	/**
	 * 纬度
	 */
	private Double lat;

	private int Size;
	
	public Integer getUserId()
	{
		return userId;
	}

	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}

	public Integer getProductId()
	{
		return productId;
	}

	public void setProductId(Integer productId)
	{
		this.productId = productId;
	}

	public String getProvince()
	{
		return province;
	}

	public void setProvince(String province)
	{
		this.province = province;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public Double getLng()
	{
		return lng;
	}

	public void setLng(Double lng)
	{
		this.lng = lng;
	}

	public Double getLat()
	{
		return lat;
	}

	public void setLat(Double lat)
	{
		this.lat = lat;
	}

	public int getSize()
	{
		return Size;
	}

	public void setSize(int size)
	{
		Size = size;
	}

}
