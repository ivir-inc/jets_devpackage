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

import org.apache.pivot.wtk.Insets;
import org.apache.pivot.wtk.ListView;
import org.apache.pivot.collections.List;

import java.awt.*;
import java.net.URL;

public class ListViewExt extends ComponentExt<ListView> {
    ListView myListView = new ListView();

    public ListViewExt(){

    }
    public ListViewExt(List<?> listData){
        setListData(listData);
    }

    public void setListData(String listData){
        myListView.setListData(listData);
    }
    public void setListData(List<?> listData){
        myListView.setListData(listData);
    }
    public void setListData(URL listData){
        myListView.setListData(listData);
    }

    public void setWrapSelectNext(boolean wrapSelectNext){
        myListView.getStyles().put("wrapSelectNext", wrapSelectNext);
    }
    public void setShowHighlight(boolean showHighlight){
        myListView.getStyles().put("showHighlight", showHighlight);
    }
    public void setCheckboxPadding(Insets checkboxPadding){
        myListView.getStyles().put("checkboxPadding", checkboxPadding);
    }
    public void setDisabledColor(Color color){
        myListView.getStyles().put("disabledColor", color);
    }
    public void setFont(Font font){
        myListView.getStyles().put("font", font);
    }
    public void setColor(Color color){
        myListView.getStyles().put("color", color);
    }
    public void setSelectionColor(Color color){
        myListView.getStyles().put("selectionColor", color);
    }
    public void setBackgroundColor(Color color){
        myListView.getStyles().put("backgroundColor", color);
    }
    public void setHighlightBackgroundColor(Color color){
        myListView.getStyles().put("highlightBackgroundColor", color);
    }
    public void setSelectionBackgroundColor(Color color){
        myListView.getStyles().put("selectionBackgroundColor", color);
    }
    public void setAlternateItemBackgroundColor(Color color){
        myListView.getStyles().put("alternateItemBackgroundColor", color);
    }
    public void setVariableItemHeight(boolean variableItemHeight){
        myListView.getStyles().put("variableItemHeight", variableItemHeight);
    }
    public void setEnabled(boolean enabled){
        myListView.setEnabled(enabled);
    }

    @Override
    public ListView getComponent() {
        return myListView;
    }
}
