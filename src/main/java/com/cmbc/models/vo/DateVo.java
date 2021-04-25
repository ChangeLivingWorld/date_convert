package com.cmbc.models.vo;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class DateVo {
//    @NotNull
    @ApiModelProperty("交易日期(yyyy-MM-dd HH:mm)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public DateVo(@NotNull Date date) {
        this.date = date;
    }
}
