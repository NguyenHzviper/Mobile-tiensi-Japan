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
import com.nvn.homely.screens.cart.CartActivity;
import com.nvn.homely.data.model.cart.ACart;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartAdapter extends ArrayAdapter<ACart> {
    Context context;
    int resource;
    ArrayList<ACart> cartArrayList;

    public CartAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ACart> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.cartArrayList = objects;
    }


    @Override
    public int getCount() {
        return cartArrayList.size();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        TextView nameCart = convertView.findViewById(R.id.tv_cart);
        TextView priceCart = convertView.findViewById(R.id.tv_pricecart);
        ImageView imageCart = convertView.findViewById(R.id.iv_cart);
        ACart cart = cartArrayList.get(position);
        TextView btnValue = convertView.findViewById(R.id.btnvalue);
        TextView btnPlus = convertView.findViewById(R.id.btnplus);
        TextView btnMinus = convertView.findViewById(R.id.btnminus);
        TextView imgDelete = convertView.findViewById(R.id.imagedeletecart);
        TextView tvtotal = convertView.findViewById(R.id.total_tv);
        TextView itemNum_tv = convertView.findViewById(R.id.itemNum_tv);

        DecimalFormat df = new DecimalFormat("###,###,###");
        priceCart.setText(df.format(cart.getPrice())+"");
        nameCart.setText(cart.getName());

        tvtotal.setText(df.format(cart.getPrice() * cart.getQuantity()) + "");
        itemNum_tv.setText(cart.getQuantity().toString());

        Picasso.get().load(cart.getImage())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.error)
                .into(imageCart);
        btnValue.setText(cart.getQuantity() + "");

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int slmoi = Integer.parseInt(btnValue.getText().toString()) + 1;
                if (slmoi <= 10 && slmoi >= 1) {
                    btnValue.setText(slmoi + "");
                    cart.setQuantity(slmoi);
                    if (cart.getId() == null) {
                        CartActivity.putCartItem(CartActivity.newIDCart, slmoi);
                    } else {
                        CartActivity.putCartItem(cart.getId(), slmoi);
                    }
                }
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int slmoi = Integer.parseInt(btnValue.getText().toString()) - 1;
                if (slmoi <= 10 && slmoi >= 1) {
                    btnValue.setText(slmoi + "");
                    cart.setQuantity(slmoi);
                    if (cart.getId() == null) {
                        CartActivity.putCartItem(CartActivity.newIDCart, slmoi);
                    } else {
                        CartActivity.putCartItem(cart.getId(), slmoi);
                    }
                }
            }
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartActivity.deleteItem(position);
            }
        });
        return convertView;
    }
}
