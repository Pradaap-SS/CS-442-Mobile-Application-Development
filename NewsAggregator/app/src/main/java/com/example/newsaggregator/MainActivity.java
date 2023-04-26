

package com.example.newsaggregator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsaggregator.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = "MainActivity";
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private String[] sources;
    private DrawerLayout drawerLayout;
    private ActivityMainBinding ActivityMainBinding;
    private NewsEntity[] newsSources;
    private static ArrayList<NewsEntity> newsArticle = new ArrayList<>();
    Map<Integer, String> filterMap = new HashMap<>();
    private ArrayAdapter<String> arrayAdapter;
    ConstraintLayout contentFrame;
    private Menu topics;
    public NewsEntity newsEntity;
    private static List<NewsEntity> news = new ArrayList<>();
    ArrayList<NewsEntity> articles;
    private static RequestQueue queue, queue2;
    private String key ="81409d6581684f05ab3415a824a2a01e";
    private NewsViewAdapter newsViewAdapter;
    RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateNewsArticleData(String item, int id) {
        if (id == 0) return;
        String previous = filterMap.get(id);
        filterMap.put(id, item);

        NewsEntity[] filteredSources = Arrays.stream(newsSources)
                .filter(source -> (source.getCategory().equals(filterMap.get(1)) || filterMap.get(1).equals("All")))
                .toArray(NewsEntity[]::new);
        newsArticle = new ArrayList<>();
        Collections.addAll(newsArticle, filteredSources);
        String[] totalSources = null;
        totalSources = Arrays.stream(filteredSources).map(NewsEntity::getName).toArray(String[]::new);

        if (totalSources.length == 0) {
            new AlertDialog.Builder(this)
                    .setTitle("No News Sources")
                    .setMessage("No news sources exist that match the specified resource")
                    .setPositiveButton("OK", (dialog, which) -> filterMap.put(id, previous)).show();
        } else {
            sources = totalSources;
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.drawer_list, sources) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    ((TextView)view.findViewById(android.R.id.text1)).setTextColor(newsEntity.getColor(filteredSources[position].getCategory()));
                    return view;
                }
            };

            drawerList.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            setTitle("News Gateway (" + sources.length + ")");
            recyclerView.setAdapter(null);
            contentFrame.setBackgroundResource(R.drawable.background);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        topics = menu;
        topics.add(1, 0, 0, "All");
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: DrawerToggle " + item);
            return true;
        }
        updateNewsArticleData(item.getTitle().toString(), item.getGroupId());
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateMenu(NewsEntity[] newsSources) {
        Arrays.stream(newsSources).map(NewsEntity::getCategory).distinct().forEach((topic) -> {
            SpannableString str = new SpannableString(topic);
            str.setSpan(new ForegroundColorSpan(newsEntity.getColor(topic)), 0, str.length(), 0);
            topics.add(1, 0, 0, str);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setNewsSource(List<NewsEntity> newsSources) {
        newsArticle = new ArrayList<>(newsSources);
        sources = new String[newsSources.size()];
        for (int i = 0; i < sources.length; i++)
            sources[i] = newsSources.get(i).getName();
        this.setTitle("News Gateway " + "(" +sources.length+")");
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.drawer_list, sources) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView)view.findViewById(android.R.id.text1)).setTextColor(newsEntity.getColor(newsSources.get(position).getCategory()));
                return view;
            }
        };
        drawerList.setAdapter(arrayAdapter);
        NewsEntity[] newsData = new NewsEntity[newsSources.size()];
        for(int i = 0; i < newsSources.size(); i++){
            newsData[i] = newsSources.get(i);
        }
        this.newsSources = newsData;
        updateMenu(newsData);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getNewsSource() {
        queue = Volley.newRequestQueue(this);
        Uri.Builder builder = Uri.parse("https://newsapi.org/v2/top-headlines/sources").buildUpon();
        builder.appendQueryParameter("apiKey", key);
        String url = builder.build().toString();
        Response.Listener<JSONObject> jsonObjectListener = response -> {
            try {
                JSONArray jSources = new JSONObject(response.toString()).getJSONArray("sources");
                for(int i = 0; i < jSources.length(); i++) {
                    JSONObject News = (JSONObject) jSources.get(i);
                    news.add(new NewsEntity(News.getString("id"),News.getString("name"),News.getString("category")));
                }
                setNewsSource(news);
            }catch (Exception e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {
            setNewsSource(null);
        };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,jsonObjectListener,errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "News-App");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void getNewsArticleData(String source){
        queue2 = Volley.newRequestQueue(this);
        Uri.Builder builder = Uri.parse("https://newsapi.org/v2/top-headlines").buildUpon();
        builder.appendQueryParameter("sources", source);
        builder.appendQueryParameter("apiKey", key);
        String url = builder.build().toString();
        Response.Listener<JSONObject> jsonObjectListener = response -> {
            articles = new ArrayList<>();
            try {
                JSONArray newsSources = new JSONObject(response.toString()).getJSONArray("articles");
                for(int i = 0; i < newsSources.length(); i++) {
                    JSONObject article = (JSONObject) newsSources.get(i);
                    articles.add(new NewsEntity(article.getString("title"),article.getString("author"),article.getString("description"),
                            article.getString("publishedAt"),article.getString("urlToImage"),article.getString("url")));

                }
                newsViewAdapter = new NewsViewAdapter(this);
                newsViewAdapter.setList(articles);
                recyclerView.setAdapter(newsViewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            }catch (Exception e){
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {
            recyclerView.setAdapter(newsViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            return;
        };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,jsonObjectListener,errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "News-App");
                return headers;
            }
        };
        queue2.add(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(ActivityMainBinding.getRoot());
        beginAPI();

        drawerList.setOnItemClickListener((parent, view, position, id) -> {
            setTitle(newsArticle.get(position).getName());
            getNewsArticleData(newsArticle.get(position).getId());
            ActivityMainBinding.contentFrame.setBackgroundColor(Color.parseColor("#ffffff"));
            drawerLayout.closeDrawer(drawerList);
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void beginAPI() {
        drawerLayout = ActivityMainBinding.drawerLayout;
        drawerList = ActivityMainBinding.leftDrawer;
        recyclerView = ActivityMainBinding.dataList;
        contentFrame = ActivityMainBinding.contentFrame;
        filterMap.put(1, "All");
        getNewsSource();
        newsEntity = new NewsEntity();
        newsEntity.setColor(this);
    }
}
