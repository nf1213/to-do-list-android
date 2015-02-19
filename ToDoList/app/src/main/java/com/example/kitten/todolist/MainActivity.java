package com.example.kitten.todolist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    ListView listView;
    private ArrayAdapter arrayAdapter;
    String [] toDoListArray = {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // call AsyncTask to perform network operation on separate thread
        new HttpAsyncTask().execute("http://10.0.2.2:3000/api/v1/tasks");

        List<String> toDoList = new ArrayList<String>(Arrays.asList(toDoListArray));
        arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.to_do_list_item, R.id.to_do_list_item_textView, toDoList);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... urls) {
            try {
                return getTasksFromJson(GET(urls[0]));
            } catch (JSONException e) {
                Log.e("HttpAsyncTask", e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                arrayAdapter.clear();
                for (String task : result) {
                    arrayAdapter.add(task);
                }
            }
        }

        private String[] getTasksFromJson(String json)
                throws JSONException {

            JSONObject tasksJson = new JSONObject(json);
            JSONArray tasksArray = tasksJson.getJSONArray("tasks");

            String[] resultStrs = new String[tasksArray.length()];
            for(int i = 0; i < tasksArray.length(); i++) {
                resultStrs[i] = tasksArray.getJSONObject(i).getString("name");
            }

            return resultStrs;

        }
    }
}
