package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.cms.entity.MemberRemovalRecordEntity;

/**
 * 会员迁移记录接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-06 18:36:05
 */
public interface MemberRemovalRecordService {

	MemberRemovalRecordEntity queryObject(Integer id);

	List<MemberRemovalRecordEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(MemberRemovalRecordEntity memberRemovalRecord);

	void update(MemberRemovalRecordEntity memberRemovalRecord);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	List<MemberRemovalRecordEntity> queryListBySourceMember(Integer sourceMember);
}
