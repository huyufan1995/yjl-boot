package io.renren.cms.service;

import io.renren.cms.entity.ForbiddenMemberEntity;

/**
 * 封禁备注
 */
public interface ForbiddenService {

	void save(ForbiddenMemberEntity forbiddenMemberEntity);

	int deleteByOpenId(String openid);

	ForbiddenMemberEntity queryObjectBySuperiorId(Integer superiorId);

	ForbiddenMemberEntity queryObjectByOpenid(String openid);
}
