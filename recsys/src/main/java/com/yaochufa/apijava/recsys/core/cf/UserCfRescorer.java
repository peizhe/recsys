/*
 * Source code for listing 5.4
 * 
 */
package com.yaochufa.apijava.recsys.core.cf;

import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.springframework.data.redis.core.RedisTemplate;

import com.yaochufa.apijava.lang.util.GeoUtil;
import com.yaochufa.apijava.recsys.util.GlobalVar;
import com.yaochufa.apijava.recsys.util.SpringContextHelper;

public class UserCfRescorer implements IDRescorer
{

	private RedisTemplate<String, Object> redisTemplate = SpringContextHelper
			.getBean("javaSerRedisTemplate", RedisTemplate.class);
	private Double lat;
	private Double lng;
	private boolean useDistance;

	public UserCfRescorer(Double lat, Double lng)
	{
		this.lat = lat;
		this.lng = lng;
		setUseDistance();
	}

	private void setUseDistance()
	{
		this.useDistance = this.lat != null && this.lng != null;

	}

	@Override
	public double rescore(long id, double originalScore)
	{
		double s = 1;
		if (useDistance)
		{
			Double toLatitude = (Double) redisTemplate.opsForHash()
					.get(GlobalVar.PRODUCT_HASH_KEY + id, "latitude");
			Double toLongitude = (Double) redisTemplate.opsForHash()
					.get(GlobalVar.PRODUCT_HASH_KEY + id, "longitude");
			if (toLatitude == null & toLongitude == null)
			{
				return originalScore;
			}
			s = GeoUtil.getDistance(lat, lng, toLatitude, toLongitude);
			if (s < 1)
			{
				s = 1d;
			}
			s = 1 + 1 / s;
		}

		return originalScore * s;
	}

	@Override
	public boolean isFiltered(long id)
	{
		return isInValid(id) && !nearBy(GlobalVar.NEARBY_SIZE, id);
	}

	private boolean nearBy(int nearbySize, long id)
	{
		Double toLatitude = (Double) redisTemplate.opsForHash()
				.get(GlobalVar.PRODUCT_HASH_KEY + id, "latitude");
		Double toLongitude = (Double) redisTemplate.opsForHash()
				.get(GlobalVar.PRODUCT_HASH_KEY + id, "longitude");
		if (toLatitude == null & toLongitude == null)
		{
			return true;
		}
		return GeoUtil.getDistance(lat, lng, toLatitude,
				toLongitude) <= nearbySize;
	}

	public boolean isInValid(long id)
	{
		Object t = redisTemplate.opsForHash()
				.get(GlobalVar.PRODUCT_HASH_KEY + id, "category");
		return t == null;
	}

}
