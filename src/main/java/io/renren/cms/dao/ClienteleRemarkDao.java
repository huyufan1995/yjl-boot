package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.ClienteleRemarkEntityVo;
import io.renren.cms.entity.ClienteleRemarkEntity;
import io.renren.dao.BaseDao;

/**
 * 客户备注
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
public interface ClienteleRemarkDao extends BaseDao<ClienteleRemarkEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	List<ClienteleRemarkEntity> queryListByClienteleId(Integer clienteleId);

    List<ClienteleRemarkEntityVo> queryListVo(Map<String, Object> map);
}
