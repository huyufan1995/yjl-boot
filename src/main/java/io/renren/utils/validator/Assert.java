package io.renren.utils.validator;

import org.apache.commons.lang.StringUtils;

import io.renren.api.exception.ApiException;
import io.renren.utils.RRException;

/**
 * 数据校验
 * 
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new RRException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new RRException(message);
        }
    }
    
    public static void isBlankApi(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new ApiException(message);
        }
    }

    public static void isNullApi(Object object, String message) {
        if (object == null) {
            throw new ApiException(message);
        }
    }
}
