package com.proj.synoposbilling.RoomPersistanceLibrary;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.android.volley.Response;
import com.proj.synoposbilling.Activity.HomeActivity;
import com.proj.synoposbilling.Adapter.SubCategoryAdapter;
import com.proj.synoposbilling.DialogFragment.SubCategoriesItemFragment;
import com.proj.synoposbilling.Fragments.OrderFragment;
import com.proj.synoposbilling.Interface.SetCartInterface;
import com.proj.synoposbilling.Payment.PaymentActivity;


import java.util.List;

public class SqliteDbMethod implements SetCartInterface
{

 List<KutumbDTO> kutumbDTOList;

    private void deleteTask(final Context context)
    {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .taskDao()
                        .deleteAll();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //new CartActivity().getVisiblity(taskList);

            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }
//1st

    public void getTasks(Context context,String from) {
        class GetTasks extends AsyncTask<Void, Void, List<KutumbDTO>> {

            @Override
            protected List<KutumbDTO> doInBackground(Void... voids) {
                kutumbDTOList = DatabaseClient
                        .getInstance(context.getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getAllProduct();

                return kutumbDTOList;
            }

            @Override
            protected void onPostExecute(List<KutumbDTO> tasks) {
                super.onPostExecute(tasks);
                Log.e("fromm",from);
                if (from.equalsIgnoreCase("payment"))
                {
                    PaymentActivity.getInstance().runThread(kutumbDTOList);
                }
                else if (from.equalsIgnoreCase("subcategoryitem"))
                {
                    SubCategoriesItemFragment.getInstance().runThread(kutumbDTOList);
                }
                else if (from.equalsIgnoreCase("sabcategadapter"))
                {
                    SubCategoryAdapter.getInstance().runThread(kutumbDTOList);
                }
                else if (from.equalsIgnoreCase("order"))
                {
                    OrderFragment.getInstance().runThread(kutumbDTOList);
                }
                else if (from.equalsIgnoreCase("home"))
                {
                    HomeActivity.getInstance().runThread(kutumbDTOList.size());
                }
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }



}
