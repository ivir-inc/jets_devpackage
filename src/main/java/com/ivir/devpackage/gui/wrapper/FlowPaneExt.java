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
import org.apache.pivot.wtk.FlowPane;

import java.awt.*;

public class FlowPaneExt extends ContainerExt<FlowPane>{
    /**
     * Flow panes arrange components in a horizontal line, wrapping when the contents don't fit on a single line.
     * Components may be horizontally aligned to left, right, or center when there is space left over within a given
     * line, and may optionally be vertically aligned by to their baselines.
     */

    private FlowPane flowPane = new FlowPane();

    @Override
    protected FlowPane getContainer() {
        return flowPane;
    }


    public void add(Component ... components){
        for(Component component : components){
            this.add(component);
        }
    }

    public void add(ComponentExt<?> ... componentExts){
        for(ComponentExt<?> componentExt : componentExts){
            this.add(componentExt);
        }
    }

    public void setPadding(int padding){
        this.flowPane.getStyles().put("padding", padding);
    }

    public void setBackgroundColor(Color color){
        flowPane.getStyles().put("backgroundColor",color);
    }



}
