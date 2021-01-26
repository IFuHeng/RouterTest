package com.changhong.telnettool.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Utils {
    public static Type getObjectType(Object object) {
        java.lang.reflect.Type[] types = object.getClass().getGenericInterfaces();
        for (java.lang.reflect.Type type : types) {
            ParameterizedType pt = (ParameterizedType) type;
            Type[] ata = pt.getActualTypeArguments();
            if (ata != null && ata.length > 0)
                return ata[0];
        }
        return null;
    }

}
