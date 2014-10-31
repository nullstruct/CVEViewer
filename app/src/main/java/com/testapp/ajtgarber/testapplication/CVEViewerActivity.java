package com.testapp.ajtgarber.testapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

/**
 * Created by ajtgarber on 9/28/14.
 */
public class CVEViewerActivity extends Activity {

    private String id;
    private String link;
    private String summary;
    private String published;
    private String lastModified;
    private String accessVector;
    private String accessComplexity;
    private String integrityImpact;
    private String availabilityImpact;
    private String confidentialityImpact;
    private String authentication;
    private double cvsScore;
    private List<String> vulnerableProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cve_viewer);

        ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        setFieldText(R.id.cve_id, bundle.getString("cve_id"));
        setFieldText(R.id.description, bundle.getString("description"));
        setFieldText(R.id.published_date, "Published: "+bundle.getString("published_date"));
        setFieldText(R.id.modified_date, "Last Modified: "+bundle.getString("modified_date"));
        setFieldText(R.id.access_vector, "Access Vector: "+bundle.getString("access_vector"));
        setFieldText(R.id.access_complexity, "Access Complexity: "+bundle.getString("access_complexity"));
        setFieldText(R.id.integrity_impact, "Integrity Impact: "+bundle.getString("integrity_impact"));
        setFieldText(R.id.availability_impact, "Availability Impact: "+bundle.getString("availability_impact"));
        setFieldText(R.id.confidentiality_impact, "Confidentiality Impact: "+bundle.getString("confidentiality_impact"));
        setFieldText(R.id.authentication, "Authentication: "+bundle.getString("authentication"));
        setFieldText(R.id.cvs_score, "CVS Score: "+bundle.getDouble("cvs_score"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        onBackPressed();
        return true;
    }

    protected void setFieldText(int id, String text) {
        TextView tv = (TextView)findViewById(id);
        tv.setText(text);
    }

    /**
     * A placeholder fragment containing a simple view. This fragment
     * would include your content.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_ad, container, false);
            return rootView;
        }
    }

    /**
     * This class makes the ad request and loads the ad.
     */
    public static class AdFragment extends Fragment {

        private AdView mAdView;

        public AdFragment() {
        }

        @Override
        public void onActivityCreated(Bundle bundle) {
            super.onActivityCreated(bundle);

            // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
            // values/strings.xml.
            mAdView = (AdView) getView().findViewById(R.id.adView);

            // Create an ad request. Check logcat output for the hashed device ID to
            // get test ads on a physical device. e.g.
            // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_ad, container, false);
        }

        /** Called when leaving the activity */
        @Override
        public void onPause() {
            if (mAdView != null) {
                mAdView.pause();
            }
            super.onPause();
        }

        /** Called when returning to the activity */
        @Override
        public void onResume() {
            super.onResume();
            if (mAdView != null) {
                mAdView.resume();
            }
        }

        /** Called before the activity is destroyed */
        @Override
        public void onDestroy() {
            if (mAdView != null) {
                mAdView.destroy();
            }
            super.onDestroy();
        }

    }
}
