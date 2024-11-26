package com.codecrafters.taskhub.request.users;

import android.os.AsyncTask;

import com.codecrafters.taskhub.utils.ConnectionFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class FindByEmailUserRequest extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder apiResponse = null;

        try {
            URL findByEmail = new URL("http://" + ConnectionFactory.SERVER_IP + ":8080/usuarios/" + strings[0] + "/encontrar-email");
            HttpURLConnection connection = (HttpURLConnection) findByEmail.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.setConnectTimeout(15000);
            connection.connect();

            if (connection.getResponseCode() != 200) return null;

            Scanner scanner = new Scanner(findByEmail.openStream());
            apiResponse = new StringBuilder();
            while (scanner.hasNext()) apiResponse.append(scanner.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (apiResponse == null) ? null : apiResponse.toString();
    }
}
