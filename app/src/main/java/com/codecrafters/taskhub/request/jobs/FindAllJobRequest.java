package com.codecrafters.taskhub.request.jobs;

import android.os.AsyncTask;

import com.codecrafters.taskhub.utils.ConnectionFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class FindAllJobRequest extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder apiResponse = null;

        try {
            URL findAll = new URL("http://" + ConnectionFactory.SERVER_IP + ":8080/trabalhos/todos");
            HttpURLConnection connection = (HttpURLConnection) findAll.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.setConnectTimeout(15000);
            connection.connect();

            if (connection.getResponseCode() != 200) return null;

            Scanner scanner = new Scanner(findAll.openStream());
            apiResponse = new StringBuilder();
            while (scanner.hasNext()) apiResponse.append(scanner.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (apiResponse == null) ? null : apiResponse.toString();
    }
}
