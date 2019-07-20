package com.itbounds.dev.springannotationdemo.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 * @Author blake
 * @Date 2019-07-19 17:52
 * @Version 1.0
 */
@Data
@NoArgsConstructor
public class ApiRelationModel extends BaseRowModel {

    @ExcelProperty(value = "接口URL", index = 0)
    private String apiRequestMappingUrl;

    @ExcelProperty(value = "接口描述", index = 1)
    private String apiOperation;

    @ExcelProperty(value = "属性名称", index = 2)
    private String fieldName;

    @ExcelProperty(value = "属性类型", index = 3)
    private String fieldType;

    @ExcelProperty(value = "属性描述", index = 4)
    private String fieldDesc;

    @ExcelProperty(value = "非空描述", index = 5)
    private String notNullOrNotBlankDesc;

}
