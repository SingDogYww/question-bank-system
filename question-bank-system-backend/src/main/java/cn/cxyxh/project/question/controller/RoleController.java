package cn.cxyxh.project.question.controller;

import cn.cxyxh.project.question.annotation.Api;
import cn.cxyxh.project.question.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
@RestController
@RequestMapping("/role")
@Api(value = "角色模块", action = "role:all", description = "角色模块所有权限", module = "role")
public class RoleController {


    @Autowired
    private IRoleService roleService;


}
