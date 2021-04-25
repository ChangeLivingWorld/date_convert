package com.cmbc.utils;

import com.cmbc.models.vo.DateVo;
import org.springframework.beans.factory.annotation.Value;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateConvertUtil {
    @Value("#{'${holiday}'.split(',')}")
    private static List<DateVo> holidayList;

    /**
     * 根据交易日,初始化交易日
     * @param dateVo
     * @return date
     */
    public static Date initializeUtil(DateVo dateVo){
        //获取传入的交易日期
        Date date = dateVo.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //如果传入时间为当天15点之前，则向前推一天
        if (calendar.HOUR_OF_DAY > 15){
            calendar.set(calendar.HOUR_OF_DAY, calendar.get(calendar.HOUR_OF_DAY) + 1);
        }
        //判断输入年份是否为2021年
        if (calendar.YEAR != 2021){
            return null;
        }
        //判断传入的交易日期是否为周末或节假日
        while (holidayList.contains(dateVo)){
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        }
        return calendar.getTime();
    }

    public static Date anytimeInitializeUtil(DateVo dateVo, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateVo.getDate());
        calendar.set(calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + day);
        return initializeUtil(new DateVo(calendar.getTime()));
    }

}
