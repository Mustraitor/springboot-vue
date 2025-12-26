package edu.friday.utils;

import java.util.Arrays;

/**
 * sql操作工具类
 */
public class SqlUtil {
    /**
     * 仅支持字母、数字、下划线、空格、逗号（支持多个字段排序）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,]+";

    /**
     * 检查字符，防止注入绕过
     */
    public static String escapeOrderBySql(String value) {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value)) {
            return StringUtils.EMPTY;
        }
        return value;
    }

    /**
     * 验证 order by 语法是否符合规范
     */
    public static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }

    /**
     * 生成批量插入的预编译语句
     *
     * @param batchSize
     * @param paramNum
     * @return string
     */
    public static String getBatchInsertSqlStr(int batchSize, int paramNum) {
        String[] pramArr = new String[paramNum];
        Arrays.fill(pramArr, "?");
        String paramStr = StringUtils.join(pramArr, ",");
        String batchStr = StringUtils.format("( {} )" , paramStr);
        String[] batchArr = new String[batchSize];
        Arrays.fill(batchArr, batchStr);
        String sql = StringUtils.join(batchArr, ",");
        return sql;
    }

}
