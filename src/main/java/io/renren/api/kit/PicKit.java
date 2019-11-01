package io.renren.api.kit;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.font.TextAttribute;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.AttributedString;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import io.renren.api.exception.ApiException;

public class PicKit extends JPanel {

	private static final long serialVersionUID = 9201763592623511432L;

	/**
	 * 将图片处理为圆形图片
	 * 传入的图片必须是正方形的才会生成圆形 如果是长方形的比例则会变成椭圆的
	 * 
	 */
	public static BufferedImage transferImgForRoundImgage(BufferedImage buffImg1) {
		BufferedImage resultImg = null;
		resultImg = new BufferedImage(buffImg1.getWidth(), buffImg1.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resultImg.createGraphics();
		Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, buffImg1.getWidth(), buffImg1.getHeight());
		// 使用 setRenderingHint 设置抗锯齿
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		resultImg = g.getDeviceConfiguration().createCompatibleImage(buffImg1.getWidth(), buffImg1.getHeight(),
				Transparency.TRANSLUCENT);
		// g.fill(new Rectangle(buffImg2.getWidth(), buffImg2.getHeight()));
		g = resultImg.createGraphics();
		// 使用 setRenderingHint 设置抗锯齿
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setClip(shape);
		g.drawImage(buffImg1, 0, 0, null);
		g.dispose();
		return resultImg;
	}

	/**
	 * 导入本地图片到缓冲区
	 */
	public static BufferedImage loadImageLocal(String imgName) {
		try {
			return ImageIO.read(new File(imgName));
		} catch (IOException e) {
			throw new ApiException("加载图片错误", 500);
		}
	}

	/**
	 * 导入网络图片到缓冲区
	 */
	public static BufferedImage loadImageUrl(String imgName) {
		try {
			URL url = new URL(imgName);
			return ImageIO.read(url);
		} catch (IOException e) {
			throw new ApiException("加载图片错误", 500);
		}
	}

	/**
	 * 生成新图片到本地
	 */
	public static void writeImageLocal(String newImage, BufferedImage img) {
		if (newImage != null && img != null) {
			try {
				File outputfile = new File(newImage);
				ImageIO.write(img, "png", outputfile);
			} catch (IOException e) {
				throw new ApiException("保存图片错误", 500);
			}
		}
	}

	/**
	 * 生成新图片到本地
	 */
	public static void writeImageLocal(File newImage, BufferedImage oldImage) {
		if (newImage != null && oldImage != null) {
			try {
				ImageIO.write(oldImage, "png", newImage);
			} catch (IOException e) {
				throw new ApiException("保存图片错误", 500);
			}
		}
	}

	/**
	 * 写入单行文字
	 * @param souceImg
	 * @param content
	 * @param font
	 * @param fontColor
	 * @param x
	 * @param y
	 * @param pxValuex 字间距
	 * @return
	 */
	public static BufferedImage writeString(BufferedImage souceImg, String content, Font font, Color fontColor, int x,
			int y, int pxValuex) {

		try {
			Graphics2D g = souceImg.createGraphics();
			g.setBackground(Color.WHITE);
			g.setColor(fontColor);// 设置字体颜色
			g.setFont(font);
			//平滑
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			if (content != null) {
				if (pxValuex > 2) {
					drawString(content, x, y, pxValuex, g);
				} else {
					g.drawString(content, x, y);
				}
			}
			g.dispose();
		} catch (Exception e) {
			throw new ApiException("写入单行文字错误", 500);
		}

		return souceImg;
	}

	/**
	 * 单行水平居中 不用x坐标
	 * @param img
	 * @param content
	 * @param font
	 * @param fontColor
	 * @param y y坐标
	 * @param pxValuex 字间距
	 * @return
	 */
	public static BufferedImage writeStringCenter(BufferedImage img, String content, Font font, Color fontColor, int y,
			int pxValuex) {

		try {
			int imgWidth = img.getWidth();
			Graphics2D g = img.createGraphics();
			g.setBackground(Color.WHITE);
			g.setColor(fontColor);// 设置字体颜色
			g.setFont(font);

			//平滑
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			FontMetrics fm = g.getFontMetrics();
			int stringWidth = fm.stringWidth(content);
			int x = imgWidth / 2 - (stringWidth + (content.length() - 1) * pxValuex) / 2;

			if (content != null) {
				if (pxValuex > 2) {
					drawString(content, x, y, pxValuex, g);
				} else {
					g.drawString(content, x, y);
				}
			}

			g.dispose();
		} catch (Exception e) {
			throw new ApiException("写入单行居中文字错误", 500);
		}

		return img;
	}

	/**
	 * 输出多个文本段  xory：true表示将内容在一行中输出；false表示将内容多行输出
	 * 
	 */
	public static BufferedImage writeStrings(BufferedImage img, String[] contentArr, Font font, Color fontColor, int x,
			int y, int pxValuex, int pxValuey, boolean xory) {
		try {
			Graphics2D g = img.createGraphics();
			g.setBackground(Color.WHITE);
			g.setColor(fontColor);
			g.setFont(font);

			//平滑
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			if (contentArr != null) {
				int arrlen = contentArr.length;
				if (xory) {
					//单行
					for (int i = 0; i < arrlen; i++) {
						drawString(contentArr[i], x, y, pxValuex, g);
						x += contentArr[i].toString().length() * font.getSize() / 2 + 5;// 重新计算文本输出位置
					}
				} else {
					//多行
					for (int i = 0; i < arrlen; i++) {
						if (pxValuex > 2) {
							drawString(contentArr[i], x, y, pxValuex, g);
						} else {
							g.drawString(contentArr[i], x, y);
						}
						FontMetrics fm = g.getFontMetrics();
						fm.stringWidth(contentArr[i]);
						y += font.getSize() + pxValuey;// 重新计算文本输出位置
					}
				}
			}
			g.dispose();
		} catch (Exception e) {
			throw new ApiException("写入多行文字错误", 500);
		}

		return img;
	}

	//写入图片
	public static BufferedImage writeImg(BufferedImage bufferedImageIn, BufferedImage bufferedImageOut, int x, int y) {

		try {
			int inWidth = bufferedImageIn.getWidth();
			int inHeight = bufferedImageIn.getHeight();
			Graphics2D g = bufferedImageOut.createGraphics();
			g.drawImage(bufferedImageIn, x, y, inWidth, inHeight, null);
			g.dispose();
		} catch (Exception e) {
			throw new ApiException("写入图片错误", 500);
		}

		return bufferedImageOut;
	}

	/**
	 * 文字写入图片 有字间距
	 * @param str
	 * @param x
	 * @param y
	 * @param pxValuex 字间距
	 * @param g
	 */
	public static void drawString(String str, int x, int y, int pxValuex, Graphics g) {
		String tempStr = new String();
		int orgStringWight = g.getFontMetrics().stringWidth(str);
		int orgStringLength = str.length();
		int tempx = x;
		int tempy = y;
		while (str.length() > 0) {
			tempStr = str.substring(0, 1);
			str = str.substring(1, str.length());
			g.drawString(tempStr, tempx, tempy);
			tempx = tempx + orgStringWight / orgStringLength + pxValuex;
		}
	}

	/**
	 * 删除线
	 * @param source
	 * @param plainFont
	 * @param fontColor
	 * @param s
	 * @param x
	 * @param y
	 * @param start
	 * @param end
	 * @return
	 */
	public static BufferedImage drawDeletedLine(BufferedImage source, Font font, Color fontColor, String content, int x,
			int y, int start, int end) {
		try {
			Graphics2D g2 = source.createGraphics();
			g2.setBackground(Color.WHITE);
			g2.setColor(fontColor);
			//平滑
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			AttributedString as = new AttributedString(content);
			as.addAttribute(TextAttribute.FONT, font);
			as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, start, end); // 删除线

			g2.drawString(as.getIterator(), x, y);
			g2.dispose();
		} catch (Exception e) {
			throw new ApiException("写入文字删除线错误", 500);
		}
		return source;
	}

	/**
	 * 下划线
	 * @param source
	 * @param plainFont
	 * @param fontColor
	 * @param s
	 * @param x
	 * @param y
	 * @param start
	 * @param end
	 * @return
	 */
	public static BufferedImage drawUnderLine(BufferedImage source, Font font, Color fontColor, String content, int x,
			int y, int start, int end) {
		try {
			Graphics2D g2 = source.createGraphics();
			g2.setBackground(Color.WHITE);
			g2.setColor(fontColor);
			//平滑
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			AttributedString as = new AttributedString(content);
			as.addAttribute(TextAttribute.FONT, font);
			as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, start, end); //下划线
			g2.drawString(as.getIterator(), x, y);
			g2.dispose();
		} catch (Exception e) {
			throw new ApiException("写入文字下划线错误", 500);
		}
		return source;
	}

	/**
	 * 传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的 
	 */
	public static BufferedImage convertCircular(BufferedImage bufferedImage) {
		// 透明底的图片
		BufferedImage bi2 = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
		Graphics2D g2 = bi2.createGraphics();
		g2.setClip(shape);
		// 使用 setRenderingHint 设置抗锯齿
		g2.drawImage(bufferedImage, 0, 0, null);
		// 设置颜色
		g2.setBackground(Color.GREEN);
		g2.dispose();
		return bi2;
	}

	public static void main(String[] args) throws IOException {

		//		BufferedImage source = PicKit.loadImageLocal("e:\\pic.jpg");
		BufferedImage source = PicKit.loadImageLocal("F:\\nginx-1.13.2\\html\\file\\template\\8\\t.jpg");

		Font font = new Font("微软雅黑", Font.PLAIN, 16);// 添加字体的属性设置
		Color fontColor = new Color(0, 0, 0);
		String str = "锄禾日当";
		//单行文字 ok
		//		PicKit.writeImageLocal("e:\\pic_.jpg", PicKit.writeString(source, str, font, fontColor, 200, 200, 50));

		//单行水平居中 ok
		PicKit.writeImageLocal("e:\\pic_.jpg", PicKit.writeStringCenter(source, str, font, fontColor, 266, 2));

		//下划线ok
		//		PicKit.writeImageLocal("e:\\pic_.jpg", PicKit.drawUnderLine(source, font, fontColor, str, 100, 200, 0, str.length()));

		//删除线ok
		//		PicKit.writeImageLocal("e:\\pic_.jpg", PicKit.drawDeletedLine(source, font, fontColor, str, 100, 100, 0, str.length()));

		//		String[] contentArr = new String[]{"锄禾日当午", "汗滴禾下土", "谁吃盘中餐", "粒粒皆辛苦"};
		//多行 ok
		//		PicKit.writeImageLocal("e:\\pic_.jpg", PicKit.writeStrings(source, contentArr, font, fontColor, 100, 200, 5, 10, false));

		// 写入图片 ok
		//		PicKit.writeImageLocal("f:\\pic_.jpg", PicKit.writeImg(PicKit.loadImageLocal("f:/pic_in2_.jpg"), source, 100, 200));
		//圆形
		//		PicKit.writeImageLocal("f:/pic_in2_.jpg", PicKit.convertCircular(PicKit.loadImageLocal("f:/pic_in2.jpg")));
		System.out.println("success");
	}
}