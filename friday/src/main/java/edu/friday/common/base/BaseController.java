package edu.friday.common.base;

import edu.friday.common.result.RestResult;

/**
 * web层通用数据处理
 */
public class BaseController {
    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected RestResult toAjax(int rows) {
        return rows > 0 ? RestResult.success() : RestResult.error();
    }
}
