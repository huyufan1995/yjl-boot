package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.dto.InformationsEntityDto;
import io.renren.api.dto.InformationsEntityInfoDto;
import io.renren.cms.entity.InformationsEntity;
import io.renren.dao.BaseDao;
import org.apache.ibatis.annotations.Param;

/**
 * 资讯表
 * 
 * @author moran
 * @date 2019-11-05 11:05:36
 */
public interface InformationDao extends BaseDao<InformationsEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

    List<InformationsEntityDto> queryListDto(Map<String, Object> map);

    InformationsEntityInfoDto queryObjectDto(@Param(value = "id") Integer id, @Param(value = "openid")String openid);

    List<InformationsEntityDto> queryListDtoByOpenIdWithCollect(Map<String, Object> map);
}
