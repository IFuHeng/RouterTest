package com.changhong.telnettool.database;

import com.sun.org.glassfish.gmbal.Description;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过映射将对象存储入数据库中。
 * 每个对象的类都是独立的一张表格。表格（Table）的名字是 {@link Description}或者类的{@link Class}.simpleName()。所以项目中使用时要特别注意不要重复。
 * 数据对象的类中的所有属性，将存储进入各自的列中，列名是属性的{@link com.sun.org.glassfish.gmbal.Description}或属性名称。所以在使用时要避免重复。
 * <p>
 * 同时，对boolean或 用来表示序号的整形的变量，可以使用{@link com.sun.org.glassfish.gmbal.DescriptorFields} 来解释其信息。这里仅用于显示，存储时都存储为数字。
 *
 * @param <T>
 */
public class SQLiteJDBC<T> extends Utils {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<ColumnItem> mArrColumns;

    private final String dbName;
    private final String tableName;

    private Class<T> mClzT;

    /**
     * @param name   db文件名，注意，name不是真正的文件名，系统会自动添加‘.db’的后缀。使用时需注意。
     * @param tClass 要生成表格的类
     */
    public SQLiteJDBC(String name, Class<T> tClass) {
        if (!name.endsWith(".db"))
            this.dbName = name + ".db";
        else
            this.dbName = name;

        this.tableName = sqliteEscape(getDescription(tClass));
        this.mClzT = tClass;
        this.mArrColumns = new ArrayList();

        String fSqlCreateTable;
        {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ");
            sb.append(this.tableName);
            sb.append(' ');
            sb.append("(ID INTEGER PRIMARY KEY   AUTOINCREMENT");
            for (Field declaredField : tClass.getDeclaredFields()) {
                ColumnItem item = new ColumnItem(declaredField);
                if (isSurportType(item.type)) {
                    mArrColumns.add(item);
                } else
                    continue;

                sb.append(',').append(sqliteEscape(item.getDescriptionName())).append(' ');
                sb.append(getSurportTypeSqlType(item));
            }
            sb.append(')').append(';');
            log(sb.toString());
            fSqlCreateTable = sb.toString();
        }

        createTable(fSqlCreateTable);
    }

    @Override
    public String toString() {
        return "SQLiteJDBC2{" +
                ", mArrColumns=" + mArrColumns +
                ", dbName='" + dbName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", mClzT=" + mClzT +
                '}';
    }

    public Connection connect(String dbName) throws SQLException, ClassNotFoundException {
        Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbName);
//        log("Opened database successfully");
        return c;
    }

    private void createTable(String fSqlCreateTable) {
        Connection c = null;
        try {
            c = connect(dbName);
            Statement stmt = c.createStatement();
            stmt.executeUpdate(fSqlCreateTable);
            stmt.close();
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
            for (ColumnItem item : mArrColumns) {
                sb.append(sqliteEscape(item.descriptionName)).append(',');
            }
            sb.deleteCharAt(sb.length() - 1).append(')').append(' ');
            sb.append("VALUES ( ");
            for (int i = 0; i < mArrColumns.size(); i++) {
                Type type = mArrColumns.get(i).getType();
                Object value = mArrColumns.get(i).getField().get(t);
                if (value == null)
                    sb.append("null");
                else if (type == String.class) {
                    String tmp = turnStringSave((String) value);
                    if (tmp.charAt(0) == '\'' && tmp.charAt(tmp.length() - 1) == '\'') {
                        sb.append(tmp);
                    } else
                        sb.append('\"').append(tmp).append('\"');
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

    /**
     * @param columnName 筛选行
     * @param value      行的值
     * @return {ID:T}对
     */
    public Map<Integer, T> selectByChangeEvent(String columnName, Object value) {

        if (!isColumnInTable(mArrColumns, columnName))
            return null;

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

                for (ColumnItem item : mArrColumns) {
                    Field field = item.field;
                    field.setAccessible(true);
                    Object v = rs.getObject(item.getDescriptionName());
                    if (v instanceof Double && isFloat(field.getType())) {
                        double d = (double) v;
                        float f = (float) d;
                        field.set(e, f);
                    } else if (isDate(field.getType())) {
                        long time = 0;
                        if (v instanceof Integer) time = (Integer) v;
                        else if (v instanceof Long) time = (Long) v;
                        field.set(e, new java.util.Date(time));
                    } else if (v instanceof String) {
                        field.set(e, v);
                    } else if (isBoolean(field.getType())) {
                        int iv = (int) v;
                        boolean is = iv == 1;
                        field.set(e, is);
                    } else
                        field.set(e, v);
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
                for (ColumnItem item : mArrColumns) {
                    Field field = item.getField();
                    field.setAccessible(true);
                    Object value = rs.getObject(item.getDescriptionName());
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
                        field.set(e, value);
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
                for (ColumnItem item : mArrColumns) {
                    System.out.print(" " + item.getDescriptionName() + " = " + rs.getObject(item.getDescriptionName()) + " ,");
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
            for (ColumnItem item : mArrColumns) {
                Type type = item.getType();
                String column = item.getDescriptionName();
                Object value = item.getField().get(t);
                sb.append(column).append(" = ");
                if (value == null)
                    sb.append("null");
                else if (type == String.class) {
                    String tmp = turnStringSave(sqliteEscape((String) value));
                    sb.append(tmp);
                } else if (isDate(type))
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

    public ArrayList<ColumnItem> getmArrColumns() {
        return mArrColumns;
    }

    public static ArrayList<String> getTables(String dbName) {
        File file = new File(dbName + ".db");
        if (!file.exists())
            return null;

        Connection c = null;
        try {
            c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table';");
            ArrayList<String> arrayList = new ArrayList<>();
            while (rs.next()) {
                String string = rs.getString("name");
                arrayList.add(string);
            }
            stmt.close();
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
//            System.err.println(e.getMessage());
            return null;
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    public static void showSelect(String dbName, String tableName) {
        Connection c = null;
        try {
            c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + ";");
            while (rs.next()) {
                System.out.print("ID = " + rs.getInt("ID"));
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
    }

    public static void main(String[] args) {
        ArrayList<String> arr = SQLiteJDBC.getTables("resource1216");
        System.out.println(arr);

        Connection c = null;
        try {
            c = DriverManager.getConnection("jdbc:sqlite:" + "resource1216" + ".db");

            Statement stmt = c.createStatement();
            for (String s : arr) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + s + ";");
                while (rs.next()) {
                    System.out.print("ID = " + rs.getInt("ID"));
                    System.out.println();
                }
                rs.close();
            }
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

    }
}