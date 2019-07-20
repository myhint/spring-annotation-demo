package com.itbounds.dev.springannotationdemo.controller;

import com.itbounds.dev.springannotationdemo.dto.request.HeroSearchRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 * @Author blake
 * @Date 2019-07-18 14:20
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/hero")
public class HeroController {

    @ApiOperation("获取英雄名称")
    @RequestMapping("/name")
    public String getHeroName(HeroSearchRequest request) {

        return "周恩来";
    }

    @ApiOperation("获取英雄出生地")
    @RequestMapping("/country")
    public String getHeroCountry() {

        return "中国";
    }

}
