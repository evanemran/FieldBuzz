package com.example.fieldbuzz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormActivity extends AppCompatActivity {
    TextView txtCvFile;
    EditText nameField, mailField, phoneField, addressField, universityField, gradField, cgpaField, experienceField, workplaceField, salaryField, referenceField, githubField;
    ImageButton btnUploadCV;
    Button btnSend;
    Spinner spinner;
    ProgressBar progressBarField;

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
        btnSend = findViewById(R.id.sendButton);
        progressBarField = findViewById(R.id.progress_field);


        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.choice));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nameField.getText().toString().isEmpty() && !mailField.getText().toString().isEmpty()
                && !phoneField.getText().toString().isEmpty() && !addressField.getText().toString().isEmpty()
                && !universityField.getText().toString().isEmpty() && !gradField.getText().toString().isEmpty()
                && !cgpaField.getText().toString().isEmpty() && !experienceField.getText().toString().isEmpty()
                && !workplaceField.getText().toString().isEmpty() && !salaryField.getText().toString().isEmpty()
                && !referenceField.getText().toString().isEmpty() && !githubField.getText().toString().isEmpty())
                {
                    String name = nameField.getText().toString();
                    String mail = mailField.getText().toString();
                    String phone = phoneField.getText().toString();
                    String address = addressField.getText().toString();
                    String university = universityField.getText().toString();
                    int gradYear = Integer.parseInt(gradField.getText().toString());
                    float cgpa = Float.parseFloat(cgpaField.getText().toString());
                    int experience = Integer.parseInt(experienceField.getText().toString());
                    String workplace = workplaceField.getText().toString();
                    int salary = Integer.parseInt(salaryField.getText().toString());
                    String reference = referenceField.getText().toString();
                    String github = githubField.getText().toString();
                    String department = spinner.getSelectedItem().toString();

                    if (!validEmail(mail))
                    {
                        mailField.setError("Enter valid e-mail");
                    }

                    else if (gradYear>2020 || gradYear<2015)
                    {
                        gradField.setError("Must between 2015-2020");
                    }
                    else if (cgpa>=4.0 || cgpa<2.0)
                    {
                        cgpaField.setError("Must between 2.0-4.0");
                    }
                    else if (experience<0 || experience >100)
                    {
                        experienceField.setError("Must between 0-100");
                    }
                    else if (salary<15000 || salary >60000)
                    {
                        salaryField.setError("Must between 15k-60k");
                    }
                    else if (department.equals("Choose Department"))
                    {
                        Toast.makeText(FormActivity.this, "Select a Department", Toast.LENGTH_SHORT).show();
                    }
                    else if (txtCvFile.getText().equals("Upload CV"))
                    {
                        Toast.makeText(FormActivity.this, "Select CV File", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //send
                        progressBarField.setVisibility(View.VISIBLE);
                        Intent i = getIntent();
                        String token = "Token " + i.getStringExtra("auth_token");

                        CV_File cv_file = new CV_File(token);
                        long time = System.currentTimeMillis();

                        Data data = new Data(token, name, mail, phone, address, university, gradYear, cgpa, experience, workplace, department, salary, reference, github, cv_file, time, time);

                        Retrofit.Builder builder = new Retrofit.Builder()
                                .baseUrl("https://recruitment.fisdev.com/api/v0/")
                                .addConverterFactory(GsonConverterFactory.create());

                        Retrofit retrofit = builder.build();
                        SendData sendData = retrofit.create(SendData.class);

                        Call<Data> response = sendData.getData(token, data);

                        response.enqueue(new Callback<Data>() {
                            @Override
                            public void onResponse(Call<Data> call, Response<Data> response) {
                                progressBarField.setVisibility(View.INVISIBLE);
                                Toast.makeText(FormActivity.this, "Success ", Toast.LENGTH_SHORT).show();
                                uploadFile(txtCvFile.getText().toString());
                            }

                            @Override
                            public void onFailure(Call<Data> call, Throwable t) {
                                progressBarField.setVisibility(View.INVISIBLE);
                                Toast.makeText(FormActivity.this, "Fail", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(FormActivity.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnUploadCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermission();

            }
        });
    }

    private void uploadFile(String s) {
    }


    public boolean validEmail(String s)
    {
        return (!s.equals("") && Patterns.EMAIL_ADDRESS.matcher(s).matches());
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
