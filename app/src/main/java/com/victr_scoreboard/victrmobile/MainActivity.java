package com.victr_scoreboard.victrmobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.net.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText ipTextBox = (EditText) findViewById(R.id.ipTextBox);
        ipTextBox.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void beforeTextChanged(CharSequence s,int start,int count,int after) {}

            private String mPreviousText = "";
            private final Pattern PARTIAl_IP_ADDRESS =
                    Pattern.compile("^((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])\\.){0,3}"+
                            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])){0,1}$");
            @Override public void afterTextChanged(Editable s) {
                if(PARTIAl_IP_ADDRESS.matcher(s).matches()) {
                    mPreviousText = s.toString();
                } else {
                    s.replace(0, s.length(), mPreviousText);
                }
            }
        });
        Button submitIP = (Button) findViewById(R.id.submitIP);
        submitIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = ipTextBox.getText().toString();
                //if(VerifyIP(s)){
                    IP = s;
                    selectScreen();
                //}
            }
        });


    }
    private String IP;
    private void selectScreen(){
        setContentView(R.layout.select);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://"+ IP +"/mobile.html");
        //myWebView.loadUrl("http://www.noahpaladino.com/scoreboard-page/mobile.html");
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.register);
                Button submit = (Button) findViewById(R.id.submitRegistration);
                Button back = (Button) findViewById(R.id.back);
                final EditText game = (EditText) findViewById(R.id.GameNameInput);
                final EditText team1 = (EditText) findViewById(R.id.Team1NameInput);
                final EditText team2 = (EditText) findViewById(R.id.Team2NameInput);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        httpGET(IP, game.getText().toString(), team1.getText().toString(), team2.getText().toString());
                        selectScreen();
                    }
                });
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectScreen();
                    }
                });
            }
        });
    }
    private boolean VerifyIP(String ip){
        try {
            if (isValidIp4Address(ip)) {
                URL url = new URL("http://" + ip + "/gameState.php");
                java.lang.String str = "";
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    str = readStream(in);
                } finally {
                    urlConnection.disconnect();
                }
                if (str != "")
                    return true;
                else
                    return false;
            } else {
                return false;
            }
        } catch (IOException i){
            return false;
        }
    }
    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }
    private boolean isValidIp4Address(final String hostName) {
        try {
            return Inet4Address.getByName(hostName) != null;
        } catch (UnknownHostException ex) {
            return false;
        }
    }

    public static void httpGET(final java.lang.String ip,  final java.lang.String game, final java.lang.String team1, final java.lang.String team2){
        try {
                URL url = new URL("http://" + ip + "/register.php?gameName=" + URLEncoder.encode(game, "UTF-8") + "&team1name=" +  URLEncoder.encode(team1, "UTF-8")+ "&team2name=" +  URLEncoder.encode(team2, "UTF-8"));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                } finally {
                    urlConnection.disconnect();
                }
        } catch (IOException i){
        }
    }
}
