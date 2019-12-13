package io.renren.cms.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import io.renren.cms.entity.ApplyEntity;
import io.renren.cms.entity.ApplyReviewEntity;
import io.renren.cms.vo.ApplyRecordEntityVO;
import io.renren.enums.AuditStatusEnum;
import io.renren.utils.easypoi.ApplyRecordPOI;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.ApplyRecordEntity;
import io.renren.cms.service.ApplyRecordService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;


/**
 * 活动报名记录
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-12 09:58:16
 */
@RestController
@RequestMapping("applyrecord")
public class ApplyRecordController {
	@Autowired
	private ApplyRecordService applyRecordService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("applyrecord:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<ApplyRecordEntityVO> applyRecordList = applyRecordService.queryListVo(query);
		int total = applyRecordService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(applyRecordList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("applyrecord:info")
	public R info(@PathVariable("id") Integer id){
		ApplyRecordEntity applyRecord = applyRecordService.queryObject(id);
		
		return R.ok().put("applyRecord", applyRecord);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("applyrecord:save")
	public R save(@RequestBody ApplyRecordEntity applyRecord){
		applyRecordService.save(applyRecord);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("applyrecord:update")
	public R update(@RequestBody ApplyRecordEntity applyRecord){
		applyRecordService.update(applyRecord);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("applyrecord:delete")
	public R delete(@RequestBody Integer[] ids){
		applyRecordService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("applyrecord:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		applyRecordService.logicDel(id);
		return R.ok();
	}

	/**
	 * 导出Excel
	 */
	@RequestMapping("/applyRecordPoi")
	//@RequiresPermissions("applyrecord:logicDel")
	public void applyRecordPoi(HttpServletResponse response,@RequestParam Map<String, Object> params)throws Exception {
		List<ApplyRecordEntityVO> applyRecordList = applyRecordService.queryListVo(params);
		List<ApplyRecordPOI> list = new ArrayList<>();
		applyRecordList.forEach(v -> {
			ApplyRecordPOI poi = new ApplyRecordPOI();
			poi.setApplyTitle(v.getApplyTitle());
			poi.setCtime(v.getCtime());
			poi.setNickName(v.getNickName());
			list.add(poi);
		});
		Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("活动报名", "数据"),
				ApplyRecordPOI.class, list);
		response.setHeader("content-Type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename=" + System.currentTimeMillis() + ".xls");
		response.setCharacterEncoding("UTF-8");
		try {
			workbook.write(response.getOutputStream());
			workbook.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
