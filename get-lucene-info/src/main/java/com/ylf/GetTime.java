package com.ylf;

import java.util.Date;

/**
 * @author ylfeng
 * @date 2022年04月20日 16:31
 */
public class GetTime {
    public static String getTime(Date riskTime , Date recordTime , String flag){
        Date finalTime;
        StringBuilder  timeInfo = new StringBuilder();
        if ("min".equals(flag)){
            //获取两个时间的最小值
            if (riskTime != null && recordTime != null) {
                finalTime = riskTime.after(recordTime) ? recordTime : riskTime;
            } else if (riskTime != null || recordTime != null){
                finalTime = riskTime == null ? recordTime : riskTime;
            }else {
                return "   该目录下风险与非风险都没有最小时间！";
            }
            timeInfo = timeInfo.append("     数据最小时间为: ").append(finalTime.toString().substring(0,10).replace("-","/"));
        }else if ("max".equals(flag)){
            //获取两个时间的最大值
            if (riskTime != null && recordTime != null){
                finalTime = riskTime.after(recordTime) ? riskTime  : recordTime;
            }else if (riskTime != null || recordTime != null){
                finalTime = riskTime == null ? recordTime : riskTime;
            }else {
                return "   该目录下风险与非风险都没有最大时间！";
            }
            timeInfo =timeInfo.append("    数据最大时间为: ").append(finalTime.toString().substring(0,10).replace("-","/"));
        }
        return timeInfo.toString();
    }
}
