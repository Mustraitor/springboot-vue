package edu.friday.common.result;

import edu.friday.common.constant.HttpStatus;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 */
public class TableDataInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<?> rows;

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 消息内容
     */
    private int msg;

    /**
     * 表格数据对象
     */
    public TableDataInfo() {
    }

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<?> list, long total) {
        this.rows = list;
        this.total = total;
    }

    /**
     * 成功的返回方法
     */
    public static TableDataInfo success(List<?> list, long total) {
        TableDataInfo tableDataInfo = new TableDataInfo(list, total);
        tableDataInfo.setCode(HttpStatus.SUCCESS);
        return tableDataInfo;
    }

    /**
     * 失败的返回方法
     */
    public static TableDataInfo failure(int msg) {
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setCode(HttpStatus.ERROR);
        tableDataInfo.setMsg(msg);
        return tableDataInfo;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }
}