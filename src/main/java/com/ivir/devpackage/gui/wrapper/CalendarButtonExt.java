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

import org.apache.pivot.wtk.CalendarButton;

public class CalendarButtonExt extends ButtonExt<CalendarButton> {
    /**
     * A component that allows a user to select a calendar date. The calendar is hidden until the user pushes the button.
     */
    private final CalendarButton calendarButton;

    public CalendarButtonExt(){
        this.calendarButton = new CalendarButton();
    }

    public CalendarButtonExt(int month, int year){
        this.calendarButton = new CalendarButton(year, month);
    }

    @Override
    protected CalendarButton getButton() {
        return calendarButton;
    }

    public int getMonth(){
        return this.calendarButton.getMonth();
    }

    public void setMonth(int month){
        this.calendarButton.setMonth(month);
    }

    public int getYear(){
        return this.calendarButton.getYear();
    }

    public void setYear(int year){
        this.calendarButton.setY(year);
    }
}
