package com.example.apicalljson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTv();
            }
        });
    }

    private void updateTv() {
        EditText editText = findViewById(R.id.editText);
        String name = editText.getText().toString();
//        InsertTask insertTask = new InsertTask();
//        insertTask.execute("https://api.github.com/search/users?q="+name);

        try {
            makeNetworkCall("https://api.github.com/search/users?q="+name);
        } catch (IOException e) {
            e.printStackTrace();
        };
    }



    void makeNetworkCall(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(MainActivity.this,"Could not find any Account with this name", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String result = response.body().string();

                Log.e(TAG,"onResponse: " + result);

//                ArrayList<Users> users= parseJSON(result);

                Gson gson = new Gson();
                apiResult ApiResult = gson.fromJson(result,apiResult.class);
                UserAdapter userAdapter = new UserAdapter(ApiResult.getItems());

//                UserAdapter userAdapter = new UserAdapter(users);
//                RecyclerView recyclerView = findViewById(R.id.rvUsers);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
//                recyclerView.setAdapter(userAdapter);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Code runs in Main Thread
                        RecyclerView recyclerView = findViewById(R.id.rvUsers);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        recyclerView.setAdapter(userAdapter);
                    }
                });

            }
        });
    }



    class InsertTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String strUrl = strings[0];

            try {
                URL url = new URL(strUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream iStream = httpURLConnection.getInputStream();
                Scanner sc = new Scanner(iStream);
                sc.useDelimiter("\\A");

                if(sc.hasNext()){
                    String str = sc.next();
                    return str;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Enter Valid Name";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<Users> users= parseJSON(s);
//            Log.e(TAG, "onPostExecute: "+ users.size() );
            UserAdapter userAdapter = new UserAdapter(users);
            RecyclerView recyclerView = findViewById(R.id.rvUsers);
            recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            recyclerView.setAdapter(userAdapter);
        }
    }

    ArrayList<Users> parseJSON(String s){
        ArrayList<Users> gitHubUsers = new ArrayList<Users>();

        //Parse the JSON
        try {
            JSONObject root = new JSONObject(s);
            JSONArray items = root.getJSONArray("items");

            for(int i=0;i<items.length();i++){
                JSONObject object = items.getJSONObject(i);

                String login = object.getString("login");
                Integer id = object.getInt("id");
                String avatar = object.getString("avatar_url");
                String html = object.getString("html_url");
                Double score = object.getDouble("score");

                Users user = new Users(login,id,avatar,html,score);
                gitHubUsers.add(user);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return gitHubUsers;
    }
}