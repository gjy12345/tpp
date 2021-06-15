package cn.gjyniubi.cinema.app.util;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Random;

/**
 * @Author gujianyang
 * @Date 2021/6/2
 * @Class QrcodeUtil
 */
public class QrcodeUtil{

    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;
    // LOGO宽度
    private static final int WIDTH = 60;
    // LOGO高度
    private static final int HEIGHT = 60;

    /*********************二维码的生成方法以及重载*********************/

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content
     *            内容
     * @param imgPath
     *            LOGO地址
     * @param destPath
     *            存放目录
     * @param needCompress
     *            是否压缩LOGO
     * @throws Exception
     */
    public static String encode(String content, String imgPath, String destPath,
                                boolean needCompress) throws Exception {
        BufferedImage image = createImage(content, imgPath,
                needCompress);
        mkdirs(destPath);
        String file = new Random().nextInt(99999999)+".jpg";
        ImageIO.write(image, FORMAT_NAME, new File(destPath+"/"+file));
        return file;
    }
    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content
     *            内容
     * @param imgPath
     *            LOGO地址
     * @param destPath
     *            存储地址
     * @throws Exception
     */
    public static String encode(String content, String imgPath, String destPath)
            throws Exception {
        String fileName = encode(content, imgPath, destPath, true);
        return fileName;
    }
    /**
     * 生成二维码(不包含LOGO)
     *
     * @param content
     *            内容
     * @param destPath
     *            存储地址
     * @param needCompress
     *            是否压缩
     * @throws Exception
     */
    public static void encode(String content, String destPath,
                              boolean needCompress) throws Exception {
        encode(content, null, destPath, needCompress);
    }

    /**
     * 生成二维码(不包含LOGO,且不压缩图片)
     *
     * @param content
     *            内容
     * @param destPath
     *            存储地址
     * @throws Exception
     */
    public static void encode(String content, String destPath) throws Exception {
        encode(content, null, destPath, false);
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content
     *            内容
     * @param imgPath
     *            LOGO地址
     * @param output
     *            输出流
     * @param needCompress
     *            是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String imgPath,
                              OutputStream output, boolean needCompress) throws Exception {
        BufferedImage image = createImage(content, imgPath,
                needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * 生成二维码
     *
     * @param content
     *            内容
     * @param output
     *            输出流
     * @throws Exception
     */
    public static void encode(String content, OutputStream output)
            throws Exception {
        encode(content, null, output, false);
    }

    /**
     * 解析二维码
     *
     * @param file
     *            二维码图片
     * @return
     * @throws Exception
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    /**
     * 解析二维码
     *
     * @param path
     * 二维码图片地址
     * @return
     * @throws Exception
     */
    public static String decode(String path) throws Exception {
        return decode(new File(path));
    }



    private static BufferedImage createImage(String content, String imgPath,
                                             boolean needCompress) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
//    	设置QR二维码的纠错级别（H为最高级别）具体级别信息
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//        内容所使用编码
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
//      设置图片的边距
        hints.put(EncodeHintType.MARGIN, 1);
//      生成矩阵
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000
                        : 0xFFFFFFFF);
            }
        }
        if (imgPath == null || "".equals(imgPath)) {
            return image;
        }
//         插入图片LOGO
        insertImage(image, imgPath, needCompress);
        return image;
    }
    /**
     * 插入LOG图片到已经生成的二维码中
     *
     * @param source
     *            二维码图片
     * @param imgPath
     *            LOGO图片地址
     * @param needCompress
     *            是否压缩
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String imgPath,
                                    boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println(""+imgPath+"   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * @param destPath 存放目录
     */
    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

}