/**
 * Copyright 2016 Paul Sterl
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific 
 * language governing permissions and limitations under the License.
 */
package org.sterl.gcm.api;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The data object in the GCM message needs to be a "complex" JSON object as so we have to wrap
 * simple strings to messages ... .
 */
@Data @AllArgsConstructor
public class GcmStringMessage {
    private final String text;
}
