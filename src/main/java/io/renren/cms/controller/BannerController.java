package io.renren.cms.controller;

import io.renren.api.constant.SystemConstant;
import io.renren.cms.entity.BannerEntity;
import io.renren.cms.service.BannerService;
import io.renren.properties.YykjProperties;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * banner
 *
 * @author moran
 * @date 2019-11-8
 */
@RestController
@RequestMapping("banner")
public class BannerController {
	@Autowired
	private BannerService bannerService;
	@Autowired
	private YykjProperties yykjProperties;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		params.put("bannertype", SystemConstant.BANNER_ADVS);
		Query query = new Query(params);
		List<BannerEntity> bannerList = bannerService.queryList(query);
/*
		bannerList.forEach(b -> {
			b.setImagePath(yykjProperties.getVisitprefix().concat(b.getImagePath()));
		});
		*/
		int total = bannerService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(bannerList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 列表
	 */
	@RequestMapping("/advsList")
	public R advsList(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		params.put("type", SystemConstant.BANNER_ADVS);
		Query query = new Query(params);
		List<BannerEntity> bannerList = bannerService.queryList(query);
		int total = bannerService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(bannerList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Integer id) {
		BannerEntity banner = bannerService.queryObject(id);

		return R.ok().put("banner", banner);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody BannerEntity banner) {
		/*if(!StringUtils.equals(banner.getType(), "advs")){
			if (StringUtils.isNotBlank(banner.getImagePath())) {
				banner.setImagePath(
						banner.getImagePath().substring(banner.getImagePath().indexOf("ajaxupload"), banner.getImagePath().length()));
			}
		}*/
		bannerService.save(banner);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody BannerEntity banner) {
		/*if(!StringUtils.equals(banner.getType(), "advs")){
			if (StringUtils.isNotBlank(banner.getImagePath())) {
				banner.setImagePath(
						banner.getImagePath().substring(banner.getImagePath().indexOf("ajaxupload"), banner.getImagePath().length()));
			}
		}*/
		bannerService.update(banner);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Integer[] ids) {
		bannerService.deleteBatch(ids);

		return R.ok();
	}

}
