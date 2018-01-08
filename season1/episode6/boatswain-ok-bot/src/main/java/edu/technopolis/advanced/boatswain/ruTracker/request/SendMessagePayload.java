package edu.technopolis.advanced.boatswain.ruTracker.request;

import org.apache.http.NameValuePair;
import java.util.ArrayList;

public class SendMessagePayload implements RequestPayload {
    private final ArrayList<NameValuePair> params;

    public SendMessagePayload(ArrayList<NameValuePair> params) {
        this.params = params;
    }

    public String getMessage() {
        if(params != null) {
            StringBuilder str = new StringBuilder();
            for (NameValuePair pair : params) {
                str.append(pair.getName()).append("=").append(pair.getValue()).append("&");
            }
            str.deleteCharAt(str.length() - 1);
            return str.toString();
        }
        return "";
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
