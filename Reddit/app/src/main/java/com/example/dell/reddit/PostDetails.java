package com.example.dell.reddit;

/**
 * Created by dell on 15-07-2017.
 */

public class PostDetails {
    String kind;
    String title;
    Double created_utc_timestamp;
    int number_of_comments;
    String thumbnail;

    PostDetails(String kind, String title, Double created_utc_timestamp, int number_of_comments, String thumbnail) {

        this.kind = kind;
        this.title = title;
        this.created_utc_timestamp = created_utc_timestamp;
        this.number_of_comments = number_of_comments;
        this.thumbnail = thumbnail;
    }
}
