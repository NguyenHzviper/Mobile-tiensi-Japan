package com.nvn.mobilent.screens.product;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nvn.mobilent.R;
import com.nvn.mobilent.data.datalocal.DataLocalManager;
import com.nvn.mobilent.data.model.cart.Cart;
import com.nvn.mobilent.data.model.product.Product;
import com.nvn.mobilent.data.model.user.User;
import com.nvn.mobilent.utils.AppUtils;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;
import java.util.List;



public class ProductDetailActivity extends AppCompatActivity {
    static Toolbar toolbar;
    Product product;

    private static User user;
    ImageView image;
    TextView name, quantity, price, detail, btnminus, btnplus;
    Button btn_addcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        user = DataLocalManager.getUser();
        setControl();
        actionToolBar();
        loadInfo();
        setEvent();
        setEventButton();
    }

    public static void postCartItem(String prod_id,String name,String image, int quantity, String userid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cartItemsRef = db.collection("cart");

        Cart newCartItem = new Cart();
        newCartItem.setProdId(prod_id);
        newCartItem.setQuantity(quantity);
        newCartItem.setUserId(userid);
        newCartItem.setName(name);
        newCartItem.setImage(image);

        cartItemsRef.add(newCartItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        AppUtils.showToast_Short(toolbar.getContext(), "Đã thêm sản phẩm vào giỏ hàng!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add cart item
                        Log.d("ERROR: ", e.toString());
                    }
                });
    }

    public static void putCartItem(String cartItem_id, int quantity) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cartItemsRef = db.collection("cart");

        cartItemsRef.document(String.valueOf(cartItem_id))
                .update("quantity", quantity)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppUtils.showToast_Short(toolbar.getContext(), "Đã cập nhật sản phẩm vào giỏ hàng!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update cart item
                        Log.d("ERROR: ", e.toString());
                    }
                });
    }

    private void setEventButton() {
        btn_addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                int amount = Integer.parseInt(quantity.getText().toString());

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference cartItemsRef = db.collection("cart");

                cartItemsRef.whereEqualTo("userId", user.getId())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot querySnapshot) {
                                List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                                boolean exist = false;

                                for (DocumentSnapshot document : documents) {
                                    Cart cartItem = document.toObject(Cart.class);
                                    assert cartItem != null;
                                    cartItem.setId(document.getId());
                                    if (cartItem.getProdId().equals(product.getId())) {
                                        cartItem.setQuantity(cartItem.getQuantity() + amount);

                                        putCartItem(cartItem.getId(), cartItem.getQuantity());
                                        exist = true;
                                    }
                                }

                                if (!exist) {
                                    postCartItem(product.getId(),product.getName(),product.getImage(), amount, user.getId());
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error retrieving cart items
                                Log.d("ERROR: ", e.toString());
                            }
                        });
            }
        });
    }


    private void loadInfo() {
        product = (Product) getIntent().getSerializableExtra("product");
        name.setText(product.getName());
        /*quantity.setText("Số lượng tồn: " + product.getQuantity());*/
        DecimalFormat df = new DecimalFormat("###,###,###");
        price.setText(df.format(product.getPrice()) + " VNĐ");
        detail.setText(product.getDetail());
        Picasso.get().load(product.getImage())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.error)
                .into(image);
    }

    private void setEvent() {
        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int slmoi = Integer.parseInt(quantity.getText().toString()) + 1;
                if (slmoi <= 10 && slmoi >= 1) {
                    quantity.setText(slmoi + "");

                }
            }
        });
        btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int slmoi = Integer.parseInt(quantity.getText().toString()) - 1;
                if (slmoi <= 10 && slmoi >= 1) {
                    quantity.setText(slmoi + "");

                }
            }
        });
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbar_productdetail);
        name = findViewById(R.id.tv_nameproductdetail);

        /*quantity = findViewById(R.id.tv_quantity);*/
        /*spinner = findViewById(R.id.spinner);*/

        TextView btnValue =findViewById(R.id.btnvalue);
        TextView btnPlus = findViewById(R.id.btnplus);
        TextView btnMinus = findViewById(R.id.btnminus);


        price = findViewById(R.id.tv_priceproduct);
        detail = findViewById(R.id.tv_productdetail);
        image = findViewById(R.id.image_productdetail);
        btn_addcart = findViewById(R.id.btn_addcart);
        btnminus = findViewById(R.id.btnminus);
        btnplus = findViewById(R.id.btnplus);
        quantity = findViewById(R.id.product_quantity);
    }

    private void actionToolBar() {
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //tạo nút home
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}