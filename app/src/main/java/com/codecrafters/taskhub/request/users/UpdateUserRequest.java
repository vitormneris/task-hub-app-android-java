package com.codecrafters.taskhub.request.users;

import android.os.AsyncTask;

import com.codecrafters.taskhub.utils.ConnectionFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateUserRequest extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL update = new URL("http://" + ConnectionFactory.SERVER_IP + ":8080/usuarios/" + strings[0] + "/atualizar");
            HttpURLConnection connection = (HttpURLConnection) update.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setDoOutput(true);
            PrintStream printStream = new PrintStream(connection.getOutputStream());
            printStream.println(strings[1]);
            connection.connect();
            if (connection.getResponseCode() == 409) return  "conflict";

            if (connection.getResponseCode() == 200) return "true";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "false";
    }
}
