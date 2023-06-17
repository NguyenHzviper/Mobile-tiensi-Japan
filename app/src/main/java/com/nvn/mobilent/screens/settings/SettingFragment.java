package com.nvn.mobilent.screens.settings;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nvn.mobilent.R;
import com.nvn.mobilent.screens.login.LoginActivity;
import com.nvn.mobilent.screens.orders.OrderActivity;
import com.nvn.mobilent.data.adapter.SettingAdapter;
import com.nvn.mobilent.data.datalocal.DataLocalManager;
import com.nvn.mobilent.data.model.SettingItem;
import com.nvn.mobilent.data.model.user.User;
import com.nvn.mobilent.utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SettingFragment extends Fragment {

    private static final int MY_REQUEST_CODE = 18;
    ArrayList<SettingItem> arrayList;
    SettingAdapter settingAdapter;
    TextView tv_cartstatus, tv_logout, welcome, account_info, edit_profile_btn;
    User user;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    FloatingActionButton btnEditImage;
    ImageView avt;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ActivityResultLauncher<Intent> activityResultLauncher;

    public SettingFragment() {
    }

    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void updateAvatar(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId  = firebaseAuth.getCurrentUser().getUid();
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String avatarUrl = document.getString("avatarUrl");
                        if (avatarUrl != null) {
                            Picasso.get().load(avatarUrl)
                                    .into(avt);
                        } else {
                            Picasso.get().load(DataLocalManager.getUriImage())
                                    .into(avt);
                        }
                    } else {
                        Toast.makeText(getContext(), "User not found!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error occurred while retrieving the data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        updateAvatar();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setControl();
        setEvent();
    }

    @Override
    public void onResume() {
        System.out.println("onResume");
        user = DataLocalManager.getUser();
        welcome.setText(user.getLastname() + " " + user.getFirstname());

        super.onResume();
    }

    private void setEvent() {
        welcome.setText(user.getLastname() + " " + user.getFirstname());

        edit_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangeInfoActivity.class);
                startActivity(intent);
            }
        });

        account_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ViewAccountInfo.class);
                startActivity(i);
            }
        });

        tv_cartstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrderActivity.class);
                startActivity(intent);
            }
        });
//        tv_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openConfirmDialog();
//            }
//        });
//        btnEditImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onRequestPermission();
//            }
//
//        });

//        activityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(), (activityResult) -> {
//                    if (activityResult.getResultCode() == RESULT_OK) {
//                        Intent dataIntent = activityResult.getData();
//                        if (dataIntent != null) {
//                            Uri imageUri = dataIntent.getData();
//                            Picasso.get().load(imageUri).into(avt);
//                            uploadAvatar(imageUri);
//                        }
//                    }
//                }
//        );
    }

//    private void uploadAvatar(Uri imageUri) {
//        if (imageUri != null) {
//            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//            StorageReference avatarRef = storageRef.child("avatars").child(userId + ".jpg");
//
//            avatarRef.putFile(imageUri)
//                    .addOnSuccessListener(taskSnapshot -> {
//                        // Image uploaded successfully, retrieve the download URL
//                        avatarRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                            // Update the user's avatar URL in Firebase Firestore or Realtime Database
//                            // For example, if using Firestore:
//                            FirebaseFirestore.getInstance()
//                                    .collection("users")
//                                    .document(userId)
//                                    .update("avatarUrl", uri.toString())
//                                    .addOnSuccessListener(aVoid -> {
//                                        // Avatar URL updated successfully
//                                        // làm thinh, làm mình làm mẫy  :))
//
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        // Handle the failure
//                                        Toast.makeText(getContext(), "Failed to update avatar", Toast.LENGTH_SHORT).show();
//                                    });
//                        });
//                    })
//                    .addOnFailureListener(e -> {
//                        // Handle the failure
//                        Toast.makeText(getContext(), "Failed to upload avatar", Toast.LENGTH_SHORT).show();
//                    });
//        }
//    }
//
//    private void openConfirmDialog() {
//        com.apachat.loadingbutton.core.customViews.CircularProgressButton btnDongY, btnHuy;
//        final Dialog dialog = new Dialog(getContext());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.layout_dialog_confirm);
//
//        btnDongY = dialog.findViewById(R.id.btnDongY);
//        btnHuy = dialog.findViewById(R.id.btnHuy);
//
//        Window window = dialog.getWindow();
//        if (window == null) {
//            return;
//        } else {
//            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            WindowManager.LayoutParams windowAttributes = window.getAttributes();
//            windowAttributes.gravity = Gravity.CENTER;
//            window.setAttributes(windowAttributes);
//            dialog.setCancelable(false);
//            dialog.show();
//            btnDongY.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    FirebaseAuth.getInstance().signOut();
//                    Intent intent = new Intent(getContext(), LoginActivity.class);
//                    startActivity(intent);
//                }
//            });
//            btnHuy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                }
//            });
//        }
//
//    }
//
    private void setControl() {
//        tv_pass = getView().findViewById(R.id.changepass);
        tv_cartstatus = getView().findViewById(R.id.cartstatus);
//        tv_logout = getView().findViewById(R.id.logout);
        welcome = getView().findViewById(R.id.welcome);
        user = DataLocalManager.getUser();
        account_info = getView().findViewById(R.id.account_info);
//        btnEditImage = getView().findViewById(R.id.btnEditImage);

        edit_profile_btn = getView().findViewById(R.id.editPersonalInfo);
        avt = getView().findViewById(R.id.avt);
    }
//
//    private void pickImageFromGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, IMAGE_PICK_CODE);
//    }
//
//
//    private void onRequestPermission() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return;
//        }
//        if (getActivity().checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            openGallery();
//        } else {
//            String[] permission = {READ_EXTERNAL_STORAGE};
//            requestPermissions(permission, MY_REQUEST_CODE);
//        }
//    }
//
//    private void openGallery() {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        activityResultLauncher.launch(Intent.createChooser(intent, "Mời lựa chọn ảnh"));
//    }

}
