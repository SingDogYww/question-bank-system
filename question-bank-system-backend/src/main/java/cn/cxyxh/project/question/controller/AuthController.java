package cn.cxyxh.project.question.controller;

import cn.cxyxh.project.question.annotation.Api;
import cn.cxyxh.project.question.common.Result;
import cn.cxyxh.project.question.dto.LoginRequest;
import cn.cxyxh.project.question.entity.User;
import cn.cxyxh.project.question.service.IUserService;
import cn.cxyxh.project.question.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.UUID;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
@Api(value = "认证模块", action = "auth:all", description = "认证模块所有权限", module = "action")
public class AuthController {
    @Autowired
    private IUserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return ResponseEntity.ok("注册成功");
    }


    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user);
        response.setHeader("X-Auth-Token", "Bearer:" + token);
        return Result.success();
    }
} 