package com.cxs.core.baseframework.mapper;

import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @param <T>
 * @author ChenXS
 * The superinterface of mapper interface, which provides some basic database operations
 */
@Service
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T>, BaseSelectMapper<T> {

}
