package com.codepath.apps.tweetbutler.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.tweetbutler.R;
import com.codepath.apps.tweetbutler.TwitterApplication;
import com.codepath.apps.tweetbutler.TwitterClient;
import com.codepath.apps.tweetbutler.adapters.EndlessScrollListener;
import com.codepath.apps.tweetbutler.adapters.TweetsArrayAdapter;
import com.codepath.apps.tweetbutler.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TweetsListFragment extends Fragment{
  private ListView lvTweets;
  private ArrayAdapter aTweets;
  private ArrayList<Tweet> tweets;
  private TwitterClient client;

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
    lvTweets = (ListView) v.findViewById(R.id.lvTweets);
    lvTweets.setAdapter(aTweets);
    return v;

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    tweets = new ArrayList<>();
    aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    client = TwitterApplication.getRestClient();
  }

}
