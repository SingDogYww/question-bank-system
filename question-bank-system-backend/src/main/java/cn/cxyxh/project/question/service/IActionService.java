package cn.cxyxh.project.question.service;

import cn.cxyxh.project.question.common.Result;
import cn.cxyxh.project.question.dto.ActionDTO;
import cn.cxyxh.project.question.entity.Action;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 操作表 服务类
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
public interface IActionService extends IService<Action> {

    Result<List<ActionDTO>> getUserActions(String userid);

    ActionDTO getActionsByPathAndMethod(String path, String method);

    ActionDTO getActionByCode(String code);

    Action saveOrUpdateByCode(Action action);
}
