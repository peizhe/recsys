package com.yaochufa.apijava.recsys.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.yaochufa.apijava.recsys.constant.CollabFilterConst;
import com.yaochufa.apijava.recsys.mapper.FActUserFirstLastOpenRecordDeviceMapper;
import com.yaochufa.apijava.recsys.mapper.FActUserFirstLastOpenRecordUserMapper;
import com.yaochufa.apijava.recsys.model.FActUserFirstLastOpenRecordDevice;
import com.yaochufa.apijava.recsys.model.FActUserFirstLastOpenRecordUser;
import com.yaochufa.apijava.recsys.service.UserOPenDeviceService;

/**
 * 处理用户登录，活跃相关的业务
 * @author root
 *
 */
@Service
public class UserOPenDeviceServiceimpl implements  UserOPenDeviceService{

	@Resource(name="javaSerRedisTemplate")
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private FActUserFirstLastOpenRecordDeviceMapper fActUserFirstLastOpenRecordDeviceMapper;
	@Autowired
	private FActUserFirstLastOpenRecordUserMapper fActUserFirstLastOpenRecordUserMapper;
	
	@Override
	public void loadActiveUserToRedisByDeltaMode() {
		Date currDate=new Date();
		Date date=(Date) redisTemplate.opsForValue().get(CollabFilterConst.FActUserFirstLastOpenRecordUser_last_read_key);
		FActUserFirstLastOpenRecordUser pa=new FActUserFirstLastOpenRecordUser();
		pa.setLastopentime(date);
		List<Map<String,Object>> list=fActUserFirstLastOpenRecordUserMapper.selectActiveUser(pa);
		if(list.size()==0){
			redisTemplate.opsForValue().set(CollabFilterConst.FActUserFirstLastOpenRecordUser_last_read_key,currDate);
		}else{
			redisTemplate.opsForValue().set(CollabFilterConst.FActUserFirstLastOpenRecordUser_last_read_key,list.get(0).get("lastOpenTime"));
		}
		ExecutorService ex=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for(Map<String,Object> map :list){
			ex.execute(new WriteActiveUserDataToRedisTask(map, redisTemplate));
		}
		ex.shutdown();
	}

	@Override
	public void loadActiveDeviceToRedisByDeltaMode() {
		Date currDate=new Date();
		Date date=(Date) redisTemplate.opsForValue().get(CollabFilterConst.FActUserFirstLastOpenRecordDevice_last_read_key);
		FActUserFirstLastOpenRecordDevice pa=new FActUserFirstLastOpenRecordDevice();
		pa.setLastopentime(date);
		List<Map<String,Object>> list=fActUserFirstLastOpenRecordDeviceMapper.selectActiveDevice(pa);
		if(list.size()==0){
			redisTemplate.opsForValue().set(CollabFilterConst.FActUserFirstLastOpenRecordDevice_last_read_key,currDate);
		}else{
			redisTemplate.opsForValue().set(CollabFilterConst.FActUserFirstLastOpenRecordDevice_last_read_key,list.get(0).get("lastOpenTime"));
		}
		ExecutorService ex=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for(Map<String,Object> map :list){
			ex.execute(new WriteActiveDeviceDataToRedisTask(map, redisTemplate));
		}
		ex.shutdown();
	}

	class WriteActiveUserDataToRedisTask implements Runnable{
		Map<String,Object> map;
		RedisTemplate<String, Object> redisTemplate;
		
		public WriteActiveUserDataToRedisTask(Map<String, Object> map,
				RedisTemplate<String, Object> redisTemplate) {
			super();
			this.map = map;
			this.redisTemplate = redisTemplate;
		}

		@Override
		public void run() {
			redisTemplate.opsForHash().put(CollabFilterConst.ACTIVE_USER_KEY_PRIFIX+map.get("userId"), "lastOpenTime",map.get("lastOpenTime"));
			redisTemplate.expire(CollabFilterConst.ACTIVE_USER_KEY_PRIFIX+map.get("userId"), 366, TimeUnit.DAYS);
		}
		
	}
	
	class WriteActiveDeviceDataToRedisTask implements Runnable{
		Map<String,Object> map;
		RedisTemplate<String, Object> redisTemplate;
		
		public WriteActiveDeviceDataToRedisTask(Map<String, Object> map,
				RedisTemplate<String, Object> redisTemplate) {
			super();
			this.map = map;
			this.redisTemplate = redisTemplate;
		}

		@Override
		public void run() {
			redisTemplate.opsForHash().put(CollabFilterConst.ACTIVE_USER_KEY_PRIFIX+map.get("machine"), "lastOpenTime",map.get("lastOpenTime"));
			redisTemplate.expire(CollabFilterConst.ACTIVE_USER_KEY_PRIFIX+map.get("userId"), 366, TimeUnit.DAYS);
		}
		
	}
}
