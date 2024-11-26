package com.codecrafters.taskhub.request.jobs;

import android.os.AsyncTask;

import com.codecrafters.taskhub.utils.ConnectionFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsertJobRequest extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            URL insert = new URL("http://" + ConnectionFactory.SERVER_IP + ":8080/trabalhos/inserir");
            HttpURLConnection connection = (HttpURLConnection) insert.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setDoOutput(true);
            PrintStream printStream = new PrintStream(connection.getOutputStream());
            printStream.println(strings[0]);
            connection.connect();

            if (connection.getResponseCode() == 201) return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}