package com.foodkeeper.foodkeeperserver.common.utils;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;

import java.util.List;
import java.util.function.Supplier;

public class ListUtil {
    public static <T> List<T> getOrElseThrowList(List<T> list){
        if (list == null || list.isEmpty()) {
            throw new AppException(ErrorType.NOT_FOUND_DATA);
        }
        return list;
    }
}
