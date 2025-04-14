package cn.cxyxh.project.question.service;

import cn.cxyxh.project.question.common.Result;
import cn.cxyxh.project.question.dto.UserDTO;
import cn.cxyxh.project.question.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
public interface IUserService extends IService<User> {

    Result<UserDTO> register(UserDTO user);

    Result<UserDTO> login(UserDTO user);

}
