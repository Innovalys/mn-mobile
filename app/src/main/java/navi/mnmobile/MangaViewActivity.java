package navi.mnmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import navi.mnmobile.navi.mnmobile.adapter.MangaAdapter;
import navi.mnmobile.navi.mnmobile.businesslayer.Manga;
import navi.mnmobile.navi.mnmobile.businesslayer.User;
import navi.mnmobile.navi.mnmobile.utils.DownloadImageTask;


public class MangaViewActivity extends Activity {

    private Manga manga;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_manga);

        manga = getIntent().getExtras().getParcelable("manga");
        user = getIntent().getExtras().getParcelable("user");

        TextView mangaTitle = (TextView) findViewById(R.id.tv_manga_title);
        ListView chapterList = (ListView) findViewById(R.id.lv_chapters);
        // TODO : data

        mangaTitle.setText(manga.getName());

        final ArrayAdapter<String> chapterAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_chapter, manga.getChapters());
        chapterList.setAdapter(chapterAdapter);
        chapterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chapterName = chapterAdapter.getItem(position);

                Intent myIntent = new Intent(MangaViewActivity.this, ChapterActivity.class);
                myIntent.putExtra("user", user);
                myIntent.putExtra("manga", manga);
                myIntent.putExtra("chapter_id", position);
                startActivity(myIntent);
            }
        });

        if(manga.getUrl() != null)
            new DownloadImageTask((ImageView) findViewById(R.id.img_manga_cover)).execute(manga.getUrl());
    }

}
