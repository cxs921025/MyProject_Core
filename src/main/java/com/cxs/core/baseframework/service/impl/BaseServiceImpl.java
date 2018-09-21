package com.cxs.core.baseframework.service.impl;

import com.cxs.core.baseframework.mapper.BaseMapper;
import com.cxs.core.baseframework.mapper.SqlMapper;
import com.cxs.core.baseframework.service.BaseService;
import com.cxs.core.exception.ServiceException;
import com.cxs.core.utils.BaseUtil;
import com.cxs.core.utils.CriteriaUtil;
import com.cxs.core.utils.EntityUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.util.*;

/**
 * @param <T>
 * @author ChenXS The implementation class of BaseService
 */
public class BaseServiceImpl<T> extends SqlMapper<T> implements BaseService<T> {

	@Autowired
	private BaseMapper<T> mapper;

	@Override
	public T get(T query) throws ServiceException {
		Assert.notNull(query, "错误信息：参数为null");
		try {
			return this.mapper.selectOne(query);
		} catch (TooManyResultsException e) {
			throw new ServiceException("查询结果过多：" + e);
		} catch (RuntimeException e) {
			throw new ServiceException("单条查询出错：" + e);
		}
	}

	@Override
	public T getById(String id) throws ServiceException {
		if (StringUtils.isBlank(id))
			throw new ServiceException("查询出错：Id为空");
		try {
			return this.mapper.selectByPrimaryKey(id);
		} catch (TooManyResultsException e) {
			throw new ServiceException("查询结果过多：" + e);
		} catch (RuntimeException e) {
			throw new ServiceException("根据ID查询出错：" + e);
		}
	}

	@Override
	public List<T> getListByConditionWithExact(T query) throws ServiceException {
		Assert.notNull(query, "出错信息：参数为null");
		try {
			return this.mapper.select(query);
		} catch (RuntimeException e) {
			throw new ServiceException("数据查询出错：" + e);
		}
	}

	@Override
	public List<T> getListByConditionWithBlurry(T query) throws ServiceException {
		Assert.notNull(query, "出错信息：参数为null");
		try {
			Example e = CriteriaUtil.setFieldQueryLikeCondtion(query);
			return this.mapper.selectByExample(e);
		} catch (RuntimeException e) {
			throw new ServiceException("数据查询出错：" + e);
		}
	}

	@Override
	public List<T> getListByExample(Example example) throws ServiceException {
		Assert.notNull(example, "出错信息：参数为null");
		try {
			return this.mapper.selectByExample(example);
		} catch (RuntimeException e) {
			throw new ServiceException("数据查询出错：" + e);
		}
	}

	@Override
	public List<T> getByIds(String ids) throws ServiceException {
		if (StringUtils.isBlank(ids))
			return new ArrayList<>();
		try {
			String newIds = formatMapperIds(Arrays.asList(ids.split(",")));
			return this.mapper.selectByIds(newIds.substring(0, newIds.length() - 1));
		} catch (RuntimeException e) {
			throw new ServiceException("数据查询出错：" + e);
		}
	}

	@Override
	public List<T> getByIds(List<String> ids) throws ServiceException {
		if (CollectionUtils.isEmpty(ids))
			return new ArrayList<>();
		try {
			String newIds = formatMapperIds(ids);
			return this.mapper.selectByIds(newIds.substring(0, newIds.length() - 1));
		} catch (RuntimeException e) {
			throw new ServiceException("数据查询出错：" + e);
		}
	}

	@Override
	public List<T> getByIds(Set<String> ids) throws ServiceException {
		if (CollectionUtils.isEmpty(ids))
			return new ArrayList<>();
		try {
			String newIds = formatMapperIds(ids);
			return this.mapper.selectByIds(newIds.substring(0, newIds.length() - 1));
		} catch (RuntimeException e) {
			throw new ServiceException("数据查询出错：" + e);
		}
	}

	@Override
	public List<T> getAll() throws ServiceException {
		try {
			return this.mapper.selectAll();
		} catch (RuntimeException e) {
			throw new ServiceException("数据查询出错：" + e);
		}
	}

	@Override
	public int getCount(T query) throws ServiceException {
		Assert.notNull(query, "出错信息：参数为null");
		try {
			return this.mapper.selectCount(query);
		} catch (RuntimeException e) {
			throw new ServiceException("查询总数出错：" + e);
		}
	}

	@Override
	public int getCountByCondition(T query) throws ServiceException {
		Assert.notNull(query, "出错信息：参数为null");
		try {
			Example example = CriteriaUtil.setFieldQueryLikeCondtion(query);
			return this.mapper.selectCountByExample(example);
		} catch (RuntimeException e) {
			throw new ServiceException("查询总数出错：" + e);
		}
	}

	@Override
	public String save(T entity) throws ServiceException {
		Assert.notNull(entity, "出错信息：参数为null");
		try {
			Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entity.getClass());
			EntityColumn pkColumn = pkColumns.iterator().next();
			Object valueWithField = EntityUtil.getValueWithField(entity, pkColumn.getProperty());
			String uuid="";
			if (valueWithField == null || StringUtils.isNotBlank(valueWithField.toString())) {
				uuid = BaseUtil.getUUID();
				EntityUtil.setValueWithField(entity, pkColumn.getProperty(), uuid);
			} else {
				uuid = EntityUtil.getValueWithField(entity, pkColumn.getProperty()).toString();
			}

			this.mapper.insertSelective(entity);
			return uuid;
		} catch (RuntimeException e) {
			throw new ServiceException("插入出错：" + e);
		}
	}

	@Override
	public int saveInBatch(List<T> entitys) throws ServiceException {
		if (CollectionUtils.isEmpty(entitys))
			return -1;
		try {
			// 利用反射清除id的值
			for (T entity : entitys) {
				Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entity.getClass());
				EntityColumn pkColumn = pkColumns.iterator().next();
				if (StringUtils.isNotBlank(EntityUtil.getValueWithField(entity, pkColumn.getProperty()).toString())) {
					EntityUtil.setValueWithField(entity, pkColumn.getProperty(), 0L);
				}
			}
			return this.mapper.insertList(entitys);
		} catch (RuntimeException e) {
			throw new ServiceException("批量插入出错：" + e);
		}
	}

	@Override
	public int update(T entity) throws ServiceException {
		Assert.notNull(entity, "出错信息：参数为null");
		try {
			return this.mapper.updateByPrimaryKeySelective(entity);
		} catch (RuntimeException e) {
			throw new ServiceException("更新出错：" + e);
		}
	}

	@Override
	public void updateInBatch(List<T> entitys) throws ServiceException {
		if (CollectionUtils.isEmpty(entitys))
			return;
		try {
			updateList(entitys);
		} catch (RuntimeException e) {
			throw new ServiceException("批量更新出错：" + e);
		}
	}

	@Override
	public int saveOrUpdate(T entity) throws ServiceException {
		Assert.notNull(entity, "出错信息：参数为null");
		int effectRow = 0;
		try {
			if (BaseUtil.isIdNull(entity)) {
				effectRow = this.mapper.insertSelective(entity);
			} else {
				effectRow = this.mapper.updateByPrimaryKeySelective(entity);
			}
		} catch (RuntimeException e) {
			throw new ServiceException("保存或更新出错：" + e);
		}
		return effectRow;
	}

	@Override
	public int saveOrUpdateInBatch(List<T> entitys) throws ServiceException {
		if (CollectionUtils.isEmpty(entitys))
			return -1;
		List saves = new ArrayList();
		List updates = new ArrayList();
		try {
			for (Object entity : entitys) {
				if (BaseUtil.isIdNull(entity)) {
					saves.add(entity);
				} else {
					updates.add(entity);
				}
			}
			this.mapper.insertList(saves);
			updateList(updates);
		} catch (Exception e) {
			throw new ServiceException("批量保存或更新出错：" + e);
		}
		return entitys.size();
	}

	@Override
	public int delete(T entity) throws ServiceException {
		Assert.notNull(entity, "出错信息：参数为null");
		try {
			return this.mapper.delete(entity);
		} catch (RuntimeException e) {
			throw new ServiceException("删除出错：" + e);
		}
	}

	@Override
	public int deleteById(String id) throws ServiceException {
		if (StringUtils.isBlank(id))
			throw new ServiceException("删除出错：Id为空");
		try {
			return this.mapper.deleteByPrimaryKey(id);
		} catch (RuntimeException e) {
			throw new ServiceException("删除出错：" + e);
		}
	}

	@Override
	public void deleteByIdInBatch(String ids) throws ServiceException {
		if (StringUtils.isBlank(ids))
			throw new ServiceException("删除出错：Id为空");
		try {
			deleteByIdBySql(ids.split(","));
		} catch (RuntimeException e) {
			throw new ServiceException("批量删除出错：" + e);
		}
	}

	@Override
	public void deleteByIdInBatch(List<String> ids) throws ServiceException {
		if (CollectionUtils.isEmpty(ids))
			throw new ServiceException("删除出错：Id为空");
		try {
			String[] id = new String[ids.size()];
			ids.toArray(id);
			deleteByIdBySql(id);
		} catch (RuntimeException e) {
			throw new ServiceException("批量删除出错：" + e);
		}
	}

	/**
	 * 重新拼装为Mapper可用的id字符串
	 *
	 * @param c
	 *            集合对象
	 * @return
	 */
	private String formatMapperIds(Collection c) {
		String idsStr;
		StringBuilder sb = new StringBuilder();
		for (Object id : c) {
			String s = id.toString();
			sb.append("'").append(s).append("'").append(",");
		}
		idsStr = sb.toString();
		return idsStr;
	}
}
