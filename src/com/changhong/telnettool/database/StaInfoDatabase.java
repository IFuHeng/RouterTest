package com.changhong.telnettool.database;

import com.changhong.telnettool.webinterface.been.StaInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaInfoDatabase {

    private static final String sSqlCreateTable = "CREATE TABLE %s " +
            "(ID INTEGER PRIMARY KEY   AUTOINCREMENT," +
            " NAME           TEXT       NOT NULL, " +
            " MAC            CHAR(17)   NOT NULL, " +
            " IP             CHAR(15), " +
            " CONNECT_TYPE   INT        NOT NULL, " +
            " FREQUENCY      INT, " +
            " RX             INT, " +
            " TX             INT, " +
            " CHANGE_EVENT   INT        NOT NULL, " +
            " Superior       CHAR(17), " +
            " LINK_TIME      INT)";

    private static final String[] ARR_COLUMNS = {"NAME", "MAC", "IP",
            "CONNECT_TYPE", "FREQUENCY", "RX",
            "TX", "CHANGE_EVENT", "Superior",
            "LINK_TIME"};
    private static final Class[] ARR_TYPE = {String.class, String.class, String.class,
            Integer.class, Integer.class, Integer.class,
            Integer.class, Integer.class, String.class,
            Integer.class};

    public static void main(String args[]) {
        StaInfoDatabase database = new StaInfoDatabase("staInfo", "staTable");
        database.insert("Mi-6", "AA:BB:CC:DD:EE:FF", "192.168.2.100", 4, 2400, 48239, 458193, 1, "EE:EE:EE:EE:EE:EE", 1000);
        database.insert("Mi-5", "AA:BB:CC:DD:EE:EE", "192.168.2.101", 2, 2400, 48239, 458193, 0, "EE:EE:EE:EE:EE:EE", 1000);
        database.selectByChangeEvent(0);
    }

    private String dbName;
    private String tableName;

    public StaInfoDatabase(String name, String tableName) {
        this.dbName = name;
        if (tableName.indexOf(' ') != -1)
            tableName = tableName.replace(' ', '_');
        if (tableName.indexOf('\t') != -1)
            tableName = tableName.replace('\t', '_');
        if (tableName.indexOf('\n') != -1)
            tableName = tableName.replace('\n', '_');

        createTable(tableName);
        this.tableName = tableName;
    }

    public Connection connect(String dbName) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
        System.out.println("Opened database successfully");
        return c;
    }

    public void createTable(String tableName) {
        Connection c = null;
        try {
            c = connect(dbName);
            Statement stmt = c.createStatement();
            String sql = String.format(sSqlCreateTable, tableName);
            System.out.println(sql);
            stmt.executeUpdate(sql);
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
        System.out.println("Table created successfully");
    }

    public void insert(String name, String mac, String ip, int connect_type, int frequency, int rx, int tx, int change_event, String supperior, int link_time) {
        Connection c = null;
        try {
            c = connect(dbName);

            Statement stmt = c.createStatement();
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO ").append(tableName).append(' ')
                    .append('(');
            for (int i = 0; i < ARR_COLUMNS.length; i++) {
                sb.append(ARR_COLUMNS[i]);
                if (i < ARR_COLUMNS.length - 1)
                    sb.append(',');
            }
            sb.append(')').append(' ');
            sb.append("VALUES ( ");

            sb.append('\"').append(name).append('\"').append(',').append(' ');
            sb.append('\"').append(mac).append('\"').append(',').append(' ');
            sb.append('\"').append(ip).append('\"').append(',').append(' ');
            sb.append(connect_type).append(',').append(' ');
            sb.append(frequency).append(',').append(' ');
            sb.append(rx).append(',').append(' ');
            sb.append(tx).append(',').append(' ');
            sb.append(change_event).append(',').append(' ');
            sb.append('\"').append(supperior).append('\"').append(',').append(' ');
            sb.append(link_time);

            sb.append(" );");

            stmt.executeUpdate(sb.toString());

            stmt.close();
            if (!c.getAutoCommit()) {
                c.commit();
            }
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
        System.out.println("Records created successfully");
    }

    public List<StaInfo> selectByChangeEvent(int change_event) {
        List<StaInfo> staInfos = new ArrayList<>();
        Connection c = null;
        try {
            c = connect(dbName);
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE CHANGE_EVENT = " + change_event + ";");
            while (rs.next()) {
                System.out.print("ID = " + rs.getInt("ID"));
                for (int i = 0; i < ARR_COLUMNS.length; i++) {
                    System.out.print(" " + ARR_COLUMNS[i] + " = " + rs.getObject(ARR_COLUMNS[i]) + " ,");
                }
                System.out.println();


                StaInfo e = new StaInfo();

                staInfos.add(e);
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
        System.out.println("Operation done successfully");

        return staInfos;
    }

    public void select() {
        Connection c = null;
        try {
            c = connect(dbName);

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + ";");
            while (rs.next()) {
                System.out.print("ID = " + rs.getInt("ID"));
                for (int i = 0; i < ARR_COLUMNS.length; i++) {
                    System.out.print(" " + ARR_COLUMNS[i] + " = " + rs.getObject(ARR_COLUMNS[i]) + " ,");
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
        System.out.println("Operation done successfully");
    }

    public void update(int id, String name, String mac, String ip, int connect_type, int frequency, int rx, int tx, int change_event, String supperior, int link_time) {
        Connection c = null;
        try {
            c = connect(dbName);
            Statement stmt = c.createStatement();
            StringBuilder sb = new StringBuilder();
            sb.append("UPDATE ").append(tableName).append(" set ");
            sb.append(ARR_COLUMNS[0]).append(" = ").append('\"').append(name).append('\"').append(',').append(' ');
            sb.append(ARR_COLUMNS[1]).append(" = ").append('\"').append(mac).append('\"').append(',').append(' ');
            sb.append(ARR_COLUMNS[2]).append(" = ").append('\"').append(ip).append('\"').append(',').append(' ');
            sb.append(ARR_COLUMNS[3]).append(" = ").append(connect_type).append(',').append(' ');
            sb.append(ARR_COLUMNS[4]).append(" = ").append(frequency).append(',').append(' ');
            sb.append(ARR_COLUMNS[5]).append(" = ").append(rx).append(',').append(' ');
            sb.append(ARR_COLUMNS[6]).append(" = ").append(tx).append(',').append(' ');
            sb.append(ARR_COLUMNS[7]).append(" = ").append(change_event).append(',').append(' ');
            sb.append(ARR_COLUMNS[8]).append(" = ").append('\"').append(supperior).append('\"').append(',').append(' ');
            sb.append(ARR_COLUMNS[9]).append(" = ").append(link_time);
            sb.append(" where ID=").append(id).append(';');
            stmt.executeUpdate(sb.toString());
            if (!c.getAutoCommit())c.commit();
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
        System.out.println("Operation done successfully");
    }

    public void deleteById(int id) {
        Connection c = null;
        try {
            c = connect(dbName);
            Statement stmt = c.createStatement();
            String sql = "DELETE from " + tableName + " where ID=" + id + ";";
            stmt.executeUpdate(sql);
            if (!c.getAutoCommit())c.commit();
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
        System.out.println("Operation done successfully");
    }
}