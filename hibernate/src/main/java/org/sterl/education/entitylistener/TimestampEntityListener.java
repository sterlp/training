/*
 * Copyright 2015 sterlp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sterl.education.entitylistener;

import java.time.Clock;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Entity Listener registered by default to manage any HasTimestamp Entities.
 */
public class TimestampEntityListener {
    @PrePersist
    void prePersist(Object entity) {
        if (entity instanceof HasTimestamp) {
            TimestampBE timestamp = getTimestamp((HasTimestamp)entity);
            final long now = Clock.systemUTC().millis();
            timestamp.setCreateDate(now);
            timestamp.setUpdateDate(now);
        }
    }

    @PreUpdate
    void preUpdate(Object entity) {
        if (entity instanceof HasTimestamp) {
            TimestampBE timestamp = getTimestamp((HasTimestamp)entity);
            final long now = Clock.systemUTC().millis();
            if (timestamp.getCreateDate() == null) timestamp.setCreateDate(now);
            timestamp.setUpdateDate(now);
        }
    }
    
    private TimestampBE getTimestamp(HasTimestamp ht) {
        TimestampBE timestamp = ht.getTimestamp();
        if (timestamp == null) {
            timestamp = new TimestampBE();
            ht.setTimestamp(timestamp);
        }
        return timestamp;
    }
}
