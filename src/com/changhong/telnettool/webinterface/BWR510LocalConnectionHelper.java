package com.changhong.telnettool.webinterface;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.changhong.telnettool.webinterface.been.EnumPPPoeState;
import com.changhong.telnettool.webinterface.been.Group;
import com.changhong.telnettool.webinterface.been.RequestBeen;
import com.changhong.telnettool.webinterface.been.StaInfo;
import com.changhong.telnettool.webinterface.been.guide.RequireAllBeen;
import com.changhong.telnettool.webinterface.been.guide.ResponseAllBeen;
import com.changhong.telnettool.webinterface.been.mesh.CustomerInfo;
import com.changhong.telnettool.webinterface.been.mesh.ListInfo;
import com.changhong.telnettool.webinterface.been.mesh.MeshRequireAllBeen;
import com.changhong.telnettool.webinterface.been.mesh.MeshResponseAllBeen;
import com.changhong.telnettool.webinterface.been.status.RequireAndResponseBeen;
import com.changhong.telnettool.webinterface.been.sys.ServiceRequireAllBeen;
import com.changhong.telnettool.webinterface.been.sys.ServiceResponseAllBeen;
import com.changhong.telnettool.webinterface.been.sys.SettingResponseAllBeen;
import com.changhong.telnettool.webinterface.been.wan.Level2Been;
import com.changhong.telnettool.webinterface.been.wan.WanRequireAllBeen;
import com.changhong.telnettool.webinterface.been.wan.WanResponseAllBeen;
import com.changhong.telnettool.webinterface.been.wifi.GuestRequireAndResponseBeen;
import com.changhong.telnettool.webinterface.been.wifi.ReqireWlanAccessDelBeen;
import com.changhong.telnettool.webinterface.been.wifi.WifiRequireAllBeen;
import com.changhong.telnettool.webinterface.been.wifi.WifiResponseAllBeen;
import javafx.util.Pair;
import okhttp3.*;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.*;

public class BWR510LocalConnectionHelper implements HttpRequestMethod {

    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//    private final ExecutorService mThreadPool;

    private String cookies;
    private String password;

    private static BWR510LocalConnectionHelper sInstance;

    private static final boolean showLog = true;

    public static BWR510LocalConnectionHelper getInstance() {
        if (sInstance == null)
            sInstance = new BWR510LocalConnectionHelper();
        return sInstance;
    }

    public BWR510LocalConnectionHelper() {
//        mThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    /**
     * login ,username is 'user'.
     *
     * @param gateway  the gate way of wlan
     * @param password the password of user
     * @param observer
     */
    public void login(String gateway, final String password, Observer<String> observer) {
        try {
            observer.onNext(login(gateway, password));
        } catch (Exception e) {
            observer.onError(e);
        } finally {
            observer.onComplete();
        }
    }

    public String login(String gateway, final String password) throws IOException {
        final String url = String.format("http://%s:80/login.html", gateway);
        String param = "password=" + password;
        cookies = httpLogin(url, param);
        this.password = password;
        return cookies;
    }

    /**
     * get base info about the devcie type
     *
     * @param gateway the gate way of wlan
     * @throws IOException
     */
    public void getBase_DeviceType(String gateway, Observer<String> observer) {

        try {
            observer.onNext(getBase_DeviceType(gateway));
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }
    }

    /**
     * get base info about the devcie type
     *
     * @param gateway the gate way of wlan
     * @throws IOException
     */
    public String getBase_DeviceType(String gateway) throws Exception {
        final String url = "http://" + gateway + ":80/rpcsupper";
        final String body = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"device_type_get\",\"params\":{\"src_type\":1}}";

        String response = httpPostWithJson(url, body);
        SettingResponseAllBeen been = JSONObject.parseObject(response, SettingResponseAllBeen.class);
        if (been.getErr_code() == 0) {
            return been.getDev_type();
        } else {
            throw new Exception(been.getMessage());
        }
    }

    /**
     * get wizard guide state: 0.not guide; 1.guide completed; 2.only set password
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public void checkWizardGuide(String gateway, Observer<Integer> observer) {
        try {
            observer.onNext(checkWizardGuide(gateway));
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }
    }

    /**
     * get wizard guide state: 0.not guide; 1.guide completed; 2.only set password
     *
     * @param gateway the gate way of wlan
     * @return 0.not guide; 1.guide completed; 2.only set password
     */
    public int checkWizardGuide(String gateway) throws Exception {
        final String url = "http://" + gateway + ":80/rpcsupper";
        final String body = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"wizard_get_guid\",\"params\":{\"src_type\":1}}";

        String response = httpPostWithJson(url, body);
        ResponseAllBeen been = JSONObject.parseObject(response, ResponseAllBeen.class);
        if (been.getErr_code() != 0)
            throw new Exception(been.getMessage());
        else
            return been.getGuid_flag();
    }

    /**
     * get router info
     *
     * @param gateway the gate way of wlan
     */
    public void getBase_RouterInfo(String gateway, Observer<SettingResponseAllBeen> observer) {
        try {
            SettingResponseAllBeen responseAllBeen = getBase_RouterInfo(gateway);
            observer.onNext(responseAllBeen);
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }
    }

    /**
     * get router info
     *
     * @param gateway the gate way of wlan
     */
    public SettingResponseAllBeen getBase_RouterInfo(String gateway) throws Exception {
        final String url = "http://" + gateway + ":80/rpc";
        String body = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"router_info_show\",\"params\":{\"src_type\":1}}";
//        final String body = new RequestBeen(METHOD_ROUTER_INFO_SHOW, new SettingRequireAllBeen()).toJsonString();
        String response = httpPostWithJson(url, body, cookies);
        SettingResponseAllBeen responseAllBeen = JSONObject.parseObject(response, SettingResponseAllBeen.class);
        if (responseAllBeen.getErr_code() != 0)
            throw new Exception(responseAllBeen.getMessage());
        else
            return responseAllBeen;
    }

    /**
     * get guide wifi default set
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public void getWizard_WifiSet(String gateway, Observer<ResponseAllBeen> observer) {
        try {
            ResponseAllBeen been = getWizard_WifiSet(gateway);
            observer.onNext(been);
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }
    }

    /**
     * get guide wifi default set
     *
     * @param gateway the gate way of wlan
     */
    public ResponseAllBeen getWizard_WifiSet(String gateway) throws Exception {
        final String url = "http://" + gateway + ":80/rpc";
        RequireAllBeen requireBody = new RequireAllBeen();
//        String body = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"wizard_get_wireless\",\"params\":{\"src_type\":1}}";
        final String body = new RequestBeen(METHOD_WIZARD_GET_WIRELESS, requireBody).toJsonString();
        String response = httpPostWithJson(url, body, cookies);

        ResponseAllBeen been
                = JSONObject.parseObject(response, ResponseAllBeen.class);
        if (been.getErr_code() != 0) {
            throw new Exception(been.getMessage());
        } else
            return been;
    }

    /**
     * get guide wan default set
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public void getWizard_WanSet(String gateway, Observer<? super ResponseAllBeen> observer) {
        try {
            observer.onNext(getWizard_WanSet(gateway));
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }
    }

    public ResponseAllBeen getWizard_WanSet(String gateway) throws Exception {
        final String url = "http://" + gateway + ":80/rpc";
        WifiRequireAllBeen requireBody = new WifiRequireAllBeen();
        final String body = new RequestBeen(METHOD_WIZARD_GET_NETWORK, requireBody).toJsonString();
        String response = httpPostWithJson(url, body, cookies);

        ResponseAllBeen been = JSONObject.parseObject(response, ResponseAllBeen.class);
        if (been.getErr_code() != 0) {
            throw new Exception(been.getMessage());
        } else
            return been;
    }

    /**
     * send reboot command to the router
     *
     * @param gateway the gate way of wlan
     * @throws Exception
     */
    public <T> void reboot(String gateway, Observer<T> observer) {
        final String url = "http://" + gateway + ":80/rpc";
        final String body = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"" + METHOD_REBOOT + "\",\"params\":{\"src_type\":1}}";

        try {

            String response = httpPostWithJson(url, body, cookies);
            SettingResponseAllBeen been
                    = JSONObject.parseObject(response, SettingResponseAllBeen.class);
            if (been.getErr_code() != 0) {
                observer.onError(new Exception(been.getMessage()));
            }
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * send recovery command to the router
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public <T> void recovery(String gateway, Observer<T> observer) {

        final String url = "http://" + gateway + ":80/rpc";
        final String body = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"" + METHOD_RESET + "\",\"params\":{\"src_type\":1}}";

        try {
            String response = httpPostWithJson(url, body, cookies);
            SettingResponseAllBeen been
                    = JSONObject.parseObject(response, SettingResponseAllBeen.class);
            if (been.getErr_code() != 0) {
                observer.onError(new Exception(been.getMessage()));
            }
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * add mesh child
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public <T> void addMesh(String gateway, List<StaInfo> devList, Observer<T> observer) {

        final String url = "http://" + gateway + ":80/rpc";

        MeshRequireAllBeen requireBeen = new MeshRequireAllBeen();
        requireBeen.setOp(1);

        List<ListInfo> list = new ArrayList<>();
        for (StaInfo staInfo : devList) {
            ListInfo item = new ListInfo();
            item.setIp(staInfo.getIp());
            item.setMac(staInfo.getMac().replace(":", ""));
            list.add(item);
        }
        requireBeen.setDev(list);
        final String body = new RequestBeen<>(METHOD_NETWORK_ADD, requireBeen).toJsonString();

        try {
            String response = httpPostWithJson(url, body, cookies);
            MeshResponseAllBeen been = JSONObject.parseObject(response, MeshResponseAllBeen.class);
            if (been.getErr_code() != 0) {
                observer.onError(new Exception(been.getMessage()));
            }
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * get mesh quick switch state
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public void getMeshQuickLink(String gateway, Observer<Boolean> observer) {

        final String url = "http://" + gateway + ":80/rpc";

        MeshRequireAllBeen requireBeen = new MeshRequireAllBeen();
        // 当前状态与目标状态不同，切换请求
        final String body = new RequestBeen<>(METHOD_MESH_QUICK_SHOW, requireBeen).toJsonString();


        try {
            String response = httpPostWithJson(url, body, cookies);
            MeshResponseAllBeen been = JSONObject.parseObject(response, MeshResponseAllBeen.class);
            if (been.getErr_code() != 0) {
                observer.onError(new Exception(been.getMessage()));
            } else {
                observer.onNext(been.getQuick_status() == 1);
            }
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * open or close the mesh quick link
     *
     * @param gateway  the gate way of wlan
     * @param enable
     * @param observer
     */
    public <T> void setMeshQuickLink(String gateway, boolean enable, Observer<T> observer) {

        final String url = "http://" + gateway + ":80/rpc";

        MeshRequireAllBeen requireBeen = new MeshRequireAllBeen();
        // 当前状态与目标状态不同，切换请求
        requireBeen.setEnable(enable ? 1 : 0);
        final String body = new RequestBeen<>(METHOD_MESH_QUICK_LINK, requireBeen).toJsonString();


        try {
            String response = httpPostWithJson(url, body, cookies);
            MeshResponseAllBeen been = JSONObject.parseObject(response, MeshResponseAllBeen.class);
            if (been.getErr_code() != 0) {
                observer.onError(new Exception(been.getMessage()));
            }
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * delete one visit access device by mac
     *
     * @param gateway             the gate way of wlan
     * @param mac                 the mac of device,do not contain colon
     * @param isEffectImmediately
     * @param observer
     */
    public <T> void deleteWlanAccess(String gateway, String mac, boolean isEffectImmediately, Observer<T> observer) {

        final String url = "http://" + gateway + ":80/rpc";

        mac = mac.replaceAll(":", "");
        ArrayList<String> macList = new ArrayList<>();
        macList.add(mac);
        ReqireWlanAccessDelBeen requireBeen = new ReqireWlanAccessDelBeen();
        requireBeen.setList(macList);
        requireBeen.setAction_flag(isEffectImmediately ? 1 : 0);

        final String body = new RequestBeen<>(METHOD_ACCESS_DEL, requireBeen).toJsonString();


        try {
            String response = httpPostWithJson(url, body, cookies);
            MeshResponseAllBeen been = JSONObject.parseObject(response, MeshResponseAllBeen.class);
            if (been.getErr_code() != 0) {
                observer.onError(new Exception(been.getMessage()));
            }
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * check pppoe connected state until connected or disconneced or error
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public void checkPPPoeState(String gateway, Observer<EnumPPPoeState> observer) {
        final String url = "http://" + gateway + ":80/rpc";
        final String body = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"" + METHOD_CHECK_PPPOE_STATE + "\",\"params\":{\"src_type\":1}}";
        try {
            while (true) {
                String response = httpPostWithJson(url, body, cookies);
                WanResponseAllBeen been = JSONObject.parseObject(response, WanResponseAllBeen.class);
                if (been.getErr_code() == 0) {
                    if (been.getPppoe_state() == null) {
                        observer.onNext(EnumPPPoeState.UNKNOWN_ERROR);
                        throw new UnknownError();
                    } else if (been.getPppoe_state() == EnumPPPoeState.CONNECTING.getValue()) {
                        observer.onNext(EnumPPPoeState.CONNECTING);
                        Thread.sleep(500);
                        continue;
                    } else if (been.getPppoe_state() == EnumPPPoeState.ACCOUNT_OR_PASSWORD_ERROR.getValue()) {
                        observer.onNext(EnumPPPoeState.ACCOUNT_OR_PASSWORD_ERROR);
                        break;
                    } else if (been.getPppoe_state() == EnumPPPoeState.DISCONNECT.getValue()) {
                        observer.onNext(EnumPPPoeState.DISCONNECT);
                        break;
                    } else if (been.getPppoe_state() == EnumPPPoeState.CONNECTED.getValue()) {
                        observer.onNext(EnumPPPoeState.CONNECTED);
                        break;
                    } else {
                        observer.onNext(EnumPPPoeState.UNKNOWN_ERROR);
                        break;
                    }
                } else
                    throw new Exception(been.getMessage());
            }
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * check pppoe connected state until connected or disconneced or error
     *
     * @param gateway the gate way of wlan
     */
    public EnumPPPoeState checkPPPoeState(String gateway) throws Exception {

        final String url = "http://" + gateway + ":80/rpc";

        final String body = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"" + METHOD_CHECK_PPPOE_STATE + "\",\"params\":{\"src_type\":1}}";

        String response = httpPostWithJson(url, body, cookies);
        WanResponseAllBeen been = JSONObject.parseObject(response, WanResponseAllBeen.class);
        if (been.getErr_code() == 0) {
            if (been.getPppoe_state() == null) {
                return EnumPPPoeState.UNKNOWN_ERROR;
            } else if (been.getPppoe_state() == EnumPPPoeState.CONNECTING.getValue()) {
                return EnumPPPoeState.CONNECTING;
            } else if (been.getPppoe_state() == EnumPPPoeState.ACCOUNT_OR_PASSWORD_ERROR.getValue()) {
                return EnumPPPoeState.ACCOUNT_OR_PASSWORD_ERROR;
            } else if (been.getPppoe_state() == EnumPPPoeState.DISCONNECT.getValue()) {
                return EnumPPPoeState.DISCONNECT;
            } else if (been.getPppoe_state() == EnumPPPoeState.CONNECTED.getValue()) {
                return EnumPPPoeState.CONNECTED;
            } else {
                return EnumPPPoeState.UNKNOWN_ERROR;
            }
        } else
            throw new Exception(been.getMessage());
    }

    /**
     * load speed limit list
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public void getSpeedLimit(String gateway, Observer<List<Level2Been>> observer) {

        final String url = "http://" + gateway + ":80/rpc";

        final String body = new RequestBeen<>(METHOD_MAC_RATE_LIMIT_SHOW, new WanRequireAllBeen()).toJsonString();


        try {
            String response = httpPostWithJson(url, body, cookies);
            WanResponseAllBeen been
                    = JSONObject.parseObject(response, WanResponseAllBeen.class);
            if (been.getErr_code() != 0) {
                observer.onError(new Exception(been.getMessage()));
            } else
                observer.onNext(been.getInfo_list());
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * load ddns option
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public void getDDNS(String gateway, Observer<ServiceResponseAllBeen> observer) {

        final String url = "http://" + gateway + ":80/rpc";

        final String body = new RequestBeen<>(METHOD_DDNS_SHOW, new WanRequireAllBeen()).toJsonString();


        try {
            String response = httpPostWithJson(url, body, cookies);
            ServiceResponseAllBeen been
                    = JSONObject.parseObject(response, ServiceResponseAllBeen.class);
            if (been.getErr_code() != 0) {
                observer.onError(new Exception(been.getMessage()));
            } else
                observer.onNext(been);
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * set DDNS
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public <T> void setDDNS(String gateway, final boolean isCreate, final ServiceRequireAllBeen been, Observer<T> observer) {

        final String url = "http://" + gateway + ":80/rpc";


        try {
            //创建
            String response = httpPostWithJson(url, new RequestBeen<>(METHOD_DDNS_SETTING, been).toJsonString(), cookies);
            ServiceResponseAllBeen been1 = JSONObject.parseObject(response, ServiceResponseAllBeen.class);
            if (been1.getErr_code() != 0) {
                throw new Exception(been1.getMessage());
            }

            //关闭
            if (isCreate) {
                response = httpPostWithJson(url, new RequestBeen<>(METHOD_DDNS_SETTING, been1).toJsonString(), cookies);
                been1 = JSONObject.parseObject(response, ServiceResponseAllBeen.class);
                if (been1.getErr_code() != 0) {
                    throw new Exception(been1.getMessage());
                }
            }
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * 10.5.2	 获取用户自定义个性化数据
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public void getDeviceCustomerInfo(String gateway, Observer<List<CustomerInfo>> observer) {

        final String url = "http://" + gateway + ":80/rpc";

        final String body = new RequestBeen<>(METHOD_DEVICE_CUSTOMER_INFO_SHOW, new WanRequireAllBeen()).toJsonString();


        try {
            String response = httpPostWithJson(url, body, cookies);
            MeshResponseAllBeen been
                    = JSONObject.parseObject(response, MeshResponseAllBeen.class);
            if (been.getErr_code() != 0) {
                observer.onError(new Exception(been.getMessage()));
            } else
                observer.onNext(been.getDev_list());
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * 10.5.2	 获取用户自定义个性化数据
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public void getGroupLimit(String gateway, Observer<Pair<List<Group>, List<Level2Been>>> observer) {

        final String url = "http://" + gateway + ":80/rpc";


        try {
            List<Group> groupInfo = getStaGroupInfo(url, cookies);
            List<Level2Been> staLimitInfo = getTimeLimitShow(url, cookies);
            observer.onNext(new Pair(groupInfo, staLimitInfo));
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * delete limit speed
     *
     * @param gateway             the gate way of wlan
     * @param macs                the mac of device,do not contain colon
     * @param isEffectImmediately
     * @param observer
     */
    public <T> void deleteSpeedLimitsLoad(String gateway, final List<String> macs, final boolean isEffectImmediately, Observer<T> observer) {

        final String url = "http://" + gateway + ":80/rpc";


        try {
            for (int i = 0; i < macs.size(); i++) {
                String mac = macs.get(i);
                WanRequireAllBeen require = new WanRequireAllBeen();
                require.setMac(mac);
                require.setMax_up_bandwidth(0);
                require.setMax_down_bandwidth(0);
                require.setMode(0);

                if (isEffectImmediately) {
                    require.setAction_flag(i == macs.size() - 1 ? 1 : 0);
                } else {
                    require.setAction_flag(0);
                }

                String response = httpPostWithJson(url, new RequestBeen<>(METHOD_MAC_RATE_LIMIT_SETTING, require).toJsonString(), cookies);
                WanResponseAllBeen been
                        = JSONObject.parseObject(response, WanResponseAllBeen.class);

                if (been.getErr_code() != 0) {
                    observer.onError(new Exception(been.getMessage()));
                    break;
                }
            }
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * get guest network option
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public void getGuestNetwork(String gateway, Observer<GuestRequireAndResponseBeen> observer) {

        final String url = "http://" + gateway + ":80/rpc";

        final String body = new RequestBeen<>(METHOD_GUEST_NETCORK_SHOW, new WanRequireAllBeen()).toJsonString();

        try {
            String response = httpPostWithJson(url, body, cookies);
            GuestRequireAndResponseBeen been = JSONObject.parseObject(response, GuestRequireAndResponseBeen.class);
            if (been.getErr_code() != 0) {
                observer.onError(new Exception(been.getMessage()));
            } else
                observer.onNext(been);
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * get lan option
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public void getLanInfo(String gateway, Observer<WanResponseAllBeen> observer) {

        final String url = "http://" + gateway + ":80/rpc";

        final String body = new RequestBeen<>(METHOD_LAN_SHOW, new WanRequireAllBeen()).toJsonString();

        try {
            observer.onNext(getLanInfo(gateway));
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }
    }

    /**
     * get lan option
     *
     * @param gateway the gate way of wlan
     * @return
     */
    public WanResponseAllBeen getLanInfo(String gateway) throws Exception {

        final String url = "http://" + gateway + ":80/rpc";

        final String body = new RequestBeen<>(METHOD_LAN_SHOW, new WanRequireAllBeen()).toJsonString();

        String response = httpPostWithJson(url, body, cookies);
        WanResponseAllBeen been = JSONObject.parseObject(response, WanResponseAllBeen.class);
        if (been.getErr_code() != 0) {
            throw new Exception(been.getMessage());
        }
        return been;
    }

    /**
     * get mesh can link device
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public void getMeshNetwork(String gateway, Observer<List<ListInfo>> observer) {
        try {
            observer.onNext(getMeshNetwork(gateway));
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }
    }

    /**
     * get mesh can link device
     *
     * @param gateway the gate way of wlan
     * @return
     */
    public List<ListInfo> getMeshNetwork(String gateway) throws Exception {

        final String url = "http://" + gateway + ":80/rpc";

        final String body = new RequestBeen<>(METHOD_NETWORK_SHOW, new WanRequireAllBeen()).toJsonString();

        String response = httpPostWithJson(url, body, cookies);
        MeshResponseAllBeen been = JSONObject.parseObject(response, MeshResponseAllBeen.class);
        if (been.getErr_code() != 0) {
            throw new Exception(been.getMessage());
        }
        return been.getMesh_clients();

    }

    /**
     * get router status
     *
     * @param gateway  the gate way of wlan
     * @param observer
     * @deprecated not used
     */
    public void getRouterStatus(String gateway, Observer<RequireAndResponseBeen> observer) {

        final String url = "http://" + gateway + ":80/rpc";

        final String body = new RequestBeen<>(METHOD_STATUS_SHOW, new WanRequireAllBeen()).toJsonString();

        try {
            String response = httpPostWithJson(url, body, cookies);
            RequireAndResponseBeen been = JSONObject.parseObject(response, RequireAndResponseBeen.class);
            if (been.getErr_code() != 0) {
                observer.onError(new Exception(been.getMessage()));
            } else
                observer.onNext(been);
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * 6.2.4	获取STA信息
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public void getStaInfo(String gateway, Observer<List<StaInfo>> observer) {
        try {
            observer.onNext(getStaInfo(gateway));
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }
    }

    /**
     * 6.2.4	获取STA信息
     *
     * @param gateway the gate way of wlan
     * @return
     */
    public List<StaInfo> getStaInfo(String gateway) throws Exception {

        final String url = "http://" + gateway + ":80/rpc";

        final String body = new RequestBeen<>(METHOD_STA_INFO_SHOW, new WanRequireAllBeen()).toJsonString();

        String response = httpPostWithJson(url, body, cookies);
        WanResponseAllBeen been = JSONObject.parseObject(response, WanResponseAllBeen.class);
        if (been.getErr_code() != 0) {
            throw new Exception(been.getMessage());
        }
        return been.getSta_info();
    }

    /**
     * 获取WIFI设置信息和高级设置信息
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public void getWifiAdvanceSetting(String gateway, Observer<WifiResponseAllBeen> observer) {

        final String url = "http://" + gateway + ":80/rpc";

        final String body = new RequestBeen<>(METHOD_WLAN_ADVANCED_SHOW, new WanRequireAllBeen()).toJsonString();

        try {
            String response = httpPostWithJson(url, body, cookies);
            WifiResponseAllBeen been = JSONObject.parseObject(response, WifiResponseAllBeen.class);
            if (been.getErr_code() != 0) {
                observer.onError(new Exception(been.getMessage()));
            } else
                observer.onNext(been);
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * 获取WIFI BASE设置信息
     *
     * @param gateway the gate way of wlan
     */
    public Pair<WifiResponseAllBeen, WifiResponseAllBeen> getWifiSetting(String gateway) throws Exception {

        final String url = "http://" + gateway + ":80/rpc";

        WifiRequireAllBeen requireBeen = new WifiRequireAllBeen();
        RequestBeen requestBeen = new RequestBeen(METHOD_WLAN_QUICK_SHOW, requireBeen);
        //获取5G wifi信息
        requireBeen.setFlag(2);
        final String body5G = requestBeen.toJsonString();

        String response = httpPostWithJson(url, body5G, cookies);
//        System.out.println(response);

        WifiResponseAllBeen been5G = JSONObject.parseObject(response, WifiResponseAllBeen.class);
        if (been5G.getErr_code() != 0) {
            throw new Exception(been5G.getMessage());
        } else if (been5G.getPrefer_5g() == 1) {
            return new Pair(been5G, been5G);
        } else {
            //获取2.4G wifi信息
            requireBeen.setFlag(1);
            final String body2G = requestBeen.toJsonString();
            response = httpPostWithJson(url, body2G, cookies);
            WifiResponseAllBeen been2G = JSONObject.parseObject(response, WifiResponseAllBeen.class);
            return new Pair(been2G, been5G);
        }

    }

    /**
     * 获取访问控制信息
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public void getWlanAccess(String gateway, Observer<WifiResponseAllBeen> observer) {

        final String url = "http://" + gateway + ":80/rpc";

        final String body = new RequestBeen<>(METHOD_ACCESS_SHOW, new WifiRequireAllBeen()).toJsonString();

        try {
            String response = httpPostWithJson(url, body, cookies);
            WifiResponseAllBeen responseAllBeen = JSONObject.parseObject(response, WifiResponseAllBeen.class);

            if (responseAllBeen.getErr_code() != 0) {
                observer.onError(new Exception(responseAllBeen.getMessage()));
            } else
                observer.onNext(responseAllBeen);
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * 10.5.1	 设置设备基本信息，主要用于为设备重新命名，添加位置信息
     *
     * @param gateway  the gate way of wlan
     * @param list
     * @param observer
     */
    public <T> void setDeviceCustomerInfo(String gateway, List<CustomerInfo> list, Observer<T> observer) {

        final String url = "http://" + gateway + ":80/rpc";
        MeshRequireAllBeen requireBody = new MeshRequireAllBeen();
        requireBody.setDev_list(list);
        final String body = new RequestBeen<>(METHOD_DEVICE_CUSTOMER_INFO_MODIFY, requireBody).toJsonString();

        try {
            String response = httpPostWithJson(url, body, cookies);

            MeshResponseAllBeen responseAllBeen = JSONObject.parseObject(response, MeshResponseAllBeen.class);

            if (responseAllBeen.getErr_code() != 0) {
                observer.onError(new Exception(responseAllBeen.getMessage()));
            }
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * 更新分组信息
     *
     * @param gateway   the gate way of wlan
     * @param groupList
     * @param staList
     * @param observer
     */
    public <T> void setDeviceCustomerInfo(final String gateway, final List<Group> groupList, final List<Level2Been> staList, Observer<T> observer) {

        final String url = "http://" + gateway + ":80/rpc";

        boolean setStaGroupSuccess = true;
        if (groupList != null)
            try {
                setStaGroupInfo(url, groupList, cookies);
            } catch (Exception e) {
                e.printStackTrace();
                observer.onError(e);
                setStaGroupSuccess = false;
            }

        if (setStaGroupSuccess && staList != null) {
            try {
                for (int i = 0; i < staList.size(); i++) {
                    setTimeLimitShow(url, staList.get(i), cookies, i == staList.size() - 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                observer.onError(e);
            }
        }

        observer.onComplete();

    }

    /**
     * 设置访客网络
     *
     * @param gateway  the gate way of wlan
     * @param been
     * @param observer
     */
    public <T> void setGuestNetwork(String gateway, GuestRequireAndResponseBeen been, Observer<T> observer) {

        final String url = "http://" + gateway + ":80/rpc";
        final String body = new RequestBeen<>(METHOD_GUEST_NETCORK_SET, been).toJsonString();

        try {
            String response = httpPostWithJson(url, body, cookies);

            GuestRequireAndResponseBeen responseAllBeen = JSONObject.parseObject(response, GuestRequireAndResponseBeen.class);

            if (responseAllBeen.getErr_code() != 0) {
                observer.onError(new Exception(responseAllBeen.getMessage()));
            }
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * 设置局域网网络
     *
     * @param gateway  the gate way of wlan
     * @param been
     * @param observer
     */
    public <T> void setLanInfo(String gateway, WanRequireAllBeen been, Observer<T> observer) {

        final String url = "http://" + gateway + ":80/rpc";
        final String body = new RequestBeen<>(METHOD_LAN_SETTING, been).toJsonString();

        try {
            String response = httpPostWithJson(url, body, cookies);

            WanResponseAllBeen responseAllBeen = JSONObject.parseObject(response, WanResponseAllBeen.class);

            if (responseAllBeen.getErr_code() != 0) {
                observer.onError(new Exception(responseAllBeen.getMessage()));
            }
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * 设置局域网网络
     *
     * @param gateway             the gate way of wlan
     * @param gateway
     * @param mac
     * @param up_speed
     * @param down_speed
     * @param isAadd              true 添加；false 删除
     * @param isEffectImmediately 是否立即执行
     * @param observer
     */
    public <T> void setSpeedLimitLoad(String gateway, String mac, int up_speed, int down_speed, boolean isAadd, boolean isEffectImmediately, Observer<T> observer) {

        final String url = "http://" + gateway + ":80/rpc";

        WanRequireAllBeen been = new WanRequireAllBeen();
        if (mac.indexOf(':') != -1)
            mac = mac.replace(":", "");
        been.setMac(mac);
        been.setMax_up_bandwidth(up_speed);
        been.setMax_down_bandwidth(down_speed);
        been.setMode(isAadd ? 1 : 0);
        been.setAction_flag(isEffectImmediately ? 1 : 0);
        final String body = new RequestBeen<>(METHOD_MAC_RATE_LIMIT_SETTING, been).toJsonString();

        try {
            String response = httpPostWithJson(url, body, cookies);

            WanResponseAllBeen responseAllBeen = JSONObject.parseObject(response, WanResponseAllBeen.class);

            if (responseAllBeen.getErr_code() != 0) {
                observer.onError(new Exception(responseAllBeen.getMessage()));
            }
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * 修改wifi高级设置
     *
     * @param gateway  the gate way of wlan
     * @param observer
     */
    public <T> void setWifiAdvanceSetting(String gateway, WifiRequireAllBeen info, Observer<T> observer) {

        final String url = "http://" + gateway + ":80/rpc";
        final String body = new RequestBeen<>(METHOD_WLAN_ADVANCED_SETTING, info).toJsonString();

        try {
            String response = httpPostWithJson(url, body, cookies);

            WanResponseAllBeen responseAllBeen = JSONObject.parseObject(response, WanResponseAllBeen.class);

            if (responseAllBeen.getErr_code() != 0) {
                observer.onError(new Exception(responseAllBeen.getMessage()));
            }
        } catch (Exception e1) {
            observer.onError(e1);
        } finally {
            observer.onComplete();
        }

    }

    /**
     * 修改wifi基本设置
     *
     * @param gateway the gate way of wlan
     * @param beens   需要更新的信息。不可为空
     */
    public void setWifiSetting(String gateway, WifiRequireAllBeen... beens) throws Exception {
        final String url = "http://" + gateway + ":80/rpc";
        if (beens == null || beens.length == 0) {
            throw new NullPointerException("No params");
        }

        for (int i = 0; i < beens.length; i++) {
            final String body = new RequestBeen<>(METHOD_WLAN_QUICK_SETTING, beens[i]).toJsonString();
            String response = httpPostWithJson(url, body, cookies);
            WanResponseAllBeen responseAllBeen = JSONObject.parseObject(response, WanResponseAllBeen.class);
            if (responseAllBeen.getErr_code() != 0) {
                throw new Exception(responseAllBeen.getMessage());
            }
        }
    }

    /**
     * 获取周围无线接入点参数
     *
     * @param gateway the gate way of wlan
     */
    public String getNeighborSSID(String gateway) throws Exception {
        final String url = "http://" + gateway + ":80/rpc";
//        final String body = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"" + METHOD_SCAN_WIRELESS_NETWORK + "\",\"params\":{\"src_type\":1}}";
        final String body = new RequestBeen<>(METHOD_SCAN_WIRELESS_NETWORK, new ReqireWlanAccessDelBeen()).toJsonString();
        String response = httpPostWithJson(url, body, cookies);
        return response;
    }

    /**
     * 修改访问控制权限
     *
     * @param gateway             the gate way of wlan
     * @param enabled             0：关闭；1：白名单；2：黑名单。
     * @param isEffectImmediately 是否立即生效
     */
    public void setWlanAccess(String gateway, int enabled, boolean isEffectImmediately) throws Exception {

        final String url = "http://" + gateway + ":80/rpc";
        WifiRequireAllBeen been = new WifiRequireAllBeen();
        been.setEnabled(enabled);
        been.setAction_flag(isEffectImmediately ? 1 : 0);
        final String body = new RequestBeen<>(METHOD_ACCESS_SETTING, been).toJsonString();

        String response = httpPostWithJson(url, body, cookies);

        WifiResponseAllBeen responseAllBeen = JSONObject.parseObject(response, WifiResponseAllBeen.class);

        if (responseAllBeen.getErr_code() != 0) {
            throw new Exception(responseAllBeen.getMessage());
        }

    }

    /**
     * 提交wan口设置
     *
     * @param gateway the gate way of wlan
     */
    public void setWan(String gateway, WanRequireAllBeen require) throws Exception {

        final String url = "http://" + gateway + ":80/rpc";
        final String body = new RequestBeen<>(METHOD_WAN_SETTING, require).toJsonString();

        String response = httpPostWithJson(url, body, cookies);

        WanResponseAllBeen responseAllBeen = JSONObject.parseObject(response, WanResponseAllBeen.class);

        if (responseAllBeen.getErr_code() != 0) {
            throw new Exception(responseAllBeen.getMessage());
        }
    }

    /**
     * 提交wan口设置
     *
     * @param gateway the gate way of wlan
     */
    public WanResponseAllBeen getWan(String gateway) throws Exception {

        final String url = "http://" + gateway + ":80/rpc";
        RequireAllBeen requireBody = new RequireAllBeen();
        final String body = new RequestBeen<>(METHOD_WAN_SHOW, requireBody).toJsonString();
        String response = httpPostWithJson(url, body, cookies);
        WanResponseAllBeen responseAllBeen = JSONObject.parseObject(response, WanResponseAllBeen.class);
        if (responseAllBeen.getErr_code() != 0) {
            throw new Exception(responseAllBeen.getMessage());
        }
        return responseAllBeen;
    }

    /**
     * 提交wan口设置
     *
     * @param gateway  the gate way of wlan
     * @param wanBeen
     * @param wifiBeen
     */
    public void setWizardWanWifi(String gateway, RequireAllBeen wanBeen, RequireAllBeen wifiBeen) throws Exception {

        final String url = "http://" + gateway + ":80/rpc";
        final String bodyWan = new RequestBeen<>(METHOD_WIZARD_SETTING, wanBeen).toJsonString();
        final String bodyWifi = new RequestBeen<>(METHOD_WIZARD_SET_WIRELESS, wifiBeen).toJsonString();
        final String bodyWizardComplete = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"wizard_set_guid\",\"params\":{\"src_type\":1,\"guid_flag\":1}}";

        String response = httpPostWithJson(url, bodyWan, cookies);
        ResponseAllBeen been = JSONObject.parseObject(response, ResponseAllBeen.class);
        if (been.getErr_code() != 0)
            throw new Exception(been.getMessage());

        response = httpPostWithJson(url, bodyWifi, cookies);
        been = JSONObject.parseObject(response, ResponseAllBeen.class);
        if (been.getErr_code() != 0)
            throw new Exception(been.getMessage());

        response = httpPostWithJson(url, bodyWizardComplete, cookies);
        been = JSONObject.parseObject(response, ResponseAllBeen.class);
        if (been.getErr_code() != 0)
            throw new Exception(been.getMessage());
    }

    /**
     * 获取dhcp STATIC 信息
     *
     * @param gateway the gate way of wlan
     * @return
     */
    public List<Level2Been> getStaticDhcp(String gateway) throws Exception {

        final String url = "http://" + gateway + ":80/rpc";

        final String body = new RequestBeen<>(METHOD_STATIC_DHCP_SHOW, new WanRequireAllBeen()).toJsonString();
        String response = httpPostWithJson(url, body, cookies);
        WanResponseAllBeen responseAllBeen = JSONObject.parseObject(response, WanResponseAllBeen.class);
        if (responseAllBeen.getErr_code() != 0) {
            throw new Exception(responseAllBeen.getMessage());
        }
        return responseAllBeen.getList();
    }

    /**
     * 设置dhcp STATIC 信息
     *
     * @param gateway             the gate way of wlan
     * @param enable              是否开启
     * @param isEffectImmediately 是否立即执行
     */
    public void setStaticDhcp(String gateway, boolean enable, boolean isEffectImmediately) throws Exception {

        final String url = "http://" + gateway + ":80/rpc";
        WanRequireAllBeen require = new WanRequireAllBeen();
        require.setEnabled(enable ? 1 : 0);
        require.setAction_flag(isEffectImmediately ? 1 : 0);
        final String body = new RequestBeen<>(METHOD_STATIC_DHCP_SETTING, require).toJsonString();
        String response = httpPostWithJson(url, body, cookies);
        WanResponseAllBeen responseAllBeen = JSONObject.parseObject(response, WanResponseAllBeen.class);

        if (responseAllBeen.getErr_code() != 0) {
            throw new Exception(responseAllBeen.getMessage());
        }
    }

    /**
     * 增加dhcp STATIC 信息
     *
     * @param gateway             the gate way of wlan
     * @param mac                 预绑定设备的mac地址
     * @param ip                  预绑定设备的ip地址
     * @param name                描述
     * @param isEffectImmediately 是否立即执行
     */
    public void addStaticDhcp(String gateway, String mac, String ip, String name, boolean isEffectImmediately) throws Exception {

        final String url = "http://" + gateway + ":80/rpc";
        WanRequireAllBeen require = new WanRequireAllBeen();
        require.setMac(mac.indexOf(':') != -1 ? mac.replace(":", "") : mac);
        require.setIp(ip);
        require.setDescription(name);
        require.setAction_flag(isEffectImmediately ? 1 : 0);
        final String body = new RequestBeen<>(METHOD_STATIC_DHCP_ADD, require).toJsonString();
        String response = httpPostWithJson(url, body, cookies);
        WanResponseAllBeen responseAllBeen = JSONObject.parseObject(response, WanResponseAllBeen.class);

        if (responseAllBeen.getErr_code() != 0) {
            throw new Exception(responseAllBeen.getMessage());
        }

    }

    /**
     * 删除dhcp STATIC 信息
     *
     * @param gateway             the gate way of wlan
     * @param list                预绑定设备的mac地址
     * @param isEffectImmediately 是否立即执行
     */
    public void delStaticDhcp(String gateway, List<Level2Been> list, boolean isEffectImmediately) throws Exception {

        final String url = "http://" + gateway + ":80/rpc";
        for (Level2Been level2Been : list) {
            String mac = level2Been.getMac();
            if (mac.indexOf(':') != -1)
                level2Been.setMac(mac.replace(":", ""));
        }
        WanRequireAllBeen require = new WanRequireAllBeen();
        require.setList(list);
        require.setAction_flag(isEffectImmediately ? 1 : 0);
        final String body = new RequestBeen<>(METHOD_STATIC_DHCP_DEL, require).toJsonString();
        String response = httpPostWithJson(url, body, cookies);
        WanResponseAllBeen responseAllBeen = JSONObject.parseObject(response, WanResponseAllBeen.class);

        if (responseAllBeen.getErr_code() != 0) {
            throw new Exception(responseAllBeen.getMessage());
        }
    }

    /* **************** other method **********************/

    private String httpPostWithJson(String registerUrl, String json) throws IOException {
        return httpPostWithJson(registerUrl, json, null);
    }

    private String httpPostWithJson(String registerUrl, String json, String ssid)
            throws IOException, AuthenticationException {
        String strResult = httpPost(registerUrl, json, ssid);
        if (strResult != null && strResult.contains("\"result\""))
            try {
                JSONObject jsobj = JSONObject.parseObject(strResult);
                strResult = jsobj.getString("result");
                return strResult;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return strResult;
    }

    private String httpPost(String registerUrl, String json, String ssid)
            throws IOException {
        print("====~ httpPost： url = " + registerUrl + ", body = " + json + ", ssid = " + ssid);
        if (ssid != null && ssid.length() > 0) {
            ssid = "ssid=" + ssid;
        }

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        OkHttpClient client = okHttpClientBuilder.build();
        RequestBody body = RequestBody.create(JSON, json);
        Request.Builder builder = new Request.Builder()
                .url(registerUrl)
                .method("POST", body)
                .addHeader("Content-Type", "application/json");
        if (ssid != null && ssid.length() > 0)
            builder.addHeader("Cookie", ssid);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {//成功
                String line = response.body().string();
                print("====~ response = " + line);
                return line;
            } else if (response.code() == 401) {
                throw new java.net.ConnectException("unauthorized");
            } else {
                print("====~ postConnection.getResponseCode() = " + response.code() + ", postConnection.getResponseMessage() = " + response.message());
                throw new IOException(response.message());
            }
        } catch (java.net.ConnectException e) {
            e.printStackTrace();
            int start = registerUrl.indexOf("://") + 3;
            int end = registerUrl.indexOf('/', start);
            if (end == -1)
                end = registerUrl.length();
            String gateway = registerUrl.substring(start, end);
            if (gateway.indexOf(':') != -1)
                gateway = gateway.substring(0, gateway.indexOf(':'));

            if (login(gateway, password) != null)
                return httpPost(registerUrl, json, this.cookies);
            else
                throw e;
        }
    }

    private String httpLogin(String registerUrl, String json)
            throws IOException {
        print("====~ url = " + registerUrl + ", body = " + json);

        java.net.CookieManager manager = new java.net.CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(manager);

        // 将JSON进行UTF-8编码,以便传输中文
        URL url = new URL(registerUrl);
        HttpURLConnection postConnection = (HttpURLConnection) url.openConnection();
        postConnection.setRequestMethod("POST");//post 请求
        postConnection.setConnectTimeout(1000 * 5);
        postConnection.setReadTimeout(1000 * 5);
        postConnection.setDoInput(true);//允许从服务端读取数据
        postConnection.setDoOutput(true);//允许写入
        postConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        postConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");
        postConnection.setRequestProperty("Content-Type", "gzip");
        OutputStream outputStream = postConnection.getOutputStream();
        outputStream.write(json.getBytes());//把参数发送过去.
        outputStream.flush();
        int code = postConnection.getResponseCode();
        if (code == 200) {//成功
            postConnection.getHeaderFields();
            CookieStore store = manager.getCookieStore();
            Iterator<HttpCookie> iterator = store.getCookies().iterator();
            while (iterator.hasNext()) {
                HttpCookie cookie = iterator.next();
                print("====~cookie = " + cookie.getName() + "  -  " + cookie.getValue());
                if ("ssid".equalsIgnoreCase(cookie.getName()))
                    return cookie.getValue();
            }
            throw new java.net.ProtocolException("Unexpected status line");
        } else if (code == 401) {
            String string = postConnection.getResponseMessage();
            if (string == null || string.length() == 0)
                string = "unauthorized";
            throw new AuthenticationException(string);
        } else {
            print("====~ postConnection.getResponseCode() = " + code);
            print("====~ postConnection.getResponseMessage() = " + postConnection.getResponseMessage());
            throw new IOException(postConnection.getResponseMessage());
        }
    }

    private List<Level2Been> getTimeLimitShow(String url, String cookies) throws IOException {
        WanRequireAllBeen requireBody = new WanRequireAllBeen();

        String response = httpPostWithJson(url, new RequestBeen(METHOD_INTERNET_TIME_LIMIT_SHOW, requireBody).toJsonString(), cookies);
        WanResponseAllBeen responseAllBeen = JSONObject.parseObject(response, WanResponseAllBeen.class);
        if (responseAllBeen.getErr_code() != 0) {
            throw new IOException(responseAllBeen.getMessage());
        } else {
            List<Level2Been> list = responseAllBeen.getInfo_list();
            // 排个序
            Collections.sort(list, (t1, t2) -> t1.getMac().compareToIgnoreCase(t2.getMac()));
            return list;
        }
    }

    private List<Group> getStaGroupInfo(String url, String cookies) throws IOException {
        WifiRequireAllBeen requireBody = new WifiRequireAllBeen();

        String response = httpPostWithJson(url, new RequestBeen(METHOD_STA_GROUP_SHOW, requireBody).toJsonString(), cookies);
        WifiResponseAllBeen responseAllBeen = JSONObject.parseObject(response, WifiResponseAllBeen.class);
        if (responseAllBeen.getErr_code() != 0) {
            throw new IOException(responseAllBeen.getMessage());
        } else {
            List<Group> list = responseAllBeen.getMac_list();
            // 排个序
            Collections.sort(list, new Comparator<Group>() {
                @Override
                public int compare(Group t1, Group t2) {
                    int result = t1.getGroup_name().compareToIgnoreCase(t2.getGroup_name());
                    if (result != 0)
                        return result;

                    return t1.getMac().compareToIgnoreCase(t2.getMac());
                }
            });
            return list;
        }
    }

    private void setStaGroupInfo(String url, List<Group> groupList, String cookies) throws IOException {
        WifiRequireAllBeen requireBody = new WifiRequireAllBeen();
        requireBody.setMac_num(groupList.size());
        requireBody.setMac_list(groupList);

        String response = httpPostWithJson(url,
                new RequestBeen(METHOD_STA_GROUP_SET, requireBody).toJsonString(),
                cookies
        );
        WifiResponseAllBeen responseAllBeen = JSONObject.parseObject(response, WifiResponseAllBeen.class);
        if (responseAllBeen.getErr_code() != 0) {
            throw new IOException(responseAllBeen.getMessage());
        }
    }

    /**
     * @param url
     * @param been
     * @param cookies
     * @param isEffectImmediately 是否立即执行，一般最后一个立即执行
     * @throws AuthenticationException
     * @throws IOException
     */
    private void setTimeLimitShow(String url, Level2Been been, String cookies, boolean isEffectImmediately) throws AuthenticationException, IOException {
        WanRequireAllBeen requireBody = new WanRequireAllBeen();
        requireBody.setMac(been.getMac().replace(":", ""));
        requireBody.setStart_time_h(been.getStart_h());
        requireBody.setStart_time_m(been.getStart_m());
        requireBody.setStart_time_w(been.getStart_w());
        requireBody.setEnd_time_h(been.getEnd_h());
        requireBody.setEnd_time_m(been.getEnd_m());
        requireBody.setEnd_time_w(been.getEnd_w());
        requireBody.setMode(been.getState() == null ? 0 : 1);
        requireBody.setAction_flag(isEffectImmediately ? 1 : 0);
        String response = httpPostWithJson(url,
                new RequestBeen(METHOD_INTERNET_TIME_LIMIT_SETTING, requireBody).toJsonString(),
                cookies
        );

        WanResponseAllBeen responseAllBeen = JSONObject.parseObject(response, WanResponseAllBeen.class);
        if (responseAllBeen.getErr_code() != 0) {
            throw new IOException(responseAllBeen.getMessage());
        }
    }

    private void print(String string) {
        if (showLog)
            System.out.println(string);
    }

}
