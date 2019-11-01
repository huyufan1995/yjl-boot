package io.renren.api.vo;


public class ErrorResponse extends ApiResult {

	public final static ApiResult OPEN_GROUP_HAVE = ApiResult.error(1000, "重复群转发信息");
	public final static ApiResult OPEN_PERSONAL_HAVE = ApiResult.error(1002, "个人转发信息");
	public final static ApiResult OPEN_GROUP_EXPECTION = ApiResult.error(1003, "无法获取群发信息");
	public static final ApiResult PRAISE_ALREAD = ApiResult.error(1004, "该模板已经点赞");
	public static final ApiResult PRAISE_CANCEL = ApiResult.error(1005, "该模板已经取消点赞");
	public static final ApiResult PRAISE_NO_EXIST = ApiResult.error(1006, "该模板未点过赞，不能直接取消点赞");
	
}
