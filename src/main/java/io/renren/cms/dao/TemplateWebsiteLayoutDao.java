package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.TemplateWebsiteLayoutEntityVo;
import io.renren.cms.entity.TemplateWebsiteLayoutEntity;
import io.renren.dao.BaseDao;

/**
 * 官网模板布局
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-03 17:50:05
 */
public interface TemplateWebsiteLayoutDao extends BaseDao<TemplateWebsiteLayoutEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	TemplateWebsiteLayoutEntity queryObjectByMemberId(Integer memberId);

	TemplateWebsiteLayoutEntity queryObjectByOpenid(String openid);

	List<TemplateWebsiteLayoutEntityVo> queryListVo(Map<String, Object> map);

	int deleteByMemberId(Integer memberId);
}
