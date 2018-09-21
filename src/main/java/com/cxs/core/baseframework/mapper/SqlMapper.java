package com.cxs.core.baseframework.mapper;

import com.cxs.core.builder.SimpleSqlBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @param <T>
 * @author ChenXS
 * Manipulating the database using the JDBCTemplate schema
 */
public class SqlMapper<T> implements InitializingBean{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private Class<T> entityClass;
    private SimpleSqlBuilder<T> simpleSqlBuilder;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SqlMapper() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] params = parameterizedType.getActualTypeArguments();

        Class entityClass = (Class) params[0];
        init(entityClass);
    }

    public SqlMapper(Class<T> entityClass) {
        init(entityClass);
    }

    /**
     * 初始化SqlMapper
     *
     * @param entityClass
     */
    private void init(Class<T> entityClass) {
        this.entityClass = entityClass;

        this.simpleSqlBuilder = new SimpleSqlBuilder(entityClass);
    }

    /**
     * 批量更新
     *
     * @param entityList
     */
    public void updateList(List<T> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return;
        }
        try {
            Map[] batchArgs = new Map[entityList.size()];
            for (int i = 0; i < batchArgs.length; ++i) {
                batchArgs[i] = this.simpleSqlBuilder.getAllSqlParameter(entityList.get(i));
            }
            this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
            this.namedParameterJdbcTemplate.batchUpdate(this.simpleSqlBuilder.getUpdateSql(batchArgs), batchArgs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    public void deleteByIdBySql(String[] ids) {
        if ((ids == null) || (ids.length == 0)) return;
        try {
            this.jdbcTemplate.execute(this.simpleSqlBuilder.getDeleteSql(ids));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
    }
}
