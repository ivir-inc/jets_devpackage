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

package com.ivir.devpackage.fed.v1;

import com.ivir.devpackage.api.model.WebItem;
import devstudio.generatedcode.HlaUrineLab;
import devstudio.generatedcode.HlaUrineLabAttributes;
import devstudio.generatedcode.HlaUrineLabUpdater;

import static com.ivir.devpackage.fed.FedListenerUtility.toFloat;

public class UrineLabV1 {

    public static void updateWebItemSwitch(HlaUrineLab hlaUrineLab, WebItem webItem, HlaUrineLabAttributes.Attribute att){
        switch (att) {
            case URINE_KETONES -> webItem.put("ketones",hlaUrineLab.getUrineKetones());
        }
    }

    public static void updateAndSendKeySwitch(HlaUrineLabUpdater urineLabUpdater, String key, Object value){
        //V1 deprecated keys
        switch(key){
            case "ketones" -> urineLabUpdater.setUrineKetones(toFloat(value));
        }

    }


}
