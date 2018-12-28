package com.cxs.core.baseframework.mapper;

import com.cxs.core.builder.SimpleSqlBuilder;
import com.cxs.core.exception.ServiceException;
import com.cxs.core.utils.LogUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @param <T>
 * @author ChenXS
 * Manipulating the database using the JDBCTemplate schema
 */
@SuppressWarnings("unchecked")
public class SqlMapper<T> implements InitializingBean {
    @Autowired
    private JdbcTemplate jdbcTemplate;
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
     * @param entityClass 实体类
     */
    private void init(Class<T> entityClass) {
        this.simpleSqlBuilder = new SimpleSqlBuilder<>(entityClass);
    }

    /**
     * 批量更新
     *
     * @param entityList 待更新实体集合
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
            this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(Objects.requireNonNull(this.jdbcTemplate.getDataSource()));
            this.namedParameterJdbcTemplate.batchUpdate(this.simpleSqlBuilder.getUpdateSql(batchArgs), batchArgs);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 批量保存
     */
    public void saveList(List<T> entityList, Object[] ids) {
        if (CollectionUtils.isEmpty(entityList))
            return;
        try {
            int entityCount = entityList.size();
            int idCount = ids.length;
            if (idCount > entityCount) {
                idCount = entityCount;
            }
            this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(Objects.requireNonNull(this.jdbcTemplate.getDataSource()));
            List<T> withIdEntitys = entityList.subList(0, idCount);
            List<T> withoutIdEntitys = entityList.subList(idCount, entityCount);
            if (!withIdEntitys.isEmpty()) {
                Map[] batchArgs = new Map[idCount];
                for (int i = 0; i < idCount; ++i) {
                    T entity = withIdEntitys.get(i);
                    batchArgs[i] = this.simpleSqlBuilder.getAllSqlParameter(entity);
                    batchArgs[i].put(this.simpleSqlBuilder.getIdField(), ids[i]);
                }
                this.namedParameterJdbcTemplate.batchUpdate(this.simpleSqlBuilder.getIncludeIdFieldInsertSql(),
                        batchArgs);
            }
            if (withoutIdEntitys.isEmpty())
                return;
            Map[] batchArgs = new Map[withoutIdEntitys.size()];
            for (int i = 0; i < batchArgs.length; ++i) {
                T entity = withoutIdEntitys.get(i);
                batchArgs[i] = this.simpleSqlBuilder.getAllSqlParameter(entity);
            }
            this.namedParameterJdbcTemplate.batchUpdate(this.simpleSqlBuilder.getIncludeIdFieldInsertSql(), batchArgs);
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 批量删除
     *
     * @param ids 要删除的id数组
     */
    public void deleteByIdBySql(String[] ids) {
        if ((ids == null) || (ids.length == 0)) return;
        try {
            this.jdbcTemplate.execute(this.simpleSqlBuilder.getDeleteSql(ids));
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void afterPropertiesSet() {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(Objects.requireNonNull(this.jdbcTemplate.getDataSource()));
    }
}
