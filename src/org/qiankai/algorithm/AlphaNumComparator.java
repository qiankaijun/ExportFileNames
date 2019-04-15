package org.qiankai.algorithm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * 文件列表导出
 */
public class AlphaNumComparator implements Comparator<String> {
    /**
     * 判断字符是不是数字
     * @param ch
     * @return 字符在'0'~'9'范围内则返回true，否则返回false
     */
    private final boolean isDigit(char ch) {
        return ((ch >= 48) && (ch <= 57));
    }

    /**
     * 从marker指定的位置开始读取一个块。块的意思是连续的数字或非数字
     * @param s       字符串
     * @param sLength 字符串长度(避免该算法内重复计算s.length())
     * @param marker  标记位(从这里开始读取块)
     * @return 返回从指定位置读到的块
     */
    private final String getChunk(String s, int sLength, int marker) {
        StringBuilder chunk = new StringBuilder();
        char c = s.charAt(marker);
        chunk.append(c);
        ++marker;
        if (isDigit(c)) { // 是数字，则连续读取到非数字
            while (marker < sLength) {
                c = s.charAt(marker);
                if (!isDigit(c))
                    break;
                chunk.append(c);
                ++marker;
            }
        } else { // 不是数字，则连续读取到数字
            while (marker < sLength) {
                c = s.charAt(marker);
                if (isDigit(c))
                    break;
                chunk.append(c);
                ++marker;
            }
        }
        return chunk.toString();
    }

    /**
     * 实现compare接口
     *
     * @param s1
     * @param s2
     * @return
     */
    public int compare(String s1, String s2) {
        if ((s1 == null) || (s2 == null)) {
            return 0;
        }

        int s1Marker = 0;
        int s2Marker = 0;
        int s1Length = s1.length();
        int s2Length = s2.length();

        while (s1Marker < s1Length && s2Marker < s2Length) {
            String s1Chunk = getChunk(s1, s1Length, s1Marker); // 读取块
            s1Marker += s1Chunk.length(); // 标记移动,指向未读取的第一位

            String s2Chunk = getChunk(s2, s2Length, s2Marker);
            s2Marker += s2Chunk.length();

            // 如果两个块都包含数字字符，则按数字对它们排序
            int result = 0;
            if (isDigit(s1Chunk.charAt(0)) && isDigit(s2Chunk.charAt(0))) { // 都为数字块
                // 按长度比较，长的置后
                int thisChunkLength = s1Chunk.length();
                result = thisChunkLength - s2Chunk.length();
                // 长度相同，对比第一个不同的数字
                if (result == 0) {
                    for (int i = 0; i < thisChunkLength; i++) {
                        result = s1Chunk.charAt(i) - s2Chunk.charAt(i);
                        if (result != 0) {
                            return result; // 对比数字，返回第一个不同位的字符差值
                        }
                    }
                }
            } else { // 不都为数字块
                result = s1Chunk.compareTo(s2Chunk);
            }

            if (result != 0)
                return result; // 对应块有不为数字的，返回通用字符串比对结果
        }

        return s1Length - s2Length; // 两个字符串的块数不同，返回长度差
    }

    /**
     * 从指定路径读取文件列表，并导出到指定路径
     * @param sourcePath // 源文件夹路径
     * @param exportPath // 导出路径
     */
    public void getList(String sourcePath, String exportPath) {
        // 源文件夹
        File sourceFolder = new File(sourcePath); // 文件夹路径
        String[] fileList = sourceFolder.list(); // 文件名列表

        // 导出文件夹
        File exportFile = new File(exportPath + "/FileList_" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".txt"); // 输出的txt文件名
        try {
            exportFile.createNewFile(); // 新建txt文档
        } catch (IOException e) {
            e.printStackTrace();
        }
        String txtList = Arrays.asList(fileList).stream().sorted(new AlphaNumComparator()).collect(Collectors.joining("\r\n")); // 注意。\r\n不能改变，否则影响换行
        try {
            FileWriter fw = new FileWriter(exportFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(txtList);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试，源文件夹与输出文件夹都为D:
     * @param args
     */
    public static void main(String[] args) {
        new AlphaNumComparator().getList("D:", "D:");
    }
}
