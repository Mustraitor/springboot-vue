package edu.friday.controller;

import edu.friday.common.base.BaseController;
import edu.friday.common.result.RestResult;
import edu.friday.common.result.TableDataInfo;
import edu.friday.model.SysUser;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/demo")
public class DemoController extends BaseController {

    /**
     * 新增
     */
    @PostMapping
    public RestResult add() {
        boolean flag = true;
        return toAjax(flag ? 1 : 0);
    }

    /**
     * 修改
     */
    @PutMapping
    public RestResult edit() {
        boolean flag = true;
        return toAjax(flag ? 1 : 0);
    }

    /**
     * 删除
     */
    @DeleteMapping()
    public RestResult remove() {
        boolean flag = true;
        return toAjax(flag ? 1 : 0);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @GetMapping(value = {"/", "/{userId}"})
    public RestResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        RestResult ajax = RestResult.success();
        ajax.put("user", new SysUser());
        return ajax;
    }

    @GetMapping("/list")
    public TableDataInfo list() {
        return TableDataInfo.success(new ArrayList<>(), 100);
    }

}

