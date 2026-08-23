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
import org.apache.pivot.wtk.Orientation;
import org.apache.pivot.wtk.SplitPane;

public class SplitPaneExt extends ContainerExt<SplitPane> {
    private final SplitPane splitPane;

    public SplitPaneExt(){
        splitPane = new SplitPane();
    }

    public SplitPaneExt(Orientation orientation){
        splitPane = new SplitPane(orientation);
    }

    public SplitPaneExt(Orientation orientation, Component topLeft, Component bottomRight){
        splitPane = new SplitPane(orientation, topLeft, bottomRight);
    }

    public SplitPaneExt(Orientation orientation, ComponentExt<?> topLeft, ComponentExt<?> bottomRight){
        splitPane = new SplitPane(orientation, topLeft.getComponent(), bottomRight.getComponent());
    }

    @Override
    protected SplitPane getContainer() {
        return splitPane;
    }

    public void setOrientation(Orientation orientation){
        this.splitPane.setOrientation(orientation);
    }

    @Override
    public void add(Component component){
        throw new RuntimeException("use set top, set bottom, instead");
    }

    @Override
    public void add(ComponentExt<?> componentExt ){
        throw new RuntimeException("use set top, set bottom, instead");
    }

    public void setTop(ComponentExt<?> componentExt) {
        this.splitPane.setTop(componentExt.getComponent());
    }

    public  void setTop(Component component) {
        this.splitPane.setTop(component);
    }

    public void setLeft(ComponentExt<?> componentExt) {
        this.splitPane.setLeft(componentExt.getComponent());
    }

    public  void setLeft(Component component) {
        this.splitPane.setLeft(component);
    }

    public  void setRight(Component component) {
        this.splitPane.setRight(component);
    }

    public void setRight(ComponentExt<?> componentExt) {
        this.splitPane.setRight(componentExt.getComponent());
    }

    public  void setBottom(Component component) {
        this.splitPane.setBottom(component);
    }

    public void setBottom(ComponentExt<?> componentExt) {
        this.splitPane.setBottom(componentExt.getComponent());
    }

    public  void setTopLeft(Component component) {
        this.splitPane.setTopLeft(component);
    }

    public void setTopLeft(ComponentExt<?> componentExt) {
        this.splitPane.setTopLeft(componentExt.getComponent());
    }

    public  void setBottomRight(Component component) {
        this.splitPane.setBottomRight(component);
    }

    public void setBottomRight(ComponentExt<?> componentExt) {
        this.splitPane.setBottomRight(componentExt.getComponent());
    }

    public void setSplitRatio(Float splitRatio) {
        this.splitPane.setSplitRatio(splitRatio);
    }

}
