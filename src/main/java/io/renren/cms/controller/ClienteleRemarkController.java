package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.ClienteleRemarkEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.ClienteleRemarkEntity;
import io.renren.cms.service.ClienteleRemarkService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

/**
 * 客户备注
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
@RestController
@RequestMapping("clienteleremark")
public class ClienteleRemarkController {
	@Autowired
	private ClienteleRemarkService clienteleRemarkService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("clienteleremark:list")
	public R list(@RequestParam Map<String, Object> params) {
		Query query = new Query(params);

		List<ClienteleRemarkEntityVo> clienteleRemarkList = clienteleRemarkService.queryListVo(query);
		int total = clienteleRemarkService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(clienteleRemarkList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("clienteleremark:info")
	public R info(@PathVariable("id") Integer id) {
		ClienteleRemarkEntity clienteleRemark = clienteleRemarkService.queryObject(id);

		return R.ok().put("clienteleRemark", clienteleRemark);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("clienteleremark:save")
	public R save(@RequestBody ClienteleRemarkEntity clienteleRemark) {
		clienteleRemarkService.save(clienteleRemark);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("clienteleremark:update")
	public R update(@RequestBody ClienteleRemarkEntity clienteleRemark) {
		clienteleRemarkService.update(clienteleRemark);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("clienteleremark:delete")
	public R delete(@RequestBody Integer[] ids) {
		clienteleRemarkService.deleteBatch(ids);

		return R.ok();
	}

	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("clienteleremark:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		clienteleRemarkService.logicDel(id);
		return R.ok();
	}

}
