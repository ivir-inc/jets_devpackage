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

package com.ivir.devpackage.fed;

import static com.ivir.devpackage.fed.FedListenerUtility.*;

import com.ivir.devpackage.model.Storage;
import com.ivir.devpackage.api.model.WebItem;
import com.ivir.devpackage.api.model.WebItemToHlaCallback;
import devstudio.generatedcode.*;
import devstudio.generatedcode.datatypes.DocumentTypeEnum;
import devstudio.generatedcode.exceptions.HlaAttributeNotOwnedException;
import devstudio.generatedcode.exceptions.HlaInternalException;
import devstudio.generatedcode.exceptions.HlaNotConnectedException;
import devstudio.generatedcode.exceptions.HlaRtiException;

import java.util.Set;

public class DocumentFedListener implements HlaDocumentListener, WebItemToHlaCallback {
    private HlaDocumentManager documentManager;

    private Storage documentStorage;

    public DocumentFedListener(Storage storage, HlaDocumentManager documentManager){
        this.documentStorage = storage;
        this.documentManager = documentManager;
        this.documentStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaDocument hlaDocument, Set<HlaDocumentAttributes.Attribute> set, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!hlaDocument.isLocal()) {
            documentStorage.updateFromHla(hlaDocument.getHlaInstanceName(), (webItem) -> {
                updateWebItem(hlaDocument, set, webItem);
            });
        }

    }

    private void updateWebItem(HlaDocument hlaDocument, Set<HlaDocumentAttributes.Attribute> set, WebItem webItem){
        for(HlaDocumentAttributes.Attribute att : set){
            switch (att){
                case DOCUMENT_BODY -> webItem.put(att.getName(),toBase64(hlaDocument.getDocumentBody()));
                case DOCUMENT_NAME -> webItem.put(att.getName(),hlaDocument.getDocumentName());
                case DOCUMENT_TYPE -> webItem.put(att.getName(),hlaDocument.getDocumentType());
                case PATIENT_ID -> webItem.put(att.getName(),hlaDocument.getPatientId());
            }
        }
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaDocument document = this.documentManager.getDocumentByHlaInstanceName(webItem.getInstanceName());
        HlaDocumentUpdater documentUpdater = document.getHlaDocumentUpdater();
        updateAndSend(webItem, documentUpdater);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaDocument document = this.documentManager.createLocalHlaDocument();
            HlaDocumentUpdater documentUpdater = document.getHlaDocumentUpdater();
            updateAndSend(webItem, documentUpdater);
            return document.getHlaInstanceName();
        } catch (HlaNotConnectedException e) {
            throw new RuntimeException(e);
        } catch (HlaInternalException e) {
            throw new RuntimeException(e);
        } catch (HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaDocumentUpdater documentUpdater){
        webItem.getMap().forEach((key,value)->{
            HlaDocumentAttributes.Attribute att = HlaDocumentAttributes.Attribute.find(key);
            if((att != null) && (value != null)){
                switch (att){
                    case DOCUMENT_BODY -> documentUpdater.setDocumentBody(toByteArray(value));
                    case DOCUMENT_NAME -> documentUpdater.setDocumentName((String)value);
                    case DOCUMENT_TYPE -> documentUpdater.setDocumentType(toDocumentTypeEnum(value));
                    case PATIENT_ID -> documentUpdater.setPatientId((String)value);
                }
            }
        });
        try {
            documentUpdater.sendUpdate();
        } catch (HlaNotConnectedException e) {
            throw new RuntimeException(e);
        } catch (HlaAttributeNotOwnedException e) {
            throw new RuntimeException(e);
        } catch (HlaInternalException e) {
            throw new RuntimeException(e);
        } catch (HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private DocumentTypeEnum toDocumentTypeEnum(Object value){
        if(value == null){
            return null;
        }
        return DocumentTypeEnum.valueOf(value.toString());
    }
}
