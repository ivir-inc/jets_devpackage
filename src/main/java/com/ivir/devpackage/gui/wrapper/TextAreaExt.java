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

import org.apache.pivot.wtk.TextArea;

import java.awt.*;
import java.io.IOException;

public class TextAreaExt extends ComponentExt<TextArea> {
    TextArea myTextArea = new TextArea();

    public TextAreaExt(){

    }

    public void setText(java.io.Reader textReader) throws IOException {
        myTextArea.setText(textReader);
    }
    public void setText(java.net.URL textURL) throws IOException {
        myTextArea.setText(textURL);
    }

    public void setBackGroundColor(Color color){
        myTextArea.getStyles().put("backgroundColor", color);
    }

    public void setColor(Color color){
        myTextArea.getStyles().put("color", color);
    }

    //Not sure what BasLine does
    public void setBaseLine(int baseline){
        myTextArea.getStyles().put("baseline", baseline);
    }

    //Not sure what LineWidth does
    public void setLineWidth(int lineWidth){
        myTextArea.getStyles().put("lineWidth", lineWidth);
    }

    public void setInactiveColor(Color color){
        myTextArea.getStyles().put("inactiveColor", color);
    }


    public void acceptsEnter(boolean acceptsEnter){
        myTextArea.getStyles().put("acceptsEnter", acceptsEnter);
    }
    public void acceptsTab(boolean acceptsTab){
        myTextArea.getStyles().put("acceptsTab", acceptsTab);
    }

    public void setFont(Font font){
        myTextArea.getStyles().put("font", font);
    }

    public void setInactiveSelectionBackgroundColor(Color color){
        myTextArea.getStyles().put("inactiveSelectionBackgroundColor", color);
    }
    public void setInactiveSelectionColor(Color color){
        myTextArea.getStyles().put("inactiveSelectionColor", color);
    }
    public void setSelectionBackgroundColor(Color color){
        myTextArea.getStyles().put("selectionBackgroundColor", color);
    }
    public void setSelectionColor(Color color){
        myTextArea.getStyles().put("selectionColor", color);
    }
    //Not sure what tabWidth does
    public void setTabWidth(int tabWidth){
        myTextArea.getStyles().put("tabWidth", tabWidth);
    }
    public void setWrapText(boolean wrapText){
        myTextArea.getStyles().put("wrapText", wrapText);
    }
    public void setMargin(int margin){
        myTextArea.getStyles().put("margin", margin);
    }
    public void setMaximumLength(int maximumLength){
        myTextArea.setMaximumLength(maximumLength);
    }

    public void setEnabled(boolean enabled){
        myTextArea.setEnabled(enabled);
    }

    public void setExpandTabs(boolean expandTabs){
        myTextArea.setExpandTabs(expandTabs);
    }

    public void setSize(int width, int height){
        myTextArea.setSize(width, height);
    }

    public void setText(String text){
        myTextArea.setText(text);
    }

    public void setEditable(boolean editable){
        myTextArea.setEditable(editable);
    }

    @Override
    public TextArea getComponent() {
        return myTextArea;
    }
}
