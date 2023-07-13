package com.example.personalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class MonthlyAnalyticsActivity extends AppCompatActivity {

    private Toolbar settingsToolbar;

    private FirebaseAuth mAuth;
    private String onlineUserId = "";
    private DatabaseReference expensesRef, personalRef;

    private TextView totalAmountSpendOn, analyticsTransportAmount, analyticsFoodAmount, analyticsHouseExpensesAmount,
            analyticsEntertainmentAmount, analyticsEducationAmount, analyticsCharityAmount, analyticsApparelAmount,
            analyticsHealthAmount, analyticsPersonalExpensesAmount, analyticsOtherAmount;

    private RelativeLayout linearLayoutTransport, linearLayoutFood, linearLayoutFoodHouse, linearLayoutEntertainment, linearLayoutEducation,
            linearLayoutCharity, linearLayoutApparel, linearLayoutHealth, linearLayoutPersonalExp, linearLayoutOther, linearLayoutAnalysis;
    private AnyChartView anyChartView;

    private TextView progress_ratio_transport, progress_ratio_food, progress_ratio_house, progress_ratio_ent, progress_ratio_edu,
            progress_ratio_cha, progress_ratio_app, progress_ratio_hea, progress_ratio_per, progress_ratio_oth,
            monthSpendAmount, monthRatioSpending;
    private ImageView status_Image_transport, status_Image_food, status_Image_house, status_Image_ent, status_Image_edu,
            status_Image_cha, status_Image_app, status_Image_hea, status_Image_per, status_Image_oth, monthRatioSpending_Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_analytics);

        settingsToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(settingsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Monthly Analytics");

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);

        totalAmountSpendOn = findViewById(R.id.totalAmountSpentOn);

        // general analytics
        monthSpendAmount = findViewById(R.id.monthSpendAmount);
        linearLayoutAnalysis = findViewById(R.id.linearLayoutAnalysis);
        monthRatioSpending = findViewById(R.id.monthRatioSpending);
        monthRatioSpending_Image = findViewById(R.id.monthRatioSpending_Image);

        analyticsTransportAmount = findViewById(R.id.analyticsTransportAmount);
        analyticsFoodAmount = findViewById(R.id.analyticsFoodAmount);
        analyticsHouseExpensesAmount = findViewById(R.id.analyticsHouseExpensesAmount);
        analyticsEntertainmentAmount = findViewById(R.id.analyticsEntertainmentAmount);
        analyticsEducationAmount = findViewById(R.id.analyticsEducationAmount);
        analyticsCharityAmount = findViewById(R.id.analyticsCharityAmount);
        analyticsApparelAmount = findViewById(R.id.analyticsApparelAmount);
        analyticsHealthAmount = findViewById(R.id.analyticsHealthAmount);
        analyticsPersonalExpensesAmount = findViewById(R.id.analyticsPersonalExpensesAmount);
        analyticsOtherAmount = findViewById(R.id.analyticsOtherAmount);

        // Relative layouts views
        linearLayoutTransport = findViewById(R.id.linearLayoutTransport);
        linearLayoutFood = findViewById(R.id.linearLayoutFood);
        linearLayoutFoodHouse = findViewById(R.id.linearLayoutFoodHouse);
        linearLayoutEntertainment = findViewById(R.id.linearLayoutEntertainment);
        linearLayoutEducation = findViewById(R.id.linearLayoutEducation);
        linearLayoutCharity = findViewById(R.id.linearLayoutCharity);
        linearLayoutApparel = findViewById(R.id.linearLayoutApparel);
        linearLayoutHealth = findViewById(R.id.linearLayoutHealth);
        linearLayoutPersonalExp = findViewById(R.id.linearLayoutPersonalExp);
        linearLayoutOther = findViewById(R.id.linearLayoutOther);

        // textview
        progress_ratio_transport = findViewById(R.id.progress_ratio_transport);
        progress_ratio_food = findViewById(R.id.progress_ratio_food);
        progress_ratio_house = findViewById(R.id.progress_ratio_house);
        progress_ratio_ent = findViewById(R.id.progress_ratio_ent);
        progress_ratio_edu = findViewById(R.id.progress_ratio_edu);
        progress_ratio_cha = findViewById(R.id.progress_ratio_cha);
        progress_ratio_app = findViewById(R.id.progress_ratio_app);
        progress_ratio_hea = findViewById(R.id.progress_ratio_hea);
        progress_ratio_per = findViewById(R.id.progress_ratio_per);
        progress_ratio_oth = findViewById(R.id.progress_ratio_oth);

        // imageview
        status_Image_transport = findViewById(R.id.status_Image_transport);
        status_Image_food = findViewById(R.id.status_Image_food);
        status_Image_house = findViewById(R.id.status_Image_house);
        status_Image_ent = findViewById(R.id.status_Image_ent);
        status_Image_edu = findViewById(R.id.status_Image_edu);
        status_Image_cha = findViewById(R.id.status_Image_cha);
        status_Image_app = findViewById(R.id.status_Image_app);
        status_Image_hea = findViewById(R.id.status_Image_hea);
        status_Image_per = findViewById(R.id.status_Image_per);
        status_Image_oth = findViewById(R.id.status_Image_oth);

        // anyChartView
        anyChartView = findViewById(R.id.anyChartView);

        getTotalWeekTransportExpense();
        getTotalMonthsFoodExpenses();
        getTotalMonthsHouseExpenses();
        getTotalMonthsEntertainmentExpenses();
        getTotalWeekEducationExpenses();
        getTotalWeekCharityExpenses();
        getTotalWeekApparelExpenses();
        getTotalWeekHealthExpenses();
        getTotalWeekPersonalExpenses();
        getTotalWeekOtherExpenses();
        getTotalWeekSpending();

        new java.util.Timer().schedule(new TimerTask() {
           @Override
              public void run() {
                loadGraph();
                setStatusAndImageResource();
              }
           },2000
        );
    }

    private void getTotalWeekTransportExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "Transport"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount+=pTotal;
                        analyticsTransportAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("monthTrans").setValue(totalAmount);
                }
                else {
                    linearLayoutTransport.setVisibility(View.GONE);
                    personalRef.child("monthTrans").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalMonthsFoodExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "Food"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsFoodAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("monthFood").setValue(totalAmount);
                }
                else {
                    linearLayoutFood.setVisibility(View.GONE);
                    personalRef.child("monthFood").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalMonthsHouseExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "House Expenses"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsHouseExpensesAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("monthHouse").setValue(totalAmount);
                }
                else {
                    linearLayoutFoodHouse.setVisibility(View.GONE);
                    personalRef.child("monthHouse").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void getTotalMonthsEntertainmentExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "Entertainment"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsEntertainmentAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("monthEnt").setValue(totalAmount);
                }
                else {
                    linearLayoutEntertainment.setVisibility(View.GONE);
                    personalRef.child("monthEnt").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void getTotalWeekEducationExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "Education"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsEducationAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("monthEdu").setValue(totalAmount);
                }
                else {
                    linearLayoutEducation.setVisibility(View.GONE);
                    personalRef.child("monthEdu").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void getTotalWeekCharityExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "Charity"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsCharityAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("monthCha").setValue(totalAmount);
                }
                else {
                    linearLayoutCharity.setVisibility(View.GONE);
                    personalRef.child("monthCha").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalWeekApparelExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "Apparel And Services"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsApparelAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("monthApp").setValue(totalAmount);
                }
                else {
                    linearLayoutApparel.setVisibility(View.GONE);
                    personalRef.child("monthApp").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalWeekHealthExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "Health"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsHealthAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("monthHea").setValue(totalAmount);
                }
                else {
                    linearLayoutHealth.setVisibility(View.GONE);
                    personalRef.child("monthHea").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalWeekPersonalExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "Personal Expenses"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsPersonalExpensesAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("monthPer").setValue(totalAmount);
                }
                else {
                    linearLayoutPersonalExp.setVisibility(View.GONE);
                    personalRef.child("monthPer").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalWeekOtherExpenses() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch, now);

        String itemNmonth = "Other"+months.getMonths();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsOtherAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("monthOther").setValue(totalAmount);
                }
                else {
                    linearLayoutOther.setVisibility(View.GONE);
                    personalRef.child("monthOther").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getTotalWeekSpending() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        Months months = Months.monthsBetween(epoch, now);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("month").equalTo(months.getMonths());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0) {
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                    }
                    totalAmountSpendOn.setText("Total month's spending: $ "+ totalAmount);
                    monthSpendAmount.setText("Total spent: $ "+totalAmount);
                }
                else {
                    totalAmountSpendOn.setText("You've not spent month");
                    anyChartView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadGraph() {
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    int traTotal;
                    if (snapshot.hasChild("monthTrans")) {
                        traTotal = Integer.parseInt(snapshot.child("monthTrans").getValue().toString());
                    } else {
                        traTotal = 0;
                    }

                    int foodTotal;
                    if (snapshot.hasChild("monthFood")) {
                        foodTotal = Integer.parseInt(snapshot.child("monthFood").getValue().toString());
                    } else {
                        foodTotal = 0;
                    }

                    int houseTotal;
                    if (snapshot.hasChild("monthHouse")) {
                        houseTotal = Integer.parseInt(snapshot.child("monthHouse").getValue().toString());
                    } else {
                        houseTotal = 0;
                    }

                    int entTotal;
                    if (snapshot.hasChild("monthEnt")) {
                        entTotal = Integer.parseInt(snapshot.child("monthEnt").getValue().toString());
                    } else {
                        entTotal = 0;
                    }

                    int eduTotal;
                    if (snapshot.hasChild("monthEdu")) {
                        eduTotal = Integer.parseInt(snapshot.child("monthEdu").getValue().toString());
                    } else {
                        eduTotal = 0;
                    }

                    int chaTotal;
                    if (snapshot.hasChild("monthCha")) {
                        chaTotal = Integer.parseInt(snapshot.child("monthCha").getValue().toString());
                    } else {
                        chaTotal = 0;
                    }

                    int appTotal;
                    if (snapshot.hasChild("monthApp")) {
                        appTotal = Integer.parseInt(snapshot.child("monthApp").getValue().toString());
                    } else {
                        appTotal = 0;
                    }

                    int heaTotal;
                    if (snapshot.hasChild("monthHea")) {
                        heaTotal = Integer.parseInt(snapshot.child("monthHea").getValue().toString());
                    } else {
                        heaTotal = 0;
                    }

                    int perTotal;
                    if (snapshot.hasChild("monthPer")) {
                        perTotal = Integer.parseInt(snapshot.child("monthPer").getValue().toString());
                    } else {
                        perTotal = 0;
                    }

                    int othTotal;
                    if (snapshot.hasChild("monthOther")) {
                        othTotal = Integer.parseInt(snapshot.child("monthOther").getValue().toString());
                    } else {
                        othTotal = 0;
                    }

                    Pie pie = AnyChart.pie();
                    List<DataEntry> data = new ArrayList<>();
                    data.add(new ValueDataEntry("Transport", traTotal));
                    data.add(new ValueDataEntry("Food", foodTotal));
                    data.add(new ValueDataEntry("House exp", houseTotal));
                    data.add(new ValueDataEntry("Entertainment", entTotal));
                    data.add(new ValueDataEntry("Education", eduTotal));
                    data.add(new ValueDataEntry("Charity", chaTotal));
                    data.add(new ValueDataEntry("Apparel", appTotal));
                    data.add(new ValueDataEntry("Health", heaTotal));
                    data.add(new ValueDataEntry("Personal", perTotal));
                    data.add(new ValueDataEntry("Other", othTotal));

                    pie.data(data);

                    pie.title("Month Analytics");

                    pie.labels().position("outside");

                    pie.legend().title().enabled(true);
                    pie.legend().title().text("items spent on").padding(0d, 0d, 10d, 0d);
                    pie.legend().position("center-bottom").itemsLayout(LegendLayout.HORIZONTAL).align(Align.CENTER);

                    anyChartView.setChart(pie);
                } else {
                    Toast.makeText(MonthlyAnalyticsActivity.this, "Child dose not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonthlyAnalyticsActivity.this, "Child dose not exist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setStatusAndImageResource(){
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    float traTotal;
                    if (snapshot.hasChild("monthTrans")){
                        traTotal = Integer.parseInt(snapshot.child("monthTrans").getValue().toString());
                    } else {
                        traTotal = 0;
                    }

                    float foodTotal;
                    if (snapshot.hasChild("monthFood")){
                        foodTotal = Integer.parseInt(snapshot.child("monthFood").getValue().toString());
                    } else {
                        foodTotal = 0;
                    }

                    float houseTotal;
                    if (snapshot.hasChild("monthHouse")){
                        houseTotal = Integer.parseInt(snapshot.child("monthHouse").getValue().toString());
                    } else {
                        houseTotal = 0;
                    }

                    float entTotal;
                    if (snapshot.hasChild("monthEnt")){
                        entTotal = Integer.parseInt(snapshot.child("monthEnt").getValue().toString());
                    } else {
                        entTotal = 0;
                    }

                    float eduTotal;
                    if (snapshot.hasChild("monthEdu")){
                        eduTotal = Integer.parseInt(snapshot.child("monthEdu").getValue().toString());
                    } else {
                        eduTotal = 0;
                    }

                    float chaTotal;
                    if (snapshot.hasChild("monthCha")){
                        chaTotal = Integer.parseInt(snapshot.child("monthCha").getValue().toString());
                    } else {
                        chaTotal = 0;
                    }

                    float appTotal;
                    if (snapshot.hasChild("monthCha")){
                        appTotal = Integer.parseInt(snapshot.child("monthCha").getValue().toString());
                    } else {
                        appTotal = 0;
                    }

                    float heaTotal;
                    if (snapshot.hasChild("monthHea")){
                        heaTotal = Integer.parseInt(snapshot.child("monthHea").getValue().toString());
                    } else {
                        heaTotal = 0;
                    }

                    float perTotal;
                    if (snapshot.hasChild("monthPer")){
                        perTotal = Integer.parseInt(snapshot.child("monthPer").getValue().toString());
                    } else {
                        perTotal = 0;
                    }

                    float othTotal;
                    if (snapshot.hasChild("monthOther")){
                        othTotal = Integer.parseInt(snapshot.child("monthOther").getValue().toString());
                    } else {
                        othTotal = 0;
                    }

                    float monthTotalSpendAmount;
                    if (snapshot.hasChild("month")){
                        monthTotalSpendAmount = Integer.parseInt(snapshot.child("month").getValue().toString());
                    } else {
                        monthTotalSpendAmount = 0;
                    }

                    // getting ratios

                    float traRatio;
                    if (snapshot.hasChild("monthTransRatio")){
                        traRatio = Integer.parseInt(snapshot.child("monthTransRatio").getValue().toString());
                    } else {
                        traRatio = 0;
                    }

                    float foodRatio;
                    if (snapshot.hasChild("monthFoodRatio")){
                        foodRatio = Integer.parseInt(snapshot.child("monthFoodRatio").getValue().toString());
                    } else {
                        foodRatio = 0;
                    }

                    float houseRatio;
                    if (snapshot.hasChild("monthHouseRatio")){
                        houseRatio = Integer.parseInt(snapshot.child("monthHouseRatio").getValue().toString());
                    } else {
                        houseRatio = 0;
                    }

                    float entRatio;
                    if (snapshot.hasChild("monthEntRatio")){
                        entRatio = Integer.parseInt(snapshot.child("monthEntRatio").getValue().toString());
                    } else {
                        entRatio = 0;
                    }

                    float eduRatio;
                    if (snapshot.hasChild("monthEduRatio")){
                        eduRatio = Integer.parseInt(snapshot.child("monthEduRatio").getValue().toString());
                    } else {
                        eduRatio = 0;
                    }

                    float chaRatio;
                    if (snapshot.hasChild("monthCharRatio")){
                        chaRatio = Integer.parseInt(snapshot.child("monthCharRatio").getValue().toString());
                    } else {
                        chaRatio = 0;
                    }

                    float appRatio;
                    if (snapshot.hasChild("monthAppRatio")){
                        appRatio = Integer.parseInt(snapshot.child("monthAppRatio").getValue().toString());
                    } else {
                        appRatio = 0;
                    }

                    float heaRatio;
                    if (snapshot.hasChild("monthHealthRatio")){
                        heaRatio = Integer.parseInt(snapshot.child("monthHealthRatio").getValue().toString());
                    } else {
                        heaRatio = 0;
                    }

                    float perRatio;
                    if (snapshot.hasChild("monthPerRatio")){
                        perRatio = Integer.parseInt(snapshot.child("monthPerRatio").getValue().toString());
                    } else {
                        perRatio = 0;
                    }

                    float othRatio;
                    if (snapshot.hasChild("monthOtherRatio")){
                        othRatio = Integer.parseInt(snapshot.child("monthOtherRatio").getValue().toString());
                    } else {
                        othRatio = 0;
                    }

                    float monthTotalSpendAmountRatio;
                    if (snapshot.hasChild("budget")){
                        monthTotalSpendAmountRatio = Integer.parseInt(snapshot.child("budget").getValue().toString());
                    } else {
                        monthTotalSpendAmountRatio = 0;
                    }

                    float monthPercent = (monthTotalSpendAmount/monthTotalSpendAmountRatio)*100;
                    if (monthPercent<50) {
                        monthRatioSpending.setText(monthPercent+" %"+"used of"+monthTotalSpendAmountRatio+". Status");
                        monthRatioSpending_Image.setImageResource(R.drawable.green);
                    } else if (monthPercent >= 50 && monthPercent <100) {
                        monthRatioSpending.setText(monthPercent+" %"+"used of"+monthTotalSpendAmountRatio+". Status");
                        monthRatioSpending_Image.setImageResource(R.drawable.brown);
                    } else {
                        monthRatioSpending.setText(monthPercent+" %"+"used of"+monthTotalSpendAmountRatio+". Status");
                        monthRatioSpending_Image.setImageResource(R.drawable.red);
                    }

                    float transportPercent = (traTotal/traRatio)*100;
                    if (transportPercent<50) {
                        progress_ratio_transport.setText(transportPercent+" %"+"used of"+traRatio+". Status");
                        status_Image_transport.setImageResource(R.drawable.green);
                    } else if (transportPercent >= 50 && transportPercent <100) {
                        progress_ratio_transport.setText(transportPercent+" %"+"used of"+traRatio+". Status");
                        status_Image_transport.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_transport.setText(transportPercent+" %"+"used of"+traRatio+". Status");
                        status_Image_transport.setImageResource(R.drawable.red);
                    }

                    float foodPercent = (foodTotal/foodRatio)*100;
                    if (foodPercent<50) {
                        progress_ratio_food.setText(foodPercent+" %"+"used of"+foodRatio+". Status");
                        status_Image_food.setImageResource(R.drawable.green);
                    } else if (foodPercent >= 50 && foodPercent <100) {
                        progress_ratio_food.setText(foodPercent+" %"+"used of"+foodRatio+". Status");
                        status_Image_food.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_food.setText(foodPercent+" %"+"used of"+foodRatio+". Status");
                        status_Image_food.setImageResource(R.drawable.red);
                    }

                    float housePercent = (houseTotal/houseRatio)*100;
                    if (housePercent<50) {
                        progress_ratio_house.setText(housePercent+" %"+"used of"+houseRatio+". Status");
                        status_Image_house.setImageResource(R.drawable.green);
                    } else if (housePercent >= 50 && housePercent <100) {
                        progress_ratio_house.setText(housePercent+" %"+"used of"+houseRatio+". Status");
                        status_Image_house.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_house.setText(housePercent+" %"+"used of"+houseRatio+". Status");
                        status_Image_house.setImageResource(R.drawable.red);
                    }

                    float entPercent = (entTotal/entRatio)*100;
                    if (entPercent<50) {
                        progress_ratio_ent.setText(entPercent+" %"+"used of"+entRatio+". Status");
                        status_Image_ent.setImageResource(R.drawable.green);
                    } else if (transportPercent >= 50 && entPercent <100) {
                        progress_ratio_ent.setText(entPercent+" %"+"used of"+entRatio+". Status");
                        status_Image_ent.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_ent.setText(entPercent+" %"+"used of"+entRatio+". Status");
                        status_Image_ent.setImageResource(R.drawable.red);
                    }

                    float eduPercent = (eduTotal/eduRatio)*100;
                    if (eduPercent<50) {
                        progress_ratio_edu.setText(eduPercent+" %"+"used of"+eduRatio+". Status");
                        status_Image_edu.setImageResource(R.drawable.green);
                    } else if (eduPercent >= 50 && eduPercent <100) {
                        progress_ratio_edu.setText(eduPercent+" %"+"used of"+eduRatio+". Status");
                        status_Image_edu.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_edu.setText(eduPercent+" %"+"used of"+eduRatio+". Status");
                        status_Image_edu.setImageResource(R.drawable.red);
                    }

                    float chaPercent = (chaTotal/chaRatio)*100;
                    if (chaPercent<50) {
                        progress_ratio_cha.setText(chaPercent+" %"+"used of"+chaRatio+". Status");
                        status_Image_cha.setImageResource(R.drawable.green);
                    } else if (chaPercent >= 50 && chaPercent <100) {
                        progress_ratio_cha.setText(chaPercent+" %"+"used of"+chaRatio+". Status");
                        status_Image_cha.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_cha.setText(chaPercent+" %"+"used of"+chaRatio+". Status");
                        status_Image_cha.setImageResource(R.drawable.red);
                    }

                    float appPercent = (appTotal/appRatio)*100;
                    if (appPercent<50) {
                        progress_ratio_app.setText(appPercent+" %"+"used of"+appRatio+". Status");
                        status_Image_app.setImageResource(R.drawable.green);
                    } else if (appPercent >= 50 && appPercent <100) {
                        progress_ratio_app.setText(appPercent+" %"+"used of"+appRatio+". Status");
                        status_Image_app.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_app.setText(appPercent+" %"+"used of"+appRatio+". Status");
                        status_Image_app.setImageResource(R.drawable.red);
                    }

                    float heaPercent = (heaTotal/heaRatio)*100;
                    if (heaPercent<50) {
                        progress_ratio_hea.setText(heaPercent+" %"+"used of"+heaRatio+". Status");
                        status_Image_hea.setImageResource(R.drawable.green);
                    } else if (heaPercent >= 50 && heaPercent <100) {
                        progress_ratio_hea.setText(heaPercent+" %"+"used of"+heaRatio+". Status");
                        status_Image_hea.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_hea.setText(heaPercent+" %"+"used of"+heaRatio+". Status");
                        status_Image_hea.setImageResource(R.drawable.red);
                    }

                    float perPercent = (perTotal/perRatio)*100;
                    if (perPercent<50) {
                        progress_ratio_per.setText(perPercent+" %"+"used of"+perRatio+". Status");
                        status_Image_per.setImageResource(R.drawable.green);
                    } else if (perPercent >= 50 && perPercent <100) {
                        progress_ratio_per.setText(perPercent+" %"+"used of"+perRatio+". Status");
                        status_Image_per.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_per.setText(perPercent+" %"+"used of"+perRatio+". Status");
                        status_Image_per.setImageResource(R.drawable.red);
                    }

                    float othPercent = (othTotal/othRatio)*100;
                    if (othPercent<50) {
                        progress_ratio_oth.setText(othPercent+" %"+"used of"+othRatio+". Status");
                        status_Image_oth.setImageResource(R.drawable.green);
                    } else if (othPercent >= 50 && othPercent <100) {
                        progress_ratio_oth.setText(othPercent+" %"+"used of"+othRatio+". Status");
                        status_Image_oth.setImageResource(R.drawable.brown);
                    } else {
                        progress_ratio_oth.setText(othPercent+" %"+"used of"+othRatio+". Status");
                        status_Image_oth.setImageResource(R.drawable.red);
                    }

                }
                else {
                    Toast.makeText(MonthlyAnalyticsActivity.this, "setStatusAndImageResource Errors", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}