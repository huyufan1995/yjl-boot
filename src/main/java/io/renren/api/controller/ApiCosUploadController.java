package io.renren.api.controller;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;

import cn.binarywang.wx.miniapp.api.WxMaService;
import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.ApiResult;
import io.renren.config.WxMaConfiguration;
import io.renren.properties.YykjProperties;
import io.renren.utils.DateUtils;
import io.renren.utils.ProjectUtils;
import io.renren.utils.annotation.IgnoreAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api("云文件上传")
@RestController
@RequestMapping("/api/cos/upload")
public class ApiCosUploadController {

	@Value("${tencent.cos.imagePrefixUrl}")
	private String imagePrefixUrl;
	@Autowired
	private COSClient cosClient;
	@Autowired
	private YykjProperties yykjProperties;

	@IgnoreAuth
	@ApiOperation(value = "图片上传")
	@PostMapping(value = "/cloud/image", headers = "content-type=multipart/form-data")
	public ApiResult cloudImage(@ApiParam(value = "上传的文件", required = true) @RequestParam("file") MultipartFile multipartFile) {
		
		HashMap<String, Object> result = Maps.newHashMap();
		try {

			if (multipartFile == null) {
				return ApiResult.error(500, "文件不能为空");
			}

			if (multipartFile.getSize() < 0 || multipartFile.getSize() > SystemConstant.MAX_UPLOAD_FILE_SIZE) {
				// 上传图片不能为空或超过3MB
				return ApiResult.error(500, "文件不能超过3MB");
			}

			// 获取上传的文件名
			String fileName = multipartFile.getOriginalFilename();
			// 获取文件的后缀名
			String extName = fileName.substring(fileName.lastIndexOf("."));

			if (SystemConstant.IMAGE_REG.indexOf(extName.toLowerCase()) == -1) {
				return ApiResult.error(500, "必须是图片类型文件");
			}

			InputStream inputStream = multipartFile.getInputStream();
			File tempFile = new File(multipartFile.getOriginalFilename());
			// 转换后会生成临时文件
			ProjectUtils.inputStreamToFile(inputStream, tempFile);
			
			final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
			if(!wxMaService.getSecCheckService().checkImage(tempFile)) {
				return ApiResult.error(500, "图片含有违法违规内容");
			}
			
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			// 文件路径
			String path = DateUtils.format(new Date(), "yyyyMM") + "/" + uuid;
			String key = path + extName;
			PutObjectRequest putObjectRequest = new PutObjectRequest(SystemConstant.BUCKET_NAME_IMAGE, key, tempFile);
			// 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
			putObjectRequest.setStorageClass(StorageClass.Standard);
			PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
			// putobjectResult会返回文件的etag
			String etag = putObjectResult.getETag();
			log.info("etag ===> {}", etag);
			// 删除临时文件
			File delFile = new File(tempFile.toURI());
			delFile.delete();
			if (StringUtils.isNotBlank(etag)) {
				result.put("key", key);
				result.put("url", imagePrefixUrl.concat(key));
				log.info("COS 上传文件成功 ===> {}", result);
				return ApiResult.ok(result);
			} else {
				return ApiResult.error(500, "上传失败");
			}

		} catch (Exception e) {
			log.error("COS上传文件异常 === ", e);
			return ApiResult.error(500, "上传失败");
		}
	}

}
