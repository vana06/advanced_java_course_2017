package edu.technopolis.advanced.boatswain.ruTracker;

import edu.technopolis.advanced.boatswain.ruTracker.request.RuTrackerRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class RuTrackerBot {
    private static final Logger log = LoggerFactory.getLogger(RuTrackerBot.class);

    private static final String schema = "http";
    private static final String host = "rutracker.org";
    private static final String loginPath = "/forum/login.php";
    private static final String searchPath = "/forum/tracker.php";
    private static final String downloadPath = "/forum/dl.php?t=";
    private static final String viewPath = "/forum/viewtopic.php?t=";

    public static final String defaultSortKey = "se-";

    private static boolean isLogged = false;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("d-MMM-yyyy");

    private static RuTrackerApiClient ruTracker;
    private static TorrentFiles files;

    static {
        ruTracker = createClient();
        TorrentFile.downloadLink = schema + "://" + host + downloadPath;
        TorrentFile.viewLink = schema + "://" + host + viewPath;
    }

    public static void main(String[] args) throws IOException{
        log.info("Log in...");
        if(ruTracker.login("okBotUser", "password")){
            log.info("Logged in");
            //search
            log.info("Searching...");
            String[] pages = ruTracker.search("south park");

            //parse
            files = new TorrentFiles();

            for (String html: pages) {

                Document doc = Jsoup.parse(html);
                Elements searchResults = doc.getElementsByClass("tCenter hl-tr");

                if (searchResults.size() == 0) {
                    throw new IOException("Ничего не найдено");
                }

                for (Element result : searchResults) {
                    TorrentFile file = parseHTML(result);
                    if (file != null) {
                        files.add(file);
                    }
                }
            }
            log.info("Количество элемнтов " + files.size());
            files.sortBySeeds(defaultSortKey.charAt(defaultSortKey.length() - 1));

        }

    }

    public static void login(String username, String password) throws IOException {
        log.info("Log in...");
        if(ruTracker.login(username, password)) {
            log.info("Logged in");
            isLogged = true;
            return;
        }
        throw new IOException("Неверный логин или пароль");
    }
    public static void search(String searchPhrase, String sortingKey) throws IOException, ParseException{
        //если не залогинился, то использую дефолтный аккаунт
        if(!isLogged){
            login("okBotUser", "password");
        }
        log.info("Searching...");
        String[] pages = ruTracker.search(searchPhrase);

        //parse
        files = new TorrentFiles();

        for (String html: pages) {

            Document doc = Jsoup.parse(html);
            Elements searchResults = doc.getElementsByClass("tCenter hl-tr");

            if (searchResults.size() == 0) {
                throw new IOException("Ничего не найдено");
            }

            for (Element result : searchResults) {
                TorrentFile file = parseHTML(result);
                if (file != null) {
                    files.add(file);
                }
            }
        }

        sort(sortingKey);
    }

    private static RuTrackerApiClient createClient() {
        return new RuTrackerApiClient(schema, host, loginPath, searchPath, viewPath);
    }
    public static void closeClient(){
        if (ruTracker != null) {
            try {
                ruTracker.close();
            } catch (IOException ce) {
                log.error("Failed to close client", ce);
            }
        }
    }

    private static TorrentFile parseHTML(Element result) {
        //проверен ли файл
        String isVerified = result.getElementsByClass("row1 t-ico").get(1).attr("title");
        if (!isVerified.equals("проверено") && !isVerified.equals("не проверено") && !isVerified.equals("временная")) {
            return null;
        }
        //id
        String id = result.select( "div.t-title > a.tLink").first().attr("data-topic_id");
        //категория
        String category = parseElement(result, "div.f-name > a.gen");
        //имя
        String name = parseElement(result, "div.t-title > a.tLink");
        //размер
        StringBuilder size = new StringBuilder(Objects.requireNonNull(parseElement(result, "td.tor-size > a.small")));
        size.deleteCharAt(size.length()-1);
        //сиды
        String seeds = parseElement(result, "b.seedmed");
        //личи
        String peers = parseElement(result, "td.leechmed > b");
        //количество скачиваний
        String downloadedTimes = parseElement(result, "td.number-format");
        //время добавления
        String time = parseElement(result, "td.number-format + td > p");
        Date date;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            return null;
        }
        return new TorrentFile(category, name, size.toString(), seeds, peers, downloadedTimes, date, id);
    }
    private static String parseElement(Element elem, String cssQuery){
        Element temp = elem.select(cssQuery).first();
        if(temp != null) {
            return temp
                    .childNode(0)
                    .toString();
        }
        return null;
    }
    public static String getImg(String id) throws IOException{
        String response = ruTracker.getTorrentPage(id);

        Document doc = Jsoup.parse(response);
        Element img = doc.select("var.postImg.postImgAligned").first();
        if(img == null)
            return null;
        String imgLink = img.attr("title");

        RuTrackerRequest req = new RuTrackerRequest(imgLink);
        if(ruTracker.exist(req)) {
            return imgLink;
        }
        return null;
    }
    private static void sort(String sortingKey) throws ParseException{
        if(sortingKey.equals(defaultSortKey)) {
            files.sortBySeeds(defaultSortKey.charAt(defaultSortKey.length() - 1));
            return;
        }
        if(sortingKey.length() != 3){
            throw new ParseException("Команда сортировки неверной длины", 0);
        }
        char sortingDirection = sortingKey.charAt(2);
        if(sortingDirection != '-' && sortingDirection != '+'){
            throw new ParseException("Последний символ метода сортировки должен указывать направление сортировки", 0);
        }
        String sortingMethod = String.valueOf(sortingKey.charAt(0)) + String.valueOf(sortingKey.charAt(1));
        switch (sortingMethod){
            case "se":
                files.sortBySeeds(sortingDirection);
                break;
            case "pe":
                files.sortByPeers(sortingDirection);
                break;
            case "dc":
                files.sortByDownloadCount(sortingDirection);
                break;
            case "sz":
                files.sortBySize(sortingDirection);
                break;
            case "rd":
                files.sortByTime(sortingDirection);
                break;
            default:
                 throw new ParseException("Неизвестный метод сортировки", 0);
        }
    }

    public static TorrentFile getNext(){
        return files.next();
    }
    public static boolean hasNext(){
        return files.hasNext();
    }
    public static boolean hasPrevious(){
        return files.hasPrevious();
    }
    public static boolean hasFiles() { return files != null; }
    public static void prepareForPrev() { files.prepareForPrev(); }
    public static int filesCount(){
        return files.size();
    }
}
