package com.changhong.telnettool.database;

import com.sun.org.glassfish.gmbal.Description;
import com.sun.org.glassfish.gmbal.DescriptorFields;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

public class Utils {
    private final boolean debug = true;

    void log(String str) {
        if (debug)
            System.out.println(str);
    }

    /**
     * @param type
     * @return 判断是否是本数据库支持的数据类型
     */
    boolean isSurportType(Class<?> type) {
        return type == String.class
                || isDate(type)
                || isInteger(type)
                || isLong(type)
                || isShort(type)
                || isFloat(type)
                || isDouble(type)
                || isBoolean(type)
                || isByte(type);
    }

    String getSurportTypeSqlType(Class<?> type) {
        if (isInteger(type) || isShort(type) || isLong(type) || isDate(type)) {
            return ("INT");
        } else if (isFloat(type)) {
            return ("FLOAT");
        } else if (isByte(type)) {
            return ("INT8");
        } else if (isDouble(type)) {
            return ("DOUBLE");
        } else if (isBoolean(type)) {
            return ("INT2");
        }
        return ("TEXT");
    }

    String getSurportTypeSqlType(ColumnItem item) {
        return getSurportTypeSqlType(item.getType());
    }

    boolean isInteger(Type type) {
        return type == Integer.class || "int".equals(type.getTypeName());
    }

    boolean isLong(Type type) {
        return type == Long.class || "long".equals(type.getTypeName());
    }

    boolean isShort(Type type) {
        return type == Short.class || "short".equals(type.getTypeName());
    }

    boolean isFloat(Type type) {
        return type == Float.class || "float".equals(type.getTypeName());
    }

    boolean isDouble(Type type) {
        return type == Double.class || "double".equals(type.getTypeName());
    }

    boolean isBoolean(Type type) {
        return type == Boolean.class || "boolean".equals(type.getTypeName());
    }

    boolean isByte(Type type) {
        return type == Byte.class || "byte".equals(type.getTypeName());
    }

    boolean isDate(Type type) {
        return type == java.util.Date.class || type == Date.class;
    }

    String getDescription(Field field) {
        String result = field.getName();
        Annotation[] annotations = field.getDeclaredAnnotations();
        if (annotations == null || annotations.length == 0)
            return result;

        for (Annotation annotation : annotations) {
            if (!(annotation instanceof Description)) {
                continue;
            }
            result = ((Description) annotation).value();
            break;
        }
        return result;
    }

    String[] getDescriptorFields(Field field) {
        String[] result = null;
        Annotation[] annotations = field.getDeclaredAnnotations();
        if (annotations == null || annotations.length == 0)
            return result;

        for (Annotation annotation : annotations) {
            if (!(annotation instanceof DescriptorFields)) {
                continue;
            }
            result = ((DescriptorFields) annotation).value();
            break;
        }
        return result;
    }

    String getDescription(Class cls) {
        String result = cls.getSimpleName();
        Annotation[] annotations = cls.getDeclaredAnnotations();
        if (annotations == null || annotations.length == 0)
            return result;

        for (Annotation annotation : annotations) {
            if (!(annotation instanceof Description)) {
                continue;
            }
            result = ((Description) annotation).value();
            break;
        }
        return result;
    }

    String turnStringSave(String original) {

        if (original == null
                || (original.length() > 0 && original.charAt(0) == '\'' && original.charAt(original.length() - 1) == '\'')
//                || (original.length() > 0 && original.charAt(0) == '\"' && original.charAt(original.length() - 1) == '\"')
        )
            return original;

        return '\'' + original + '\'';
    }

    boolean isColumnInTable(ArrayList<ColumnItem> arr, String columnName) {
        boolean isContain = false;
        for (ColumnItem mArrColumn : arr) {
            if (columnName.equalsIgnoreCase(mArrColumn.getDescriptionName())) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }

    boolean isContainSpecialChar(String str) {
        return !Pattern.matches("[a-zA-Z][a-zA-Z_0-9]*", str);
    }

    String sqliteEscape(String keyWord) {
        if (!isContainSpecialChar(keyWord))
            return keyWord;
//        keyWord = keyWord.replace("/", "//");
        keyWord = keyWord.replace("'", "''");
//        keyWord = keyWord.replace("\"", "\"\"");
//        keyWord = keyWord.replace("[", "/[");
//        keyWord = keyWord.replace("]", "/]");
//        keyWord = keyWord.replace("%", "/%");
//        keyWord = keyWord.replace("&", "/&");
//        keyWord = keyWord.replace("_", "/_");
//        keyWord = keyWord.replace("(", "/(");
//        keyWord = keyWord.replace(")", "/)");
        return '\'' + keyWord + '\'';
    }

}
