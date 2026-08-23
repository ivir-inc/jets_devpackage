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

import java.util.Base64;

public class FedListenerUtility {
    public static float toFloat(Object valueOrg){
        return ((Double)valueOrg).floatValue();
    }

    public static Integer toInteger(Object valueOrg){
        return (Integer) valueOrg;
    }

    public static Boolean toBoolean(Object valueOrg){
        return (Boolean) valueOrg;
    }

    public static Double toDouble(Object valueOrg){
        return (Double) valueOrg;
    }

    public static byte[] toByteArray(Object base64String){
        return Base64.getDecoder().decode(base64String.toString());
    }

    public static String toBase64(byte[] arrayToEncode){
        return Base64.getEncoder().encodeToString(arrayToEncode);
    }

    /**
     * Convert HlA Enum to WebItem value.  If the value is null, then return "NOT_APPLICABLE"
     */
    public static <T extends Enum<T>> String toWebItemValueWithNotApplicable(T enumValue) {
        return enumValue == null ? "NOT_APPLICABLE" : enumValue.name();
    }

    /**
     * Convert HlA Enum to WebItem value.  If the value is null, then return "NOT_APPLICABLE"
     */
    public static <T extends Enum<T>> String toWebItemValueWithNull(T enumValue) {
        return enumValue == null ? null : enumValue.name();
    }
}

