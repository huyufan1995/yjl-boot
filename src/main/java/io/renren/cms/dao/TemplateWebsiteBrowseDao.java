package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.TemplateWebsiteBrowseEntityVo;
import org.apache.ibatis.annotations.Param;

import io.renren.cms.entity.TemplateWebsiteBrowseEntity;
import io.renren.dao.BaseDao;

/**
 * 官网模板浏览记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-04 10:30:08
 */
public interface TemplateWebsiteBrowseDao extends BaseDao<TemplateWebsiteBrowseEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	TemplateWebsiteBrowseEntity queryObjectByOwnerOpenidAndBrowseOpenid(@Param("ownerOpenid") String ownerOpenid,
			@Param("browseOpenid") String browseOpenid);

	List<TemplateWebsiteBrowseEntity> queryListByOwnerOpenid(String ownerOpenid);

    List<TemplateWebsiteBrowseEntityVo> queryListVo(Map<String, Object> map);
}
