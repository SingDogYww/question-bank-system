package cn.cxyxh.project.question.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Api {
    String value() default "";

    String name() default "";

    String action(); // 唯一不重复

    String description() default "";

    ApiType type() default ApiType.METHOD;

    String module() default "";
}
