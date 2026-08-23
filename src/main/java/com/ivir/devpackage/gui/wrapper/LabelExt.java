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

import org.apache.pivot.wtk.HorizontalAlignment;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.TextDecoration;
import org.apache.pivot.wtk.VerticalAlignment;

import java.awt.*;

public class LabelExt extends ComponentExt<Label> {
    private Label label = new Label();

    public LabelExt(){

    }

    public LabelExt(String text){
        setText(text);
    }

    public LabelExt(String text, Color color){
        setText(text);
        setColor(color);
    }

    public LabelExt(String text, Color color, Font font){
        setText(text);
        setColor(color);
        setFont(font);
    }

    @Override
    public Label getComponent() {
        return label;
    }

    public void setConfiguration(LabelConfig ... labelConfigs){
        for(LabelConfig config : labelConfigs){
            switch (config){
                case UNDERLINE -> setUnderline();
                case STRIKETHROUGH -> setStrikethrough();
                case HORIZONTAL_CENTER -> setHorizontalAlignment(HorizontalAlignment.CENTER);
                case HORIZONTAL_LEFT -> setHorizontalAlignment(HorizontalAlignment.LEFT);
                case HORIZONTAL_RIGHT -> setHorizontalAlignment(HorizontalAlignment.RIGHT);
                case VERTICAL_BOTTOM -> setVerticalAlignment(VerticalAlignment.BOTTOM);
                case VERTICAL_CENTER -> setVerticalAlignment(VerticalAlignment.CENTER);
                case VERTICAL_TOP -> setVerticalAlignment(VerticalAlignment.TOP);
            }
        }
    }

    public void setText(String text) {
        this.label.setText(text);
    }

    public void setColor(Color color) {
        this.label.getStyles().put("color", color);
    }

    public void setFont(Font font) {
        label.getStyles().put("font", font);
    }

    public void setHorizontalAlignment(HorizontalAlignment alignment) {
        label.getStyles().put("horizontalAlignment", alignment);
    }

    public void setVerticalAlignment(VerticalAlignment alignment) {
        label.getStyles().put("verticalAlignment", alignment);
    }

    public void setToolTip(String text) {
        label.setTooltipText(text);
    }

    public void setBackgroundColor(Color color) {
        label.getStyles().put("backgroundColor", color);
    }

    public void setUnderline() {
        label.getStyles().put("textDecoration", TextDecoration.UNDERLINE);
    }

    public void setStrikethrough() {
        label.getStyles().put("textDecoration", TextDecoration.STRIKETHROUGH);
    }
}
