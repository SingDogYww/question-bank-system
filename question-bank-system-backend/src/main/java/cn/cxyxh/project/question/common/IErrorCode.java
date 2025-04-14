package cn.cxyxh.project.question.common;

/**
 * 错误码接口
 */
public interface IErrorCode {
    /**
     * 获取错误码
     */
    Integer getCode();

    /**
     * 获取错误消息
     */
    String getMessage();
} 