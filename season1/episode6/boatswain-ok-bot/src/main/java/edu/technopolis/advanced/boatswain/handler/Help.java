package edu.technopolis.advanced.boatswain.handler;

import edu.technopolis.advanced.boatswain.incoming.request.Message;
import edu.technopolis.advanced.boatswain.ruTracker.TorrentFiles;

import java.util.stream.Collectors;

public class Help extends Command {

    Help(String phrase) {
        super(phrase);
    }

    @Override
    public Message exec() {
        String help = "Бот \"RuTracker\" поможет вам легко найти и скачать необходимые торренты.\n";
        help += "Для того чтобы войти в систему под своим аккаунтом достаточно ввести команду \"/login username password\","
                + "где username это ваш логин, а password - пароль.\n";
        help += "Если вы не хотите использовать свой аккаунт то бот зайдет в систему через стандартного пользователя.\n";
        help += "Для поиска введите команду \"/search data\", где data это искомый торрент.\n";
        help += "Все результаты по умолчанию сортируются по количеству сидов и выводятся по " + TorrentFiles.itemOnPage + ".\n\n";

        help += "Результат поиска выдается в формате:\n" +
                "{НАЗВАНИЕ РАЗДАЧИ}\n" +
                "{ССЫЛКА НА РАЗДАЧУ}\n" +
                "S {КОЛ. СИДОВ} | L {КОЛ. ПИРОВ} | D {КОЛ. СКАЧИВАНИЙ} | Reg {ДАТА РЕЛИЗА} | Size {РАЗМЕР}\n" +
                "{ССЫЛКА НА СКАЧИВАНИЕ ТОРРЕНТ-ФАЙЛА} \n\n";

        help += "Для продвинутого поиска, с сортировкой по другим полям, после команды \"/search\" через пробел введите \"/s\".\n";
        help += "Далее через пробел укажите тип сортировки. Доступные методы сортировки:\n";
        help += Handler.sortKeys.entrySet()
                .stream()
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.joining("\n"));
        help += "\nПорядок сортировки указывается ключами + и - т.е. по возрастанию и по убыванию соответственно\n\n";

        help += "Пример:\n";
        help += "/search /s rd- skyrim\n";
        return new Message(help, null);
    }
}
