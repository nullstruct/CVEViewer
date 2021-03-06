package com.testapp.ajtgarber.testapplication;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CVEParser extends DefaultHandler {
    public static String tag = "CVEParser";

    private List<CVEEntry> entries;
    private boolean parsingEntry;
    private String id;
    private String summary;
    private String published;
    private String lastModified;
    private String accessVector;
    private String accessComplexity;
    private String integrityImpact;
    private String availabilityImpact;
    private String confidentialityImpact;
    private boolean shouldNotify;

    private String authentication;
    private double cvsScore;
    private List<String> vulnerableProducts;

    private StringBuffer buffer;
    private long fileLastModified;

    public CVEParser(long fileLastModified) {
        this.fileLastModified = fileLastModified;
        entries = new LinkedList<CVEEntry>();
        vulnerableProducts = new LinkedList<String>();
        parsingEntry = false;
        buffer = new StringBuffer();
    }

    public List<CVEEntry> getEntries() {
        return entries;
    }

    @Override
    public void startElement(String namespace, String name, String qname, Attributes attrs) {
        buffer = new StringBuffer();
        if(name.equals("entry")) {
            parsingEntry = true;
        }
    }

    @Override
    public void endElement(String namespace, String name, String qname) {
        if(name.equals("entry")) {
            //Log.i(tag, "ENTRY ENDED");
            parsingEntry = false;
            if(id != null) {
                CVEEntry entry = new CVEEntry(id, "", cvsScore);
                entry.setDescription(summary);
                entry.setPublishedDate(published);
                entry.setLastModified(lastModified);
                entry.setAccessVector(accessVector);
                entry.setAccessComplexity(accessComplexity);
                entry.setIntegrityImpact(integrityImpact);
                entry.setAvailabilityImpact(availabilityImpact);
                entry.setConfidentialityImpact(confidentialityImpact);
                entry.setAuthentication(authentication);
                entry.setShouldNotify(shouldNotify);
                entries.add(entry);
                id = null;
                summary = null;
                published = null;
                lastModified = null;
                vulnerableProducts = new LinkedList<String>();
                shouldNotify = false;
            }
        } else if(qname.equals("vuln:cve-id") && parsingEntry) {
            id = buffer.toString();
            //Log.i(tag, "cve-id: "+id);
        } else if(qname.equals("vuln:summary") && parsingEntry) {
            summary = buffer.toString();
            //Log.i(tag, "vuln:summary: "+summary);
        } else if(qname.equals("vuln:published-datetime") && parsingEntry) {
            String tempDate = buffer.toString().substring(0, 10);
            published = tempDate;
            String[] parts = tempDate.split("-");
            int[] intParts = {Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])};
            Calendar publishedDate = Calendar.getInstance();
            publishedDate.set(intParts[0], intParts[1] - 1, intParts[2]);

            Calendar lastChecked = Calendar.getInstance();
            lastChecked.setTime(new Date(fileLastModified));
            String friendlyLastChecked = lastChecked.get(Calendar.DAY_OF_MONTH)+"/"+lastChecked.get(Calendar.MONTH)+"/"+lastChecked.get(Calendar.YEAR);
            String friendlyPublished = publishedDate.get(Calendar.DAY_OF_MONTH)+"/"+publishedDate.get(Calendar.MONTH)+"/"+publishedDate.get(Calendar.YEAR);
            Log.i("CVEParser", "lastChecked = "+friendlyLastChecked+", publishedDate = "+friendlyPublished);

            if(publishedDate.compareTo(lastChecked) >= 0) {
                Log.i("CVEParser", "We should notify");
                shouldNotify = true;
            }

            //Log.i(tag, "vuln:published-datetime: "+published);
        } else if(qname.equals("vuln:last-modified-datetime") && parsingEntry) {
            String tempDate = buffer.toString().substring(0, 10);
            lastModified = tempDate;

            String[] parts = tempDate.split("-");
            int[] intParts = {Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])};
            Calendar publishedDate = Calendar.getInstance();
            publishedDate.set(intParts[0], intParts[1] - 1, intParts[2]);

            Calendar lastChecked = Calendar.getInstance();
            lastChecked.setTime(new Date(fileLastModified));
            String friendlyLastChecked = lastChecked.get(Calendar.DAY_OF_MONTH)+"/"+lastChecked.get(Calendar.MONTH)+"/"+lastChecked.get(Calendar.YEAR);
            String friendlyPublished = publishedDate.get(Calendar.DAY_OF_MONTH)+"/"+publishedDate.get(Calendar.MONTH)+"/"+publishedDate.get(Calendar.YEAR);
            Log.i("CVEParser", "lastChecked = "+friendlyLastChecked+", publishedDate = "+friendlyPublished);

            if(publishedDate.compareTo(lastChecked) >= 0) {
                Log.i("CVEParser", "We should notify");
                shouldNotify = true;
            }
            //Log.i(tag, "vuln:last-modified-datetime: "+lastModified);
        } else if(qname.equals("vuln:product") && parsingEntry) {
            String str = buffer.toString();
            vulnerableProducts.add(str);
            //Log.i(tag, "vuln:product: "+str);
        } else if(qname.equals("cvss:score") && parsingEntry) {
            String str = buffer.toString();
            cvsScore = Double.parseDouble(str);
            //Log.i(tag, "cvss:score: "+cvsScore);
        } else if(qname.equals("cvss:access-vector") && parsingEntry) {
            accessVector = buffer.toString();
            //Log.i(tag, "cvss:access-vector: "+accessVector);
        } else if(qname.equals("cvss:access-complexity") && parsingEntry) {
            accessComplexity = buffer.toString();
            //Log.i(tag, "cvss:access-complexity: "+accessComplexity);
        } else if(qname.equals("cvss:authentication") && parsingEntry) {
            authentication = buffer.toString();
            //Log.i(tag, "cvss:authentication: "+authentication);
        } else if(qname.equals("cvss:confidentiality-impact") && parsingEntry) {
            confidentialityImpact = buffer.toString();
            //Log.i(tag, "cvss:confidentiality-impact: "+confidentialityImpact);
        } else if(qname.equals("cvss:integrity-impact") && parsingEntry) {
            integrityImpact = buffer.toString();
            //Log.i(tag, "cvss:integrity-impact: "+integrityImpact);
        } else if(qname.equals("cvss:availability-impact") && parsingEntry) {
            availabilityImpact = buffer.toString();
            //Log.i(tag, "cvss:availability-impact: "+availabilityImpact);
        }
    }

    @Override
    public void characters(char[] data, int start, int length) {
        buffer.append(new String(data, start, length));
    }
}
