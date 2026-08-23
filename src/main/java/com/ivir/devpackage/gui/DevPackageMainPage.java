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
import com.ivir.devpackage.gui.action.*;
import com.ivir.devpackage.gui.wrapper.*;
import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.wtk.*;
import org.apache.pivot.wtk.ScrollPane;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtk.media.Image;

import java.awt.*;

public class DevPackageMainPage extends BoxPaneExt {
    private Window window;
    private ControlWebClient controlWebClient;
    private TextAreaExt queryResultTA;

    private PushButtonExt joinFedBtn;

    public DevPackageMainPage(ControlWebClient controlWebClient, Window window) throws TaskExecutionException {
        this.window = window;
        this.controlWebClient = controlWebClient;
        this.setOrientation(Orientation.VERTICAL);
        this.setFill();
        this.setBackgroundColor(new Color(24, 49, 76));

        this.add(mainTitleBox());
        this.add(joinFedBox());
        this.add(querySplitPane());
    }

    private BoxPaneExt mainTitleBox() throws TaskExecutionException {

        BoxPaneExt bannerBoxPane = new BoxPaneExt();
        bannerBoxPane.setOrientation(Orientation.VERTICAL);
        bannerBoxPane.setFill();
        bannerBoxPane.setBackgroundColor(Color.BLACK);

        BoxPaneExt titleImageBox = new BoxPaneExt();
        titleImageBox.setBackgroundColor(Color.BLACK);
        titleImageBox.setOrientation(Orientation.HORIZONTAL);
        titleImageBox.setHorizontalAlignment(HorizontalAlignment.LEFT);
        titleImageBox.setVerticalAlignment(VerticalAlignment.CENTER);
        titleImageBox.setPadding(10);

        BoxPaneExt titlePaneExt = new BoxPaneExt();
        titlePaneExt.setBackgroundColor(Color.BLACK);
        titlePaneExt.setOrientation(Orientation.HORIZONTAL);
        titlePaneExt.setFill();
        titlePaneExt.add(titleImageBox.getComponent());

        ImageViewExt mmsImageView = new ImageViewExt();
        Image mmsLogo = Image.load(this.getClass().getResource("/images/mms_logo_title.png"));
        mmsImageView.setImage(mmsLogo);

        titleImageBox.add(mmsImageView);
        titleImageBox.add(new LabelExt("  JETS Developer Package   ",
                Color.WHITE,
                new Font("Arial Black", Font.PLAIN, 18)));

        bannerBoxPane.add(titlePaneExt);

        return bannerBoxPane;
    }

    private BoxPaneExt joinFedBox() {
        BoxPaneExt fedBoxPane = new BoxPaneExt();
        fedBoxPane.setFill();
        fedBoxPane.setPadding(20);
        fedBoxPane.setBackgroundColor(new Color(24, 49, 76));
        fedBoxPane.setHorizontalAlignment(HorizontalAlignment.CENTER);
        fedBoxPane.setPreferredHeight(100);

        joinFedBtn = new PushButtonExt("Join Federation");
        joinFedBtn.setAction(new JoinFederationAction(controlWebClient, this));
        joinFedBtn.setPadding(10);
        joinFedBtn.setBackgroundColor(new Color(33,150,243));
        joinFedBtn.setBorderColor(new Color(33,150,243));
        joinFedBtn.setColor(Color.white);
        joinFedBtn.setPreferredWidth(400);

        fedBoxPane.add(joinFedBtn);

        return  fedBoxPane;

    }

    private SplitPaneExt querySplitPane() throws TaskExecutionException {
        SplitPaneExt querySplitPane = new SplitPaneExt();
        querySplitPane.setOrientation(Orientation.VERTICAL);
        querySplitPane.setTopLeft(queryPane());
        querySplitPane.setBottomRight(queryResultsPane());
        querySplitPane.setSplitRatio(0.35F);
        querySplitPane.setPreferredHeight(500);

        return querySplitPane;

    }

    private BoxPaneExt queryPane() {
        // Setting Query Title
        BoxPaneExt queryBox = new BoxPaneExt();
        queryBox.setOrientation(Orientation.VERTICAL);
        queryBox.setPreferredHeight(50);
        queryBox.setFill();
        queryBox.setBackgroundColor(new Color(24, 49, 76));


        BoxPaneExt titleImageBox = new BoxPaneExt();
        titleImageBox.setBackgroundColor(Color.BLACK);
        titleImageBox.setOrientation(Orientation.HORIZONTAL);
        titleImageBox.setHorizontalAlignment(HorizontalAlignment.LEFT);
        titleImageBox.setVerticalAlignment(VerticalAlignment.CENTER);
        titleImageBox.setPadding(5);

        titleImageBox.add(new LabelExt("  Queries   ",
                Color.WHITE,
                new Font("Arial Black", Font.PLAIN, 14)));

        queryBox.add(titleImageBox);

        // Creating Query Content
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

        tabPaneExt.addTab("Queries", queryButtonsBox());
        tabPaneExt.addTab("Mutations", mutationButtons());

        queryPane.add(tabPaneExt);
        queryBox.add(queryPane);

        return queryBox;

    }

    private BoxPaneExt queryResultsPane() {
        BoxPaneExt queryResultBox = new BoxPaneExt();
        queryResultBox.setOrientation(Orientation.VERTICAL);
        queryResultBox.setFill();
        queryResultBox.setBackgroundColor(new Color(24, 49, 76));

        BoxPaneExt titleImageBox = new BoxPaneExt();
        titleImageBox.setBackgroundColor(Color.BLACK);
        titleImageBox.setOrientation(Orientation.HORIZONTAL);
        titleImageBox.setHorizontalAlignment(HorizontalAlignment.LEFT);
        titleImageBox.setVerticalAlignment(VerticalAlignment.CENTER);
        titleImageBox.setPadding(5);

        titleImageBox.add(new LabelExt("  Query Results   ",
                Color.WHITE,
                new Font("Arial Black", Font.PLAIN, 14)));

        queryResultBox.add(titleImageBox);

        // Creating Query Result Content
        BoxPaneExt queryResults = new BoxPaneExt();
        queryResults.setFill();
        queryResults.setBackgroundColor(Color.RED);
        queryResults.setHorizontalAlignment(HorizontalAlignment.CENTER);
        queryResults.setVerticalAlignment(VerticalAlignment.CENTER);


        queryResultTA = new TextAreaExt();
        queryResultTA.setBackGroundColor(new Color(24, 49, 76));
        queryResultTA.setColor(Color.WHITE);
        queryResultTA.setText("Query Results Will Display Here");
        queryResultTA.setEditable(false);
        queryResultTA.setMargin(10);

        BorderExt queryResultBorder = new BorderExt();
        queryResultBorder.setPreferredWidth(850);
        queryResultBorder.setContent(queryResultTA);

        queryResults.add(queryResultBorder);

        ScrollPane queryResultScrollPane = new ScrollPane();
        queryResultScrollPane.setVerticalScrollBarPolicy(ScrollPane.ScrollBarPolicy.AUTO);
        queryResultScrollPane.setPreferredHeight(300);
        queryResultScrollPane.setView(queryResults.getComponent());
        queryResultScrollPane.getStyles().put("backgroundColor", new Color(24, 49, 76));

        queryResultBox.add(queryResultScrollPane);


        return queryResultBox;
    }

    private BoxPaneExt queryButtonsBox() {
        BoxPaneExt boxForQueryBtns = new BoxPaneExt();
        boxForQueryBtns.setFill();
        boxForQueryBtns.setHorizontalAlignment(HorizontalAlignment.CENTER);
        boxForQueryBtns.setVerticalAlignment(VerticalAlignment.CENTER);
        boxForQueryBtns.setSpacing(50);
        boxForQueryBtns.setPadding(30);

        // Button Creation
        PushButtonExt getVersionsBtn = new PushButtonExt("Get Versions");
        getVersionsBtn.setPadding(10);
        getVersionsBtn.setAction(new GetVersionAction(controlWebClient,this));

        PushButtonExt getFedListBtn = new PushButtonExt("Get Federate List");
        getFedListBtn.setPadding(10);
        getFedListBtn.setAction(new GetFederateListAction(controlWebClient,this));

        PushButtonExt getEventListBtn = new PushButtonExt("Get Event List");
        getEventListBtn.setPadding(10);
        getEventListBtn.setAction(new GetEventListAction(controlWebClient,this));

        PushButtonExt getPhysiologyListBtn = new PushButtonExt("Get Vital Signs List");
        getPhysiologyListBtn.setPadding(10);
        getPhysiologyListBtn.setAction(new GetPhysiologyListAction(controlWebClient,this));

        // Adding buttons to box
        boxForQueryBtns.add(getVersionsBtn);
        boxForQueryBtns.add(getFedListBtn);
        boxForQueryBtns.add(getEventListBtn);
        boxForQueryBtns.add(getPhysiologyListBtn);



        return boxForQueryBtns;
    }

    private BoxPaneExt mutationButtons() {
        BoxPaneExt buttonBox = new BoxPaneExt();
        buttonBox.setFill();
        buttonBox.setHorizontalAlignment(HorizontalAlignment.CENTER);
        buttonBox.setVerticalAlignment(VerticalAlignment.CENTER);
        buttonBox.setSpacing(50);
        buttonBox.setPadding(30);

        // Button Creation
        PushButtonExt CreateVitalSignsDialog = new PushButtonExt("Create Vital Signs");
        CreateVitalSignsDialog.setOnClicked((button -> new CreateVitalSignsDialog(controlWebClient, this).open(window)));
        CreateVitalSignsDialog.setPadding(10);

        PushButtonExt createEventDialog = new PushButtonExt("Create Event");
        createEventDialog.setOnClicked((button -> new CreateEventDialog(controlWebClient, this).open(window)));
        createEventDialog.setPadding(10);


        // Adding Buttons to Box
        buttonBox.add(CreateVitalSignsDialog);
        buttonBox.add(createEventDialog);

        return buttonBox;

    }

    public void setQueryText(String text){
        ApplicationContext.queueCallback(()->{
            queryResultTA.setText(text);
        });
    }

    public void disableJoinFedBtn() {
        joinFedBtn.setEnabled(false);
    }

}
