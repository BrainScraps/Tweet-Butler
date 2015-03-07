package com.codepath.apps.tweetbutler.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetbutler.R;
import com.codepath.apps.tweetbutler.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{

  public TweetsArrayAdapter(Context context, List<Tweet> objects) {
    super(context, android.R.layout.simple_list_item_1, objects);

  }

  private static class ViewHolder {
    TextView handle;
    TextView displayName;
    TextView tweet;
    ImageView profilePic;
    TextView relTime;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Tweet tweet = getItem(position);
    ViewHolder holder;
    if (convertView == null){
      holder = new ViewHolder();
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
      holder.handle = (TextView) convertView.findViewById(R.id.tvHandle);
      holder.displayName = (TextView) convertView.findViewById(R.id.tvDisplayName);
      holder.tweet = (TextView) convertView.findViewById(R.id.tvText);
      holder.profilePic = (ImageView) convertView.findViewById(R.id.ivProfile);
      holder.relTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.handle.setText(tweet.getUser().getScreenNameForDisplay());
    holder.tweet.setText(tweet.getBody());
    holder.displayName.setText(tweet.getUser().getName());
    holder.profilePic.setImageResource(android.R.color.transparent);
    holder.relTime.setText(tweet.getAbbrevTime());
    Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(holder.profilePic);

    return convertView;
  }
}
