package cn.cxyxh.project.question.controller;

import cn.cxyxh.project.question.annotation.Api;
import cn.cxyxh.project.question.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
@RestController
@RequestMapping("/menu")
@Api(value = "菜单模块", action = "menu:all", description = "菜单模块所有权限", module = "menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;

}
