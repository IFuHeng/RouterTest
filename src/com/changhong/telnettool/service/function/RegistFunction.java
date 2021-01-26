package com.changhong.telnettool.service.function;

import com.alibaba.fastjson.JSONObject;
import com.changhong.telnettool.database.SQLiteJDBC;
import com.changhong.telnettool.service.ResponseBeen;
import com.changhong.telnettool.service.ServiceCallback;
import com.changhong.telnettool.service.TestService;
import com.changhong.telnettool.service.been.RegisterData;
import com.sun.org.glassfish.gmbal.Description;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.regex.Pattern;

@Description("注册")
public class RegistFunction implements ServiceCallback {
    public static final String PATTERN_MAC = "/^[A-F0-9a-f]{2}(-[A-F0-9a-f]{2}){5}$|^[A-F0-9a-f]{2}(:[A-F0-9a-f]{2}){5}$|^[A-F0-9a-f]{12}$|^[A-F0-9a-f]{4}(\\.[A-F0-9a-f]{4}){2}$";

    @Override
    public ResponseBeen callback(String ip, String string) throws Exception {
        RequestData obj = JSONObject.parseObject(string, RequestData.class);

        if (obj.mac == null)
            throw new Exception("mac is null");

        if (!Pattern.matches(PATTERN_MAC, obj.mac))
            throw new Exception("Mac format error: " + obj.mac);

        JSONObject json = new JSONObject();
        json.put("version", TestService.SOFT_VER);
        json.put("client_port", TestService.PORT_OF_CLIENT);
        SQLiteJDBC<RegisterData> sqLiteJDBC = new SQLiteJDBC("Client.db", RegisterData.class);
        Map<Integer, RegisterData> map = sqLiteJDBC.selectByChangeEvent("mac", obj.mac);
        if (map.isEmpty()) {
            sqLiteJDBC.insert(new RegisterData(obj.mac, ip, obj.version));
            return new ResponseBeen(0, "Regist complete.", json);
        } else {
            Iterator<Map.Entry<Integer, RegisterData>> iterator = map.entrySet().iterator();
            long lastTime = 0;
            for (int i = 0; iterator.hasNext(); i++) {
                Map.Entry<Integer, RegisterData> next = iterator.next();
                if (i == 0) {
                    lastTime = next.getValue().getTime();
                    next.getValue().setTime(System.currentTimeMillis());
                    sqLiteJDBC.update(next.getKey(), next.getValue());
                } else { // 多余的都删除掉
                    sqLiteJDBC.deleteById(next.getKey());
                }
            }
            return new ResponseBeen(0, "Last regist at " + new Date(lastTime), json);
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    class RequestData {
        String mac;
        String type;
        String version;

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
