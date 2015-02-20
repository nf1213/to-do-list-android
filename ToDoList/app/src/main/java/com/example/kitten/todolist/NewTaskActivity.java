package com.example.kitten.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
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
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        inputField = (EditText) findViewById(R.id.task_name_input_field);
        submitButton = (Button) findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpPostAsyncTask(NewTaskActivity.this).execute("http://10.0.2.2:3000/api/v1/tasks");
            }
        });
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

            HttpResponse response = httpclient.execute(post);
        } catch (Exception e) {
            Log.d("PostRequest", e.getLocalizedMessage());
        }
    }

    private class HttpPostAsyncTask extends AsyncTask<String, Void, Void> {
        Context context;
        private HttpPostAsyncTask(Context context) {
            this.context = context.getApplicationContext();
        }
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
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
    }
}
