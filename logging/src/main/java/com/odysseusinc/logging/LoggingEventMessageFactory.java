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

import java.util.Arrays;
import java.util.List;

public class LoggingEventMessageFactory {

    private final static List<Class> supportedEvents = Arrays.asList(
            AddDataSourceEvent.class,
            AddPermissionEvent.class,
            AddRoleEvent.class,
            AddUserEvent.class,
            AssignRoleEvent.class,
            ChangeDataSourceEvent.class,
            ChangeRoleEvent.class,
            DeleteDataSourceEvent.class,
            DeletePermissionEvent.class,
            DeleteRoleEvent.class,
            DeleteUserEvent.class,
            FailedDbConnectEvent.class,
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
            message = String.format("Data source id = %d, name = %s was created", ((AddDataSourceEvent) event).getId(), ((AddDataSourceEvent) event).getName());
        } else if (event instanceof AddPermissionEvent) {
            message = String.format("Permission id = %d was added to role id = %d", ((AddPermissionEvent) event).getPermissionId(), ((AddPermissionEvent) event).getRoleId());
        } else if (event instanceof AddRoleEvent) {
            message = String.format("Role id = %d, name = %s was created", ((AddRoleEvent) event).getId(), ((AddRoleEvent) event).getName());
        } else if (event instanceof AddUserEvent) {
            message = String.format("User id = %d, login = %s was created", ((AddUserEvent) event).getId(), ((AddUserEvent) event).getLogin());
        } else if (event instanceof AssignRoleEvent) {
            message = String.format("Role id = %d was assigned to user id = %d", ((AssignRoleEvent) event).getUserId(), ((AssignRoleEvent) event).getUserId());
        } else if (event instanceof ChangeDataSourceEvent) {
            message = String.format("Data source id = %d, name = %s was changed", ((ChangeDataSourceEvent) event).getId(), ((ChangeDataSourceEvent) event).getName());
        } else if (event instanceof ChangeRoleEvent) {
            message = String.format("Role id = %d, name = %s was changed", ((ChangeRoleEvent) event).getId(), ((ChangeRoleEvent) event).getName());
        } else if (event instanceof DeleteDataSourceEvent) {
            message = String.format("Data source id = %d, name = %s was deleted", ((DeleteDataSourceEvent) event).getId(), ((DeleteDataSourceEvent) event).getName());
        } else if (event instanceof DeletePermissionEvent) {
            message = String.format("Permission id = %d was removed from role id = %d", ((DeletePermissionEvent) event).getPermissionId(), ((DeletePermissionEvent) event).getRoleId());
        } else if (event instanceof DeleteRoleEvent) {
            message = String.format("Role id = %d was deleted", ((DeleteRoleEvent) event).getId());
        } else if (event instanceof DeleteUserEvent) {
            message = String.format("User id = %d, login = %s was deleted", ((DeleteUserEvent) event).getId(), ((DeleteUserEvent) event).getLogin());
        } else if (event instanceof FailedDbConnectEvent) {
            message = ((FailedDbConnectEvent) event).getException();
        } else if (event instanceof FailedLogoffEvent) {
            message = String.format("Logoff failed for user login = %s", ((FailedLogoffEvent) event).getLogin());
        } else if (event instanceof FailedLogonEvent) {
            message = String.format("Logon failed for user login = %s", ((FailedLogonEvent) event).getLogin());
        } else if (event instanceof LockoutStartEvent) {
            message = String.format("Lockout started for user login = %s", ((LockoutStartEvent) event).getLogin());
        } else if (event instanceof LockoutStopEvent) {
            message = String.format("Lockout stopped for user login = %s", ((LockoutStopEvent) event).getLogin());
        } else if (event instanceof SuccessLogoffEvent) {
            message = String.format("User login = %s was logged off", ((SuccessLogoffEvent) event).getLogin());
        } else if (event instanceof SuccessLogonEvent) {
            message = String.format("User login = %s was logged in", ((SuccessLogonEvent) event).getLogin());
        } else if (event instanceof UnassignRoleEvent) {
            message = String.format("Role id = %d was unassigned from user id = %d", ((UnassignRoleEvent) event).getUserId(), ((UnassignRoleEvent) event).getUserId());
        }
        return message;
    }

    public List<Class> getSupportedEventTypes() {
        return supportedEvents;
    }
}
