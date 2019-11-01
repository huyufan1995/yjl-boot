package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.dto.SessionMember;
import io.renren.api.vo.TemplateWebsiteLayoutEntityVo;
import io.renren.cms.entity.TemplateWebsiteLayoutEntity;

/**
 * 官网模板布局接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-03 17:50:05
 */
public interface TemplateWebsiteLayoutService {

	TemplateWebsiteLayoutEntity queryObject(Integer id);

	List<TemplateWebsiteLayoutEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(TemplateWebsiteLayoutEntity templateWebsiteLayout);

	void update(TemplateWebsiteLayoutEntity templateWebsiteLayout);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	TemplateWebsiteLayoutEntity queryObjectByMemberId(Integer memberId);

	void saveConf(Integer templateId, String layout, String metadata, SessionMember sessionMember);

	TemplateWebsiteLayoutEntity queryObjectByOpenid(String openid);

	List<TemplateWebsiteLayoutEntityVo> queryListVo(Map<String, Object> map);

	int deleteByMemberId(Integer memberId);
}
