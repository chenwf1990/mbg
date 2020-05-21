package com.macro.mall;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import java.util.List;

public class BatchInsertPlugin extends PluginAdapter {

//    private final static String BATCH_INSERT = "batchInsert";

    private final static String BATCH_INSERT_SELECTIVE = "batchInsertSelective";

    private final static String BATCH_DELETE = "batchDelete";

    private final static String PARAMETER_NAME = "recordList";

    @Override
    public boolean validate(List<String> list) {
        return true;
    }


    /**
     * java代码Mapper生成
     *
     * @param interfaze
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientGenerated(Interface interfaze,
                                   TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (BaseGenTool.isMybatisMode(introspectedTable)) {
            //生成batchInsert 和 batchInsertSelective的java方法
            MethodGeneratorTool.defaultBatchInsertOrUpdateMethodGen(MethodGeneratorTool.INSERT, interfaze, introspectedTable, context);
        }
        topLevelClass.addAnnotation("@Data");
        return super.clientGenerated(interfaze, topLevelClass,
                introspectedTable);
    }

    /**
     * sqlMapper生成
     *
     * @param document
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document,
                                           IntrospectedTable introspectedTable) {
        if (BaseGenTool.isMybatisMode(introspectedTable)) {
            //生成batchInsert 和 batchInsertSelective的java方法
            addSqlMapper(document, introspectedTable);
        }
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    /**
     * batchInsert和batchInsertSelective的SQL生成
     *
     * @param document
     * @param introspectedTable
     */
    private void addSqlMapper(Document document, IntrospectedTable introspectedTable) {
        //table名名字
        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        //column信息
        List<IntrospectedColumn> columnList = introspectedTable.getAllColumns();

        XmlElement baseElement = SqlMapperGeneratorTool.baseElementGenerator(SqlMapperGeneratorTool.INSERT,
                BATCH_INSERT_SELECTIVE,
                FullyQualifiedJavaType.getNewListInstance());

        XmlElement foreachElement = SqlMapperGeneratorTool.baseForeachElementGenerator(PARAMETER_NAME,
                "item",
                "index",
                ",");
        baseElement.addElement(new TextElement(String.format("insert into %s", tableName)));
        baseElement.addElement(new TextElement("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >"));

        foreachElement.addElement(new TextElement("<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >"));

        for (int i = 0; i < columnList.size(); i++) {
            String columnInfo = "";
            String valueInfo = "";
            IntrospectedColumn introspectedColumn = columnList.get(i);
            if (introspectedColumn.isIdentity()) {
                continue;
            }

            columnInfo = introspectedColumn.getActualColumnName();
            columnInfo = "<if test=\""+introspectedColumn.getJavaProperty()+"!= null\">\n        "+ columnInfo;
            columnInfo += (",\n");
            columnInfo += "    </if>";

            valueInfo += MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "item.");
            valueInfo = "<if test=\""+introspectedColumn.getJavaProperty()+"!= null\">\n          "+ valueInfo;
            valueInfo += (",\n");
            valueInfo += "      </if>";

            baseElement.addElement(new TextElement(columnInfo));
            foreachElement.addElement(new TextElement(valueInfo));

        }
        foreachElement.addElement(new TextElement("</trim>"));

        baseElement.addElement(new TextElement("</trim>"));

        baseElement.addElement(foreachElement);

        //3.parent Add
        document.getRootElement().addElement(baseElement);
    }

}
