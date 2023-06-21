package com.nvn.homely.screens.category;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nvn.homely.R;
import com.nvn.homely.screens.product.ProductDetailActivity;
import com.nvn.homely.data.adapter.ItemCategoryAdapter;
import com.nvn.homely.data.model.product.Product;
import com.nvn.homely.screens.cart.CartActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String idCate;
    String nameCate;
    ItemCategoryAdapter itemCategoryAdapter;
    ArrayList<Product> productArrayList;


    View footerView;
    boolean isLoading = false;
    boolean limitData = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        setControl();
        idCate = getIntent().getStringExtra("idCate");
        nameCate = getIntent().getStringExtra("nameCate");
        toolbar.setTitle(nameCate);

        actionToolBar();
        getItemCategory(idCate);
        getMoreItemCategory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.cartmenu: {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void getMoreItemCategory() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                intent.putExtra("product", productArrayList.get(i));
                startActivity(intent);
            }
        });
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbar_itemcategory);
        listView = findViewById(R.id.listview_itemcategory);
        productArrayList = new ArrayList<>();

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

    private void getItemCategory(String cateId) {

        System.out.println(idCate);
        db.collection("products")
                .whereEqualTo("cateId", cateId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Product> productList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = document.toObject(Product.class);
                                if (product.getStatus()) {
                                    product.setId(document.getId());
                                    productList.add(product);
                                }
                            }
                            if (productList.size() > 0) {
                                listView.removeFooterView(footerView);
                                productArrayList.addAll(productList);
                                itemCategoryAdapter = new ItemCategoryAdapter(getApplicationContext(), productArrayList);
                                listView.setAdapter(itemCategoryAdapter);
                                itemCategoryAdapter.notifyDataSetChanged();
                            } else {
                                limitData = true;
                                listView.removeFooterView(footerView);
                            }
                        } else {
                            Log.d("Firebase", "Error getting items: ", task.getException());
                        }
                    }
                });
    }
}