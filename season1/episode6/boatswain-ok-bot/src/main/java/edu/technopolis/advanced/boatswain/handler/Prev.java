package edu.technopolis.advanced.boatswain.handler;

import edu.technopolis.advanced.boatswain.incoming.request.Message;
import edu.technopolis.advanced.boatswain.ruTracker.RuTrackerBot;

public class Prev extends Command {

    Prev(String phrase) {
        super(phrase);
    }

    @Override
    public Message exec() {
        if (!RuTrackerBot.hasFiles()) {
            return new Message("Сначала введите фразу для поиска", null);
        }
        if (!RuTrackerBot.hasPrevious()) {
            return new Message("Конец поиска", null);
        }
        RuTrackerBot.prepareForPrev();
        return new Next("").exec();
    }
}
