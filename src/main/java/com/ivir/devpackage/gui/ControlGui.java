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
import com.ivir.devpackage.gui.wrapper.TabPaneExt;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;

public class ControlGui implements Application {
    private Window window = null;
    private ControlWebClient controlWebClient;

    @Override
    public void startup(Display display, Map<String, String> map) throws TaskExecutionException {
        controlWebClient = new ControlWebClient();
        controlWebClient.setup();

        window = new Window();
        // Commenting out TabPane for now so that it can be used in future
//        TabPaneExt tabPane = new TabPaneExt();
//        tabPane.addTab("Main", new DevPackageMainPage(controlWebClient));

//        window.setContent(tabPane.getComponent());

        window.setContent(new DevPackageMainPage(controlWebClient, window).getComponent());
        window.setTitle("JETS Developer Package");
        window.setMaximized(true);

        window.open(display);
    }

    @Override
    public boolean shutdown(boolean b) throws Exception {
        return false;
    }

    @Override
    public void suspend() throws Exception {

    }

    @Override
    public void resume() throws Exception {

    }

    public static void main (String...args){
        DesktopApplicationContext.main(ControlGui.class, args);
    }
}
