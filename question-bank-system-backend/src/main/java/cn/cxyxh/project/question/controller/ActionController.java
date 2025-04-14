package cn.cxyxh.project.question.controller;

import cn.cxyxh.project.question.annotation.Api;
import cn.cxyxh.project.question.service.IActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 操作表 前端控制器
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
@RestController
@RequestMapping("/action")
@Api(value = "操作模块", action = "action:all", description = "操作模块所有权限", module = "action")
public class ActionController {
    @Autowired
    private IActionService actionService;


}
