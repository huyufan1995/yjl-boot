package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.MemberRemovalRecordEntity;
import io.renren.dao.BaseDao;

/**
 * 会员迁移记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-06 18:36:05
 */
public interface MemberRemovalRecordDao extends BaseDao<MemberRemovalRecordEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	List<MemberRemovalRecordEntity> queryListBySourceMember(Integer sourceMember);
}
