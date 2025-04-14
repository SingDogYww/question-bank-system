package cn.cxyxh.project.question.service;

import cn.cxyxh.project.question.common.Result;
import cn.cxyxh.project.question.dto.ActionDTO;
import cn.cxyxh.project.question.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final IUserService userService;
    private final IActionService actionService;
    private static final String CACHE_KEY_PREFIX = "user_permissions:";

    public Set<String> getPermissions(String userId) {
        String key = CACHE_KEY_PREFIX + userId;
        Set<String> permissions = (Set<String>) redisTemplate.opsForValue().get(key);
        if (CollectionUtils.isEmpty(permissions)) {
            permissions = loadPermissionsFromDB(userId);
            redisTemplate.opsForValue().set(key, permissions, 1, TimeUnit.HOURS);
        }
        return permissions;
    }

    private Set<String> loadPermissionsFromDB(String userId) {
        User user = userService.getById(userId);
        Result<List<ActionDTO>> userActions = actionService.getUserActions(userId);
        return userActions.getData().stream().map(ActionDTO::getCode).collect(Collectors.toSet());
    }

    public void clearCache(String userId) {
        redisTemplate.delete(CACHE_KEY_PREFIX + userId);
    }

}
