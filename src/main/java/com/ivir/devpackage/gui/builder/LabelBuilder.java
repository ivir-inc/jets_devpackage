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

package com.ivir.devpackage.gui.builder;

import org.apache.pivot.wtk.Label;

import java.awt.*;

public class LabelBuilder {
    private Label label;

    private LabelBuilder(){
        label = new Label();
    }

    public static LabelBuilder create(){
        return new LabelBuilder();
    }

    public Label build(){
        return this.label;
    }

    public LabelBuilder text(String text){
        this.label.setText(text);
        return this;
    }

    public LabelBuilder color(Color color){
        this.label.getStyles().put("color", color);
        return this;
    }

    public LabelBuilder font(Font font){
        label.getStyles().put("font", font);
        return this;
    }
}
