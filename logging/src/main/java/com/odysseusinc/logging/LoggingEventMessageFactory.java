/*
 *
 * Copyright 2018 Odysseus Data Services, inc.
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
 *
 * Company: Odysseus Data Services, Inc.
 * Product Owner/Architecture: Gregory Klebanov
 * Authors: Anastasiia Klochkova
 * Created: August 28, 2018
 *
 */

package com.odysseusinc.logging;

import com.odysseusinc.logging.event.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class LoggingEventMessageFactory {

    private final static List<Class> supportedEvents = Arrays.asList(
            AddDataSourceEvent.class,
            AddPermissionEvent.class,
            AddRoleEvent.class,
            AddUserEvent.class,
            AssignRoleEvent.class,
            ChangeDataSourceEvent.class,
            DeleteDataSourceEvent.class,
            DeletePermissionEvent.class,
            DeleteUserEvent.class,
            FailedLogoffEvent.class,
            FailedLogonEvent.class,
            LockoutStartEvent.class,
            LockoutStopEvent.class,
            SuccessLogoffEvent.class,
            SuccessLogonEvent.class,
            UnassignRoleEvent.class);

    public String getMessage(LoggingEvent event) {

        String message = "";
        if (event instanceof AddDataSourceEvent) {
            message = "AddDataSourceEvent is received";
        } else if (event instanceof AddPermissionEvent) {
            message = "AddPermissionEvent is received";
        } else if (event instanceof AddRoleEvent) {
            message = "AddRoleEvent is received";
        } else if (event instanceof AddUserEvent) {
            message = "AddUserEvent is received";
        } else if (event instanceof AssignRoleEvent) {
            message = "AssignRoleEvent is received";
        } else if (event instanceof ChangeDataSourceEvent) {
            message = "ChangeDataSourceEvent is received";
        } else if (event instanceof DeleteDataSourceEvent) {
            message = "DeleteDataSourceEvent is received";
        } else if (event instanceof DeletePermissionEvent) {
            message = "DeletePermissionEvent is received";
        } else if (event instanceof DeleteUserEvent) {
            message = "DeleteUserEvent is received";
        } else if (event instanceof FailedLogoffEvent) {
            message = "FailedLogoffEvent is received";
        } else if (event instanceof FailedLogonEvent) {
            message = "FailedLogonEvent is received";
        } else if (event instanceof LockoutStartEvent) {
            message = "LockoutStartEvent event is received";
        } else if (event instanceof LockoutStopEvent) {
            message = "LockoutStopEvent is received";
        } else if (event instanceof SuccessLogoffEvent) {
            message = "SuccessLogoffEvent is received";
        } else if (event instanceof SuccessLogonEvent) {
            message = "SuccessLogonEvent is received";
        } else if (event instanceof UnassignRoleEvent) {
            message = "UnassignRoleEvent is received";
        }
        return message;
    }

    public List<Class> getSupportedEventTypes() {
        return supportedEvents;
    }
}
