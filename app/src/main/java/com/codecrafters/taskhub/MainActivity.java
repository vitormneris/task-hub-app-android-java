package com.codecrafters.taskhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.codecrafters.taskhub.domain.Job;
import com.codecrafters.taskhub.service.JobService;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        JobService jobService = new JobService();
        List<Job> jobList = jobService.findAll();

        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout layoutViewjob = findViewById(R.id.listViewJobs);

        if (jobList == null) {
            View playerView = inflater.inflate(R.layout.message_error, layoutViewjob, false);
            TextView textView = playerView.findViewById(R.id.message);

            textView.setText("Não há conexão com a internet.");

            layoutViewjob.addView(playerView);
        } else if (jobList.isEmpty()) {
            View playerView = inflater.inflate(R.layout.message_error, layoutViewjob, false);
            TextView textView = playerView.findViewById(R.id.message);
            ImageView imageView = playerView.findViewById(R.id.image_error);

            textView.setText("Não há eventos registrados, registre-os logo abaixo.");
            textView.setTextColor(R.color.orange);
            imageView.setImageResource(R.drawable.cara_feliz);

            layoutViewjob.addView(playerView);
        } else {
            for (Job job : jobList) {
                View playerView = inflater.inflate(R.layout.layout_job, layoutViewjob, false);

                TextView name = playerView.findViewById(R.id.jobName);
                TextView description = playerView.findViewById(R.id.jobDescription);
                TextView price = playerView.findViewById(R.id.jobPrice);
                TextView date = playerView.findViewById(R.id.jobDate);
                TextView creafter = playerView.findViewById(R.id.jobPostedBy);
                TextView local = playerView.findViewById(R.id.jobLocation);

                name.setText(job.getTitle());
                description.setText(job.getDetails());
                price.setText("R$ " + job.getPayment());
                date.setText("Data: " + job.getMoment());
                local.setText(job.getAddress().getCity() + "/" + job.getAddress().getNeighborhood());
                creafter.setText("Postado por: " + job.getCrafter().getName());

                layoutViewjob.addView(playerView);
            }
        }

        CardView button = findViewById(R.id.announceBtn);
        button.setOnClickListener((e) -> startActivity(new Intent(this, AnnouncePage.class)));
    }
}