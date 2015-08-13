package com.loadtesting.ajax.impl;

import org.apache.jmeter.config.Argument;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class AJAXCall implements Runnable {

    private static final Logger logger = LoggingManager.getLoggerForClass();

    private static final int TIMEOUT = 10000;
    public static CumulativeAJAXResult ajaxresult;
    private Argument argrument;
    private JMeterContext ctx;

    public AJAXCall(Argument argument, CumulativeAJAXResult result, JMeterContext ctx, Entry e) {
        this.argrument = argument;
        ajaxresult = result;
        this.ctx = ctx;
    }

    private static AJAXResult singleErrorResult(Date start) {
        AJAXResult singleResult = new AJAXResult();
        singleResult.setResult(AJAXResult.ERROR_RESULT);
        singleResult.finish(start);
        return singleResult;
    }

    private static AJAXResult execute(String method, String url, String data, JMeterContext ctx) throws IOException {
        AJAXResult result = new AJAXResult();
        logger.info("Executing " + method + " request to URL: " + url);
        result.setUrl(url);
        Date start = new Date();
        HttpURLConnection connection = (HttpURLConnection) (new URL(url))
                .openConnection();
        connection.setReadTimeout(TIMEOUT);
        CookieManager cookieManager = (CookieManager) ctx.getCurrentSampler().getProperty("HTTPSampler.cookie_manager").getObjectValue();
        if (cookieManager != null)
            for (int i = 0; i < cookieManager.getCookieCount(); i++) {
                connection.addRequestProperty("Cookie", cookieManager.get(i).getName() + "=" + cookieManager.get(i).getValue());
            }
        if (method.equals("GET")) {
            connection.connect();
        } else {
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes().length));
            connection.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(data);
            wr.flush();
            wr.close();

        }


        result.setResult(connection.getResponseCode());
        connection.disconnect();
        result.finish(start);
        logger.info("Executed " + method + " request to URL: " + url + " with result: " + result.getResult());

        return result;
    }

    public void run() {
        AJAXResult singleResult = null;
        final Date start = new Date();
        try {
            singleResult = execute(argrument.getName(), argrument.getValue(), argrument.getDescription(), ctx);
        } catch (IOException e) {
            singleResult = singleErrorResult(start);
        }
        ajaxresult.addResult(singleResult.getUrl(), singleResult.getResult());
        ajaxresult.addTime(singleResult.totalTime());
    }
}
