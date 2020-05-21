package com.macro.mall;

import org.mybatis.generator.api.IntrospectedTable;

/**
 * @author chenwf
 * @date 2020/3/13
 */
public class BaseGenTool {
    /**
     * 判断是不是Mybatis3运行生成的.
     *
     * @param introspectedTable the introspected table
     * @return the boolean
     */
    public static boolean isMybatisMode(IntrospectedTable introspectedTable){
        if (introspectedTable.getTargetRuntime().equals(IntrospectedTable.TargetRuntime.MYBATIS3)) {
            return true;
        }
        return false;
    }
}
