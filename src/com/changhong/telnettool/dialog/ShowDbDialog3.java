package com.changhong.telnettool.dialog;

import com.changhong.telnettool.database.SQLiteJDBC2;
import com.sun.istack.internal.NotNull;
import com.sun.org.glassfish.gmbal.Description;
import com.sun.org.glassfish.gmbal.DescriptorFields;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ShowDbDialog3 extends JDialog implements ItemListener, ActionListener {

    private static final String ACTION_REFRESH = "刷新";

    private final String dbFilePath;
    private final Class[] mTypes;
    private final TableItem[] tableArr;
    private final JPanel mCardPanel;
    private final CardLayout mCardLayout;
    private final ButtonGroup mCheckBoxGroup;

    public ShowDbDialog3(@NotNull Frame frame, String title, String dbFilePath, Class... types) {
        super(frame, title);
        this.mTypes = types;
        this.dbFilePath = dbFilePath;
        setLayout(new BorderLayout());
        {
            JPanel panelN = new JPanel();
            panelN.setBorder(BorderFactory.createTitledBorder("控制台"));
            mCheckBoxGroup = new ButtonGroup();
            for (int i = 0; i < types.length; i++) {
                JRadioButton cb = new JRadioButton(getDescription(types[i]));
                cb.getModel().setActionCommand(types[i].getSimpleName());
                cb.getModel().setSelected(i == 0);
                mCheckBoxGroup.add(cb);
                cb.addItemListener(this);
                panelN.add(cb);
            }

            JButton jButton = new JButton(ACTION_REFRESH);
            jButton.setActionCommand(ACTION_REFRESH);
            jButton.addActionListener(this);
            panelN.add(jButton);

            this.add(panelN, BorderLayout.NORTH);
        }
        {
            tableArr = new TableItem[types.length];
            mCardLayout = new CardLayout(10, 10);
            mCardPanel = new JPanel(mCardLayout);
            for (int i = 0; i < types.length; i++) {
                tableArr[i] = new TableItem(types[i]);
                if (i == 0) {
                    tableArr[i].reload();
                }
                mCardLayout.addLayoutComponent(tableArr[i].scrollPane, types[i].getSimpleName());
                mCardPanel.add(tableArr[i].scrollPane, BorderLayout.CENTER);
            }
//            mCardPanel.setSize(800, 600);
            this.add(mCardPanel, BorderLayout.CENTER);
        }
        pack();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (e.getSource() instanceof Dialog) {
                    Dialog dialog = (Dialog) e.getSource();
                    dialog.dispose();
                }
            }
        });

        setLocationRelativeTo(frame);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object item = e.getItem();
        Object source = e.getSource();
        if (source instanceof JRadioButton) {
            JRadioButton rb = ((JRadioButton) source);
            if (rb.getModel().isSelected())
                refresh(rb.getModel().getActionCommand());
        }
    }

    private void refresh(String tab) {
        int index = -1;
        for (int i = 0; i < mTypes.length; i++) {
            if (mTypes[i].getSimpleName().equals(tab)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            for (TableItem tableItem : tableArr) {
                if (tableItem.type.getSimpleName().equals(tab)) {
                    tableItem.reload();
                }
            }
            mCardLayout.show(mCardPanel, tab);
        }
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b)
            refresh(mCheckBoxGroup.getSelection().getActionCommand());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(ACTION_REFRESH)) {
            if (mCheckBoxGroup.getSelection() != null) {
                String command = mCheckBoxGroup.getSelection().getActionCommand();
                for (TableItem tableItem : tableArr) {
                    if (tableItem.type.getSimpleName().equals(command)) {
                        tableItem.reload();
                    }
                }
            }
//            pack();
        }
    }

    private class TableItem<T> {
        Class<T> type;
        SQLiteJDBC2 database;
        ArrayList<T> arrayList;
        JTable table;
        JScrollPane scrollPane;

        public TableItem(Class<T> type) {
            this.type = type;
            this.database = new SQLiteJDBC2(dbFilePath, type);
            this.arrayList = new ArrayList<>();
            initView();
        }

        private void initView() {
            SQLiteJDBC2 database = new SQLiteJDBC2(dbFilePath, type);

            String[] titleColums = new String[database.getfColumns().length];
            System.arraycopy(database.getfColumns(), 0, titleColums, 0, titleColums.length);
            for (int i = 0; i < titleColums.length; i++) {
                try {
                    titleColums[i] = getDescription(type.getDeclaredField(titleColums[i]));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            table = new JTable(new MyTableMode(titleColums, database.getfColumns(), database.getfColumnTypes(), arrayList));
            RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
            table.setRowSorter(sorter);
            table.setRowHeight(table.getFont().getSize());
            {
                int fontSize = table.getFont().getSize();
                TableColumn column = table.getColumnModel().getColumn(0);
                column.setMaxWidth(fontSize * 6);
                column.setMinWidth(fontSize*2);
                DefaultTableCellRenderer render = new DefaultTableCellRenderer();
                render.setHorizontalAlignment(SwingConstants.CENTER);
                render.setForeground(Color.white);
                render.setBackground(Color.darkGray);
                column.setCellRenderer(render);
            }
            scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createTitledBorder(getDescription(type)));
        }

        public void reload() {
            Map map = database.select();
            arrayList.clear();
            arrayList.addAll(map.values());

            ((AbstractTableModel) table.getModel()).fireTableDataChanged();
        }
    }

    private class MyTableMode<T> extends AbstractTableModel {

        private final String[] TITLES;
        private final String[] fieldNames;
        private final List<T> mData;
        private final Class[] types;

        public MyTableMode(String[] TITLES, String[] fieldNames, Class[] types, List<T> mData) {
            this.TITLES = TITLES;
            this.types = types;
            this.fieldNames = fieldNames;
            this.mData = mData;
        }

        public int getColumnCount() {
            return TITLES.length + 1;
        }

        public int getRowCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0)
                return "序号";
            return TITLES[column - 1];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0)
                return Integer.class;
            Class<?> type = types[columnIndex - 1];
            if (TITLES[columnIndex - 1].equals("上电时间")) {
                return String.class;
            } else if (type == java.util.Date.class)
                return String.class;
            else if (type == Boolean.class) {
                if (mData != null && !mData.isEmpty() && mData.get(0) != null) {
                    Field field = null;
                    try {
                        field = mData.get(0).getClass().getDeclaredField(fieldNames[columnIndex - 1]);
                        String[] choices = getDescriptorFields(field);
                        if (choices != null && choices.length > 1)
                            return String.class;
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
                return type;
            }
            return type;
        }

        public Object getValueAt(int row, int col) {
            if (col == 0)
                return row + 1;

            T info = mData.get(row);
            try {
                Field field = info.getClass().getDeclaredField(fieldNames[col - 1]);
                field.setAccessible(true);
                Object value = field.get(info);
                if (value != null && value instanceof java.util.Date)
                    return turnData2String((Date) value);
                else if (value != null && value instanceof Boolean) {
                    String[] choices = getDescriptorFields(field);
                    if (choices != null && choices.length > 1)
                        return choices[((Boolean) value) ? 1 : 0];
                    return value;
                } else if (TITLES[col - 1].equals("上电时间")) {
                    return turnUptime2String((Integer) value);
                }
                return value;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private String turnUptime2String(int time) {
        int ms = (int) (time % 1000);
        int s = (int) (time / 1000);
        int m = s / 60;
        int h = m / 60;
        int day = h / 24;
        h %= 24;
        m %= 60;
        s %= 60;

        StringBuilder sb = new StringBuilder();
        if (day > 0)
            sb.append(day).append("天 ");
        if (h > 0)
            sb.append(h).append(':');

        sb.append(String.format("%02d:%02d", m, s));
        return sb.toString();
    }

    private Object turnData2String(Date value) {
        long curTime = System.currentTimeMillis();
        long today = curTime / 1000 / 60 / 60 / 24;
        long day = value.getTime() / 1000 / 60 / 60 / 24;
        if (today == day)
            return DateFormat.getTimeInstance().format(value);
        else if (today - day == 1)
            return "昨天 " + DateFormat.getTimeInstance().format(value);
        else if (today - day == 2)
            return "前天 " + DateFormat.getTimeInstance().format(value);

        return DateFormat.getDateTimeInstance().format(value);
    }

    public String getDescription(Field field) {
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

    public String[] getDescriptorFields(Field field) {
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

    public String getDescription(Class cls) {
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
}
