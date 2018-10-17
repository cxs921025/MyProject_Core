package com.cxs.core.baseframework.mapper;

import com.cxs.core.provider.BaseSelectProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @param <T>
 * @author ChenXS
 * Provide dynamic Sql.
 */
public interface BaseSelectMapper<T> {
    @SelectProvider(type = BaseSelectProvider.class, method = "dynamicSQL")
    List<T> selectByIds(String ids);
}
