package com.codecrafters.taskhub;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.codecrafters.taskhub.domain.Job;
import com.codecrafters.taskhub.domain.User;
import com.codecrafters.taskhub.service.JobService;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AnnouncePage extends AppCompatActivity {
    private static final String TAG = "Upload ###";

    private Uri imagePath;
    private ImageView imageview;

    private final ActivityResultLauncher<Intent> selectImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imagePath = result.getData().getData();
                    if (imagePath != null) {
                        Picasso.get().load(imagePath).into(imageview);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_announce_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String userId = getIntent().getStringExtra("userId");
        imageview = findViewById(R.id.imageView);

        try {
            Log.i("SOU EUUU", String.valueOf(MediaManager.get()));
        } catch (RuntimeException e) {
            initConfig();
        }

        imageview.setOnClickListener(v ->{
            requestPermission();
        });

        EditText edtTitle = findViewById(R.id.edtTitle);
        EditText edtDescription = findViewById(R.id.edtDescription);
        EditText edtPrice = findViewById(R.id.edtPrice);
        EditText edtDate = findViewById(R.id.edtDate);
        EditText edtLocation = findViewById(R.id.edtLocation);
        Button announceBtn = findViewById(R.id.btnAnunciar);

        edtDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                this,
                                (timeView, selectedHour, selectedMinute) -> {
                                    String selectedDateTime = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear +
                                            " " + selectedHour + ":" + String.format("%02d", selectedMinute);
                                    edtDate.setText(selectedDateTime);
                                },
                                hour, minute, true
                        );
                        timePickerDialog.show();
                    },
                    year, month, day);
            datePickerDialog.show();
        });


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean isTitleFilled = edtTitle.getText().toString().trim().isEmpty();
                boolean isDescriptionFilled = edtDescription.getText().toString().trim().isEmpty();
                boolean isPriceFilled = edtPrice.getText().toString().trim().isEmpty();
                boolean isDateFilled = edtDate.getText().toString().trim().isEmpty();
                boolean isLocationFilled = edtLocation.getText().toString().trim().isEmpty();

                announceBtn.setEnabled(!isTitleFilled && !isDescriptionFilled && !isPriceFilled && !isDateFilled && !isLocationFilled);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

         edtTitle.addTextChangedListener(textWatcher);
         edtDescription.addTextChangedListener(textWatcher);
         edtPrice.addTextChangedListener(textWatcher);
         edtDate.addTextChangedListener(textWatcher);
         edtLocation.addTextChangedListener(textWatcher);

        JobService jobService = new JobService();
        announceBtn.setOnClickListener((event) -> {
            if (imagePath == null) {
                Log.d(TAG, "Nenhuma imagem selecionada.");
                Toast.makeText(AnnouncePage.this                                                                                                                                                                                                                                                                                                                           , "Nenhuma imagem selecionada.", Toast.LENGTH_SHORT).show();
                return;
            }

            MediaManager.get().upload(imagePath)
                    .option("folder", "userImage")  // Define a pasta para onde a imagem será enviada
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            Log.d(TAG, "Upload iniciado.");
                            Toast.makeText(AnnouncePage.this, "Upload Iniciado.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                            Log.d(TAG, "Enviando...");
                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            Log.d(TAG, "Upload concluído.");
                            Toast.makeText(AnnouncePage.this, "Upload concluído.", Toast.LENGTH_SHORT).show();
                            String uploadedImageUrl = (String) resultData.get("secure_url");


                                Job jobNew = new Job();
                                jobNew.setTitle(edtTitle.getText().toString());
                                jobNew.setDetails(edtDescription.getText().toString());
                                jobNew.setImageUrl(uploadedImageUrl);
                                jobNew.setPayment(Double.valueOf(edtPrice.getText().toString()));
                                jobNew.setCrafter(new User(userId));
                                jobNew.setMoment(edtDate.getText().toString());
                                jobNew.setAddress(edtLocation.getText().toString());

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                                try {
                                    Date dateUser = simpleDateFormat.parse(jobNew.getMoment());
                                    Date now = new Date();

                                    if (dateUser.after(now)) {
                                        if (jobService.insert(jobNew)) Toast.makeText(getApplicationContext(), "Trabalho registrado com sucesso!", Toast.LENGTH_LONG).show();
                                        else Toast.makeText(getApplicationContext(), "Não há conexão com a internet!", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(AnnouncePage.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("userId", userId);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "A data do seu trabalho não pode ser anterior a data de agora!", Toast.LENGTH_LONG).show();
                                    }

                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }


                            Log.d(TAG, "URL da imagem: " + uploadedImageUrl); // A URL segura da imagem no Cloudinary
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            Log.e(TAG, "Erro: " + error.getDescription());
                            Toast.makeText(AnnouncePage.this, "Erro: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            Log.d(TAG, "Reagendado: " + error.getDescription());
                            Toast.makeText(AnnouncePage.this, "Reagendado: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                        }
                    }).dispatch();
        });

        Button accountButton = findViewById(R.id.accountButton);
        accountButton.setOnClickListener((e) -> {
            Intent intent= new Intent(this, MyProfilePage.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
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