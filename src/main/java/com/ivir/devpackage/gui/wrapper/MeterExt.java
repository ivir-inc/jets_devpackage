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

import org.apache.pivot.wtk.Meter;
import org.apache.pivot.wtk.Orientation;

import java.awt.*;

public class MeterExt extends ComponentExt<Meter> {
    Meter myMeter = new Meter();
    Orientation orientation = Orientation.HORIZONTAL;

    public MeterExt(){

    }

    public MeterExt(Orientation orientation){
        myMeter.setOrientation(orientation);
    }

    public Orientation getOrientation(){
        return myMeter.getOrientation();
    }
    public double getPercentage(){
        return myMeter.getPercentage();
    }
    public String getText(){
        return myMeter.getText();
    }
    public void setOrientation(Orientation orientation){
        myMeter.setOrientation(orientation);
    }
    public void setPercentage(double percentage){
        myMeter.setPercentage(percentage);
    }
    public void setText(String text){
        myMeter.setText(text);
    }

    public void setColor(Color color) {
        myMeter.getStyles().put("color", color);
    }
    public void setGridColor(Color color) {
        myMeter.getStyles().put("gridColor", color);
    }
    public void setTextColor(Color color) {
        myMeter.getStyles().put("textColor", color);
    }
    public void setTextFillColor(Color color) {
        myMeter.getStyles().put("textFillColor", color);
    }
    public void setGridFrequency(double frequency){
        myMeter.getStyles().put("frequency", frequency);
    }
    public void setFont(Font font) {
        myMeter.getStyles().put("font", font);
    }

    @Override
    public Meter getComponent() {
        return myMeter;
    }
}
