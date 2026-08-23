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

import org.apache.pivot.wtk.Calendar;

import java.awt.*;

public class CalenderExt extends ComponentExt<Calendar> {
    Calendar myCalendar = new Calendar();

    public void setFont(Font font){
        myCalendar.getStyles().put("font", font);
    }
    public void setColor(Color color){
        myCalendar.getStyles().put("color", color);
    }
    public void setSelectionColor(Color color){
        myCalendar.getStyles().put("selectionColor", color);
    }
    public void setPadding(int padding){
        myCalendar.getStyles().put("padding", padding);
    }
    public void setHighlightColor(Color color){
        myCalendar.getStyles().put("highlightColor", color);
    }
    public void setDividerColor(Color color){
        myCalendar.getStyles().put("dividerColor", color);
    }
    public void setDisabledColor(Color color){
        myCalendar.getStyles().put("disabledColor", color);
    }
    public void setSelectionBackgroundColor(Color color){
        myCalendar.getStyles().put("selectionBackgroundColor", color);
    }
    public void setHighlightBackgroundColor(Color color){
        myCalendar.getStyles().put("highlightBackgroundColor", color);
    }
    public void setBackgroundColor(Color color){
        myCalendar.getStyles().put("backgroundColor", color);
    }
    public void setBackgroundPaint(Color color){
        myCalendar.getStyles().put("backgroundPaint", color);
    }

    @Override
    public Calendar getComponent() {
        return myCalendar;
    }
}
