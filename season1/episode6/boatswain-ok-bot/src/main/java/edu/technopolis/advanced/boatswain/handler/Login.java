package edu.technopolis.advanced.boatswain.handler;

import edu.technopolis.advanced.boatswain.incoming.request.Message;
import edu.technopolis.advanced.boatswain.ruTracker.RuTrackerBot;

public class Login extends Command {

    Login(String phrase) {
        super(phrase);
    }

    @Override
    public Message exec() {
        if(st.countTokens() != 2){
            return new Message("Ведеите и логин, и пароль", null);
        }
        String username = st.nextToken();
        String password = st.nextToken();
        try {
            RuTrackerBot.login(username, password);
            return new Message("Вы успешно вошли в систему", null);
        } catch (Exception e) {
            return new Message(e.getMessage(), null);
        }
    }
}
