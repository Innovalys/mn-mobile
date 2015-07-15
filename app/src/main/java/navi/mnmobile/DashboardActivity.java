package navi.mnmobile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import navi.mnmobile.navi.mnmobile.businesslayer.User;


public class DashboardActivity extends Activity {
    Spinner listManga;
    User usr;
    List<String> strAllMangas = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        usr = getIntent().getExtras().getParcelable("user");

        listManga = (Spinner) findViewById(R.id.sp_choose_manga);
        HTTPGetMangas gm = new HTTPGetMangas();
        gm.execute();
        try {
            ArrayAdapter<String> adapterManga = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, strAllMangas);
            listManga.setAdapter(adapterManga);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
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

    public class HTTPGetMangas extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Debut du traitement asynchrone", Toast.LENGTH_LONG).show();
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
                        String name = actor.getString("title");
                        strAllMangas.add(name);
                    }
                } else if (jsonObject.optString("code").equals("403")) {
                    Toast.makeText(getApplicationContext(), "Nom de compte et/ou mot de passe n'existe pas !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Serveur ne repond pas !", Toast.LENGTH_SHORT).show();
                }
                //Log.d("SERVEUR OUTPUT", getResponseText("http", "server.vuzi.fr", 200, "/mn-server/user/mangas/1", "Basic dGVzdDp0ZXN0"));
            } catch (JSONException je) {
                je.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(getApplicationContext(), "Le traitement asynchrone est termine", Toast.LENGTH_LONG).show();


        }

        private String encodeBase64(String __password) {
            return Base64.encodeToString(__password.getBytes(), Base64.DEFAULT);
        }

        /**
         * Method to check whether my device has or not a network connection
         * Need permission : android.permission.ACCESS_NETWORK_STATE
         *
         * @return True if device is connected to network and false else
         */
        private boolean checkDeviceConnected() {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            // Need permission : android.permission.ACCESS_NETWORK_STATE
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        }


    }
    public String[] getAllManga() {
        String tmp[] = {"Dragon Ball", "Naruto", "DeathNote"};
        return tmp;
    }
}
