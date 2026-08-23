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

import org.apache.pivot.wtk.TreeView;

import java.awt.*;

import org.apache.pivot.collections.List;

public class TreeViewExt extends ComponentExt<TreeView> {
    TreeView myTreeView = new TreeView();

    public TreeViewExt(){

    }

    public void setTreeData(List<?> treeData){
        myTreeView.setTreeData((org.apache.pivot.collections.List<?>) treeData);
    }

    public void setColor(Color color){
        myTreeView.getStyles().put("color", color);
    }
    public void setBackgroundColor(Color color){
        myTreeView.getStyles().put("backgroundColor", color);
    }
    public void setBranchControlColor(Color color){
        myTreeView.getStyles().put("branchControlColor", color);
    }
    public void setSelectionColor(Color color){
        myTreeView.getStyles().put("selectionColor", color);
    }
    public void setHighlightBackgroundColor(Color color){
        myTreeView.getStyles().put("highlightBackgroundColor", color);
    }
    public void setSelectionBackgroundColor(Color color){
        myTreeView.getStyles().put("selectionBackgroundColor", color);
    }
    public void setDisabledColor(Color color){
        myTreeView.getStyles().put("disabledColor", color);
    }
    public void setHighlightColor(Color color){
        myTreeView.getStyles().put("highlightColor", color);
    }
    public void setControlSelectionColor(Color color){
        myTreeView.getStyles().put("controlSelectionColor", color);
    }
    public void setGridColor(Color color){
        myTreeView.getStyles().put("gridColor", color);
    }
    public void setPreferredSize(int width, int height){
        myTreeView.setPreferredSize(width, height);
    }

    public void getNodeRender(){
        myTreeView.getNodeRenderer();
    }
    public void setNodeRender(TreeView.NodeRenderer nodeRenderer){
        myTreeView.setNodeRenderer(nodeRenderer);
    }
    public void setFont(Font font){
        myTreeView.getStyles().put("font", font);
    }
    public void setSpacing(int spacing){
        myTreeView.getStyles().put("spacing", spacing);
    }
    public void showGridLines(boolean showGridLines){
        myTreeView.getStyles().put("showGridLines", showGridLines);
    }

    @Override
    public TreeView getComponent() {
        return myTreeView;
    }
}
