package com.changhong.telnettool.service;

import java.util.Observer;

public interface ServiceCallback<T> extends Observer {
    ResponseBeen<T> callback(String ip,String string) throws Exception;
}
