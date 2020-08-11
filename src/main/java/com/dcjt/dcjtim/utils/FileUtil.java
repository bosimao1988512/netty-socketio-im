package com.dcjt.dcjtim.utils;

import java.io.*;

/**
 * <p>
 * 文件与字节数据相互转换
 * </p>
 *
 * @author 滔哥
 * @since 2020/7/27
 */
public class FileUtil {

    public static byte[] file2byte(String fileName) {
        byte[] file = null;
        try (InputStream in = new FileInputStream(new File(fileName)); ByteArrayOutputStream out = new ByteArrayOutputStream(1024)) {
            byte[] buf = new byte[1024];
            for (int n = 0; n != -1; n = in.read(buf)) {
                out.write(buf, 0, n);
            }
            file = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void byte2file(byte[] bfile, String path) {
        File dir = new File(path);
        if (!dir.getParentFile().exists()) {
            dir.getParentFile().mkdirs();
        }
        try (OutputStream out = new FileOutputStream(path); BufferedOutputStream bufferedOut = new BufferedOutputStream(out)) {
            bufferedOut.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String imagepath = "D:\\osi七层.png";
        String bak = "D:\\osi七层_bak.png";
        byte[] image = file2byte(imagepath);
        byte2file(image, bak);
    }
}
