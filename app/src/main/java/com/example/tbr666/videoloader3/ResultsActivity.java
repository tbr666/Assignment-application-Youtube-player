package com.example.tbr666.videoloader3;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tibor Žukina
 * @version 1.0
 * This activity displays youtube video search results and starts playing video when item is selected.
 */
public class ResultsActivity extends ListActivity {
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        /**
         * When item of list is clicked an intent is started to play the video. Video result represented by that item is
         * sent to PlayVideo activity
         */
        Object o = this.getListAdapter().getItem(position);
        Result result = (Result) o;
        Intent intent = new Intent(ResultsActivity.this, PlayVideo.class);
        intent.putExtra("video", result);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("success0", "starting list activity");
        super.onCreate(savedInstanceState);
        /**
         * When activity is created, it receives a list of search results from DisplayActivity activity
         */
        Log.i("success1", "starting list activity");
        @SuppressWarnings("unchecked")
        List<Result> results = (List<Result>)
                getIntent().getSerializableExtra("scheduleList");
        String filter=(String) getIntent().getSerializableExtra("searchTerm");
        Log.i("success2", "fetching results");
        for(Result result:results) Log.i("item",result.getName() + result.getUrl() + result.getId());
        /**
         * A list adapter is created to display the list of results
         */
        setListAdapter(new ResultsAdapter(this, results, filter));
        Log.i("success3", "adapter set");
        ListView lv=getListView();
        lv.setTextFilterEnabled(true);
        /**
         * A header that contains search button and input field is added to the header of the layout. It enables new search input
         * and display of new results.
         */
        LayoutInflater inflater= getLayoutInflater();
        ViewGroup header=(ViewGroup)inflater.inflate(R.layout.activity_display,lv,false);
        lv.addHeaderView(header, null, false);
        final Button btnSearch = (Button) findViewById(R.id.search);
        final TextView term=(TextView) findViewById(R.id.term);
        term.setText(filter);
        /**
         * When search button in the header is pressed the ResultsActivity is restarted, but with new search term
         */
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayResults(term.getText().toString());
            }
        });
    }

    /**
     * This method fetches results for the selected filter
     * @param filter2 the search term given to the method
     */
    protected void displayResults(String filter2) {
        final String filter=filter2;
        AsyncTask<Void, Void, List<Result>> task =
                new AsyncTask<Void, Void, List<Result>>() {
                    @Override
                    protected List<Result> doInBackground(Void... params) {
                        Search search = new Search();
                        List<Result> results= search.getResults(filter);
                        for(Result result:results) Log.i("item",result.getName() + result.getUrl() + result.getId());
                        return  search.getResults(filter);
                    }
                    @Override
                    protected void onPostExecute(List<Result> results) {
                        if(results != null) {
                            Intent intent = new Intent(ResultsActivity.this, ResultsActivity.class);
                            for(Result result:results) Log.i("item",result.getName() + result.getUrl() + result.getId());
                            intent.putExtra("scheduleList", (ArrayList<Result>) results);
                            intent.putExtra("searchTerm",filter);
                            startActivity(intent);
                        }
                    }
                };
        task.execute();
    }
}
