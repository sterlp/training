package org.sterl.gcm.server.msg.smak;

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