package com.hbird.base.util;

import java.lang.reflect.Field;

/**
 * Created by Liul on 2018/9/4.
 */

public class SQLUtils {
    /**
     * 构建更新sql
     *根据实体类生成sql
     * @param entity
     * @return
     */
    public static String buildUpdateSqlById(String tableName, Object entity) {
        StringBuilder sql = new StringBuilder();
        String primaryName = "id";
        String primaryValue = "";
        sql.append("update ");
        sql.append(tableName);
        sql.append(" set ");
        //获取对象属性值
        Field[] fields = entity.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            // 对于每个属性，获取属性名
            String varName = fields[i].getName();
            varName= UnderLine2Name.underscoreName(varName);
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o;
                try {
                    o = fields[i].get(entity);
                    if (o == null) {
                        continue;
                    }
                    if (primaryName.equalsIgnoreCase(varName)) {
                        primaryValue = o.toString();
                    }
                    sql.append(varName);
                    sql.append(" = '");
                    sql.append(o.toString());
                    sql.append("',");
                    System.err.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" where ");
        sql.append(primaryName);
        sql.append(" = '" + primaryValue+"'");
        System.out.println("结果==============" + sql.toString());
        return sql.toString();
    }

}
