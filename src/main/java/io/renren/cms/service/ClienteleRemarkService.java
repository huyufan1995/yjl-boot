package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.ClienteleRemarkEntityVo;
import io.renren.cms.entity.ClienteleRemarkEntity;
import io.renren.utils.Query;

/**
 * 客户备注接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
public interface ClienteleRemarkService {

	ClienteleRemarkEntity queryObject(Integer id);

	List<ClienteleRemarkEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(ClienteleRemarkEntity clienteleRemark);

	void update(ClienteleRemarkEntity clienteleRemark);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	List<ClienteleRemarkEntity> queryListByClienteleId(Integer clienteleId);

	List<ClienteleRemarkEntityVo> queryListVo(Map<String, Object> map);
}
