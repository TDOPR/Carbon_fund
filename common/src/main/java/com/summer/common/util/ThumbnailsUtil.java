package com.summer.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThumbnailsUtil {

//    public static void main(String[] args) throws IOException {
//        FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\exmple.png"));
//        InputStream result = addWaterMark(fileInputStream, "Zero Carbon Envoy/", "Musk", "7189-A947-4fc3-83d1-B58e", "JULY 17,2023");
//        FileUtils.copyInputStreamToFile(result, new File("C:\\Users\\Administrator\\Desktop\\save.png"));
//    }

    /**
     * @param srcImgFile 源图片输入流
     * @return 加水印之后的输入流
     */
    public static InputStream addWaterMark(InputStream srcImgFile, String level, String name, String number, String date) {
        try {
            //文件转化为图片
            Image srcImg = ImageIO.read(srcImgFile);
            //获取图片的宽
            int srcImgWidth = srcImg.getWidth(null);
            //获取图片的高
            int srcImgHeight = srcImg.getHeight(null);
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            {
                //获取水印对象
                Graphics2D g = bufImg.createGraphics();
                g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
                // 抗锯齿
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                //1.设置编号
                Color color = new Color(65, 144, 222);
                //根据图片的背景设置水印颜色
                g.setColor(color);
                Font font = new Font("黑体", Font.PLAIN, 32);
                //设置字体
                g.setFont(font);
                //画字
                g.drawString(number, 221, 890);

                //2.设置日期
                font = new Font("黑体", Font.PLAIN, 20);
                //设置字体
                g.setFont(font);
                g.drawString(date, 702, 707);

                //3.设置级别
                Color levelColor = new Color(153,153,153);
                g.setColor(levelColor);
                int levelFontSize=28;
                font = new Font("Sans-serif", Font.PLAIN, levelFontSize);
                //设置字体
                g.setFont(font);
                g.drawString(level, 596, 541);
                //计算字体占用的大小
                int len= level.length()*levelFontSize/2;

                //4.设置姓名
                Color nameColor = Color.BLACK;
                g.setColor(nameColor);
                font = new Font("Sans-serif", Font.BOLD, 46);
                //设置字体
                g.setFont(font);
                g.drawString(name, 596+len, 541);

                //渲染
                g.dispose();
            }
            // 输出图片
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Thumbnails.of(bufImg).scale(1f).outputQuality(0.25f).outputFormat("png").toOutputStream(os);
//            Thumbnails.of(bufImg).scale(1f).outputQuality(0.25f).outputFormat("png").toFile(new File("D:\\work\\2.png"));
            return new ByteArrayInputStream(os.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //造白图
    static BufferedImage buildWhiteImage(int width,
                                         int height,
                                         int imageType) {

        BufferedImage bi = new BufferedImage(width, height, imageType);
        Graphics2D dg = bi.createGraphics();
        dg.setColor(Color.WHITE);//设置笔刷白色
        dg.fillRect(0, 0, bi.getWidth(), bi.getHeight());//填充整个屏幕
        return bi;
    }

    //切圆角
    static BufferedImage roundImage(BufferedImage image, int width, int height, int cornerRadius) {
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = outputImage.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return outputImage;
    }


    private static void test1() throws Exception {
        //先获取三张图的 image
        BufferedImage image1 = Thumbnails.of("C:\\Users\\Administrator\\Desktop\\test.jpg").scale(1).outputQuality(1).asBufferedImage();
        //将图片处理到合适的分辨率大小

        //先造一张空白大小的正方形白色图片
        BufferedImage whiteImage = buildWhiteImage(1200, 1200, image1.getType());

        //处理第一张图片
        int width1 = image1.getWidth();
        int height1 = image1.getHeight();
        System.out.println(width1);
        System.out.println(height1);
        double sc1 = new BigDecimal(1160).divide(new BigDecimal(width1), BigDecimal.ROUND_CEILING).doubleValue();
        BufferedImage image11 = Thumbnails.of(image1).scale(sc1).outputQuality(1).asBufferedImage();
        System.out.println(image11.getWidth());
        BufferedImage image111 = Thumbnails.of(image11).sourceRegion(Positions.CENTER, image11.getWidth(), 570).scale(1).outputQuality(1).asBufferedImage();
        //第一张图 切圆角
        BufferedImage roundImage1 = roundImage(image111, image111.getWidth(), image111.getHeight(), 20);
        //处理完毕开始贴图
        //第一张图 贴图
        whiteImage = Thumbnails.of(whiteImage).watermark((int enclosingWidth,
                                                          int enclosingHeight,
                                                          int width,
                                                          int height,
                                                          int insetLeft,
                                                          int insetRight,
                                                          int insetTop,
                                                          int insetBottom) -> {
            System.out.println(enclosingWidth);
            System.out.println(enclosingHeight);
            System.out.println(width);
            System.out.println(height);
            System.out.println(insetLeft);
            System.out.println(insetRight);
            System.out.println(insetTop);
            System.out.println(insetBottom);
            return new Point(20, 20);//左上角对其

        }, roundImage1, 1).scale(1).outputQuality(1).asBufferedImage();
        whiteImage.flush();
    }

}
