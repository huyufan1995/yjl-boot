package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.ClienteleAllotRecordEntityVo;
import io.renren.api.vo.ClienteleAllotRecordVo;
import io.renren.cms.entity.ClienteleAllotRecordEntity;
import io.renren.dao.BaseDao;

/**
 * 客户分片记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-29 17:16:27
 */
public interface ClienteleAllotRecordDao extends BaseDao<ClienteleAllotRecordEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	void insertBatch(List<ClienteleAllotRecordEntity> record);

	List<ClienteleAllotRecordVo> queryListByClienteleId(Integer clienteleId);

	List<ClienteleAllotRecordEntityVo> queryListVo(Map<String, Object> map);
}
