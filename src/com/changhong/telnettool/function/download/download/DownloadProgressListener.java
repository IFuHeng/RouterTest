package com.changhong.telnettool.function.download.download;

public interface DownloadProgressListener {
    void onDownloadSize(int size, int total);

    void onChildDownloadSize(int id, int length, int size);
}
