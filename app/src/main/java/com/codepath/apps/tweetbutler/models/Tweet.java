package com.codepath.apps.tweetbutler.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        continue;
      }
    }
    return tweets;
  }

  public String getAbbrevTime(){
    SimpleDateFormat f = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
    try {
      Date d = f.parse(this.getCreatedAt());
      return getTimeString(d);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }
  private static String getTimeString(Date fromdate) {

    long then;
    then = fromdate.getTime();
    Date date = new Date(then);

    StringBuffer dateStr = new StringBuffer();

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    Calendar now = Calendar.getInstance();

    int days = daysBetween(calendar.getTime(), now.getTime());
    int minutes = hoursBetween(calendar.getTime(), now.getTime());
    int hours = minutes / 60;
    if (days == 0) {

      int second = minuteBetween(calendar.getTime(), now.getTime());
      if (minutes > 60) {

        if (hours >= 1 && hours <= 24) {
          dateStr.append(hours).append("h");
        }

      } else {

        if (second <= 10) {
          dateStr.append("Now");
        } else if (second > 10 && second <= 30) {
          dateStr.append("few seconds ago");
        } else if (second > 30 && second <= 60) {
          dateStr.append(second).append("s");
        } else if (second >= 60 && minutes <= 60) {
          dateStr.append(minutes).append("m");
        }
      }
    } else

    if (hours > 24 && days <= 7) {
      dateStr.append(days).append("d");
    } else {
      dateStr.append(oldTweetDate(fromdate));
    }

    return dateStr.toString();
  }

  private static int minuteBetween(Date d1, Date d2) {
    return (int) ((d2.getTime() - d1.getTime()) / DateUtils.SECOND_IN_MILLIS);
  }

  private static int hoursBetween(Date d1, Date d2) {
    return (int) ((d2.getTime() - d1.getTime()) / DateUtils.MINUTE_IN_MILLIS);
  }

  private static int daysBetween(Date d1, Date d2) {
    return (int) ((d2.getTime() - d1.getTime()) / DateUtils.DAY_IN_MILLIS);
  }
  private static String oldTweetDate(Date date){
    SimpleDateFormat format = new SimpleDateFormat("MMM dd");
    return format.format(date);
  }

}
