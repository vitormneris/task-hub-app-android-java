package com.codecrafters.taskhub;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.codecrafters.taskhub.domain.Job;
import com.codecrafters.taskhub.domain.User;
import com.codecrafters.taskhub.service.JobService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditJob extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_job);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String userId = getIntent().getStringExtra("userId");
        String jobId = getIntent().getStringExtra("jobId");

        EditText edtTitle = findViewById(R.id.edtTitle);
        EditText edtDescription = findViewById(R.id.edtDescription);
        EditText edtPrice = findViewById(R.id.edtPrice);
        EditText edtDate = findViewById(R.id.edtDate);
        EditText edtLocation = findViewById(R.id.edtLocation);
        Button btnSaveChanges = findViewById(R.id.btnSaveChanges);

        JobService jobService = new JobService();
        Job job = jobService.findById(jobId);

        edtTitle.setText(job.getTitle());
        edtDescription.setText(job.getDetails());
        edtPrice.setText(String.valueOf(job.getPayment()));
        edtDate.setText(job.getMoment());
        edtLocation.setText(job.getAddress());

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

                btnSaveChanges.setEnabled(!isTitleFilled && !isDescriptionFilled && !isPriceFilled && !isDateFilled && !isLocationFilled);
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

        btnSaveChanges.setOnClickListener((event) -> {

            Job jobNew = new Job();
            jobNew.setTitle(edtTitle.getText().toString());
            jobNew.setDetails(edtDescription.getText().toString());
            jobNew.setImageUrl("NO IMAGE");
            jobNew.setPayment(Double.valueOf(edtPrice.getText().toString()));
            jobNew.setMoment(edtDate.getText().toString());
            jobNew.setAddress(edtLocation.getText().toString());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            try {
                Date dateUser = simpleDateFormat.parse(jobNew.getMoment());
                Date now = new Date();

                if (dateUser.after(now)) {
                    if (jobService.update(jobId, jobNew)) Toast.makeText(getApplicationContext(), "Trabalho atualizado com sucesso!", Toast.LENGTH_LONG).show();
                    else Toast.makeText(getApplicationContext(), "Não há conexão com a internet!", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "A data do seu trabalho não pode ser anterior a data de agora!", Toast.LENGTH_LONG).show();
                }

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        Button accountButton = findViewById(R.id.accountButton);
        accountButton.setOnClickListener((e) -> {
            Intent intent= new Intent(this, MyProfilePage.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }
}