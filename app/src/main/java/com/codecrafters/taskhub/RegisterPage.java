package com.codecrafters.taskhub;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.codecrafters.taskhub.domain.User;
import com.codecrafters.taskhub.service.UserService;

public class RegisterPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button buttonLogin = findViewById(R.id.btnToLogin);
        buttonLogin.setOnClickListener((e) -> startActivity(new Intent(this, LoginPage.class)));

        UserService userService = new UserService();
        Button buttonSignUp = findViewById(R.id.btnSignUp);

        EditText editTextName = findViewById(R.id.edtName);
        EditText editTextEmail = findViewById(R.id.edtEmail);
        EditText editTextPassword = findViewById(R.id.edtPassword);
        EditText editTextTelephone = findViewById(R.id.edtTelephone);
        TextView errorMessage = findViewById(R.id.errorMessage);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean isNameFilled = editTextName.getText().toString().trim().isEmpty();
                boolean isEmailFilled = editTextEmail.getText().toString().trim().isEmpty();
                boolean isPasswordFilled = editTextPassword.getText().toString().trim().isEmpty();
                boolean isTelephoneFilled = editTextTelephone.getText().toString().trim().isEmpty();

                buttonSignUp.setEnabled(!isNameFilled && !isEmailFilled && !isPasswordFilled && !isTelephoneFilled);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        editTextName.addTextChangedListener(textWatcher);
        editTextEmail.addTextChangedListener(textWatcher);
        editTextPassword.addTextChangedListener(textWatcher);
        editTextTelephone.addTextChangedListener(textWatcher);

        buttonSignUp.setOnClickListener((event) -> {
            User user = new User();
            user.setName(editTextName.getText().toString());
            user.setEmail(editTextEmail.getText().toString());
            user.setPassword(editTextPassword.getText().toString());
            user.setPhone(editTextTelephone.getText().toString());

            String result = userService.insert(user);
            if (result.equals("true")) {
                Toast.makeText(getApplicationContext(), "Usuário registrado com sucesso!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, LoginPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else if (result.equals("false")) errorMessage.setText("Não há conexão com a internet!");
            else if (result.equals("conflict")) errorMessage.setText("Este email já está cadastrado!");
        });

    }
}