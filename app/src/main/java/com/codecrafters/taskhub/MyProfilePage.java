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

import com.codecrafters.taskhub.domain.User;
import com.codecrafters.taskhub.service.UserService;
import com.squareup.picasso.Picasso;

public class MyProfilePage extends AppCompatActivity {
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profile_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String userId = getIntent().getStringExtra("userId");

        UserService userService = new UserService();
        User user = userService.findById(userId);

        ImageView imageUser = findViewById(R.id.imageUser);
        TextView txtUserName = findViewById(R.id.txtUserName);
        TextView txtUserEmail = findViewById(R.id.txtUserEmail);

        txtUserName.setText(user.getName());
        txtUserEmail.setText(user.getEmail());
        Picasso.get().load(user.getImageUrl()).into(imageUser);

        Button btnEditProfile = findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener((e) -> {
            Intent intent= new Intent(this, EditProfilePage.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        Button btnShowJobs = findViewById(R.id.btnShowMyJobs);
        btnShowJobs.setOnClickListener((e) -> {
            Intent intent= new Intent(this, MyJobs.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        Button btnShowParticipatedJobs = findViewById(R.id.btnShowMyParticipatedJobs);
        btnShowParticipatedJobs.setOnClickListener((e) -> {
            Intent intent= new Intent(this, ParticipatedJobs.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        Button btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnDeleteAccount.setOnClickListener((e) -> {
            count++;
            if (count < 2) {
                Toast.makeText(getApplicationContext(), "Clique mais uma vez para deletar o usuário", Toast.LENGTH_LONG).show();
            } else {

                if (userService.deleteById(userId))
                    Toast.makeText(getApplicationContext(), "Usuário deletado com sucesso!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Não a conexão com a internet!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, LoginPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
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

        Button btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener((e) -> {
            count++;
            if (count < 2) {
                Toast.makeText(getApplicationContext(), "Clique mais uma vez para deslogar sua conta", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, LoginPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}