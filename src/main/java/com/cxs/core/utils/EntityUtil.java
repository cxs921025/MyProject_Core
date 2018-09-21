package com.cxs.core.utils;

import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * @author ChenXS
 * Entity tool class.
 */
public class EntityUtil {

    /**
     * 不允许实例化
     */
    private EntityUtil() {
    }

    /**
     * 根据属性名获取实体属性值
     *
     * @param entity
     * @param fieldName
     * @return
     */
    public static Object getValueWithField(Object entity, String fieldName) {
        MethodAccess methodAccess = MethodAccess.get(entity.getClass());
        Object value = null;
        try {
            if (fieldName != null) {
                fieldName = String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
                int nameIndex = methodAccess.getIndex("get" + fieldName);
                value = methodAccess.invoke(entity, nameIndex);
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
        }
        return value;
    }

    /**
     * 根据属性名设置属性值
     *
     * @param entity
     * @param fieldName
     * @param value
     * @param <T>
     */
    public static <T> void setValueWithField(Object entity, String fieldName, Object value) {
        MethodAccess methodAccess = MethodAccess.get(entity.getClass());
        try {
            if (fieldName != null) {
                fieldName = String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
                int nameIndex = methodAccess.getIndex("set" + fieldName);
                methodAccess.invoke(entity, nameIndex, value);
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
        }
    }
}
