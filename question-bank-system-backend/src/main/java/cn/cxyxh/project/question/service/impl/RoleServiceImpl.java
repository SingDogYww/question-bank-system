package cn.cxyxh.project.question.service.impl;

import cn.cxyxh.project.question.common.Result;
import cn.cxyxh.project.question.dto.RoleDTO;
import cn.cxyxh.project.question.entity.Role;
import cn.cxyxh.project.question.entity.UserRoleMap;
import cn.cxyxh.project.question.mapper.RoleMapper;
import cn.cxyxh.project.question.service.IRoleActionMapService;
import cn.cxyxh.project.question.service.IRoleMenuMapService;
import cn.cxyxh.project.question.service.IRoleService;
import cn.cxyxh.project.question.service.IUserRoleMapService;
import cn.cxyxh.project.question.utils.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private IUserRoleMapService userRoleMapService;
    @Autowired
    private IRoleMenuMapService roleMenuMapService;
    @Autowired
    private IRoleActionMapService roleActionMapService;

    @Override
    public Result<List<RoleDTO>> getUserRoles(String userid) {
        QueryWrapper<UserRoleMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userid);
        List<UserRoleMap> list = userRoleMapService.list(queryWrapper);
        List<Integer> roleIds = list.stream().map(UserRoleMap::getRoleId).toList();
        if (CollectionUtils.isEmpty(roleIds)){
            return Result.success(new ArrayList<>());
        }
        List<Role> roles = roleMapper.selectByIds(roleIds);
        List<RoleDTO> roleDTOS = BeanUtils.copyPropertiesBatch(roles, RoleDTO.class);
        return Result.success(roleDTOS);
    }
}
