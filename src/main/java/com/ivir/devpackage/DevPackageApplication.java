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

package com.ivir.devpackage;

import com.ivir.devpackage.controller.ControlWebClient;
import com.ivir.devpackage.gui.ControlGui;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class DevPackageApplication implements ApplicationRunner {

	public static void main(String[] args) {
		new SpringApplicationBuilder(DevPackageApplication.class).headless(false).run(args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (args.containsOption("displayUi")) {
			System.out.println("DisplayUi is present with value: " + args.getOptionValues("displayUi") +
					" Running DevPackage Headless...");
			ControlWebClient controlWebClient = new ControlWebClient();
			controlWebClient.setup();
		} else {
			System.out.println("No displayUi args detected. Running Displaying UI.");
			ControlGui controlGui = new ControlGui();
			controlGui.main();

		}
	}
}
