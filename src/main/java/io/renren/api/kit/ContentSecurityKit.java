package io.renren.api.kit;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContentSecurityKit {

	public static final String MSG_SEC_CHECK_URL = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token={}";
	public static final String IMG_SEC_CHECK_URL = "https://api.weixin.qq.com/wxa/img_sec_check?access_token={}";

	public static boolean chechMsg(String accessToken, String msg) {
		log.info("检测敏感词 {}", msg);
		JSONObject paramJson = new JSONObject();
		paramJson.put("content", msg);
		String result = HttpUtil.post(StrUtil.format(MSG_SEC_CHECK_URL, accessToken), paramJson.toJSONString());
		log.info("检测敏感词结果 {}", result);
		JSONObject resultJson = JSON.parseObject(result);
		int errcode = resultJson.getIntValue("errcode");
		String errmsg = resultJson.getString("errmsg");
		if (errcode == 87014) {
			log.warn("检测出敏感词汇 {}", errmsg);
			return true;
		} else if (errcode == 0) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean chechImg(String accessToken, String filePath) {
		log.info("检测敏感图片 {}", filePath);
		HashMap<String, Object> paramMap = new HashMap<>();
		// 文件上传只需将参数中的键指定（默认file），值设为文件对象即可，对于使用者来说，文件上传与普通表单提交并无区别
		paramMap.put("media", FileUtil.file(filePath));
		String result = HttpUtil.post(StrUtil.format(IMG_SEC_CHECK_URL, accessToken), paramMap);
		log.info("检测敏感图片结果 {}", result);
		JSONObject resultJson = JSON.parseObject(result);
		int errcode = resultJson.getIntValue("errcode");
		String errmsg = resultJson.getString("errmsg");
		if (errcode == 87014) {
			log.warn("检测出敏感图片 {}", errmsg);
			return true;
		} else if (errcode == 87015) {
			log.warn("检测图片过大 {}", errmsg);
			return true;
		} else if (errcode == 0) {
			return false;
		} else {
			return true;
		}
	}

}
