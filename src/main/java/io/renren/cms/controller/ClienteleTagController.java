package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.ClienteleTagEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.ClienteleTagEntity;
import io.renren.cms.service.ClienteleTagService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

/**
 * 客户标签
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:48
 */
@RestController
@RequestMapping("clienteletag")
public class ClienteleTagController {
	@Autowired
	private ClienteleTagService clienteleTagService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("clienteletag:list")
	public R list(@RequestParam Map<String, Object> params) {
		Query query = new Query(params);

		List<ClienteleTagEntityVo> clienteleTagList = clienteleTagService.queryListVo(query);
		int total = clienteleTagService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(clienteleTagList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("clienteletag:info")
	public R info(@PathVariable("id") Integer id) {
		ClienteleTagEntity clienteleTag = clienteleTagService.queryObject(id);

		return R.ok().put("clienteleTag", clienteleTag);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("clienteletag:save")
	public R save(@RequestBody ClienteleTagEntity clienteleTag) {
		clienteleTagService.save(clienteleTag);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("clienteletag:update")
	public R update(@RequestBody ClienteleTagEntity clienteleTag) {
		clienteleTagService.update(clienteleTag);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("clienteletag:delete")
	public R delete(@RequestBody Integer[] ids) {
		clienteleTagService.deleteBatch(ids);

		return R.ok();
	}

	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("clienteletag:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		clienteleTagService.logicDel(id);
		return R.ok();
	}

}
