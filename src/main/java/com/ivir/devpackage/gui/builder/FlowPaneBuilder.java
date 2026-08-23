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

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.FlowPane;

public class FlowPaneBuilder {
    /**
     * Flow panes arrange components in a horizontal line, wrapping when the contents don't fit on a single line.
     * Components may be horizontally aligned to left, right, or center when there is space left over within a given
     * line, and may optionally be vertically aligned by to their baselines.
     */

    private FlowPane flowPane;

    private FlowPaneBuilder(){
        flowPane = new FlowPane();
    }

    public static FlowPaneBuilder create(){
        return new FlowPaneBuilder();
    }

    public FlowPane build(){
        return this.flowPane;
    }

    public FlowPaneBuilder padding(int padding){
        this.flowPane.getStyles().put("padding", padding);
        return this;
    }


    public FlowPaneBuilder add(Component comp){
        this.flowPane.add(comp);
        return this;
    }

}
