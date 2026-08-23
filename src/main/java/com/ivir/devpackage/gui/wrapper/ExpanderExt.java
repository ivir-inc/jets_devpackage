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

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Expander;
import org.apache.pivot.wtk.Insets;

import java.awt.*;

public class ExpanderExt extends ComponentExt<Expander> {
    Expander myExpander = new Expander();

    public ExpanderExt(){}

    public void setCollapsible(boolean collapsible){
        myExpander.setCollapsible(collapsible);
    }
    public void setContent(Component content){
        myExpander.setContent(content);
    }
    public void setExpanded(boolean expanded){
        myExpander.setExpanded(expanded);
    }
    public void setTitle(String title){
        myExpander.setTitle(title);
    }
    public void setPadding(Insets padding){
        myExpander.getStyles().put("padding", padding);
    }
    public void setTitleBarColor(Color color){
        myExpander.getStyles().put("color", color);
    }
    public void setExpandDuration(int expandDuration){
        myExpander.getStyles().put("expandDuration", expandDuration);
    }
    public void setExpandRate(int expandRate){
        myExpander.getStyles().put("expandRate", expandRate);
    }
    public void setTitleBarFont(Font font){
        myExpander.getStyles().put("Font", font);
    }
    public void setTitleBarBackgroundColor(Color color){
        myExpander.getStyles().put("titleBarBackgroundColor", color);
    }
    public void setBarBorderColor(Color color){
        myExpander.getStyles().put("barBorderColor", color);
    }
    public void setDisabledShadeButtonColor(Color color){
        myExpander.getStyles().put("disabledShadeButtonColor", color);
    }
    public void setShadeButtonColor(Color color){
        myExpander.getStyles().put("shadeButtonColor", color);
    }
    public void setBorderColor(Color color){
        myExpander.getStyles().put("borderColor", color);
    }
    public void setBackgroundPaint(Color color){
        myExpander.getStyles().put("backgroundPaint", color);
    }
    public void setBackgroundColor(Color color){
        myExpander.getStyles().put("backgroundColor", color);
    }

    @Override
    public Expander getComponent() {
        return myExpander;
    }
}
