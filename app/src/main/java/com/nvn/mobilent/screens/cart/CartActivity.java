package com.nvn.mobilent.screens.cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nvn.mobilent.R;
import com.nvn.mobilent.data.adapter.CartAdapter;
import com.nvn.mobilent.data.datalocal.DataLocalManager;
import com.nvn.mobilent.data.model.cart.ACart;
import com.nvn.mobilent.data.model.cart.Cart;
import com.nvn.mobilent.data.model.product.Product;
import com.nvn.mobilent.data.model.user.User;
import com.nvn.mobilent.utils.AppUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    public static String newIDCart;
    static TextView tv_TotalCart, btnThanhToan, total;
    private static User user;
    static ListView lvCart;
    static CartAdapter cartAdapter;
    static TextView tv_NoticeCart;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    Toolbar toolbar;

    public static ArrayList<ACart> cartArrayList;

    public static void putCartItem(String cartItem_id, int quantity) {
        System.out.println("putCartItem:" + cartItem_id + "|" + quantity + "|");

        // Update the cart item quantity in Firestore
        db.collection("cart").document(cartItem_id).update("quantity", quantity)
                .addOnSuccessListener(documentReference -> {
                    AppUtils.showToast_Short(tv_TotalCart.getContext(), "Đã cập nhật sản phẩm vào giỏ hàng!");
                    addCart(null);
                })
                .addOnFailureListener(e -> {
                    // Handle the failure
                });
    }

    public static void postCartItem(String prod_id, String image, String name, int quantity, String userid) {
        // Create a new cart item in Firestore
        DocumentReference cartRef = db.collection("cart").document();
        String cartId = cartRef.getId();

        cartRef.set(new Cart(cartId, prod_id, image, name, quantity, userid))
                .addOnSuccessListener(aVoid -> {
                    AppUtils.showToast_Short(tv_TotalCart.getContext(), "Đã thêm sản phẩm vào giỏ hàng!");
                    newIDCart = cartId;
                })
                .addOnFailureListener(e -> {
                    // Handle the failure
                });
    }


    public static void deleteCartItem(String id) {
        // Delete the cart item from Firestore
        System.out.println(id);
        db.collection("cart").document(id).delete()
                .addOnSuccessListener(documentReference -> {
                    AppUtils.showToast_Short(tv_TotalCart.getContext(), "Đã xoá sản phẩm khỏi giỏ hàng!");
                    addCart(null);
                })
                .addOnFailureListener(e -> AppUtils.showToast_Short(tv_TotalCart.getContext(), "Lỗi xoá!"));
    }

    public static void update() {
        if (cartArrayList.size() > 0) {
            lvCart.setVisibility(View.VISIBLE);
            tv_NoticeCart.setVisibility(View.INVISIBLE);
        } else {
            lvCart.setVisibility(View.INVISIBLE);
            tv_NoticeCart.setVisibility(View.VISIBLE);
        }
        cartAdapter.notifyDataSetChanged();
        //catchOnItemListView();
    }

    public static void loadCartArrayList() {
        // Fetch the cart items from Firestore for the current user
        db.collection("cart").whereEqualTo("userId", user.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Cart> arrs = new ArrayList<>(queryDocumentSnapshots.toObjects(Cart.class));
                    for (Cart item : arrs) {
                        item.setId(queryDocumentSnapshots.getDocuments().get(arrs.indexOf(item)).getId());
                        // Fetch the corresponding product for each cart item
                        db.collection("products").document(item.getProdId()).get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    Product product = documentSnapshot.toObject(Product.class);
                                    assert product != null;
                                    ACart aCart = new ACart(item, product.getPrice());
                                    addCart(aCart);
                                })
                                .addOnFailureListener(e -> Log.d("ERROR: ", e.toString()));
                    }
                })
                .addOnFailureListener(e -> Log.d("ERROR: ", e.toString()));
        cartAdapter.notifyDataSetChanged();
        update();
    }

    public static void addCart(ACart aCart) {
        int money = 0;
        if (aCart != null) {
            cartArrayList.add(aCart);
            cartAdapter.notifyDataSetChanged();
        }

        for (ACart item : cartArrayList) {
            money += item.getQuantity() * item.getPrice();
        }
        DecimalFormat df = new DecimalFormat("###,###,###");
        tv_TotalCart.setText(df.format(money) + "đ");
        total.setText(df.format(money) + "đ");
        update();
    }

    public static void deleteItem(int position) {
        if (cartArrayList.get(position).getId() == null) {
            deleteCartItem(newIDCart);
            addCart(null);
//              loadListCart();
        } else {
            deleteCartItem(cartArrayList.get(position).getId());
            //    loadListCart();
            addCart(null);
        }
        cartArrayList.remove(position);
        AppUtils.showToast_Short(tv_TotalCart.getContext(), "Đã xoá sản phẩm ra khỏi giỏ hàng");
        cartAdapter.notifyDataSetChanged();
    }

    public static void catchOnItemListView() {
        lvCart.setOnItemLongClickListener((adapterView, view, position, l) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(lvCart.getContext());
            builder.setTitle("Xác nhận xoá sản phẩm");
            builder.setMessage("Bạn có chắc xoá sản phẩm này?");
            builder.setPositiveButton("Có", (dialogInterface, i) -> {
                if (cartArrayList.size() <= 0) {
                    tv_NoticeCart.setVisibility(View.VISIBLE);
                } else {
                    // Xử lý ArrayCart
                    if (cartArrayList.get(position).getId() == null) {
                        deleteCartItem(newIDCart);
                    } else {
                        deleteCartItem(cartArrayList.get(position).getId());
                    }
                    cartArrayList.remove(position);
                    AppUtils.showToast_Short(lvCart.getContext(), "Đã xoá sản phẩm khỏi giỏ hàng");
                    cartAdapter.notifyDataSetChanged();
                    if (cartArrayList.size() <= 0) {
                        tv_NoticeCart.setVisibility(View.VISIBLE);
                    } else {
                        tv_NoticeCart.setVisibility(View.INVISIBLE);
                        cartAdapter.notifyDataSetChanged();
                    }
                    //                loadListCart();
                }
            });
            builder.setNegativeButton("Không", (dialogInterface, i) -> {
                cartAdapter.notifyDataSetChanged();
            });
            builder.show();
            return true;
        });
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    private void setControl() {
        lvCart = findViewById(R.id.listviewcart);
        tv_NoticeCart = findViewById(R.id.tv_noticecart);
        tv_TotalCart = findViewById(R.id.totalcart);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        toolbar = findViewById(R.id.toolbarcart);
        total = findViewById(R.id.total);

        cartArrayList = new ArrayList<>();
        cartAdapter = new CartAdapter(getApplicationContext(), R.layout.line_cart_item, cartArrayList);
        lvCart.setAdapter(cartAdapter);
    }

    private void setActionBar() {
        setSupportActionBar(toolbar); // hỗ trợ toolbar như actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //set nút backhome toolbar
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        user = DataLocalManager.getUser();
        setControl();
        Cart newCart = (Cart) getIntent().getSerializableExtra("newcartitem");
        setActionBar();

        loadCartArrayList();
        setEventButton();
    }

    private void setEventButton() {
        btnThanhToan.setOnClickListener(view -> {
            if (cartArrayList.size() <= 0) {
                AppUtils.showToast_Short(getApplicationContext(), "Giỏ hàng trống!");
            } else {
                finish(); // destroy activity
                System.out.println("CARTSIZE odio:" + cartArrayList.size());
                Intent intent = new Intent(getApplicationContext(), InfoCartActivity.class);
                intent.putExtra("sizecart", cartArrayList.size());
                startActivity(intent);
            }
        });
    }
}
