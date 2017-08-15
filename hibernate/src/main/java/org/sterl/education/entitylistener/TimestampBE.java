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

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 * Make sure to implement the HasTimestamp interface, nothing else is required.
 */
@Embeddable @Getter @Setter
public class TimestampBE implements Serializable {
    /**
     * The millisecond-based instant, measured from 1970-01-01T00:00Z (UTC) when this entity was created.
     */
    @Column(name = "create_date", nullable = false, updatable = false)
    private Long createDate;
    /**
     * The millisecond-based instant, measured from 1970-01-01T00:00Z (UTC) when this entity was last updated.
     */
    @Column(name = "update_date", nullable = false, updatable = true)
    private Long updateDate;
}