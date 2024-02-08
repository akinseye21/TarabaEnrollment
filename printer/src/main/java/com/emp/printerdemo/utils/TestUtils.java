package com.emp.printerdemo.utils;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * @User Administrator
 * @Date 2020/12/29 17:15
 */
public class TestUtils {
    static String TAG = "TestUtils";

    /**
     * esc * 图片打印算法
     *
     * @param bitmap
     * @return
     */
    public static byte[] escBmpToByte(Bitmap bitmap) {
        Log.d(TAG, "bitmap: " + bitmap.getWidth() + " - " + bitmap.getHeight());
        // 每包的纵向取三个字节
        int printPackageByteNumber = 3;
        // 每个字节八位
        int byteDigitNumber = 8;
        // 每包的总行数
        int printPackageLineNumber = printPackageByteNumber * byteDigitNumber;

        // 获取图片宽度
        int scaleWidth = bitmap.getWidth();
        // 获取分包发送数量，（height/printLineNumber）
        int bitHeight = (bitmap.getHeight() + printPackageLineNumber - 1) / printPackageLineNumber;
        // 分包数据包头
        byte[] bitHead = new byte[]{0x1b, 0x2a, 0x21, (byte) (scaleWidth % 256), (byte) (scaleWidth / 256)};
        // 分包数据包头
        byte[] bitTail = new byte[]{0x1b, 0x4a, 0x00};
        // 定义数据包数组长度  （包头、图片内容、进纸距离）
        int bitLength = bitHead.length + scaleWidth * printPackageByteNumber + bitTail.length;

        byte[] dataArray = new byte[bitHeight * (bitLength)];
        int offset = 0;

        // 遍历每个包的数据
        for (int h = 0; h < bitHeight; h++) {
            byte dataVec[] = new byte[bitLength];
            // 第一部分、包头
            System.arraycopy(bitHead, 0, dataVec, 0, bitHead.length);
            // 第二部分、包内容 长度为(scaleWidth * printPackageByteNumber)
            int k = 5;
            for (int w = 0; w < scaleWidth; w++) {
                for (int i = 0; i < printPackageByteNumber; i++) {
                    int value = 0;
                    for (int j = 0; j < byteDigitNumber; j++) {
                        int index = (h * printPackageLineNumber + (i * byteDigitNumber) + j);
                        if (index >= bitmap.getHeight()) {
                            value |= 0;
                        } else {
                            value |= px2Byte(bitmap.getPixel(w, index)) << ((byteDigitNumber - 1) - j);
                        }
                    }
                    dataVec[k++] = (byte) value;
                }
            }
            // 第三部分、进纸距离
            System.arraycopy(bitTail, 0, dataVec, k, bitTail.length);

            System.arraycopy(dataVec, 0, dataArray, offset, dataVec.length);
            offset += dataVec.length;
        }

        return dataArray;
    }

    public static byte px2Byte(int pixel) {
        byte b;

        int red = (pixel & 0x00ff0000) >> 16; // 取高两位
        int green = (pixel & 0x0000ff00) >> 8; // 取中两位
        int blue = pixel & 0x000000ff; // 取低两位
        int gray = RGB2Gray(red, green, blue);
        if (gray < 127) {
            b = 1;
        } else {
            b = 0;
        }
        return b;
    }

    /**
     * 图片灰度的转化
     */
    private static int RGB2Gray(int r, int g, int b) {
        int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b);  //灰度转化公式
        return gray;
    }

}
