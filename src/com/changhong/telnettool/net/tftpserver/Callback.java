package com.changhong.telnettool.net.tftpserver;

public interface Callback<T> {
    boolean onError(Exception e);

    boolean onNext(T e);
}
