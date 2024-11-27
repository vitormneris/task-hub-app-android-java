package com.codecrafters.taskhub;

import android.content.Intent;
import android.net.Uri;
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
import com.codecrafters.taskhub.service.JobService;
import com.codecrafters.taskhub.service.UserService;
import com.squareup.picasso.Picasso;

public class MyJobs extends AppCompatActivity {
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_jobs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String userId = getIntent().getStringExtra("userId");

        JobService jobService = new JobService();
        UserService userService = new UserService();
        User user = userService.findById(userId);

        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout layoutViewjob = findViewById(R.id.listViewJobs);

        if (user == null) {
            View playerView = inflater.inflate(R.layout.message_error, layoutViewjob, false);
            TextView textView = playerView.findViewById(R.id.message);

            textView.setText("Não há conexão com a internet.");

            layoutViewjob.addView(playerView);
        } else if (user.getJobsCreated().isEmpty()) {
            View playerView = inflater.inflate(R.layout.message_error, layoutViewjob, false);
            TextView textView = playerView.findViewById(R.id.message);
            ImageView imageView = playerView.findViewById(R.id.image_error);

            textView.setText("Você não criou nenhum trabalho ainda, registre-os no botão abaixo.");
            imageView.setImageResource(R.drawable.cara_feliz);

            layoutViewjob.addView(playerView);
        } else {
            for (Job job : user.getJobsCreated()) {
                View cardView = inflater.inflate(R.layout.layout_my_job, layoutViewjob, false);
                LinearLayout playerView = cardView.findViewById(R.id.listViewUsers);

                ImageView imageJob = cardView.findViewById(R.id.imageJob);
                TextView name = cardView.findViewById(R.id.jobName);
                TextView description = cardView.findViewById(R.id.jobDescription);
                TextView price = cardView.findViewById(R.id.jobPrice);
                TextView date = cardView.findViewById(R.id.jobDate);
                TextView creafter = cardView.findViewById(R.id.jobPostedBy);
                TextView local = cardView.findViewById(R.id.jobLocation);
                TextView available = cardView.findViewById(R.id.jobAvailable);


                if (job.getSubscribers() != null) {
                    for (User subscribers : job.getSubscribers()) {
                        View layout_user = inflater.inflate(R.layout.layout_user, playerView, false);

                        ImageView imageSub = layout_user.findViewById(R.id.imageUser);
                        TextView userName = layout_user.findViewById(R.id.userName);
                        TextView userEmail = layout_user.findViewById(R.id.userEmail);
                        TextView userPhone = layout_user.findViewById(R.id.userPhone);

                        userName.setText(subscribers.getName());
                        userEmail.setText(subscribers.getEmail());
                        userPhone.setText(subscribers.getPhone());
                        Picasso.get().load(subscribers.getImageUrl()).into(imageSub);

                        layout_user.setOnClickListener(v -> {
                            String url = "https://wa.me/" + subscribers.getPhone();
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(browserIntent);
                        });

                        playerView.addView(layout_user);
                    }
                }

                name.setText(job.getTitle());
                description.setText(job.getDetails());
                price.setText("R$ " + job.getPayment());
                date.setText("Data: " + job.getMoment());
                local.setText(job.getAddress());
                creafter.setText("Postado por: " + job.getCrafter().getName());
                available.setText(job.getAvailable() ? "Disponível" : "Indisponível");
                Picasso.get().load(job.getImageUrl()).into(imageJob);

                cardView.setOnClickListener((e) -> {
                    Intent intent= new Intent(this, EditJob.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("jobId", job.getId());
                    startActivity(intent);
                });

                Button btnAvailable = cardView.findViewById(R.id.btnAvailable);
                btnAvailable.setOnClickListener((e) -> {
                    if (jobService.updateAvailable(job.getId())) {
                        Toast.makeText(getApplicationContext(), "Atualizado com sucesso!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, MyJobs.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    } else Toast.makeText(getApplicationContext(), "Sem internet!", Toast.LENGTH_LONG).show();

                });

                Button btnDelete = cardView.findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener((e) -> {
                    count++;
                    if (count < 2) {
                        Toast.makeText(getApplicationContext(), "Clique mais uma vez para deletar o trabalho", Toast.LENGTH_LONG).show();
                    } else {

                        if (jobService.deleteById(job.getId()))
                            Toast.makeText(getApplicationContext(), "Trabalho deletado com sucesso!", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "Não a conexão com a internet!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

                layoutViewjob.addView(cardView);
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