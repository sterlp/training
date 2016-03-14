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
package org.sterl.gcm.smack;

import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.util.XmlStringBuilder;

import lombok.ToString;

@ToString(of = "json")
public class GcmPacketExtension extends DefaultPacketExtension {
    public static final String GCM_ELEMENT_NAME = "gcm";
    public static final String GCM_NAMESPACE = "google:mobile:data";
    private final String json;
    
    public GcmPacketExtension(String json) {
        super(GCM_ELEMENT_NAME, GCM_NAMESPACE);
        this.json = json;
    }

    public String getJson() {
        return json;
    }

    @Override
    public CharSequence toXML() {
        XmlStringBuilder buf = new XmlStringBuilder();
        final String elementName = getElementName();
        buf.halfOpenElement(elementName).xmlnsAttribute(getNamespace()).rightAngelBracket();
        buf.append(json);
        buf.closeElement(elementName);
        return buf;
    }
}