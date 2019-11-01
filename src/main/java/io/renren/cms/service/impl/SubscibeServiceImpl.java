package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.cms.dao.SubscibeDao;
import io.renren.cms.entity.SubscibeEntity;
import io.renren.cms.service.SubscibeService;

/**
 * 订阅服务实现
 * 
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("subscibeService")
public class SubscibeServiceImpl implements SubscibeService {

	@Autowired
	private SubscibeDao subscibeDao;

	@Override
	public SubscibeEntity queryObject(Integer id) {
		return subscibeDao.queryObject(id);
	}

	@Override
	public List<SubscibeEntity> queryList(Map<String, Object> map) {
		return subscibeDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return subscibeDao.queryTotal(map);
	}

	@Override
	public void save(SubscibeEntity subscibe) {
		subscibeDao.save(subscibe);
	}

	@Override
	public void update(SubscibeEntity subscibe) {
		subscibeDao.update(subscibe);
	}

	@Override
	public void delete(Integer id) {
		subscibeDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		subscibeDao.deleteBatch(ids);
	}

	@Override
	public SubscibeEntity queryObjectByOpenid(String openid) {
		return subscibeDao.queryObjectByOpenid(openid);
	}

	@Override
	public List<SubscibeEntity> queryListByStatus() {
		return subscibeDao.queryListByStatus();
	}

	@Override
	public int deleteByFormid(String formid) {
		return subscibeDao.deleteByFormid(formid);
	}

	@Override
	public List<SubscibeEntity> queryListByPlatformGroup(String platform) {
		return subscibeDao.queryListByPlatformGroup(platform);
	}

}
