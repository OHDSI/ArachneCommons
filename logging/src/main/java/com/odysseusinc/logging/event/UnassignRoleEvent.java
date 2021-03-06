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
 * Created: August 29, 2018
 *
 */

package com.odysseusinc.logging.event;

import com.odysseusinc.logging.LogLevel;

public class UnassignRoleEvent extends LoggingEvent {
    private long roleId;
    private long userId;

    public UnassignRoleEvent(Object source, LogLevel logLevel, long roleId, long userId) {
        super(source, logLevel);
        this.roleId = roleId;
        this.userId = userId;
    }

    public UnassignRoleEvent(Object source, long roleId, long userId) {
        this(source, LogLevel.INFO, roleId, userId);
    }

    public long getRoleId() {
        return roleId;
    }

    public long getUserId() {
        return userId;
    }
}
