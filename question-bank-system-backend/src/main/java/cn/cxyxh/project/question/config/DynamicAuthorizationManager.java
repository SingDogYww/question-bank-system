package cn.cxyxh.project.question.config;

import cn.cxyxh.project.question.dto.ActionDTO;
import cn.cxyxh.project.question.service.IActionService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class DynamicAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Autowired
    private IActionService actionService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        // 0. 获取请求信息
        HttpServletRequest request = context.getRequest();
        String path = request.getRequestURI();
        String method = request.getMethod();
        Authentication authentication = authenticationSupplier.get();
        // 1. 检查用户是否拥有 ALL:ALL 权限
        if (hasAuthority(authentication, "all:all")) {
            return new AuthorizationDecision(true);
        }
        // 2. 查询当前请求需要的具体权限
        ActionDTO actionsByPathAndMethod = actionService.getActionsByPathAndMethod(path, method);
        if (actionsByPathAndMethod == null) {
            return new AuthorizationDecision(false); // 未配置权限的接口默认无法访问
        }
        String requiredPermission = actionsByPathAndMethod.getCode();
        // 3. 检查用户是否直接拥有该权限
        if (hasAuthority(authentication, requiredPermission)) {
            return new AuthorizationDecision(true);
        }
        // 4. 提取模块名（如 user:read → user）
        String module = requiredPermission.split(":")[0];
        // 5. 检查用户是否拥有模块级权限（如 USER:ALL）
        if (hasAuthority(authentication, module + ":ALL")) {
            return new AuthorizationDecision(true);
        }
        return new AuthorizationDecision(false);
    }

    private boolean hasAuthority(Authentication authentication, String authority) {
        return authentication.getAuthorities().stream()
                .anyMatch(granted ->
                        StringUtils.endsWithIgnoreCase(granted.getAuthority(), authority)
                );
    }


}
