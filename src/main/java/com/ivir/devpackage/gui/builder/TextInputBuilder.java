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

import org.apache.pivot.wtk.*;

public class TextInputBuilder {
    private TextInput textInput;

    private TextInputBuilder(){
        textInput = new TextInput();
    }

    public static TextInputBuilder create(){
        return new TextInputBuilder();
    }

    public TextInput build(){
        return this.textInput;
    }

    public TextInputBuilder prompt(String prompt){
        textInput.setPrompt(prompt);
        return this;
    }

    public TextInputBuilder text(String text){
        textInput.setText(text);
        return this;
    }
}
