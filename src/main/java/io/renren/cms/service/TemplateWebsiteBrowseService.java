package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.TemplateWebsiteBrowseEntityVo;
import io.renren.cms.entity.TemplateWebsiteBrowseEntity;
import io.renren.utils.Query;

/**
 * 官网模板浏览记录接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-04 10:30:08
 */
public interface TemplateWebsiteBrowseService {

	TemplateWebsiteBrowseEntity queryObject(Integer id);

	List<TemplateWebsiteBrowseEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(TemplateWebsiteBrowseEntity templateWebsiteBrowse);

	void update(TemplateWebsiteBrowseEntity templateWebsiteBrowse);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	TemplateWebsiteBrowseEntity queryObjectByOwnerOpenidAndBrowseOpenid(String ownerOpenid, String browseOpenid);

	List<TemplateWebsiteBrowseEntity> queryListByOwnerOpenid(String ownerOpenid);

	List<TemplateWebsiteBrowseEntityVo> queryListVo(Map<String, Object> map);
}
