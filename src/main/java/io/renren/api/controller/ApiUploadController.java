package io.renren.api.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.hutool.json.JSONObject;
import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.ApiResult;
import io.renren.properties.YykjProperties;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/upload")
@Api("上传")
public class ApiUploadController {

	@Autowired
	private YykjProperties yykjProperties;
	
	private DateFormat df = new SimpleDateFormat("yyyyMM");

	@IgnoreAuth
	@ApiOperation(value = "上传",notes = "图片上传")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", dataType="file", name = "file", value = "图片文件", required = true),
        @ApiImplicitParam(paramType = "query", dataType="string", name = "openid", value = "openid", required = true)
    })
	@PostMapping("/image")
	public ApiResult uploadImage(MultipartFile file, String openid) throws IOException {
		String relativePath;
		String newFileName;
		JSONObject result = new JSONObject();
		try {
			Assert.isBlankApi(openid, "openid不能为空");

			if (file == null) {
				return ApiResult.error(401, "上传文件为 空");
			}

			if (file.getBytes().length > 1000 * 1024) {
				return ApiResult.error(402, "图片只允许1MBK大小");
			}

			// 获取上传的文件名
			String fileName = file.getOriginalFilename();
			// 获取文件的后缀名
			String extName = fileName.substring(fileName.lastIndexOf("."));
			
			if (SystemConstant.IMAGE_REG.indexOf(extName.toLowerCase()) == -1) {
				return ApiResult.error(403, "请上传图片文件");
			}
			
			String datePath = df.format(new Date());
			// 文件保存的相对位置
			relativePath = "upload/" + datePath + "/";
			// 生成的文件名
			newFileName = generateFileName(openid, extName);
			// 文件保存的完整目录
			String absolutePath = yykjProperties.getUploaddir() + relativePath;
			File temp = new File(absolutePath);
			if (!temp.exists()) {
				temp.mkdirs(); // 如果目录不存在则创建
			}
			BufferedOutputStream stream = null;
			stream = new BufferedOutputStream(new FileOutputStream(absolutePath + newFileName));
			if (stream != null) {
				stream.write(file.getBytes());
				stream.close();
			}
			String resultFilePath = yykjProperties.getVisitprefix() + relativePath + newFileName;
			result.put("src", resultFilePath);
			result.put("path", relativePath + newFileName);
			return ApiResult.ok(result);
		} catch (Exception e) {
			return ApiResult.error(500, "上传图片错误");
		}

	}
	

	/**
	 * 生成规范的文件名
	 */
	private String generateFileName(String openId, String extName) {
		return openId + "_" + System.currentTimeMillis() + extName;
	}

}
