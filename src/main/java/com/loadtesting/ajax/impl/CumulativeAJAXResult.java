package com.loadtesting.ajax.impl;

import java.util.HashMap;
import java.util.Map;

public class CumulativeAJAXResult extends AJAXResult {


    private Map<String, Integer> results = new HashMap<String, Integer>();
    private long timeSum = 0;

    public Map<String, Integer> getResults() {
        return results;
    }

    public int getResult() {

        for (int result : results.values()) {
            if (result != RESULT_OK)
                return ERROR_RESULT;
        }
        return RESULT_OK;
    }

    public long timeSum() {
        return timeSum;
    }

    protected void addResult(String url, int result) {
        results.put(url, result);
    }

    protected void addTime(long time) {
        timeSum += time;
    }
}
