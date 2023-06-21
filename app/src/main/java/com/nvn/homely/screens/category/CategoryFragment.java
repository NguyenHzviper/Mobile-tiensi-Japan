package com.nvn.homely.screens.category;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nvn.homely.R;
import com.nvn.homely.screens.cart.CartActivity;
import com.nvn.homely.data.adapter.CategoryAdapter;
import com.nvn.homely.data.model.category.Category;

import com.nvn.homely.utils.AppUtils;

import java.util.ArrayList;


public class CategoryFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView listViewCategory;
    public ArrayList<Category> categoryArrayList;
    CategoryAdapter categoryAdapter;

    ImageView reportCategory;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            System.out.println("OK");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.cart_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.cartmenu: {
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void setControl() {
        listViewCategory = requireView().findViewById(R.id.listviewcategory);
        reportCategory = requireView().findViewById(R.id.reportcategory);
        categoryArrayList = new ArrayList<>();
    }

    private void getCategory() {

        CollectionReference categoriesRef = db.collection("categories");

        categoriesRef.whereEqualTo("status", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Category> categoryArrayList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Category category = document.toObject(Category.class);
                        category.setId(document.getId());
                        categoryArrayList.add(category);
                    }

                    listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (!AppUtils.haveNetworkConnection(requireContext())) {
                                AppUtils.showToast_Short(getContext(), "Kiểm tra lại kết nối Internet");
                            } else {
                                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                                intent.putExtra("idCate", categoryArrayList.get(i).getId());
                                intent.putExtra("nameCate", categoryArrayList.get(i).getName());
                                startActivity(intent);
                            }
                        }
                    });

                    categoryAdapter = new CategoryAdapter(getContext(), categoryArrayList);
                    listViewCategory.setAdapter(categoryAdapter);
                } else {
                    Log.e("CategoryFetch", "Error getting categories: " + task.getException());
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl();
        setEvent();
        if (!AppUtils.haveNetworkConnection(getContext())) {
            AppUtils.showToast_Short(getContext(), "Kiểm tra lại kết nối Internet");
        } else {
            getCategory();
        }
    }

    private void setEvent() {
        reportCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void lisenCategory() {
        listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (!AppUtils.haveNetworkConnection(getContext())) {
                    AppUtils.showToast_Short(getContext(), "Kiểm tra lại kết nối Internet");
                } else {
                    Intent intent = new Intent(getActivity(), CategoryActivity.class); //CategoryFragment.this.getActivity()
                    intent.putExtra("idCate", String.valueOf(categoryAdapter.getItemId(i)));
                    startActivity(intent);
                }
            }
        });
    }


}