package com.github.hcsp.multithread;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class MultiThreadWordCount1 {
    // 使用threadNum个线程，并发统计文件中各单词的数量
    public static Object lock = new Object();

    public static Map<String, Integer> count(int threadNum, List<File> files) {
        for (int i = 0; i < threadNum; i++) {
            synchronized (lock) {
                List<String> wordsList = handleFileToStringList(files);
                Map<String, Integer> wordsCounts = handleWordsCounts(wordsList);
                return wordsCounts;
            }
        }

        return null;
    }

    public static List<String> handleFileToStringList(List<File> fileList) {
        List<String> wordsList = new ArrayList<>();
        for (File file : fileList) {
            try {
                String line;
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                while ((line = bufferedReader.readLine()) != null) {
                    // 将文本转化成数组
                    String[] tempStr = line.split("\\s+");
                    // 将数组元素添加到数组列表中
                    wordsList.addAll(Arrays.asList(tempStr));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return wordsList;
    }

    public static Map<String, Integer> handleWordsCounts(List<String> wordsList) {
        Map<String, Integer> wordsCounts = new TreeMap<>();
        for (String word :
                wordsList) {
            if (wordsCounts.get(word) != null) {
                wordsCounts.put(word, wordsCounts.get(word) + 1);
            } else {
                wordsCounts.put(word, 1);
            }
        }
        return wordsCounts;
    }

    public static void main(String[] args) {
        String s = System.getProperty("basedir", System.getProperty("user.dir"));
        s = s + "\\src\\main\\java\\com\\github\\hcsp\\multithread\\";
        File dir = new File(s);
        File[] files = dir.listFiles();
        List<File> fileList = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.endsWith(".txt")) {
                    fileList.add(file.getAbsoluteFile());
                }
            }
        }
        List<String> wordsList = handleFileToStringList(fileList);
        Map<String, Integer> wordsCounts = handleWordsCounts(wordsList);
        System.out.println(wordsCounts);
    }
}
