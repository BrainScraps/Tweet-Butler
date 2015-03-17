package com.codepath.apps.tweetbutler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.tweetbutler.models.Tweet;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
  public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
  public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
  public static final String REST_CALLBACK_URL = "oauth://tweetbutlerpro"; // Change this (here and in manifest)
  private SharedPreferences preferences;
  private SharedPreferences.Editor editor;
  private ProfilePictureUrlLoaderListener pictureListener;
  private UserProfileLoaderListener userListener;
  private UserShowLoaderListener userShowListener;

  public TwitterClient(Context context) {
    super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
  }

  // CHANGE THIS
  // DEFINE METHODS for different API endpoints here

  /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
   * 	  i.e getApiUrl("statuses/home_timeline.json");
   * 2. Define the parameters to pass to the request (query or body)
   *    i.e RequestParams params = new RequestParams("foo", "bar");
   * 3. Define the request method and make a call to the client
   *    i.e client.get(apiUrl, params, handler);
   *    i.e client.post(apiUrl, params, handler);
   */
  public void getHomeTimeline(AsyncHttpResponseHandler handler){
    String apiUrl = getApiUrl("statuses/home_timeline.json");
    RequestParams params = new RequestParams();
    params.put("count", 30);
    params.put("since_id", 1);
    getClient().get(apiUrl, params, handler);
  }

  public void getUserMentions( String username, JsonHttpResponseHandler handler){
    String apiUrl = getApiUrl("statuses/mentions_timeline.json");
    RequestParams params = new RequestParams();
    params.put("count", 30);
    getClient().get(apiUrl, params, handler);
  }
  //  public ArrayList<Tweet> loadTweets(String oldestTweetId){
//    ArrayList<Tweet> results = new ArrayList<>();
//    String apiUrl = getApiUrl("statuses/home_timeline.json");
//    RequestParams params = new RequestParams();
//    params.put("count", 30);
//    params.put("since_id", oldestTweetId);
//    getClient().get(apiUrl, params, new JsonHttpResponseHandler());
//  }
//  public interface OnSuccessfulResponseCallback {
//    public void onJsonResponse(JSONArray jsonArray);
//  }
  public void loadOlderTweets(String tweetId, AsyncHttpResponseHandler handler){
    String apiUrl = getApiUrl("statuses/home_timeline.json");
    RequestParams params = new RequestParams();
    params.put("count", 12);
    params.put("max_id", tweetId);
    getClient().get(apiUrl, params, handler);
  }

  public void userProfile(final UserProfileLoaderListener listener){
    this.userListener = listener;

    String apiUrl = getApiUrl("account/verify_credentials.json");
    RequestParams params = new RequestParams();
    params.put("skip_status", "1");

    getClient().get(apiUrl, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
          String screenName = response.getString("screen_name");
          String profileImageUrl = response.getString("profile_image_url");
          String backgroundImageUrl = response.getString("background_image_url");
          listener.onProfileLoaded(screenName, profileImageUrl, backgroundImageUrl);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Log.d("Failure in userProfile", "sad");
      }

      ;

    });
  }

  public void profilePicture(String screenName, final ProfilePictureUrlLoaderListener listener){
    this.pictureListener = listener;
    String apiUrl = getApiUrl("users/show.json");
    RequestParams params = new RequestParams();
    params.put("screen_name", screenName);

    getClient().get(apiUrl, params, new JsonHttpResponseHandler(){
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
          listener.onProfilePictureUrlLoaded(response.getString("profile_image_url"), response.getString("name"));
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Log.d("Failure in", "profilePicture");
      };

    });
  }

  public void postTweet(String tweetText, JsonHttpResponseHandler jsonHttpResponseHandler) {
    String apiUrl = getApiUrl("statuses/update.json");
    RequestParams params = new RequestParams();
    params.put("status", tweetText);
    getClient().post(apiUrl, params, jsonHttpResponseHandler);
  }

  public void loadOlderMentions(String tweetId, JsonHttpResponseHandler handler) {
    String apiUrl = getApiUrl("statuses/mentions_timeline.json");
    RequestParams params = new RequestParams();
    params.put("count", 12);
    params.put("max_id", tweetId);
    getClient().get(apiUrl, params, handler);

  }

  public void getUserObject(String userName, final UserShowLoaderListener listener) {
    this.userShowListener = listener;

    String apiUrl = getApiUrl("users/show.json");
    RequestParams params = new RequestParams();

    Toast.makeText(context, userName, Toast.LENGTH_SHORT).show();
    params.put("screen_name", userName);


    getClient().get(apiUrl, params, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
          listener.onProfileLoaded(response);
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Toast.makeText(context, "Problem loading profile", Toast.LENGTH_SHORT).show();
        Log.d("Failure in userProfile", "sad");
      }

    });

  }

  public void getUserTimeline(String twitterUsername, JsonHttpResponseHandler handler) {
    String apiUrl = getApiUrl("statuses/user_timeline.json");
    RequestParams params = new RequestParams();
    params.put("screen_name", twitterUsername);
    params.put("count", 30);
    getClient().get(apiUrl, params, handler);
  }

  public void loadOlderTimelineTweets(String screenName, String uid, JsonHttpResponseHandler handler) {
    String apiUrl = getApiUrl("statuses/user_timeline.json");
    RequestParams params = new RequestParams();
    params.put("count", 12);
    params.put("max_id", uid);
    params.put("screen_name", screenName);
    getClient().get(apiUrl, params, handler);
  }

  public static interface UserProfileLoaderListener{
    public void onProfileLoaded(String screenName, String profileImageUrl, String profileBackgroundImageUrl);
  }

  public static interface ProfilePictureUrlLoaderListener {
    public void onProfilePictureUrlLoaded(String url, String name);
  }

  public static interface UserShowLoaderListener{
    public void onProfileLoaded(JSONObject jsonObject);
  }
}