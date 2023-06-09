package com.nvn.homely.data.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.nvn.homely.R;
import com.nvn.homely.screens.orders.OrderActivity;
import com.nvn.homely.data.model.order.Order;

import java.text.DecimalFormat;
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
        TextView cancelOrder = convertView.findViewById(R.id.cancelorder);
        TextView price = convertView.findViewById(R.id.tv_price_order);
        Order order = orderArrayList.get(position);
        dateOder.setText(order.getBuyDate());
        address.setText(order.getDeliveryAddress());
        DecimalFormat df = new DecimalFormat("###,###,###");

        if (order.getStatus()){
            statusOrder.setText("Chưa xử lý");
            statusOrder.setTextColor(Color.BLUE);
            cancelOrder.setVisibility(View.VISIBLE);
        }else {
            statusOrder.setText("Đơn đã huỷ");
            statusOrder.setTextColor(Color.RED);
            cancelOrder.setVisibility(View.INVISIBLE);
        }
        price.setText(df.format(order.getMoney())+ "đ");
        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusOrder.setText("Đơn đã huỷ");
                statusOrder.setTextColor(Color.RED);
               OrderActivity.cancelOrder(order.getId());
                cancelOrder.setVisibility(View.INVISIBLE);
           }
        });
        return convertView;
    }
}
