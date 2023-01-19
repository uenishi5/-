package com.example.youtubeDownload.service;

import com.example.youtubeDownload.entity.SearchResult;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.Thumbnail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchService {
    private  static final String PROPERTIES_FILENAME = "youtube.properties";
    private  static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    //取得する動画の数
    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
    private static YouTube youtube;

    private String queryTerm;

    Properties properties = new Properties();

    public ArrayList<SearchResult> getList(String queryTerm){
        this.queryTerm = queryTerm;
        try{
            InputStream in = SearchService.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
            properties.load(in);
        } catch (IOException e){
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
                    + " : " + e.getMessage());
            System.exit(1);
        }

        try{
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {}
            }).setApplicationName("youtube-cmdline-search-sample").build();

            YouTube.Search.List search = youtube.search().list(Collections.singletonList("id,snippet"));

            //APIキーを設定する
            String apiKey = properties.getProperty("youtube.apikey");
            search.setKey(apiKey);
            search.setQ(queryTerm);

            search.setType(Collections.singletonList("video"));

            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            //APIの実行と思われる
            SearchListResponse searchResponse = search.execute();

            List<com.google.api.services.youtube.model.SearchResult> searchResultList = searchResponse.getItems();

            if(searchResultList != null) {
                return acquisition(searchResultList.iterator(), queryTerm);
            }

            }catch (GoogleJsonResponseException e){
             //return e;
            }catch (IOException e){
//            return e;
            }catch (Throwable e){
//            return e;
        }
        return null;
    }


    private static ArrayList<SearchResult> acquisition(Iterator<com.google.api.services.youtube.model.SearchResult> iteratorSearchResults, String query) {
        ArrayList<SearchResult> results = new ArrayList<>();

        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {

            com.google.api.services.youtube.model.SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Double checks the kind is video.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("default");

                SearchResult result = new SearchResult();
                result.setVideoId(rId.getVideoId());
                result.setTitle(singleVideo.getSnippet().getTitle());
                result.setThumbnail(thumbnail.getUrl());
                results.add(result);
            }
        }
        return results;
    }
}
