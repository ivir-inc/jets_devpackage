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

package com.ivir.devpackage.tester;

import java.util.Scanner;

public class Tester {
    private TesterFederate testerFederate;



    public void runCommandMenu(){
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Welcome to the tester");

        CommandMenu menu = menuStart();
        boolean running = true;
        while(running){
            menu = menu.runMenu(inputScanner);
            if(menu == null){
                running = false;
            }
        }
        System.out.println("Done");
    }

    private CommandMenu menuStart(){
        return new CommandMenu() {
            @Override
            public CommandMenu runMenu(Scanner inputScanner) {
                System.out.println("1) connect");
                System.out.println("2) quit");
                String input = inputScanner.nextLine();
                if(input.equals("1")){
                    return connectActionAndMenu();
                }
                return null;
            }
        };
    }

    private CommandMenu connectActionAndMenu(){
        return new CommandMenu() {
            @Override
            public CommandMenu runMenu(Scanner inputScanner) {
                testerFederate = new TesterFederate();
                testerFederate.connect("TesterFederate");
                System.out.println("Creating Events...");
                testerFederate.createEvents(5);
                System.out.println("Creating Physiology...");
                testerFederate.createPhysiology(5);
                return federateMenu();
            }
        };
    }

    private CommandMenu federateMenu(){
        return new CommandMenu() {
            @Override
            public CommandMenu runMenu(Scanner inputScanner) {
                System.out.println("Connection Status: " + testerFederate.isConnected());
                System.out.println("1) Check status");
                System.out.println("2) Get Events");
                System.out.println("3) Get Physiology");
                System.out.println("4) Disconnect");
                String input = inputScanner.nextLine();

                if(input.equals("1")){
                    return federateMenu();
                }else if(input.equals("2")){
                    testerFederate.getEvents();
                    return federateMenu();
                }else if(input.equals("3")){
                    testerFederate.getPhysiology();
                    return federateMenu();
                }else if(input.equals("4")){
                    testerFederate.disconnect();
                    return menuStart();
                }
                return federateMenu();
            }
        };
    }

    public static void main(String ...args){
        new Tester().runCommandMenu();
    }

    public interface CommandMenu{
        public CommandMenu runMenu(Scanner inputScanner);
    }

}
