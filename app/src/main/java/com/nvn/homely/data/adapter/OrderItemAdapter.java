package com.nvn.homely.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nvn.homely.R;
import com.nvn.homely.data.model.order.ListOrderItem;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class OrderItemAdapter extends ArrayAdapter<ListOrderItem> {
    Context context;
    int resource;
    ArrayList<ListOrderItem> orderItemArrayList;

    public OrderItemAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ListOrderItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.orderItemArrayList = objects;
    }

    @Override
    public int getCount() {
        return orderItemArrayList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        DecimalFormat df = new DecimalFormat("###,###,###");
        TextView name = convertView.findViewById(R.id.nameorderdetail);
        TextView price = convertView.findViewById(R.id.priceorderdetail);
        TextView amount = convertView.findViewById(R.id.amountorderdetail);
        ImageView image = convertView.findViewById(R.id.imageorderdetail);
        TextView money = convertView.findViewById(R.id.moneyorderdetail);
        ListOrderItem itemOrder = orderItemArrayList.get(position);
        name.setText(itemOrder.getName());
        price.setText(df.format(itemOrder.getPrice()) + "");
        amount.setText(itemOrder.getQuantity() + "");
        money.setText(df.format(itemOrder.getQuantity() * itemOrder.getPrice()) + "");
        Picasso.get().load(itemOrder.getImage())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image1)
                .into(image);
        return convertView;
    }
}
