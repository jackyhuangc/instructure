package com.jacky.strive.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

public class BeanUtil extends BeanUtils {
    
    public static Object getFieldValue(Object o, String fieldName) {
        return getFieldValue(o, fieldName, null);
    }
    
    /**
     * 根据属性名获取属性值
     */
    public static Object getFieldValue(Object o, String fieldName, Object defaultValue) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Throwable e) {
            String msg = String.format("从对象%s获取属性：%s失败.%s", null == o ? "" : o.getClass(), fieldName, e.getMessage());
            LogUtil.error(msg, e);
            return defaultValue;
        }
    }
    
    /**
     * 获取属性名数组
     */
    public List<String> getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        List<String> fieldNames = new ArrayList<String>();
        for (int i = 0; i < fields.length; i++) {
            fieldNames.add(fields[i].getName());
        }
        return fieldNames;
    }
    
    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     */
    @SuppressWarnings("rawtypes")
    public List<Map> getFiledsInfo(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        List<Map> list = new ArrayList<Map>();
        Map<String, Object> infoMap = null;
        for (int i = 0; i < fields.length; i++) {
            infoMap = new HashMap<String, Object>();
            infoMap.put("type", fields[i].getType().toString());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value", getFieldValue(o.toString(), fields[i].getName()));
            list.add(infoMap);
        }
        return list;
    }
    
    /**
     * 获取对象的所有属性值，返回一个对象数组
     */
    public Object[] getFiledValues(Object o) {
        List<String> fieldNames = this.getFiledName(o);
        Object[] value = new Object[fieldNames.size()];
        for (int i = 0; i < fieldNames.size(); i++) {
            value[i] = getFieldValue(o, fieldNames.get(i));
        }
        return value;
    }
    
    /**
     * 将对象{@link} 中 相同属性名 转换成target
     *
     * @param source 源对象
     * @param targetClass 目标对象
     * @return
     */
    @SuppressWarnings("hiding")
    public static <S, T> T map(S source, Class<T> targetClass) {
        DozerBeanMapper beanMapper = new DozerBeanMapper();
        if (null == source) {
            return null;
        }
        return beanMapper.map(source, targetClass);
    }
    
    /**
     * 将对象转换成另外一个对象[同属性转换]
     *
     * @param sources     源对象
     * @param targetClass 目标对象
     * @return
     */
    @SuppressWarnings("hiding")
    public static <S, T> List<T> map(List<S> sources, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sources)) {
            return new ArrayList<>();
        }
        List<T> result = new ArrayList<>();
        for (S source : sources) {
            result.add(map(source, targetClass));
        }
        return result;
    }
    
    @SuppressWarnings({"hiding", "unchecked"})
    public static <T> T map(Map<String, Object> sources, Class<T> targetClass) {
        try {
            
            
            T result = targetClass.newInstance();
            Field[] fields = targetClass.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                JsonProperty meta = f.getAnnotation(JsonProperty.class);
                Object val = null;
                if (meta != null) {
                    val = sources.get(meta.value());
                    if (null == val) {
                        continue;
                    }
                } else {
                    val = sources.get(f.getName());
                    if (null == val) {
                        continue;
                    }
                }
                String format = StringUtils.EMPTY;
                if (Date.class == f.getType()) {
                    JsonFormat jsonFormat = f.getAnnotation(JsonFormat.class);
                    if (null != jsonFormat) {
                        format = jsonFormat.pattern();
                    }
                }
                Object attrVal = getFieldValue(f.getType(), val, format);
                if (null != attrVal) {// 是对象
                    f.set(result, attrVal);
                } else {
                    if (val instanceof Map) {
                        f.set(result, map((Map<String, Object>) val, f.getType()));
                    } else {
                        f.set(result, map(val, f.getType()));
                    }
                }
            }
            return result;
        } catch (Exception ex) {
            throw new RuntimeException(ExceptionUtil.getMessage(ex), ex);
        }
    }
    
    @SuppressWarnings("rawtypes")
    private static Object getFieldValue(Class<?> classs, Object val, String format) {
        if (String.class == classs) {
            return (String) val;
        } else if (Integer.class == classs) {
            return Integer.valueOf(val.toString());
        } else if (BigDecimal.class == classs) {
            return new BigDecimal(val.toString());
        } else if (Long.class == classs) {
            return new Long(val.toString());
        } else if (Date.class == classs) {
            if (StringUtils.isEmpty(format)) {
                format = DateUtil.DEFAULT_FORMAT_STRING;
            }
            return DateUtil.parseDate(val.toString(), format);
        } else if (Boolean.class == classs) {
            return Boolean.valueOf(val.toString());
        } else if (List.class == classs) {
            return (List) val;
        } else if (Byte.class == classs) {
            return new Byte(val.toString());
        }
        return null;
    }
}
