package io.renren.cms.controller;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import io.renren.utils.*;
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

import io.renren.api.constant.SystemConstant;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/cos/upload")
public class CosUploadController {

	@Value("${tencent.cos.imagePrefixUrl}")
	private String imagePrefixUrl;
	@Autowired
	private COSClient cosClient;

	@Autowired
	private RedisUtils redisUtils;

	@PostMapping("/cloud/image")
	public R cloudImage(@RequestParam("file") MultipartFile multipartFile) {
		HashMap<String, Object> result = Maps.newHashMap();
		try {

			if (multipartFile == null) {
				return R.error(500, "上传文件不能为空");
			}

			if (multipartFile.getSize() < 0 || multipartFile.getSize() > SystemConstant.MAX_UPLOAD_FILE_SIZE) {
				// 上传图片不能为空或超过3MB
				return R.error(500, "文件不能超过3MB");
			}

			// 获取上传的文件名
			String fileName = multipartFile.getOriginalFilename();
			// 获取文件的后缀名
			String extName = fileName.substring(fileName.lastIndexOf("."));

			if (SystemConstant.IMAGE_REG.indexOf(extName.toLowerCase()) == -1) {
				return R.error(500, "只允许上传图片");
			}

			InputStream inputStream = multipartFile.getInputStream();
			File tempFile = new File(multipartFile.getOriginalFilename());
			// 转换后会生成临时文件
			ProjectUtils.inputStreamToFile(inputStream, tempFile);
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
				return R.ok().put("data", result);
			} else {
				return R.error(500, "上传文件异常");
			}

		} catch (Exception e) {
			log.error("COS上传文件异常 === ", e);
			return R.error(500, "上传文件异常");
		}
	}

	@PostMapping("/cloud/templateImage")
	public R templateImage(@RequestParam("file") MultipartFile multipartFile) {
		HashMap<String, Object> result = Maps.newHashMap();
		try {

			if (multipartFile == null) {
				return R.error(500, "上传文件不能为空");
			}

			if (multipartFile.getSize() < 0 || multipartFile.getSize() > SystemConstant.MAX_UPLOAD_FILE_SIZE) {
				// 上传图片不能为空或超过3MB
				return R.error(500, "文件不能超过3MB");
			}

			// 获取上传的文件名
			String fileName = multipartFile.getOriginalFilename();
			// 获取文件的后缀名
			String extName = fileName.substring(fileName.lastIndexOf("."));

			if (SystemConstant.IMAGE_REG.indexOf(extName.toLowerCase()) == -1) {
				return R.error(500, "只允许上传图片");
			}

			InputStream inputStream = multipartFile.getInputStream();
			File tempFile = new File(multipartFile.getOriginalFilename());
			// 转换后会生成临时文件
			ProjectUtils.inputStreamToFile(inputStream, tempFile);
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			// 文件路径
		/*	String catalogue = templateWebsiteService.queryByLayoutWithCtime().getCatalogue();
			String[] boxes = catalogue.split("box");
			int i =  Integer.parseInt(boxes[1])+1;
			String boxUrl ="/app/box"+i+"";*/
			String path = "app/" + uuid;
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
				return R.ok().put("data", result);
			} else {
				return R.error(500, "上传文件异常");
			}

		} catch (Exception e) {
			log.error("COS上传文件异常 === ", e);
			return R.error(500, "上传文件异常");
		}
	}

	@PostMapping("/cloud/applyImage")
	public R applyImage(@RequestParam("file") MultipartFile multipartFile) {
		HashMap<String, Object> result = Maps.newHashMap();
		try {

			if (multipartFile == null) {
				return R.error(500, "上传文件不能为空");
			}

			if (multipartFile.getSize() < 0 || multipartFile.getSize() > SystemConstant.MAX_UPLOAD_FILE_SIZE) {
				// 上传图片不能为空或超过3MB
				return R.error(500, "文件不能超过3MB");
			}

			// 获取上传的文件名
			String fileName = multipartFile.getOriginalFilename();
			// 获取文件的后缀名
			String extName = fileName.substring(fileName.lastIndexOf("."));

			if (SystemConstant.IMAGE_REG.indexOf(extName.toLowerCase()) == -1) {
				return R.error(500, "只允许上传图片");
			}

			InputStream inputStream = multipartFile.getInputStream();
			File tempFile = new File(multipartFile.getOriginalFilename());
			// 转换后会生成临时文件
			ProjectUtils.inputStreamToFile(inputStream, tempFile);
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			// 文件路径
		/*	String catalogue = templateWebsiteService.queryByLayoutWithCtime().getCatalogue();
			String[] boxes = catalogue.split("box");
			int i =  Integer.parseInt(boxes[1])+1;
			String boxUrl ="/app/box"+i+"";*/
			String path = "app/pageBm/teming/" + uuid;
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
				return R.ok().put("data", result);
			} else {
				return R.error(500, "上传文件异常");
			}

		} catch (Exception e) {
			log.error("COS上传文件异常 === ", e);
			return R.error(500, "上传文件异常");
		}
	}

	/**
	 * 批量上传图片
	 * @param multipartFile
	 * @return
	 */
	@PostMapping("/cloud/templateImages")
	public R templateImages(@RequestParam("file") MultipartFile multipartFile,@RequestParam("data") String data) {
		try {

			if (multipartFile == null) {
				return R.error(500, "上传文件不能为空");
			}

			if (multipartFile.getSize() < 0 || multipartFile.getSize() > SystemConstant.MAX_UPLOAD_FILE_SIZE) {
				// 上传图片不能为空或超过3MB
				return R.error(500, "文件不能超过3MB");
			}

			// 获取上传的文件名
			String fileName = multipartFile.getOriginalFilename();

			//通过文件名称生成存储redis  key的名称
			String keyName = TemplateWebsiteConfigUtil.getValue(fileName);
			// 获取文件的后缀名
			String extName = fileName.substring(fileName.lastIndexOf("."));

			if (SystemConstant.IMAGE_REG.indexOf(extName.toLowerCase()) == -1) {
				return R.error(500, "只允许上传图片");
			}

			InputStream inputStream = multipartFile.getInputStream();
			File tempFile = new File(multipartFile.getOriginalFilename());
			// 转换后会生成临时文件
			ProjectUtils.inputStreamToFile(inputStream, tempFile);
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			// 文件路径
		/*	String catalogue = templateWebsiteService.queryByLayoutWithCtime().getCatalogue();
			String[] boxes = catalogue.split("box");
			int i =  Integer.parseInt(boxes[1])+1;
			String boxUrl ="/app/box"+i+"";*/
			String path = "app/boxgw/"+data+"/" + uuid;
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
				redisUtils.set(keyName,key);
				log.info("COS 上传文件成功 ===> {}", key);
				return R.ok();
			} else {
				return R.error(500, "上传文件异常");
			}

		} catch (Exception e) {
			log.error("COS上传文件异常 === ", e);
			return R.error(500, "上传文件异常");
		}
	}

	@PostMapping("/cloud/functionsIconImage")
	public R functionsIconImage(@RequestParam("file") MultipartFile multipartFile) {
		HashMap<String, Object> result = Maps.newHashMap();
		try {

			if (multipartFile == null) {
				return R.error(500, "上传文件不能为空");
			}

			if (multipartFile.getSize() < 0 || multipartFile.getSize() > SystemConstant.MAX_UPLOAD_FILE_SIZE) {
				// 上传图片不能为空或超过3MB
				return R.error(500, "文件不能超过3MB");
			}

			// 获取上传的文件名
			String fileName = multipartFile.getOriginalFilename();
			// 获取文件的后缀名
			String extName = fileName.substring(fileName.lastIndexOf("."));

			if (SystemConstant.IMAGE_REG.indexOf(extName.toLowerCase()) == -1) {
				return R.error(500, "只允许上传图片");
			}

			InputStream inputStream = multipartFile.getInputStream();
			File tempFile = new File(multipartFile.getOriginalFilename());
			// 转换后会生成临时文件
			ProjectUtils.inputStreamToFile(inputStream, tempFile);
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			// 文件路径
			String path = "app/vip/" + uuid;
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
				return R.ok().put("data", result);
			} else {
				return R.error(500, "上传文件异常");
			}

		} catch (Exception e) {
			log.error("COS上传文件异常 === ", e);
			return R.error(500, "上传文件异常");
		}
	}
}
