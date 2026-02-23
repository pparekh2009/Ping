package com.priyanshparekh.ping.websocketconfig;

import lombok.Data;
import tools.jackson.databind.JsonNode;

@Data
public class SocketEvent {

    private SocketEventType socketEventType;
    private JsonNode payload;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SocketEvent{");
        sb.append("socketEventType=").append(socketEventType);
        sb.append(", payload='").append(payload).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
