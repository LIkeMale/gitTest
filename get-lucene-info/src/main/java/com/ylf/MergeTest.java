package com.ylf;

import org.apache.commons.lang3.time.StopWatch;

import java.util.Timer;

/**
 * @author ylfeng
 * @date 2022年07月07日 20:10
 */
public class MergeTest {
    public static void main(String[] args) throws InterruptedException {
        //获取当前时间
        System.out.println(System.currentTimeMillis());
        Thread.sleep(1000);
        System.out.println(System.currentTimeMillis());
        StopWatch stopWatch = StopWatch.createStarted();
        Thread.sleep(1000);
        System.out.println(stopWatch.getTime());
    }
}
