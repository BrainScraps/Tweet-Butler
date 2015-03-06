package com.codepath.apps.tweetbutler.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tweet {
  private String body;
  private long uid;
  private User user;
  private String createdAt;

  public User getUser() {
    return user;
  }

  public String getBody() {
    return body;
  }

  public long getUid() {
    return uid;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  // Deserialize the JSON

  public static Tweet fromJSON(JSONObject json){
    Tweet tweet = new Tweet();
    try {
      tweet.body = json.getString("text");
      tweet.uid = json.getLong("id");
      tweet.createdAt = json.getString("created_at");
      tweet.user = User.fromJson(json.getJSONObject("user"));
    } catch (JSONException e){
      e.printStackTrace();
    }

    return tweet;
  }

  public static ArrayList<Tweet> fromJSONArray(JSONArray array){
    ArrayList<Tweet> tweets = new ArrayList<>();
    for(int i = 0; i < array.length(); i++){
      try {
        JSONObject tweetJson = array.getJSONObject(i);
        Tweet tweet = Tweet.fromJSON(tweetJson);
        if (tweet != null){
          tweets.add(tweet);
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return tweets;
  }
}
