package com.ylf;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author ylfeng
 * @date 2022年04月12日 14:31
 */
public class GetLuceneInfo {
    public static final String DIR_RECORD_END =  "/index/other/index/audit_record";
    public static final String DIR_LUCENE= "/index/other/index/";
    public static final String DIR_RECORD_RISK_END =  "/index/other/index/audit_record_risk";
    public static final String LUCENE_DIR = "/data/";
    public static void main(String[] args) {
        //获取所有的lucene目录
        String dirPrefix = "lucene";
        File file = new File(LUCENE_DIR);
        //设置过滤条件为目录并且按照Lucene开头
        FileFilter fileFilter = (File pathname) -> pathname.isDirectory() && pathname.getName().startsWith(dirPrefix);
        File[] files = file.listFiles(fileFilter);
        List<String> fileNames = new ArrayList<>();
        if (files != null){
            Arrays.stream(files).forEach((fileObject) -> {
                fileNames.add(fileObject.getName());
            });
        }
        //循环读取lucene目录的信息
        for (String fileName : fileNames) {
            File luceneFile = new File(LUCENE_DIR + fileName + DIR_LUCENE);
            //没有Lucene的路径，直接跳过
            if (!luceneFile.exists()){
                System.out.println("目录：" + fileName + "不包含Lucene数据，跳过");
                System.out.println();
                continue;
            }
            //当前分片，直接跳过
            if ("lucene".equals(fileName)) continue;
            //获取风险最晚时间
            Date riskLastTime = ReadLucene.recordTime(LUCENE_DIR + fileName + DIR_RECORD_RISK_END,true);
            //获取风险最早时间
            Date riskFirstTime = ReadLucene.recordTime(LUCENE_DIR + fileName + DIR_RECORD_RISK_END,false);
            //获取非风险最晚时间
            Date recordLastTime = ReadLucene.recordTime(LUCENE_DIR + fileName + DIR_RECORD_END,true);
            //获取非风险最早时间
            Date recordFirstTime = ReadLucene.recordTime(LUCENE_DIR + fileName + DIR_RECORD_END,false);
            //获取风险总数
            long riskCount = ReadLucene.recordCount(LUCENE_DIR + fileName + DIR_RECORD_RISK_END);
            //获取非风险总数
            long recordCount = ReadLucene.recordCount(LUCENE_DIR + fileName + DIR_RECORD_END);
            //得到时间的最小值
            String minTimeInfo = GetTime.getTime(riskFirstTime,recordFirstTime,"min");
            //得到时间的最大值
            String maxTimInfo = GetTime.getTime(riskLastTime,recordLastTime,"max");
            String countInfo = (riskCount + recordCount > 2100000000L)? "    该目录数据超过21亿，请不要添加该目录" : "   该目录数据总量: " + (riskCount + recordCount) + " 条";
            System.out.println("目录名: " + LUCENE_DIR + fileName + minTimeInfo + maxTimInfo  + countInfo);
            System.out.println();
        }
    }
}
