package com.testapp.ajtgarber.testapplication;

import android.app.Fragment;
import android.os.Bundle;

import java.util.List;

/**
 * Created by ajtgarber on 9/30/14.
 */
public class CVEFragment extends Fragment {
    private List<CVEEntry> entries;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public List<CVEEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<CVEEntry> entries) {
        this.entries = entries;
    }
}
