package com.example.fieldbuzz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class FormActivity extends AppCompatActivity {
    TextView txtCvFile;
    EditText nameField, mailField, phoneField, addressField, universityField, gradField, cgpaField, experienceField, workplaceField, salaryField, referenceField, githubField;
    ImageButton btnUploadCV;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        txtCvFile = findViewById(R.id.cvField);
        btnUploadCV = findViewById(R.id.cvButton);
        spinner = findViewById(R.id.applyingInField);
        nameField = findViewById(R.id.nameField);
        mailField = findViewById(R.id.emailField);
        phoneField = findViewById(R.id.phoneField);
        addressField = findViewById(R.id.addressField);
        universityField = findViewById(R.id.universityField);
        gradField = findViewById(R.id.graduationField);
        cgpaField = findViewById(R.id.cgpaField);
        experienceField = findViewById(R.id.experienceField);
        workplaceField = findViewById(R.id.currentWorkplaceField);
        salaryField = findViewById(R.id.expectedSalaryField);
        referenceField = findViewById(R.id.referenceField);
        githubField = findViewById(R.id.githubField);


        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.choice));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);


        btnUploadCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermission();

            }
        });
    }

    private void askPermission() {
        Dexter.withContext(FormActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        fileIntent.setType("*/*");
                        startActivityForResult(fileIntent, 1);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(FormActivity.this, "Permission Required to upload CV!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String filePath = data.getData().getPath();
                txtCvFile.setText(filePath);
            }
        }
    }
}
