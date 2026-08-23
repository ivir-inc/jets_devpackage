/*
 * Copyright 2026 IVIR Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivir.devpackage.gui;

import com.ivir.devpackage.controller.ControlWebClient;
import com.ivir.devpackage.gui.action.GetVersionAction;
import com.ivir.devpackage.gui.wrapper.*;
import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.wtk.*;
import org.apache.pivot.wtk.media.Image;

import java.awt.*;

public class SplitPaneExample extends SplitPaneExt {
    private ControlWebClient controlWebClient;
    private TextAreaExt queryResultTA;

    private PushButtonExt joinFedBtn;
    private PushButtonExt connectionStatusBtn;
    private PushButtonExt resignFederationBtn;
    public SplitPaneExample(ControlWebClient controlWebClient) throws TaskExecutionException {
        this.setOrientation(Orientation.VERTICAL);
        this.setTop(oldStyleExample());
        this.setBottom(newStyleExample());
    }

    private BoxPaneExt oldStyleExample() throws TaskExecutionException {
        BoxPaneExt queryPane = new BoxPaneExt();
        queryPane.setOrientation(Orientation.VERTICAL);
        queryPane.setBackgroundColor(Color.white);
        queryPane.setFill();

        TabPaneExt tabPaneExt = new TabPaneExt();
        tabPaneExt.setTabOrientation(Orientation.VERTICAL);
        tabPaneExt.setBackgroundColor(Color.BLACK);
        tabPaneExt.setButtonColor(Color.WHITE);
        tabPaneExt.setInactiveTabColor(Color.BLACK);
        tabPaneExt.setInactiveBorderColor(Color.BLACK);
        tabPaneExt.setActiveTabColor(new Color(24, 49, 76));

        tabPaneExt.addTab("Queries", queryButtonFlowPane() );
        tabPaneExt.addTab("Mutations", new LabelExt("Mutations"));

        queryPane.add(tabPaneExt);
        return queryPane;
    }

    private BoxPaneExt newStyleExample() throws TaskExecutionException {
        BoxPaneExt queryResults = new BoxPaneExt();
        queryResults.setFill();
        queryResults.setPadding(25);

        queryResultTA = new TextAreaExt();
        queryResultTA.setText("Query Results Will Display Here");
        queryResultTA.setEditable(false);
        queryResults.setPreferredHeight(50);

        queryResults.add(queryResultTA);

        return  queryResults;


    }
    private FlowPaneExt queryButtonFlowPane() {
        // Button Creation
        PushButtonExt getVersionsBtn = new PushButtonExt("Get Versions");
        getVersionsBtn.setPadding(5);
//        getVersionsBtn.setAction(new GetVersionAction(controlWebClient,this));

        // Adding buttons to flow
        FlowPaneExt queryFlowPane = new FlowPaneExt();
        queryFlowPane.add(getVersionsBtn);

        return queryFlowPane;
    }


}
