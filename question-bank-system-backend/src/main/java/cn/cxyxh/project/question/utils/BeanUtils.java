package cn.cxyxh.project.question.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class BeanUtils extends org.springframework.beans.BeanUtils {




    /**
     * 仅支持targetClass为非泛型类型
     */
    public static <S, T> List<T> copyPropertiesBatch(List<S> list, Class<T> targetClass) {
        List<T> targetList = new ArrayList<T>();
        list.forEach(item -> {
            try {
                T t = targetClass.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(item, t);
                targetList.add(t);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        return targetList;
    }
}
