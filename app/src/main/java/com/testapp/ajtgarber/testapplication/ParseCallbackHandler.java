package com.testapp.ajtgarber.testapplication;

import java.util.List;

public interface ParseCallbackHandler {
    public void parsingCompleted(List<CVEEntry> entries);
}
