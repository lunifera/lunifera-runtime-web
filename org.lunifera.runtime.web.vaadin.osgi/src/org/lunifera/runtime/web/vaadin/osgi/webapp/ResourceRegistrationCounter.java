/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Benno Luthiger *
 */
package org.lunifera.runtime.web.vaadin.osgi.webapp;

/** Singleton to count the number of registered servlet resources */
public enum ResourceRegistrationCounter {
    INSTANCE;

    private int counter;

    public void increment() {
        counter++;
    }

    public void decrement() {
        counter--;
    }

    /** @return boolean <code>true</code> if at least one resource has been registered */
    public boolean hasRegistrations() {
        return counter > 0;
    }

}
