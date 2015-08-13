package com.loadtesting.ajax.impl;

import java.util.Date;


public class AJAXResult {

    public static final int RESULT_OK = 200;
    public static final int ERROR_RESULT = -1;
    protected long totalTime;
    private int result;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getResult() {
        return result == RESULT_OK ? RESULT_OK : ERROR_RESULT;
    }

    protected void setResult(int result) {
        this.result = result;
    }

    public boolean isOk() {
        return getResult() == RESULT_OK;
    }

    public long totalTime() {
        return totalTime;
    }

    protected void finish(Date start) {
        this.totalTime = (new Date()).getTime() - start.getTime();
    }
}
