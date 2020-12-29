package com.changhong.telnettool.database;

import java.lang.reflect.Field;
import java.util.Arrays;

public class ColumnItem extends Utils {
    String fieldName;
    String descriptionName;
    String[] choicesDescriptor;
    Field field;
    Class<?> type;

    public ColumnItem() {
    }

    public ColumnItem(Field field) {
        this.field = field;
        field.setAccessible(true);
        this.type = field.getType();
        this.fieldName = field.getName();
        descriptionName = getDescription(field);
        choicesDescriptor = getDescriptorFields(field);
    }

    public ColumnItem(String fieldName, Class<?> cls, String descriptionName, String[] choicesDescriptor) {
        this.fieldName = fieldName;
        this.descriptionName = descriptionName;
        this.choicesDescriptor = choicesDescriptor;
        this.type = cls;
    }

    public String getDescriptionName() {
        return descriptionName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String[] getChoicesDescriptor() {
        return choicesDescriptor;
    }

    public Class<?> getType() {
        return type;
    }

    public Field getField() {
        return field;
    }

    @Override
    public String toString() {
        return "ColumnItem{" +
                "fieldName='" + fieldName + '\'' +
                ", descriptionName='" + descriptionName + '\'' +
                ", choicesDescriptor=" + Arrays.toString(choicesDescriptor) +
                ", field=" + field +
                ", type=" + type +
                '}';
    }
}
