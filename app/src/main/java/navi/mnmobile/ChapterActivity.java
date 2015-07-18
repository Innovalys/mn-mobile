package navi.mnmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

import navi.mnmobile.navi.mnmobile.adapter.ChapterAdapter;
import navi.mnmobile.navi.mnmobile.adapter.MangaAdapter;
import navi.mnmobile.navi.mnmobile.businesslayer.Chapter;
import navi.mnmobile.navi.mnmobile.businesslayer.Manga;
import navi.mnmobile.navi.mnmobile.businesslayer.Page;
import navi.mnmobile.navi.mnmobile.businesslayer.User;


public class ChapterActivity extends Activity {

    private User usr;
    private Manga manga;
    private Chapter chapter;

    private int chapter_id;
    private ListView listImages;
    private TextView chapterTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        usr = getIntent().getExtras().getParcelable("user");
        manga = getIntent().getExtras().getParcelable("manga");
        chapter_id = getIntent().getExtras().getInt("chapter_id");

        chapterTitle = (TextView) findViewById(R.id.tv_chapter_title);
        listImages = (ListView) findViewById(R.id.lv_chapter_pages);

    }

    @Override
    protected void onResume() {
        super.onResume();

        chapterTitle.setText(manga.getName() + " - " + manga.getChapters().get(chapter_id));

        // Launch download of manga info
        HTTPGetChapters gc = new HTTPGetChapters();
        gc.execute();
    }

    public class HTTPGetChapters extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        private String getResponseText(String header, String url, int port, String URI, String credentials) throws IOException {
            StringBuilder response = new StringBuilder();
            URL _url = new URL(header, url, port, URI);

            HttpURLConnection httpconn = (HttpURLConnection) _url.openConnection();
            httpconn.setRequestProperty("Authorization", credentials);

            if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
                String strLine = null;

                while ((strLine = input.readLine()) != null) {
                    response.append(strLine);
                }

                input.close();
            }
            return response.toString();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                // CONNEXION
                String uri = "/mn-server/user/manga/id/" + manga.getId() + "/" + chapter_id;
                String JSONUser = getResponseText("http", "server.vuzi.fr", 200, "/mn-server/user/manga/id/" + manga.getId() + "/" + (chapter_id + 1), "Basic " + usr.getPassword());
                JSONObject jsonObject = new JSONObject(JSONUser);
                if (jsonObject.optString("code").equals("200")) {

                    JSONObject jsonChapter = jsonObject.getJSONObject("data");
                    JSONArray pages = jsonChapter.getJSONArray("pages");

                    chapter = new Chapter(jsonChapter.optString("title"), new ArrayList<Page>());

                    for (int i = 0; i < pages.length(); i++) {
                        JSONObject jsonPage = pages.getJSONObject(i);

                        // Link & page number
                        chapter.getPages().add(new Page(jsonPage.optString("link"), i));
                    }
                } else if (jsonObject.optString("code").equals("403")) {
                    Toast.makeText(getApplicationContext(), "Nom de compte et/ou mot de passe n'existe pas !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Serveur ne repond pas !", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException je) {
                je.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            final ChapterAdapter chapterAdapter = new ChapterAdapter(chapter, getLayoutInflater());
            listImages.setAdapter(chapterAdapter);

        }
    }

}
