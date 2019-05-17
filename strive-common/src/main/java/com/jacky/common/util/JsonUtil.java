package com.jacky.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jacky.common.entity.CustomDateDeserise;
import com.jacky.common.entity.CustomDateSerise;
import com.jacky.common.entity.exception.SerializeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Json工具类
 *
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc
 **/
public class JsonUtil {

    private static final ObjectMapper USE_ANNOTATION_MAPPER = new ObjectMapper();
    private static final ObjectMapper EXCLUDE_NULL_MAPPER = new ObjectMapper();
    private static final XmlMapper XML_MAPPER = new XmlMapper();

    static {
        initObjectMapper(USE_ANNOTATION_MAPPER);

        initObjectMapper(EXCLUDE_NULL_MAPPER);

        initObjectMapper(XML_MAPPER);

        //字段为null不显示
        EXCLUDE_NULL_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static ObjectMapper getUseAnnotationMapper() {
        return USE_ANNOTATION_MAPPER;
    }

    public static ObjectMapper getExcludeNullMapper() {
        return EXCLUDE_NULL_MAPPER;
    }

    public static ObjectMapper getSpringJsonObjectMapper() {
        return USE_ANNOTATION_MAPPER;
    }


    private static void initObjectMapper(ObjectMapper objectMapper) {

        // 不把date转换成timestamps
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 忽略不存在的字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        // 允许没加引号的字段
        objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(Feature.IGNORE_UNDEFINED, true);
        // 是否使用注解
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, true);
        // BigDecmal ；输出原来的值，不使用科学计数
        objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);

        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SimpleModule module = new SimpleModule("CommonDateDeserise", PackageVersion.VERSION);
        module.addDeserializer(Date.class, new CustomDateDeserise());
        module.addSerializer(Date.class, new CustomDateSerise());
        objectMapper.registerModule(module);

    }

    public static String toJson(Object obj, boolean prettyPrinter, boolean excludeNull) {
        if (null == obj) {
            return "";
        }
        if (obj instanceof CharSequence) {
            return obj.toString();
        }

        ObjectMapper objectMapper = USE_ANNOTATION_MAPPER;
        if (excludeNull) {
            objectMapper = EXCLUDE_NULL_MAPPER;
        }

        String content;
        try {
            if (prettyPrinter) {
                content = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            } else {
                content = objectMapper.writeValueAsString(obj);
            }
            return content;
        } catch (Exception e) {
            LogUtil.error(String.format("%s不能转换为json,%s", obj.getClass().toString(), ExceptionUtil.getMessage(e)));
        }
        return "";
    }

    /**
     * 对象转换成字符串，不抛出异常
     *
     * @param obj
     * @param prettyPrinter 开启格式化
     * @return
     */
    public static String toJson(Object obj, boolean prettyPrinter) {
        return toJson(obj, prettyPrinter, false);
    }


    /**
     * 对象转换成字符串(不使用注解)
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return toJson(obj, false);
    }

    private static <T> T toObj(String content, TypeReference<T> typeOfT) {

        if (StringUtil.isEmpty(content)) {
            return null;
        }

        try {
            return USE_ANNOTATION_MAPPER.readValue(content, typeOfT);
        } catch (Exception ex) {
            throw new SerializeException(String.format("将%s解析为%s出错,%s",
                    content,
                    typeOfT.toString(),
                    ExceptionUtil.getMessage(ex)),
                    ex);
        }
    }

    private static <T> T toObj(String content, Class<T> classOfT) {

        if (StringUtil.isEmpty(content)) {
            return null;
        }

        try {
            return USE_ANNOTATION_MAPPER.readValue(content, classOfT);
        } catch (Exception ex) {
            throw new SerializeException(String.format("将%s解析为%s出错,%s",
                    content,
                    classOfT.toString(),
                    ExceptionUtil.getMessage(ex)),
                    ex);
        }
    }

    /**
     * 字符串转换成指定类型
     *
     * @param <T>
     * @param content
     * @param typeOfT
     * @return
     */
    public static <T> T fromJson(String content, TypeReference<T> typeOfT) {
        return toObj(content, typeOfT);
    }

    /**
     * 字符串转换成指定类型
     *
     * @param <T>
     * @param content
     * @param classOfT
     * @return
     */
    public static <T> T fromJson(String content, Class<T> classOfT) {
        return toObj(content, classOfT);
    }

    /**
     * 字符串转换成指定类型
     *
     * @param <T>
     * @param content
     * @param classOfT
     * @return
     */
    public static <T> T fromJson(String content, JavaType classOfT) {
        if (StringUtil.isEmpty(content)) {
            return null;
        }

        if (classOfT == null) {
            throw new IllegalArgumentException("classOfT");
        }

        try {
            return USE_ANNOTATION_MAPPER.readValue(content, classOfT);
        } catch (Exception ex) {
            throw new SerializeException(String.format("将%s解析为%s出错,%s",
                    content,
                    classOfT.getRawClass().toString(),
                    ExceptionUtil.getMessage(ex)),
                    ex);
        }
    }


    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     */
    public static JavaType getCollectionType(ObjectMapper mapper, Class<?> collectionClass,
                                             Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * @param content
     * @param collectionClass
     * @param elementClasses
     * @return
     * @throws Exception
     */
    public static <T> T toObj(String content, Class<?> collectionClass, Class<?>... elementClasses) {
        try {
            return USE_ANNOTATION_MAPPER.readValue(content,
                    getCollectionType(USE_ANNOTATION_MAPPER, collectionClass, elementClasses));
        } catch (Exception ex) {
            throw new SerializeException(ExceptionUtil.getMessage(ex), ex);
        }
    }

    public static String toForm(Object obj) {
        return toForm(obj, false);
    }

    /**
     * 将Bean url 参数化 key=11&key=222
     *
     * @param obj 需要序列号为form字符串的对象。
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String toForm(Object obj, boolean excludeNull) {
        if (obj == null) {
            return "";
        }

        if (obj instanceof CharSequence) {
            return obj.toString();
        }

        String json = JsonUtil.toJson(obj, false, excludeNull);

        Map<String, Object> map = new LinkedHashMap<>();
        map = JsonUtil.fromJson(json, map.getClass());

        return toForm(map, excludeNull);
    }

    public static String toForm(Map parameterMap) {
        return toForm(parameterMap, false);
    }

    public static String toForm(Map parameterMap, boolean excludeNull) {
        StringBuilder parameterBuffer = new StringBuilder();
        if (parameterMap != null) {
            Iterator iterator = parameterMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = null;
                if (parameterMap.get(key) != null) {
                    Object valueObj = parameterMap.get(key);
                    if (valueObj instanceof Character
                            || valueObj.getClass().isPrimitive()) {
                        value = valueObj.toString();
                    } else {
                        value = JsonUtil.toJson(valueObj);
                    }
                }
                try {
                    if (!StringUtil.isEmpty(value)) {
                        value = URLEncoder.encode(value, "utf-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    LogUtil.error(String.format("%s不能做url编码", value), e);
                }

                if (StringUtil.isEmpty(value)) {
                    value = "";
                }

                if (!StringUtil.isEmpty(value) || !excludeNull) {
                    parameterBuffer.append(String.format("%s=%s", key, value));
                    if (iterator.hasNext()) {
                        parameterBuffer.append("&");
                    }

                }
            }
        }
        return parameterBuffer.toString();
    }


    /**
     * 对象转xml
     *
     * @param obj obj
     * @return
     */
    public static String toXml(Object obj) {

        if (obj == null) {
            return "";
        }
        if (obj instanceof CharSequence) {
            return obj.toString();
        }
        try {
            return XML_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return "";

    }

    public static <T> T getAttrFromJson(String jsonString, String attrRoute, TypeReference<T> typeRefrense) {
        return JsonUtil.getAttrFromJson(jsonString, attrRoute, ".", typeRefrense);
    }

    public static <T> T getAttr(String jsonString, TypeReference<T> typeRefrense, String... attrs) {
        return JsonUtil.getAttrFromJson(jsonString, StringUtils.join(attrs, "."), ".", typeRefrense);
    }

    /**
     * 从json字符串中获取某个key的值
     *
     * @param jsonString   原始json字符串
     * @param attrRoute    xpath路径，以分隔符隔开
     * @param routeSpliter xpath分隔符
     * @return
     */
    public static <T> T getAttrFromJson(String jsonString, String attrRoute, String routeSpliter,
                                        TypeReference<T> typeRefrense) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        String attrVal;
        try {
            if (StringUtils.isEmpty(attrRoute)) {
                attrVal = jsonString;
            } else {
                JsonNode jsonNode = USE_ANNOTATION_MAPPER.readTree(jsonString);
                String[] routes = StringUtils.split(attrRoute, routeSpliter);
                for (String route : routes) {
                    if (StringUtils.isEmpty(route)) {
                        continue;
                    }
                    if (null == jsonNode) {
                        return null;
                    }
                    if (NumberUtils.isCreatable(route)) {
                        jsonNode = jsonNode.get(NumberUtils.toInt(route));
                    } else {
                        jsonNode = jsonNode.get(route);
                    }
                }
                if (null == jsonNode) {
                    return null;
                }
                attrVal = jsonNode.toString();
            }
            return toObj(attrVal, typeRefrense);
        } catch (Exception e) {
            LogUtil.error(String.format("从json[%s]获取属性[%s]失败", jsonString, attrRoute), e);
            return null;
        }
    }


    /**
     * @param xml
     * @param clzss
     * @param <T>
     * @return
     */
    public static <T> T fromXml(String xml, Class<T> clzss) {
        try {
            return XML_MAPPER.readValue(xml, clzss);
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return null;
    }
}
