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

package com.loadtesting.ajax.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import com.loadtesting.ajax.AJAXSampler;
import javax.swing.SwingConstants;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

public class AJAXSamplerGui extends AbstractSamplerGui {

    private static final long serialVersionUID = 240L;

    private JTextArea data;
    private JTextArea host;

    public AJAXSamplerGui() {
        init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabelResource() {
        return "example_title"; // $NON-NLS-1$
    }

    @Override
    public String getStaticLabel() {
        return "AJAX Sampler";
    }    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(TestElement element) {
        host.setText(element.getPropertyAsString(AJAXSampler.HOST));
        data.setText(element.getPropertyAsString(AJAXSampler.DATA));
        super.configure(element);        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestElement createTestElement() {
        AJAXSampler sampler = new AJAXSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyTestElement(TestElement te) {
        te.clear();
        configureTestElement(te);
        te.setProperty(AJAXSampler.HOST, host.getText());
        te.setProperty(AJAXSampler.DATA, data.getText());
    }

    /*
     * Helper method to set up the GUI screen
     */
    private void init() {
        // Standard setup
        setLayout(new BorderLayout(5,5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH); // Add the standard title

        // Specific setup
        add(createDataPanel(), BorderLayout.CENTER);
        add(createHostPanel(), BorderLayout.SOUTH); 
    }

    /*
     * Create a data input text field
     *
     * @return the panel for entering the data
     */
    private Component createDataPanel() {
        JLabel label_data = new JLabel("<html>AJAX URLs<br>(one URL per line)</html>", SwingConstants.CENTER); //$NON-NLS-1$
        
        data = new JTextArea();
        data.setName(AJAXSampler.DATA);
        label_data.setLabelFor(data);

        JPanel dataPanel = new JPanel(new BorderLayout(5,5));
        dataPanel.add(label_data, BorderLayout.WEST);
        dataPanel.add(data, BorderLayout.CENTER);

        return dataPanel;
    }
    
    /*
     * Create a hostname input text field
     *
     * @return the panel for entering the hostname
     */
    private Component createHostPanel() {
        JLabel label_host = new JLabel("<html>Base URL<br>(http://hostname/)</html>", SwingConstants.CENTER); //$NON-NLS-1$

        host = new JTextArea();
        host.setName(AJAXSampler.HOST);
        label_host.setLabelFor(host);

        JPanel hostPanel = new JPanel(new BorderLayout(5,5));
        hostPanel.add(label_host, BorderLayout.WEST);
        hostPanel.add(host, BorderLayout.CENTER);

        return hostPanel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearGui() {
        super.clearGui();
        host.setText(""); // $NON-NLS-1$
        data.setText(""); // $NON-NLS-1$
    }
}