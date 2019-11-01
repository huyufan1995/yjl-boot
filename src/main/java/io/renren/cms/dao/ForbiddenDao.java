package io.renren.cms.dao;

import io.renren.cms.entity.ForbiddenMemberEntity;
import io.renren.dao.BaseDao;

/**
 * 封禁备注
 *
 */
public interface ForbiddenDao extends BaseDao<ForbiddenMemberEntity> {

	ForbiddenMemberEntity queryObjectBySuperiorId(Integer superiorId);

	ForbiddenMemberEntity queryObjectByOpenid(String openid);

}
