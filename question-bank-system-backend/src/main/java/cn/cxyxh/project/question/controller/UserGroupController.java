package cn.cxyxh.project.question.controller;

import cn.cxyxh.project.question.annotation.Api;
import cn.cxyxh.project.question.service.IUserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 用户组表 前端控制器
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
@RestController
@RequestMapping("/userGroup")
@Api(value = "用户组模块", action = "userGroup:all", description = "用户组模块所有权限", module = "userGroup")
public class UserGroupController {

    @Autowired
    private IUserGroupService userGroupService;

}
