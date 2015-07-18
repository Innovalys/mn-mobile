package navi.mnmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import navi.mnmobile.navi.mnmobile.businesslayer.User;


public class MainActivity extends Activity {
    TextView mTextView;
    EditText et_login, et_password;
    Button bt_connect;
    public User usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind View and code behind
        et_login = (EditText) findViewById(R.id.et_login);
        et_password = (EditText) findViewById(R.id.et_password);
        bt_connect = (Button) findViewById(R.id.bt_connexion);

        TextView text = (TextView) findViewById(R.id.titleapp);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/MfKazincbarcika.ttf");
        text.setTypeface(tf);

        // Event Onclick Connection
        bt_connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (et_login.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Nom de compte ne peut etre vide !", Toast.LENGTH_SHORT).show();
                } else if (et_password.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Mot de passe ne peut etre vide !", Toast.LENGTH_SHORT).show();
                } else {
                    ConnectionLogin cl = new ConnectionLogin();
                    cl.execute();


                }
            }
        });
    }

    public class ConnectionLogin extends AsyncTask<Void, Void, Void> {

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
                String pass64 = encodeBase64(et_login.getText().toString() + ":" + et_password.getText().toString());
                String JSONUser = getResponseText("http", "server.vuzi.fr", 200, "/mn-server/user/", "Basic " + pass64);
                JSONObject jsonObject = new JSONObject(JSONUser);
                if (jsonObject.optString("code").equals("200")) {
                    JSONObject aUser = new JSONObject(jsonObject.optString("data"));
                    Log.d("USER", aUser.optString("login"));
                    usr = new User(aUser.optString("id"),aUser.optString("login"), pass64);
                    Intent myIntent = new Intent(MainActivity.this, DashboardActivity.class);
                    myIntent.putExtra("user", usr);
                    startActivity(myIntent);
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
}
