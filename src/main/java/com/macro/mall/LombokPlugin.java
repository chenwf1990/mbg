package com.macro.mall;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenwf
 * @date 2020/5/21
 */
public class LombokPlugin extends PluginAdapter {
    @Resource
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //添加domain的import
        topLevelClass.addImportedType("lombok.Data");
        topLevelClass.addImportedType("javax.persistence.*");
        topLevelClass.addImportedType("org.springframework.format.annotation.DateTimeFormat");
        topLevelClass.addImportedType("io.swagger.annotations.*");

        //添加domain的注解
        topLevelClass.addAnnotation("@Data");
        topLevelClass.addAnnotation("@Entity(name = \""+introspectedTable.getTableConfiguration().getTableName() +"\")");
        topLevelClass.addAnnotation("@ApiModel(\""+introspectedTable.getTableConfiguration().getTableName() +"\")");
        return true;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }
}
