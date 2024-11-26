package com.codecrafters.taskhub;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.codecrafters.taskhub.domain.User;
import com.codecrafters.taskhub.service.UserService;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button = findViewById(R.id.btnToCadastro);

        button.setOnClickListener((e) -> startActivity(new Intent(this, RegisterPage.class)));

        UserService userService = new UserService();
        Button buttonLogin = findViewById(R.id.buttonLogin);
        TextView errorMessage = findViewById(R.id.errorMessage);
        EditText editTextEmail = findViewById(R.id.edtEmail);
        EditText editTextPassword = findViewById(R.id.edtPassword);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean isEmailFilled = editTextEmail.getText().toString().trim().isEmpty();
                boolean isPasswordFilled = editTextPassword.getText().toString().trim().isEmpty();

                buttonLogin.setEnabled(!isEmailFilled && !isPasswordFilled);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        editTextEmail.addTextChangedListener(textWatcher);
        editTextPassword.addTextChangedListener(textWatcher);

        buttonLogin.setOnClickListener((event) -> {
            Boolean login = userService.login(editTextEmail.getText().toString(),
                    editTextPassword.getText().toString());
            if (login == null) {
                errorMessage.setText("Sem conexão com a internet");
            } else if (login) {
                User user = userService.findByEmail(editTextEmail.getText().toString());
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("userId", user.getId());
                startActivity(intent);
            } else {
                errorMessage.setText("E-mail ou senha informados estão incorretos");
            }
        });
    }
}