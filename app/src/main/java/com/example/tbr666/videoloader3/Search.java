package com.example.tbr666.videoloader3;

import android.util.Log;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 @author Tibor Žukina
 @version 1.0
 */
public class Search {
    /**
     * Define an array list where search results are stored and determines a maximal number of returned videos
     */
    List<Result> results=new ArrayList<Result>();
    private static final long NUMBER_OF_VIDEOS_RETURNED = 30;

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;

    /**
     * @param queryTerm a search term used to filter out youtube videos
     * @return list of Result class instances that contains a maximal number of search results for the given queryTerm
     */
    public  List<Result> getResults(String queryTerm) {
        try {
            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("videoloader-954").build();
            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");
            // Set your developer key from the Google Developers Console for
            // non-authenticated requests.
            String apiKey = "AIzaSyDGjFLYDZRaUbbhpnEZ-UTJXZm5lQuiZhI";
            search.setKey(apiKey);
            search.setQ(queryTerm);
            // Restrict the search results to only include videos.
            search.setType("video");
            // To increase efficiency, only retrieve the fields that the
            // application uses.
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            /**
             * Define a maximal number of search results.
             */
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            /**
             *Fetches a list of search results according to predefined requirements.
             */
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
                /*
                 *Creates a list where video name, video id and thumbnail url of search results are stored
                 */
                addToList(searchResultList.iterator());
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return results;
    }

    /**
     * @param iteratorSearchResults An iterator of search result used to extract video id, name and url of thumbnail
     */
    private void addToList(Iterator<SearchResult> iteratorSearchResults) {

        if (!iteratorSearchResults.hasNext()) {
            Log.i("No results"," There aren't any results for your query.");
        }
        while (iteratorSearchResults.hasNext()) {
            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();
            /**
             * If the search result is video then add its' data to a list of results
             */
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
                results.add(new Result(singleVideo.getSnippet().getTitle(), thumbnail.getUrl(), rId.getVideoId()));
            }
        }
    }
}
