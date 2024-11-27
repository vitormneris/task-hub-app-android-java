package com.codecrafters.taskhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.codecrafters.taskhub.domain.Job;
import com.codecrafters.taskhub.domain.User;
import com.codecrafters.taskhub.service.UserService;

import java.util.Set;

public class ParticipatedJobs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_participated_jobs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String userId = getIntent().getStringExtra("userId");

        UserService userService = new UserService();
        User user = userService.findById(userId);
        Set<Job> jobList = user.getJobsSubscribed();

        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout layoutViewjob = findViewById(R.id.listViewJobs);

        if (user == null) {
            View playerView = inflater.inflate(R.layout.message_error, layoutViewjob, false);
            TextView textView = playerView.findViewById(R.id.message);

            textView.setText("Não há conexão com a internet.");

            layoutViewjob.addView(playerView);
        } else if (jobList.isEmpty()) {
            View playerView = inflater.inflate(R.layout.message_error, layoutViewjob, false);
            TextView textView = playerView.findViewById(R.id.message);
            ImageView imageView = playerView.findViewById(R.id.image_error);

            textView.setText("Você não se inscreveu em nenhum trabalho ainda, encontre-os no botão abaixo.");
            imageView.setImageResource(R.drawable.cara_feliz);

            layoutViewjob.addView(playerView);
        } else {
            for (Job job : jobList) {
                View playerView = inflater.inflate(R.layout.layout_job_participated, layoutViewjob, false);

                TextView name = playerView.findViewById(R.id.jobName);
                TextView description = playerView.findViewById(R.id.jobDescription);
                TextView price = playerView.findViewById(R.id.jobPrice);
                TextView date = playerView.findViewById(R.id.jobDate);
                TextView creafter = playerView.findViewById(R.id.jobPostedBy);
                TextView local = playerView.findViewById(R.id.jobLocation);
                TextView available = playerView.findViewById(R.id.jobAvailable);

                name.setText(job.getTitle());
                description.setText(job.getDetails());
                price.setText("R$ " + job.getPayment());
                date.setText("Data: " + job.getMoment());
                local.setText(job.getAddress());
                creafter.setText("Postado por: " + job.getCrafter().getName());
                available.setText(job.getAvailable() ? "Disponível" : "Indisponível");

                playerView.setOnClickListener((e) -> {
                    Intent intent= new Intent(this, JobPage.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("jobId", job.getId());
                    startActivity(intent);
                });

                Button button = playerView.findViewById(R.id.btnUnsubscribe);
                button.setOnClickListener((e) -> {
                    if (userService.unsubscribe(userId, job.getId())) {
                        Toast.makeText(getApplicationContext(), "Desinscrito com sucesso!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, ParticipatedJobs.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    } else Toast.makeText(getApplicationContext(), "Sem internet!", Toast.LENGTH_LONG).show();

                });

                layoutViewjob.addView(playerView);

            }
        }

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