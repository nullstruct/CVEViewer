package com.testapp.ajtgarber.testapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class CVEViewerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cve_viewer);

        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
        } else {
            Log.e("CVEViewerActivity", "actionBar == null, cannot enable back button");
        }

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
}
