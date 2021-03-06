package io.renren.cms.service.impl;

import io.renren.cms.vo.LikeEntityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.cms.dao.LikeDao;
import io.renren.cms.entity.LikeEntity;
import io.renren.cms.service.LikeService;

/**
 * 点赞表服务实现
 * @author huyufan
 *
 */
@Service("likeService")
public class LikeServiceImpl implements LikeService {

	@Autowired
	private LikeDao likeDao;
	
	@Override
	public LikeEntity queryObject(Integer id){
		return likeDao.queryObject(id);
	}
	
	@Override
	public List<LikeEntity> queryList(Map<String, Object> map){
		return likeDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return likeDao.queryTotal(map);
	}
	
	@Override
	public void save(LikeEntity like){
		likeDao.save(like);
	}
	
	@Override
	public void update(LikeEntity like){
		likeDao.update(like);
	}
	
	@Override
	public void delete(Integer id){
		likeDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		likeDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return likeDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return likeDao.logicDelBatch(ids);
	}

	@Override
	public Boolean deleteByOpenIdAndLikeTypeAndDataId(HashMap<String, Object> params) {
		return likeDao.deleteByOpenIdAndLikeTypeAndDataId(params);
	}

	@Override
	public List<LikeEntityVO> queryListVO(Map<String, Object> map) {
		return likeDao.queryListVO(map);
	}


}
