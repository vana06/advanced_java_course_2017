package edu.technopolis.advanced.boatswain.ruTracker.request;

public class RuTrackerRequest extends Request<SendMessagePayload, RuTrackerRequest> {
    private final String query;


    public RuTrackerRequest(String query) {
        this.query = query;
    }

    @Override
    protected RuTrackerRequest thiss() {
        return this;
    }

    @Override
    public String getQueryStart() {
        return query;
    }

    @Override
    public String toString() {
        return "RuTrackerRequest{" +
                "queryStart='" + getQueryStart() + '"' +
                '}';
    }
}
