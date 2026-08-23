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
import org.apache.pivot.wtk.ComponentMouseButtonListener;
import org.apache.pivot.wtk.Mouse;

import java.util.function.Consumer;

public abstract class ComponentExt<T extends Component>{
    public abstract T getComponent();

    public void setPreferredHeight(int height){
        getComponent().setPreferredHeight(height);
    }

    public void setPreferredWidth(int width){
        getComponent().setPreferredWidth(width);
    }

    public void setOnClicked(Consumer<Mouse.Button> clickedConsumer) {
        getComponent().getComponentMouseButtonListeners().add(new ComponentMouseButtonListener() {
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
    }
}
