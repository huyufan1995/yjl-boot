package io.renren.cms.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import io.renren.properties.YykjProperties;

@Controller
public class UploadController {

	@Autowired
	private YykjProperties yykjProperties;
	
	public static final String IMAGE_REG = ".jpg|.jpeg|.png|.bmp|.gif";

	/**
	 * Simditor 图片上传
	 */
	@ResponseBody
	@RequestMapping("/upload/image")
	public Object upload(@RequestParam("upload_file") MultipartFile file) throws IOException {
		String relativePath;
		String newFileName;
		Map<String, Object> result = new HashMap<String, Object>();
		try {

			if (file == null) {
				result.put("success", false);
				result.put("msg", "上传文件为 空");
				return result;
			}
			
//			if (file.getBytes().length > 200 * 1024) {
//				result.put("success", false);
//				result.put("msg", "图片尺寸只允许 200K 大小");
//				return result;
//			}

			// 获取上传的文件名
			String fileName = file.getOriginalFilename();
			// 获取文件的后缀名
			String extName = fileName.substring(fileName.lastIndexOf("."));
			// 文件保存的相对位置
			relativePath = "simditor/";
			// 生成的文件名
			newFileName = UUID.randomUUID().toString() + extName;
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
			// 返回给编辑器的文件位置
			String resultFilePath = yykjProperties.getVisitprefix() + relativePath + newFileName;
			result.put("success", true);
			result.put("file_path", resultFilePath);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", e.getMessage());
			return result;
		}

	}
	
	@ResponseBody
	@RequestMapping("/ajaxupload/image")
	public Object ajaxupload(@RequestParam("file") MultipartFile file) throws IOException {
		String relativePath;
		String newFileName;
		Map<String, Object> result = new HashMap<String, Object>();
		try {

			if (file == null) {
				result.put("code", 401);
				result.put("msg", "上传文件为 空");
				return result;
			}
			
//			if (file.getBytes().length > 200 * 1024) {
//				result.put("code", 402);
//				result.put("msg", "图片尺寸只允许 200K 大小");
//				return result;
//			}

			// 获取上传的文件名
			String fileName = file.getOriginalFilename();
			// 获取文件的后缀名
			String extName = fileName.substring(fileName.lastIndexOf("."));
			// 文件保存的相对位置
//			relativePath = "ajaxupload" + File.separator;
			relativePath = "ajaxupload/";
			// 生成的文件名
			newFileName = UUID.randomUUID().toString() + extName;
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
			// 返回给编辑器的文件位置
			String resultFilePath = yykjProperties.getVisitprefix() + relativePath + newFileName;
			result.put("code", 200);
			result.put("src", resultFilePath);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 500);
			result.put("msg", e.getMessage());
			return result;
		}

	}
	
	@ResponseBody
	@RequestMapping("/ajax_upload_type/{type}")
	public Object ajax_upload_type(@RequestParam("files") MultipartFile files, @PathVariable String type) throws IOException {
		String relativePath;
		String newFileName;
		Map<String, Object> result = new HashMap<String, Object>();
		try {

			if (files == null) {
				result.put("code", 401);
				result.put("msg", "上传文件为 空");
				return result;
			}
			
//			if (file.getBytes().length > 200 * 1024) {
//				result.put("code", 402);
//				result.put("msg", "图片尺寸只允许 200K 大小");
//				return result;
//			}

			// 获取上传的文件名
			String fileName = files.getOriginalFilename();
			// 获取文件的后缀名
			String extName = fileName.substring(fileName.lastIndexOf("."));
			
			if("image".equals(type)){
				if(IMAGE_REG.indexOf(extName.toLowerCase()) == -1) {
					result.put("code", 402);
					result.put("msg", "只允许上传图片文件!");
					return result;
				}
			}
			
			// 文件保存的相对位置
//			relativePath = "ajaxupload" + File.separator;
			relativePath = "ajaxupload/";
			// 生成的文件名
			newFileName = UUID.randomUUID().toString() + extName;
			// 文件保存的完整目录
			String absolutePath = yykjProperties.getUploaddir() + relativePath;
			File temp = new File(absolutePath);
			if (!temp.exists()) {
				temp.mkdirs(); // 如果目录不存在则创建
			}
			BufferedOutputStream stream = null;
			stream = new BufferedOutputStream(new FileOutputStream(absolutePath + newFileName));
			if (stream != null) {
				stream.write(files.getBytes());
				stream.close();
			}
			// 返回给编辑器的文件位置
			String resultFilePath = yykjProperties.getVisitprefix() + relativePath + newFileName;
			result.put("code", 200);
			result.put("path", relativePath + newFileName);
			result.put("src", resultFilePath);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 500);
			result.put("msg", e.getMessage());
			return result;
		}

	}

	/**
	 * 生成规范的文件名
	 */
	/*private String generateFileName(Long userId, String extName) {
		return userId + "_" + System.currentTimeMillis() + extName;
	}*/

}
