package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.cms.dao.MemberRemovalRecordDao;
import io.renren.cms.entity.MemberRemovalRecordEntity;
import io.renren.cms.service.MemberRemovalRecordService;

/**
 * 会员迁移记录服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("memberRemovalRecordService")
public class MemberRemovalRecordServiceImpl implements MemberRemovalRecordService {

	@Autowired
	private MemberRemovalRecordDao memberRemovalRecordDao;

	@Override
	public MemberRemovalRecordEntity queryObject(Integer id) {
		return memberRemovalRecordDao.queryObject(id);
	}

	@Override
	public List<MemberRemovalRecordEntity> queryList(Map<String, Object> map) {
		return memberRemovalRecordDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return memberRemovalRecordDao.queryTotal(map);
	}

	@Override
	public void save(MemberRemovalRecordEntity memberRemovalRecord) {
		memberRemovalRecordDao.save(memberRemovalRecord);
	}

	@Override
	public void update(MemberRemovalRecordEntity memberRemovalRecord) {
		memberRemovalRecordDao.update(memberRemovalRecord);
	}

	@Override
	public void delete(Integer id) {
		memberRemovalRecordDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		memberRemovalRecordDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return memberRemovalRecordDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return memberRemovalRecordDao.logicDelBatch(ids);
	}

	@Override
	public List<MemberRemovalRecordEntity> queryListBySourceMember(Integer sourceMember) {
		return memberRemovalRecordDao.queryListBySourceMember(sourceMember);
	}

}
