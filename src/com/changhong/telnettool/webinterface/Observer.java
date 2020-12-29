package com.changhong.telnettool.webinterface;

public interface Observer<T> {
    void onError(Throwable throwable);

    void onComplete();

    void onNext(T t);
}
