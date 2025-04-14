package cn.cxyxh.project.question.controller.example;

import cn.cxyxh.project.question.common.ApiException;
import cn.cxyxh.project.question.common.Result;
import cn.cxyxh.project.question.common.ResultCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 示例控制器，展示统一返回类型和异常处理
 */
@RestController
@RequestMapping("/api/example")
@Validated
public class ExampleController {

    /**
     * 成功示例 - 直接返回数据，自动包装为Result
     */
    @GetMapping("/success")
    public String success() {
        return "Success";
    }

    /**
     * 成功示例 - 手动包装Result
     */
    @GetMapping("/success2")
    public Result<String> success2() {
        return Result.success("Success with custom message", "操作成功");
    }

    /**
     * 使用断言工具类
     */
    @GetMapping("/assert")
    public String assertExample(@RequestParam(required = false) String param) {
        // 使用断言工具类检查参数
        return "参数值: " + param;
    }

    /**
     * 抛出自定义异常
     */
    @GetMapping("/exception")
    public String exception(@RequestParam(defaultValue = "false") boolean throwError) {
        if (throwError) {
            throw new ApiException(ResultCode.FAILED);
        }
        return "No exception thrown";
    }

    /**
     * 参数验证示例
     */
    @PostMapping("/validate")
    public ValidateDto validate(@RequestBody @Valid ValidateDto dto) {
        return dto;
    }

    /**
     * 参数验证DTO
     */
    @Data
    public static class ValidateDto {
        @NotBlank(message = "用户名不能为空")
        private String username;

        @NotBlank(message = "密码不能为空")
        private String password;
    }
} 