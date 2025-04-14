package cn.cxyxh.project.question.boot;

import cn.cxyxh.project.question.annotation.ActionType;
import cn.cxyxh.project.question.annotation.Api;
import cn.cxyxh.project.question.dto.ActionDTO;
import cn.cxyxh.project.question.entity.Action;
import cn.cxyxh.project.question.service.IActionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class ApiScanner implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Autowired
    private IActionService actionService;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods =
                requestMappingHandlerMapping.getHandlerMethods();
        Set<String> modules = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            // 1. 扫描方法级别的 @Api 注解（具体操作权限）
            Api api = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Api.class);
            if (api != null) {
                registerMethodPermission(api, entry.getKey());
                String module = api.module();
                if (StringUtils.isNotBlank(module)) {
                    modules.add(module);
                }
            }
        }
        // 注册module级别的权限
        modules.forEach(this::registerModulePermissions);
    }

    /**
     * 注册模块级权限（如 user:all）及其下属方法权限
     */
    private void registerModulePermissions(String module) {
        Action action = new Action();
        action.setType(ActionType.API.name());
        action.setCode(StringUtils.lowerCase(module) + ":all");
        action.setName(module);
        action.setType(ActionType.API.name());
        action.setPath(StringUtils.join("/api/", module, "/**"));
        action.setDescription(module + "模块全部权限");
        action.setCreateBy("SuperUser");
        action.setUpdateBy("SuperUser");
        action.setCreateTime(LocalDateTime.now());
        action.setUpdateTime(LocalDateTime.now());
        actionService.saveOrUpdateByCode(action);
    }

    /**
     * 注册方法级权限（如 user:read）
     */
    private void registerMethodPermission(Api methodApi, RequestMappingInfo mappingInfo) {
        String path = getFirstPath(mappingInfo);
        for (RequestMethod method : mappingInfo.getMethodsCondition().getMethods()) {
            saveOrUpdatePermission(
                    methodApi,
                    path,
                    method.name()
            );
        }
    }

    /**
     * 获取 Controller 的基础路径（如 "/api/users"）
     */
    private String getControllerBasePath(Class<?> controllerClass) {
        RequestMapping controllerMapping = AnnotationUtils.findAnnotation(controllerClass, RequestMapping.class);
        return controllerMapping != null ?
                Arrays.stream(controllerMapping.value()).findFirst().orElse("") : "";
    }

    /**
     * 保存权限（区分模块和方法类型）
     */
    private void saveOrUpdatePermission(Api api, String path, String method) {
        if (!StringUtils.startsWith(path, "/")){
            path = "/" + path;
        }
        String code = api.action();
        ActionDTO existAction = actionService.getActionByCode(code);
        Action action = new Action();
        action.setCode(code);
        action.setName(api.value());
        action.setDescription(api.description());
        action.setMethod(method);
        action.setPath(StringUtils.join("/api", path));
        action.setCreateBy("SuperUser");
        action.setUpdateBy("SuperUser");
        action.setCreateTime(LocalDateTime.now());
        action.setUpdateTime(LocalDateTime.now());
        if (existAction != null) {
            action.setId(existAction.getId());
        }
        actionService.saveOrUpdate(action);
    }

    /**
     * 获取接口路径（取第一个）
     */
    private String getFirstPath(RequestMappingInfo mappingInfo) {
        return mappingInfo.getPathPatternsCondition().getPatterns().stream()
                .findFirst()
                .map(PathPattern::getPatternString)
                .orElse("");
    }


}
