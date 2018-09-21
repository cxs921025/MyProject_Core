package com.cxs.core.baseframework.model;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author ChenXS The superclass of the entity
 */
@Data
public class BaseModel implements Serializable {
	/**
	 * 创建人
	 */
	@Column(name = "CREATE_USER")
	private String createUser;
	/**
	 * 创建时间
	 */
	@Column(name = "CREATE_DATE")
	private String createDate;
	/**
	 * 更新人
	 */
	@Column(name = "UPDATE_USER")
	private String updateUser;
	/**
	 * 更新时间
	 */
	@Column(name = "UPDATE_DATE")
	private String updateDate;
}
