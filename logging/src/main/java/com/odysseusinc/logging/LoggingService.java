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

import com.odysseusinc.logging.event.LoggingEvent;
import org.apache.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {
    private LoggingEventMessageFactory factory;
    private final Logger log = Logger.getLogger(LoggingService.class);

    public LoggingService(LoggingEventMessageFactory factory) {
        this.factory = factory;
    }

    @EventListener
    public void logEvent(LoggingEvent event) {
        String message = factory.getMessage(event);
        LogLevel level = event.getLogLevel();
        switch (level) {
            case DEBUG: {
                log.debug(message);
                break;
            }
            case INFO: {
                log.info(message);
                break;
            }
            case WARN: {
                log.warn(message);
                break;
            }
            case ERROR: {
                log.error(message);
                break;
            }
        }
    }
}
