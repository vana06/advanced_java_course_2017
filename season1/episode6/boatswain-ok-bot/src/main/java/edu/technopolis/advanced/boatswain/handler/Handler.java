package edu.technopolis.advanced.boatswain.handler;

import edu.technopolis.advanced.boatswain.incoming.request.Message;

import java.util.*;

public class Handler {
    public static final Map<String, String> sortKeys = Collections.unmodifiableMap(new HashMap<String, String>(){
        {
            put("se", "Сортировка по количеству сидов");
            put("pe", "Сортировка по количеству пиров");
            put("dc", "Сортировка по количеству скачиваний");
            put("sz", "Сортировка по размеру раздачи");
            put("rd", "Сортировка по времени добавления");
        }
    });
    private static List<Command> commands = Collections.unmodifiableList(new ArrayList<Command>(){
        {
            add(new Login("/login"));
            add(new Search("/search"));
            add(new Next("/next"));
            add(new Prev("/prev"));
            add(new Help("/help"));
        }
    });

    public static Message messageHandle(String message){
        for (Command c: commands){
            if(c.matches(message)){
                return c.exec();
            }
        }
        return new Message("Неизвестная команда\nВведите /help для помощи", null);
    }
}
