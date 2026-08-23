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

package com.ivir.devpackage.gui.wrapper;

import org.apache.pivot.collections.List;
import org.apache.pivot.wtk.ListButton;

import java.net.URL;

public class ListButtonExt extends ButtonExt<ListButton>{
    private final ListButton listButton;

    public ListButtonExt(){
       listButton = new ListButton();
    }

    public ListButtonExt(Object buttonData){
        listButton = new ListButton(buttonData);
    }

    public ListButtonExt(List<?> listData){
        listButton = new ListButton(listData);
    }

    public ListButtonExt(Object buttonData, List<?> listData){
        listButton = new ListButton(buttonData, listData);
    }

    @Override
    protected ListButton getButton() {
        return listButton;
    }

    public void setListData(List<?> listData){
        this.listButton.setListData(listData);
    }

    public void setListData(String jsonString){
        this.listButton.setListData(jsonString);
    }

    public void setListData(URL urlToJson){
        this.listButton.setListData(urlToJson);
    }

    public void setSelectedIndex(int index){
        this.listButton.setSelectedIndex(index);
    }

    public int getSelectedIndex(){
        return this.listButton.getSelectedIndex();
    }

    public int getListSize(){
        return this.listButton.getListSize();
    }

    public void setListSize(int size){
        this.listButton.setListSize(size);
    }

    public Object getSelectedItem(){
        return this.listButton.getSelectedItem();
    }

}
