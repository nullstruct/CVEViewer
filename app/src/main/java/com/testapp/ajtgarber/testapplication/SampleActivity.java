package com.testapp.ajtgarber.testapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class SampleActivity extends Activity implements AdapterView.OnItemClickListener, ParseCallbackHandler, DownloadCallbackHandler {

    private List<CVEEntry> entries;
    private SimpleAdapter adapter;
    private CVEFragment dataFragment;
    public static String recentFeed = "https://nvd.nist.gov/feeds/xml/cve/nvdcve-2.0-Recent.xml";
    public static String recentFile = "nvdcve-2.0-Recent.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        FragmentManager fm = getFragmentManager();
        dataFragment = (CVEFragment) fm.findFragmentByTag("data");
        if(dataFragment != null) {
            entries = dataFragment.getEntries();
            if(entries != null) {
                addEntriesToList(entries);
                return;
            }
        }
        dataFragment = new CVEFragment();
        fm.beginTransaction().add(dataFragment, "data").commit();

        final SampleActivity sampleActivity = this;
        //Make sure we're connected to the Internet
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected()) {
            Log.v("SampleActivity", "Network not connected, cannot download new CVE information");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You must be connected to the Internet to get up to date CVE information")
                    .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            sampleActivity.finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            new ParseTask(sampleActivity, sampleActivity).execute(recentFile);
                        }
                    });
            builder.create().show();
        } else {
            Uri recentUri = Uri.parse(recentFeed);
            new DownloadTask(this, this).execute(recentUri);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // store the data in the fragment
        dataFragment.setEntries(entries);
    }

    @Override
    public void onPause() {
        super.onPause();
        // store the data in the fragment
        dataFragment.setEntries(entries);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
                if(newText.equals("")) {
                    adapter.getFilter().filter("");
                    return true;
                }
                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                adapter.getFilter().filter(query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.action_refresh) {
            final SampleActivity sampleActivity = this;
            ConnectivityManager connectivity = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            if(networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected()) {
                Log.v("SampleActivity", "Network not connected, cannot download new CVE information");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You must be connected to the Internet to download up to date CVE information")
                        .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new ParseTask(sampleActivity, sampleActivity).execute(recentFile);
                            }
                        });
                builder.create().show();
            } else {
                Uri recentUri = Uri.parse(recentFeed);
                new DownloadTask(this, this).execute(recentUri);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GridLayout gl = (GridLayout)view;
        TextView tv = (TextView)gl.getChildAt(0);
        String title = tv.getText().toString();

        for(CVEEntry entry : entries) {
            if(entry.getId().equals(title.substring(0, 13))) {
                Intent intent = new Intent(getApplicationContext(), CVEViewerActivity.class);
                intent.putExtra("cve_id", entry.getId());
                intent.putExtra("description", entry.getDescription());
                intent.putExtra("published_date", entry.getPublishedDate());
                intent.putExtra("modified_date", entry.getLastModified());
                intent.putExtra("access_vector", entry.getAccessVector());
                intent.putExtra("access_complexity", entry.getAccessComplexity());
                intent.putExtra("integrity_impact", entry.getIntegrityImpact());
                intent.putExtra("availability_impact", entry.getAvailabilityImpact());
                intent.putExtra("confidentiality_impact", entry.getConfidentialityImpact());
                intent.putExtra("authentication", entry.getAuthentication());
                intent.putExtra("cvs_score", entry.getCvsScore());
                startActivity(intent);
            }
        }
    }

    public void addEntriesToList(List<CVEEntry> entries) {
        if(entries == null) {
            Log.e("SampleActivity", "addEntriesToList: entries == null");
            return;
        }
        Collections.sort(entries);
        for(int i = 0; i < entries.size()/2; i++) {
            CVEEntry e1 = entries.get(i);
            CVEEntry e2 = entries.get(entries.size()-1-i);
            entries.set(i, e2);
            entries.set(entries.size()-1-i, e1);
        }

        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        ArrayList<Map<String, String>> contents = new ArrayList<Map<String, String>>();
        for(CVEEntry entry : entries) {
            Map<String, String> temp = new Hashtable<String, String>();
            String cveTitle = entry.getId()+" ("+entry.getCvsScore()+")";
            if(entry.isShouldNotify())
                cveTitle += "*";
            temp.put("CVE", cveTitle);
            temp.put("URL", entry.getDescription());
            if(entry.isShouldNotify())
                contents.add(0, temp);
            else
                contents.add(temp);
        }
        String[] from = {"CVE", "URL"};
        int[] to = {R.id.item_cve, R.id.item_url};
        adapter = new SimpleAdapter(this, contents, R.layout.grid_item, from, to);
        listView.setAdapter(adapter);
    }

    @Override
    public void parsingCompleted(List<CVEEntry> entries) {
        if(entries == null) {
            Log.e("SampleActivity", "An error occurred while parsing");
            return;
        }
        this.entries = entries;
        Log.i("SampleActivity", "parsingCompleted!");
        addEntriesToList(entries);
    }
    public void downloadCompleted(List<String> successfulUris) {
        Log.i("SampleActivity", "downloadCompleted");
        new ParseTask(this, this).execute(recentFile);
    }
}
