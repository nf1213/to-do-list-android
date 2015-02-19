package com.example.kitten.todolist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class NewTaskActivity extends ActionBarActivity {

    EditText inputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        inputField = (EditText) findViewById(R.id.task_name_input_field);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_task, menu);
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

    public static void POST(String url, String data) {
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost post = new HttpPost(url);

            List<NameValuePair> postParams = new ArrayList<NameValuePair>(2);
            postParams.add(new BasicNameValuePair("name", data));

            post.setEntity(new UrlEncodedFormEntity(postParams));
        } catch (Exception e) {
            Log.d("PostRequest", e.getLocalizedMessage());
        }
    }

    private class HttpPostAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... urls) {
            try {
                String input = inputField.getText().toString();
                POST(urls[0], input);
            }
            catch (Exception e) {
                Log.d("HttpPostAsyncTask", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
