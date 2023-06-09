package com.nvn.homely.screens.forgot;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.nvn.homely.R;

public class EmailFragment extends Fragment {
    EditText email;
    Button btnRecovery;
    private TextInputLayout textInputLayoutEmail;
    private String regexEmail = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";

    public EmailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl();
        setEvent();
    }

    private boolean checkData() {
        boolean check = true;
        if (email.getText().toString().trim().equals("")) {
            textInputLayoutEmail.setError("Không được để trống!");
            return false;
        } else {
            textInputLayoutEmail.setError(null);
        }
        // Check TextInput null
        if (!(textInputLayoutEmail.getError() == null)) {
            return false;
        }
        return check;
    }

    private void setEvent() {
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty() && !charSequence.toString().matches(regexEmail)) {
                    textInputLayoutEmail.setError("Email có định dạng username@abc.com");
                } else {
                    textInputLayoutEmail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        btnRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    String userEmail = email.getText().toString().trim();
                    sendPasswordResetEmail(userEmail);
                }
            }
        });
    }
    private void sendPasswordResetEmail(String userEmail) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Đã gửi email khôi phục mật khẩu", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Gửi email khôi phục mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setControl() {
        email = getActivity().findViewById(R.id.recoveryemail);
        btnRecovery = getActivity().findViewById(R.id.btnrecovery);
        textInputLayoutEmail = getActivity().findViewById(R.id.til_recoveremail);
    }
}