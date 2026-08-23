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

import org.apache.pivot.wtk.Checkbox;

public class CheckboxExt extends ButtonExt<Checkbox> {
    private Checkbox checkbox;

    public CheckboxExt(){
        this.checkbox = new Checkbox();
    }

    public CheckboxExt(String label){
        this.checkbox = new Checkbox(label);
    }

    public CheckboxExt(Object buttonData){
        this.checkbox = new Checkbox(buttonData);
    }

    @Override
    protected Checkbox getButton() {
        return checkbox;
    }


}
