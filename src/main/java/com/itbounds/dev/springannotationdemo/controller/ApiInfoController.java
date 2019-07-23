package com.itbounds.dev.springannotationdemo.controller;

import com.itbounds.dev.springannotationdemo.model.ApiRelationModel;
import com.itbounds.dev.springannotationdemo.util.ApiDetailUtil;
import com.itbounds.dev.springannotationdemo.util.ExcelUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @Description TODO
 * @Author blake
 * @Date 2019-07-20 10:05
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/info")
public class ApiInfoController {

    @GetMapping
    public void exportApiModels(HttpServletResponse response) throws IOException {

        List<ApiRelationModel> apiRelationModels = ApiDetailUtil.listControllerApiInformation();

        String fileName = "ApiDocs"; // Excel文件的sheet名称
        String excelName = UUID.randomUUID().toString();
        ExcelUtil.writeExcel(response, apiRelationModels, excelName, fileName, new ApiRelationModel());

    }

}
