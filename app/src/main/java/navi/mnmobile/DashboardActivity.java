package navi.mnmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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


public class DashboardActivity extends Activity {
    ListView listManga;
    User usr;
    List<Manga> userMangaList = new ArrayList<Manga>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        usr = getIntent().getExtras().getParcelable("user");

        listManga = (ListView) findViewById(R.id.lv_manga_list);

        // Launch download of manga info
        HTTPGetMangas gm = new HTTPGetMangas();
        gm.execute();

    }

    public class HTTPGetMangas extends AsyncTask<Void, Void, Void> {

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
                String JSONUser = getResponseText("http", "server.vuzi.fr", 200, "/mn-server/user/mangas/"+usr.getID(), "Basic " + usr.getPassword());
                JSONObject jsonObject = new JSONObject(JSONUser);
                if (jsonObject.optString("code").equals("200")) {
                    JSONArray aData = new JSONArray(jsonObject.optString("data"));
                    for (int i=0; i<aData.length(); i++) {
                        JSONObject actor = aData.getJSONObject(i);

                        // Name and cover
                        String id = actor.getString("id");
                        String name = actor.getString("title");
                        String cover = actor.getString("cover");

                        // Chapters
                        JSONArray jsonChapters = actor.getJSONArray("chapters");
                        List<String> chapters = new ArrayList<String>();

                        for(int j = 0; j < jsonChapters.length(); j++) {
                            JSONObject chapter = jsonChapters.getJSONObject(j);
                            chapters.add(chapter.getString("title").toString());
                        }

                        userMangaList.add(new Manga(id, name, chapters, null, null, cover));
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

            findViewById(R.id.loader).setVisibility(View.GONE);

            final MangaAdapter mangaAdapter = new MangaAdapter(userMangaList, getLayoutInflater());
            listManga.setAdapter(mangaAdapter);
            listManga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent myIntent = new Intent(DashboardActivity.this, MangaViewActivity.class);
                    myIntent.putExtra("user", usr);
                    myIntent.putExtra("manga", (Manga) mangaAdapter.getItem(position));
                    startActivity(myIntent);
                }
            });

        }
    }

}
