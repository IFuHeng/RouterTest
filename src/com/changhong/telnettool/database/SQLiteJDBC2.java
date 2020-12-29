package com.changhong.telnettool.database;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SQLiteJDBC2<T> extends Utils{

    private final String fSqlCreateTable;

    private final String[] fColumns;
    private final Class[] fColumnTypes;

    private String dbName;
    private String tableName;

    private Class<T> mClzT;

    public SQLiteJDBC2(String name, Class<T> tClass) {
        this.dbName = name;
        this.tableName = tClass.getSimpleName();
        this.mClzT = tClass;
        {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(tClass.getSimpleName()).append(' ');
            sb.append("(ID INTEGER PRIMARY KEY   AUTOINCREMENT");
            ArrayList<String> arrNames = new ArrayList<>();
            ArrayList<Class> arrTypes = new ArrayList<>();
            for (Field declaredField : tClass.getDeclaredFields()) {
                Class type = declaredField.getType();
                String fieldName = declaredField.getName();
                if (isSurportType(type)) {
                    arrNames.add(fieldName);
                    arrTypes.add(type);
                } else
                    continue;

                if (type == String.class) {
                    sb.append(',').append(fieldName).append(' ').append("TEXT");
                } else if (isInteger(type) || isShort(type) || isLong(type) || isDate(type)) {
                    sb.append(',').append(fieldName).append(' ').append("INT");
                } else if (isFloat(type)) {
                    sb.append(',').append(fieldName).append(' ').append("FLOAT");
                } else if (isByte(type)) {
                    sb.append(',').append(fieldName).append(' ').append("INT8");
                } else if (isDouble(type)) {
                    sb.append(',').append(fieldName).append(' ').append("DOUBLE");
                } else if (isBoolean(type)) {
                    sb.append(',').append(fieldName).append(' ').append("INT2");
                }
                log(declaredField.getName() + "  " + declaredField.getGenericType().getTypeName() + ", isAccessible = " + declaredField.isAccessible());
            }
            sb.append(')').append(';');
            log(sb.toString());
            this.fSqlCreateTable = sb.toString();
            fColumns = new String[arrNames.size()];
            for (int i = 0; i < arrNames.size(); i++) {
                fColumns[i] = arrNames.get(i);
            }
            fColumnTypes = new Class[arrTypes.size()];
            for (int i = 0; i < arrTypes.size(); i++) {
                fColumnTypes[i] = arrTypes.get(i);
            }
        }

        createTable();
    }

    @Override
    public String toString() {
        return "SQLiteJDBC{" +
                "fSqlCreateTable='" + fSqlCreateTable + '\'' +
                ", fColumns=" + Arrays.toString(fColumns) +
                ", fColumnTypes=" + Arrays.toString(fColumnTypes) +
                ", dbName='" + dbName + '\'' +
                ", tableName='" + tableName + '\'' +
                '}';
    }

    public Connection connect(String dbName) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
        log("Opened database successfully");
        return c;
    }

    public void createTable() {
        Connection c = null;
        try {
            c = connect(dbName);
            Statement stmt = c.createStatement();
            stmt.executeUpdate(fSqlCreateTable);
            stmt.close();
            c.close();
        } catch (Exception e) {
//            e.printStackTrace();
            System.err.println(e.getMessage());
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
//        log("Table created successfully");
    }

    public void insert(T t) {
        Connection c = null;
        try {
            c = connect(dbName);

            Statement stmt = c.createStatement();
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO ").append(tableName).append(' ')
                    .append('(');

            for (String column : fColumns) {
                sb.append(column).append(',');
            }
            sb.deleteCharAt(sb.length() - 1).append(')').append(' ');
            sb.append("VALUES ( ");
            for (int i = 0; i < fColumns.length; i++) {
                Type type = fColumnTypes[i];
                String column = fColumns[i];

                Object value = getFieldObject(column, t);
                if (value == null)
                    sb.append("null");
                if (type == String.class) {
                    sb.append('\"').append(turnStringSave((String) value)).append('\"');
                } else if (isDate(type)) {
                    sb.append(((java.util.Date) value).getTime());
                } else if (isBoolean(type))
                    sb.append(((Boolean) value) ? 1 : 0);
                else
                    sb.append(value);
                sb.append(',').append(' ');
            }
            sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1).append(')').append(';');
            log("====>>>   " + sb.toString());
            stmt.executeUpdate(sb.toString());

            stmt.close();
            if (!c.getAutoCommit()) {
                c.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        log("Records created successfully");
    }

    private Object getFieldObject(String fieldName, T t) {
        Object value = null;
        try {
            Field field = mClzT.getDeclaredField(fieldName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            value = field.get(t);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                String aimName = "get" + fieldName;
                for (Method declaredMethod : mClzT.getDeclaredMethods()) {
                    String methodName = declaredMethod.getName();
                    if (aimName.equalsIgnoreCase(methodName)) {
                        value = declaredMethod.invoke(t);
                        break;
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return value;
    }

    /**
     * @param columnName 筛选行
     * @param value      行的值
     * @return {ID:T}对
     */
    public Map<Integer, T> selectByChangeEvent(String columnName, Object value) {
        boolean isContain = false;
        for (String fColumn : fColumns) {
            if (columnName.equalsIgnoreCase(fColumn)) {
                isContain = true;
                break;
            }
        }
        if (!isContain) {
            return null;
        }
        Map<Integer, T> result = new HashMap();
        Connection c = null;
        try {
            c = connect(dbName);
            Statement stmt = c.createStatement();
            if (value instanceof String)
                value = "\"" + value + "\"";
            String sql = "SELECT * FROM " + tableName + " WHERE " + columnName + "=" + value + ";";
            log("> " + sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("ID");
//                System.out.print("ID = " + rs.getInt("ID"));
                T e = mClzT.newInstance();

                for (int i = 0; i < fColumns.length; i++) {
                    Field field = mClzT.getDeclaredField(fColumns[i]);
                    field.setAccessible(true);
                    Object v = rs.getObject(fColumns[i]);
                    if (value instanceof Double && isFloat(field.getType())) {
                        double d = (double) value;
                        float f = (float) d;
                        field.set(e, f);
                    } else if (isDate(field.getType())) {
                        long time = 0;
                        if (value instanceof Integer) time = (Integer) value;
                        else if (value instanceof Long) time = (Long) value;
                        field.set(e, new java.util.Date(time));
                    } else if (value instanceof String) {
                        field.set(e, turnStringLoad((String) value));
                    } else if (isBoolean(field.getType())) {
                        int iv = (int) value;
                        boolean is = iv == 1;
                        field.set(e, is);
                    } else
                        field.set(e, value);
                }
                result.put(id, e);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        log("Operation done successfully");

        return result;
    }

    /**
     * @return {ID:T}对
     */
    public Map<Integer, T> select() {
        Map<Integer, T> result = new HashMap();
        Connection c = null;
        try {
            c = connect(dbName);
            Statement stmt = c.createStatement();
            String sql = "SELECT * FROM " + tableName + ";";
            log("> " + sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("ID");
                T e = mClzT.newInstance();
                for (int i = 0; i < fColumns.length; i++) {
                    Field field = mClzT.getDeclaredField(fColumns[i]);
                    field.setAccessible(true);
                    Object value = rs.getObject(fColumns[i]);
                    if (value instanceof Double && isFloat(field.getType())) {
                        double d = (double) value;
                        float f = (float) d;
                        field.set(e, f);
                    } else if (isDate(field.getType())) {
                        long time = 0;
                        if (value instanceof Integer) time = (Integer) value;
                        else if (value instanceof Long) time = (Long) value;
                        field.set(e, new java.util.Date(time));
                    } else if (value instanceof String) {
                        field.set(e, turnStringLoad((String) value));
                    } else if (isBoolean(field.getType())) {
                        int iv = (int) value;
                        boolean is = iv == 1;
                        field.set(e, is);
                    } else
                        field.set(e, value);
                }
                result.put(id, e);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        log("Operation done successfully");

        return result;
    }

    public void showSelect() {
        Connection c = null;
        try {
            c = connect(dbName);

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + ";");
            while (rs.next()) {
                System.out.print("ID = " + rs.getInt("ID"));
                for (int i = 0; i < fColumns.length; i++) {
                    System.out.print(" " + fColumns[i] + " = " + rs.getObject(fColumns[i]) + " ,");
                }
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        log("Operation done successfully");
    }

    public void update(int id, T t) {
        Connection c = null;
        try {
            c = connect(dbName);

            Statement stmt = c.createStatement();
            StringBuilder sb = new StringBuilder();
            sb.append("UPDATE ").append(tableName).append(" set ");
            for (int i = 0; i < fColumns.length; i++) {
                Type type = fColumnTypes[i];
                String column = fColumns[i];
                Object value = getFieldObject(column, t);
                sb.append(column).append(" = ");
                if (value == null)
                    sb.append("null");
                if (type == String.class)
                    sb.append('\"').append(turnStringSave((String) value)).append('\"');
                else if (isDate(type))
                    sb.append(((java.util.Date) value).getTime());
                else if (isBoolean(type))
                    sb.append(((Boolean) value) ? 1 : 0);
                else
                    sb.append(value);
                sb.append(',').append(' ');
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
            sb.append(" where ID=").append(id).append(';');
            log("====>>>   " + sb.toString());
            stmt.executeUpdate(sb.toString());

            stmt.close();
            if (!c.getAutoCommit()) {
                c.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

        log("Operation done successfully");
    }

    public void deleteById(int id) {
        Connection c = null;
        try {
            c = connect(dbName);
            Statement stmt = c.createStatement();
            String sql = "DELETE from " + tableName + " where ID=" + id + ";";
            stmt.executeUpdate(sql);
            if (!c.getAutoCommit()) {
                c.commit();
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        log("Operation done successfully");
    }

    public void drop() {
        Connection c = null;
        try {
            c = connect(dbName);
            Statement stmt = c.createStatement();
            String sql = "DROP TABLE " + tableName + ";";
            stmt.executeUpdate(sql);
            if (!c.getAutoCommit()) {
                c.commit();
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        log("Operation done successfully");
    }

    public void clear() {
        Connection c = null;
        try {
            c = connect(dbName);
            Statement stmt = c.createStatement();
            String sql = "DELETE FROM " + tableName + ";";
            stmt.executeUpdate(sql);
            if (!c.getAutoCommit()) {
                c.commit();
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        log("clear done successfully");
    }

    /**
     * @return {size, id_min, id_max}
     */
    public int[] rowSize() {
        int[] result = new int[3];
        Connection c = null;
        try {
            c = connect(dbName);
            Statement stmt = c.createStatement();
            String sql = "SELECT COUNT(*), MIN(ID), MAX(ID) FROM " + tableName + ";";
            log("> " + sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result[0] = rs.getInt("COUNT(*)");
                result[1] = rs.getInt("MIN(ID)");
                result[2] = rs.getInt("MAX(ID)");
//                int available = rs.getBinaryStream(0).available();
//                byte[] data = rs.getBytes(0);
//                Reader reader = rs.getCharacterStream(0);
//                String str = rs.getString(0);
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        log("Operation done successfully");

        return result;
    }

    public String[] getfColumns() {
        return fColumns;
    }

    public Class[] getfColumnTypes() {
        return fColumnTypes;
    }

    private String turnStringLoad(String original) {
        if (original == null || original.indexOf("\\\"") == -1)
            return original;

        return original.replace("\\\"", "\"");
    }
}