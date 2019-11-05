package io.renren.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.swing.ImageIcon;

import org.apache.commons.lang3.StringUtils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.kit.PicKit;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目工具
 * @author yujia
 *
 */
@Slf4j
public class ProjectUtils {

	public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	
	
	public static boolean checkRowTitleObj(List<Object> rowTitle) {
		// [姓名, 手机号, 公司, 职位, 备注]
		try {
			String name = rowTitle.get(0).toString();
			if (!StringUtils.equals("姓名", name)) {
				return false;
			}
			String mobile = rowTitle.get(1).toString();
			if (!StringUtils.equals("手机", mobile)) {
				return false;
			}
			String company = rowTitle.get(2).toString();
			if (!StringUtils.equals("公司", company)) {
				return false;
			}
			String position = rowTitle.get(3).toString();
			if (!StringUtils.equals("职位", position)) {
				return false;
			}
			String remark = rowTitle.get(4).toString();
			if (!StringUtils.equals("备注", remark)) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean checkRowTitleStr(List<String> rowTitle) {
		// [姓名, 手机号, 公司, 职位, 备注]
		try {
			String name = rowTitle.get(0);
			if (!StringUtils.equals("姓名", name)) {
				return false;
			}
			String mobile = rowTitle.get(1);
			if (!StringUtils.equals("手机", mobile)) {
				return false;
			}
			String company = rowTitle.get(2);
			if (!StringUtils.equals("公司", company)) {
				return false;
			}
			String position = rowTitle.get(3);
			if (!StringUtils.equals("职位", position)) {
				return false;
			}
			String remark = rowTitle.get(4);
			if (!StringUtils.equals("备注", remark)) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String blankListToDefault(List<Object> data, int index, String defaultStr) {
		try {
			String dataStr = data.get(index).toString();
			if (StringUtils.isBlank(dataStr)) {
				return defaultStr;
			}
			return dataStr;
		} catch (Exception e) {
			return defaultStr;
		}
	}


	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		image = new ImageIcon(image).getImage();
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			int transparency = Transparency.OPAQUE;
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
		}

		if (bimage == null) {
			int type = BufferedImage.TYPE_INT_RGB;
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}
		Graphics g = bimage.createGraphics();

		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	public static String[] splitStr(String str, int splitLen) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		int count = str.length() / splitLen + (str.length() % splitLen == 0 ? 0 : 1);
		String[] strs = new String[count];
		for (int i = 0; i < count; i++) {
			if (str.length() <= splitLen) {
				strs[i] = str;
			} else {
				strs[i] = str.substring(0, splitLen);
				str = str.substring(splitLen);
			}
		}
		return strs;
	}

	public static String substringUrlRelative(String full, String key) {
		if (StringUtils.isBlank(full) || StringUtils.isBlank(key)) {
			return null;
		}
		return full.substring(full.indexOf(key), full.length());
	}

	public static File downloadFile(String fileUrl, String localFilePath) {
		if (StringUtils.isBlank(fileUrl) || StringUtils.isBlank(localFilePath)) {
			return null;
		}
		File localFile = FileUtil.file(localFilePath);
		long size = HttpUtil.downloadFile(fileUrl, localFile);
		log.info("Download size: " + size);
		return localFile;
	}

	/**
	 * 生成订单编号
	 * 
	 * @param prefix
	 *            N:新购订单 U:升级订单 R:续费
	 * @return
	 */
	public static String genOrderNum(String prefix) {
		StringBuffer sb = new StringBuffer();
		sb.append(prefix);
		String datestr = SIMPLE_DATE_FORMAT.format(new Date());
		String randomNumbers = RandomUtil.randomNumbers(6);
		sb.append(datestr).append(randomNumbers);
		return sb.toString();
	}

	/**
	 * 接口描述：MultipartFile 转换为 File
	 */
	public static void inputStreamToFile(InputStream ins, File file) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != os) {
					os.close();
				}
				if (null != ins) {
					ins.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String uploadCosFile(COSClient cosClient, File file) {

		try {
			if (file == null) {
				return null;
			}

			String extName = FileTypeUtil.getType(file);
			// 转换后会生成临时文件
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			// 文件路径
			String path = DateUtils.format(new Date(), "yyyyMM") + "/" + uuid;
			String key = path + "." + extName;
			PutObjectRequest putObjectRequest = new PutObjectRequest(SystemConstant.BUCKET_NAME_IMAGE, key, file);
			// 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
			putObjectRequest.setStorageClass(StorageClass.Standard);
			PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
			// putobjectResult会返回文件的etag
			String etag = putObjectResult.getETag();
			log.info("etag ===> {}", etag);
			if (StringUtils.isNotBlank(etag)) {
				log.info("COS 上传文件成功 ===> {}", key);
				return key;
			}

		} catch (Exception e) {
			log.error("COS上传文件异常 === ", e);
		}
		return null;
	}

	/**
	 * 获取IP地址
	 *
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip))
			return ip;
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			int index = ip.indexOf(',');
			if (index != -1)
				return ip.substring(0, index);
			else
				return ip;
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("WL-Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("HTTP_CLIENT_IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getRemoteAddr();
		return ip;
	}

}
