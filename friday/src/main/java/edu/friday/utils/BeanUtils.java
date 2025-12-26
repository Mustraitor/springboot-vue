package edu.friday.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * 转换PO和VO的list
     *
     * @param source po list
     * @param tCls   目标实体类class
     */
    public static <S, T> List<T> copyProperties(List<S> source, Class<T> tCls) {
        List<T> target = new ArrayList<T>();
        T vo;
        for (S item : source) {
            if (item == null) continue;
            try {
                vo = tCls.newInstance();
            } catch (Exception e) {
                vo = null;
            }
            org.springframework.beans.BeanUtils.copyProperties(item, vo);
            target.add(vo);
        }
        return target;
    }

    /**
     * 通过反射获取对象中空值的字段
     *
     * @param source 检查对象
     */
    public static String[] getNullPropertyNames(Object source) {
        return getNullPropertyNames(source, false);
    }

    /**
     * 通过反射获取对象中空值的字段
     *
     * @param source      检查对象
     * @param checkString 是否检查空字符串
     */
    public static String[] getNullPropertyNames(Object source, boolean checkString) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
            if (checkString) {
                if (srcValue instanceof String && StringUtils.isBlank(srcValue.toString()))
                    emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 拷贝对象值的时候忽略空值null(不包括空字符串)
     *
     * @param source 数据源
     * @param target 目标对象
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        copyProperties(source, target, getNullPropertyNames(source));
    }

    /**
     * 拷贝对象值的时候忽略空值null和空字符串
     *
     * @param source 数据源
     * @param target 目标对象
     */
    public static void copyPropertiesIgnoreEmpty(Object source, Object target) {
        copyProperties(source, target, getNullPropertyNames(source, true));
    }
}
