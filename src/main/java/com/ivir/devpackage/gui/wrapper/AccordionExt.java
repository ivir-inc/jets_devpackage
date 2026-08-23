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

import org.apache.pivot.wtk.Accordion;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Insets;

import java.awt.*;

public class AccordionExt extends ComponentExt<Accordion> {
    Accordion myAccordion = new Accordion();

    public AccordionExt(){}


    public void setHeaderDataRender(Button.DataRenderer headerDataRender){
        myAccordion.setHeaderDataRenderer(headerDataRender);

    }
    public void setSelectedIndex(int selectedIndex){
        myAccordion.setSelectedIndex(selectedIndex);
    }

    public void addComponent(Component component){
        myAccordion.getPanels().add(component);
    }

    public void setSelectionChangeDuration(int selectionChangeDuration){
        myAccordion.getStyles().put("selectionChangeDuration", selectionChangeDuration);
    }
    public void setSelectionChangeRate(int selectionChangeRate){
        myAccordion.getStyles().put("selectionChangeRate", selectionChangeRate);
    }
    public void setDisabledButtonColor(Color color){
        myAccordion.getStyles().put("disabledButtonColor", color);
    }
    public void setButtonBackgroundColor(Color color){
        myAccordion.getStyles().put("ButtonBackgroundColor", color);
    }
    public void setPadding(Insets padding){
        myAccordion.getStyles().put("padding", padding);
    }
    public void setBorderColor(Color color){
        myAccordion.getStyles().put("borderColor", color);
    }
    public void setButtonColor(Color color){
        myAccordion.getStyles().put("buttonColor", color);
    }
    public void setButtonFont(Font font){
        myAccordion.getStyles().put("buttonFont", font);
    }
    public void setButtonPadding(Insets padding){
        myAccordion.getStyles().put("padding", padding);
    }
    public void setBackgroundColor(Color color){
        myAccordion.getStyles().put("backgroundColor", color);
    }
    public void setBackgroundPaint(Color color){
        myAccordion.getStyles().put("backgroundPaint", color);
    }


    @Override
    public Accordion getComponent() {
        return myAccordion;
    }
}
