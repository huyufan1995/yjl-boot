package io.renren.cms.service.impl;

import io.renren.cms.dao.ForbiddenDao;
import io.renren.cms.entity.ForbiddenMemberEntity;
import io.renren.cms.service.ForbiddenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("forbiddenSerivce")
public class ForbiddenServiceImpl implements ForbiddenService {

	@Autowired
	private ForbiddenDao forbiddenDao;

	@Override
	public void save(ForbiddenMemberEntity forbiddenMemberEntity) {
		forbiddenDao.save(forbiddenMemberEntity);
	}

	@Override
	public int deleteByOpenId(String openId) {
		return forbiddenDao.deleteByOpenId(openId);
	}

	@Override
	public ForbiddenMemberEntity queryObjectBySuperiorId(Integer superiorId) {
		return forbiddenDao.queryObjectBySuperiorId(superiorId);
	}

	@Override
	public ForbiddenMemberEntity queryObjectByOpenid(String openid) {
		return forbiddenDao.queryObjectByOpenid(openid);
	}
}
