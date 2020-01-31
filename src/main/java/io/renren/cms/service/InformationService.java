package io.renren.cms.service;

import io.renren.api.dto.InformationsEntityDto;
import io.renren.api.dto.InformationsEntityInfoDto;
import io.renren.cms.entity.InformationsEntity;
import io.renren.utils.Query;

import java.util.List;
import java.util.Map;

/**
 * 接口
 * 
 * @author huyufan
 * @date 2019-11-05 10:36:31
 */
public interface InformationService {
	
	InformationsEntity queryObject(Integer id);
	
	List<InformationsEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(InformationsEntity information);
	
	void update(InformationsEntity information);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

	int release(Integer id);

	int commit(Integer id);

	int revocation(Integer id);

	int rejectInformation(Integer id);

    List<InformationsEntityDto> queryListDto(Map<String, Object> map);

    InformationsEntityInfoDto queryObjectDto(Integer id, String openid);

    List<InformationsEntityDto> queryListDtoByOpenIdWithCollect(Map<String, Object> map);

    List<InformationsEntity> queryListVO(Map<String, Object> map);
}
