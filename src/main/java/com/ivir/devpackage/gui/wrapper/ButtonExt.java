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

import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;

import java.awt.*;

public abstract class ButtonExt<T extends Button> extends ComponentExt<T> {
    protected abstract T getButton();

    public T getComponent(){
        return getButton();
    }

    public void setButtonData(Object data){
        getButton().setButtonData(data);
    }

    public void setText(String text){
        getButton().setButtonData(text);
    }

    public void setToggleButton(boolean toggle){
        getButton().setToggleButton(toggle);
    }

    public void setAction(Action action){
        getButton().setAction(action);
    }

    public void addButtonPressedListener(ButtonPressListener listener){
       getButton().getButtonPressListeners().add(listener);
    }

    public void setEnabled(boolean enabled){
        if(getButton().getAction() != null) {
            getButton().getAction().setEnabled(enabled);
        }
        getButton().setEnabled(enabled);
    }

    public void setBackgroundColor(Color color){
        getButton().getStyles().put("backgroundColor",color);
    }

    public void setBorderColor(Color color){
        getButton().getStyles().put("borderColor",color);
    }

    public void setColor(Color color){
        getButton().getStyles().put("color",color);
    }
}
