package com.example.tbr666.videoloader3;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class ResultsAdapter extends BaseAdapter {
    private Activity context;
    private List<Result> results;
    private String filter;

    public ResultsAdapter(Activity context, List<Result> results, String filter) {
        this.context = context;
        this.results = results;
        this.filter=filter;
    }

    public Activity getContext() {
        return context;
    }

    public List<Result> getSchedule() {
        return results;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return results.get(position-1);
    }

    @Override
    public long getItemId(int position) {
        return position-1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_results, null);

        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
        Result result = results.get(position);
        title.setText(result.getName());
        new DownloadImageTask((ImageView)thumbnail)
                .execute(result.getUrl());

        return convertView;
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
