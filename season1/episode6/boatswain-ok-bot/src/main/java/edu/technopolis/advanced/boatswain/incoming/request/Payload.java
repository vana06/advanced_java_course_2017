package edu.technopolis.advanced.boatswain.incoming.request;

public class Payload {
    private String url;

    public Payload() {
    }

    public Payload(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "{" +
                "url='" + url + '\'' +
                '}';
    }
}
