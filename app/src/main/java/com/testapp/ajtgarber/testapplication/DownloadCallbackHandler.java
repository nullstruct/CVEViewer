package com.testapp.ajtgarber.testapplication;

import java.util.List;

public interface DownloadCallbackHandler {
    public void downloadCompleted(List<String> successfulUris);
}
