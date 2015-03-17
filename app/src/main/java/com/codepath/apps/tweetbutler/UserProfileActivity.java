package com.codepath.apps.tweetbutler;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.tweetbutler.fragments.UserTimelineFragment;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;


public class UserProfileActivity extends ActionBarActivity {
  String userName;
  User user;
  TwitterClient client;
  ImageView profileImage;
  TextView twitterBio;
  TextView headerFullName;
  TextView headerScreenName;
  RelativeLayout headerLayout;
  TextView tvFollowingNumber;
  TextView tvFollowersNumber;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_profile);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    userName = getIntent().getStringExtra("user");
    client = TwitterApplication.getRestClient();
    client.getUserObject(userName, new TwitterClient.UserShowLoaderListener() {
      @Override
      public void onProfileLoaded(JSONObject jsonObject) {
        user = new User(jsonObject);
        getSupportActionBar().setTitle(user.getName());
        populateHeader(user);
      }
    });

    if (savedInstanceState == null){
      UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(userName);
      android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
      fragmentTransaction.replace(R.id.flUserTimeline, fragmentUserTimeline);
    fragmentTransaction.commit();
    }


  }

  private void populateHeader(User user) {
    headerFullName = (TextView) findViewById(R.id.headerFullName);
    headerFullName.setText(user.getName());
    headerScreenName = (TextView) findViewById(R.id.headerScreenName);
    headerScreenName.setText(user.getScreenName());
    twitterBio = (TextView) findViewById(R.id.twitterBio);
    twitterBio.setText(user.getTagline());
    tvFollowersNumber = (TextView) findViewById(R.id.tvFollowersNum);
    tvFollowersNumber.setText(String.valueOf(user.getFollowersCount()));
    tvFollowingNumber = (TextView) findViewById(R.id.tvFollowingCount);
    tvFollowingNumber.setText(String.valueOf(user.getFollowingCount()));

    profileImage = (ImageView) findViewById(R.id.headerProfileImage);
    Picasso.with(this).load(user.getProfileImageUrl()).into(profileImage);
    headerLayout = (RelativeLayout) findViewById(R.id.profileHeader);
    Picasso.with(this).load(user.getBackgroundImageUrl()).into(new Target() {
      @Override
      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        BitmapDrawable draw = new BitmapDrawable(getResources(), bitmap);
        headerLayout.setBackground(draw);
      }

      @Override
      public void onBitmapFailed(Drawable errorDrawable) {

      }

      @Override
      public void onPrepareLoad(Drawable placeHolderDrawable) {

      }
    });
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_user_profile, menu);
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
