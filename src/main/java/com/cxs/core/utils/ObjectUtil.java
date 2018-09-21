package com.cxs.core.utils;

import org.springframework.core.convert.converter.Converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenXS
 * Object tool class
 */
public class ObjectUtil {
    public static final List<Converter<String, ? extends Object>> CONVERTERS = new ArrayList<Converter<String, ? extends Object>>();

    /**
     * 不允许实例化
     */
    private ObjectUtil() {
    }

    /**
     * 根据注解类获取获取对象类中属性集合
     *
     * @param clazz
     * @param atClass
     * @return
     */
    public static List<Field> getFieldsByAnnotation(Class<?> clazz, Class<? extends Annotation> atClass) {
        List list = getAllField(clazz);
        List ret = new ArrayList();
        for (Object field : list) {
            ret.add((Field) field);
        }
        return list;
    }

    /**
     * 判断两个类是否是继承关系
     *
     * @param clazz
     * @param superClass
     * @return
     */
    public static boolean isExtends(Class<?> clazz, Class<?> superClass) {
        if (clazz == superClass) {
            return true;
        }
        Class _class = clazz.getSuperclass();
        while (_class != null) {
            if (_class == superClass) {
                return true;
            }
            _class = _class.getSuperclass();
        }
        return false;
    }

    /**
     * 判断两个类是否是实现关系
     *
     * @param clazz
     * @param interfaceClass
     * @return
     */
    public static boolean isImplement(Class<?> clazz, Class<?> interfaceClass) {
        if (!(interfaceClass.isInterface())) {
            return false;
        }
        if (clazz == interfaceClass) {
            return true;
        }
        List interfaces = new ArrayList();
        if (clazz.isInterface())
            interfaces.addAll(getSuperInterfaces(clazz));
        else {
            interfaces.addAll(getClassInterfaces(clazz));
        }
        return interfaces.contains(interfaceClass);
    }

    /**
     * 获取类中所有public属性集合
     *
     * @param clazz
     * @return
     */
    public static List<Field> getAllField(Class<?> clazz) {
        List list = new ArrayList();
        for (Field field : clazz.getDeclaredFields()) {
            list.add(field);
        }
        Class _clazz = clazz.getSuperclass();
        while (_clazz != null) {
            list.addAll(getAllField(_clazz));
            _clazz = _clazz.getSuperclass();
        }
        return list;
    }

    /**
     * 获取类中所有属性集合
     *
     * @param clazz
     * @return
     */
    public static List<Field> getAllDeclareFields(Class<?> clazz) {
        List list = new ArrayList();
        for (Field field : clazz.getDeclaredFields()) {
            list.add(field);
        }
        return list;
    }

    /**
     * 将一个对象序列化为字节数组
     *
     * @param obj
     * @return
     */
    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object unSerialize(byte[] bytes) {
        Object obj = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Converter<String, ? extends Object> getConverter(Class<? extends Object> type) {
        for (Converter converter : CONVERTERS) {
            ParameterizedType parameterizedType = (ParameterizedType) converter.getClass().getGenericInterfaces()[0];
            Type[] params = parameterizedType.getActualTypeArguments();

            Class _type = (Class) params[1];
            if (isExtends(type, _type)) {
                return converter;
            }
        }
        return null;
    }

    private static List<Class<?>> getClassInterfaces(Class<?> clazz) {
        List interfaces = new ArrayList();
        for (Class interface0 : clazz.getInterfaces()) {
            interfaces.addAll(getSuperInterfaces(interface0));
        }
        Class class0 = clazz.getSuperclass();
        while (class0 != null) {
            interfaces.addAll(getClassInterfaces(class0));
            class0 = class0.getSuperclass();
        }
        return interfaces;
    }

    private static List<Class<?>> getSuperInterfaces(Class<?> interface0) {
        List interfaces = new ArrayList();
        interfaces.add(interface0);
        for (Class interface1 : interface0.getInterfaces()) {
            interfaces.addAll(getSuperInterfaces(interface1));
        }
        return interfaces;
    }
}
