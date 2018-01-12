package edu.technopolis.advanced.boatswain.handler;

import edu.technopolis.advanced.boatswain.incoming.request.Message;
import edu.technopolis.advanced.boatswain.ruTracker.RuTrackerBot;

import java.io.IOException;
import java.text.ParseException;

public class Search extends Command {

    Search(String phrase) {
        super(phrase);
    }

    @Override
    public Message exec() {
        if(st.countTokens() == 0){
            return new Message("Ведите фразу для поиска", null);
        }
        StringBuilder searchPhrase = new StringBuilder();
        //есть ли во фразе уловия сортиовки
        String sortingKey = RuTrackerBot.defaultSortKey;
        String str = st.nextToken();
        if(str.startsWith("/s")){
            if(st.countTokens() == 0 || st.countTokens() == 1){
                return new Message("Недостаточно аргументов", null);
            }
            sortingKey = st.nextToken();
        } else {
            searchPhrase.append(str).append(" ");
        }
        while (st.hasMoreTokens()){
            searchPhrase.append(st.nextToken()).append(" ");
        }
        //поиск
        try {
            RuTrackerBot.search(searchPhrase.toString(), sortingKey);
            String addition = "По запросу \"" + searchPhrase + "\" найдено "
                    + RuTrackerBot.filesCount() + " результатов\n"
                    + Handler.sortKeys.get(String.valueOf(sortingKey.charAt(0)) + String.valueOf(sortingKey.charAt(1))) + "\n\n";
            Message message =  new Next("").exec();
            message.setText(addition + message.getText());
            return message;
        } catch (IOException | ParseException e) {
            return new Message(e.getMessage(), null);
        }
    }
}