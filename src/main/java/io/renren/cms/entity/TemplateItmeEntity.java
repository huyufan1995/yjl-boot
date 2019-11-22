package io.renren.cms.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 模板项
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
@Getter
@Setter
public class TemplateItmeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	// 模板ID
	private Integer templateId;
	// 创建时间
	private Date ctime;
	// 更新时间
	private Date utime;
	// 是否删除 t:删除 f:未删除
	private String isDel;
	// 序号
	private int sortNum;
	// 类型 font:文本 image:图片
	private String type;
	// 字体名称
	private String fontName;
	// 字体样式 0普通 1加粗 2倾斜 3加粗+倾斜
	private int fontStyle;
	// 字体大小
	private int fontSize;
	// 字体颜色R
	private int fontColorR;
	// 字体颜色G
	private int fontColorG;
	// 字体颜色B
	private int fontColorB;
	// 是否多行 t:是 f:否
	private String isMultiLine;
	// 描述实例
	private String describe;
	// 左边X
	private int x;
	// 坐标Y
	private int y;
	// 字间距
	private int wordSpace;
	// 行距
	private int lineSpace;
	// 宽
	private int width;
	// 高
	private int height;
	// 图片路径
	private String imagePath;
	// 是否水平居中
	private String isCenter;
	// 是否删除线
	private String fontDeletedLine;
	// 是否下滑线
	private String fontUnderLine;
	// 图片形状
	private String imageShape;

	// 非持久化
	// 建议字符长度
	private int fontLength;

	@Override
	public String toString() {
		return "TemplateItmeEntity [templateId=" + templateId + ", type=" + type + ", fontName=" + fontName
				+ ", fontStyle=" + fontStyle + ", fontSize=" + fontSize + ", fontColorR=" + fontColorR + ", fontColorG="
				+ fontColorG + ", fontColorB=" + fontColorB + ", isMultiLine=" + isMultiLine + ", describe=" + describe
				+ ", x=" + x + ", y=" + y + ", wordSpace=" + wordSpace + ", lineSpace=" + lineSpace + ", width=" + width
				+ ", height=" + height + ", imagePath=" + imagePath + ", isCenter=" + isCenter + ", fontDeletedLine="
				+ fontDeletedLine + ", fontUnderLine=" + fontUnderLine + ", imageShape=" + imageShape + ", fontLength="
				+ fontLength + "]";
	}

}
