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

package com.ivir.devpackage.gui.action;

import com.ivir.devpackage.gui.DevPackageMainPage;
import com.ivir.devpackage.controller.ControlWebClient;
import com.ivir.devpackage.controller.query.QueryResponse;
import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Component;

import java.util.concurrent.ExecutionException;

public class GetEventListAction  extends Action {
    private ControlWebClient controlWebClient;
    private DevPackageMainPage gui;

    public GetEventListAction(ControlWebClient controlWebClient, DevPackageMainPage gui){
        this.controlWebClient = controlWebClient;
        this.gui = gui;
    }


    @Override
    public void perform(Component component) {
        try {
            QueryResponse response = controlWebClient.queryEventList().get();
            gui.setQueryText(response.toString());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}