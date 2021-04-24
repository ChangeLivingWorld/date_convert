package com.cmbc.controller;

import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "交易日期转换")
@RequestMapping("date/convert")
@Validated
public class DateConvert {

}
