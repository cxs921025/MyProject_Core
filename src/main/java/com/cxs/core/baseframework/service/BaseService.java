package com.cxs.core.baseframework.service;

import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;

/**
 * @param <T> genericity: Specify any entity with an @Table annotation
 * @author ChenXS
 * The superclass of the service class, which provides some basic data operations
 * .e.g: select, save, update, delete
 */
@SuppressWarnings("unused")
public interface BaseService<T> {
    /**
     * 根据参数实体中提供的条件查询数据库中对应的一条数据
     *
     * @param query 参数实体
     * @return 数据库中的一条数据, 若无返回null
     */
    T get(T query);

    /**
     * 根据id获取数据库中对应的一条数据
     *
     * @param id 数据id
     * @return 数据库中的一条数据, 若无返回null
     */
    T getById(String id);

    /**
     * 根据多个id拼装的字符串获取数据库中对应的多条数据
     *
     * @param ids 多个id拼装的字符串，',' 分割
     * @return 数据库中的多条数据, 若无返回空的List集合
     */
    List<T> getByIds(String ids);

    /**
     * 根据存储id的List集合获取数据库中对应的多条数据
     *
     * @param ids 多个id的List集合
     * @return 数据库中的多条数据, 若无返回空的List集合
     */
    List<T> getByIds(List<String> ids);

    /**
     * 根据存储id的Set集合获取数据库中对应的多条数据
     *
     * @param ids 多个id的Set集合
     * @return 数据库中的多条数据, 若无返回空的List集合
     */
    List<T> getByIds(Set<String> ids);

    /**
     * 根据参数实体中提供的条件精确查询多条数据
     *
     * @param query 参数实体
     * @return 数据库中的多条数据集合, 若无返回空的List集合
     */
    List<T> getListByConditionWithExact(T query);

    /**
     * 根据参数实体中提供的条件模糊查询多条数据
     *
     * @param query 参数实体
     * @return 数据库中的多条数据集合, 若无返回空的List集合
     */
    List<T> getListByConditionWithBlurry(T query);

    /**
     * 根据MyBatis Example对象查询多条数据
     *
     * @param example Example对象
     * @return 数据库中的多条数据集合, 若无返回空的List集合
     */
    List<T> getListByExample(Example example);

    /**
     * 查询数据库表中所有的数据
     *
     * @return 数据库表中所有数据集合, 若无返回空的List集合
     */
    List<T> getAll();

    /**
     * 根据参数实体中提供的条件精确查询数据库中的数据总数
     *
     * @param query 实体参数
     * @return 数据库中数据总数
     */
    int getCount(T query);

    /**
     * 根据参数实体中提供的条件模糊查询数据库中的数据总数
     *
     * @param query 实体参数
     * @return 数据库中数据总数
     */
    int getCountByCondition(T query);

    /**
     * 将参数实体持久化到数据库中
     *
     * @param entity 待保存实体
     * @return 保存后数据的主键
     */
    String save(T entity);

    /**
     * 将参数实体集合持久化到数据库中
     *
     * @param entitys 待保存实体集合
     */
    void saveInBatch(List<T> entitys);

    /**
     * 更新数据库中参数实体对应id的数据
     *
     * @param entity 待更新实体
     * @return 操作影响数据库的行数
     */
    int update(T entity);

    /**
     * 更新数据库中所有参数实体对应id的数据
     *
     * @param entitys 待更新实体集合
     */
    void updateInBatch(List<T> entitys);

    /**
     * 更新或保存数据库中数据
     * 若参数实体有id则更新
     * 若没有则保存
     *
     * @param entity 待保存或更新实体
     * @return 数据id
     */
    String saveOrUpdate(T entity);

    /**
     * 批量更新或保存数据库中数据
     * 若参数集合实体中有id则更新
     * 若没有则保存
     *
     * @param entitys 待保存或更新实体集合
     * @return 操作影响数据库的行数 -1为保存或更新出错
     */
    int saveOrUpdateInBatch(List<T> entitys);

    /**
     * 根据实体参数删除数据库中数据
     *
     * @param entity 参数实体
     * @return 操作影响数据库的行数 -1位删除出错
     */
    int delete(T entity);

    /**
     * 根据id 删除数据库中数据
     *
     * @param id 待删除的数据ID
     * @return 操作影响数据库的行数 -1位删除出错
     */
    int deleteById(String id);

    /**
     * 根据拼装的多个id字符串删除数据库中数据
     *
     * @param ids 拼装的多个id字符串，','分割
     */
    void deleteByIdInBatch(String ids);

    /**
     * 根据id集合删除数据库中数据
     *
     * @param ids 存放多个id的List集合
     */
    void deleteByIdInBatch(List<String> ids);
}
