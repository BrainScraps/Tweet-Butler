package com.codepath.apps.tweetbutler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.codepath.apps.tweetbutler.adapters.EndlessScrollListener;
import com.codepath.apps.tweetbutler.adapters.TweetsArrayAdapter;
import com.codepath.apps.tweetbutler.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class TimelineActivity extends ActionBarActivity {

  private static final int COMPOSE_TWEET_CODE = 1337;
  private TwitterClient client;
  private ArrayList<Tweet> tweets;
  private TweetsArrayAdapter aTweets;
  private ListView lvTweets;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);
    lvTweets = (ListView) findViewById(R.id.lvTweets);
    tweets = new ArrayList<>();
    aTweets = new TweetsArrayAdapter(this, tweets);
    lvTweets.setAdapter(aTweets);
    lvTweets.setOnScrollListener(new EndlessScrollListener() {
      @Override
      public void onLoadMore(int page, int totalItemsCount) {
        Log.d("oldertweets", String.valueOf(totalItemsCount));
        addOlderTweets(totalItemsCount);
      }
    });
    client = TwitterApplication.getRestClient();
    populateTimeline();
    if (usernameNotInPreferences()){
      storeUsername();
    }
  }

  private void storeUsername(){
     client.userProfile( new TwitterClient.UserProfileLoaderListener(){

     @Override
     public void onProfileLoaded(String screenName) {
       SharedPreferences preferences = getSharedPreferences("TweetButler", MODE_PRIVATE);
       SharedPreferences.Editor editor = preferences.edit();
       editor.putString("screenName", screenName);
       editor.apply();
//       preferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
//         @Override
//         public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//
//         }
//       });
     }
   });
  }

  private boolean usernameNotInPreferences(){
    SharedPreferences pref = getSharedPreferences("TweetButler", MODE_PRIVATE);
    return pref.getString("screenName", "").equals("");
  }

  private void populateTimeline(){
    client.getHomeTimeline(new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        aTweets.clear();
        aTweets.addAll(Tweet.fromJSONArray(response));
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Toast.makeText(getApplicationContext(), "Problem retrieving tweets :/", Toast.LENGTH_SHORT).show();
        Log.d("DEBUG", errorResponse.toString());
      }
    });
  }

  private void addOlderTweets(int totalItemsCount){
    Tweet lastTweet = aTweets.getItem((totalItemsCount - 1));
    String uid = String.valueOf(lastTweet.getUid());
    client.loadOlderTweets(uid, new JsonHttpResponseHandler(){
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        aTweets.addAll(Tweet.fromJSONArray(response));
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Toast.makeText(getApplicationContext(), "Problem retrieving tweets :/", Toast.LENGTH_SHORT).show();
        Log.d("DEBUG", errorResponse.toString());
      }

    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_timeline, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.compose_tweet) {
      Intent i = new Intent(this, ComposeTweet.class);
      startActivityForResult(i, COMPOSE_TWEET_CODE);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == COMPOSE_TWEET_CODE && resultCode == RESULT_OK) {
      postTweet(data.getStringExtra("tweetText"));
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  private void postTweet(String tweetText){
    client.postTweet( tweetText, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Toast.makeText(getApplicationContext(), "Tweet sent", Toast.LENGTH_SHORT).show();
        populateTimeline();
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Toast.makeText(getApplicationContext(), "Problem sending tweet", Toast.LENGTH_SHORT).show();
        Log.d("DEBUG", responseString);
      }

    });

  }
}
