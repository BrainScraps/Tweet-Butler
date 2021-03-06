package com.codepath.apps.tweetbutler.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.tweetbutler.R;
import com.codepath.apps.tweetbutler.TwitterApplication;
import com.codepath.apps.tweetbutler.TwitterClient;
import com.codepath.apps.tweetbutler.UserProfileActivity;
import com.codepath.apps.tweetbutler.adapters.EndlessScrollListener;
import com.codepath.apps.tweetbutler.adapters.TweetsArrayAdapter;
import com.codepath.apps.tweetbutler.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserTimelineFragment extends TweetsListFragment{

  private TwitterClient client;
  private ListView lvTweets;
  private TweetsArrayAdapter aTweets;
  private ArrayList<Tweet> tweets;
  private String twitterUsername;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    tweets = new ArrayList<>();
    aTweets = new TweetsArrayAdapter(getActivity().getApplicationContext(), tweets);
    client = TwitterApplication.getRestClient();
    twitterUsername = getArguments().getString("screenName", "");
    populateTimeline();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_user_mentions, container, false);
    lvTweets = (ListView) v.findViewById(R.id.lvTweets);
    lvTweets.setAdapter(aTweets);
    lvTweets.setOnScrollListener(new EndlessScrollListener() {
      @Override
      public void onLoadMore(int page, int totalItemsCount) {
        Log.d("oldertweets", String.valueOf(totalItemsCount));
        addOlderTimelineTweets(totalItemsCount);
      }
    });

    lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), UserProfileActivity.class);
        Tweet tweet = tweets.get(position);
        i.putExtra("user", tweet.getUser().getScreenName());
        startActivity(i);
      }
    });

    return v;
  }
  public static UserTimelineFragment newInstance(String screenName) {
    UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
    Bundle args = new Bundle();
    args.putString("screenName", screenName);
    userTimelineFragment.setArguments(args);
    return userTimelineFragment;
  }

  private void populateTimeline(){
    client.getUserTimeline(twitterUsername, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        aTweets.clear();
        aTweets.addAll(Tweet.fromJSONArray(response));
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Toast.makeText(getActivity().getApplicationContext(), "Problem retrieving tweets :/", Toast.LENGTH_SHORT).show();
        Log.d("DEBUG", errorResponse.toString());
      }
    });
  }


  public void addOlderTimelineTweets(int totalItemsCount){
    Tweet lastTweet = (Tweet) aTweets.getItem((totalItemsCount - 1));
    String uid = String.valueOf(lastTweet.getUid());
    client.loadOlderTimelineTweets(twitterUsername, uid, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        aTweets.addAll(Tweet.fromJSONArray(response));
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Toast.makeText(getActivity().getApplicationContext(), "Problem retrieving tweets :/", Toast.LENGTH_SHORT).show();
        Log.d("DEBUG", errorResponse.toString());
      }

    });
  }
}
