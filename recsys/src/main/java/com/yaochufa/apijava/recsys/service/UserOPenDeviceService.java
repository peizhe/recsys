package com.yaochufa.apijava.recsys.service;

/**
 * 处理用户登录，活跃相关的业务
 * @author root
 *
 */
public interface UserOPenDeviceService {

	/**
	 * 根据最后一次打开时间增量更新活跃用户
	 */
	void loadActiveUserToRedisByDeltaMode();
	/**
	 * 根据最后一次打开时间增量更新活跃设备
	 */
	void loadActiveDeviceToRedisByDeltaMode();
}
