package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.ClienteleEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.ClienteleEntity;
import io.renren.cms.service.ClienteleService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

/**
 * 客户
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
@RestController
@RequestMapping("clientele")
public class ClienteleController {
	@Autowired
	private ClienteleService clienteleService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("clientele:list")
	public R list(@RequestParam Map<String, Object> params) {
		Query query = new Query(params);

		List<ClienteleEntityVo> clienteleList = clienteleService.queryListVo(query);
		int total = clienteleService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(clienteleList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("clientele:info")
	public R info(@PathVariable("id") Integer id) {
		ClienteleEntity clientele = clienteleService.queryObject(id);

		return R.ok().put("clientele", clientele);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("clientele:save")
	public R save(@RequestBody ClienteleEntity clientele) {
		clienteleService.save(clientele);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("clientele:update")
	public R update(@RequestBody ClienteleEntity clientele) {
		clienteleService.update(clientele);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("clientele:delete")
	public R delete(@RequestBody Integer[] ids) {
		clienteleService.deleteBatch(ids);

		return R.ok();
	}

	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("clientele:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		clienteleService.logicDel(id);
		return R.ok();
	}


	/**
	 * cms后台管理系统查询客户信息
	 */
	@RequestMapping("/clienteleInfo/{id}")
	//@RequiresPermissions("clientele:info")
	public R clienteleInfo(@PathVariable("id") Integer id) {
		ClienteleEntityVo clientele = clienteleService.queryObjectVo(id);

		return R.ok().put("clientele", clientele);
	}


	/**
	 * Excel导入列表
	 */
	@RequestMapping("/importList")
	//@RequiresPermissions("clientele:list")
	public R importList(@RequestParam Map<String, Object> params) {
		params.put("source","imports");
		Query query = new Query(params);
		List<ClienteleEntityVo> clienteleList = clienteleService.queryListVo(query);
		int total = clienteleService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(clienteleList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}
}
