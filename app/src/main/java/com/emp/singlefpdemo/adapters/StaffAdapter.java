package com.emp.singlefpdemo.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.emp.singlefpdemo.MyDatabaseHelper;
import com.emp.singlefpdemo.MyDatabaseHelperStaff;
import com.emp.singlefpdemo.R;
import com.emp.singlefpdemo.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class StaffAdapter extends BaseAdapter {

    MyDatabaseHelperStaff myDB;
    private RequestQueue rQueue;

    public Context context;
    private ArrayList<Integer> arr_id;
    private ArrayList<String> arr_firstname;
    private ArrayList<String> arr_surname;
    private ArrayList<String> arr_othername;
    private ArrayList<String> arr_dob;
    private ArrayList<String> arr_email;
    private ArrayList<String> arr_phone;
    private ArrayList<String> arr_nin;
    private ArrayList<String> arr_qualification;
    private ArrayList<String> arr_question1;
    private ArrayList<String> arr_question2;
    private ArrayList<String> arr_question3;
    private ArrayList<String> arr_question4;
    private ArrayList<String> arr_religion;
    private ArrayList<String> arr_address;
    private ArrayList<String> arr_gender;
    private ArrayList<String> arr_nok;
    private ArrayList<String> arr_referee;
    private ArrayList<String> arr_subject;
    private ArrayList<String> arr_experience;
    private ArrayList<String> arr_school;
    private ArrayList<String> arr_schoollga;
    private ArrayList<String> arr_createdby;
    private ArrayList<String> arr_psnnumber;
    private ArrayList<byte[]> arr_picture;
    private ArrayList<byte[]> arr_fingerprint;
    private ArrayList<byte[]> arr_signature;
    private ArrayList<byte[]> arr_idcard;
    private ArrayList<byte[]> arr_employment;
    private ArrayList<byte[]> arr_promotion;
    private ArrayList<String> arr_category;

    String endpoint;

    public StaffAdapter(Context context, ArrayList<Integer> arr_id, ArrayList<String> arr_firstname, ArrayList<String> arr_surname,
                          ArrayList<String> arr_othername, ArrayList<String> arr_dob, ArrayList<String> arr_email, ArrayList<String> arr_phone,
                          ArrayList<String> arr_nin, ArrayList<String> arr_qualification, ArrayList<String> arr_question1,
                          ArrayList<String> arr_question2, ArrayList<String> arr_question3, ArrayList<String> arr_question4,
                          ArrayList<String> arr_religion, ArrayList<String> arr_address, ArrayList<String> arr_gender, ArrayList<String> arr_nok,
                        ArrayList<String> arr_referee, ArrayList<String> arr_subject, ArrayList<String> arr_experience, ArrayList<String> arr_school,
                        ArrayList<String> arr_schoollga, ArrayList<String> arr_createdby, ArrayList<String> arr_psnnumber, ArrayList<byte[]> arr_picture,
                        ArrayList<byte[]> arr_fingerprint, ArrayList<byte[]> arr_signature, ArrayList<byte[]> arr_idcard, ArrayList<byte[]> arr_employment,
                        ArrayList<byte[]> arr_promotion, ArrayList<String> arr_category){
        //Getting all the values
        this.context = context;
        this.arr_id = arr_id;
        this.arr_firstname = arr_firstname;
        this.arr_surname = arr_surname;
        this.arr_othername = arr_othername;
        this.arr_dob = arr_dob;
        this.arr_email = arr_email;
        this.arr_phone= arr_phone;
        this.arr_nin = arr_nin;
        this.arr_qualification = arr_qualification;
        this.arr_question1 = arr_question1;
        this.arr_question2 = arr_question2;
        this.arr_question3 = arr_question3;
        this.arr_question4 = arr_question4;
        this.arr_religion = arr_religion;
        this.arr_address = arr_address;
        this.arr_gender = arr_gender;
        this.arr_nok = arr_nok;
        this.arr_referee = arr_referee;
        this.arr_subject = arr_subject;
        this.arr_experience = arr_experience;
        this.arr_school = arr_school;
        this.arr_schoollga = arr_schoollga;
        this.arr_createdby = arr_createdby;
        this.arr_psnnumber = arr_psnnumber;
        this.arr_picture = arr_picture;
        this.arr_fingerprint = arr_fingerprint;
        this.arr_signature = arr_signature;
        this.arr_idcard = arr_idcard;
        this.arr_employment = arr_employment;
        this.arr_promotion = arr_promotion;
        this.arr_category = arr_category;
    }

    @Override
    public int getCount() {
        return arr_firstname.size();
    }

    @Override
    public Object getItem(int i) {
        return arr_firstname.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_saved_staff, parent, false);
        }

        myDB = new MyDatabaseHelperStaff(context);

        RelativeLayout container = convertView.findViewById(R.id.container);
        LinearLayout buttons = convertView.findViewById(R.id.buttons);
        CircleImageView userimg = convertView.findViewById(R.id.userimg);
        TextView name = convertView.findViewById(R.id.name);
        TextView dob = convertView.findViewById(R.id.dob);
        TextView psnNumber = convertView.findViewById(R.id.psnNumber);
        TextView qualification = convertView.findViewById(R.id.qualification);
        TextView category = convertView.findViewById(R.id.category);
        TextView gender = convertView.findViewById(R.id.gender);
        TextView schoolname = convertView.findViewById(R.id.schoolname);
        Button sync = convertView.findViewById(R.id.sync);
        Button delete = convertView.findViewById(R.id.delete);
        ProgressBar progressBar = convertView.findViewById(R.id.progressBar);
        ImageView check = convertView.findViewById(R.id.check);

        Glide.with(context).load(arr_picture.get(i)).into(userimg);
        name.setText(arr_firstname.get(i)+" "+arr_surname.get(i));
        dob.setText(arr_dob.get(i));
        psnNumber.setText(arr_psnnumber.get(i));
        qualification.setText(arr_qualification.get(i));
        category.setText(arr_category.get(i));
        gender.setText(arr_gender.get(i));
        schoolname.setText(arr_school.get(i));

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sync.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                if (arr_category.get(i).equals("teacher")){
                    endpoint = "https://taraenroll.com/api/v1/add_teacher";
                }else if (arr_category.get(i).equals("school head")){
                    endpoint = "https://taraenroll.com/api/v1/add_school_head";
                }else if (arr_category.get(i).equals("principal")){
                    endpoint = "https://taraenroll.com/api/v1/add_principal";
                }


                System.out.println("Sending \n"+arr_firstname.get(i)
                +"\n"+arr_surname.get(i)
                        +"\n"+arr_othername.get(i)
                        +"\n"+arr_dob.get(i)
                        +"\n"+arr_email.get(i)
                        +"\n"+arr_phone.get(i)
                        +"\n"+arr_nin.get(i)
                        +"\n"+arr_qualification.get(i)
                        +"\n"+arr_question1.get(i)
                        +"\n"+arr_question2.get(i)
                        +"\n"+arr_question3.get(i)
                        +"\n"+arr_question4.get(i)
                        +"\n"+arr_religion.get(i)
                        +"\n"+arr_address.get(i)
                        +"\n"+arr_gender.get(i)
                        +"\n"+arr_nok.get(i)
                        +"\n"+arr_referee.get(i)
                        +"\n"+arr_subject.get(i)
                        +"\n"+arr_experience.get(i)
                        +"\n"+arr_school.get(i)
                        +"\n"+arr_schoollga.get(i)
                        +"\n"+arr_createdby.get(i)
                        +"\n"+arr_psnnumber.get(i)
                        +"\n"+arr_picture.get(i)
                        +"\n"+arr_fingerprint.get(i)
                        +"\n"+arr_signature.get(i)
                        +"\n"+arr_idcard.get(i)
                        +"\n"+arr_employment.get(i)
                        +"\n"+arr_promotion.get(i));

                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, endpoint,
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
                                        buttons.setVisibility(View.GONE);
                                        progressBar.setVisibility(View.GONE);
                                        check.setVisibility(View.VISIBLE);
                                        //delete record from DB
                                        myDB.deleteRecord(arr_id.get(i));
//                                        count_local.setText(String.valueOf(myDB.getRecordCount()));

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
                        params.put("email", arr_email.get(i));
                        params.put("phone", arr_phone.get(i));
                        params.put("nin", arr_nin.get(i));
                        params.put("qualification", arr_qualification.get(i));
                        params.put("question1", arr_question1.get(i));
                        params.put("question2", arr_question2.get(i));
                        params.put("question3", arr_question3.get(i));
                        params.put("question4", arr_question4.get(i));
                        params.put("religion", arr_religion.get(i));
                        params.put("address", arr_address.get(i));
                        params.put("gender", arr_gender.get(i));
                        params.put("next_of_kin", arr_nok.get(i));
                        params.put("referee", arr_referee.get(i));
                        params.put("subjects", arr_subject.get(i));
                        params.put("experience", arr_experience.get(i));
                        params.put("schoolname", arr_school.get(i));
                        params.put("school_lga", arr_schoollga.get(i));
                        params.put("created_by", arr_createdby.get(i));
                        params.put("psn", arr_psnnumber.get(i));
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
                        params.put("idcard", new DataPart("idcard" , arr_idcard.get(i)));
                        params.put("employmentletter", new DataPart("employmentletter" , arr_employment.get(i)));
                        params.put("promotionletter", new DataPart("promotionletter" , arr_promotion.get(i)));
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
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog myDialog = new Dialog(context);
                myDialog.setContentView(R.layout.custom_popup_delete_record);

                EditText validation_code = myDialog.findViewById(R.id.validation_code);
                Button confirm = myDialog.findViewById(R.id.confirm_button);

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkValidity(validation_code.getText().toString(), arr_id.get(i), myDialog, validation_code, container);
                    }
                });

                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();
            }
        });


        return convertView;
    }

    private void checkValidity(String code, int id, Dialog dialog, EditText edtval, RelativeLayout container) {
        if (code.equals("419205")){
            //delete the record
            myDB.deleteRecord(arr_id.get(id));
            dialog.dismiss();
            Toast.makeText(context, "Successfully deleted record", Toast.LENGTH_SHORT).show();
            int backgroundColor = ContextCompat.getColor(context, R.color.red);
            ColorDrawable colorDrawable = new ColorDrawable(backgroundColor);
            container.setBackground(colorDrawable);
        }else{
            Toast.makeText(context, "Wrong code!!! Please try again", Toast.LENGTH_SHORT).show();
            edtval.setError("Wrong code");
        }
    }
}
