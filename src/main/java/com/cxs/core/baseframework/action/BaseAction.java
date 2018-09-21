package com.cxs.core.baseframework.action;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cxs.core.baseframework.service.BaseService;

/**
 * @param <T>
 *            genericity: Specify any entity with an @Table annotation
 * @author ChenXS The superclass of the controller
 */
public class BaseAction<T> {
	private BaseService<T> baseService;

	@GetMapping("list")
	public String list() {
		return getJspPagePath() + "/list";
	}

	public String getJspPagePath() {
		RequestMapping RequestMapping = (RequestMapping) super.getClass()
				.getAnnotation(org.springframework.web.bind.annotation.RequestMapping.class);
		return RequestMapping.value()[0];
	}
}
