package com.testapp.ajtgarber.testapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Parses RSS Feed from NIST's NVD and places information into a ListView
 */
public class ParseTask extends AsyncTask<String, Float, List<CVEEntry>> {
    public static final String tag = "ParseTask";

    private ParseCallbackHandler handler;
    private Activity activity;
    private ProgressDialog progress;

    public ParseTask(Activity activity, ParseCallbackHandler handler) {
        super();
        this.activity = activity;
        this.handler = handler;
        progress = new ProgressDialog(activity);
        progress.setMessage("Parsing CVE information... please wait");
    }

    @Override
    protected void onPreExecute() {
        progress.setIndeterminate(true);
        progress.show();
    }

    @Override
    protected List<CVEEntry> doInBackground(String... uris) {
        List<CVEEntry> result = new LinkedList<CVEEntry>();
        Log.i(tag, "REACHED doInBackground");
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            for(String uri : uris) {
                File file = activity.getFileStreamPath(uri);
                InputStream in = activity.openFileInput(uri);
                CVEParser cveParser = new CVEParser(file.lastModified());
                parser.parse(in, cveParser);
                result.addAll(cveParser.getEntries());
            }
        } catch(ParserConfigurationException configEx) {
            Log.e(tag, configEx.toString());
            result = null;
        } catch(IOException ioEx) {
            Log.e(tag, ioEx.toString());
            result = null;
        } catch(SAXException saxEx) {
            Log.e(tag, saxEx.toString());
            result = null;
        }
        return result;
    }

    @Override
    public void onProgressUpdate(Float... update) {

    }

    @Override
    public void onPostExecute(List<CVEEntry> entries) {
        if(handler == null) {
            Log.e(tag, "ParseCallbackHandler was specified as null, my job was meaningless :c");
            return;
        }
        progress.dismiss();
        handler.parsingCompleted(entries);
    }
}
