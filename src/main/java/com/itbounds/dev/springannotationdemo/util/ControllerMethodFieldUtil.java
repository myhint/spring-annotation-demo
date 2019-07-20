package com.itbounds.dev.springannotationdemo.util;

import com.itbounds.dev.springannotationdemo.config.ApplicationContextConfig;
import com.itbounds.dev.springannotationdemo.model.ApiRelationModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description 获取API入参中存在@NotNull或@NotBlank注解标记的参数列表
 * @Author blake
 * @Date 2019-07-19 10:45
 * @Version 1.0
 */
public class ControllerMethodFieldUtil {

    public static void main(String[] args) {

        // 1 获取数据列表Models
        List<ApiRelationModel> apiRelationModels = listControllerAndMethodFieldInfo();

        for (ApiRelationModel apiRelationModel : apiRelationModels) {
            System.out.println(apiRelationModel);
        }

        /**
         *
         @Override public void exportHomeVisitDataList(HomeVisitSearchRequest request, HttpServletResponse response)
         throws IOException {

         // 满足查询条件的家访数据列表
         List<ExportHomeVisitModel> homeVisitModels = adminHomeVisitDAO.listHomeVisit(request);

         // 导出Excel文件
         String excelName = fileName + YMDHMS_FORMAT.format(new Date());
         ExcelUtil.writeExcel(response, homeVisitModels, excelName, fileName, new ExportHomeVisitModel());
         }
         */
    }

    public static List<ApiRelationModel> listControllerAndMethodFieldInfo() {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationContextConfig.class);

        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RestController.class);

        // TODO Final Data List
        List<ApiRelationModel> finalApiRelationModelList = new ArrayList<>();

        for (String bean : beansWithAnnotation.keySet()) {
            // System.out.println(" ========== " + bean + " -> " + beansWithAnnotation.get(bean));

            if (bean.equalsIgnoreCase("ApiInfoController")) {
                continue;
            }

            Class<?> clazz = beansWithAnnotation.get(bean).getClass();
            // System.out.println(clazz);

            String controllerMappingVal = null;

            // 1 获取Controller本身标注@RequestMapping的注解信息
            RequestMapping controllerMapping = clazz.getAnnotation(RequestMapping.class);
            if (Objects.nonNull(controllerMapping)) {
                controllerMappingVal = Arrays.toString(controllerMapping.value());
            }

            // TODO 针对单一Controller de Data List
            List<ApiRelationModel> apiRelationModelList = null;

            // 2 获取Controller中相关方法标注@RequestMapping的注解信息
            Method[] methods = clazz.getDeclaredMethods();

            if (methods.length > 0) {
                for (Method method : methods) {
                    RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
                    ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                    Class<?>[] parameterTypes = method.getParameterTypes();

                    String completeMappingVal = null;
                    String apiOperationVal = null;

                    // 获取 method requestMapping ands ...
                    if (Objects.nonNull(methodMapping)) {
                        String methodMappingVal = Arrays.toString(methodMapping.value());
                        // TODO 1
                        completeMappingVal = controllerMappingVal + methodMappingVal;
                        completeMappingVal = completeMappingVal.replace("[", "").replace("]", "");

                        // System.out.println(" controllerMappingVal + methodMappingVal : " + completeMappingVal);
                        System.out.println();
                    }

                    // 获取 ApiOperation Value
                    if (Objects.nonNull(apiOperation)) {
                        // TODO 2
                        apiOperationVal = apiOperation.value();
                        // System.out.println(" ApiOperation: " + apiOperationVal);
                        System.out.println();
                    }

                    // 获取方法入参fields的相关信息
                    if (parameterTypes.length > 1) {
                        for (Class<?> parameterType : parameterTypes) {
                            if (!CollectionUtils.isEmpty(apiRelationModelList)) {
                                apiRelationModelList.clear();
                            }
                            apiRelationModelList = getDeclareFieldsInformation(parameterType);
                            if (CollectionUtils.isEmpty(apiRelationModelList)) {
                                continue;
                            }
                            modifyFirstElementProperties(apiRelationModelList, completeMappingVal, apiOperationVal);
                            finalApiRelationModelList.addAll(apiRelationModelList);
                        }
                    } else if (parameterTypes.length == 1) {
                        Class<?> parameterType = parameterTypes[0];
                        if (!CollectionUtils.isEmpty(apiRelationModelList)) {
                            apiRelationModelList.clear();
                        }
                        apiRelationModelList = getDeclareFieldsInformation(parameterType);
                        if (CollectionUtils.isEmpty(apiRelationModelList)) {
                            continue;
                        }
                        modifyFirstElementProperties(apiRelationModelList, completeMappingVal, apiOperationVal);
                        finalApiRelationModelList.addAll(apiRelationModelList);
                    }
                }
            }
        }

        if (CollectionUtils.isEmpty(finalApiRelationModelList))
            return finalApiRelationModelList;

        finalApiRelationModelList.forEach(item -> {
            if (Objects.isNull(item.getApiRequestMappingUrl())) {
                item.setApiRequestMappingUrl("");
            }
            if (Objects.isNull(item.getApiOperation())) {
                item.setApiOperation("");
            }
        });

        return finalApiRelationModelList;
    }

    /**
     * @return void
     * @throws
     * @description 修改列表中的第一个元素属性值
     * @params [apiRelationModelList, completeMappingVal, apiOperationVal]
     */
    private static void modifyFirstElementProperties(List<ApiRelationModel> apiRelationModelList,
                                                     String completeMappingVal, String apiOperationVal) {
        ApiRelationModel firstModel = apiRelationModelList.get(0);
        firstModel.setApiRequestMappingUrl(completeMappingVal);
        firstModel.setApiOperation(apiOperationVal);

        apiRelationModelList.set(0, firstModel);
    }

    /**
     * @return void
     * @throws
     * @description 获取Controller中方法入参fields相关信息
     * @params [parameterType]
     */
    private static List<ApiRelationModel> getDeclareFieldsInformation(Class<?> parameterType) {

        List<ApiRelationModel> apiRelationModelList = new ArrayList<>();

        Field[] declaredFields = parameterType.getDeclaredFields();
        if (declaredFields.length > 1) {
            for (Field declaredField : declaredFields) {
                NotNull notNullAnnotation = declaredField.getAnnotation(NotNull.class);
                NotBlank notBlankAnnotation = declaredField.getAnnotation(NotBlank.class);
                ApiModelProperty apiModelPropertyAnnotation = declaredField.getAnnotation(ApiModelProperty.class);

                // TODO 3
                // 属性不为空描述信息
                String notNullOrNotBlankDesc = null;
                if (Objects.nonNull(notNullAnnotation)) {
                    notNullOrNotBlankDesc = notNullAnnotation.message();
                    // System.out.println(" ==== notNullAnnotation: " + notNullAnnotation.message());
                }
                if (Objects.nonNull(notBlankAnnotation)) {
                    notNullOrNotBlankDesc = notBlankAnnotation.message();
                    // System.out.println(" ==== notBlankAnnotation: " + notBlankAnnotation.message());
                }
                // TODO 4
                // 属性描述
                String fieldDesc = null;
                if (Objects.nonNull(apiModelPropertyAnnotation)) {
                    String propertyValue = apiModelPropertyAnnotation.value();
                    String propertyNotes = apiModelPropertyAnnotation.notes();
                    fieldDesc = StringUtils.isEmpty(propertyValue) ? propertyNotes : propertyValue;
                }
                // TODO 5
                // 属性数据类型
                String fieldTypeName = declaredField.getType().getSimpleName();
                // TODO 6
                // 属性名称
                String fieldName = declaredField.getName();

                // System.out.println("fieldName: " + fieldName + " fieldType: " + fieldTypeName + " fieldDesc: " + fieldDesc + " notNullOrNotBlankDesc: " + notNullOrNotBlankDesc);

                ApiRelationModel apiRelationModel = new ApiRelationModel();
                apiRelationModel.setFieldDesc(fieldDesc);
                apiRelationModel.setFieldName(fieldName);
                apiRelationModel.setFieldType(fieldTypeName);
                apiRelationModel.setNotNullOrNotBlankDesc(notNullOrNotBlankDesc);

                apiRelationModelList.add(apiRelationModel);
            }
        }

        return apiRelationModelList;
    }

}
