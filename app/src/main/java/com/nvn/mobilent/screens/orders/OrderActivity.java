package com.nvn.mobilent.screens.orders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nvn.mobilent.R;
import com.nvn.mobilent.data.adapter.OrderAdapter;
import com.nvn.mobilent.data.datalocal.DataLocalManager;
import com.nvn.mobilent.data.model.order.Order;
import com.nvn.mobilent.data.model.user.User;
import com.nvn.mobilent.utils.AppUtils;
import java.util.ArrayList;
import java.util.List;



public class OrderActivity extends AppCompatActivity {
    ListView listOrder;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static ArrayList<Order> orderArrayList;
    static OrderAdapter orderAdapter;
    User user;

    Toolbar toolbar;
    View footerView;


    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setControl();
        actionToolBar();
        setEvent();
    }

    private void setEvent() {
        getOrderbyUserId();
        getMoreItemCategory();
    }

    private void setControl() {
        orderArrayList = new ArrayList<>();
        listOrder = findViewById(R.id.listOrder);
        toolbar = findViewById(R.id.toolbar_order);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView = inflater.inflate(R.layout.processbar, null);
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

    public static void cancelOrder(String id) {
        db.collection("orders")
                .document(id)
                .update("status", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppUtils.showToast_Short(orderAdapter.getContext(), "Đã huỷ đơn hàng thành công!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AppUtils.showToast_Short(orderAdapter.getContext(), "Lỗi huỷ đơn hàng!");
                    }
                });
    }


    private void getOrderbyUserId() {
        user = DataLocalManager.getUser();
        db.collection("orders").whereEqualTo("userId", user.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        listOrder.removeFooterView(footerView);
                        List<Order> orders = queryDocumentSnapshots.toObjects(Order.class);
                        for (int i = 0; i < orders.size(); i++) {
                            String orderId = queryDocumentSnapshots.getDocuments().get(i).getId();
                            orders.get(i).setId(orderId);
                        }
                        orderArrayList.addAll(orders);
                        orderAdapter = new OrderAdapter(getApplicationContext(), R.layout.line_order, orderArrayList);
                        listOrder.setAdapter(orderAdapter);
                        orderAdapter.notifyDataSetChanged();
                    } else {
                        listOrder.removeFooterView(footerView);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the failure
                });
    }


    private void getMoreItemCategory() {
        listOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), OrderDetailActivity.class);
                intent.putExtra("order", orderArrayList.get(i));
                startActivity(intent);
            }
        });


    }





}