package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.ClienteleAllotRecordEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.ClienteleAllotRecordEntity;
import io.renren.cms.service.ClienteleAllotRecordService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

/**
 * 客户分片记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-29 17:16:27
 */
@RestController
@RequestMapping("clienteleallotrecord")
public class ClienteleAllotRecordController {
	@Autowired
	private ClienteleAllotRecordService clienteleAllotRecordService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("clienteleallotrecord:list")
	public R list(@RequestParam Map<String, Object> params) {
		Query query = new Query(params);

		List<ClienteleAllotRecordEntityVo> clienteleAllotRecordList = clienteleAllotRecordService.queryListVo(query);
		int total = clienteleAllotRecordService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(clienteleAllotRecordList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("clienteleallotrecord:info")
	public R info(@PathVariable("id") Integer id) {
		ClienteleAllotRecordEntity clienteleAllotRecord = clienteleAllotRecordService.queryObject(id);

		return R.ok().put("clienteleAllotRecord", clienteleAllotRecord);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("clienteleallotrecord:save")
	public R save(@RequestBody ClienteleAllotRecordEntity clienteleAllotRecord) {
		clienteleAllotRecordService.save(clienteleAllotRecord);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("clienteleallotrecord:update")
	public R update(@RequestBody ClienteleAllotRecordEntity clienteleAllotRecord) {
		clienteleAllotRecordService.update(clienteleAllotRecord);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("clienteleallotrecord:delete")
	public R delete(@RequestBody Integer[] ids) {
		clienteleAllotRecordService.deleteBatch(ids);

		return R.ok();
	}

	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("clienteleallotrecord:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		clienteleAllotRecordService.logicDel(id);
		return R.ok();
	}

}
