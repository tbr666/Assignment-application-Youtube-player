package com.example.tbr666.videoloader3;

import java.io.Serializable;

/**
  @author Tibor Zukina
  @version 1.0
  This class represent the result of youtube video search. It contains of video name, video id and thumbnail url
 */
public class Result implements Serializable {
    /**
     *
     * @param name the name of video contained in search result
     * @param url  the url of thumbnail contained in search result
     * @param id   the video id contained in search result
     * This method initializes a new instance of search result.
     */
    public Result(String name, String url, String id){
        this.name=name;
        this.url=url;
        this.id=id;
    }
    private String url;
    private String name;
    private String id;

    /**
     *
     * @param url the new thumbnail url contained in the search result
     * This method sets a new thumbnail url of search result.
     */
    public void setUrl(String url){
        this.url=url;
    }

    /**
     * @param name the new name of video contained in the search result
     * This method sets a new video name of search result.
     */
    public void setName(String name){
        this.name=name;
    }

    /**
     *
     * @param id the new video id contained in the search result
     * This method sets a new video id of search result.
     */
    public void setId(String id){
        this.id=id;
    }

    /**
     * @return name the name of the video contained in the search result
     * This method returns a video name of search result.
     */
    public String getName(){
        return name;
    }

    /**
     * @return url the thumbnail url contained in the search result
     * This method returns a thumbnail url of search result
     */
    public String getUrl(){
        return url;
    }

    /**
     * @return id the video id contained in the search result
     * This method returns a video id or search result
     */
    public String getId(){
        return id;
    }

}
