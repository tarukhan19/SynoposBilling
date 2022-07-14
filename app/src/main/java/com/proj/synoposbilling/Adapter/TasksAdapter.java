package com.proj.synoposbilling.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.proj.synoposbilling.Activity.HomeActivity;
import com.proj.synoposbilling.Fragments.OrderFragment;
import com.proj.synoposbilling.Payment.PaymentActivity;
import com.proj.synoposbilling.R;
import com.proj.synoposbilling.RoomPersistanceLibrary.DatabaseClient;
import com.proj.synoposbilling.RoomPersistanceLibrary.KutumbDTO;
import com.proj.synoposbilling.Utils.ApiMethods;
import com.proj.synoposbilling.Utils.Constant;
import com.proj.synoposbilling.Utils.DistanceCalcMethods;
import com.proj.synoposbilling.Utils.EndPoints;
import com.proj.synoposbilling.Utils.HideKeyborad;
import com.proj.synoposbilling.databinding.ItemSubcategoryBinding;
import com.proj.synoposbilling.session.SessionManager;
import com.squareup.picasso.Picasso;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private Context mCtx;
    private List<KutumbDTO> taskList;
    Activity activity;
    ItemSubcategoryBinding binding;
    SessionManager sessionManager;
    int count, pos, quantity;
    LinearLayout checkoutLL;
    ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    double distance,maxDistance;
    RecyclerView recyclerView;
    LinearLayout nrfLL;
    List<String> sendproductidList, sentquantityList;
    double subtotalAmount=0.0,
            totaltaxamount=0.0, subTotal=0.0;
    DistanceCalcMethods distanceCalcMethods;
    String orderno = "";
    private List<String> commaseperatelist;
    String ObjDgv;
    Date date;
    SimpleDateFormat dateFormat;
    int totalquantity;
    static TasksAdapter tasksAdapter;
    public TasksAdapter(Context mCtx, List<KutumbDTO> taskList,
                        LinearLayout nrfLL, RecyclerView recyclerView,
                        LinearLayout checkoutLL) {
        this.mCtx = mCtx;
        this.taskList = taskList;
        activity = (Activity) mCtx;
        sessionManager = new SessionManager(mCtx);
        this.recyclerView = recyclerView;
        this.nrfLL = nrfLL;
        this.checkoutLL=checkoutLL;
        tasksAdapter=this;
        sendproductidList = new ArrayList<>();
        sentquantityList = new ArrayList<>();
        distanceCalcMethods = new DistanceCalcMethods();
        commaseperatelist = new ArrayList<>();
        progressDialog = new ProgressDialog(mCtx);
        requestQueue = Volley.newRequestQueue(mCtx);
    }

    public static TasksAdapter getInstance()
    {
        return tasksAdapter;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_subcategory, parent, false);
        return new TasksViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final TasksViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.itemRowBinding.nameTV.setText(taskList.get(position).getProductName());
        holder.itemRowBinding.priceTV.setText("Rs. " + taskList.get(position).getProductMRP());
        holder.itemRowBinding.descrTV.setText(taskList.get(position).getDescription());


        if (taskList.get(position).getStrikeprice()==0  ||
                taskList.get(position).getStrikeprice()==0.0 )
        {
            holder.itemRowBinding.strikepriceTV.setVisibility(View.GONE);
        }
        else
        {
            holder.itemRowBinding.strikepriceTV.setPaintFlags(holder.itemRowBinding.strikepriceTV.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.itemRowBinding.strikepriceTV.setVisibility(View.VISIBLE);
            holder.itemRowBinding.strikepriceTV.setText(taskList.get(position).getStrikeprice()+"");

        }

        if (taskList.get(position).getProductImage().isEmpty()) {
            holder.itemRowBinding.imageRL.setVisibility(View.GONE);
            holder.itemRowBinding.vegnvwithoutimageIV.setVisibility(View.VISIBLE);
            if (taskList.get(position).getIsvegnonveg().equalsIgnoreCase("1")) {
                holder.itemRowBinding.vegnvwithoutimageIV.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.nonvegdot));
            } else {
                holder.itemRowBinding.vegnvwithoutimageIV.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.vegdot));

            }


        } else {
            holder.itemRowBinding.imageRL.setVisibility(View.VISIBLE);
            holder.itemRowBinding.vegnvwithoutimageIV.setVisibility(View.GONE);
            if (taskList.get(position).getIsvegnonveg().equalsIgnoreCase("1")) {
                holder.itemRowBinding.vegnvIV.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.nonvegdot));
            } else {
                holder.itemRowBinding.vegnvIV.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.vegdot));

            }
            try {
                Picasso.get()
                        .load(taskList.get(position).getProductImage())
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(holder.itemRowBinding.imageView);
            } catch (Exception e) {
            }


        }


        if (taskList.get(position).getQuantity() == 0) {
            holder.itemRowBinding.qtyTV.setText("Add");

        } else {
            holder.itemRowBinding.qtyTV.setText(String.valueOf(taskList.get(position).getQuantity()));

        }


        if (!sessionManager.getCartSize().get(SessionManager.KEY_CART_SIZE).equalsIgnoreCase("0"))
        {
            count = Integer.parseInt(sessionManager.getCartSize().get(SessionManager.KEY_CART_SIZE));
        }

        holder.itemRowBinding.plusTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    pos = position;
                    plusMethod();

                } catch (Exception e) {
                }


            }
        });


        holder.itemRowBinding.minusTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    pos = position;
                    minusMethod();
                } catch (Exception e) {
                }

            }
        });



        checkoutLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyborad.hideKeyboard(mCtx);

                if (sessionManager.isLoggedIn()) {
                    try
                    {

                        new ApiMethods().loadCheckoutData(mCtx);

                    } catch (Exception e) {
                    }


                } else {
                    Intent in7 = new Intent(mCtx, HomeActivity.class);
                    in7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    in7.putExtra("for", "login");
                    mCtx.startActivity(in7);
                    activity.overridePendingTransition(R.anim.trans_left_in,
                            R.anim.trans_left_out);
                }
            }
        });

        calculation();
    }

    public void runThread() {


        new Thread() {
            public void run() {
                try {
                    ((Activity) mCtx).runOnUiThread(new Runnable() {

                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void run()
                        {
                            distance=Double.parseDouble(sessionManager.getLatLngDistance().get(SessionManager.KEY_LATLONG_DISTANCE));
                            maxDistance=Double.parseDouble(sessionManager.getCheckoutData().get(SessionManager.KEY_MAX_DISTANCE));

                            Log.e("distance",distance+"");
                            Log.e("maxDistance",maxDistance+"");

                            getCurrentTime();
                        }
                    });
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();


    }

    private void openDialog(String msg, String imagetype) {
        final Dialog dialog = new Dialog(mCtx, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialogbox);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        TextView descriptionTV = dialog.findViewById(R.id.descriptionTV);
        TextView titleTV = dialog.findViewById(R.id.titleTV);
        ImageView imageView = dialog.findViewById(R.id.imageView);
        Button okBtn = dialog.findViewById(R.id.okBtn);

        descriptionTV.setText(msg);
        if (imagetype.equalsIgnoreCase("warning")) {
            //imageView.setImageResource(R.drawable.warning);
            titleTV.setText("Warning!");
        } else if (imagetype.equalsIgnoreCase("success")) {
            // imageView.setImageResource(R.drawable.success);
            titleTV.setText("Success!");
        } else if (imagetype.equalsIgnoreCase("failure")) {
            // imageView.setImageResource(R.drawable.sorry);
            titleTV.setText("Failure!");
        }


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imagetype.equalsIgnoreCase("warning") || imagetype.equalsIgnoreCase("failure")) {
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    Intent in7 = new Intent(mCtx, HomeActivity.class);
                    in7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mCtx.startActivity(in7);
                    activity.overridePendingTransition(R.anim.trans_left_in,
                            R.anim.trans_left_out);
                }


            }
        });
    }


    private void getCurrentTime() {

        date = new Date();
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat.format(date);
        Log.e("current time", dateFormat.format(date));
        listtostringconver();

    }

    private void checkConditions()
    {
        try {
            if (subtotalAmount < 99.0) {
                openDialog("Min amount should be Rs. 99/-", "warning");
            } else if (sessionManager.getDeliveryAddress().get(SessionManager.KEY_ADDRESS_DELIVERY_ID).isEmpty()) {
                openDialog("Add / Select address.", "warning");
            }
             else if (dateFormat.parse(dateFormat.format(date)).after(dateFormat.parse(sessionManager.getCheckoutData().get(SessionManager.KEY_CLOSING_TIME)))) {
                openDialog("Order time should be between "+ (sessionManager.getCheckoutData().get(SessionManager.KEY_OPENING_TIME))+ " to " +
                                sessionManager.getCheckoutData().get(SessionManager.KEY_CLOSING_TIME)
                       , "warning");
              }
            else if (dateFormat.parse(dateFormat.format(date)).before(dateFormat.parse(sessionManager.getCheckoutData().get(SessionManager.KEY_OPENING_TIME)))) {
                openDialog("Order time should be between "+ (sessionManager.getCheckoutData().get(SessionManager.KEY_OPENING_TIME))+ " to " +
                                sessionManager.getCheckoutData().get(SessionManager.KEY_CLOSING_TIME)
                        , "warning");
            }
            else if (!sessionManager.getCheckoutData().get(SessionManager.KEY_ORDER_STATUS).equalsIgnoreCase("Enable")) {
                openDialog("We are unable to deliver the order now due to heavy order flow. Please try again after some time. Thank you!", "warning");
            }
//            else if (distance>maxDistance) {
//                openDialog("We are not serviceable to this area, Thank you!", "warning");
//            }
            else {
                submitData();
            }


        }
        catch (Exception e)
        {}

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public class TasksViewHolder extends RecyclerView.ViewHolder {


        ItemSubcategoryBinding itemRowBinding;

        public TasksViewHolder(ItemSubcategoryBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }


    }


    private void plusMethod()
    {
        try {

            quantity = taskList.get(pos).getQuantity();
            if (quantity >= 0)
            {
                quantity = quantity + 1;
                count = count + 1;
                sessionManager.setCartSize(String.valueOf(count));
                KutumbDTO task = taskList.get(pos);
                updateTask(task);
            }

        } catch (Exception e) {
        }
    }


    private void minusMethod()
    {
        try {
            quantity = taskList.get(pos).getQuantity();

            if (quantity > 1) {


                quantity = quantity - 1;
                count = count - 1;

                sessionManager.setCartSize(String.valueOf(count));

                KutumbDTO task = taskList.get(pos);
                updateTask(task);


            } else if (quantity == 1) {

                quantity = taskList.get(pos).getQuantity();
                // Toast.makeText(mCtx, quantity + "", Toast.LENGTH_SHORT).show();
                quantity = quantity - 1;
                count = count - 1;
                sessionManager.setCartSize(String.valueOf(count));


                KutumbDTO task = taskList.get(pos);
                deleteTask(task);
            }

        } catch (Exception e) {
        }
    }


    private void calculation() {

        subtotalAmount = 0.0;
        for (int i = 0; i < taskList.size(); i++) {
            double amount = taskList.get(i).getQuantity() * taskList.get(i).getAmountplustax();
            subtotalAmount = Math.round((subtotalAmount + amount) * 100.0) / 100.0;
            Log.e("subtotalAmount", subtotalAmount + "");

        }
    }


    private void deleteTask(final KutumbDTO task) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(mCtx.getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                taskList.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, taskList.size());
                sessionManager.setTotalRateForOffers("");
                HomeActivity.getInstance().runThread(count);
                OrderFragment.getInstance().runThread("updateTasks");


            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }


    private void updateTask(final KutumbDTO task)
    {
        class UpdateTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                task.setProductName(taskList.get(pos).getProductName());
                task.setProductMRP(taskList.get(pos).getProductMRP());
                task.setProductId(taskList.get(pos).getProductId());
                task.setProductImage(taskList.get(pos).getProductImage());
                task.setDescription(taskList.get(pos).getDescription());
                task.setIsvegnonveg(taskList.get(pos).getIsvegnonveg());
                task.setAmountplustax(taskList.get(pos).getAmountplustax());
                task.setTaxpercent(taskList.get(pos).getTaxpercent());
                task.setTaxamount(taskList.get(pos).getTaxamount());
                task.setStrikeprice(taskList.get(pos).getStrikeprice());
                task.setIsDiscount(taskList.get(pos).getIsDiscount());

                task.setQuantity(quantity);
                task.setAddtocart("1");


                DatabaseClient.getInstance(mCtx).getAppDatabase()
                        .taskDao()
                        .update(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                try {
                  //  taskList.get(pos).setQuantity(quantity);

                    notifyItemChanged(pos);
                    notifyDataSetChanged();
                    HomeActivity.getInstance().runThread(count);

                    OrderFragment.getInstance().runThread("updateTasks");

                } catch (Exception e) {
                }
            }
        }

        UpdateTask gt = new UpdateTask();
        gt.execute();
    }

    private void listtostringconver()
    {
        for (int i = 0; i < taskList.size(); i++) {
            try
            {
                int quantity = taskList.get(i).getQuantity();
                double rate = (taskList.get(i).getProductMRP());
                double total = quantity * taskList.get(i).getProductMRP();
                double taxrate = taskList.get(i).getTaxpercent();
                double tax = taskList.get(i).getTaxamount();
                double taxamount = taskList.get(i).getTaxamount();

                totaltaxamount = (quantity * taxamount) + totaltaxamount;
                Log.e("totaltaxamount", totaltaxamount + "");
                subTotal = subTotal + total;
                totalquantity=totalquantity+quantity;


                String data =

                        taskList.get(i).getProductId() + ","
                                //prodname
                                + taskList.get(i).getProductName() + "," +
                                //prodquantity
                                quantity + ","
                                //rate
                                + rate + ","
                                //Total
                                + total + ","
                                //DishComment
                                + "" + "," +
                                //Discount
                                "" + "0" + ","
                                //Rs
                                + "0" + "," +
                                //Pct
                                "" + "0" + "," +
                                //TaxRate
                                "" + taxrate + ","
                                //Tax
                                + tax + "," +
                                //AddonCode
                                "" + "0" + "," +
                                //item_index
                                "" + "0" + "," +
                                //kot_time
                                "" + "0";

                Log.e("data", data);

                commaseperatelist.add(data);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        StringBuilder sbString = new StringBuilder();

        //iterate through ArrayList
        for (String services : commaseperatelist) {

            //append ArrayList element followed by comma
            sbString.append(services).append(",");
        }


        ObjDgv = sbString.toString().trim();
        if (ObjDgv.length() > 0) {
            ObjDgv = ObjDgv.substring(0, ObjDgv.length() - 1);
        }
        HomeActivity.getInstance().runThread(totalquantity);

        checkConditions();




    }


    private void submitData()
    {

        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, EndPoints.SAVE_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("SAVE_CART", response);

                        try {
                            //{"Code":200,"BillNo":"116","Status":"Success"}

                            JSONObject jsonObject=new JSONObject(response);
                            int code=jsonObject.getInt("Code");
                            String status=jsonObject.getString("Status");

                            if (code==200 && status.equalsIgnoreCase("Success") )
                            {
                                orderno=jsonObject.getString("BillNo");

                                sessionManager.setOrderId(orderno);
                                Intent in7 = new Intent(mCtx, PaymentActivity.class);
//                                in7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mCtx.startActivity(in7);
                                activity.overridePendingTransition(R.anim.trans_left_in,
                                        R.anim.trans_left_out);

                            }
                            else
                            {
                                openDialog(status, "warning");

//                                sessionManager.setPaymentAmount(String.valueOf(subtotalAmount));
//                                sessionManager.setOrderId("10");
//                                Intent in7 = new Intent(mCtx, PaymentActivity.class);
////                                in7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                mCtx.startActivity(in7);
//                                activity.overridePendingTransition(R.anim.trans_left_in,
//                                        R.anim.trans_left_out);
                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
        ) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Bill_Amount", String.valueOf(subtotalAmount));
                params.put("Dis_Amount", "0");
                params.put("Payment_Mode", "3");
                params.put("No_of_Item", sessionManager.getCartSize().get(SessionManager.KEY_CART_SIZE));
                params.put("Cashier", sessionManager.getLoginData().get(SessionManager.KEY_F_NAME));
                params.put("tax1", String.valueOf(totaltaxamount));
                params.put("Bill_DiscountPct", "0");
                params.put("SubTotal", String.valueOf(subTotal));
                params.put("ObjDgv", ObjDgv);
                params.put("OutletID", "1");
                params.put("Addr_ID", "1");
                params.put("Cust_code", sessionManager.getLoginData().get(SessionManager.KEY_ID));
            return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);



    }

}