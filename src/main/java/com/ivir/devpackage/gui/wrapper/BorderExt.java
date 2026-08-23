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

import org.apache.pivot.wtk.Border;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.CornerRadii;

public class BorderExt extends ContainerExt<Border>{
    private Border border;

    public BorderExt(){
        this.border = new Border();
    }


    @Override
    protected Border getContainer() {
        return border;
    }

    @Override
    public void add(Component component){
        throw new RuntimeException("use set content instead Border");
    }

    @Override
    public void add(ComponentExt<?> componentExt){
        throw new RuntimeException("used set content instead for Border");
    }

    public void setContent(Component component){
        this.border.setContent(component);
    }

    public void setContent(ComponentExt<?> componentExt){
        this.border.setContent(componentExt.getComponent());
    }

    public void setCornerRadii(int radius){
        this.border.getStyles().put("cornerRadii", new CornerRadii(radius));
    }
}
