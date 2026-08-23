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

package com.ivir.devpackage.gui;

import com.ivir.devpackage.controller.ControlWebClient;
import com.ivir.devpackage.gui.builder.BoxPaneBuilder;
import com.ivir.devpackage.gui.builder.PushButtonBuilder;
import com.ivir.devpackage.gui.builder.TextInputBuilder;
import com.ivir.devpackage.controller.query.QueryVariable;
import com.ivir.devpackage.controller.query.types.EventInput;
import devstudio.generatedcode.datatypes.EventTypeEnum;
import org.apache.pivot.collections.List;
import org.apache.pivot.wtk.*;

import java.util.concurrent.ExecutionException;

public class CreateEventDialog extends Dialog {
    private ControlWebClient controlWebClient;
    private DevPackageMainPage gui;

    TextInput patientId;
    TextInput instructorId;
    TextInput learnerId;
    TextInput teamId;
    TextInput trainingFacilityId;
    TextInput source;
    ListButton listButton;
    TextInput time;
    TextInput simTime;
    TextInput description;
    TextInput notes;

    public CreateEventDialog(ControlWebClient controlWebClient, DevPackageMainPage gui){
        this.controlWebClient = controlWebClient;
        this.gui = gui;
        this.setModal(true);
        this.setTitle("Create Event");

        Form form = new Form();
        Form.Section section = new Form.Section();
        patientId = TextInputBuilder.create().prompt("Patient ID").build();
        section.add(patientId);
        instructorId = TextInputBuilder.create().prompt("Instructor ID").build();
        section.add(instructorId);
        learnerId = TextInputBuilder.create().prompt("Learner ID").build();
        section.add(learnerId);
        teamId = TextInputBuilder.create().prompt("Team ID").build();
        section.add(teamId);
        source = TextInputBuilder.create().prompt("Source").build();
        section.add(source);
        trainingFacilityId = TextInputBuilder.create().prompt("Training Facility ID").build();
        section.add(trainingFacilityId);

        listButton = new ListButton();
        listButton.setListData(buildEnumList());
        section.add(listButton);

        time = TextInputBuilder.create().prompt("Time").build();
        section.add(time);
        simTime = TextInputBuilder.create().prompt("Sim Time").build();
        section.add(simTime);
        description = TextInputBuilder.create().prompt("Description").build();
        section.add(description);
        notes = TextInputBuilder.create().prompt("Notes").build();
        section.add(notes);

        form.getSections().add(section);

        this.setContent(BoxPaneBuilder.create()
                .orientation(Orientation.VERTICAL)
                .horizontalAlignment(HorizontalAlignment.RIGHT)
                .add(form)
                .add(BoxPaneBuilder.create()
                        .add(PushButtonBuilder.create()
                                .text("Send")
                                .onPressed((button)->{
                                    try {
                                        gui.setQueryText(controlWebClient.mutateCreateEvent(buildEventInput()).get().toString());
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    } catch (ExecutionException e) {
                                        throw new RuntimeException(e);
                                    }
                                    this.close(true);
                                })
                                .build())
                        .add(PushButtonBuilder.create()
                                .text("Cancel")
                                .onPressed((button)->this.close(true))
                                .build())
                        .build())
                .build());
    }

    private List<String> buildEnumList(){
        List<String> list = new org.apache.pivot.collections.ArrayList<>();
        for(EventTypeEnum eventTypeEnum : EventTypeEnum.values()){
            list.add(eventTypeEnum.toString());
        }
        return list;
    }

    private EventInput buildEventInput(){
        EventInput eventInput = new EventInput();
        if(notNullOrEmpty(time.getText())){
            eventInput.setTime(Long.parseLong(time.getText()));
        }
        if(notNullOrEmpty(simTime.getText())){
            eventInput.setSimTime(Long.parseLong(simTime.getText()));
        }
        if(listButton.getSelectedItem() != null){
            eventInput.setType(EventTypeEnum.valueOf(listButton.getSelectedItem().toString()));
        }
        if(notNullOrEmpty(patientId.getText())){
            eventInput.setPatientId(patientId.getText());
        }
        if(notNullOrEmpty(learnerId.getText())){
            eventInput.setLearnerId(learnerId.getText());
        }
        if(notNullOrEmpty(instructorId.getText())){
            eventInput.setInstructorId(instructorId.getText());
        }
        if(notNullOrEmpty(teamId.getText())){
            eventInput.setTeamId(teamId.getText());
        }
        if(notNullOrEmpty(trainingFacilityId.getText())){
            eventInput.setTrainingFacilityId(trainingFacilityId.getText());
        }
        if(notNullOrEmpty(notes.getText())){
            eventInput.setNotes(notes.getText());
        }
        if(notNullOrEmpty(description.getText())){
            eventInput.setDescription(description.getText());
        }
        return eventInput;
    }

    private java.util.List<QueryVariable> buildVariableList(){
        java.util.ArrayList<QueryVariable> varList = new java.util.ArrayList<>();
        if(notNullOrEmpty(time.getText())){
            varList.add(new QueryVariable("time","Long",Long.parseLong(time.getText())));
        }
        if(notNullOrEmpty(simTime.getText())){
            varList.add(new QueryVariable("simTime","Long",Long.parseLong(simTime.getText())));
        }
        if(listButton.getSelectedItem() != null){
            varList.add(new QueryVariable("type","EventTypeEnum",listButton.getSelectedItem().toString()));
        }
        if(notNullOrEmpty(patientId.getText())){
            varList.add(new QueryVariable("patientId","String",patientId.getText()));
        }
        if(notNullOrEmpty(learnerId.getText())){
            varList.add(new QueryVariable("learnerId","String",learnerId.getText()));
        }
        if(notNullOrEmpty(instructorId.getText())){
            varList.add(new QueryVariable("instructorId","String",instructorId.getText()));
        }
        if(notNullOrEmpty(teamId.getText())){
            varList.add(new QueryVariable("teamId","String",teamId.getText()));
        }
        if(notNullOrEmpty(trainingFacilityId.getText())){
            varList.add(new QueryVariable("trainingFacilityId","String",trainingFacilityId.getText()));
        }
        if(notNullOrEmpty(notes.getText())){
            varList.add(new QueryVariable("notes","String",notes.getText()));
        }
        if(notNullOrEmpty(description.getText())){
            varList.add(new QueryVariable("description","String",description.getText()));
        }

        return varList;
    }

    private boolean notNullOrEmpty(String textStr){
        if(textStr == null){
            return false;
        }
        if(textStr.isEmpty()){
            return false;
        }
        return true;
    }
}
