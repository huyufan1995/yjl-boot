package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.WxUserEntity;
import io.renren.cms.service.WxUserService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

/**
 * 微信用户
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
@RestController
@RequestMapping("wxuser")
public class WxUserController {
	@Autowired
	private WxUserService wxUserService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);

		List<WxUserEntity> wxUserList = wxUserService.queryList(query);
		int total = wxUserService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(wxUserList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Long id) {
		WxUserEntity wxUser = wxUserService.queryObject(id);

		return R.ok().put("wxUser", wxUser);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody WxUserEntity wxUser) {
		wxUserService.save(wxUser);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody WxUserEntity wxUser) {
		wxUserService.update(wxUser);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Long[] ids) {
		wxUserService.deleteBatch(ids);

		return R.ok();
	}

}
