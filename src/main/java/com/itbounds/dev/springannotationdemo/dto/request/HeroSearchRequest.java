package com.itbounds.dev.springannotationdemo.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description TODO
 * @Author blake
 * @Date 2019-07-19 14:39
 * @Version 1.0
 */
@Data
@NoArgsConstructor
public class HeroSearchRequest {

    @ApiModelProperty(value = "姓名")
    @NotBlank(message = "姓名不允许为空")
    private String name;

    @ApiModelProperty(value = "国家")
    @NotBlank(message = "国家不允许为空")
    private String country;

    @ApiModelProperty(value = "年龄")
    @NotNull(message = "年龄不允许为空")
    private Integer age;

}
