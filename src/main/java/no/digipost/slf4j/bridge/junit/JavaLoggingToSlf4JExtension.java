/*
 * Copyright (C) Posten Norge AS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.digipost.slf4j.bridge.junit;

import org.junit.jupiter.api.extension.Extension;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Logger;

public class JavaLoggingToSlf4JExtension implements Extension {

    static {
        if (!SLF4JBridgeHandler.isInstalled()) {
            SLF4JBridgeHandler.removeHandlersForRootLogger();
            SLF4JBridgeHandler.install();
            Logger.getLogger(JavaLoggingToSlf4JExtension.class.getName()).info("Java Logging successfully bridged to Slf4J");
        } else {
            Logger.getLogger(JavaLoggingToSlf4JExtension.class.getName()).fine("Java Logging was already bridged to Slf4J");
        }
    }

}