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

package com.ivir.devpackage.gui.builder;

import org.apache.pivot.wtk.*;

import java.util.function.Consumer;

public class MenuBarBuilder {
    private MenuBar menuBar;

    private MenuBarBuilder(){
        menuBar = new MenuBar();
    }

    public static MenuBarBuilder create(){
        return new MenuBarBuilder();
    }

    public MenuBar build(){
        return this.menuBar;
    }

    public MenuBarItemBuilder menuBarItem(String text){
        return new MenuBarItemBuilder(this, this.menuBar, text);
    }

    public class MenuBarItemBuilder{
        private MenuBar.Item _menuBarItem;
        private MenuBarBuilder _menuBarBuilder;
        private MenuBar _menuBar;
        private Menu _menu;

        public MenuBarItemBuilder(MenuBarBuilder builder, MenuBar menuBar, String text){
            _menuBarItem = new MenuBar.Item();
            _menuBarBuilder = builder;
            _menuBar = menuBar;
            _menuBarItem.setButtonData(text);
            _menu = new Menu();
            _menuBarItem.setMenu(_menu);
        }

        public MenuBarBuilder buildMenuBarItem(){
            _menuBar.getItems().add(_menuBarItem);
            return _menuBarBuilder;
        }

        public MenuSectionBuilder section(){
            return new MenuSectionBuilder(this, _menu);
        }

    }

    public class MenuSectionBuilder{
        Menu.Section _section;
        MenuBarItemBuilder _menuBarItemBuilder;
        Menu _menu;

        public MenuSectionBuilder(MenuBarItemBuilder menuBarItemBuilder, Menu menu){
            _menu = menu;
            _menuBarItemBuilder = menuBarItemBuilder;
            _section = new Menu.Section();
        }

        public MenuBarItemBuilder buildMenuSection(){
            _menu.getSections().add(_section);
            return _menuBarItemBuilder;
        }

        public MenuItemBuilder menu(String text){
            return new MenuItemBuilder(this, _section, text);
        }

    }

    public class MenuItemBuilder{
        MenuSectionBuilder _menuSectionBuilder;
        Menu.Section _section;
        Menu.Item _menuItem;

        public MenuItemBuilder(MenuSectionBuilder sectionBuilder, Menu.Section section, String text){
            _section = section;
            _menuSectionBuilder = sectionBuilder;
            _menuItem = new Menu.Item();
            _menuItem.setButtonData(text);
        }

        public MenuSectionBuilder buildMenuItem(){
            _section.add(_menuItem);
            return _menuSectionBuilder;
        }

        public MenuItemBuilder action(Action action){
            _menuItem.setAction(action);
            return this;
        }

        public MenuItemBuilder onClicked(Consumer<Mouse.Button> clickedConsumer) {
            _menuItem.getComponentMouseButtonListeners().add(new ComponentMouseButtonListener() {
                @Override
                public boolean mouseDown(Component component, Mouse.Button button, int i, int i1) {
                    return false;
                }

                @Override
                public boolean mouseUp(Component component, Mouse.Button button, int i, int i1) {
                    return false;
                }

                @Override
                public boolean mouseClick(Component component, Mouse.Button button, int i, int i1, int i2) {
                    clickedConsumer.accept(button);
                    return true;
                }
            });
            return this;
        }

    }

}
