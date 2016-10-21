package com.zeno.viewinject.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Zeno on 2016/10/21.
 */
public class PrintUtils {

    /**
     * print log
     * @param msg log message
     */
    public static void print(String msg) {
        BufferedWriter bufferedWriter = null;
        try{
            bufferedWriter = new BufferedWriter(new FileWriter(new File("f://log.txt"),true));
            bufferedWriter.write(msg);
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
