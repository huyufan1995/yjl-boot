package io.renren.cms.service.impl;

import io.renren.cms.dao.ShareDao;
import io.renren.cms.entity.ShareEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.service.ShareService;

/**
 * 分享表服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("shareService")
public class ShareServiceImpl implements ShareService {

	@Autowired
	private ShareDao shareDao;
	
	@Override
	public ShareEntity queryObject(Integer id){
		return shareDao.queryObject(id);
	}
	
	@Override
	public List<ShareEntity> queryList(Map<String, Object> map){
		return shareDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return shareDao.queryTotal(map);
	}
	
	@Override
	public void save(ShareEntity share){
		shareDao.save(share);
	}
	
	@Override
	public void update(ShareEntity share){
		shareDao.update(share);
	}
	
	@Override
	public void delete(Integer id){
		shareDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		shareDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return shareDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return shareDao.logicDelBatch(ids);
	}
	
	
}
