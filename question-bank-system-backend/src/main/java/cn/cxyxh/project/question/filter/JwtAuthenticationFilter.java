package cn.cxyxh.project.question.filter;

import cn.cxyxh.project.question.service.PermissionCacheService;
import cn.cxyxh.project.question.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PermissionCacheService permissionCacheService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = extractToken(request);
        if (token != null && jwtUtil.validateToken(token)) {
            String userId = jwtUtil.extractUserId(token);
            Set<String> permissions = permissionCacheService.getPermissions(userId);
            List<SimpleGrantedAuthority> authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userId, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("X-Auth-Token");
        if (header != null && header.startsWith("Bearer:")) {
            return header.substring(7);
        }
        return null;
    }


}
