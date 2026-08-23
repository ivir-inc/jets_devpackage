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
import org.apache.pivot.wtk.*;

import java.awt.*;

public class BoxPaneBuilder {
    private BoxPane boxPane;

    private BoxPaneBuilder(){
        boxPane = new BoxPane();
    }

    public static BoxPaneBuilder create(){
        return new BoxPaneBuilder();
    }

    public BoxPane build(){
        return this.boxPane;
    }


    public BoxPaneBuilder add(Component comp){
        this.boxPane.add(comp);
        return this;
    }

    public BoxPaneBuilder orientation(Orientation orientation){
        this.boxPane.setOrientation(orientation);
        return this;
    }

    /**
     * whether or not the box pane should size all components to fill the available space; if true, horizontal box
     * panes will make all components the same height, and vertical box panes will make all components the same width.
     * @param enableFill
     * @return
     */
    public BoxPaneBuilder fill(boolean enableFill){
        this.boxPane.getStyles().put("fill",enableFill);
        return this;
    }

    /**
     * whether or not the box pane should size all components to fill the available space; if true, horizontal box
     * panes will make all components the same height, and vertical box panes will make all components the same width.
     */
    public BoxPaneBuilder fill(){
        return fill(true);
    }


    public BoxPaneBuilder backgroundColor(Color color){
        this.boxPane.getStyles().put("backgroundColor", color);
        return this;
    }

    /**
     *  how the box pane aligns components on the x-axis.
     * @param alignment
     * @return
     */
    public BoxPaneBuilder horizontalAlignment(HorizontalAlignment alignment){
        this.boxPane.getStyles().put("horizontalAlignment", alignment);
        return this;
    }

    /**
     *  how the box pane aligns components on the y-axis
     * @param alignment
     * @return
     */
    public BoxPaneBuilder verticalAlignment(VerticalAlignment alignment){
        this.boxPane.getStyles().put("verticalAlignment", alignment);
        return this;
    }

    /**
     * the amount of space the box pane reserves around the perimeter of the container
     * @param padding
     * @return
     */
    public BoxPaneBuilder padding(int padding){
        this.boxPane.getStyles().put("padding", padding);
        return this;
    }

    /**
     * the amount of space the box pane inserts between components.
     * @param spacing
     * @return
     */
    public BoxPaneBuilder spacing(int spacing){
        this.boxPane.getStyles().put("spacing", spacing);
        return this;
    }

    public BoxPaneBuilder preferredWidth(int width){
        this.boxPane.setPreferredWidth(width);
        return this;
    }

}
