package edu.technopolis.advanced.boatswain.handler;

import edu.technopolis.advanced.boatswain.incoming.request.Message;

import java.util.StringTokenizer;

public abstract class Command {
    private final String phrase;
    StringTokenizer st;

    public Command(String phrase) {
        this.phrase = phrase;
    }

    public boolean matches(String message){
        st = new StringTokenizer(message);
        String start = st.nextToken();
        return start.equals(phrase);
    }

    public abstract Message exec();
}
