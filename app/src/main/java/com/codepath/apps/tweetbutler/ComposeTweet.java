package com.codepath.apps.tweetbutler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class ComposeTweet extends ActionBarActivity {
  ImageView ivProfilePicture;
  EditText etTweet;
  TextView tvScreenName;
  TextView tvFullName;
  TwitterClient client;
  String screenName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_compose_tweet);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    etTweet = (EditText) findViewById(R.id.etTweet);
    etTweet.requestFocus();
    client = TwitterApplication.getRestClient();
    SharedPreferences prefs = getSharedPreferences("TweetButler", MODE_PRIVATE);
    screenName = prefs.getString("screenName", "");
    tvScreenName = (TextView) findViewById(R.id.tvScreenName);
    tvScreenName.setText("@" + screenName);
    getUserInfo(screenName);


  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_compose_tweet, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.send_tweet) {
      etTweet = (EditText) findViewById(R.id.etTweet);
      String tweetText = etTweet.getText().toString();
      Intent i = new Intent();
      i.putExtra("tweetText", tweetText);
      setResult(RESULT_OK, i);
      finish();
    }

    return super.onOptionsItemSelected(item);
  }

  public void getUserInfo(String screenName){
    client.profilePicture(screenName, new TwitterClient.ProfilePictureUrlLoaderListener() {

      @Override
      public void onProfilePictureUrlLoaded(String url, String name) {
//        ImageView profilePicture = (ImageView) findViewById(R.id.ivProfile);
        ImageView profilePic = (ImageView) findViewById(R.id.ivProfile);
        Picasso.with(getApplicationContext()).load(url).into(profilePic);
        TextView tvFullName = (TextView) findViewById(R.id.tvFullName);
        tvFullName.setText(name);

      }
    });
  }
}
