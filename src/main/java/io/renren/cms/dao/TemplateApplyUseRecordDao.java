package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.TemplateApplyUseRecordEntity;
import io.renren.dao.BaseDao;

/**
 * 报名模板使用记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-18 17:54:54
 */
public interface TemplateApplyUseRecordDao extends BaseDao<TemplateApplyUseRecordEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	List<TemplateApplyUseRecordEntity> queryListByTemplateApplyId(Integer templateApplyId);
}
