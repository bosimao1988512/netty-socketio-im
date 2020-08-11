package com.dcjt.dcjtim.utils;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * <p>
 * 图片与字节数据相互转换
 * </p>
 *
 * @author 滔哥
 * @since 2020/7/27
 */
public class ImageUtil {

    public static byte[] image2byte(String path) {
        byte[] image = null;
        try (ImageInputStream in = new FileImageInputStream(new File(path)); ByteArrayOutputStream out = new ByteArrayOutputStream(1024)) {
            byte[] buf = new byte[1024];
            for (int read = 0; read != -1; read = in.read(buf)) {
                out.write(buf, 0, read);
            }
            image = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public static void byte2image(byte[] data, String path) {
        if (data.length < 3 || path.equals("")) {
            return;
        }
        File dir = new File(path);
        if (!dir.getParentFile().exists()) {
            dir.getParentFile().mkdirs();
        }
        try (ImageOutputStream imageOut = new FileImageOutputStream(new File(path));) {
            imageOut.write(data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String imagepath = "D:\\osi七层.png";
        String bak = "D:\\osi七层_bak.png";
        byte[] image = image2byte(imagepath);
        byte2image(image, bak);
    }
}
