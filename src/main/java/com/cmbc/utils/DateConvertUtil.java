package com.cmbc.utils;

import com.cmbc.models.dto.JsonResult;
import com.cmbc.models.dto.ResultStatusCode;
import com.cmbc.models.vo.DateVo;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
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
    public static JsonResult initializeUtil(DateVo dateVo){
        //获取传入的交易日期
        Date date = dateVo.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //如果传入时间为当天15点之前，则向前推一天
        int year = calendar.get(calendar.YEAR);
        int hour = calendar.get(calendar.HOUR_OF_DAY);
        if (hour > 15){
            calendar.set(calendar.DAY_OF_MONTH, calendar.get(calendar.DAY_OF_MONTH) + 1);
        }
        //判断输入年份是否为2021年
        if (year != 2021){
            return new JsonResult(ResultStatusCode.YEAR_ERROR);
        }
        //判断传入的交易日期是否为周末或节假日
        while (holidayList.contains(dateVo)){
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        }
        //修改日期格式
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        return JsonResult.success(sdf.format(calendar.getTime()));
    }

    /**
     * 给定任意时间，返回给定时间的T+n交易日
     * @param dateVo
     * @param day
     * @return date
     */
    public static JsonResult anytimeInitializeUtil(DateVo dateVo, int day){
        //获取传入交易时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateVo.getDate());
        //调用初始化交易日的工具类
        calendar.set(calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + day);
        return initializeUtil(new DateVo(calendar.getTime()));
    }

}
