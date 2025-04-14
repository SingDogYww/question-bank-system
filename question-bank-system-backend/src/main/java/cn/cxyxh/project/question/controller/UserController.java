package cn.cxyxh.project.question.controller;

import cn.cxyxh.project.question.annotation.Api;
import cn.cxyxh.project.question.dto.UserDTO;
import cn.cxyxh.project.question.entity.User;
import cn.cxyxh.project.question.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/list")
    @Api(value = "获取所有用户", action = "user:list", module = "user")
    public ResponseEntity<List<UserDTO>> getUsers() {
        // 业务逻辑
        List<User> list = userService.list();
        List<UserDTO> result = new ArrayList<>();
        for (User user : list) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            result.add(userDTO);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create")
    @Api(value = "创建用户", action = "user:create", module = "user")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        // 业务逻辑
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        userService.save(user);
        return ResponseEntity.ok(userDTO);
    }

}
