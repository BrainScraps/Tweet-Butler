package com.codepath.apps.tweetbutler.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.codepath.apps.tweetbutler.models.Tweet;

import java.util.List;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{

  public TweetsArrayAdapter(Context context, List<Tweet> objects) {
    super(context, android.R.layout.simple_list_item_1, objects);
  }
}
