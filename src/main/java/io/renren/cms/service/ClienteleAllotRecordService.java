package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.ClienteleAllotRecordEntityVo;
import io.renren.api.vo.ClienteleAllotRecordVo;
import io.renren.cms.entity.ClienteleAllotRecordEntity;
import io.renren.utils.Query;

/**
 * 客户分片记录接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-29 17:16:27
 */
public interface ClienteleAllotRecordService {

	ClienteleAllotRecordEntity queryObject(Integer id);

	List<ClienteleAllotRecordEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(ClienteleAllotRecordEntity clienteleAllotRecord);

	void update(ClienteleAllotRecordEntity clienteleAllotRecord);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	void insertBatch(List<ClienteleAllotRecordEntity> record);

	List<ClienteleAllotRecordVo> queryListByClienteleId(Integer clienteleId);

	List<ClienteleAllotRecordEntityVo> queryListVo(Map<String, Object> map);
}
