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

import org.apache.pivot.wtk.*;

import java.util.function.Consumer;

public class PushButtonBuilder {
    private PushButton pushButton;

    private PushButtonBuilder(){
        pushButton = new PushButton();
        pushButton.setMinimumHeight(40);
    }

    public static PushButtonBuilder create(){
        return new PushButtonBuilder();
    }

    public PushButton build(){
        return this.pushButton;
    }

    public PushButtonBuilder text(String text){
        pushButton.setButtonData(text);
        return this;
    }

    public PushButtonBuilder asToggle(){
        pushButton.setToggleButton(true);
        return this;
    }

    public PushButtonBuilder action(Action action){
        pushButton.setAction(action);
        return this;
    }

    public PushButtonBuilder onPressed(ButtonPressListener listener){
        pushButton.getButtonPressListeners().add(listener);
        return this;
    }

    public PushButtonBuilder padding(int padding){
        this.pushButton.getStyles().put("padding", padding);
        return this;
    }

    public PushButtonBuilder enabled(boolean enabled) {
        pushButton.setEnabled(enabled);
        return this;
    }

    public PushButtonBuilder onClicked(Consumer<Mouse.Button> clickedConsumer) {
        pushButton.getComponentMouseButtonListeners().add(new ComponentMouseButtonListener() {
            @Override
            public boolean mouseDown(Component component, Mouse.Button button, int i, int i1) {
                return false;
            }

            @Override
            public boolean mouseUp(Component component, Mouse.Button button, int i, int i1) {
                return false;
            }

            @Override
            public boolean mouseClick(Component component, Mouse.Button button, int i, int i1, int i2) {
                clickedConsumer.accept(button);
                return true;
            }
        });
        return this;
    }


}
