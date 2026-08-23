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

import org.apache.pivot.wtk.RadioButton;
import org.apache.pivot.wtk.RadioButtonGroup;

public class RadioButtonExt extends ButtonExt<RadioButton> {
    private RadioButton radioButton;

    public RadioButtonExt(RadioButtonExt group){
        radioButton = new RadioButton(group);
    }

    public RadioButtonExt(RadioButtonGroup group, String label){
        radioButton = new RadioButton(group, label);
    }

    public RadioButtonExt(RadioButtonGroup group, Object data){
        radioButton = new RadioButton(group, data);
    }

    @Override
    protected RadioButton getButton() {
        return radioButton;
    }
}
