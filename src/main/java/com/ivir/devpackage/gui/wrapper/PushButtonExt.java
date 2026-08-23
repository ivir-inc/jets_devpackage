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
import org.apache.pivot.wtk.content.ButtonData;
import org.apache.pivot.wtk.media.Image;

public class PushButtonExt extends ButtonExt<PushButton>{
    private PushButton pushButton;

    @Override
    protected PushButton getButton() {
        return pushButton;
    }

    public PushButtonExt(){
        pushButton = new PushButton();
    }

    public PushButtonExt(String text){
        pushButton = new PushButton(text);
    }

    public PushButtonExt(String text, Image icon){
        ButtonData buttonData = new ButtonData(icon, text);
        pushButton = new PushButton(buttonData);
    }



    public void setPadding(int padding){
        this.pushButton.getStyles().put("padding", padding);
    }
}
