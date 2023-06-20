package com.nvn.mobilent.screens.cart;

import android.app.Notification;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.nvn.mobilent.R;
import com.nvn.mobilent.data.base.NotificationApp;

import com.nvn.mobilent.data.datalocal.DataLocalManager;
import com.nvn.mobilent.data.model.cart.Cart;

import com.nvn.mobilent.data.model.product.Product;
import com.nvn.mobilent.data.model.user.User;

import com.nvn.mobilent.utils.AppUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoCartActivity extends AppCompatActivity {
    static  FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText recipientName, phone, address;
    Button btnDatHang;
    User user;
    Toolbar toolbar;
    int sizeCart;
    private String regexName = "[aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆ fFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ]+";
    private String regexPhone = "[0]\\d{9}";

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutPhone;
    private TextInputLayout textInputLayoutAddress;

    public static void deleteAllCart(String userId) {
        db.collection("cart")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                    WriteBatch batch = db.batch();
                    for (DocumentSnapshot document : documents) {
                        batch.delete(document.getReference());
                    }
                    batch.commit()
                            .addOnSuccessListener(aVoid -> System.out.println("Deleted all cart items for user: " + userId))
                            .addOnFailureListener(e -> {
                                // Handle the failure
                            });
                })
                .addOnFailureListener(e -> {
                    // Handle the failure
                });
    }


    @Override
    public void onBackPressed() {
    }

    private void catchData() {
        recipientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty() && !charSequence.toString().matches(regexName)) {
                    textInputLayoutName.setError("Chỉ dùng các chữ cái");
                } else {
                    textInputLayoutName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty() && !charSequence.toString().matches(regexPhone)) {
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

    private boolean checkData() {
        boolean check = true;
        if (recipientName.getText().toString().trim().equals("")) {
            textInputLayoutName.setError("Không được để trống!");
            return false;
        }
        if (address.getText().toString().trim().equals("")) {
            textInputLayoutAddress.setError("Không được để trống!");
            return false;
        }
        if (phone.getText().toString().trim().equals("")) {
            textInputLayoutPhone.setError("Không được để trống!");
            return false;
        } else {
            textInputLayoutPhone.setError(null);
        }

        // Check TextInput null
        if (!(textInputLayoutName.getError() == null)) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_cart);
        user = DataLocalManager.getUser();
        setControl();
        recipientName.setText(user.getLastname() + " " + user.getFirstname());
        address.setText(user.getAddress());
        phone.setText(user.getPhone());
        catchData();
        setEvent();
        actionToolBar();
    }

    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //tạo nút home
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void setEvent() {
        if (!AppUtils.haveNetworkConnection(getApplicationContext())) {
            AppUtils.showToast_Short(getApplicationContext(), "Kiểm tra lại kết nối Internet");
        } else {
            btnDatHang.setOnClickListener(view -> {
                String name = recipientName.getText().toString().trim();
                String sdt = phone.getText().toString().trim();
                String diaChi = address.getText().toString().trim();
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = dateFormat.format(currentDate);

                if (checkData()) {
                    Map<String, Object> orderData = new HashMap<>();
                    orderData.put("userId", user.getId());
                    orderData.put("deliveryAddress", diaChi);
                    orderData.put("phone", sdt);
                    orderData.put("recipientName", name);
                    orderData.put("status", true);
                    orderData.put("buyDate", formattedDate);

                    db.collection("orders")
                            .add(orderData)
                            .addOnSuccessListener(documentReference -> {
                                String idOrder = documentReference.getId();
                                db.collection("cart")
                                        .whereEqualTo("userId", user.getId())
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                            if (!queryDocumentSnapshots.isEmpty()) {
                                                List<Cart> cartItems = queryDocumentSnapshots.toObjects(Cart.class);
                                                for (Cart cart : cartItems) {

                                                    db.collection("products")
                                                            .document(cart.getProdId())
                                                            .get()
                                                            .addOnSuccessListener(documentSnapshot -> {
                                                                Product product = documentSnapshot.toObject(Product.class);
                                                                if (product != null) {
                                                                    int price = product.getPrice();


                                                                    Map<String, Object> orderDetailData = new HashMap<>();
                                                                    orderDetailData.put("quantity", cart.getQuantity());
                                                                    orderDetailData.put("prodId", cart.getProdId());
                                                                    orderDetailData.put("name", cart.getName());
                                                                    orderDetailData.put("image", cart.getImage());
                                                                    orderDetailData.put("price", price);
                                                                    orderDetailData.put("orderId", idOrder);

                                                                    db.collection("orderDetails")
                                                                            .add(orderDetailData)
                                                                            .addOnSuccessListener(documentReference1 -> System.out.println("ADDED!"));

                                                                }
                                                            });

                                                }
                                                deleteAllCart(user.getId());
                                                sendOnChannel1();
                                                finish();
                                            } else {
                                                AppUtils.showToast_Short(getApplicationContext(), "Giỏ hàng trống!");
                                            }
                                        });

                            })
                            .addOnFailureListener(e -> {
                                System.out.println("Lỗi " + e);
                            });
                } else {
                    AppUtils.showToast_Short(getApplicationContext(), "Kiểm tra lại dữ liệu nhập vào");
                }
            });
        }
    }

    private void setControl() {
        sizeCart = getIntent().getIntExtra("sizecart", 0);
        toolbar = findViewById(R.id.toolbar_infocart);
        recipientName = findViewById(R.id.txtnguoinhan);
        phone = findViewById(R.id.txtsdtnguoinhan);
        address = findViewById(R.id.txtdiachi);
        btnDatHang = findViewById(R.id.btncheckout);
        textInputLayoutName = findViewById(R.id.namepayment);
        textInputLayoutPhone = findViewById(R.id.phonepayment);
        textInputLayoutAddress = findViewById(R.id.addresspayment);

    }

    private void sendOnChannel1() {
        String title = "Mobile Shop App";
        String message = "Đơn hàng của bạn đã được đặt thành công!";

        Notification notification = new NotificationCompat.Builder(this, NotificationApp.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_ok)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        int notificationId = 1;
        NotificationManagerCompat.from(getApplicationContext())
                .notify("tag", notificationId, notification);
    }

}