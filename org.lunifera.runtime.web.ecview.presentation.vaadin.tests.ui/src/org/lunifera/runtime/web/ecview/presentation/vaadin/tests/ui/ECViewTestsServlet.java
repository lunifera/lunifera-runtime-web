 /**
 * Copyright 2013 Lunifera GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.ui;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

/**
 * An implementation of VaadinServlet that uses SimpleUI as its base UI.
 */
@SuppressWarnings("serial")
@VaadinServletConfiguration(ui = ECViewTestsUI.class, productionMode = false)
public class ECViewTestsServlet extends VaadinServlet {

}
