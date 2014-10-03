package com.testapp.ajtgarber.testapplication;

import java.util.List;

/**
 * Created by ajtgarber on 9/28/14.
 */
public interface ParseCallbackHandler {
    public void parsingCompleted(List<CVEEntry> entries);
}
