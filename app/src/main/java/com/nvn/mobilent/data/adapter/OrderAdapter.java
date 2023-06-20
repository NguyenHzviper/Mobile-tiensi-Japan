package com.nvn.mobilent.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.nvn.mobilent.R;
import com.nvn.mobilent.screens.orders.OrderActivity;
import com.nvn.mobilent.data.model.order.Order;

import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter<Order> {
    Context context;
    int resource;

    ArrayList<Order> orderArrayList;

    public OrderAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Order> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.orderArrayList = objects;
    }
    @Override
    public int getCount() {
        return orderArrayList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        TextView dateOder = convertView.findViewById(R.id.tvDate);
        TextView address = convertView.findViewById(R.id.tv_address);
        TextView statusOrder = convertView.findViewById(R.id.statusorder);
        /*TextView price = convertView.findViewById(R.id.tv_price_order);*/
        TextView cancelOrder = convertView.findViewById(R.id.cancelorder);
        Order order = orderArrayList.get(position);
        dateOder.setText(order.getBuyDate());
        address.setText(order.getDeliveryAddress());
        /*price.setText(String.valueOf(order.getTotal()) );*/
//        if (order.getStatus()){
//            statusOrder.setText("Chưa xử lý");
//            cancelOrder.setVisibility(View.VISIBLE);
//        }else {
//            statusOrder.setText("Đơn đã huỷ");
//            cancelOrder.setVisibility(View.INVISIBLE);
//        }
        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusOrder.setText("Đơn đã huỷ");
               OrderActivity.cancelOrder(order.getId());
                cancelOrder.setVisibility(View.INVISIBLE);
           }
        });
        return convertView;
    }
}
