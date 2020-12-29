package com.changhong.telnettool.function.cpu;

import com.changhong.telnettool.task.ReceiverConnectChangeListener;

public interface Callback<T> extends ReceiverConnectChangeListener {
    void callback(T t);
}
