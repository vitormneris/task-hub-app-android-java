package com.codecrafters.taskhub.request.users;

import android.os.AsyncTask;

import com.codecrafters.taskhub.utils.ConnectionFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteUserRequest extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            URL delete = new URL("http://" + ConnectionFactory.SERVER_IP + ":8080/usuarios/" + strings[0] + "/deletar");
            HttpURLConnection connection = (HttpURLConnection) delete.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setDoOutput(false);
            connection.setConnectTimeout(15000);
            connection.connect();

            if (connection.getResponseCode() == 204) return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}