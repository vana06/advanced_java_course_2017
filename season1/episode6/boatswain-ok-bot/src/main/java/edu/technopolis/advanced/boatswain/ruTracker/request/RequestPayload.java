package edu.technopolis.advanced.boatswain.ruTracker.request;

import java.io.Serializable;

public interface RequestPayload extends Serializable {

    String toString();


    class EmptyPayload implements RequestPayload {
        private EmptyPayload() {

        }

    }
}
