package com.nvn.mobilent.screens.category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nvn.mobilent.R;
import com.nvn.mobilent.data.base.PathAPI;
import com.nvn.mobilent.data.base.RetrofitClient;
import com.nvn.mobilent.data.model.category.Category;
import com.nvn.mobilent.data.model.chart.ChartCategory;
import com.nvn.mobilent.data.model.chart.RChart;
import com.nvn.mobilent.data.api.CategoryAPI;
import com.nvn.mobilent.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartActivity extends AppCompatActivity {
    int sum = 0;
    BarChart barChart;
    PieChart pieChart;
    Toolbar toolbar;
    CategoryAPI categoryAPI;
    ArrayList<ChartCategory> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setControl();
        actionToolBar();
        if (!AppUtils.haveNetworkConnection(getApplicationContext())) {
            AppUtils.showToast_Short(getApplicationContext(), "Kiểm tra lại kết nối Internet");
        }
        arrayList = new ArrayList<>();
        categoryAPI = (CategoryAPI) RetrofitClient.getClient(PathAPI.linkAPI).create(CategoryAPI.class);
        setEvent();
    }

    private void actionToolBar() {
        toolbar.setTitle("    Biểu đồ sản phẩm");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //tạo nút home
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setEvent() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> categoryList = task.getResult().getDocuments();
                            ArrayList<BarEntry> barEntries = new ArrayList<>();
                            ArrayList<PieEntry> pieEntries = new ArrayList<>();

                            for (DocumentSnapshot categoryDoc : categoryList) {
                                Category category = categoryDoc.toObject(Category.class);
                                assert category != null;
                                 // Set the ID of the category
                                category.setId(String.valueOf(categoryDoc.getId()));
                                String categoryId = category.getId();
                                System.out.println(categoryId);
                                db.collection("products")
                                        .whereEqualTo("cateId", categoryId)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    int productCount = task.getResult().size();
                                                    System.out.println(productCount);
                                                    sum += productCount;
                                                    System.out.println(sum);
                                                    barEntries.add(new BarEntry(barEntries.size() + 1, (productCount * 100 / sum)));
                                                    pieEntries.add(new PieEntry(productCount * 100 / sum, category.getName()));

                                                    // Update the charts when all category documents are processed
                                                    if (barEntries.size() == categoryList.size()) {
                                                        //Initialize bar data set
                                                        BarDataSet barDataSet = new BarDataSet(barEntries, "Danh mục sản phẩm");
                                                        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                                        barDataSet.setDrawValues(false); //Hide draw value
                                                        barChart.setData(new BarData(barDataSet));
                                                        barChart.animateY(5000); //Set animation
                                                        barChart.getDescription().setText("");

                                                        //Initialize pie data set
                                                        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
                                                        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                                        pieChart.setData(new PieData(pieDataSet));
                                                        pieChart.animateXY(5000, 5000);
                                                        pieChart.getDescription().setEnabled(false);
                                                    }
                                                } else {
                                                    Log.d("Firebase", "Error getting products for category: " + category.getName(), task.getException());
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.d("Firebase", "Error getting categories: ", task.getException());
                        }
                    }
                });
    }




    private void setControl() {
        barChart = findViewById(R.id.bar_chart);
        pieChart = findViewById(R.id.pie_chart);
        toolbar = findViewById(R.id.toolbar_chart);
    }
}