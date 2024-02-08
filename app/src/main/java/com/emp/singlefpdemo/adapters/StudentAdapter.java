package com.emp.singlefpdemo.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.emp.singlefpdemo.BaseActivity;
import com.emp.singlefpdemo.MyDatabaseHelper;
import com.emp.singlefpdemo.R;
import com.emp.singlefpdemo.SFingerActivity;
import com.emp.singlefpdemo.VolleyMultipartRequest;
import com.szsicod.print.utils.BitmapUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAdapter extends BaseAdapter {

    private RequestQueue rQueue;

    private Context context;
    private ArrayList<Integer> arr_id;
    private ArrayList<String> arr_firstname;
    private ArrayList<String> arr_surname;
    private ArrayList<String> arr_othername;
    private ArrayList<String> arr_dob;
    private ArrayList<String> arr_guardianname;
    private ArrayList<String> arr_guardianphone;
    private ArrayList<String> arr_guardianaddress;
    private ArrayList<String> arr_guardianrelationship;
    private ArrayList<String> arr_question1;
    private ArrayList<String> arr_question2;
    private ArrayList<String> arr_question3;
    private ArrayList<String> arr_question4;
    private ArrayList<String> arr_question5;
    private ArrayList<String> arr_religion;
    private ArrayList<String> arr_address;
    private ArrayList<byte[]> arr_picture;
    private ArrayList<byte[]> arr_fingerprint;
    private ArrayList<byte[]> arr_signature;
    private ArrayList<String> arr_gender;
    private ArrayList<String> arr_class;
    private ArrayList<String> arr_shoe_size;
    private ArrayList<String> arr_height;
    private ArrayList<String> arr_weight;
    private ArrayList<String> arr_origin;
    private ArrayList<String> arr_schoolname;
    private ArrayList<String> arr_lga;
    private ArrayList<String> arr_createdby;
    private ArrayList<String> arr_tesis_number;
    TextView count_local;
    TextView count_sync;

    public StudentAdapter(Context context, ArrayList<Integer> arr_id, ArrayList<String> arr_firstname, ArrayList<String> arr_surname,
                          ArrayList<String> arr_othername, ArrayList<String> arr_dob, ArrayList<String> arr_guardianname, ArrayList<String> arr_guardianphone,
                          ArrayList<String> arr_guardianaddress, ArrayList<String> arr_guardianrelationship, ArrayList<String> arr_question1,
                          ArrayList<String> arr_question2, ArrayList<String> arr_question3, ArrayList<String> arr_question4, ArrayList<String> arr_question5,
                          ArrayList<String> arr_religion, ArrayList<String> arr_address, ArrayList<byte[]> arr_picture, ArrayList<byte[]> arr_fingerprint,
                          ArrayList<byte[]> arr_signature, ArrayList<String> arr_gender, ArrayList<String> arr_class, ArrayList<String> arr_shoe_size,
                          ArrayList<String> arr_height, ArrayList<String> arr_weight, ArrayList<String> arr_origin, ArrayList<String> arr_schoolname,
                          ArrayList<String> arr_lga, ArrayList<String> arr_createdby, ArrayList<String> arr_tesis_number, TextView count_local, TextView count_sync){
        //Getting all the values
        this.context = context;
        this.arr_id = arr_id;
        this.arr_firstname = arr_firstname;
        this.arr_surname = arr_surname;
        this.arr_othername = arr_othername;
        this.arr_dob = arr_dob;
        this.arr_guardianname = arr_guardianname;
        this.arr_guardianphone = arr_guardianphone;
        this.arr_guardianaddress = arr_guardianaddress;
        this.arr_guardianrelationship = arr_guardianrelationship;
        this.arr_question1 = arr_question1;
        this.arr_question2 = arr_question2;
        this.arr_question3 = arr_question3;
        this.arr_question4 = arr_question4;
        this.arr_question5 = arr_question5;
        this.arr_religion = arr_religion;
        this.arr_address = arr_address;
        this.arr_picture = arr_picture;
        this.arr_fingerprint = arr_fingerprint;
        this.arr_signature = arr_signature;
        this.arr_gender = arr_gender;
        this.arr_class = arr_class;
        this.arr_shoe_size = arr_shoe_size;
        this.arr_height = arr_height;
        this.arr_weight = arr_weight;
        this.arr_origin = arr_origin;
        this.arr_schoolname = arr_schoolname;
        this.arr_lga = arr_lga;
        this.arr_createdby = arr_createdby;
        this.arr_tesis_number = arr_tesis_number;
        this.count_local = count_local;
        this.count_sync = count_sync;
    }

    @Override
    public int getCount() {
        return arr_schoolname.size();
    }

    @Override
    public Object getItem(int i) {
        return arr_schoolname.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_saved_users, parent, false);
        }

        MyDatabaseHelper myDB = new MyDatabaseHelper(context);

        CircleImageView userimg = convertView.findViewById(R.id.userimg);
        TextView name = convertView.findViewById(R.id.name);
        TextView dob = convertView.findViewById(R.id.dob);
        TextView tesisNumber = convertView.findViewById(R.id.tesisNumber);
        TextView religion = convertView.findViewById(R.id.religion);
        TextView gender = convertView.findViewById(R.id.gender);
        TextView schoolname = convertView.findViewById(R.id.schoolname);
        Button sync = convertView.findViewById(R.id.sync);
        ProgressBar progressBar = convertView.findViewById(R.id.progressBar);
        ImageView check = convertView.findViewById(R.id.check);

        Glide.with(context).load(arr_picture.get(i)).into(userimg);
        name.setText(arr_firstname.get(i)+" "+arr_surname.get(i));
        dob.setText(arr_dob.get(i));
        tesisNumber.setText(arr_tesis_number.get(i));
        religion.setText(arr_religion.get(i));
        gender.setText(arr_gender.get(i));
        schoolname.setText(arr_schoolname.get(i));

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sync.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "https://taraenroll.com/api/v1/add_student",
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                            System.out.println("Server response "+new String(response.data));
                                rQueue.getCache().clear();

                                try {
                                    JSONObject jsonObject = new JSONObject(new String(response.data));
                                    String status = jsonObject.getString("status");

                                    if (status.equals("successful")){
                                        //change to checkmark
                                        progressBar.setVisibility(View.GONE);
                                        check.setVisibility(View.VISIBLE);
                                        //delete record from DB
                                        myDB.deleteRecord(arr_id.get(i));
                                        count_local.setText(String.valueOf(myDB.getRecordCount()));

                                    }
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                    progressBar.setVisibility(View.GONE);
                                    sync.setVisibility(View.VISIBLE);
                                    Toast.makeText(context, "Error syncing, please try again", Toast.LENGTH_SHORT).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("Server error "+error);
                                progressBar.setVisibility(View.GONE);
                                sync.setVisibility(View.VISIBLE);
                                Toast.makeText(context, "Error, please check for good internet connectivity", Toast.LENGTH_SHORT).show();
//                                myDialog.dismiss();
                            }
                        }) {

                    /*
                     * If you want to add more parameters with the image
                     * you can do it here
                     * here we have only one parameter with the image
                     * which is tags
                     * */
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("firstname", arr_firstname.get(i));
                        params.put("surname", arr_surname.get(i));
                        params.put("othername", arr_othername.get(i));
                        params.put("dob", arr_dob.get(i));
                        params.put("gfullname", arr_guardianname.get(i));
                        params.put("gphone", arr_guardianphone.get(i));
                        params.put("gaddress", arr_guardianaddress.get(i));
                        params.put("grelationship", arr_guardianrelationship.get(i));
                        params.put("secret_question1", arr_question1.get(i));
                        params.put("secret_question2", arr_question2.get(i));
                        params.put("secret_question3", arr_question3.get(i));
                        params.put("secret_question4", arr_question4.get(i));
                        params.put("secret_question5", arr_question5.get(i));
                        params.put("religion", arr_religion.get(i));
                        params.put("address", arr_address.get(i));
                        params.put("gender", arr_gender.get(i));
                        params.put("class", arr_class.get(i));
                        params.put("shoe", arr_shoe_size.get(i));
                        params.put("height", arr_height.get(i));
                        params.put("weight", arr_weight.get(i));
                        params.put("origin", arr_origin.get(i));
                        params.put("schoolname", arr_schoolname.get(i));
                        params.put("school_lga", arr_lga.get(i));
                        params.put("created_by", arr_createdby.get(i));
                        params.put("tesis", arr_tesis_number.get(i));
                        return params;
                    }

                    /*
                     *pass files using below method
                     * */
                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        params.put("picture", new DataPart("picture" , arr_picture.get(i)));
                        params.put("fingerprint", new DataPart("fingerprint" , arr_fingerprint.get(i)));
                        params.put("signature", new DataPart("signature" , arr_signature.get(i)));
                        return params;
                    }
                };


                volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                rQueue = Volley.newRequestQueue(context);
                rQueue.add(volleyMultipartRequest);
            }
        });


        return convertView;
    }
}
