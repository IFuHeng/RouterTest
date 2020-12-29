package com.changhong.telnettool.tool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.nio.charset.Charset;

public class Preferences {

    static final String PATH_SETTINGS = "settings.info";
    /**
     * 缓存数据
     */
    private volatile JSONObject mJson;

    private static Preferences sInstance;

    public static Preferences getInstance() {
        System.out.println("getInstance run in --->  " + Thread.currentThread().getName());
        if (sInstance == null)
            synchronized (PATH_SETTINGS) {
                if (sInstance == null) {
                    sInstance = new Preferences();
                    System.out.println("create Preferences run in --->  " + Thread.currentThread().getName());
                } else
                    System.out.println("Preferences is created ,run in --->  " + Thread.currentThread().getName());

            }
        return sInstance;
    }

    private Preferences() {
        mJson = getContentJson();
        if (mJson == null)
            mJson = new JSONObject();
    }

    public synchronized String readString(String key) {
        return readString(key, null);
    }

    public synchronized String readString(String key, String defaultValue) {
        if (mJson.containsKey(key))
            try {
                return mJson.getString(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return defaultValue;
    }

    public synchronized Preferences saveString(String key, String value) {
        return save(key, value);
    }

    public synchronized int readInt(String key, int defaultValue) {
        if (mJson.containsKey(key))
            try {
                return mJson.getIntValue(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return defaultValue;
    }

    public synchronized Preferences saveInt(String key, int value) {
        return save(key, value);
    }

    public synchronized short readShort(String key, short defaultValue) {
        if (mJson.containsKey(key))
            try {
                return mJson.getShortValue(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return defaultValue;
    }

    public synchronized Preferences saveShort(String key, short value) {
        return save(key, value);
    }

    public synchronized float readFloat(String key, float defaultValue) {
        if (mJson.containsKey(key))
            try {
                return mJson.getFloatValue(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return defaultValue;
    }

    public synchronized Preferences saveFloat(String key, float value) {
        return save(key, value);
    }

    public synchronized byte readByte(String key, byte defaultValue) {
        if (mJson.containsKey(key))
            try {
                return mJson.getByteValue(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return defaultValue;
    }

    public synchronized Preferences saveByte(String key, byte value) {
        return save(key, value);
    }

    public synchronized boolean readBoolean(String key, boolean defaultValue) {
        if (mJson.containsKey(key))
            try {
                return mJson.getBooleanValue(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return defaultValue;
    }

    public synchronized Preferences saveBoolean(String key, boolean value) {
        return save(key, value);
    }

    public synchronized long readLong(String key, long defaultValue) {
        if (mJson.containsKey(key))
            try {
                return mJson.getLongValue(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return defaultValue;
    }

    public synchronized Preferences saveLong(String key, long value) {
        return save(key, value);
    }

    public synchronized <T> T readObject(String key, Class<T> clz) {
        if (mJson.containsKey(key))
            try {
                return mJson.getObject(key, clz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }

    public synchronized String[] readStringArray(String key) {
        if (mJson.containsKey(key)) {
            try {
                JSONArray jsonarr = mJson.getJSONArray(key);
                if (jsonarr.size() > 0) {
                    String[] result = new String[jsonarr.size()];
                    for (int i = 0; i < result.length; i++) {
                        result[i] = jsonarr.getString(i);
                    }
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public synchronized Preferences saveStringArray(String key, String[] value) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < value.length; i++) {
            jsonArray.add(value[i]);
        }
        mJson.put(key, jsonArray);
        return this;
    }

    public synchronized double readDouble(String key, double defaultValue) {
        if (mJson.containsKey(key))
            try {
                return mJson.getDouble(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return defaultValue;
    }

    public synchronized Preferences saveDouble(String key, double value) {
        return save(key, value);
    }

    private synchronized JSONObject getContentJson() {
        String string = IOUtils.loadFile(PATH_SETTINGS);
        if (string != null)
            return JSONObject.parseObject(string);
        else
            return null;
    }

    public synchronized Preferences save(String key, Object value) {
        mJson.put(key, value);
        return this;
    }

    public synchronized void commit() {
        saveJSONContent(mJson);
    }

    private synchronized void saveJSONContent(JSONObject jsonObject) {
        synchronized (PATH_SETTINGS) {
            File file = new File(PATH_SETTINGS);
            IOUtils.writeFile(file, jsonObject.toJSONString(), Charset.defaultCharset().name());
        }
    }
}
