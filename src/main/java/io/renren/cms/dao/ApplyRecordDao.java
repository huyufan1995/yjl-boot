package io.renren.cms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.api.dto.ApplyRecordEntiyDto;
import io.renren.api.dto.VerifyMemberInfoDto;
import io.renren.cms.entity.ApplyRecordEntity;
import io.renren.cms.vo.ApplyRecordEntityVO;
import io.renren.dao.BaseDao;
import org.apache.ibatis.annotations.Param;

/**
 * 活动报名记录
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-12 09:58:16
 */
public interface ApplyRecordDao extends BaseDao<ApplyRecordEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

    List<ApplyRecordEntiyDto> queryPortrait(HashMap<String, Object> query);

	Boolean deleteByOpenIdAndApplyId(HashMap<String, Object> params);

    List<VerifyMemberInfoDto> queryVerifyMember(@Param(value = "code")String code);

    List<ApplyRecordEntityVO> queryListVo(Map<String, Object> map);
}
