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

import org.apache.pivot.wtk.ColorChooser;

import java.awt.*;

public class ColorChooserExt extends ComponentExt<ColorChooser> {
    ColorChooser myColorChooser = new ColorChooser();

    public void setSpacing(int spacing){
        myColorChooser.getStyles().put("spacing", spacing);
    }
    public void setBackgroundColor(Color color){
        myColorChooser.getStyles().put("backgroundColor", color);
    }
    public void setBackgroundPaint(Color color){
        myColorChooser.getStyles().put("backgroundPaint", color);
    }
    public void setSelectedColor(Color color){
        myColorChooser.setSelectedColor(color);
    }
    //I'm unsure if I'm calling the method correctly on line 12 of ColorChooserExamplePane. Throws an error when I use it.
    public void setMyColorChooser(String color){
        setMyColorChooser(color);
    }
    public Color getSelectedColor(){
        return myColorChooser.getSelectedColor();
    }


    @Override
    public ColorChooser getComponent() {
        return myColorChooser;
    }
}
