package com.nvn.homely.screens.settings;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nvn.homely.R;
import com.nvn.homely.data.datalocal.DataLocalManager;
import com.nvn.homely.data.model.user.Info;
import com.nvn.homely.data.model.user.User;

import com.nvn.homely.utils.AppUtils;

import java.util.HashMap;
import java.util.Map;


public class ChangeInfoActivity extends AppCompatActivity {
    EditText firstName, lastName, birthday_d, birthday_m, birthday_y, address, phone, email;
    TextView btnChangeInfo;
    Toolbar toolbar;

    private TextInputLayout textInputLayoutFirstName, textInputLayoutLastName, textInputLayoutBirthDay_d,
            textInputLayoutBirthDay_m, textInputLayoutBirthDay_y,
            textInputLayoutAddress, textInputLayoutPhone;


    private String regexName = "[aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆ fFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTu UùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ]+";
    private String regexEmail = "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$";
    private String regexPhone = "[0]{1}\\d{9}";
    private User user;
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        user = DataLocalManager.getUser();
        setControl();
        actionToolBar();
        loadDefault();
        catchData();
        setEvent();
    }

    private void updateUser(User info){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String collectionPath = "users";
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("email", info.getEmail());
        updatedFields.put("firstname", info.getFirstname());
        updatedFields.put("lastname", info.getLastname());
        updatedFields.put("address", info.getAddress());
        updatedFields.put("phone", info.getPhone());
        updatedFields.put("birthday", info.getBirthday());

        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection(collectionPath)
                    .document(userId)
                    .update(updatedFields)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Update successful
                            // Perform any desired actions after updating the document
                            AppUtils.showToast_Short(getApplicationContext(), "Cập nhật thông tin thành công!");
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Update failed
                            // Handle the failure case
                            AppUtils.showToast_Short(getApplicationContext(), "Lỗi cập nhật thông tin rồi");
                        }
                    });
        }
    }

    private void setEvent() {
        btnChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    String day = birthday_d.getText().toString();
                    String month = birthday_m.getText().toString();
                    String year = birthday_y.getText().toString();
                    String birthday = String.format("%s-%s-%s", day, month, year);
                    System.out.println(birthday);
                    Info info = new Info(
                            user.getId(),
                            user.getEmail(),
                            firstName.getText().toString().trim(),
                            lastName.getText().toString().trim(),
                            address.getText().toString().trim(),
                            phone.getText().toString().trim(),
                            user.getGender(),
                            birthday
                    );

                    if (!AppUtils.haveNetworkConnection(getApplicationContext())) {
                        AppUtils.showToast_Short(getApplicationContext(), "Kiểm tra lại kết nối Internet");
                    } else {
                        user.setEmail(info.getEmail());
                        user.setFirstname(info.getFirstname());
                        user.setLastname(info.getLastname());
                        user.setAddress(info.getAddress());
                        user.setPhone(info.getPhone());
                        user.setBirthday(info.getBirthday());
                        DataLocalManager.setUser(user);

                        updateUser(user);

                        loadDefault();
                    }
                }
            }
        });
    }

    private void loadDefault() {
        String[] birthday = user.getBirthday().split("-");

        birthday_d.setText(birthday[0]);
        birthday_m.setText(birthday[1]);
        birthday_y.setText(birthday[2]);

        firstName.setText(user.getFirstname());
        lastName.setText(user.getLastname());
        address.setText(user.getAddress());
        phone.setText(user.getPhone());
        email.setText(user.getEmail());
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbar_changeinfo);
        firstName = findViewById(R.id.changefname);
        lastName = findViewById(R.id.changelname);

        birthday_d = findViewById(R.id.changebirthday_d);
        birthday_m = findViewById(R.id.changebirthday_m);
        birthday_y = findViewById(R.id.changebirthday_y);

        address = findViewById(R.id.changeaddress);
        phone = findViewById(R.id.changephone);
        email = findViewById(R.id.changeemail);

        btnChangeInfo = findViewById(R.id.btn_update_user_info);

        textInputLayoutFirstName = findViewById(R.id.til_changefname);
        textInputLayoutLastName = findViewById(R.id.til_changelname);
        textInputLayoutBirthDay_d = findViewById(R.id.til_changebirthday_d);
        textInputLayoutBirthDay_y = findViewById(R.id.til_changebirthday_m);
        textInputLayoutBirthDay_m = findViewById(R.id.til_changebirthday_y);
        textInputLayoutAddress = findViewById(R.id.til_changeaddress);
        textInputLayoutPhone = findViewById(R.id.til_changephone);
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

    private boolean checkData() {
        boolean check = true;
        if (firstName.getText().toString().trim().equals("")) {
            textInputLayoutFirstName.setError("Không được để trống!");
            return false;
        }
        if (lastName.getText().toString().trim().equals("")) {
            textInputLayoutLastName.setError("Không được để trống!");
            return false;
        }
        if (address.getText().toString().trim().equals("")) {
            textInputLayoutAddress.setError("Không được để trống!");
            return false;
        }
        if (phone.getText().toString().trim().equals("")) {
            textInputLayoutPhone.setError("Không được để trống!");
            return false;
        }

        // Check TextInput null
        if (!(textInputLayoutFirstName.getError() == null)) {
            return false;
        }
        if (!(textInputLayoutLastName.getError() == null)) {
            return false;
        }

        if (!(textInputLayoutBirthDay_d.getError() == null)) {
            return false;
        }

        if (!(textInputLayoutBirthDay_m.getError() == null)) {
            return false;
        }

        if (!(textInputLayoutBirthDay_y.getError() == null)) {
            return false;
        }
        if (!(textInputLayoutAddress.getError() == null)) {
            return false;
        }
        if (!(textInputLayoutPhone.getError() == null)) {
            return false;
        }
        return check;
    }

    private void catchData() {
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty() && !charSequence.toString().matches(regexName)) {
                    textInputLayoutFirstName.setError("Chỉ dùng các chữ cái");
                } else {
                    textInputLayoutFirstName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty() && !charSequence.toString().matches(regexName)) {
                    textInputLayoutLastName.setError("Chỉ dùng các chữ cái");
                } else {
                    textInputLayoutLastName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // Address chữ và số
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()
                        && !charSequence.toString().matches(regexPhone)) {
                    textInputLayoutPhone.setError("Số điện thoại gồm 10 số và bắt đầu bằng 0");
                } else {
                    textInputLayoutPhone.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

}