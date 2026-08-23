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

package com.ivir.devpackage.model;

import com.ivir.devpackage.api.model.WebItem;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.NitriteCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class StorageService {
    private static ConcurrentHashMap<String, Storage> storageByTypeMap = new ConcurrentHashMap<>();

    @Autowired
    Nitrite nitrite;

    public Storage getStorage(String storageType) {
        return storageByTypeMap.computeIfAbsent(storageType, (key)->{
            NitriteCollection collection = nitrite.getCollection(storageType);
            collection.createIndex(WebItem.ITEM_ID_KEY, WebItem.INSTANCE_NAME_KEY);
            return new Storage(storageType, collection);
        });
    }
}
