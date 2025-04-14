package cn.cxyxh.project.question.service.impl;

import cn.cxyxh.project.question.common.Result;
import cn.cxyxh.project.question.dto.ActionDTO;
import cn.cxyxh.project.question.dto.RoleDTO;
import cn.cxyxh.project.question.entity.Action;
import cn.cxyxh.project.question.entity.RoleActionMap;
import cn.cxyxh.project.question.mapper.ActionMapper;
import cn.cxyxh.project.question.service.IActionService;
import cn.cxyxh.project.question.service.IRoleActionMapService;
import cn.cxyxh.project.question.service.IRoleService;
import cn.cxyxh.project.question.service.IUserRoleMapService;
import cn.cxyxh.project.question.utils.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 操作表 服务实现类
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
@Service
public class ActionServiceImpl extends ServiceImpl<ActionMapper, Action> implements IActionService {

    @Autowired
    private ActionMapper actionMapper;
    @Autowired
    private IRoleActionMapService roleActionMapService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserRoleMapService userRoleMapService;

    @Override
    public Result<List<ActionDTO>> getUserActions(String userid) {
        Result<List<RoleDTO>> userRoles = roleService.getUserRoles(userid);
        List<Integer> roleIds = userRoles.getData().stream().map(RoleDTO::getId).toList();
        if (CollectionUtils.isEmpty(roleIds)) {
            return Result.success(new ArrayList<>());
        }
        QueryWrapper<RoleActionMap> roleActionMapQueryWrapper = new QueryWrapper<>();
        roleActionMapQueryWrapper.in("role_id", roleIds);
        List<RoleActionMap> list = roleActionMapService.list(roleActionMapQueryWrapper);
        List<Integer> actionIds = list.stream().map(RoleActionMap::getActionId).toList();
        if (CollectionUtils.isEmpty(actionIds)) {
            return Result.success(new ArrayList<>());
        }
        List<Action> actionList = actionMapper.selectByIds(actionIds);
        List<ActionDTO> actionDTOS = BeanUtils.copyPropertiesBatch(actionList, ActionDTO.class);
        return Result.success(actionDTOS);
    }

    @Override
    public ActionDTO getActionsByPathAndMethod(String path, String method) {
        QueryWrapper<Action> actionQueryWrapper = new QueryWrapper<>();
        actionQueryWrapper.eq("path", path);
        actionQueryWrapper.eq("method", method);
        Action action = actionMapper.selectOne(actionQueryWrapper);
        if (action == null) {
            return null;
        }
        ActionDTO actionDTO = new ActionDTO();
        BeanUtils.copyProperties(action, actionDTO);
        return actionDTO;
    }

    @Override
    public ActionDTO getActionByCode(String code) {
        QueryWrapper<Action> actionQueryWrapper = new QueryWrapper<>();
        actionQueryWrapper.eq("code", code);
        Action action = actionMapper.selectOne(actionQueryWrapper);
        if (action == null) {
            return null;
        }
        ActionDTO actionDTO = new ActionDTO();
        BeanUtils.copyProperties(action, actionDTO);
        return actionDTO;
    }

    @Override
    public Action saveOrUpdateByCode(Action actionDTO) {
        Action action = new Action();
        BeanUtils.copyProperties(actionDTO, action);
        action.setUpdateTime(LocalDateTime.now());
        QueryWrapper<Action> actionQueryWrapper = new QueryWrapper<>();
        actionQueryWrapper.eq("code", actionDTO.getCode());
        Action exist = actionMapper.selectOne(actionQueryWrapper);
        if (exist != null) {
            action.setId(exist.getId());
        } else {
            action.setCreateTime(LocalDateTime.now());
        }
        saveOrUpdate(action);
        return action;
    }


}
