/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.loadtesting.ajax;

import com.loadtesting.ajax.impl.AJAXCall;
import com.loadtesting.ajax.impl.AJAXCaller;
import com.loadtesting.ajax.impl.AJAXResult;
import com.loadtesting.ajax.impl.CumulativeAJAXResult;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jmeter.config.Arguments;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;

public class AJAXSampler extends AbstractSampler {

    private static final long serialVersionUID = 240L;

    // The name of the property used to hold our data
    public static final String DATA = "AJAXSampler.data"; //$NON-NLS-1$
    public static final String HOST = "AJAXSampler.host"; //$NON-NLS-1$

    private static AtomicInteger classCount = new AtomicInteger(0); // keep track of classes created

    public AJAXSampler() {
        classCount.incrementAndGet();
    }

    @Override
    public SampleResult sample(Entry e) {
        SampleResult rv = new SampleResult();
        String host = getHost(); // Sampler host
        String data = getData(); // Sampler data

        rv.setSampleLabel(getTitle());
        /*
         * Perform the sampling
         */
        rv.sampleStart();
        Arguments args = new Arguments();     
        List<String> urlList = Arrays.asList(data.split("\\r?\\n"));
        Iterator<String> url = urlList.iterator();
	while (url.hasNext()) {
            try {
                args.addArgument("GET", new URL(new URL(host), url.next()).toString());
            } catch (MalformedURLException ex) {
                Logger.getLogger(AJAXSampler.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
        AJAXResult AJAXResult = AJAXCaller.execute(args, getThreadContext(), e);
        rv.sampleEnd();
        if (AJAXResult.isOk()) {
            rv.setSuccessful(true);
            rv.setResponseMessage("AJAX Requests Execution was successful");
        } else {
            rv.setSuccessful(false);
            rv.setResponseMessage("AJAX Requests Execution failed");
        }
        CumulativeAJAXResult results = AJAXCall.ajaxresult;
        StringBuilder datawriter = new StringBuilder();
        for (Object o : results.getResults().entrySet()) {
            Map.Entry result = (Map.Entry) o;            
            datawriter.append(result.getKey()).append(" - ").append(result.getValue());
            datawriter.append(System.getProperty("line.separator"));
        }
        rv.setResponseData(datawriter.toString(), "UTF-8");
        return rv;
    }

    /**
     * @return a string for the sampleResult Title
     */
    private String getTitle() {
        return this.getName();
    }

    /**
     * @return the data for the sample
     */
    public String getData() {
        return getPropertyAsString(DATA);
    }
    
    /**
     * @return the host for the sample
     */
    public String getHost() {
        return getPropertyAsString(HOST);
    }
}