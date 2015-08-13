package com.loadtesting.ajax.impl;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.threads.JMeterContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class AJAXCaller {


    public static CumulativeAJAXResult execute(Arguments args, JMeterContext ctx, Entry e) {
        CumulativeAJAXResult result = new CumulativeAJAXResult();
        Date start = new Date();

        ExecutorService executorService = Executors.newFixedThreadPool(args.getArgumentCount());
        List<Future> futures = new ArrayList<Future>(args.getArgumentCount());
        for (int i = 0; i < args.getArgumentCount(); i++) {
            Future future = executorService.submit(new AJAXCall(
                    args.getArgument(i), result, ctx, e));
            futures.add(future);
        }

        for (Future future : futures) {
            try {

                future.get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        result.finish(start);
        return result;

    }


}
