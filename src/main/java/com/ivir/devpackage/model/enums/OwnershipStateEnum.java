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

package com.ivir.devpackage.model.enums;

public enum OwnershipStateEnum {
    UNKNOWN(false),
    CREATED(true),
    DISCOVERED(false),
    RELEASE_OWNERSHIP_REQUESTED(false),
    OWNERSHIP_RELEASED(false),
    OWNERSHIP_ACQUIRED(true),
    OWNERSHIP_OFFERED(true),
    ATTRIBUTE_OWNERSHIP_DENIED(false),
    CANCEL_ACQUIRE_OWNERSHIP_SUCCEEDED(false);

    boolean hasOwnership = false;

    private OwnershipStateEnum(boolean hasOwnership){
        this.hasOwnership = hasOwnership;
    }

    public  boolean isHasOwnership;
}
