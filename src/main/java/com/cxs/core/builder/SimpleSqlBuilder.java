package com.cxs.core.builder;

import com.cxs.core.utils.LogUtil;
import com.cxs.core.utils.ObjectUtil;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.util.StringUtil;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.beans.PropertyDescriptor;
import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provide the SQL statements required by JDBCTemlpate.
 *
 * @param <T>
 * @author ChenXS
 */
@SuppressWarnings("unchecked")
public class SimpleSqlBuilder<T> {
    private String tableName;
    private String idField;
    private Map<String, String> fieldColumnMapping;
    private Class<T> entityClass;

    public SimpleSqlBuilder(Class<T> entityClass) {
        init(entityClass);
    }

    /**
     * 初始化SimpleSqlBuilder
     *
     * @param entityClass 实体类
     */
    private void init(Class<T> entityClass) {
        this.entityClass = entityClass;
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = entityClass.getAnnotation(Table.class);
            if (!(table.name().equals(""))) {
                this.tableName = table.name();
            }
        }

        setTableName(this.tableName);
        List columnFields = ObjectUtil.getFieldsByAnnotation(this.entityClass, Transient.class);
        if (CollectionUtils.isEmpty(columnFields)) {
            return;
        }

        Map mapping = new HashMap();
        for (Object f : columnFields) {
            Field field = (Field) f;
            if (field.isAnnotationPresent(Transient.class))
                continue;
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (field.isAnnotationPresent(Id.class)) {
                Id id = field.getAnnotation(Id.class);
                if (id != null) {
                    setIdField(field.getName());
                }
            }
            Column column = field.getAnnotation(Column.class);
            String name;
            if (column != null) {
                name = column.name();
            } else {
                name = field.getName();
                name = StringUtil.camelhumpToUnderline(name);
            }
            mapping.put(field.getName(), name);
        }
        setFieldColumnMapping(mapping);
    }

    /**
     * 获取实体中所有的数据库表字段以及对应值
     *
     * @param entity 待操作实体
     * @return map
     */
    public Map<String, Object> getAllSqlParameter(T entity) {
        return getSqlParameters(entity);
    }

    /**
     * 获取实体中所有的数据库表字段以及对应值
     *
     * @param entity 待操作实体
     * @return map
     */
    private Map<String, Object> getSqlParameters(T entity) {
        Map params = new HashMap();
        for (String field : this.fieldColumnMapping.keySet()) {
            try {
                Object value = new PropertyDescriptor(
                        field, this.entityClass).getReadMethod().invoke(entity);
                if (value == null) {
                    params.put(field, null);
                    continue;
                }
                params.put(field, value);
            } catch (Exception e) {
                throw new RuntimeException("getSqlParameters exception!", e);
            }
        }
        return params;
    }

    /**
     * 获取JDBCTemplate所需的更新语句
     *
     * @param batchArgs batchArgs
     * @return String
     */
    public String getUpdateSql(Map<String, Object>[] batchArgs) {
        Map mapArgs = new HashMap();
        for (Map map : batchArgs) {
            for (Object entry : map.entrySet()) {
                Map.Entry e = (Map.Entry) entry;
                if (e.getValue() != null) {
                    mapArgs.put(e.getKey(), e.getValue());
                }
            }
        }
        return getUpdateSql(this.fieldColumnMapping.keySet(), mapArgs);
    }

    /**
     * 获取JDBCTemplate所需的更新语句
     *
     * @param fieldSet fieldSet
     * @param mapArgs  mapArgs
     * @return String
     */
    public String getUpdateSql(Set<String> fieldSet, Map<String, Object> mapArgs) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(this.tableName).append(" SET");
        for (String field : fieldSet) {
            if (field.equalsIgnoreCase(this.idField)) {
                continue;
            }
            for (Map.Entry entry : mapArgs.entrySet()) {
                if (this.fieldColumnMapping.get(field).equalsIgnoreCase(StringUtil.camelhumpToUnderline((String) entry.getKey()))) {
                    sb.append(" ").append(this.fieldColumnMapping.get(field)).append(" =:").append(field).append(",");
                }
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" WHERE ").append(this.fieldColumnMapping.get(this.idField)).append(" =:").append(this.idField);
        return sb.toString();
    }

    /**
     * 获取JDBCTemplate所需的删除语句
     *
     * @param ids ids
     * @return String
     */
    public String getDeleteSql(String[] ids) {
        String condtion = getSqlByArray(ids, this.fieldColumnMapping.get(this.idField));
        return "DELETE FROM " + this.tableName + " WHERE " + condtion;
    }

    /**
     * 根据数组获取sql语句
     *
     * @param strArry    strArry
     * @param columnName columnName
     * @return String
     */
    public static String getSqlByArray(String[] strArry, String columnName) {
        StringBuilder sql = new StringBuilder();
        if (strArry != null) {
            sql.append(columnName).append(" IN ( ");
            for (int i = 0; i < strArry.length; ++i) {
                sql.append("'").append(strArry[i]).append("',");
                if (((i + 1) % 1000 == 0) && (i + 1 < strArry.length)) {
                    sql.deleteCharAt(sql.length() - 1);
                    sql.append(" ) OR ").append(columnName).append(" IN (");
                }
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(" )");
        }
        return sql.toString();
    }

    public String getIncludeIdFieldInsertSql() {
        return getInsertSql(this.fieldColumnMapping.keySet());
    }

    private String getInsertSql(Set<String> fieldSet) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        sb.append("INSERT INTO ").append(this.tableName).append("(");
        for (String field : fieldSet) {
            sb.append(this.fieldColumnMapping.get(field)).append(", ");
            sb2.append(":").append(field).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb2.delete(sb2.length() - 2, sb2.length());
        sb.append(") VALUES(");
        sb.append(sb2);
        sb.append(")");
        String sql = sb.toString();
        LogUtil.info(sql);
        return sql;
    }

    private void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIdField() {
        return this.idField;
    }

    private void setIdField(String idField) {
        this.idField = idField;
    }

    private void setFieldColumnMapping(Map<String, String> fieldColumnMapping) {
        this.fieldColumnMapping = fieldColumnMapping;
    }
}