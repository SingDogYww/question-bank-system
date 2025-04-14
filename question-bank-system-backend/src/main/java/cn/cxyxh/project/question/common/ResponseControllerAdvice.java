package cn.cxyxh.project.question.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一响应处理
 */
@Slf4j
@RestControllerAdvice
public class ResponseControllerAdvice implements ResponseBodyAdvice<Object> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果已经是Result类型，则不需要处理
        return !returnType.getParameterType().equals(Result.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // 处理null值
        if (body == null) {
            return Result.success();
        }

        // 处理String类型返回值
        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(Result.success(body));
            } catch (JsonProcessingException e) {
                log.error("Failed to convert Result to JSON string", e);
                throw new ApiException("响应结果序列化异常");
            }
        }

        // 处理Result类型
        if (body instanceof Result) {
            return body;
        }

        // 统一包装为Result
        return Result.success(body);
    }
} 