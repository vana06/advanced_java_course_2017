package edu.technopolis.advanced.boatswain.incoming.request;

public class Attachment {
    private String type = "IMAGE";
    private Payload payload;

    public Attachment() {
    }

    public Attachment(Payload payload) {
        this.payload = payload;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "type='" + type + '\'' +
                ", payload=" + payload +
                '}';
    }
}
