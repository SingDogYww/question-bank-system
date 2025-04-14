package cn.cxyxh.project.question.common;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;
import java.util.Set;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义API异常
     */
    @ExceptionHandler(ApiException.class)
    public Result<Object> handleApiException(ApiException e) {
        log.error("ApiException: {}", e.getMessage(), e);
        if (e.getErrorCode() != null) {
            return Result.failed(e.getErrorCode());
        }
        return Result.failed(e.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: {}", e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getDefaultMessage();
            }
        }
        return Result.validateFailed(message);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public Result<Object> handleBindException(BindException e) {
        log.error("BindException: {}", e.getMessage(), e);
        FieldError fieldError = e.getFieldError();
        String message = null;
        if (fieldError != null) {
            message = fieldError.getDefaultMessage();
        }
        return Result.validateFailed(message);
    }

    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Object> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("ConstraintViolationException: {}", e.getMessage(), e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String message = "";
        if (!violations.isEmpty()) {
            message = violations.iterator().next().getMessage();
        }
        return Result.validateFailed(message);
    }

    /**
     * 处理请求参数缺失异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException: {}", e.getMessage(), e);
        return Result.validateFailed("请求参数 " + e.getParameterName() + " 不能为空");
    }

    /**
     * 处理请求参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException: {}", e.getMessage(), e);
        return Result.validateFailed("参数类型不匹配，参数" + e.getName() + "应该为" + Objects.requireNonNull(e.getRequiredType()).getName() + "类型");
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException: {}", e.getMessage(), e);
        return Result.failed(ResultCode.METHOD_NOT_ALLOWED, "不支持" + e.getMethod() + "请求方法");
    }

    /**
     * 处理请求体不可读异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException: {}", e.getMessage(), e);
        return Result.validateFailed("请求体无法解析");
    }

    /**
     * 处理资源不存在异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<Object> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("NoHandlerFoundException: {}", e.getMessage(), e);
        return Result.failed(ResultCode.NOT_FOUND, "请求的资源不存在");
    }

    /**
     * 处理所有其他未知异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Object> handleException(Exception e) {
        log.error("Exception: {}", e.getMessage(), e);
        return Result.failed(ResultCode.SERVER_ERROR, "服务器内部错误");
    }
} 