package com.cmbc.controller;

import com.cmbc.models.vo.DateVo;
import com.cmbc.utils.DateConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Date;

@RestController
@Api(tags = "交易日期转换")
@RequestMapping("date/convert")
public class DateConvertController {

    @PostMapping("initialize")
    @ApiOperation("初始化交易日")
    public Date initialize(@ApiParam(value = "查询日期") @NotNull DateVo dateVo){
        return DateConvertUtil.initializeUtil(dateVo);
    }

    @PostMapping("anytimeInitialize")
    @ApiOperation("任意时间初始化交易日")
    public Date anytimeInitialize(@ApiParam(value = "查询日期") @NotNull DateVo dateVo,
                                  @ApiParam(value = "第N天") @NotNull int day){
        return DateConvertUtil.anytimeInitializeUtil(dateVo, day);
    }
}
