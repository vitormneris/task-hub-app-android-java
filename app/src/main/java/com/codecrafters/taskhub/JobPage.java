package com.codecrafters.taskhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.codecrafters.taskhub.domain.Job;
import com.codecrafters.taskhub.domain.User;
import com.codecrafters.taskhub.service.JobService;
import com.codecrafters.taskhub.service.UserService;
import com.squareup.picasso.Picasso;

public class JobPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String userId = getIntent().getStringExtra("userId");
        String jobId = getIntent().getStringExtra("jobId");

        ImageView imageJob = findViewById(R.id.imageJob);
        ImageView imageUser = findViewById(R.id.imageUser);
        TextView txtTitle = findViewById(R.id.txtTitle);
        TextView txtDescription = findViewById(R.id.txtDescription);
        TextView txtPrice = findViewById(R.id.txtPrice);
        TextView txtDate = findViewById(R.id.txtDate);
        TextView txtLocation = findViewById(R.id.txtLocation);
        TextView userName = findViewById(R.id.userName);

        JobService jobService = new JobService();
        Job job = jobService.findById(jobId);

        txtTitle.setText(job.getTitle());
        txtDescription.setText(job.getDetails());
        txtPrice.setText("Valor ofertado em R$: " + job.getPayment());
        txtDate.setText(job.getMoment());
        txtLocation.setText(job.getAddress());
        Picasso.get().load(job.getImageUrl()).into(imageJob);
        userName.setText(job.getCrafter().getName());
        Picasso.get().load(job.getCrafter().getImageUrl()).into(imageUser);

        Button button = findViewById(R.id.btnContact);
        button.setOnClickListener((e) -> {
            UserService userService = new UserService();
            String result = userService.subscribe(userId, jobId);

            if (result.equals("true")) {
                Toast.makeText(getApplicationContext(), "Inscrito com sucesso!", Toast.LENGTH_LONG).show();
            }
            else if (result.equals("false")) Toast.makeText(getApplicationContext(), "Não há conexão com a internet!", Toast.LENGTH_LONG).show();
            else if (result.equals("conflict")) Toast.makeText(getApplicationContext(), "Você já está inscrito neste trabalho", Toast.LENGTH_LONG).show();
            });

        Button announceBtn = findViewById(R.id.announceBtn);
        announceBtn.setOnClickListener((e) -> {
            Intent intent= new Intent(this, AnnouncePage.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        Button homeBtn = findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener((e) -> {
            Intent intent= new Intent(this, MainActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        Button accountButton = findViewById(R.id.accountButton);
        accountButton.setOnClickListener((e) -> {
            Intent intent= new Intent(this, MyProfilePage.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }
}