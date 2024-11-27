package com.codecrafters.taskhub.request.users;

import android.os.AsyncTask;

import com.codecrafters.taskhub.utils.ConnectionFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SubscribeUserRequest extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL delete = new URL("http://" + ConnectionFactory.SERVER_IP + ":8080/usuarios/" + strings[0] + "/" + strings[1] + "/inscrever");
            HttpURLConnection connection = (HttpURLConnection) delete.openConnection();
            connection.setRequestMethod("PATCH");
            connection.setDoOutput(false);
            connection.setConnectTimeout(15000);
            connection.connect();
            if (connection.getResponseCode() == 409) return  "conflict";

            if (connection.getResponseCode() == 200) return "true";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "false";
    }
}
