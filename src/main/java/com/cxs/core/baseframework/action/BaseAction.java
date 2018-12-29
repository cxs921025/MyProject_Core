package com.cxs.core.baseframework.action;

import com.cxs.core.baseframework.service.BaseService;
import com.cxs.core.exception.ServiceException;
import com.cxs.core.utils.JsonUtil;
import com.cxs.core.utils.LogUtil;
import com.cxs.core.vo.ReturnVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationObjectSupport;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param <T> genericity: Specify any entity with an @Table annotation
 * @author ChenXS The superclass of the controller
 */
@SuppressWarnings("unchecked")
public class BaseAction<T> extends WebApplicationObjectSupport {

    public BaseService getServer() {
        String className = super.getClass().getSimpleName();
        String bean = String.valueOf(Character.toLowerCase(className.charAt(0))) + className.substring(1);
        bean = bean.substring(0, bean.lastIndexOf("Action")).concat("Service");
        WebApplicationContext webApplicationContext = getWebApplicationContext();
        assert webApplicationContext != null;
        return (BaseService) webApplicationContext.getBean(bean);
    }

    /**
     * 跳转到list页面
     *
     * @return 页面地址
     */
    @GetMapping("list")
    public String list() {
        return getJspPagePath() + "/list";
    }

    /**
     * 模糊获取数据
     *
     * @param entity 查询条件
     * @return Controller层返回数据格式的对象
     */
    @PostMapping("getWithBlurry")
    @ResponseBody
    public ReturnVo getWithBlurry(T entity) {
        ReturnVo returnVo = new ReturnVo();
        try {
            List<T> list = getServer().getListByConditionWithBlurry(entity);
            Map<String, Object> attributesMap = new HashMap<>();
            attributesMap.put("result", list);
            returnVo.setAttributes(attributesMap);
        } catch (ServiceException e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("数据获取失败: " + e.getMessage());
            LogUtil.error("[" + this.getClass().toString() + "] 模糊获取失败: " + e.getMessage());
        }
        return returnVo;
    }

    /**
     * 精确获取数据
     *
     * @param entity 查询条件
     * @return Controller层返回数据格式的对象
     */
    @PostMapping("getWithExact")
    @ResponseBody
    public ReturnVo getWithExact(T entity) {
        ReturnVo returnVo = new ReturnVo();
        try {
            List<T> list = getServer().getListByConditionWithExact(entity);
            Map<String, Object> attributesMap = new HashMap<>();
            attributesMap.put("result", list);
            returnVo.setAttributes(attributesMap);
        } catch (ServiceException e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("数据获取失败: " + e.getMessage());
            LogUtil.error("[" + this.getClass().toString() + "] 精确获取失败: " + e.getMessage());
        } catch (Exception e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("系统异常");
            LogUtil.error(e);
        }
        return returnVo;
    }

    /**
     * 保存单条数据
     *
     * @param entity 待保存数据
     * @return Controller层返回数据格式的对象
     */
    @PostMapping("save")
    @ResponseBody
    public ReturnVo save(T entity) {
        ReturnVo returnVo = new ReturnVo();
        try {
            String id = this.getServer().save(entity);
            returnVo.setObj(id);
        } catch (ServiceException e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("保存失败: " + e.getMessage());
            LogUtil.error("[" + this.getClass().toString() + "] 保存失败: " + e.getMessage());
        } catch (Exception e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("系统异常");
            LogUtil.error(e);
        }
        return returnVo;
    }

    /**
     * 保存数据集合
     *
     * @param jsonStr JSON格式的数据集合
     * @return Controller层返回数据格式的对象
     */
    @PostMapping("saveInBatch")
    @ResponseBody
    public ReturnVo saveInBatch(String jsonStr) {
        ReturnVo returnVo = new ReturnVo();
        try {
            ParameterizedType superclass = (ParameterizedType) this.getClass().getGenericSuperclass();
            Type[] typeArguments = superclass.getActualTypeArguments();
            List<Object> list = JsonUtil.toBeanList(jsonStr, Class.forName(typeArguments[0].getTypeName()));
            this.getServer().saveInBatch(list);
        } catch (ServiceException e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("批量保存失败: " + e.getMessage());
            LogUtil.error("[" + this.getClass().toString() + "] 批量保存失败: " + e.getMessage());
        } catch (Exception e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("系统异常");
            LogUtil.error(e);
        }
        return returnVo;
    }

    /**
     * 更新单条数据
     *
     * @param entity 待更新数据
     * @return Controller层返回数据格式的对象
     */
    @PostMapping("update")
    @ResponseBody
    public ReturnVo update(T entity) {
        ReturnVo returnVo = new ReturnVo();
        try {
            this.getServer().update(entity);
        } catch (ServiceException e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("更新失败: " + e.getMessage());
            LogUtil.error("[" + this.getClass().toString() + "] 更新失败: " + e.getMessage());
        } catch (Exception e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("系统异常");
            LogUtil.error(e);
        }
        return returnVo;
    }

    /**
     * 更新数据集合
     *
     * @param jsonStr JSON格式的数据集合
     * @return Controller层返回数据格式的对象
     */
    @PostMapping("updateInBatch")
    @ResponseBody
    public ReturnVo updateInBatch(String jsonStr) {
        ReturnVo returnVo = new ReturnVo();
        try {
            ParameterizedType superclass = (ParameterizedType) this.getClass().getGenericSuperclass();
            Type[] typeArguments = superclass.getActualTypeArguments();
            List<Object> list = JsonUtil.toBeanList(jsonStr, Class.forName(typeArguments[0].getTypeName()));
            this.getServer().updateInBatch(list);
        } catch (ServiceException e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("批量更新失败: " + e.getMessage());
            LogUtil.error("[" + this.getClass().toString() + "] 批量更新失败: " + e.getMessage());
        } catch (Exception e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("系统异常");
            LogUtil.error(e);
        }
        return returnVo;
    }

    /**
     * 保存或更新（有id则更新，无id则保存）
     *
     * @param entity 待操作数据
     * @return Controller层返回数据格式的对象
     */
    @PostMapping("saveOrUpdate")
    @ResponseBody
    public ReturnVo saveOrUpdate(T entity) {
        ReturnVo returnVo = new ReturnVo();
        try {
            this.getServer().saveOrUpdate(entity);
        } catch (ServiceException e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("保存或更新失败: " + e.getMessage());
            LogUtil.error("[" + this.getClass().toString() + "] 保存或更新失败: " + e.getMessage());
        } catch (Exception e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("系统异常");
            LogUtil.error(e);
        }
        return returnVo;
    }

    /**
     * 批量保存或更新（有id则更新，无id则保存）
     *
     * @param jsonStr JSON格式的数据集合
     * @return Controller层返回数据格式的对象
     */
    @PostMapping("saveOrUpdateInBatch")
    @ResponseBody
    public ReturnVo saveOrUpdateInBatch(String jsonStr) {
        ReturnVo returnVo = new ReturnVo();
        try {
            ParameterizedType superclass = (ParameterizedType) this.getClass().getGenericSuperclass();
            Type[] typeArguments = superclass.getActualTypeArguments();
            List<Object> list = JsonUtil.toBeanList(jsonStr, Class.forName(typeArguments[0].getTypeName()));
            this.getServer().saveOrUpdateInBatch(list);
        } catch (ServiceException e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("批量保存或更新失败: " + e.getMessage());
            LogUtil.error("[" + this.getClass().toString() + "] 批量保存或更新失败: " + e.getMessage());
        } catch (Exception e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("系统异常");
            LogUtil.error(e);
        }
        return returnVo;
    }

    /**
     * 删除单条数据
     *
     * @param entity 待删除数据
     * @return Controller层返回数据格式的对象
     */
    @PostMapping("delete")
    @ResponseBody
    public ReturnVo delete(T entity) {
        ReturnVo returnVo = new ReturnVo();
        try {
            this.getServer().delete(entity);
        } catch (ServiceException e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("删除失败: " + e.getMessage());
            LogUtil.error("[" + this.getClass().toString() + "] 删除失败: " + e.getMessage());
        } catch (Exception e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("系统异常");
            LogUtil.error(e);
        }
        return returnVo;
    }

    /**
     * 批量删除数据
     *
     * @param ids 待删除数据多个id拼接的字符串，逗号分隔
     * @return Controller层返回数据格式的对象
     */
    @PostMapping("deleteInBatch")
    @ResponseBody
    public ReturnVo deleteInBatch(String ids) {
        ReturnVo returnVo = new ReturnVo();
        try {
            this.getServer().deleteByIdInBatch(ids);
        } catch (ServiceException e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("批量删除失败: " + e.getMessage());
            LogUtil.error("[" + this.getClass().toString() + "] 批量删除失败: " + e.getMessage());
        } catch (Exception e) {
            returnVo.setSuccess(false);
            returnVo.setMsg("系统异常");
            LogUtil.error(e);
        }
        return returnVo;
    }

    public String getJspPagePath() {
        RequestMapping requestMapping = super.getClass().getAnnotation(org.springframework.web.bind.annotation.RequestMapping.class);
        return requestMapping.value()[0];
    }
}
