package techstageone;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class FirstChallenge {

    private static final String BASE_URL = "https://jsonmock.hackerrank.com/api/article_users/search";
    private static final String OPERATION_DEFAULT = "DEFAULT";
    private static final String OPERATION_SORT = "SORT";

    public static List<String> getUsernames(int threshold) throws Exception {

        return  retrieveUsernames(OPERATION_DEFAULT,threshold);
    }


    public static String getUsernameWithHighestCommentCount() throws Exception {

        return retriveUsername();
    }


    public static List<String> getUsernamesSortedByRecordDate(int threshold) throws Exception {

        return retrieveUsernames(OPERATION_SORT,threshold);
    }

    public static List<String> retrieveUsernames(String operation,int threshold) throws Exception {

        int numberOfPages = getPages();
        List<String> result = new ArrayList<>();

        if (numberOfPages > 0 && threshold > 0) {

            IntStream stream = IntStream.rangeClosed(1, numberOfPages).limit(threshold);
            Stream<Map> allData = stream.mapToObj(FirstChallenge::getDataByPage).flatMap(List::stream);


            switch (operation) {
                case OPERATION_DEFAULT:
                    result = retrieveUsernames(allData);
                    break;
                
                case OPERATION_SORT:
                    result = retrieveUsernamesSortedByCreatedDate(allData);
                    break;
            
                default:
                    break;
            }

        }

        return result;

    }

    public static String retriveUsername() throws Exception {
        int numberOfPages = getPages();
   
        String result = "";

        if (numberOfPages > 0) {

            IntStream stream = IntStream.rangeClosed(1, numberOfPages);
            Stream<Map> allData = stream.mapToObj(FirstChallenge::getDataByPage).flatMap(List::stream);
            return retrieveUsernameWithHighestCommentCount(allData);

        }
        return result;

    }

    public static List<String> retrieveUsernames(Stream<Map> data){

        return data.map(m->(String)m.get("username")).collect(Collectors.toList());

    }

    public static String retrieveUsernameWithHighestCommentCount(Stream<Map> data){

        Optional<Map> authorObj = data.max(Comparator.comparing(x->(int)x.get("comment_count")));

        return (String)authorObj.get().get("username");
    }


    public static List<String> retrieveUsernamesSortedByCreatedDate(Stream<Map> data){

        return data.sorted(Comparator.comparingInt(x->(int)x.get("created_at"))).map(m->(String)m.get("username")).collect(Collectors.toList());

    }


    private static int getPages() throws Exception {
        Map result = retreveDataFromUrl(BASE_URL);
        return (int) result.get("total_pages");

    }

    private static List<Map> getDataByPage(int pageNum) {
        try {

            Map result = retreveDataFromUrl(BASE_URL + "?page=" + pageNum);

            return (List<Map>) result.get("data");

        } catch (Exception e) {

            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static Map convertJsonResponseToMap(String jsonString) throws ScriptException {

        String java2json = "Java.asJSONCompatible(" + jsonString + ")";
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("javascript");
        return (Map) scriptEngine.eval(java2json);
    }

    public static Map retreveDataFromUrl(String urlString) throws Exception {

        URL url = new URL(urlString);
        try (InputStream is = url.openStream(); BufferedReader in = new BufferedReader(new InputStreamReader(is))) {

            String inputline;
            StringBuilder content = new StringBuilder();
            while ((inputline = in.readLine()) != null) {

                content.append(inputline);

            }

            return convertJsonResponseToMap(content.toString());
        }
    }

}