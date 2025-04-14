package cn.cxyxh.project.question.service.impl;

import cn.cxyxh.project.question.common.Result;
import cn.cxyxh.project.question.dto.UserDTO;
import cn.cxyxh.project.question.entity.User;
import cn.cxyxh.project.question.mapper.UserMapper;
import cn.cxyxh.project.question.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService, UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IActionService actionService;
    @Autowired
    private IUserGroupService userGroupService;


    @Override
    public Result<UserDTO> register(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);


        return null;
    }

    @Override
    public Result<UserDTO> login(UserDTO user) {
        return null;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }
}
