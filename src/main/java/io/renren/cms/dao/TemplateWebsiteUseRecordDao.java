package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.TemplateWebsiteUseRecordEntityVo;
import org.apache.ibatis.annotations.Param;

import io.renren.cms.entity.TemplateWebsiteUseRecordEntity;
import io.renren.dao.BaseDao;

/**
 * 官网模板使用记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-02 14:16:12
 */
public interface TemplateWebsiteUseRecordDao extends BaseDao<TemplateWebsiteUseRecordEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	TemplateWebsiteUseRecordEntity queryObjectByTemplateWebsiteIdAndMemberId(
			@Param("templateWebsiteId") Integer templateWebsiteId, @Param("memberId") Integer memberId);

	List<TemplateWebsiteUseRecordEntity> queryListByTemplateWebsiteId(Integer templateWebsiteId);

    List<TemplateWebsiteUseRecordEntityVo> queryListVo(Map<String, Object> map);
}
