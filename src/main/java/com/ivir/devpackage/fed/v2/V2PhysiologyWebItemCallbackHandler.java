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

package com.ivir.devpackage.fed.v2;

import com.ivir.devpackage.api.model.WebItem;
import com.ivir.devpackage.api.model.WebItemToHlaCallback;
import com.ivir.devpackage.fed.RespiratoryPhysiologyFedListener;
import com.ivir.devpackage.fed.VitalSignsFedListener;

public class V2PhysiologyWebItemCallbackHandler implements WebItemToHlaCallback {
    private VitalSignsFedListener vitalSignsFedListener;
    private RespiratoryPhysiologyFedListener respiratoryPhysiologyFedListener;
    public V2PhysiologyWebItemCallbackHandler(VitalSignsFedListener vitalSignsFedListener, RespiratoryPhysiologyFedListener respiratoryPhysiologyFedListener){
       this.vitalSignsFedListener = vitalSignsFedListener;
       this.respiratoryPhysiologyFedListener = respiratoryPhysiologyFedListener;
    }

    @Override
    public void sendToHla(WebItem webItem) {
        vitalSignsFedListener.v2PhysiologyHandlerSendToHla(webItem);
        respiratoryPhysiologyFedListener.v2PhysiologyHandlerSendToHla(webItem);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        vitalSignsFedListener.sendNewItemToHla(webItem);
        respiratoryPhysiologyFedListener.sendNewItemToHla(webItem);
        return webItem.get("patientId").toString();
    }
}
