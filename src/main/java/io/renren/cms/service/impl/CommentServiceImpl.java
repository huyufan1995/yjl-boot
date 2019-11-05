package io.renren.cms.service.impl;

import io.renren.cms.dao.CommentDao;
import io.renren.cms.entity.CommentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.service.CommentService;

/**
 * 评论表服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentDao commentDao;
	
	@Override
	public CommentEntity queryObject(Integer id){
		return commentDao.queryObject(id);
	}
	
	@Override
	public List<CommentEntity> queryList(Map<String, Object> map){
		return commentDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return commentDao.queryTotal(map);
	}
	
	@Override
	public void save(CommentEntity comment){
		commentDao.save(comment);
	}
	
	@Override
	public void update(CommentEntity comment){
		commentDao.update(comment);
	}
	
	@Override
	public void delete(Integer id){
		commentDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		commentDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return commentDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return commentDao.logicDelBatch(ids);
	}
	
	
}
