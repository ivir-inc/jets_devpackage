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

import org.apache.pivot.wtk.validation.Validator;
import java.awt.*;

public class TextInputExt extends ComponentExt<org.apache.pivot.wtk.TextInput> {
    org.apache.pivot.wtk.TextInput myTextInput = new org.apache.pivot.wtk.TextInput();

    public TextInputExt(){

    }

    public void setBackGroundColor(Color color){
        myTextInput.getStyles().put("backgroundColor", color);
    }

    public void setBorderColor(Color color){
        myTextInput.getStyles().put("borderColor", color);
    }

    public void setColor(Color color){
        myTextInput.getStyles().put("color", color);
    }

    public void setBaseline(int baseline){
        myTextInput.getStyles().put("baseline", baseline);
    }

    public void setLineWidth(int lineWidth){
        myTextInput.getStyles().put("lineWidth", lineWidth);
    }

    public void setPromptColor(Color color){
        myTextInput.getStyles().put("promptColor", color);
    }

    public void setSelectionBackgroundColor(Color color){
        myTextInput.getStyles().put("selectionBackgroundColor", color);
    }

    public void setDisabledBackgroundColor(Color color){
        myTextInput.getStyles().put("disabledBackgroundColor", color);
    }
    public void setDisabledBorderColor(Color color){
        myTextInput.getStyles().put("disabledBorderColor", color);
    }
    public void setDisabledColor(Color color){
        myTextInput.getStyles().put("disabledColor", color);
    }

    public void setPrompt(String prompt){
            myTextInput.setPrompt(prompt);
    }

    public void setMaximumLength(int maximumLength){
        myTextInput.setMaximumLength(maximumLength);
    }

    public void setEditable(boolean editable){
        myTextInput.setEditable(editable);
    }

    public void setText(String text){
        myTextInput.setText(text);
    }

    public void setPassword(boolean password){
        myTextInput.setPassword(password);
    }

    public void setTextSize(int textSize){
        myTextInput.setTextSize(textSize);
    }
    public void setValidator(Validator validator){
        myTextInput.setValidator((org.apache.pivot.wtk.validation.Validator) validator);
    }

    public void setFont(Font font){
        myTextInput.getStyles().put("font", font);
    }
    public void setHorizontalAlignment(HorizontalAlignment alignment){
        myTextInput.getStyles().put("horizontalAlignment", alignment);
    }
    public void setInactiveSelectionBackgroundColor(Color color){
        myTextInput.getStyles().put("inactiveSelectionBackgroundColor", color);
    }
    public void setInactiveSelectionColor(Color color){
        myTextInput.getStyles().put("inactiveSelectionColor", color);
    }
    public void setInvalidBackgroundColor(Color color){
        myTextInput.getStyles().put("invalidBackgroundColor", color);
    }
    public void setInvalidColor(Color color){
        myTextInput.getStyles().put("invalidColor", color);
    }
    public void setSelectionColor(Color color){
        myTextInput.getStyles().put("selectionColor", color);
    }

    public void setEnabled(boolean enabled){
        myTextInput.setEnabled(enabled);
    }

    @Override
    public org.apache.pivot.wtk.TextInput getComponent() {
        return myTextInput;
    }
}
