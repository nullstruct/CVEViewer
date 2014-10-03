package com.testapp.ajtgarber.testapplication;

import java.util.List;

/**
 * Created by ajtgarber on 9/29/14.
 */
public interface DownloadCallbackHandler {
    public void downloadCompleted(List<String> successfulUris);
}
