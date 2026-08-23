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
import com.ivir.devpackage.api.model.WebItemToHlaCallback;
import com.ivir.devpackage.db.NitriteConfig;
import org.dizitart.no2.collection.NitriteCollection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = StorageTest.class)
public class StorageTest {
    private static NitriteCollection collection;

    @BeforeAll
    public static void oneTimeSetup(){
        collection = new NitriteConfig().nitriteDatabase().getCollection("test");
    }

    @Mock
    WebItemToHlaCallback webItemToHlaCallback;

    @InjectMocks
    private Storage sut = new Storage("test", collection);

    @Test
    public void getAllItems_getsAll(){
        sut.updateFromWeb(buildWebItem(1));
        sut.updateFromWeb(buildWebItem(2));
        sut.updateFromWeb(buildWebItem(3));

        assertEquals(3,sut.getAllItems().size());
    }

    @Test
    public void getItemById_getsTheCorrectItem(){
        sut.updateFromWeb(buildWebItem(1));
        sut.updateFromWeb(buildWebItem(2));
        sut.updateFromWeb(buildWebItem(3));
        WebItem webItem = sut.getItemById(2).get();
        assertEquals("test value2", webItem.get("testVar"));
    }

    private WebItem buildWebItem(int idNum){
        WebItem webItem = new WebItem(idNum);
        webItem.put("testVar", "test value" + idNum);
        return webItem;
    }
}
