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

package com.ivir.devpackage.gui.wrapper;

import org.apache.pivot.wtk.FileBrowser;

import java.awt.*;

public class FileBrowserExt extends ComponentExt<FileBrowser> {
    FileBrowser myFileBrowser = new FileBrowser();

    public FileBrowserExt(){

    }

    public void setBackgroundColor(Color color){
        myFileBrowser.getStyles().put("backgroundColor", color);
    }

    public void setbackGroundPaint(Color color){
        myFileBrowser.getStyles().put("backgroundPaint", color);
    }
    public void setMultiSelect(boolean selectState){
        myFileBrowser.setMultiSelect(selectState);
    }

    @Override
    public FileBrowser getComponent() {
        return myFileBrowser;
    }
}
