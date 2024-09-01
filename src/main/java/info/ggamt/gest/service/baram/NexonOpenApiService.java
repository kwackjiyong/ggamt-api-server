package info.ggamt.gest.service.baram;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
// import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

// import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.List;
import java.util.ArrayList;
import java.net.ConnectException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import info.ggamt.gest.dto.baram.BaramUserBasicInfoDTO;
import info.ggamt.gest.dto.common.UserOcidDTO;
import info.ggamt.gest.util.util;


/** 
 * Nexon Open API를 접근하는 서비스
 */
@Service
public class NexonOpenApiService {
    private static String key = "";
    private static ObjectMapper objectMapper = new ObjectMapper();

    /** api key 로드 */
    public void keyLoading() {
        Properties properties = new Properties();
        try (InputStream input = NexonOpenApiService.class.getResourceAsStream("/apiKey.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find apiKey.properties");
                return;
            }
            
            // Load properties from the file
            properties.load(input);
            key = properties.getProperty("apiKey");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** 
     * 바람홈페이지 html parser 
     * @description 직업별로 나누어 결과 생성
     */
    public List<List<String>> parseGthrUserName() {
        List<List<String>> result = new ArrayList<List<String>>();
        int jLast = 10; // 직업 수
        int iLast = 500; // 직업별 반복 호출 (건당 20명씩)
        String url = "https://baram.nexon.com/Rank/List"; // 바람 랭킹페이지 url
        for (int j=1; j<=jLast; j++) {
            List<String> slot = new ArrayList<String>();
            for (int i=0; i<iLast; i++) {
                // List<String> result = searchList(String.valueOf(1+(i*20)),String.valueOf(j));
                try {
                    Document document = Jsoup.connect(url)
                                                    .data("maskGameCode", "131073") // 연
                                                    // .data("maskGameCode", "131086") // 유리
                                                    .data("n4Rank_start", String.valueOf(1+(i*20)))
                                                    .data("codeGameJob", String.valueOf(j))
                                                    .get();
                    
                    Elements titles = document.select("body").select(".gameid a");
                    for (org.jsoup.nodes.Element title : titles) {
                        if(title != null) {
                            if(title.text() != null) {
                                slot.add(title.text());
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Failed to retrieve the web page.");
                }
                System.out.println("index:" + i);
                System.out.println("job:" + j);
            }
            // 직업별 유저명 삽입
            result.add(slot);
        }
        return result;
    }

    /**
     * [open api]
     * 모든 유저의 ocid 조회
     * @param client
     * @param userNames
     * @return ocids
     */
    public List<String> retvOcidOpenApi (HttpClient client, List<String> userNames) {
        List<String> ocids = new ArrayList<String>();
        
        List<CompletableFuture<HttpResponse<String>>> futures = new ArrayList<>();
        // 각 URL에 대해 비동기 요청을 수행하고 futures 리스트에 추가
        for (String userName : userNames) {
            String characterName = "character_name=" + userName; //buildQueryString(id);
            String serverName = "server_name=%EC%97%B0"; // 연
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://open.api.nexon.com/baram/v1/id?" + characterName + "&" + serverName))
                    .header("x-nxopen-api-key", key)
                    .version(HttpClient.Version.HTTP_2)
                    .build();

            CompletableFuture<HttpResponse<String>> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            futures.add(future);
        }
        // 모든 요청이 완료되기를 기다림
        CompletableFuture<Void> allOfFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        allOfFutures.thenRun(() -> {
            // 모든 응답을 가져와서 처리
            for (CompletableFuture<HttpResponse<String>> future : futures) {
                try {
                    HttpResponse<String> response = future.get(); // 각 요청의 응답을 가져옴
                    if(response.body() != null) {
                        UserOcidDTO uo = objectMapper.readValue(response.body(), UserOcidDTO.class);
                        ocids.add(uo.getOcid());
                    }
                } catch (java.util.concurrent.CompletionException | InterruptedException | ExecutionException | JsonProcessingException e) {
                    // e.printStackTrace();
                }
            }
        }).join(); // 모든 요청이 완료될 때까지 대기
        return ocids;
    }

    /**
     * [open api]
     * 모든 유저의 기본정보 조회
     * @param client
     * @param ocids
     * @return baramInfos
     */
    public List<BaramUserBasicInfoDTO> retvBasicInfoOpenApi (HttpClient client, List<String> ocids) {
        List<BaramUserBasicInfoDTO> baramInfos = new ArrayList<BaramUserBasicInfoDTO>();
        List<CompletableFuture<HttpResponse<String>>> futures = new ArrayList<>();
        for (String ocid : ocids) {
            String queryOcid = "ocid=" + util.buildQueryString(ocid);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://open.api.nexon.com/baram/v1/character/basic?" + queryOcid))
                    .header("x-nxopen-api-key", key)
                    .version(HttpClient.Version.HTTP_2)
                    .build();
            CompletableFuture<HttpResponse<String>> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            futures.add(future);
        }
        CompletableFuture<Void> allOfFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        allOfFutures.thenRun(() -> {
            // 모든 응답을 가져와서 처리
            for (CompletableFuture<HttpResponse<String>> future : futures) {
                try {
                    HttpResponse<String> response = future.get(); // 각 요청의 응답을 가져옴
                    if(response.body() != null) {
                        BaramUserBasicInfoDTO basicInfo = objectMapper.readValue(response.body(), BaramUserBasicInfoDTO.class);
                        baramInfos.add(basicInfo);
                    }
                } catch (java.util.concurrent.CompletionException | InterruptedException | ExecutionException | JsonProcessingException e) {
                    // e.printStackTrace();
                }
            }
        }).join(); // 모든 요청의 완료까지 대기
        return baramInfos;
    }
}
