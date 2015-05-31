package com.example.tbr666.videoloader3;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tibor Žukina
 * @version 1.0
 * This activity provides the input of search term for youtube videos and starts the search
 */
public class DisplayActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        /*Defines the button that starts the search of youtube videos
         */
        final Button btnSearch = (Button) findViewById(R.id.search);
        /*Defines the text field for search term input
         */
        final TextView term=(TextView) findViewById(R.id.term);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Search results are displayed when search button is pressed
                 */
                displayResults(term.getText().toString());
            }
        });
    }
   /*
     @param filter2 the search term given to the method
    */
    protected void displayResults(String filter2) {
        final String filter=filter2;
        AsyncTask<Void, Void, List<Result>> task =
                new AsyncTask<Void, Void, List<Result>>() {
                    @Override
                    protected List<Result> doInBackground(Void... params) {
                        /*
                        Creates an instance of search class and fetches list of search results- video id, video name, thumbnail url
                         */
                        Search search = new Search();
                        List<Result> results= search.getResults(filter);
                        return  search.getResults(filter);
                    }

                    @Override
                    protected void onPostExecute(List<Result> results) {
                        if(results != null) {
                            /*
                            After fetching search results a new intent is started to display them and enable their playing
                             */
                            Intent intent = new Intent(DisplayActivity.this, ResultsActivity.class);
                            intent.putExtra("scheduleList", (ArrayList<Result>) results);
                            intent.putExtra("searchTerm",(String)filter);
                            startActivity(intent);
                        }
                    }
                };

        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display, menu);
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

