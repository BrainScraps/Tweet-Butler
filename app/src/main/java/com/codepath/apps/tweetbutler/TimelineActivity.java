package com.codepath.apps.tweetbutler;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.activeandroid.util.Log;
import com.codepath.apps.tweetbutler.adapters.TweetsArrayAdapter;
import com.codepath.apps.tweetbutler.models.Tweet;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class TimelineActivity extends ActionBarActivity {

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
    client = TwitterApplication.getRestClient();
    populateTimeline();
  }

  private void populateTimeline(){
    client.getHomeTimeline(new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        Log.d("DEBUG", response.toString());

        aTweets.addAll(Tweet.fromJSONArray(response));
        aTweets.notifyDataSetChanged();
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
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
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
