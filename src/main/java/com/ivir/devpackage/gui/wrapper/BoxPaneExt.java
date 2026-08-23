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
import org.apache.pivot.wtk.*;
import java.awt.Color;

public class BoxPaneExt extends ContainerExt<BoxPane>{
    private final BoxPane boxPane = new BoxPane();

    public BoxPaneExt(){
    }

    public BoxPaneExt(Component component){
        boxPane.add(component);
    }

    public BoxPaneExt(ComponentExt<?> componentExt){
        boxPane.add(componentExt.getComponent());
    }

    @Override
    protected BoxPane getContainer() {
        return boxPane;
    }

    public void add(ComponentExt<?> ... componentExts){
        for(ComponentExt<?> compExt : componentExts){
            boxPane.add(compExt.getComponent());
        }
    }

    /**
     * whether or not the box pane should size all components to fill the available space; if true, horizontal box
     * panes will make all components the same height, and vertical box panes will make all components the same width.
     * @param enableFill
     */
    public void setFill(boolean enableFill){
        this.boxPane.getStyles().put("fill",enableFill);
    }

    /**
     * whether or not the box pane should size all components to fill the available space; if true, horizontal box
     * panes will make all components the same height, and vertical box panes will make all components the same width.
     */
    public void setFill(){
        this.setFill(true);
    }

    public void setBackgroundColor(Color color){
        this.boxPane.getStyles().put("backgroundColor", color);
    }

    public void setOrientation(Orientation orientation){
        this.boxPane.setOrientation(orientation);
    }

    /**
     *  how the box pane aligns components on the x-axis.
     * @param alignment
     * @return
     */
    public void setHorizontalAlignment(HorizontalAlignment alignment){
        this.boxPane.getStyles().put("horizontalAlignment", alignment);
    }

    /**
     *  how the box pane aligns components on the y-axis
     * @param alignment
     * @return
     */
    public void setVerticalAlignment(VerticalAlignment alignment){
        this.boxPane.getStyles().put("verticalAlignment", alignment);
    }

    /**
     * the amount of space the box pane reserves around the perimeter of the container
     * @param padding
     * @return
     */
    public void setPadding(int padding){
        this.boxPane.getStyles().put("padding", padding);
    }

    /**
     * the amount of space the box pane inserts between components.
     * @param spacing
     * @return
     */
    public void setSpacing(int spacing){
        this.boxPane.getStyles().put("spacing", spacing);
    }
}
