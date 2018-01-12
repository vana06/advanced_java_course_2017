package edu.technopolis.advanced.boatswain.handler;

import edu.technopolis.advanced.boatswain.incoming.request.Attachment;
import edu.technopolis.advanced.boatswain.incoming.request.Message;
import edu.technopolis.advanced.boatswain.incoming.request.Payload;
import edu.technopolis.advanced.boatswain.ruTracker.RuTrackerBot;
import edu.technopolis.advanced.boatswain.ruTracker.TorrentFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Next extends Command {
    private static final Logger log = LoggerFactory.getLogger(Next.class);
    private String addition;

    Next(String phrase) {
        super(phrase);
    }

    @Override
    public Message exec() {
        if (!RuTrackerBot.hasFiles()) {
            return new Message("Сначала введите фразу для поиска", null);
        }
        if(!RuTrackerBot.hasNext()){
            return new Message("Конец поиска",null);
        }
        TorrentFile file = RuTrackerBot.getNext();
        try {
            String img = RuTrackerBot.getImg(file.getId());
            Attachment attachment = null;
            if (img != null) {
                attachment = new Attachment(new Payload(img));
            }
            return new Message(addition + file.toString(), attachment);
        } catch (IOException e) {
            log.info(e.getMessage());
            return new Message(addition + file.toString(), null);
        }
    }
}
