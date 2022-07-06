package com.ylf;

import cn.hutool.core.date.DateUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

/**
 * @author ylfeng
 * @date 2022年04月18日 17:23
 */
public class ReadLucene {
    public static final String ID =  "id";
    public static final String HAPPEN_TIME =  "happenTime";
    //返回Lucene的最大最小时间
    public static Date recordTime(String dir,Boolean reverse) {
        String recordLastTime;
        Sort sort;
        Date recordLastTimeToDate = null;
        File file;
        try {
            //先判断目录下有没有索引文件
            file = new File(dir);
            if (file.list().length<1) return null;
            //1. 创建分词器(对搜索的关键词进行分词使用)
            //注意: 分词器要和创建索引的时候使用的分词器一模一样
            Analyzer analyzer = new StandardAnalyzer();
            //2. 创建查询对象,
            //第一个参数: 默认查询域, 如果查询的关键字中带搜索的域名, 则从指定域中查询, 如果不带域名则从, 默认搜索域中查询
            //第二个参数: 使用的分词器
            QueryParser queryParser = new QueryParser(ID, analyzer);
            //3. 设置搜索关键词
            Query query = queryParser.parse("*:*");
            //4. 创建Directory目录对象, 指定索引库的位置
            Directory directory = FSDirectory.open(Paths.get(dir));
            //5. 创建输入流对象
            IndexReader indexReader = DirectoryReader.open(directory);
            //6. 创建搜索对象
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            //7. 搜索, 并返回结果
            //第二个参数: 是返回多少条数据用于展示, 分页使用
            if (reverse){
                 sort = new Sort(new SortField(ID,SortField.Type.LONG,true));
            }else {
                 sort = new Sort(new SortField(ID,SortField.Type.LONG));
            }

            TopDocs topDocs = indexSearcher.search(query, 1, sort);
            //8. 获取结果集
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            if (scoreDocs.length > 0){
                ScoreDoc scoreDoc = scoreDocs[0];
                Document document = indexSearcher.doc(scoreDoc.doc);
                recordLastTime = document.get(HAPPEN_TIME);
                recordLastTimeToDate = DateUtil.date(Long.parseLong(recordLastTime)* 1000L);
            }
            //9. 关闭流
            indexReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return recordLastTimeToDate;
    }

    //返回lucene的数据量
    public static long recordCount(String dir) {
        long recordCount = 0L;
        File file;
        try {
            //先判断目录下有没有索引文件
            file = new File(dir);
            if (file.list().length<1) return 0L;
            //1. 创建分词器(对搜索的关键词进行分词使用)
            //注意: 分词器要和创建索引的时候使用的分词器一模一样
            Analyzer analyzer = new StandardAnalyzer();
            //2. 创建查询对象,
            //第一个参数: 默认查询域, 如果查询的关键字中带搜索的域名, 则从指定域中查询, 如果不带域名则从, 默认搜索域中查询
            //第二个参数: 使用的分词器
            QueryParser queryParser = new QueryParser(ID, analyzer);
            //3. 设置搜索关键词
            Query query = queryParser.parse("*:*");
            //4. 创建Directory目录对象, 指定索引库的位置
            Directory directory = FSDirectory.open(Paths.get(dir));
            //5. 创建输入流对象
            IndexReader indexReader = DirectoryReader.open(directory);
            //6. 创建搜索对象
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            //7. 搜索, 并返回结果
            recordCount = indexSearcher.count(query);
            //9. 关闭流
            indexReader.close();
    }catch (Exception e){
        e.printStackTrace();
    }
        return recordCount;
    }
}
