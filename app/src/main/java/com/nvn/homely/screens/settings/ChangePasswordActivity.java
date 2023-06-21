package com.nvn.homely.screens.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nvn.homely.R;
import com.nvn.homely.data.datalocal.DataLocalManager;

import com.nvn.homely.data.model.user.User;

import com.nvn.homely.utils.AppUtils;



public class ChangePasswordActivity extends AppCompatActivity {
    EditText oldPass, newPass, newRePass;
    TextView btnChangePass;

    Toolbar toolbar;
    private TextInputLayout textInputLayoutOldPass;
    private TextInputLayout textInputLayoutPass;
    private TextInputLayout textInputLayoutRepass;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        user = DataLocalManager.getUser();
        setControl();
        setEvent();
        actionToolBar();
    }

    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //tạo nút home

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setEvent() {
        if (!AppUtils.haveNetworkConnection(getApplicationContext())) {
            AppUtils.showToast_Short(getApplicationContext(), "Kiểm tra lại kết nối Internet");
        } else {
            btnChangePass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkData()) {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (firebaseUser != null) {
                            String email = firebaseUser.getEmail();
                            String oldPassword = oldPass.getText().toString().trim();
                            String newPassword = newPass.getText().toString().trim();

                            AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);
                            firebaseUser.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                firebaseUser.updatePassword(newPassword)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    AppUtils.showToast_Short(getApplicationContext(), "Cập nhật mật khẩu thành công!");
                                                                    finish();
                                                                } else {
                                                                    AppUtils.showToast_Short(getApplicationContext(), "Có lỗi xảy ra. Vui lòng thử lại!");
                                                                }
                                                            }
                                                        });
                                            } else {
                                                AppUtils.showToast_Short(getApplicationContext(), "Mật khẩu hiện tại không đúng!");
                                            }
                                        }
                                    });
                        } else {
                            // User is not logged in
                            AppUtils.showToast_Short(getApplicationContext(), "Người dùng chưa đăng nhập!");
                        }
                    }
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
    }

    private boolean checkData() {
        boolean check = true;
        if (oldPass.getText().toString().trim().equals("")) {
            textInputLayoutOldPass.setError("Không được để trống");
            return false;
        } else {
            textInputLayoutOldPass.setError(null);
        }
        if (newPass.getText().toString().trim().equals("")) {
            textInputLayoutPass.setError("Không được để trống");
            return false;
        } else {
            textInputLayoutPass.setError(null);
        }

        if (newRePass.getText().toString().trim().equals("")) {
            textInputLayoutRepass.setError("Không được để trống");
            return false;
        } else {
            textInputLayoutRepass.setError(null);
        }
        if (!newPass.getText().toString().trim().equals(newRePass.getText().toString().trim())) {
            textInputLayoutRepass.setError("Mật khẩu không trùng khớp");
            return false;
        } else {
            textInputLayoutRepass.setError(null);
        }

        // Check TextInput null
        if (!(textInputLayoutOldPass.getError() == null)) {
            return false;
        }
        if (!(textInputLayoutPass.getError() == null)) {
            return false;
        }
        if (!(textInputLayoutRepass.getError() == null)) {
            return false;
        }
        return check;
    }

    private void setControl() {
        oldPass = findViewById(R.id.oldpass);
        newPass = findViewById(R.id.newpass);
        newRePass = findViewById(R.id.renewpass);
        btnChangePass = findViewById(R.id.btnchangepass);
        toolbar = findViewById(R.id.toolbar_changepass);

        textInputLayoutOldPass = findViewById(R.id.til_changepass1);
        textInputLayoutPass = findViewById(R.id.til_changepass2);
        textInputLayoutRepass = findViewById(R.id.til_changepass3);
    }


}