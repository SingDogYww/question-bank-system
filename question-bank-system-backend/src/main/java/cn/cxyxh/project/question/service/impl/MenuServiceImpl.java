package cn.cxyxh.project.question.service.impl;

import cn.cxyxh.project.question.entity.Menu;
import cn.cxyxh.project.question.mapper.MenuMapper;
import cn.cxyxh.project.question.service.IMenuService;
import cn.cxyxh.project.question.service.IRoleMenuMapService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private IRoleMenuMapService roleMenuMapService;

}
