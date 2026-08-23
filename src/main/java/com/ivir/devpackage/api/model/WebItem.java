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

package com.ivir.devpackage.api.model;

import org.dizitart.no2.collection.Document;
import org.dizitart.no2.common.tuples.Pair;

import java.util.HashMap;
import java.util.Map;

public class WebItem {
    public static final String ITEM_IDENTIFIER_KEY="itemIdentifier";
    public static final String ITEM_ID_KEY="itemId";
    public static final String INSTANCE_NAME_KEY="instanceName";
    public static final String IS_OWNER_KEY = "isOwner";
    Document document;
    private String type;

    public WebItem(Document document){
        this.document = document;
    }

    public WebItem(Integer itemId){
        document = Document.createDocument();
        document.put(ITEM_ID_KEY, itemId);
    }

    public WebItem(String instanceName, Integer itemId, Boolean isOwner){
        document = Document.createDocument();
        document.put(ITEM_ID_KEY, itemId);
        document.put(INSTANCE_NAME_KEY, instanceName);
        document.put(IS_OWNER_KEY, isOwner);
    }

    public WebItem(Map<String, Object> map, Boolean isOwner){
        Map<String,Object> identifierMap = new HashMap<>();
        if(map.containsKey(ITEM_IDENTIFIER_KEY)){
            identifierMap = (Map<String,Object>)map.remove(ITEM_IDENTIFIER_KEY);
        }
        document = Document.createDocument(map);
        identifierMap.computeIfPresent(INSTANCE_NAME_KEY,(key,value)->document.put(key,value));
        identifierMap.computeIfPresent(ITEM_ID_KEY,(key,value)->document.put(key,value));
        document.put(IS_OWNER_KEY, isOwner);
    }

    public void put(String key, Object value){
        if(key == ITEM_IDENTIFIER_KEY){
            Map<String,Object> identifierMap = (Map<String,Object>)value;
            identifierMap.computeIfPresent(ITEM_ID_KEY,(idKey,idValue)->document.put(idKey,idValue));
            identifierMap.computeIfPresent(INSTANCE_NAME_KEY,(idKey,idValue)->document.put(idKey,idValue));
            identifierMap.computeIfPresent(IS_OWNER_KEY,(idKey,idValue)->document.put(idKey,idValue));
        }else{
            document.put(key, value);
        }
    }

    public Object get(String key){
        return document.get(key);
    }

    public boolean contains(String key){
        return document.containsKey(key);
    }

    public Map<String, Object> getMap(){
        HashMap<String, Object> docMap = new HashMap<>();
        HashMap<String, Object> identifierMap = new HashMap<>();
        for (Pair<String, Object> stringObjectPair : document) {
            switch (stringObjectPair.getFirst()){
                case ITEM_ID_KEY:
                case INSTANCE_NAME_KEY:
                case IS_OWNER_KEY:
                    identifierMap.put(stringObjectPair.getFirst(), stringObjectPair.getSecond());
                default:
                    docMap.put(stringObjectPair.getFirst(), stringObjectPair.getSecond());
            }
        }
        docMap.put(ITEM_IDENTIFIER_KEY, identifierMap);
        return docMap;
    }

    public Document getDocument(){
        return this.document;
    }

    public Integer getItemId(){
        return document.get(ITEM_ID_KEY, Integer.class);
    }

    public WebItem setItemId(Integer itemId) {
        document.put(ITEM_ID_KEY, itemId);
        return this;
    }

    public String getInstanceName(){
        return document.get(INSTANCE_NAME_KEY, String.class);
    }

    public WebItem setInstanceName(String instanceName){
        document.put(INSTANCE_NAME_KEY, instanceName);
        return this;
    }

    public WebItem replaceWith(Map<String,Object> newMap){
        Map<String,Object> identifierMap = new HashMap<>();
        if(newMap.containsKey(ITEM_IDENTIFIER_KEY)){
            identifierMap = (Map<String,Object>)newMap.remove(ITEM_IDENTIFIER_KEY);
        }
        identifierMap.computeIfPresent(INSTANCE_NAME_KEY,(key,value)->document.put(key,value));
        identifierMap.computeIfPresent(ITEM_ID_KEY,(key,value)->document.put(key,value));
        identifierMap.computeIfPresent(IS_OWNER_KEY,(key,value)->document.put(key,value));

        newMap.forEach((key, value)->{
            document.put(key,value);
        });
        return this;
    }

    public WebItem setType(String type){
       this.type = type;
       return this;
    }

    public String getType(){
        return this.type;
    }

    @Override
    public String toString() {
        return "WebItem{" +
                "document=" + document +
                ", type='" + type + '\'' +
                '}';
    }
}
