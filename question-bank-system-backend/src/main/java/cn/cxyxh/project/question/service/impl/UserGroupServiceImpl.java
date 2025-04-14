package cn.cxyxh.project.question.service.impl;

import cn.cxyxh.project.question.entity.UserGroup;
import cn.cxyxh.project.question.mapper.UserGroupMapper;
import cn.cxyxh.project.question.service.IUserGroupDataMapService;
import cn.cxyxh.project.question.service.IUserGroupMemberService;
import cn.cxyxh.project.question.service.IUserGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 用户组表 服务实现类
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
@Service
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper, UserGroup> implements IUserGroupService {

    @Autowired
    private UserGroupMapper userGroupMapper;
    @Autowired
    private IUserGroupMemberService userGroupMemberService;
    @Autowired
    private IUserGroupDataMapService userGroupDataMapService;

}
