package com.codecrafters.taskhub;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.codecrafters.taskhub.domain.User;
import com.codecrafters.taskhub.service.UserService;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfilePage extends AppCompatActivity {
    private static final String TAG = "Upload ###";

    ImageView imageUser;
    private Uri imagePath;

    private final ActivityResultLauncher<Intent> selectImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imagePath = result.getData().getData();
                    if (imagePath != null) {
                        Picasso.get().load(imagePath).into(imageUser);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String userId = getIntent().getStringExtra("userId");

        UserService userService = new UserService();
        User userFound = userService.findById(userId);
        imageUser = findViewById(R.id.imageUser);
        Picasso.get().load(userFound.getImageUrl()).into(imageUser);

        try {
            Log.i("SOU EUUU", String.valueOf(MediaManager.get()));
        } catch (RuntimeException e) {
            initConfig();
        }

        imageUser.setOnClickListener(v ->{
            requestPermission();
        });


        Button buttonSignUp = findViewById(R.id.btnSaveChanges);

        EditText editTextName = findViewById(R.id.edtName);
        EditText editTextEmail = findViewById(R.id.edtEmail);
        EditText editTextPassword = findViewById(R.id.edtPassword);
        EditText editTextTelephone = findViewById(R.id.edtTelephone);
        TextView errorMessage = findViewById(R.id.errorMessage);

        editTextName.setText(userFound.getName());
        editTextEmail.setText(userFound.getEmail());
        editTextPassword.setText(userFound.getPassword());
        editTextTelephone.setText(userFound.getPhone());

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
            if (imagePath != null) {

                MediaManager.get().upload(imagePath)
                        .option("folder", "userImage")  // Define a pasta para onde a imagem será enviada
                        .callback(new UploadCallback() {
                            @Override
                            public void onStart(String requestId) {
                                Log.d(TAG, "Upload iniciado.");
                                Toast.makeText(EditProfilePage.this, "Upload Iniciado.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onProgress(String requestId, long bytes, long totalBytes) {
                                Log.d(TAG, "Enviando...");
                            }

                            @Override
                            public void onSuccess(String requestId, Map resultData) {
                                Log.d(TAG, "Upload concluído.");
                                Toast.makeText(EditProfilePage.this, "Upload concluído.", Toast.LENGTH_SHORT).show();
                                String uploadedImageUrl = (String) resultData.get("secure_url");

                                User user = new User();
                                user.setName(editTextName.getText().toString());
                                user.setEmail(editTextEmail.getText().toString());
                                user.setImageUrl(uploadedImageUrl);
                                user.setPassword(editTextPassword.getText().toString());
                                user.setPhone(editTextTelephone.getText().toString());

                                String result = userService.update(userId, user);
                                if (result.equals("true")) {
                                    Toast.makeText(getApplicationContext(), "Usuário atualizado com sucesso!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(EditProfilePage.this, MyProfilePage.class);
                                    intent.putExtra("userId", userId);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else if (result.equals("false"))
                                    errorMessage.setText("Não há conexão com a internet!");
                                else if (result.equals("conflict"))
                                    errorMessage.setText("Este email já está cadastrado!");
                            }

                            @Override
                            public void onError(String requestId, ErrorInfo error) {
                                Log.e(TAG, "Erro: " + error.getDescription());
                                Toast.makeText(EditProfilePage.this, "Erro: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onReschedule(String requestId, ErrorInfo error) {
                                Log.d(TAG, "Reagendado: " + error.getDescription());
                                Toast.makeText(EditProfilePage.this, "Reagendado: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                            }
                        }).dispatch();
            } else {
                User user = new User();
                user.setName(editTextName.getText().toString());
                user.setEmail(editTextEmail.getText().toString());
                user.setPassword(editTextPassword.getText().toString());
                user.setPhone(editTextTelephone.getText().toString());

                String result = userService.update(userId, user);
                if (result.equals("true")) {
                    Toast.makeText(getApplicationContext(), "Usuário atualizado com sucesso!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EditProfilePage.this, MyProfilePage.class);
                    intent.putExtra("userId", userId);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (result.equals("false"))
                    errorMessage.setText("Não há conexão com a internet!");
                else if (result.equals("conflict"))
                    errorMessage.setText("Este email já está cadastrado!");
            }
        });
    }

    private void initConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", "dmpecfc6l");
        config.put("api_key", "293148339379561");
        config.put("api_secret", "UePhM5tkP2KwQu29uEXln_ZiCYc");
        MediaManager.init(this, config);
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, 1);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        selectImageLauncher.launch(intent);
    }
}