package com.changhong.telnettool.been;

import com.alibaba.fastjson.JSONArray;
import com.changhong.telnettool.tool.Tool;

import java.util.ArrayList;
import java.util.List;

public class HttpInterfaceData {

    private static final String PATH = "interface.json";
    private List<Item> array = new ArrayList<>();

    public HttpInterfaceData() {
        String fileContent = Tool.loadLocalFile(PATH);

        if (fileContent == null)
            fileContent = Tool.loadFile(PATH);

        if (fileContent == null)
            return;

        array = JSONArray.parseArray(fileContent, Item.class);
        System.out.println(array);
    }

    public List<Item> getArray() {
        return array;
    }

    public List<String> getNameList() {
        if (array == null)
            return null;

        ArrayList<String> result = new ArrayList<>();
        for (Item item : array) {
            result.add(item.getName());
        }
        return result;
    }

    public static final class Item {

        String name;

        String path;
        String params;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getParams() {
            return params;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", path='" + path + '\'' +
                    ", params='" + params + '\'' +
                    '}';
        }
    }
}
