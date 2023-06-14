package com.nvn.mobilent.screens.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.nvn.mobilent.R;
import com.nvn.mobilent.screens.forgot.ForgotPasswordActivity;
import com.nvn.mobilent.MainActivity;
import com.nvn.mobilent.screens.register.RegisterActivity;

import com.nvn.mobilent.data.model.user.User;
import com.nvn.mobilent.utils.AppUtils;


import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button btnLogin;
    TextView tv_ForgotPass;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);
        setControl();
        setEvent();
    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }

    private void setEvent() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Login success
                                        FirebaseUser currentUser = auth.getCurrentUser();
                                        String userId = currentUser.getUid();

                                        // Use the userId to retrieve user data from Firestore or perform any other actions
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("users")
                                                .document(userId)
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        if (documentSnapshot.exists()) {
                                                            // User data found
                                                            User user = documentSnapshot.toObject(User.class);
                                                            assert user != null;
                                                            user.setId(userId);
                                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                            intent.putExtra("user", user);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Error occurred while retrieving user data
                                                        AppUtils.showToast_Short(getApplicationContext(), "Lấy thông tin user không thành công!");
                                                    }
                                                });

                                        AppUtils.showToast_Short(getApplicationContext(), "Đăng nhập thành công!");

                                        // Start the MainActivity or any other desired activity
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // Login failed
                                        String errorMessage = task.getException().getMessage();
                                        System.out.println(errorMessage);
                                        AppUtils.showToast_Short(getApplicationContext(), errorMessage);
                                    }
                                }
                            });
                }
            }
        });
        tv_ForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setControl() {
        email = findViewById(R.id.loginemail);
        password = findViewById(R.id.loginpassword);
        btnLogin = findViewById(R.id.btnlogin);
        email.setText("ngu@gmail.com");
        password.setText("123456");
        tv_ForgotPass = findViewById(R.id.tv_forgotpass);
        textInputLayoutEmail = findViewById(R.id.til_loginemail);
        textInputLayoutPassword = findViewById(R.id.til_loginpassword);
    }

    private boolean checkData() {
        boolean check = true;
        if (email.getText().toString().trim().equals("")) {
            textInputLayoutEmail.setError("Không được để trống");
            return false;
        } else {
            textInputLayoutEmail.setError(null);
        }
        if (password.getText().toString().trim().equals("")) {
            textInputLayoutPassword.setError("Không được để trống");
            return false;
        } else {
            textInputLayoutPassword.setError(null);
        }

        // Check TextInput null
        if (!(textInputLayoutEmail.getError() == null)) {
            return false;
        }
        if (!(textInputLayoutPassword.getError() == null)) {
            return false;
        }
        return check;
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
       /* overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);*/
    }

}