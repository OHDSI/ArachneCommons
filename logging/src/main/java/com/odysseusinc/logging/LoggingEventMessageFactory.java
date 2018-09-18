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
            message = "Data source id = " + ((AddDataSourceEvent) event).getId() + ", name = " + ((AddDataSourceEvent) event).getName() + " was created";
        } else if (event instanceof AddPermissionEvent) {
            message = "Permission id = " + ((AddPermissionEvent) event).getPermissionId() + " was added to role id = " + ((AddPermissionEvent) event).getRoleId();
        } else if (event instanceof AddRoleEvent) {
            message = "Role id = " + ((AddRoleEvent) event).getId() + ", name = " + ((AddRoleEvent) event).getName() + " was created";
        } else if (event instanceof AddUserEvent) {
            message = "User id = " + ((AddUserEvent) event).getId() + ", login = " + ((AddUserEvent) event).getLogin() + " was created";
        } else if (event instanceof AssignRoleEvent) {
            message = "Role id = " + ((AssignRoleEvent) event).getUserId() + " was assigned to user id = " + ((AssignRoleEvent) event).getUserId();
        } else if (event instanceof ChangeDataSourceEvent) {
            message = "Data source id = " + ((ChangeDataSourceEvent) event).getId() + ", name = " + ((ChangeDataSourceEvent) event).getName() + " was changed";
        } else if (event instanceof ChangeRoleEvent) {
            message = "Role id = " + ((ChangeRoleEvent) event).getId() + ", name = " + ((ChangeRoleEvent) event).getName() + " was changed";
        } else if (event instanceof DeleteDataSourceEvent) {
            message = "Data source id = " + ((DeleteDataSourceEvent) event).getId() + ", name = " + ((DeleteDataSourceEvent) event).getName() + " was deleted";
        } else if (event instanceof DeletePermissionEvent) {
            message = "Permission id = " + ((DeletePermissionEvent) event).getPermissionId() + " was removed from role id = " + ((DeletePermissionEvent) event).getRoleId();
        } else if (event instanceof DeleteRoleEvent) {
            message = "Role id = " + ((DeleteRoleEvent) event).getId() + " was deleted";
        } else if (event instanceof DeleteUserEvent) {
            message = "User id = " + ((DeleteUserEvent) event).getId() + ", login = " + ((DeleteUserEvent) event).getLogin() + " was deleted";
        } else if (event instanceof FailedDbConnectEvent) {
            message = ((FailedDbConnectEvent) event).getException();
        } else if (event instanceof FailedLogoffEvent) {
            message = "Logoff failed for user login = " + ((FailedLogoffEvent) event).getLogin();
        } else if (event instanceof FailedLogonEvent) {
            message = "Logon failed for user login = " + ((FailedLogonEvent) event).getLogin();
        } else if (event instanceof LockoutStartEvent) {
            message = "Lockout started for user login = " + ((LockoutStartEvent) event).getLogin();
        } else if (event instanceof LockoutStopEvent) {
            message = "Lockout stopped for user login = " + ((LockoutStopEvent) event).getLogin();
        } else if (event instanceof SuccessLogoffEvent) {
            message = "User login = " + ((SuccessLogoffEvent) event).getLogin() + " was logged off";
        } else if (event instanceof SuccessLogonEvent) {
            message = "User login = " + ((SuccessLogonEvent) event).getLogin() + " was logged in";
        } else if (event instanceof UnassignRoleEvent) {
            message = "Role id = " + ((UnassignRoleEvent) event).getUserId() + " was unassigned from user id = " + ((UnassignRoleEvent) event).getUserId();
        }
        return message;
    }

    public List<Class> getSupportedEventTypes() {
        return supportedEvents;
    }
}
