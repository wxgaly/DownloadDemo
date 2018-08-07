package nova.android.slidingcarddemo.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * nova.android.slidingcarddemo.utils.
 *
 * @author Created by WXG on 2018/8/8 0:06.
 * @version V1.0
 */
public class TypeUtils {

    public static Class<?> analysisClassInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }

}
