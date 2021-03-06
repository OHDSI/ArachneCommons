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

public class AddRoleEvent extends LoggingEvent {
    private long id;
    private String name;

    public AddRoleEvent(Object source, LogLevel logLevel, long id, String name) {
        super(source, logLevel);
        this.id = id;
        this.name = name;
    }

    public AddRoleEvent(Object source, long id, String name) {
        this(source, LogLevel.INFO, id, name);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
