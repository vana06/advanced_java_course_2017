package edu.technopolis.advanced.boatswain.ruTracker;

import edu.technopolis.advanced.boatswain.ruTracker.request.Request;
import edu.technopolis.advanced.boatswain.ruTracker.request.RuTrackerRequest;
import edu.technopolis.advanced.boatswain.ruTracker.request.SendMessagePayload;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;

public class RuTrackerApiClient implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(RuTrackerApiClient.class);
    private static final String UserAgent = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36";
    private static final String Accept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";

    private final String apiSchema;
    private final String apiHost;
    private final String loginPath;
    private final String searchPath;
    private final String viewPath;
    private final CloseableHttpClient client;

    private String cookies;

    RuTrackerApiClient(String apiSchema, String apiHost, String loginPath, String searchPath, String viewPath) {
        this.apiSchema = apiSchema;
        this.apiHost = apiHost;
        this.loginPath = loginPath;
        this.searchPath = searchPath;
        this.viewPath = viewPath;

        client = HttpClients.custom()
                .setDefaultHeaders(Arrays.asList(
                        new BasicHeader("Host", apiHost),
                        new BasicHeader("User-Agent", UserAgent),
                        new BasicHeader("Accept", Accept)
                        )
                ).build();

    }

    boolean login(String username, String password) throws IOException{
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("login_username", username));
        params.add(new BasicNameValuePair("login_password", password));
        params.add(new BasicNameValuePair("login", "%C2%F5%EE%E4"));

        RuTrackerRequest req = new RuTrackerRequest(loginPath).setPayload(new SendMessagePayload(params));
        ArrayList<NameValuePair> headers = new ArrayList<>(
                Collections.singletonList(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"))
        );
        CloseableHttpResponse response = client.execute(post(req, headers));
        if(response.getStatusLine().getStatusCode() != 302){
            log.info("Login error, wrong login or password");
            return false;
        }
        //save cookies;
        cookies = response.getFirstHeader("Set-Cookie").getValue();
        return true;
    }

    String[] search(String query) throws IOException{
        //если слов для поиска больше двух, то между словами должно стоять "%20"
        StringTokenizer st = new StringTokenizer(query);
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()){
            sb.append(st.nextToken()).append("%20");
        }
        sb.delete(sb.length()-3, sb.length()); //удалить лишний символ в конце
        query = sb.toString();

        //получаем первую страницу
        String path = searchPath + "?nm=" + query;
        RuTrackerRequest req = new RuTrackerRequest(path).setPayload(new SendMessagePayload(null));
        ArrayList<NameValuePair> headers = new ArrayList<>(
                Collections.singletonList(new BasicNameValuePair("Cookie", cookies))
        );
        String firstPage = readResponse(post(req, headers));

        //получим остальные страницы
        Document doc = Jsoup.parse(firstPage);
        Elements pages = doc.select("b > a.pg");
        String[] result = new String[Math.max(pages.size(), 1)];
        result[0] = firstPage;
        for(int i = 0; i < result.length - 1; i++){
            Element child = pages.get(i);
            String pageLink = child.attr("href");

            req = new RuTrackerRequest("/forum/" + pageLink);
            result[i+1] = readResponse(get(req, headers));
        }

        return result;
    }

    String getTorrentPage(String id) throws IOException{
        String path =  viewPath + id;
        RuTrackerRequest req = new RuTrackerRequest(path).setPayload(new SendMessagePayload(null));
        ArrayList<NameValuePair> headers = new ArrayList<>(
                Collections.singletonList(new BasicNameValuePair("Cookie", cookies))
        );
        return readResponse(get(req, headers));
    }

    <REQ extends Request> boolean exist(REQ req) throws IOException{
        HttpURLConnection con = (HttpURLConnection) new URL(req.getQueryStart()).openConnection();
        con.setRequestProperty("User-Agent", UserAgent);
        con.setRequestProperty("Accept", "image/gif,image/png,image/jpeg");

        return ImageIO.read(con.getInputStream()) != null;
    }

    private <REQ extends Request> HttpUriRequest get(REQ req, ArrayList<NameValuePair> headers) {
        String queryString = getQueryString(req);
        log.info("Sending GET to [{}]", queryString);
        HttpGet get = new HttpGet(queryString);
        for(NameValuePair header: headers) {
            get.setHeader(header.getName(), header.getValue());
        }
        return get;
    }

    private <REQ extends Request> HttpUriRequest post(REQ req, ArrayList<NameValuePair> headers) {
        String queryString = getQueryString(req);
        log.info("Sending POST to [{}] with following data [{}]", queryString, req.getPayload());

        String params = req.getPayload().toString();
        HttpPost post = new HttpPost(queryString);
        for(NameValuePair header: headers) {
            post.setHeader(header.getName(), header.getValue());
        }
        post.setEntity(new ByteArrayEntity(params.getBytes()));
        return post;
    }

    private String readResponse(HttpUriRequest httpUriRequest) throws IOException{
        CloseableHttpResponse response = client.execute(httpUriRequest);
        if(response.getStatusLine().getStatusCode() != 200){
            log.info("Search error");
            throw new IOException("Ошибка поиска");
        }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "Windows-1251"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String content = sb.toString();
        log.info("Read response {}", content);
        return content;
    }

    private <REQ extends Request> String getQueryString(REQ req) {
        String queryString = req.getQueryStart();
        return apiSchema + "://" + apiHost + queryString;
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
