
package com.testapp.ajtgarber.testapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DownloadTask extends AsyncTask<Uri, Float, List<String>> {
    public static final String tag = "DownloadTask";

    private DownloadCallbackHandler handler;
    private Activity activity;
    private ProgressDialog progress;

    public DownloadTask(Activity activity, DownloadCallbackHandler handler) {
        super();
        this.activity = activity;
        this.handler = handler;
        progress = new ProgressDialog(activity);
        progress.setMessage("Checking for new CVEs");
    }

    @Override
    protected void onPreExecute() {
        progress.setIndeterminate(true);
        progress.show();
    }

    @Override
    protected List<String> doInBackground(Uri... uris) {
        List<String> result = new LinkedList<String>();
        for(int i = 0; i < uris.length; i++) {
            try {
                URL url = new URL(uris[i].toString());
                URLConnection con = url.openConnection();

                String filename = getFileName(url.getFile());
                File file = activity.getFileStreamPath(filename);
                if(file.exists()) {
                    Date lastRetrieved = new Date(file.lastModified());
                    Date lastModified = new Date(con.getLastModified());
                    if(lastModified.compareTo(lastRetrieved) <= 0) {
                        continue;
                    }
                }

                InputStream is = con.getInputStream();
                FileOutputStream fos = activity.openFileOutput(filename, Context.MODE_PRIVATE);
                byte[] buffer = new byte[1024];
                int n;
                while((n = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, n);
                    if(isCancelled()) {
                        break;
                    }
                }
                fos.close();
                is.close();
                result.add(uris[i].toString());

                if(isCancelled()) {
                    try {
                        file.delete(); //The download may not have completed properly
                    } catch(Exception ex) {
                        Log.e(tag, ex.getMessage());
                    }
                }
            } catch(IOException ex) {
                Log.e(tag, ex.getMessage());
            }
        }
        return result;
    }

    private String getFileName(String path) {
        String[] split = path.split("/");
        return split[split.length-1];
    }

    @Override
    public void onProgressUpdate(Float... update) {

    }

    @Override
    public void onPostExecute(List<String> entries) {
        if(handler == null) {
            Log.e(tag, "DownloadCallbackHandler was specified as null");
            return;
        }
        progress.dismiss();
        handler.downloadCompleted(entries);
    }
}
