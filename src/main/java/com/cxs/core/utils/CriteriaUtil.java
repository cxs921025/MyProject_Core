package com.cxs.core.utils;

import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author ChenXS
 * Criteria tool class.
 */
public class CriteriaUtil {
    /**
     * 不允许实例化
     */
    private CriteriaUtil() {
    }

    /**
     * 设置参数实体所有字段为like查询
     *
     * @param entity
     * @return
     */
    public static Example setFieldQueryLikeCondtion(Object entity) {
        Example example = getExample(entity);
        Example.Criteria criteria = getCriteria(example);
        Field[] fileds = entity.getClass().getDeclaredFields();
        Field[] superFields = entity.getClass().getSuperclass().getDeclaredFields();
        for (Field field : superFields) {
            if (filterQueryField(field)) {
                continue;
            }
            Object fieldValue = EntityUtil.getValueWithField(entity, field.getName());
            if (StringUtils.isBlank(fieldValue.toString())) {
                continue;
            }
            criteria.andLike(field.getName(), "%" + StringUtils.trim(fieldValue.toString()) + "%");
        }
        for (Field field : fileds) {
            if (filterQueryField(field)) {
                continue;
            }
            Object fieldValue = EntityUtil.getValueWithField(entity, field.getName());
            fieldValue = fieldValue == null ? "" : fieldValue;
            if (StringUtils.isBlank(fieldValue.toString())) {
                continue;
            }
            criteria.andLike(field.getName(), "%" + StringUtils.trim(fieldValue.toString()) + "%");
        }
        return example;
    }

    /**
     * 判断是否过滤某些字段
     * 过滤字段为带有Transient注解 和 static标识的字段
     *
     * @param field
     * @return
     */
    private static boolean filterQueryField(Field field) {
        return ((field.isAnnotationPresent(Transient.class)) || (Modifier.isStatic(field.getModifiers())));
    }

    public static Example.Criteria getCriteria(Example example) {
        return example.createCriteria();
    }

    public static Example getExample(Object entity) {
        return new Example(entity.getClass());
    }

}