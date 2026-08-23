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

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;

public class WebItemTests {
    @Test
    void constructor_withItemId_populatesMap(){
        WebItem sut = new WebItem(45);
        assertThat(sut.getItemId()).isEqualTo(45);
        Map<String,Object> mapIdentifier = (Map<String,Object>)sut.getMap().get("itemIdentifier");
        assertThat(mapIdentifier.get("itemId")).isEqualTo(45);
    }

    @Test
    void constructor_withIdentifiers_populatesMap(){
        WebItem sut = new WebItem("myinstance", 22, true);
        assertThat(sut.getInstanceName()).isEqualTo("myinstance");
        assertThat(sut.getItemId()).isEqualTo(22);
        Map<String,Object> mapIdentifier = (Map<String,Object>)sut.getMap().get("itemIdentifier");
        assertThat(mapIdentifier.get("itemId")).isEqualTo(22);
        assertThat(mapIdentifier.get("instanceName")).isEqualTo("myinstance");
        assertThat(mapIdentifier.get("isOwner")).isEqualTo(true);
    }

    @Test
    void constructor_withMap_populatesVariables(){
        WebItem sut = new WebItem(
                new MapBuilder()
                        .entry("patientId","Test")
                        .childMap("itemIdentifier", (child)->{
                            child.entry("itemId", 99)
                                 .entry("instanceName", "myInstance");
                        }).toMap(),true
        );
        assertThat(sut.getItemId()).isEqualTo(99);
        assertThat(sut.getInstanceName()).isEqualTo("myInstance");
    }

    @Test
    void put_updatesMap(){
        WebItem sut = new WebItem("myinstance", 22, true);
        sut.put("patientId", "p1");
        assertThat(sut.getMap()).contains(entry("patientId", "p1"));
    }

    @Test
    void put_updatesIdentifierVariables(){
        WebItem sut = new WebItem("myinstance", 22, true);
        sut.put("itemIdentifier",new MapBuilder()
                .entry("itemId", 99)
                .entry("instanceName", "newInstance")
                .toMap());
        assertThat(sut.getItemId()).isEqualTo(99);
        assertThat(sut.getInstanceName()).isEqualTo("newInstance");
    }

    @Test
    void setItemId_updatesOnlyItemId(){
        WebItem sut = new WebItem("myinstance", 22, true);
        sut.setItemId(99);
        Map<String,Object> mapIdentifier = (Map<String,Object>)sut.getMap().get("itemIdentifier");

        assertThat(mapIdentifier.get("itemId")).isEqualTo(99);
        assertThat(mapIdentifier.get("instanceName")).isEqualTo("myinstance");
        assertThat(mapIdentifier.get("isOwner")).isEqualTo(true);
    }

    @Test
    void setInstanceName_updateOnlyInstanceName(){
        WebItem sut = new WebItem("myinstance", 22, true);
        sut.setInstanceName("abcde");
        Map<String,Object> mapIdentifier = (Map<String,Object>)sut.getMap().get("itemIdentifier");

        assertThat(mapIdentifier.get("itemId")).isEqualTo(22);
        assertThat(mapIdentifier.get("instanceName")).isEqualTo("abcde");
        assertThat(mapIdentifier.get("isOwner")).isEqualTo(true);
    }

    @Test
    void replaceWith_newMapOnlyChangesNewItems(){
        WebItem sut = new WebItem(
                new MapBuilder()
                        .entry("patientId","Test")
                        .entry("heartRate", 21)
                        .childMap("itemIdentifier", (child)->{
                            child.entry("itemId", 11)
                                 .entry("instanceName", "myInstance");
                        }).toMap(),true
        );
        sut.replaceWith(new MapBuilder()
                .entry("heartRate",9)
                .entry("temp",78).toMap());
        Map<String,Object> mapIdentifier = (Map<String,Object>)sut.getMap().get("itemIdentifier");
        assertThat(mapIdentifier.get("itemId")).isEqualTo(11);
        assertThat(sut.getItemId()).isEqualTo(11);
        assertThat(sut.getMap().get("heartRate")).isEqualTo(9);
        assertThat(sut.getMap().get("patientId")).isEqualTo("Test");
        assertThat(sut.getMap().get("temp")).isEqualTo(78);
    }

    class MapBuilder {
        private HashMap<String,Object> map = new HashMap<String,Object>();
        public MapBuilder entry(String key, Object value){
            map.put(key,value);
            return this;
        }

        public MapBuilder childMap(String key, Consumer<MapBuilder> child){
            MapBuilder childBuilder = new MapBuilder();
            child.accept(childBuilder);
            this.map.put(key,childBuilder.toMap());
            return this;
        }

        public HashMap<String,Object> toMap(){
            return this.map;
        }
    }
}
