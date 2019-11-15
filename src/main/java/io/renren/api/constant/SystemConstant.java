package io.renren.api.constant;

import java.util.Arrays;
import java.util.List;

public interface SystemConstant {

	String CREATE_PEOPLE = "越柬寮世联会";

	String IMAGE_REG = ".jpg|.jpeg|.png|.bmp|.gif";

	String TRUE_STR = "true";
	String FALSE_STR = "false";

	String MEMBER_TYPE_MSG = "请先认证";

	String T_STR = "t";
	String F_STR = "f";

	String TEXT_TYPE = "1";
	String IMAGE_TYPE = "2";
	String VIDEO_TYPE = "3";

	String INFORMATION_TYPE = "1";
	String COMMENT_TYPE = "2";
	String APPLY_TYPE = "3";
	String MEMBER_TYPE = "4";

	String FONT_STR = "font";
	String IMAGE_STR = "image";

	String IMAGE_SHAPE_YX = "yx";//圆形
	String IMAGE_SHAPE_JX = "jx";//矩形

	/** 默认名片头像*/
	String DEFAULT_CARD_PORTRAIT = "http://yykj-public-image-1252188577.cos.ap-beijing.myqcloud.com/app/DEFAULT_CARD_PORTRAIT.png";
	/** 默认名片头像 随机*/
	String DEFAULT_CARD_RANDOM_PORTRAIT = "http://yykj-public-image-1252188577.cos.ap-beijing.myqcloud.com/app/head/{}.jpg";
	/** 默认名片姓名*/
	String DEFAULT_CARD_NAME = "暂无昵称";
	String DEFAULT_CARD_POSITION = "暂无职位信息";
	String DEFAULT_CARD_COMPANY = "暂无公司信息";
	/** 短信验证码模板ID */
	Integer SMS_TEMPLATE_ID_VERIFICATION_CODE = 403320;
	/** 短信签名 */
	String SMS_SIGN = "影约可见";
	/** 小程序名称 */
	String APP_NAME = "影约可见";
	/** cos 存储桶 */
	String BUCKET_NAME_IMAGE = "yykj-public-image-1252188577";
	/** vip单价KEY */
	String KEY_VIP_UNIT_PRICE = "VIP_UNIT_PRICE";
	/** vip说明  */
	String KEY_VIP_EXPLAIN = "VIP_EXPLAIN";
	/** 默认报名表单项目 KEY */
	String KEY_DEFAULT_APPLY_ITEM = "DEFAULT_APPLY_ITEM";
	/** 最大上传图片大小 */
	int MAX_UPLOAD_FILE_SIZE = 3145728;// 3MB
	
//	String TMP_DIR = System.getProperty("java.io.tmpdir").concat("/");
	String TMP_DIR = "/mnt/app/yykj/tmp/";
	
	/** 小程序路径 官网详情 */
	String APP_PAGE_PATH_Member_DETAIL = "pages/my/smhx/smhx?code={}";
	String APP_PAGE_PATH_CARD_DETAIL = "pages/index/mp/mp?id={}&is=1";
	String APP_PAGE_PATH_ACTIVATE_VIP = "pages/index/index?type=vip&acode={}";
	/** 报名详情二维码 */
	String APP_PAGE_PATH_APPLY_DETAIL = "pageBm/bmlook/bmlook?applyId={}&shareMemberId={}";
	/** 案例详情二维码 */
	String APP_PAGE_PATH_CASE_DETAIL = "pageAnli/look/look?caseId={}&shareMemberId={}";
	/** 小程序 个人中心页面 */
	String APP_PAGE_PATH_CENTER = "pages/center/center";
	/** 小程序 管理页面 */
	String APP_PAGE_PATH_HOME = "pages/home/home";
	/** 小程序首页路径 */
	String APP_PAGE_PATH_INDEX = "pages/index/index";
	
	/** 微官网默认元数据 */
	String DEFAULT_METADATA = "{\"box2\":{\"title\":\"公司简介\",\"text\":\"影约可见，数字化营销crm系统。帮助企业实现销售资源及流程数字化管理，全渠道多样式智能获取商机线索。客户数据实时同步，精准转化。为企业营销及销售团队提供智慧支持，助力业绩增长！\",\"banner\":\"https://yykj-public-image-1252188577.cos.ap-beijing.myqcloud.com/app/userdata/box_2_img.png\"},\"box3\":{\"title\":\"产品服务\",\"imgs\":[\"https://yykj-public-image-1252188577.cos.ap-beijing.myqcloud.com/app/userdata/box_3_img.png\"]},\"list\":[{\"type\":\"box4\",\"title\":\"更多介绍\",\"imgs\":[\"https://yykj-public-image-1252188577.cos.ap-beijing.myqcloud.com/app/userdata/box_4_nav1.png\",\"https://yykj-public-image-1252188577.cos.ap-beijing.myqcloud.com/app/userdata/box_4_nav2.png\"]}],\"box7\":{\"show\":true,\"title\":\"留言管理\"},\"box8\":{\"show\":true,\"title\":\"联系我们\",\"text\":\"公司地址：北京市昌平区龙旗广场\"}}";
	
	/** 会员迁移数据更改的表名 */
	List<String> REMOVAL_TABLE = Arrays.asList("tb_template_website_layout", "tb_tag", "tb_leave", "tb_clientele_tag",
			"tb_clientele_remark", "tb_clientele", "tb_case", "tb_apply", "tb_apply_record");
	
	/** 案例简介最大每行19个汉字 */
	String REGEX_CASE_INTRO = "(.{19})";
	/** 案例标题最大每行19个汉字 */
	String REGEX_CASE_TITLE = "(.{11})";

	/**
	 * 默认最热使用天数
	 */
	Integer DEFAULT_SORT_DAYS = 7;

	/**
	 * 默认推荐条数
	 */
	Integer DEFAULT_PRAISE_CNT = 10;

	/**
	 * 默认推荐使用天数
	 */
	Integer DEFAULT_PRAISE_DAYS = 10;

	/**
	 * 点赞头像个数
	 */
	Integer DEFAULT_HEADER_CNT = 7;

	/**
	 * 分页起始
	 */
	Integer DEFAULT_OFFSET = 0;

	/**
	 * 最热使用天数规则
	 */
	String SORT_HOT = "hot";

	/**
	 * 推荐使用天数规则
	 */
	String SORT_PRAISE = "praise";

	/**
	 * 推荐查询条数规则
	 */
	String PRAISE_SEARCH = "praisesearch";

	/**
	 * 点赞成功状态
	 */
	String PRAISE_SUCCESS = "praise";

	/**
	 * 点赞取消状态
	 */
	String PRAISE_CANCEL = "cancel_praise";

	/**
	 * 个人中心广告位类型
	 */
	String BANNER_ADVS = "advs";

}
