package cn.cxyxh.project.question.service;

import cn.cxyxh.project.question.common.Result;
import cn.cxyxh.project.question.dto.RoleDTO;
import cn.cxyxh.project.question.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
public interface IRoleService extends IService<Role> {

    Result<List<RoleDTO>> getUserRoles(String userid);

}
