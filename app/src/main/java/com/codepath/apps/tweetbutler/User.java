package com.codepath.apps.tweetbutler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by isaac on 3/16/15.
 */
public class User {
  private String name;
  private long uid;
  private String screenName;
  private String profileImageUrl;
  private String backgroundImageUrl;
  private String tagline;
  private int followersCount;
  private int followingCount;

  public String getName() {
    return name;
  }

  public long getUid() {
    return uid;
  }

  public String getScreenName() {
    return screenName;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public String getBackgroundImageUrl() {
    return backgroundImageUrl;
  }

  public String getTagline() {
    return tagline;
  }

  public int getFollowersCount() {
    return followersCount;
  }

  public int getFollowingCount() {
    return followingCount;
  }

  public User(JSONObject json) {
    try {

      this.name = json.getString("name");
      this.uid = json.getLong("id");
      this.screenName = json.getString("screen_name");
      this.profileImageUrl = json.getString("profile_image_url");
      this.backgroundImageUrl = json.getString("profile_background_image_url");
      this.tagline = json.getString("description");
      this.followersCount = json.getInt("followers_count");
      this.followingCount = json.getInt("friends_count");
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

}
