package io.sansam.sswork.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * SSBeanUtils
 * </p>
 *
 * @author houcb
 * @since 2019-10-18 11:12
 */
public class SSBeanUtils {

    private static Logger logger = LoggerFactory.getLogger(SSBeanUtils.class);

    /**
     * bean转为map，忽略父类字段，忽略值为null的字段
     *
     * @param bean 待转换的bean
     * @return Map<String, Object>
     */
    public static Map<String, Object> convertBean2Map(Object bean) {
        return convertBean2Map(bean, false, true, null);
    }

    /**
     * bean转换为map
     *
     * @param bean            待转换的bean
     * @param includeParent   是否包含父类字段
     * @param ignoreNullValue 是否过滤值为null的字段
     * @param ignoreFields    忽略字段集合
     * @return Map<String, Object>
     */
    public static Map<String, Object> convertBean2Map(Object bean,
                                                      boolean includeParent,
                                                      boolean ignoreNullValue,
                                                      List<String> ignoreFields) {
        final boolean debugEnabled = logger.isDebugEnabled();
        Map<String, Object> dataMap = Maps.newHashMap();
        if (Objects.isNull(bean)) {
            return dataMap;
        }
        try {
            List<PropertyDescriptor> pdList = Lists.newArrayList();
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            pdList.addAll(Arrays.asList(beanInfo.getPropertyDescriptors()));
            if (includeParent) {
                Class<?> superClass = bean.getClass().getSuperclass();
                while (superClass != null) {
                    beanInfo = Introspector.getBeanInfo(superClass);
                    pdList.addAll(Arrays.asList(beanInfo.getPropertyDescriptors()));
                    superClass = superClass.getSuperclass();
                }
            }
            if (debugEnabled) {
                logger.debug("SSBeanUtils - convertBean2Map find all propertyDescriptor {}", pdList);
            }
            String fileName;
            Object val;
            for (PropertyDescriptor pd : pdList) {
                fileName = pd.getName();
                if (skipField(fileName, ignoreFields)) {
                    continue;
                }
                val = pd.getReadMethod().invoke(bean);
                if (!(Objects.isNull(val) && ignoreNullValue)) {
                    dataMap.put(fileName, val);
                }
            }
            logger.info("SSBeanUtils - convertBean2Map result {}", dataMap);
        } catch (Exception e) {
            logger.error("SSBeanUtils - convertBean2Map failure!", e);
        }

        return dataMap;
    }

    private static boolean skipField(String fileName, List<String> ignoreFields) {
        return "class".equalsIgnoreCase(fileName)
                || (!CollectionUtils.isEmpty(ignoreFields) && ignoreFields.contains(fileName));
    }
}
