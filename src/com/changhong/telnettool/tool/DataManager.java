package com.changhong.telnettool.tool;

import com.changhong.telnettool.been.CommandBeen;
import com.changhong.telnettool.database.SQLiteJDBC;
import javafx.util.Pair;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataManager {

    private static final String PATH = "data.dat";
    private static final String VERSION_INFO = "v.info";
    private static final String PATH_SETTINGS = "setting.db";

    public static List<CommandBeen> load() {
        ArrayList<CommandBeen> result = new ArrayList<>();
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(PATH));
            String line;
            while ((line = fileReader.readLine()) != null) {
                result.add(new CommandBeen(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static void save(List<CommandBeen> data) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(PATH));
            for (int i = 0; i < data.size(); i++) {
                if (i > 0)
                    bufferedWriter.write('\n');
                bufferedWriter.write(data.get(i).toSaveString());
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static <T> Map<Integer, T> loadFromSql(String key, Class<T> vCls) {
        SQLiteJDBC<T> sqLiteJDBC = new SQLiteJDBC<>(PATH_SETTINGS, vCls);
        return sqLiteJDBC.select();
    }

    public static <T> void saveToSql(String key, T item) {
        SQLiteJDBC<T> sqLiteJDBC = new SQLiteJDBC(PATH_SETTINGS, item.getClass());
        sqLiteJDBC.insert(item);
    }

    public static Pair<String, Integer> getVersionInfo() {

        URL url = DataManager.class.getClassLoader().getResource(VERSION_INFO);
        if (url == null) {
            File file = new File(VERSION_INFO);
            if (!file.exists())
                return null;
            try {
                url = new URL("file:/" + file.getAbsolutePath());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }

        String versionName = null;
        int versionCode = -1;
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = fileReader.readLine()) != null) {
                if (line.startsWith("versionCode")) {
                    versionCode = Integer.parseInt(line.substring(line.indexOf('=') + 1));
                } else if (line.startsWith("versionName")) {
                    versionName = line.substring(line.indexOf('=') + 1);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (versionCode != -1 && versionName != null)
            return new Pair<>(versionName, versionCode);
        else
            return null;
    }

//    public void output(File file, File outFile) {
//
//        BufferedReader br = null;
//        FileOutputStream fos = null;
//        try {
//            br = new BufferedReader(new FileReader(file));
//
//            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
//            HSSFSheet hssfSheet = hssfWorkbook.createSheet("三国志吞食天地孔明传");
//            String temp;
//            int rowIndex = 0;
//            while ((temp = br.readLine()) != null) {
//                System.out.println(temp);
//                HSSFRow row = hssfSheet.createRow(rowIndex++);
//                String[] tempArr = temp.split(" ");
//                for (int i = 0; i < tempArr.length; ++i) {
//                    HSSFCell cell = row.createCell(i);
//                    if (Pattern.matches("\\d+$", tempArr[i]))
//                        cell.setCellValue(Integer.parseInt(tempArr[i]));
//                    else
//                        cell.setCellValue(tempArr[i]);
//                }
//            }
//            br.close();
//            br = null;
//
//            fos = new FileOutputStream(outFile);
//            fos.write(hssfWorkbook.getBytes());
//            fos.flush();
//            System.out.printf("create xls file [%s] completed.\n", outFile.getAbsolutePath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null)
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            if (fos != null)
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//        }
//    }

//    public static MenuBar createMenubar(String menuFile) {
//        MenuBar menubar = new MenuBar();
//        BufferedReader br = new BufferedReader(new InputStreamReader(menubar.getClass()
//                .getClassLoader().getResourceAsStream(menuFile)));
//        if (br != null)
//            try {
//                JsonObject jsonObject = new JsonObject();
//                Gson gons  = new Gson();
//
//                for (String str = br.readLine(); str != null; str = br
//                        .readLine()) {
//
//                    if (str.length() < 1)
//                        continue;
//                    else {
//
//                        int index = str.indexOf(':');
//                        index = index == -1 ? str.indexOf("：") : index;
//                        if (index == -1)
//                            continue;
//                        Menu menui = new Menu(str.substring(0, index).trim());
//
//                        menubar.add(menui);
//
//                        String[] arrStr = tool.getStrings(str
//                                .substring(index + 1), ',');
//                        for (int i = 0; i < arrStr.length; ++i) {
//                            String temp = arrStr[i].trim();
//                            if (temp.equals("|"))
//                                menui.addSeparator();
//                            else {
//                                MenuItem menuItem = new MenuItem(temp);
//                                menui.add(menuItem);
//                                menuItem.addActionListener(this);
//                            }
//                        }
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        return menubar;
//    }

}
