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

import devstudio.generatedcode.datatypes.*;

import java.util.HashMap;
import java.util.Map;

public class BodyLocationMapper {

    /**
     * use this method to convert bodylocation record into something that can be serialized.
     * @param bodyLocationRecord
     * @return
     */
    public static Map<String,Object> toSerializableObject(BodyLocationRecord bodyLocationRecord){
        HashMap<String, Object> outMap = new HashMap<>();
        outMap.put("coronalPlane",bodyLocationRecord.getCoronalPlane());
        outMap.put("detailedAnatomy",bodyLocationRecord.getDetailedAnatomy());
        outMap.put("fmaid",bodyLocationRecord.getFmaid());
        outMap.put("generalRegion",bodyLocationRecord.getGeneralRegion());
        outMap.put("internalAnatomy",bodyLocationRecord.getInternalAnatomy());
        outMap.put("regionTissueType",bodyLocationRecord.getRegionTissueType());
        outMap.put("sagittalPlane",bodyLocationRecord.getSagittalPlane());
        outMap.put("skeletalSystem",bodyLocationRecord.getSkeletalSystem());
        outMap.put("transversePlane",bodyLocationRecord.getTransversePlane());
        return outMap;
    }

    // Convert any value to BodyLocationRecord, handling both enums and existing records
    public static BodyLocationRecord toBodyLocationRecord(Object value) {
        if (value == null) {
            return null;
        }
        Map<String, Object> map = (Map<String, Object>) value;

        return BodyLocationRecord.create(
                toGeneralRegionEnum(map, "generalRegion"),
                toRegionTissueTypeEnum(map, "regionTissueType"),
                toInternalAnatomyEnum(map, "internalAnatomy"),
                toSagittalPlaneEnum(map, "sagittalPlane"),
                toTransversePlaneEnum(map, "transversePlane"),
                toCoronalPlaneEnum(map, "coronalPlane"),
                toSkeletalSystemEnum(map, "skeletalSystem"),
                toDetailedAnatomyEnum(map, "detailedAnatomy"),
                toFmaId(map, "fmaid")
        );

    }

    private static GeneralRegionEnum toGeneralRegionEnum(Map<String,Object> map, String key){
       Object value = map.get(key);
       if(value == null){
           return null;
       }
       return GeneralRegionEnum.valueOf(value.toString());
    }

    private static RegionTissueTypeEnum toRegionTissueTypeEnum(Map<String,Object> map, String key){
        Object value = map.get(key);
        if(value == null){
            return null;
        }
        return RegionTissueTypeEnum.valueOf(value.toString());
    }

    private static InternalAnatomyEnum toInternalAnatomyEnum(Map<String,Object> map, String key){
        Object value = map.get(key);
        if(value == null){
            return null;
        }
        return InternalAnatomyEnum.valueOf(value.toString());
    }

    private static SagittalPlaneEnum toSagittalPlaneEnum(Map<String,Object> map, String key){
        Object value = map.get(key);
        if(value == null){
            return null;
        }
        return SagittalPlaneEnum.valueOf(value.toString());
    }


    private static TransversePlaneEnum toTransversePlaneEnum(Map<String,Object> map, String key){
        Object value = map.get(key);
        if(value == null){
            return null;
        }
        return TransversePlaneEnum.valueOf(value.toString());
    }

    private static CoronalPlaneEnum toCoronalPlaneEnum(Map<String,Object> map, String key){
        Object value = map.get(key);
        if(value == null){
            return null;
        }
        return CoronalPlaneEnum.valueOf(value.toString());
    }

    private static SkeletalSystemEnum toSkeletalSystemEnum(Map<String,Object> map, String key){
        Object value = map.get(key);
        if(value == null){
            return null;
        }
        return SkeletalSystemEnum.valueOf(value.toString());
    }

    private static DetailedAnatomyEnum toDetailedAnatomyEnum(Map<String,Object> map, String key){
        Object value = map.get(key);
        if(value == null){
            return null;
        }
        return DetailedAnatomyEnum.valueOf(value.toString());
    }

    private static int toFmaId(Map<String,Object> map, String key){
        Object value = map.get(key);
        if(value == null){
            return 0;
        }
        return (Integer) value;
    }

    // Default BodyLocationRecord instance
    private static BodyLocationRecord createDefaultRecord() {
        return BodyLocationRecord.create(
            GeneralRegionEnum.NOT_APPLICABLE,
            RegionTissueTypeEnum.NOT_APPLICABLE,
            InternalAnatomyEnum.NOT_APPLICABLE,
            SagittalPlaneEnum.NOT_APPLICABLE,
            TransversePlaneEnum.NOT_APPLICABLE,
            CoronalPlaneEnum.NOT_APPLICABLE,
            SkeletalSystemEnum.NOT_APPLICABLE,
            DetailedAnatomyEnum.NOT_APPLICABLE,
            0
        );
    }
}


