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
import org.apache.pivot.wtk.Separator;

import java.awt.*;

/**
 * Separators are simple components that are similar to a horizontal rule in HTML. They have an optional heading
 * and are generally used to partition content
 */
public class SeparatorExt extends ComponentExt<Separator> {
    private Separator separator;

    public SeparatorExt(){
        separator = new Separator();
    }

    public SeparatorExt(String title){
        separator = new Separator(title);
    }

    @Override
    public Separator getComponent() {
        return separator;
    }

    public void setThickness(int thickness){
        separator.getStyles().put("thickness", thickness);
    }

    public void setColor(Color color) {
        separator.getStyles().put("color", color);
    }

    public void setFont(Font font) {
        separator.getStyles().put("font", font);
    }

    public void setPadding(int top, int left, int bottom, int right){
        Insets padding = new Insets(top, left, bottom, right);
        separator.getStyles().put("padding", new Insets(padding));
    }

    public void setHeadingColor(Color headingColor){
        separator.getStyles().put("headingColor", headingColor);
    }


}
