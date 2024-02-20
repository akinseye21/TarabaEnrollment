package com.emp.singlefpdemo;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.emp.printerdemo.ui.PrintActivity;
import com.emp.singlefpdemo.adapters.StaffAdapter;
import com.emp.singlefpdemo.adapters.StudentAdapter;
import com.emp.xdcommon.common.io.FileUtils;
import com.emp.xdfp.SFingerManager;
import com.example.jy.demo.fingerprint.CallDecoder;
import com.example.jy.demo.fingerprint.CallFprint;
import com.google.android.material.textfield.TextInputLayout;
import com.kyanogen.signatureview.SignatureView;
import com.szsicod.print.utils.BitmapUtils;
import com.techshino.fingerprint.CaptureListener;
import com.techshino.fingerprint.FingerDeviceStatusListener;
import com.xiongdi.natives.IoControl;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SFingerActivity extends BaseActivity implements FingerDeviceStatusListener, CaptureListener {

    SharedPreferences preferences, preferences2;


    MyDatabaseHelper myDB;
    MyDatabaseHelperStaff myDB_Staff;
    private RequestQueue rQueue;
    int i;
    int count=0;
    ByteArrayOutputStream baos = null;
    ByteArrayOutputStream baos2 = null;
    ByteArrayOutputStream baos3 = null;
//    Import from other code


    TextView defaultLGA, defaultLGA2, defaultSchool, defaultSchool2;
    TextView username;
    LinearLayout leftSide, middle;
    LinearLayout studentEnrollment, staffEnrollment, dataSync;
    RelativeLayout tab1, tab2, tab5;
    LinearLayout next;
    LinearLayout container3;
    ScrollView container1, container2, container4;
    LinearLayout topLoader, complete, end;
    RelativeLayout container5, container6, container7;
    RelativeLayout bar1, bar2, bar3, bar4, bar5, bar6, bar7;

    Spinner spinner_lga, spinner_school_type, spinner_school, spinner_class;

    //student location
    ArrayAdapter<String> lgaspinnerAdapter, schooltypespinnerAdapter;
    ArrayList<String> primary_Schools = new ArrayList<>();
    ArrayList<String> secondary_Schools = new ArrayList<>();
    ArrayList<String> taraba_lga = new ArrayList<>();
    String selected_taraba_lga = "", selected_taraba_schooltype="";
    ArrayList<String> populated_school = new ArrayList<>();
    String lga="", schoolType="", schoolClass="", school="";


    //student bio data
    ArrayAdapter<String> schoolspinnerAdapter, classspinnerAdapter;
    ArrayAdapter<String> genderAdapter, religionAdapter;
    TextInputLayout til_surname, til_firstname, til_othername, til_dob, til_nin, til_address, til_phone, til_email, til_shoe, til_height, til_weight;
    EditText edt_surname, edt_firstname, edt_othername, edt_dob, edt_nin, edt_address, edt_phone, edt_email, edt_shoe, edt_height, edt_weight;
    ImageView img_surname, img_firstname, img_othername, img_soo, img_lga, img_dob, img_gender, img_nin, img_address, img_phone, img_email, img_religion, img_shoe, img_height, img_weight;
    Spinner spinner_gender, spinner_religion, spinner_soo, spinner_lga2;
    DatePickerDialog datePickerDialog;
    Boolean surname=false, firstname=false, othername=false, state=false, lga2=false, dob=false, gender=false, nin=false, address=false, phone=false;
    Boolean religion=false, shoe=false, height=false, weight=false;
    ArrayList<String> state_list = new ArrayList<>();
    String selected_state = "";
    ArrayList<String> lga_list = new ArrayList<>();
    String selected_lga = "";
    String str_surname="", str_firstname="", str_othername="", str_dob="", str_nin="", str_address="", str_phone="", str_email="", str_shoe="", str_height="", str_weight="";
    String str_gender="", str_religion="", str_soo="", str_lga2="";

    //parent data
    TextInputLayout til_parentname, til_parentphone, til_parentrelationship, til_parentaddress;
    EditText edt_parentname, edt_parentphone, edt_parentrelationship, edt_parentaddress;
    ImageView img_parentname, img_parentphone, img_parentrelationship, img_parentaddress;
    Boolean parentname=false, parentphone=false, parentrelationship=false, parentaddress=false;
    String str_parentname="", str_parentphone="", str_parentrelationship="", str_parentaddress="";

    //security questions
    EditText edt_security1, edt_security2, edt_security3, edt_security4;
    String str_security1="", str_security2="", str_security3="", str_security4="";

    //portrait capture
    Button openCamera;
    ImageView mPreview;
    Bitmap bitmap;
    Uri portrait_uri;
    String encodedImageString;
    byte[] b;

    //fingerprint
    String encodedFingerprintString;
    byte[] b_finger;
    Bitmap bitmapfinger;

    //signature capture
    SignatureView signatureView;
    Bitmap bitmap_signature;
    String encodedSignatureString;
    byte[] b_signature;

    //enrollment verification
    ImageView finalImage, finalImage2, finalFingerprint, finalSignature, finalSignature2;
    TextView finalName, finalAddress, finalName2, finalAddress2, finalDOB, dateEnrolled, finalGender, finalState, finalClass, tesisNumber, finalHeight;

    //end
    Button btn_addstudent, btn_end;
    Button printslip;

    //data sync page
    TextView count_local, count_sync, count_local_staff;
    TextView back_txt, back_txt_staff;
    ListView myList, myList_staff;
    TextView speedtest, uploadspeed, downloadspeed;
    Button btn_data_sync;
    LinearLayout allrecords, allrecordsStaff, default_sync;
    Bitmap pictureDB, signatureDB, fingerprintDB;
    //create Array to store all student records in
    ArrayList<Integer> arr_id = new ArrayList<>();
    ArrayList<String> arr_firstname = new ArrayList<>();
    ArrayList<String> arr_surname = new ArrayList<>();
    ArrayList<String> arr_othername = new ArrayList<>();
    ArrayList<String> arr_dob = new ArrayList<>();
    ArrayList<String> arr_guardianname = new ArrayList<>();
    ArrayList<String> arr_guardianphone = new ArrayList<>();
    ArrayList<String> arr_guardianaddress = new ArrayList<>();
    ArrayList<String> arr_guardianrelationship = new ArrayList<>();
    ArrayList<String> arr_question1 = new ArrayList<>();
    ArrayList<String> arr_question2 = new ArrayList<>();
    ArrayList<String> arr_question3 = new ArrayList<>();
    ArrayList<String> arr_question4 = new ArrayList<>();
    ArrayList<String> arr_question5 = new ArrayList<>();
    ArrayList<String> arr_religion = new ArrayList<>();
    ArrayList<String> arr_address = new ArrayList<>();
    ArrayList<byte[]> arr_picture = new ArrayList<>();
    ArrayList<byte[]> arr_fingerprint = new ArrayList<>();
    ArrayList<byte[]> arr_signature = new ArrayList<>();
    ArrayList<String> arr_gender = new ArrayList<>();
    ArrayList<String> arr_class = new ArrayList<>();
    ArrayList<String> arr_shoe_size = new ArrayList<>();
    ArrayList<String> arr_height = new ArrayList<>();
    ArrayList<String> arr_weight = new ArrayList<>();
    ArrayList<String> arr_origin = new ArrayList<>();
    ArrayList<String> arr_schoolname = new ArrayList<>();
    ArrayList<String> arr_lga = new ArrayList<>();
    ArrayList<String> arr_createdby = new ArrayList<>();
    ArrayList<String> arr_tesis_number = new ArrayList<>();

    //create Array to store all staff records in
    ArrayList<Integer> arr_staff_id = new ArrayList<>();
    ArrayList<String> arr_staff_firstname = new ArrayList<>();
    ArrayList<String> arr_staff_surname = new ArrayList<>();
    ArrayList<String> arr_staff_othername = new ArrayList<>();
    ArrayList<String> arr_staff_dob = new ArrayList<>();
    ArrayList<String> arr_staff_email = new ArrayList<>();
    ArrayList<String> arr_staff_phone = new ArrayList<>();
    ArrayList<String> arr_staff_nin = new ArrayList<>();
    ArrayList<String> arr_staff_qualification = new ArrayList<>();
    ArrayList<String> arr_staff_question1 = new ArrayList<>();
    ArrayList<String> arr_staff_question2 = new ArrayList<>();
    ArrayList<String> arr_staff_question3 = new ArrayList<>();
    ArrayList<String> arr_staff_question4 = new ArrayList<>();
    ArrayList<String> arr_staff_religion = new ArrayList<>();
    ArrayList<String> arr_staff_address = new ArrayList<>();
    ArrayList<String> arr_staff_gender = new ArrayList<>();
    ArrayList<String> arr_staff_nok = new ArrayList<>();
    ArrayList<String> arr_staff_referee = new ArrayList<>();
    ArrayList<String> arr_staff_subject = new ArrayList<>();
    ArrayList<String> arr_staff_experience = new ArrayList<>();
    ArrayList<String> arr_staff_school = new ArrayList<>();
    ArrayList<String> arr_staff_schoollga = new ArrayList<>();
    ArrayList<String> arr_staff_createdby = new ArrayList<>();
    ArrayList<String> arr_staff_psnnumber = new ArrayList<>();
    ArrayList<byte[]> arr_staff_picture = new ArrayList<>();
    ArrayList<byte[]> arr_staff_fingerprint = new ArrayList<>();
    ArrayList<byte[]> arr_staff_signature = new ArrayList<>();
    ArrayList<byte[]> arr_staff_idcard = new ArrayList<>();
    ArrayList<byte[]> arr_staff_employment = new ArrayList<>();
    ArrayList<byte[]> arr_staff_promotion = new ArrayList<>();
    ArrayList<String> arr_staff_category = new ArrayList<>();
    ArrayList<String> arr_bank_name = new ArrayList<>();
    ArrayList<String> arr_acc_number = new ArrayList<>();
    ArrayList<String> arr_acc_name = new ArrayList<>();
    ArrayList<String> bank_list = new ArrayList<>();



    TextView back, home;
    LinearLayout logout1, logout2, logout3;
    String uid="", from="";
    TextView totalReg, totalSchools, todaysReg, previousDay;
    String tesis="";




    //Staff Enrollment
    TextView txt_staff_lga, txt_staff_schooltype, txt_staff_school;
    LinearLayout next2, staff_toploader;
    ScrollView staffContainer1, staffContainer2, staffContainer4, staffContainer5, staffContainer6, staffContainer7, staffContainer8;
    LinearLayout staffDashboard, staffContainer3, staffContainerAccount;
    RelativeLayout staffContainer9, staffContainer10;
    RelativeLayout staff_bar1, staff_bar2, staff_bar3, staff_bar4, staff_bar5, staff_bar6, staff_bar7, staff_bar8, staff_bar9, staff_bar10, staff_bar_account;
    LinearLayout teacherForm, schoolHeadForm, principalForm;
//    Spinner spinner_staff_lga, spinner_staff_schooltype, spinner_staff_school;
    String selected_staff_lga = "", selected_staff_schooltype="", selected_staff_school="";
    String staff_lga = "", staff_schoolType = "", staff_school = "";
    String selected_staff_type = "";
    EditText edt_staff_surname, edt_staff_firstname, edt_staff_dob, edt_staff_phone, edt_staff_nin, edt_staff_email;
    Spinner spinner_staff_gender;
    TextInputLayout til_staff_surname, til_staff_firstname, til_staff_dob, til_staff_phone, til_staff_nin, til_staff_email;
    ImageView img_staff_surname, img_staff_firstname, img_staff_dob, img_staff_phone, img_staff_nin, img_staff_email, img_staff_gender;
    Boolean staff_surname=false, staff_firstname=false, staff_gender=false, staff_dob=false, staff_phone=false, staff_nin=false, staff_email=false;
    String str_staff_surname="", str_staff_firstname="", str_staff_gender="", str_staff_dob="", str_staff_phone="", str_staff_nin="", str_staff_email="";
    EditText edt_psnNum;
    TextInputLayout til_psnNum;
    ImageView img_psnNum;
    Boolean staff_psnNum=false;
    String str_staff_psnNum="";
    Spinner spinner_postgraduate, spinner_tertiary, spinner_secondary;
    String edu_quality="";
    EditText edt_staff_experience, edt_subject;
    ImageView img_postgraduate, img_tertiary, img_secondary, img_staff_experience, img_staff_subject;
    String str_postgrad="", str_tertiary="", str_secondary="", str_staff_subject="", str_staff_experience="";
    TextInputLayout til_staff_experience, til_staff_subject;
    EditText edt_nok_fullname, edt_nok_phone, edt_nok_relationship, edt_nok_address;
    ImageView img_nok_fullname, img_nok_phone, img_nok_relationship, img_nok_address;
    Boolean nok_fullname=false, nok_phone=false, nok_relationship=false, nok_address=false;
    String str_nok_fullname="", str_nok_phone="", str_nok_relationship="", str_nok_address="";
    TextInputLayout til_nok_fullname, til_nok_phone, til_nok_relationship, til_nok_address;
    EditText edt_ref1_fullname, edt_ref1_phone, edt_ref1_position, edt_ref1_organisation;
    EditText edt_ref2_fullname, edt_ref2_phone, edt_ref2_position, edt_ref2_organisation;
    ImageView img_ref1_fullname, img_ref1_phone, img_ref1_position, img_ref1_organisation;
    ImageView img_ref2_fullname, img_ref2_phone, img_ref2_position, img_ref2_organisation;
    String str_ref1_fullname="", str_ref1_phone="", str_ref1_position="", str_ref1_organisation="";
    String str_ref2_fullname="", str_ref2_phone="", str_ref2_position="", str_ref2_organisation="";
    Boolean referee1=false, referee2=false;
    TextView question, questionnumber;
    RelativeLayout option1, option2, option3, option4;
    TextView txt_option1, txt_option2, txt_option3, txt_option4;
    RadioButton radio_option1, radio_option2, radio_option3, radio_option4;
    TextView counter, nextQuestion;
    String answer="";
    int index = 1;
    RelativeLayout employmentLetter, promotionLetter, idCard;
    ImageView img_employmentLetter, img_promotionLetter, img_idCard;
    byte[] b_employment, b_promotion, b_idCard, b_staffPortrait;
    Button openCamera_staff;
    ImageView surfaceView_staff;
    ImageView prev_staff, captureIv_staff;
    TextView tip_staff, quality_staff;
    Button printslip_staff, btn_addstaff, btn_end_staff;
    ArrayList<String> arr_questions = new ArrayList<>();
    ArrayList<String> arr_responses = new ArrayList<>();
    int randomNumber1, randomNumber2;
    LinearLayout endStaff;
    EditText accNumber, accName;
    Spinner spinner_bank_list;
    Boolean staffAccountNumber=false, staffAccountName=false, staffBankName=false;
    String str_staff_accnum="", str_staff_accname="", str_staff_bankname="";
    ArrayAdapter<String> bankAdapter;

//    Import ends here


    Button captureBtn;
    Button verifyBtn;
    TextView verify_tv;
    TextView tip;
    ImageView prev;
    TextView quality;
    ImageView captureIv;
    private SFingerManager mApi;
    private ProgressDialog mDialog;
    private CompositeDisposable compositeDisposable;

    private String srcfile = AppConfig.DATA_PATH + File.separator + "1.bmp";
    private String dstfile = AppConfig.DATA_PATH + File.separator + "2.bmp";
    private boolean verifyMode;
    private static final int threadHold = 22;


    @Override
    protected void initViews(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        uid = i.getStringExtra("userid");
        from = i.getStringExtra("from");


        defaultLGA = findViewById(R.id.defaultLGA);
        defaultSchool = findViewById(R.id.defaultSchool);
        defaultLGA2 = findViewById(R.id.defaultLGA2);
        defaultSchool2 = findViewById(R.id.defaultSchool2);

        captureBtn = findViewById(R.id.capture_btn);
        verifyBtn = findViewById(R.id.verify_btn);
        verify_tv = findViewById(R.id.verify_Tv);
        tip = findViewById(R.id.tip);
        prev = findViewById(R.id.prev);
        quality = findViewById(R.id.quality);
        captureIv = findViewById(R.id.captureIv);


        totalReg = findViewById(R.id.totalRegistration);
        totalSchools = findViewById(R.id.totalSchools);
        todaysReg = findViewById(R.id.todaysRegistration);
        previousDay = findViewById(R.id.previousDay);
        logout1 = findViewById(R.id.logout1);
        logout2 = findViewById(R.id.logout2);
        logout3 = findViewById(R.id.logout3);
        logout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        logout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        logout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        getDashboardData();

        username = findViewById(R.id.username);
        username.setText("Hello, "+uid+"!");
        leftSide = findViewById(R.id.leftside);
        middle = findViewById(R.id.middle);
        tab1 = findViewById(R.id.tab1);
        next = findViewById(R.id.next);

        state_list.add("State of Origin");
        lga_list.add("Local Government Area");


        tab2 = findViewById(R.id.tab2);
        tab5 = findViewById(R.id.tab5);

        signatureView = findViewById(R.id.signature_view);
        prev_staff = findViewById(R.id.prev_staff);
        tip_staff = findViewById(R.id.tip_staff);
        quality_staff = findViewById(R.id.quality_staff);
        captureIv_staff = findViewById(R.id.captureIv_staff);

        topLoader = findViewById(R.id.topLoader);
        container1 = findViewById(R.id.container1);
        container2 = findViewById(R.id.container2);
        container3 = findViewById(R.id.container3);
        container4 = findViewById(R.id.container4);
        container5 = findViewById(R.id.container5);
        container6 = findViewById(R.id.container6);
        container7 = findViewById(R.id.container7);
        complete = findViewById(R.id.complete);
        end = findViewById(R.id.end);

        bar1 = findViewById(R.id.bar1);
        bar2 = findViewById(R.id.bar2);
        bar3 = findViewById(R.id.bar3);
        bar4 = findViewById(R.id.bar4);
        bar5 = findViewById(R.id.bar5);
        bar6 = findViewById(R.id.bar6);
        bar7 = findViewById(R.id.bar7);

        //staff enrollment
        txt_staff_lga = findViewById(R.id.staff_lga);
        txt_staff_schooltype = findViewById(R.id.staff_schooltype);
        txt_staff_school = findViewById(R.id.staff_school);
        next2 = findViewById(R.id.next2);
        staff_toploader = findViewById(R.id.staff_topLoader);
        staff_bar1 = findViewById(R.id.staff_bar1);
        staff_bar2 = findViewById(R.id.staff_bar2);
        staff_bar3 = findViewById(R.id.staff_bar3);
        staff_bar4 = findViewById(R.id.staff_bar4);
        staff_bar5 = findViewById(R.id.staff_bar5);
        staff_bar6 = findViewById(R.id.staff_bar6);
        staff_bar7 = findViewById(R.id.staff_bar7);
        staff_bar8 = findViewById(R.id.staff_bar8);
        staff_bar9 = findViewById(R.id.staff_bar9);
        staff_bar10 = findViewById(R.id.staff_bar10);
        staff_bar_account = findViewById(R.id.staff_bar_account);
        staffContainer1 = findViewById(R.id.staff_container1);
        staffContainer2 = findViewById(R.id.staff_container2);
        staffContainer3 = findViewById(R.id.staff_container3);
        staffContainer4 = findViewById(R.id.staff_container4);
        staffContainer5 = findViewById(R.id.staff_container5);
        staffContainer6 = findViewById(R.id.staff_container6);
        staffContainer7 = findViewById(R.id.staff_container7);
        staffContainer8 = findViewById(R.id.staff_container8);
        staffContainer9 = findViewById(R.id.staff_container9);
        staffContainer10 = findViewById(R.id.staff_container10);
        staffContainerAccount = findViewById(R.id.staff_container_account);
        staffDashboard = findViewById(R.id.staff_dashboard);
        teacherForm = findViewById(R.id.teachersForm);
        schoolHeadForm = findViewById(R.id.headofschoolform);
        principalForm = findViewById(R.id.principalsform);
        teacherForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_staff_type = "teacher";
                staffPortal(selected_staff_type);
            }
        });
        schoolHeadForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_staff_type = "school head";
                staffPortal(selected_staff_type);
            }
        });
        principalForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_staff_type = "principal";
                staffPortal(selected_staff_type);
            }
        });
//        spinner_staff_lga = findViewById(R.id.spinner_staff_lga);
//        spinner_staff_schooltype = findViewById(R.id.spinner_staff_schooltype);
//        spinner_staff_school = findViewById(R.id.spinner_staff_school);

        //enrollment verification
        finalImage = findViewById(R.id.finalImage);
        finalImage2 = findViewById(R.id.finalImage2);
        finalFingerprint = findViewById(R.id.finalFingerprint);
        finalSignature = findViewById(R.id.finalSignature);
        finalSignature2 = findViewById(R.id.finalSignature2);
        finalName = findViewById(R.id.finalName);
        finalName2 = findViewById(R.id.finalName2);
        finalAddress = findViewById(R.id.finalAddress);
        finalAddress2 = findViewById(R.id.finalAddress2);
        finalDOB = findViewById(R.id.finalDOB);
        dateEnrolled = findViewById(R.id.dateEnrolled);
        finalGender = findViewById(R.id.finalGender);
        finalState = findViewById(R.id.finalState);
        finalClass = findViewById(R.id.finalClass);
        tesisNumber = findViewById(R.id.tesisNumber);
        finalHeight = findViewById(R.id.finalHeight);

        readLGAForTab1();
        student_bio_data();

        readPrimarySchoolFromExcel();
        readSecondarySchoolFromExcel();
        readStateLgaFromExcel();
        readLgaFromExcel(selected_lga);

        myDB = new MyDatabaseHelper(SFingerActivity.this);
        myDB_Staff = new MyDatabaseHelperStaff(SFingerActivity.this);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tab1.getVisibility()==View.VISIBLE){
                    tab1.setVisibility(View.GONE);
                    middle.setVisibility(View.VISIBLE);

                }
                if (container2.getVisibility()==View.VISIBLE && tab1.getVisibility()==View.GONE){
                    tab1.setVisibility(View.VISIBLE);
                    container2.setVisibility(View.GONE);
                    bar2.setBackgroundResource(R.drawable.quote2);
                    container1.setVisibility(View.VISIBLE);
                }
                if (container3.getVisibility()==View.VISIBLE && tab1.getVisibility()==View.GONE){
                    tab1.setVisibility(View.VISIBLE);
                    container3.setVisibility(View.GONE);
                    bar3.setBackgroundResource(R.drawable.quote2);
                    container2.setVisibility(View.VISIBLE);
                }
                if (container4.getVisibility()==View.VISIBLE && tab1.getVisibility()==View.GONE){
                    tab1.setVisibility(View.VISIBLE);
                    container4.setVisibility(View.GONE);
                    bar4.setBackgroundResource(R.drawable.quote2);
                    container3.setVisibility(View.VISIBLE);
                }
                if (container5.getVisibility()==View.VISIBLE && tab1.getVisibility()==View.GONE){
                    tab1.setVisibility(View.VISIBLE);
                    container5.setVisibility(View.GONE);
                    bar5.setBackgroundResource(R.drawable.quote2);
                    container4.setVisibility(View.VISIBLE);
                }
                if (container6.getVisibility()==View.VISIBLE && tab1.getVisibility()==View.GONE){
                    tab1.setVisibility(View.VISIBLE);
                    container6.setVisibility(View.GONE);
                    bar6.setBackgroundResource(R.drawable.quote2);
                    container5.setVisibility(View.VISIBLE);
                }
                if (container7.getVisibility()==View.VISIBLE && tab1.getVisibility()==View.GONE){
                    tab1.setVisibility(View.VISIBLE);
                    container7.setVisibility(View.GONE);
                    bar7.setBackgroundResource(R.drawable.quote2);
                    container6.setVisibility(View.VISIBLE);
                }
                if (complete.getVisibility()==View.VISIBLE && tab1.getVisibility()==View.GONE){
                    tab1.setVisibility(View.VISIBLE);
                    complete.setVisibility(View.GONE);
                    topLoader.setVisibility(View.VISIBLE);
//                    bar7.setBackgroundResource(R.drawable.quote2);
                    container7.setVisibility(View.VISIBLE);
                }
                if (end.getVisibility()==View.VISIBLE && tab1.getVisibility()==View.GONE){
                    tab1.setVisibility(View.VISIBLE);
                    end.setVisibility(View.GONE);
                    complete.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (container1.getVisibility()==View.VISIBLE){
                    checkLocationInputValidity();
                }
                else if (container2.getVisibility()==View.VISIBLE){
                    if (surname && firstname && state && lga2 && dob && gender && address && religion && shoe && height){
                        container2.setVisibility(View.GONE);
                        bar3.setBackgroundResource(R.drawable.quote1);
                        container3.setVisibility(View.VISIBLE);
                        parent_data();
                    }else{
                        Toast.makeText(SFingerActivity.this, "Please check your inputs", Toast.LENGTH_SHORT).show();
                    }

                }
                else if (container3.getVisibility()==View.VISIBLE){
                    if (parentname && parentphone && parentrelationship && parentaddress){
                        container3.setVisibility(View.GONE);
                        bar4.setBackgroundResource(R.drawable.quote1);
                        container4.setVisibility(View.VISIBLE);

                        securityQuestion();
                    }else{
                        Toast.makeText(SFingerActivity.this, "Please check your inputs", Toast.LENGTH_SHORT).show();
                    }

                }
                else if (container4.getVisibility()==View.VISIBLE){
                    container4.setVisibility(View.GONE);
                    bar5.setBackgroundResource(R.drawable.quote1);
                    container5.setVisibility(View.VISIBLE);

                    str_security1 = edt_security1.getText().toString();
                    str_security2 = edt_security2.getText().toString();
                    str_security3 = edt_security3.getText().toString();
                    str_security4 = edt_security4.getText().toString();

                    portraitCapture();
                }
                else if (container5.getVisibility()==View.VISIBLE){
                    //if bitmap is not null, go to next container
                    if (mPreview.getDrawable() == null){
                        Toast.makeText(SFingerActivity.this, "Please snap a picture", Toast.LENGTH_SHORT).show();
                    }else{
                        container5.setVisibility(View.GONE);
                        bar6.setBackgroundResource(R.drawable.quote1);
                        container6.setVisibility(View.VISIBLE);

                        //指纹上电
                        IoControl.getInstance().setIoStatus(IoControl.USBFP_PATH, IoControl.IOSTATUS.ENABLE);
                        mApi = new SFingerManager(SFingerActivity.this);
                        openDevice(true);
                    }
                }
                else if (container6.getVisibility()==View.VISIBLE){
                    container6.setVisibility(View.GONE);
                    bar7.setBackgroundResource(R.drawable.quote1);
                    container7.setVisibility(View.VISIBLE);

//                    openDevice(false);
                }
                else if (container7.getVisibility()==View.VISIBLE){
                    signatureCapture();
//                    Bitmap bitmap = signatureView.getSignatureBitmap();
                    if (bitmap_signature != null){
                        container7.setVisibility(View.GONE);
                        topLoader.setVisibility(View.GONE);
                        complete.setVisibility(View.VISIBLE);

                        enrollmentVerification();
                    }else{
                        Toast.makeText(SFingerActivity.this, "Please give an e-signature", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (complete.getVisibility()==View.VISIBLE){
                    //store info in SQLITE DB
                    storeInSqliteDB();
                    complete.setVisibility(View.GONE);
                    end.setVisibility(View.VISIBLE);
                    next.setVisibility(View.GONE);
                }
            }
        });
        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (staffContainer1.getVisibility()==View.VISIBLE){
                    checkStaffLocationInputValidity();
                    staffContainer2.setVisibility(View.VISIBLE);
                    staffContainer1.setVisibility(View.GONE);
                    staff_bar2.setBackgroundResource(R.drawable.quote1);

                }
                else if (staffContainer2.getVisibility()==View.VISIBLE){
                    if (edt_staff_email.equals("")){
                        str_staff_email = "";
                    }
                    if (edt_staff_nin.equals("")){
                        str_staff_nin = "";
                    }

                    if (staff_surname && staff_firstname && staff_dob && staff_gender && staff_phone){
                        staffContainer2.setVisibility(View.GONE);
                        staff_bar3.setBackgroundResource(R.drawable.quote1);
                        staffContainer3.setVisibility(View.VISIBLE);
                        psnNumber();
                    }else{
                        Toast.makeText(SFingerActivity.this, "Please check your inputs", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (staffContainer3.getVisibility()==View.VISIBLE){

                    if (staff_psnNum){
                        staffContainer3.setVisibility(View.GONE);
                        staffContainerAccount.setVisibility(View.VISIBLE);
                        staff_bar_account.setBackgroundResource(R.drawable.quote1);

                        staffAccountInput();


//                        staffContainer3.setVisibility(View.GONE);
//                        staff_bar4.setBackgroundResource(R.drawable.quote1);
//                        staffContainer4.setVisibility(View.VISIBLE);
//
//                        eduQualification();
                    }else{
                        Toast.makeText(SFingerActivity.this, "Wrong PSN Number", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (staffContainerAccount.getVisibility()==View.VISIBLE){
                    if (staffAccountName && staffAccountNumber && staffBankName){
                        staffContainerAccount.setVisibility(View.GONE);
//                        staff_bar4.setVisibility(View.VISIBLE);
                        staffContainer4.setVisibility(View.VISIBLE);
                        staff_bar4.setBackgroundResource(R.drawable.quote1);

                        eduQualification();
                    }else{
                        Toast.makeText(SFingerActivity.this, "Please check your inputs again", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (staffContainer4.getVisibility()==View.VISIBLE){
                    if (str_staff_experience.equals("") || str_staff_subject.equals("")){
                        Toast.makeText(SFingerActivity.this, "Wrong input fields", Toast.LENGTH_SHORT).show();
                    }else{
                        staffContainer4.setVisibility(View.GONE);
                        staff_bar5.setBackgroundResource(R.drawable.quote1);
                        staffContainer5.setVisibility(View.VISIBLE);

                        edu_quality = str_postgrad+", "+str_tertiary+", "+str_secondary;

                        nextOfKin();
                    }

                }
                else if (staffContainer5.getVisibility()==View.VISIBLE){
                    staffContainer5.setVisibility(View.GONE);
                    staff_bar6.setBackgroundResource(R.drawable.quote1);
                    staffContainer6.setVisibility(View.VISIBLE);

                    references();
                }
                else if (staffContainer6.getVisibility()==View.VISIBLE){
                    if (!str_ref1_fullname.equals("") && !str_ref1_phone.equals("") && !str_ref1_position.equals("") && !str_ref1_organisation.equals("")){
                        referee1 = true;
                    }

                    if (!str_ref2_fullname.equals("") && !str_ref2_phone.equals("") && !str_ref2_position.equals("") && !str_ref2_organisation.equals("")){
                        referee2 = true;
                    }
                    
                    if (referee1 || referee2){
                        staffContainer6.setVisibility(View.GONE);
                        staff_bar7.setBackgroundResource(R.drawable.quote1);
                        staffContainer7.setVisibility(View.VISIBLE);

                        questionBank();
                        next2.setVisibility(View.GONE);
                    }else{
                        Toast.makeText(SFingerActivity.this, "Complete at least one reference.", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (staffContainer7.getVisibility()==View.VISIBLE){

                }
                else if (staffContainer8.getVisibility()==View.VISIBLE){
                    if (img_employmentLetter.getDrawable() == null && img_promotionLetter.getDrawable() == null && img_idCard.getDrawable() == null){
                        Toast.makeText(SFingerActivity.this, "Ensure you snapp necessary documents before proceeding", Toast.LENGTH_SHORT).show();
                    }else{
                        staffContainer8.setVisibility(View.GONE);
                        staff_bar9.setBackgroundResource(R.drawable.quote1);
                        staffContainer9.setVisibility(View.VISIBLE);

                        staffPortrait();
                    }

                }
                else if (staffContainer9.getVisibility()==View.VISIBLE){
                    if (surfaceView_staff.getDrawable() == null){
                        Toast.makeText(SFingerActivity.this, "Please snap staff picture", Toast.LENGTH_SHORT).show();
                    }else{
                        staffContainer9.setVisibility(View.GONE);
                        staff_bar10.setBackgroundResource(R.drawable.quote1);
                        staffContainer10.setVisibility(View.VISIBLE);

                        staffFingerprint();
                    }

                }
                else if (staffContainer10.getVisibility()==View.VISIBLE){
                    next2.setVisibility(View.GONE);
                    staff_toploader.setVisibility(View.GONE);
                    staffContainer10.setVisibility(View.GONE);
                    saveStaffToSQLite();
                    endStaffView();
                    checkButtonClicked();

                }
            }
        });

        openCamera = findViewById(R.id.openCamera);
        mPreview = findViewById(R.id.surfaceView);

        //side menu listener
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDashboardData();
                tab1.setVisibility(View.GONE);
                tab2.setVisibility(View.GONE);
                tab5.setVisibility(View.GONE);
                middle.setVisibility(View.VISIBLE);
//                rightSide.setVisibility(View.VISIBLE);
            }
        });
        studentEnrollment = findViewById(R.id.studentEnrollment);
        studentEnrollment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                middle.setVisibility(View.GONE);
//                rightSide.setVisibility(View.GONE);
                tab2.setVisibility(View.GONE);
                tab5.setVisibility(View.GONE);
                tab1.setVisibility(View.VISIBLE);
            }
        });
        dataSync = findViewById(R.id.dataSync);
        dataSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab1.setVisibility(View.GONE);
                tab2.setVisibility(View.GONE);
                tab5.setVisibility(View.VISIBLE);

                default_sync.setVisibility(View.VISIBLE);
                allrecords.setVisibility(View.GONE);
                count_local.setText(String.valueOf(myDB.getRecordCount()));
                count_local_staff.setText(String.valueOf(myDB_Staff.getRecordCount()));
            }
        });
        staffEnrollment = findViewById(R.id.staffEnrollment);
        staffEnrollment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences2 = getSharedPreferences("TempStorage", Context.MODE_PRIVATE);
                String pref_lga = preferences2.getString("lga", "");
                String pref_schooltype = preferences2.getString("schooltype", "");
                String pref_school = preferences2.getString("school", "");

                txt_staff_lga.setText("LGA: "+pref_lga);
                txt_staff_schooltype.setText("SCHOOL TYPE: "+pref_schooltype);
                txt_staff_school.setText("SCHOOL: "+pref_school);

                tab1.setVisibility(View.GONE);
                tab2.setVisibility(View.VISIBLE);
                tab5.setVisibility(View.GONE);

                next2.setVisibility(View.GONE);
                staff_toploader.setVisibility(View.GONE);
                staffDashboard.setVisibility(View.VISIBLE);
                staffContainer1.setVisibility(View.GONE);
                staffContainer2.setVisibility(View.GONE);
                staffContainer3.setVisibility(View.GONE);
                staffContainer4.setVisibility(View.GONE);
                staffContainer5.setVisibility(View.GONE);
                staffContainer6.setVisibility(View.GONE);
                staffContainer7.setVisibility(View.GONE);
                staffContainer8.setVisibility(View.GONE);
                staffContainer9.setVisibility(View.GONE);
                staffContainer10.setVisibility(View.GONE);
            }
        });

        //end process
        btn_addstudent = findViewById(R.id.btn_addstudent);
        btn_end = findViewById(R.id.btn_end);
        btn_addstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadActivity();
            }
        });
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadActivity();
            }
        });
        printslip = findViewById(R.id.printslip);
        printslip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SFingerActivity.this, PrintActivity.class);
                i.putExtra("Firstname", str_firstname);
                i.putExtra("Surname", str_surname);
                i.putExtra("Tesisnumber", tesis);
                i.putExtra("School", school);
                i.putExtra("userid", uid);
                startActivity(i);
            }
        });

        //data sync page
        default_sync = findViewById(R.id.default_sync);
        allrecords = findViewById(R.id.allrecords);
        allrecordsStaff = findViewById(R.id.allrecordsStaff);
        count_local = findViewById(R.id.count_local);
        count_local.setText(String.valueOf(myDB.getRecordCount()));
        count_local_staff = findViewById(R.id.count_local_staff);
        count_local_staff.setText(String.valueOf(myDB_Staff.getRecordCount()));
        count_sync = findViewById(R.id.count_sync);
        uploadspeed = findViewById(R.id.uploadspeed);
        downloadspeed = findViewById(R.id.downloadspeed);
        speedtest = findViewById(R.id.speedtest);
        speedtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networkCheck();
            }
        });
        btn_data_sync = findViewById(R.id.btn_data_sync);
        btn_data_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog myDialog = new Dialog(SFingerActivity.this);
                myDialog.setContentView(R.layout.custom_popup_category);
                Button student = myDialog.findViewById(R.id.student);
                Button staff = myDialog.findViewById(R.id.staff);
                student.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        default_sync.setVisibility(View.GONE);
                        allrecords.setVisibility(View.VISIBLE);
                        getAllRecords();
                        myDialog.dismiss();
                    }
                });
                staff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        default_sync.setVisibility(View.GONE);
                        allrecordsStaff.setVisibility(View.VISIBLE);
                        getAllStaffRecords();
                        myDialog.dismiss();
                    }
                });

                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(true);
                myDialog.show();




//                sendToServer();
                getDashboardData();
            }
        });

        if (from.equals("login")){
            showGlobalDialog();
        }else if (from.equals("refresh")){
            //do nothing
            //update the default lga name and school name
            preferences2 = getSharedPreferences("TempStorage", Context.MODE_PRIVATE);
            String pref_lga = preferences2.getString("lga", "");
            String pref_schooltype = preferences2.getString("schooltype", "");
            String pref_school = preferences2.getString("school", "");

            defaultLGA.setText(pref_lga);
            defaultSchool.setText(pref_school);
            defaultLGA2.setText(pref_lga);
            defaultSchool2.setText(pref_school);

            checker(pref_schooltype);
        }

    }



    private void showGlobalDialog() {
        Dialog myDialog = new Dialog(SFingerActivity.this);
        myDialog.setContentView(R.layout.custom_popup_onlogin);

        spinner_lga = myDialog.findViewById(R.id.spinner_lga);
        spinner_school_type = myDialog.findViewById(R.id.spinner_schooltype);
        spinner_school = myDialog.findViewById(R.id.spinner_school);
        Button btnnext = myDialog.findViewById(R.id.btnnext);

        lgaspinnerAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, taraba_lga);
        spinner_lga.setAdapter(lgaspinnerAdapter);
        spinner_lga.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_taraba_lga = spinner_lga.getSelectedItem().toString();
                readSchoolPerSelectedTarabaLga(selected_taraba_lga, selected_taraba_schooltype);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String school_type[] = {"Select School Type","Primary School", "Secondary School"};
        schooltypespinnerAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, school_type);
        spinner_school_type.setAdapter(schooltypespinnerAdapter);
        spinner_school_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position==1){
                    //school list should be primary school
                    populated_school.clear();
                    selected_taraba_schooltype = "Primary";
                    readSchoolPerSelectedTarabaLga(selected_taraba_lga, selected_taraba_schooltype);

                    schoolspinnerAdapter = new ArrayAdapter<>(SFingerActivity.this, R.layout.simple_spinner_item, R.id.tx, populated_school);
                    spinner_school.setAdapter(schoolspinnerAdapter);
//                    checker("Primary");
                }else if (position==2){
                    //school list should be secondary school
                    populated_school.clear();
                    selected_taraba_schooltype = "Secondary";
                    readSchoolPerSelectedTarabaLga(selected_taraba_lga, selected_taraba_schooltype);

                    schoolspinnerAdapter = new ArrayAdapter<>(SFingerActivity.this, R.layout.simple_spinner_item, R.id.tx, populated_school);
                    spinner_school.setAdapter(schoolspinnerAdapter);
//                    checker("Secondary");
                }else if (position==0){
                    //school list should be secondary school
                    populated_school.clear();

                    String school[] = {"Select School"};
                    schoolspinnerAdapter = new ArrayAdapter<>(SFingerActivity.this, R.layout.simple_spinner_item, R.id.tx, school);
                    spinner_school.setAdapter(schoolspinnerAdapter);
//                    checker("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (spinner_lga.getSelectedItem().toString().equals("Select Local Government Area") || spinner_lga.getSelectedItem().toString().equals("")){
                    lga = "";
                }else{
                    lga = spinner_lga.getSelectedItem().toString();
                }
                if (spinner_school_type.getSelectedItem().toString().equals("Select School Type") || spinner_school_type.getSelectedItem().toString().equals("")){
                    schoolType = "";
                }else{
                    schoolType = spinner_school_type.getSelectedItem().toString();
                }
                if (spinner_school.getSelectedItem().toString().equals("Select School") || spinner_school.getSelectedItem().toString().equals("")){
                    school = "";
                }else{
                    school = spinner_school.getSelectedItem().toString();
                }

                if (!lga.equals("") && !schoolType.equals("") && !school.equals("")){
                    String final_schooltype = "";
                    if (schoolType.equals("Primary School")){
                        final_schooltype = "Primary";
                    }else if(schoolType.equals("Secondary School")){
                        final_schooltype = "Secondary";
                    }

                    preferences = getSharedPreferences("TempStorage", Context.MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = preferences.edit();
                    myEdit.putString("lga", lga);
                    myEdit.putString("schooltype", final_schooltype);
                    myEdit.putString("school", school);
                    myEdit.commit();
                    myDialog.dismiss();

                    //update the default lga name and school name
                    defaultLGA.setText(lga);
                    defaultSchool.setText(school);
                    defaultLGA2.setText(lga);
                    defaultSchool2.setText(school);

                    student_location();

                }else{
                    Toast.makeText(SFingerActivity.this, "Please select the right options", Toast.LENGTH_SHORT).show();
                }



            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();
    }


    @Override
    protected void initDatas() {
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_s_finger;
    }

    public void openDevice(boolean isChecked) {
        if (isChecked) {
            startLoadding("Opening Device...");
            mApi.openFingerDevice(this);
        } else {
            mApi.release();
            captureBtn.setEnabled(false);
            verifyBtn.setEnabled(false);
            stopLoadding();
        }
    }

    public void startLoadding(String content) {
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
            mDialog.setCancelable(false);
        }
        mDialog.setMessage(content);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void stopLoadding() {
        if (mDialog != null && !isFinishing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void connect(int result) {
        captureBtn.setEnabled(true);
        Log.e(TAG, "FingerInit状态" + result);
        stopLoadding();
    }

    @Override
    public void disconnect() {
        stopLoadding();
        Log.e(TAG, "disconnect");
        showToast("disconnect");
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void captureSuccess(int NFIQ, byte[] img_Buffer, int[] size, byte[] templateBuffer) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(
                img_Buffer, 0, 256 * 360 + 1024 + 54);
        prev.setImageBitmap(bitmap);
        prev_staff.setImageBitmap(bitmap);
        finalFingerprint.setImageBitmap(bitmap);
        tip.setText(getString(R.string.capture_finish));
        tip_staff.setText(getString(R.string.capture_finish));
        Log.e(TAG, "NFIQ:" + NFIQ);
        quality.setText(String.valueOf((5 - NFIQ) * 20));
        quality_staff.setText(String.valueOf((5 - NFIQ) * 20));
        if (verifyMode) {
            startVerify(img_Buffer);
        } else {
            captureIv.setImageBitmap(bitmap);
            captureIv_staff.setImageBitmap(bitmap);
            register(img_Buffer);
        }

        //converting bitmap to bytearray
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        b_finger = baos.toByteArray();
    }

    @Override
    public void captureFail(int result, String failMsg) {
        tip.setText(failMsg);
        tip_staff.setText(failMsg);
        captureBtn.setEnabled(true);
    }

    @Override
    public void captureOnPress(int NFIQ, byte[] img_Buffer, int[] size) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(
                img_Buffer, 0, 256 * 360 + 1024 + 54);
        prev.setImageBitmap(bitmap);
        prev_staff.setImageBitmap(bitmap);
        Log.e(TAG, "NFIQ:" + NFIQ);
        quality.setText(String.valueOf((5 - NFIQ) * 20));
        quality_staff.setText(String.valueOf((5 - NFIQ) * 20));
    }

    @OnClick({R.id.capture_btn, R.id.verify_btn, R.id.capture_btn_staff})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.capture_btn:
                Log.e(TAG, "capture_btn");
                tip.setText("capture...");
                verifyMode = false;
                verify_tv.setText("");
                prev.setImageBitmap(null);
                captureIv.setImageBitmap(null);
                captureBtn.setEnabled(false);
                verifyBtn.setEnabled(false);
//                mDevice.startCapture(this);
                mApi.startCapture(40, this);
                break;
            case R.id.verify_btn:
                verify_tv.setText("");
                captureBtn.setEnabled(false);
                verifyBtn.setEnabled(false);
                verifyMode = true;
                tip.setText("verify...");
                mApi.startCapture(40, this);
                break;
            case R.id.capture_btn_staff:
                verifyMode = false;
                verify_tv.setText("");
                prev_staff.setImageBitmap(null);
                captureIv_staff.setImageBitmap(null);
//                captureBtn.setEnabled(false);
//                verifyBtn.setEnabled(false);
//                mDevice.startCapture(this);
                mApi.startCapture(40, this);
                break;

        }
    }

    private void register(byte[] bmpdata) {
        bmpToXyt(srcfile, bmpdata);
    }

    private void startVerify(byte[] bmpdata) {
        bmpToXyt(dstfile, bmpdata);
    }

    private void bmpToXyt(String filepath, byte[] bmpData) {
        Disposable disposable = Observable.create((ObservableOnSubscribe<Message>) emitter -> {
                    if (bmpData != null) {
                        File file = new File(AppConfig.DATA_PATH);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        FileUtils.writeByteArrayToFile(new File(filepath), bmpData);
                        String pgmname = filepath.replace(".bmp", ".pgm");
                        String xytname = filepath.replace(".bmp", ".xyt");
                        CallDecoder.Bmp2PgmNoTripDistort(filepath, pgmname);
                        CallFprint.pgmChangeToXyt(pgmname, xytname);
                        Message msg = Message.obtain();
                        msg.what = 0;
                        if (verifyMode) {
                            msg.what = 1;
                            long start = System.currentTimeMillis();
                            int nRet = CallFprint.fprintCompare(dstfile.replace(".bmp", ".xyt"), srcfile.replace(".bmp", ".xyt"));
                            Log.i(TAG, "fprintCompare nRet= " + nRet);
                            msg.arg1 = nRet;
                            long stop = System.currentTimeMillis();
                            Log.i(TAG, "use time:" + (stop - start) + "ms");
                        }
                        emitter.onNext(msg);
                        emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msg -> {
                    if (msg.what == 1) {
                        Log.e(TAG, "比对完成");
                        captureBtn.setEnabled(true);
                        verifyBtn.setEnabled(true);
                        int score = msg.arg1;
                        tip.setText("score:" + score);
                        if (score > threadHold) {
                            verify_tv.setText(getResources().getString(R.string.verify_success));
                            verify_tv.setTextColor(getResources().getColor(R.color.green));
                        } else {
                            verify_tv.setText(getResources().getString(R.string.verify_faild));
                            verify_tv.setTextColor(getResources().getColor(R.color.red));
                        }
                    } else {
                        Log.e(TAG, "register complete!");
                        verifyBtn.setEnabled(true);
                        tip.setText("register complete!");
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //指纹下电
        IoControl.getInstance().setIoStatus(IoControl.USBFP_PATH, IoControl.IOSTATUS.DISABLE);
        openDevice(false);
    }







//    STUDENT ENROLLMENT
    private void getAllRecords() {
        back_txt = findViewById(R.id.back_txt);
        back_txt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            default_sync.setVisibility(View.VISIBLE);
            allrecords.setVisibility(View.GONE);
        }
    });
        myList = findViewById(R.id.listview);

        //create DB instance
        myDB = new MyDatabaseHelper(SFingerActivity.this);
        if (myDB.getRecordCount()==0){
        Toast.makeText(SFingerActivity.this, "No records to sync", Toast.LENGTH_SHORT).show();
        default_sync.setVisibility(View.VISIBLE);
        allrecords.setVisibility(View.GONE);
    }else {
        //to read all records in DB
        Cursor cursor = myDB.readAllData();
        if (cursor != null && cursor.moveToFirst()) {

            arr_id.clear();
            arr_firstname.clear();
            arr_surname.clear();
            arr_othername.clear();
            arr_dob.clear();
            arr_guardianname.clear();
            arr_guardianphone.clear();
            arr_guardianaddress.clear();
            arr_guardianrelationship.clear();
            arr_question1.clear();
            arr_question2.clear();
            arr_question3.clear();
            arr_question4.clear();
            arr_question5.clear();
            arr_religion.clear();
            arr_address.clear();
            arr_picture.clear();
            arr_fingerprint.clear();
            arr_signature.clear();
            arr_gender.clear();
            arr_class.clear();
            arr_shoe_size.clear();
            arr_height.clear();
            arr_weight.clear();
            arr_origin.clear();
            arr_schoolname.clear();
            arr_lga.clear();
            arr_createdby.clear();
            arr_tesis_number.clear();

            do {
                arr_id.add(cursor.getInt(0));
                arr_firstname.add(cursor.getString(1));
                arr_surname.add(cursor.getString(2));
                arr_othername.add(cursor.getString(3));
                arr_dob.add(cursor.getString(4));
                arr_guardianname.add(cursor.getString(5));
                arr_guardianphone.add(cursor.getString(6));
                arr_guardianaddress.add(cursor.getString(7));
                arr_guardianrelationship.add(cursor.getString(8));
                arr_question1.add(cursor.getString(9));
                arr_question2.add(cursor.getString(10));
                arr_question3.add(cursor.getString(11));
                arr_question4.add(cursor.getString(12));
                arr_question5.add(cursor.getString(13));
                arr_religion.add(cursor.getString(14));
                arr_address.add(cursor.getString(15));
                arr_picture.add(cursor.getBlob(16));
                arr_fingerprint.add(cursor.getBlob(17));
                arr_signature.add(cursor.getBlob(18));
                arr_gender.add(cursor.getString(19));
                arr_class.add(cursor.getString(20));
                arr_shoe_size.add(cursor.getString(21));
                arr_height.add(cursor.getString(22));
                arr_weight.add(cursor.getString(23));
                arr_origin.add(cursor.getString(24));
                arr_schoolname.add(cursor.getString(25));
                arr_lga.add(cursor.getString(26));
                arr_createdby.add(cursor.getString(27));
                arr_tesis_number.add(cursor.getString(28));
            } while (cursor.moveToNext());
        }
    }

        StudentAdapter studentAdapter = new StudentAdapter(SFingerActivity.this, arr_id, arr_firstname, arr_surname, arr_othername,
            arr_dob, arr_guardianname, arr_guardianphone, arr_guardianaddress, arr_guardianrelationship, arr_question1, arr_question2,
            arr_question3, arr_question4, arr_question5, arr_religion, arr_address, arr_picture, arr_fingerprint, arr_signature,
            arr_gender, arr_class, arr_shoe_size, arr_height, arr_weight, arr_origin, arr_schoolname, arr_lga, arr_createdby, arr_tesis_number, count_local, count_sync);
        myList.setAdapter((ListAdapter) studentAdapter);

    }

    private void getAllStaffRecords() {
        back_txt_staff = findViewById(R.id.back_txt_staff);
        back_txt_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                default_sync.setVisibility(View.VISIBLE);
                allrecordsStaff.setVisibility(View.GONE);
            }
        });
        myList_staff = findViewById(R.id.listview_staff);

        //create DB instance
        myDB_Staff = new MyDatabaseHelperStaff(SFingerActivity.this);
        if (myDB_Staff.getRecordCount()==0){
            Toast.makeText(SFingerActivity.this, "No records to sync", Toast.LENGTH_SHORT).show();
            default_sync.setVisibility(View.VISIBLE);
            allrecordsStaff.setVisibility(View.GONE);
        }else {
            //to read all records in DB
            Cursor cursor = myDB_Staff.readAllData();
            if (cursor != null && cursor.moveToFirst()) {
                arr_staff_id.clear();
                arr_staff_firstname.clear();
                arr_staff_surname.clear();
                arr_staff_othername.clear();
                arr_staff_dob.clear();
                arr_staff_email.clear();
                arr_staff_phone.clear();
                arr_staff_nin.clear();
                arr_staff_qualification.clear();
                arr_staff_question1.clear();
                arr_staff_question2.clear();
                arr_staff_question3.clear();
                arr_staff_question4.clear();
                arr_staff_religion.clear();
                arr_staff_address.clear();
                arr_staff_gender.clear();
                arr_staff_nok.clear();
                arr_staff_referee.clear();
                arr_staff_subject.clear();
                arr_staff_experience.clear();
                arr_staff_school.clear();
                arr_staff_schoollga.clear();
                arr_staff_createdby.clear();
                arr_staff_psnnumber.clear();
                arr_staff_picture.clear();
                arr_staff_fingerprint.clear();
                arr_staff_signature.clear();
                arr_staff_idcard.clear();
                arr_staff_employment.clear();
                arr_staff_promotion.clear();
                arr_staff_category.clear();
                arr_bank_name.clear();
                arr_acc_number.clear();
                arr_acc_name.clear();

                do {
                    arr_staff_id.add(cursor.getInt(0));
                    arr_staff_firstname.add(cursor.getString(1));
                    arr_staff_surname.add(cursor.getString(2));
                    arr_staff_othername.add(cursor.getString(3));
                    arr_staff_dob.add(cursor.getString(4));
                    arr_staff_email.add(cursor.getString(5));
                    arr_staff_phone.add(cursor.getString(6));
                    arr_staff_nin.add(cursor.getString(7));
                    arr_staff_qualification.add(cursor.getString(8));
                    arr_staff_question1.add(cursor.getString(9));
                    arr_staff_question2.add(cursor.getString(10));
                    arr_staff_question3.add(cursor.getString(11));
                    arr_staff_question4.add(cursor.getString(12));
                    arr_staff_religion.add(cursor.getString(13));
                    arr_staff_address.add(cursor.getString(14));
                    arr_staff_gender.add(cursor.getString(15));
                    arr_staff_nok.add(cursor.getString(16));
                    arr_staff_referee.add(cursor.getString(17));
                    arr_staff_subject.add(cursor.getString(18));
                    arr_staff_experience.add(cursor.getString(19));
                    arr_staff_school.add(cursor.getString(20));
                    arr_staff_schoollga.add(cursor.getString(21));
                    arr_staff_createdby.add(cursor.getString(22));
                    arr_staff_psnnumber.add(cursor.getString(23));
                    arr_staff_picture.add(cursor.getBlob(24));
                    arr_staff_fingerprint.add(cursor.getBlob(25));
                    arr_staff_signature.add(cursor.getBlob(26));
                    arr_staff_idcard.add(cursor.getBlob(27));
                    arr_staff_employment.add(cursor.getBlob(28));
                    arr_staff_promotion.add(cursor.getBlob(29));
                    arr_staff_category.add(cursor.getString(30));
                    arr_bank_name.add(cursor.getString(31));
                    arr_acc_number.add(cursor.getString(32));
                    arr_acc_name.add(cursor.getString(33));
                } while (cursor.moveToNext());
            }
        }

        StaffAdapter staffAdapter = new StaffAdapter(SFingerActivity.this, arr_staff_id, arr_staff_firstname, arr_staff_surname,
                arr_staff_othername, arr_staff_dob, arr_staff_email, arr_staff_phone, arr_staff_nin, arr_staff_qualification,
                arr_staff_question1, arr_staff_question2, arr_staff_question3, arr_staff_question4, arr_staff_religion, arr_staff_address,
                arr_staff_gender, arr_staff_nok, arr_staff_referee, arr_staff_subject, arr_staff_experience, arr_staff_school, arr_staff_schoollga,
                arr_staff_createdby, arr_staff_psnnumber, arr_staff_picture, arr_staff_fingerprint, arr_staff_signature, arr_staff_idcard, arr_staff_employment,
                arr_staff_promotion, arr_staff_category, arr_bank_name, arr_acc_number, arr_acc_name);
        myList_staff.setAdapter(staffAdapter);

    }

    public void getDashboardData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://taraenroll.com/api/v1/get_stats",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonObject = new JSONObject(response);

                            String total_registration = jsonObject.getString("total_registration");
                            String total_schools_covered = jsonObject.getString("total_schools_covered");
                            String today_registration = jsonObject.getString("today_registration");
                            String prev_day_registration = jsonObject.getString("prev_day_registration");

                            totalReg.setText(total_registration);
                            totalSchools.setText(total_schools_covered);
                            todaysReg.setText(today_registration);
                            previousDay.setText(prev_day_registration);
                            count_sync.setText(total_registration);

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Log.e(TAG, e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        if(volleyError == null){
                            return;
                        }
                        Log.e(TAG, volleyError.toString());
                        System.out.println("Network Error "+volleyError);
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", uid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }

    // Function to reload the activity to its initial state
    private void reloadActivity() {
        getDashboardData();
        Intent intent = getIntent();
        intent.putExtra("from", "refresh");
        finish();  // Finish the current instance of the activity
        startActivity(intent);  // Start a new instance of the activity

        //set defaults to shared preference values
    }

    private void logout(){
        Dialog myDialog = new Dialog(SFingerActivity.this);
        myDialog.setContentView(R.layout.custom_popup_logout);
        Button yes = myDialog.findViewById(R.id.yes);
        Button no = myDialog.findViewById(R.id.no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                Intent i = new Intent(SFingerActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }


    private void readLGAForTab1(){
        try{
            InputStream myInput;
            // initialize asset manager
            AssetManager assetManager = getAssets();
            //  open excel sheet
            myInput = assetManager.open("secondary_school.xls");
            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            // We now need something to iterate through the cells.
            Iterator<Row> rowIter = mySheet.rowIterator();
            int rowno =0;
//            textView.append("\n");
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if(rowno !=0) {
                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno =0;
                    String lga="";
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno==0){
                            lga = myCell.toString();
                            if (taraba_lga.contains(lga)){
                                //do not add
                            }else{
                                taraba_lga.add(lga);
                            }
                        }
                        colno++;
                    }
//                    textView.append( sno + " -- "+ date+ "  -- "+ det+"\n");
//                    primary_Schools.add(name);

                }
                rowno++;
            }


        }catch (Exception e) {
//            Log.e(TAG, "error "+ e.toString());
            System.out.println("error "+ e.toString());
        }
    }
    private void readSchoolPerSelectedTarabaLga(String selected_taraba_lga, String school_type) {

        try{
            InputStream myInput = null;
            // initialize asset manager
            AssetManager assetManager = getAssets();
            //  open excel sheet
            if (school_type.equals("Primary")){
                myInput = assetManager.open("primary_school.xls");
            }else if(school_type.equals("Secondary")){
                myInput = assetManager.open("secondary_school.xls");
            }

            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            // We now need something to iterate through the cells.
            Iterator<Row> rowIter = mySheet.rowIterator();
            int rowno =0;
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if(rowno !=0) {
                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno =0;
                    String lga="", school="";
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno==0){
                            lga = myCell.toString();
                            if (lga.equals(selected_taraba_lga)){
                                //increase the colno by 1
                                colno=colno+1;
                                school = cellIter.next().toString();
                                //pick the cell, convert to string
                                //add string to lga_list
                                populated_school.add(school);
                            }else{
                                //do not add
                            }
                        }
                        colno++;
                    }
//                    textView.append( sno + " -- "+ date+ "  -- "+ det+"\n");
//                    primary_Schools.add(name);


                }
                rowno++;
            }

        }catch (Exception e) {
//            Log.e(TAG, "error "+ e.toString());
            System.out.println("error "+ e.toString());
        }
    }
    private void readPrimarySchoolFromExcel() {
        try{
            InputStream myInput;
            // initialize asset manager
            AssetManager assetManager = getAssets();
            //  open excel sheet
            myInput = assetManager.open("primary_school.xls");
            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            // We now need something to iterate through the cells.
            Iterator<Row> rowIter = mySheet.rowIterator();
            int rowno =0;
//            textView.append("\n");
            while (rowIter.hasNext()) {
//                Log.e(TAG, " row no "+ rowno );
                System.out.println("row no "+ rowno);
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if(rowno !=0) {
                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno =0;
                    String sno="", lga="", name="";
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno==0){
                            sno = myCell.toString();
                        }else if (colno==1){
                            lga = myCell.toString();
                        }else if (colno==2){
                            name = myCell.toString();
                        }
                        colno++;
                        System.out.println(" Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());
                    }
//                    textView.append( sno + " -- "+ date+ "  -- "+ det+"\n");
                    primary_Schools.add(name);

                }
                rowno++;
            }


        }catch (Exception e) {
//            Log.e(TAG, "error "+ e.toString());
            System.out.println("error "+ e.toString());
        }
    }
    private void readSecondarySchoolFromExcel() {
        try{
            InputStream myInput;
            // initialize asset manager
            AssetManager assetManager = getAssets();
            //  open excel sheet
            myInput = assetManager.open("secondary_school.xls");
            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            // We now need something to iterate through the cells.
            Iterator<Row> rowIter = mySheet.rowIterator();
            int rowno =0;
//            textView.append("\n");
            while (rowIter.hasNext()) {
//                Log.e(TAG, " row no "+ rowno );
                System.out.println("row no "+ rowno);
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if(rowno !=0) {
                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno =0;
                    String lga="", name="";
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno==0){
                            lga = myCell.toString();
                        }else if (colno==1){
                            name = myCell.toString();
                        }
                        colno++;
                        System.out.println(" Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());
                    }
//                    textView.append( sno + " -- "+ date+ "  -- "+ det+"\n");
                    secondary_Schools.add(name);

                }
                rowno++;
            }


        }catch (Exception e) {
//            Log.e(TAG, "error "+ e.toString());
            System.out.println("error "+ e.toString());
        }
    }
    private void readStateLgaFromExcel() {
        try{
            InputStream myInput;
            // initialize asset manager
            AssetManager assetManager = getAssets();
            //  open excel sheet
            myInput = assetManager.open("state_lga.xls");
            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            // We now need something to iterate through the cells.
            Iterator<Row> rowIter = mySheet.rowIterator();
            int rowno =0;
//            textView.append("\n");
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if(rowno !=0) {
                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno =0;
                    String state="", lga="";
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno==0){
                            state = myCell.toString();
                            if (state_list.contains(state)){
                                //do not add
                            }else{
                                state_list.add(state);
                            }
                        }else if (colno==1){
                            lga = myCell.toString();
                        }
                        colno++;
                    }
//                    textView.append( sno + " -- "+ date+ "  -- "+ det+"\n");
//                    primary_Schools.add(name);

                }
                rowno++;
            }


        }catch (Exception e) {
//            Log.e(TAG, "error "+ e.toString());
            System.out.println("error "+ e.toString());
        }
    }
    private void readLgaFromExcel(String seleceted_state) {
        lga_list.add("Local Government Area");
//        lga_list.add("");

        try{
            InputStream myInput;
            // initialize asset manager
            AssetManager assetManager = getAssets();
            //  open excel sheet
            myInput = assetManager.open("state_lga.xls");
            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            // We now need something to iterate through the cells.
            Iterator<Row> rowIter = mySheet.rowIterator();
            int rowno =0;
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if(rowno !=0) {
                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno =0;
                    String state="", lga="";
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno==0){
                            state = myCell.toString();
                            if (state.equals(seleceted_state)){
                                //increase the colno by 1
                                colno=colno+1;
                                lga = cellIter.next().toString();
                                //pick the cell, convert to string
                                //add string to lga_list
                                lga_list.add(lga);
                            }else{
                                //do not add
                            }
                        }
                        colno++;
                    }
//                    textView.append( sno + " -- "+ date+ "  -- "+ det+"\n");
//                    primary_Schools.add(name);


                }
                rowno++;
            }

        }catch (Exception e) {
//            Log.e(TAG, "error "+ e.toString());
            System.out.println("error "+ e.toString());
        }
    }

    public void student_location(){
//        spinner_lga = findViewById(R.id.spinner_lga);
//        spinner_school_type = findViewById(R.id.spinner_schooltype);
//        spinner_school = findViewById(R.id.spinner_school);



//        lgaspinnerAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, taraba_lga);
//        spinner_lga.setAdapter(lgaspinnerAdapter);
//        spinner_lga.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                selected_taraba_lga = spinner_lga.getSelectedItem().toString();
//                readSchoolPerSelectedTarabaLga(selected_taraba_lga, selected_taraba_schooltype);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        String school_type[] = {"Select School Type","Primary School", "Secondary School"};
//        schooltypespinnerAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, school_type);
//        spinner_school_type.setAdapter(schooltypespinnerAdapter);
//        spinner_school_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // your code here
//                if (position==1){
//                    //school list should be primary school
//                    populated_school.clear();
//                    selected_taraba_schooltype = "Primary";
//                    readSchoolPerSelectedTarabaLga(selected_taraba_lga, selected_taraba_schooltype);
//                    checker("Primary");
//                }else if (position==2){
//                    //school list should be secondary school
//                    populated_school.clear();
//                    selected_taraba_schooltype = "Secondary";
//                    readSchoolPerSelectedTarabaLga(selected_taraba_lga, selected_taraba_schooltype);
//                    checker("Secondary");
//                }else if (position==0){
//                    //school list should be secondary school
//                    populated_school.clear();
//                    checker("");
//                }
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });


//        String classs[] = {"Select Class", "Primary 1", "Primary 2", "Primary 3", "Primary 4", "Primary 5", "Primary 6"};
//        classspinnerAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, classs);
//        spinner_class.setAdapter(classspinnerAdapter);

//        if (selected_taraba_schooltype.equals("Primary")){
//            checker("Primary");
//        }
//        if (selected_taraba_schooltype.equals("Secondary")){
//            checker("Secondary");
//        }

        checker(selected_taraba_schooltype);
    }

    private void checker(String passed) {

        spinner_class = findViewById(R.id.spinner_class);

        if (passed.equals("Primary")){
//            schoolspinnerAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, populated_school);
//            spinner_school.setAdapter(schoolspinnerAdapter);

            String classs[] = {"Select Class", "Primary 1", "Primary 2", "Primary 3", "Primary 4", "Primary 5", "Primary 6"};
            classspinnerAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, classs);
            spinner_class.setAdapter(classspinnerAdapter);

        }else if (passed.equals("Secondary")){
//            schoolspinnerAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, populated_school);
//            spinner_school.setAdapter(schoolspinnerAdapter);

            String classs[] = {"Select Class", "JSS 1", "JSS 2", "JSS 3", "SS 1", "SS 2", "SS 3"};
            classspinnerAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, classs);
            spinner_class.setAdapter(classspinnerAdapter);

        }else if (passed.equals("")){
//            String school[] = {"Select School"};
//            schoolspinnerAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, school);
//            spinner_school.setAdapter(schoolspinnerAdapter);

            String classs[] = {"Select Class"};
            classspinnerAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, classs);
            spinner_class.setAdapter(classspinnerAdapter);
        }
    }

    public void checkLocationInputValidity() {
//        if (spinner_lga.getSelectedItem().toString().equals("Select Local Government Area") || spinner_lga.getSelectedItem().toString().equals("")){
//            lga = "";
//        }else{
//            lga = spinner_lga.getSelectedItem().toString();
//        }
//        if (spinner_school_type.getSelectedItem().toString().equals("Select School Type") || spinner_school_type.getSelectedItem().toString().equals("")){
//            schoolType = "";
//        }else{
//            schoolType = spinner_school_type.getSelectedItem().toString();
//        }
//        if (spinner_school.getSelectedItem().toString().equals("Select School") || spinner_school.getSelectedItem().toString().equals("")){
//            school = "";
//        }else{
//            school = spinner_school.getSelectedItem().toString();
//        }
        if (spinner_class.getSelectedItem().toString().equals("Select Class") || spinner_class.getSelectedItem().toString().equals("")){
            schoolClass = "";
        }else{
            schoolClass = spinner_class.getSelectedItem().toString();
        }

        if (!schoolClass.equals("")){
//            Toast.makeText(this, lga+" "+schoolType+" "+schoolClass+" "+school, Toast.LENGTH_SHORT).show();
            container1.setVisibility(View.GONE);
            bar2.setBackgroundResource(R.drawable.quote1);
            container2.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(this, "Wrong Selection", Toast.LENGTH_SHORT).show();
        }


    }

    public void student_bio_data(){

        edt_surname =  findViewById(R.id.edt_surname);
        edt_firstname = findViewById(R.id.edt_firstname);
        edt_othername = findViewById(R.id.edt_othernames);
        spinner_soo = findViewById(R.id.spinner_soo);
        spinner_lga2 = findViewById(R.id.spinner_lga2);
        edt_dob = findViewById(R.id.edt_dob);
        spinner_gender = findViewById(R.id.spinner_gender);
        edt_nin = findViewById(R.id.edt_nin);
        edt_address = findViewById(R.id.edt_address);
        edt_phone = findViewById(R.id.edt_phone);
        edt_email = findViewById(R.id.edt_email);
        spinner_religion = findViewById(R.id.spinner_religion);
        edt_shoe = findViewById(R.id.edt_shoe_size);
        edt_height = findViewById(R.id.edt_height);
        edt_weight = findViewById(R.id.edt_weight);

        til_surname = findViewById(R.id.til_surname);
        til_firstname = findViewById(R.id.til_firstname);
        til_othername = findViewById(R.id.til_othername);
        til_dob = findViewById(R.id.til_dob);
        til_nin = findViewById(R.id.til_nin);
        til_address = findViewById(R.id.til_address);
        til_phone = findViewById(R.id.til_phone);
        til_email = findViewById(R.id.til_email);
        til_shoe = findViewById(R.id.til_shoe);
        til_height = findViewById(R.id.til_height);
        til_weight = findViewById(R.id.til_weight);

        img_surname = findViewById(R.id.img_surname);
        img_firstname = findViewById(R.id.img_firstname);
        img_othername = findViewById(R.id.img_othernames);
        img_soo = findViewById(R.id.img_soo);
        img_lga = findViewById(R.id.img_lga);
        img_dob = findViewById(R.id.img_dob);
        img_gender = findViewById(R.id.img_gender);
        img_nin = findViewById(R.id.img_nin);
        img_address = findViewById(R.id.img_address);
        img_phone = findViewById(R.id.img_phone);
        img_email = findViewById(R.id.img_email);
        img_religion = findViewById(R.id.img_religion);
        img_shoe = findViewById(R.id.img_shoe_size);
        img_height = findViewById(R.id.img_height);
        img_weight = findViewById(R.id.img_weight);

        edt_surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_surname.getText().toString().length()>2){
                    img_surname.setColorFilter(Color.parseColor("#006837"));
                    surname = true;
                }else{
                    img_surname.setColorFilter(Color.parseColor("#ed1b24"));
                    surname = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_surname=edt_surname.getText().toString();
            }
        });
        edt_firstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_firstname.getText().toString().length()>2){
                    img_firstname.setColorFilter(Color.parseColor("#006837"));
                    firstname = true;
                }else{
                    img_firstname.setColorFilter(Color.parseColor("#ed1b24"));
                    firstname = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_firstname=edt_firstname.getText().toString();
            }
        });
        edt_othername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_othername.getText().toString().length()>2){
                    img_othername.setColorFilter(Color.parseColor("#006837"));
                    othername = true;
                }else{
                    img_othername.setColorFilter(Color.parseColor("#ed1b24"));
                    othername = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_othername=edt_othername.getText().toString();
            }
        });
        edt_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(SFingerActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                monthOfYear+=1;
                                // set day of month, month and year value in the edit text
                                String mt;
                                if(monthOfYear<10){
                                    mt = "0"+monthOfYear;
                                }
                                else mt = String.valueOf(monthOfYear);
                                String dy;
                                if(dayOfMonth<10)
                                    dy = "0"+dayOfMonth;
                                else dy = String.valueOf(dayOfMonth);

                                edt_dob.setText(year + "-"
                                        + mt + "-" + dy);
                                dob = true;
                                img_dob.setColorFilter(Color.parseColor("#006837"));
                                str_dob = edt_dob.getText().toString();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        edt_nin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_nin.getText().toString().length()>10){
                    img_nin.setColorFilter(Color.parseColor("#006837"));
                    nin = true;
                }else{
                    img_nin.setColorFilter(Color.parseColor("#ed1b24"));
                    nin = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_nin=edt_nin.getText().toString();
            }
        });
        edt_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_address.getText().toString().length()>10){
                    img_address.setColorFilter(Color.parseColor("#006837"));
                    address = true;
                }else{
                    img_address.setColorFilter(Color.parseColor("#ed1b24"));
                    address = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_address=edt_address.getText().toString();
            }
        });
        edt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_phone.getText().toString().length()>10){
                    img_phone.setColorFilter(Color.parseColor("#006837"));
                    phone = true;
                }else{
                    img_phone.setColorFilter(Color.parseColor("#ed1b24"));
                    phone = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_phone=edt_phone.getText().toString();
            }
        });
        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Pattern pattern;
                Matcher matcher;
                String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                pattern = Pattern.compile(EMAIL_PATTERN);
                CharSequence cs = (CharSequence) editable;
                matcher = pattern.matcher(cs);
                if(!(matcher.matches()==true)){
                    img_email.setColorFilter(Color.parseColor("#ed1b24"));
                    str_email=edt_email.getText().toString();
                }else{
                    img_email.setColorFilter(Color.parseColor("#006837"));
                    str_email=edt_email.getText().toString();
                }
            }
        });
        edt_shoe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_shoe.getText().toString().length()>1){
                    img_shoe.setColorFilter(Color.parseColor("#006837"));
                    shoe = true;
                }else{
                    img_shoe.setColorFilter(Color.parseColor("#ed1b24"));
                    shoe = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_shoe=edt_shoe.getText().toString();
            }
        });
        edt_height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_height.getText().toString().length()>1){
                    img_height.setColorFilter(Color.parseColor("#006837"));
                    height = true;
                }else{
                    img_height.setColorFilter(Color.parseColor("#ed1b24"));
                    height = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_height=edt_height.getText().toString();
            }
        });
        edt_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_weight.getText().toString().length()>1){
                    img_weight.setColorFilter(Color.parseColor("#006837"));
                    weight = true;
                }else{
                    img_weight.setColorFilter(Color.parseColor("#ed1b24"));
                    weight = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_weight=edt_weight.getText().toString();
            }
        });

//        ArrayAdapter<String> genderAdapter;
        String gender_type[] = {"Gender", "Male", "Female"};
        genderAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, gender_type);
        spinner_gender.setAdapter(genderAdapter);
        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position==0){
                    img_gender.setColorFilter(Color.parseColor("#ed1b24"));
                    gender = false;
                }else {
                    img_gender.setColorFilter(Color.parseColor("#006837"));
                    gender = true;
                    str_gender=spinner_gender.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

//        ArrayAdapter<String> religionAdapter;
        String religion_type[] = {"Religion","Christianity", "Muslim"};
        religionAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, religion_type);
        spinner_religion.setAdapter(religionAdapter);
        spinner_religion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position==0){
                    img_religion.setColorFilter(Color.parseColor("#ed1b24"));
                    religion = false;
                }else {
                    img_religion.setColorFilter(Color.parseColor("#006837"));
                    religion = true;
                    str_religion=spinner_religion.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        ArrayAdapter<String> stateAdapter;
        stateAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, state_list);
        spinner_soo.setAdapter(stateAdapter);
        spinner_soo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position==0){
                    img_soo.setColorFilter(Color.parseColor("#ed1b24"));
                    state = false;
                }else {
                    img_soo.setColorFilter(Color.parseColor("#006837"));
                    state = true;
                    selected_state = spinner_soo.getSelectedItem().toString();
                    lga_list.clear();
                    readLgaFromExcel(selected_state);
                    str_soo=selected_state;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        ArrayAdapter<String> lga2Adapter;
        lga2Adapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, lga_list);
        spinner_lga2.setAdapter(lga2Adapter);
        spinner_lga2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position==0){
                    img_lga.setColorFilter(Color.parseColor("#ed1b24"));
                    lga2 = false;
                }else {
                    img_lga.setColorFilter(Color.parseColor("#006837"));
                    lga2 = true;
                    selected_lga = spinner_lga2.getSelectedItem().toString();
                    str_lga2=selected_lga;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    public void parent_data(){
        edt_parentname =  findViewById(R.id.edt_parentname);
        edt_parentphone = findViewById(R.id.edt_parentphone);
        edt_parentrelationship = findViewById(R.id.edt_parentrelationship);
        edt_parentaddress = findViewById(R.id.edt_parentaddress);

        til_parentname = findViewById(R.id.til_parentname);
        til_parentphone = findViewById(R.id.til_parentphone);
        til_parentrelationship = findViewById(R.id.til_parentrelationship);
        til_parentaddress = findViewById(R.id.til_parentaddress);

        img_parentname = findViewById(R.id.img_parentname);
        img_parentphone = findViewById(R.id.img_parentphone);
        img_parentrelationship = findViewById(R.id.img_parentrelationship);
        img_parentaddress = findViewById(R.id.img_parentaddress);

        edt_parentname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_parentname.getText().toString().length()>2){
                    img_parentname.setColorFilter(Color.parseColor("#006837"));
                    parentname = true;
                }else{
                    img_parentname.setColorFilter(Color.parseColor("#ed1b24"));
                    parentname = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_parentname = edt_parentname.getText().toString();
            }
        });
        edt_parentphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_parentphone.getText().toString().length()>10){
                    img_parentphone.setColorFilter(Color.parseColor("#006837"));
                    parentphone = true;
                }else{
                    img_parentphone.setColorFilter(Color.parseColor("#ed1b24"));
                    parentphone = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_parentphone = edt_parentphone.getText().toString();
            }
        });
        edt_parentrelationship.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_parentrelationship.getText().toString().length()>4){
                    img_parentrelationship.setColorFilter(Color.parseColor("#006837"));
                    parentrelationship = true;
                }else{
                    img_parentrelationship.setColorFilter(Color.parseColor("#ed1b24"));
                    parentrelationship = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_parentrelationship = edt_parentrelationship.getText().toString();
            }
        });
        edt_parentaddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_parentaddress.getText().toString().length()>10){
                    img_parentaddress.setColorFilter(Color.parseColor("#006837"));
                    parentaddress = true;
                }else{
                    img_parentaddress.setColorFilter(Color.parseColor("#ed1b24"));
                    parentaddress = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_parentaddress = edt_parentaddress.getText().toString();
            }
        });

    }

    //security question
    private void securityQuestion() {
        edt_security1 = findViewById(R.id.edt_security_1);
        edt_security2 = findViewById(R.id.edt_security_2);
        edt_security3 = findViewById(R.id.edt_security_3);
        edt_security4 = findViewById(R.id.edt_security_4);
    }

    private void portraitCapture() {

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(SFingerActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // here, Permission is not granted
                    ActivityCompat.requestPermissions(SFingerActivity.this, new String[] {android.Manifest.permission.CAMERA}, 50);
                }else{
                    // her, permission is granted
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 101);
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            bitmap = (Bitmap) data.getExtras().get("data");
            mPreview.setImageBitmap(bitmap);
            finalImage.setImageBitmap(bitmap);
            finalImage2.setImageBitmap(bitmap);

            //converting bitmap to bytearray
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            b = baos.toByteArray();
        }else if (requestCode == 100){
            bitmap = (Bitmap) data.getExtras().get("data");
            img_employmentLetter.setImageBitmap(bitmap);

            //converting bitmap to bytearray
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            b_employment = baos.toByteArray();
        }else if (requestCode == 200){
            bitmap = (Bitmap) data.getExtras().get("data");
            img_promotionLetter.setImageBitmap(bitmap);

            //converting bitmap to bytearray
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            b_promotion = baos.toByteArray();
        }else if (requestCode == 300){
            bitmap = (Bitmap) data.getExtras().get("data");
            img_idCard.setImageBitmap(bitmap);

            //converting bitmap to bytearray
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            b_idCard = baos.toByteArray();
        }else if (requestCode == 400){
            bitmap = (Bitmap) data.getExtras().get("data");
            surfaceView_staff.setImageBitmap(bitmap);

            //converting bitmap to bytearray
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            b_staffPortrait = baos.toByteArray();
        }
    }

    private void signatureCapture() {
        bitmap_signature = signatureView.getSignatureBitmap();
        finalSignature.setImageBitmap(bitmap_signature);
        finalSignature2.setImageBitmap(bitmap_signature);

        //converting bitmap to bytearray
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap_signature.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        b_signature = baos.toByteArray();
    }

    private void enrollmentVerification() {
        preferences2 = getSharedPreferences("TempStorage", Context.MODE_PRIVATE);
        String pref_lga = preferences2.getString("lga", "");

        String firstFourLga = pref_lga.substring(0, Math.min(pref_lga.length(), 3)).toUpperCase(Locale.ROOT);
//        String firstLetterSchoolType = selected_taraba_schooltype.substring(0, Math.min(selected_taraba_schooltype.length(), 1));
        Random random = new Random();
        // Generate three random numbers
        int randomNumber1 = random.nextInt(10);
        int randomNumber2 = random.nextInt(10);
        int randomNumber3 = random.nextInt(10);
        int randomNumber4 = random.nextInt(10);
        int randomNumber5 = random.nextInt(10);
        int randomNumber6 = random.nextInt(10);
        tesis = firstFourLga+randomNumber1+randomNumber2+randomNumber3+randomNumber4+randomNumber5+randomNumber6;


        finalName.setText(edt_surname.getText().toString()+" "+edt_firstname.getText().toString());
        finalName2.setText(edt_surname.getText().toString()+" "+edt_firstname.getText().toString());
        finalAddress.setText(edt_address.getText().toString());
        finalAddress2.setText(edt_address.getText().toString());
        finalState.setText(spinner_soo.getSelectedItem().toString());
        finalHeight.setText(edt_height.getText().toString());
        finalDOB.setText(edt_dob.getText().toString());
        finalGender.setText(spinner_gender.getSelectedItem().toString());   //change to spinner selection
        finalClass.setText(schoolClass);
        tesisNumber.setText(tesis);

    }

    private void networkCheck() {
        //connectivity manager
        ConnectivityManager cm = (ConnectivityManager) SFingerActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Network Capabilities of Active Network
        NetworkCapabilities nc = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        }
        //check if internet is enabled on phone
        try {
            NetworkInfo networkInfo = ((ConnectivityManager) SFingerActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            networkInfo.isConnected();

            // DownSpeed in MBPS
            int downSpeed = nc.getLinkDownstreamBandwidthKbps() / 1000;
            // UpSpeed  in MBPS
            int upSpeed = nc.getLinkUpstreamBandwidthKbps() / 1000;

            downloadspeed.setText(String.valueOf(downSpeed));
            uploadspeed.setText(String.valueOf(upSpeed));

            Dialog myDialog = new Dialog(SFingerActivity.this);
            myDialog.setContentView(R.layout.custom_popup_networktest);
            TextView text = myDialog.findViewById(R.id.text);
            ImageView img = myDialog.findViewById(R.id.img);
            if (downSpeed>=5 && upSpeed>=1){
                text.setText("Good network connection!\nYou can synchronise your data");
                img.setImageResource(R.drawable.network_check);
            }else if (downSpeed<5 && upSpeed<1){
                text.setText("Poor network connection!\nDo not synchronise data");
                img.setImageResource(R.drawable.xmark);
            }
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.setCanceledOnTouchOutside(true);
            myDialog.show();
        } catch (Exception e) {
            Dialog myDialog = new Dialog(SFingerActivity.this);
            myDialog.setContentView(R.layout.custom_popup_networktest);
            TextView text = myDialog.findViewById(R.id.text);
            ImageView img = myDialog.findViewById(R.id.img);
            text.setText("Please enable internet");
            img.setImageResource(R.drawable.xmark);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.setCanceledOnTouchOutside(true);
            myDialog.show();
        }

    }

    private void storeInSqliteDB() {
        preferences2 = getSharedPreferences("TempStorage", Context.MODE_PRIVATE);
        String pref_lga = preferences2.getString("lga", "");
        String pref_schooltype = preferences2.getString("schooltype", "");
        String pref_school = preferences2.getString("school", "");

        //create DB instance
        myDB = new MyDatabaseHelper(SFingerActivity.this);
        //add data to the table
        myDB.addToTable(str_firstname, str_surname, str_othername, str_dob, str_parentname, str_parentphone, str_parentaddress,
                str_parentrelationship, str_security1, str_security2, str_security3, str_security4, "", str_religion,
                str_address, b, b_finger, b_signature, str_gender, schoolClass, str_shoe, str_height, str_weight,
                str_soo, pref_school, pref_lga, uid, tesis);

        count_local.setText(String.valueOf(myDB.getRecordCount()));
    }




//    Staff Enrollment functions

    private void staffPortal(String principal) {
        next2.setVisibility(View.VISIBLE);
        staff_toploader.setVisibility(View.VISIBLE);
        staffDashboard.setVisibility(View.GONE);
        staffContainer1.setVisibility(View.VISIBLE);

//        spinner_staff_lga.setAdapter(lgaspinnerAdapter);
//        spinner_staff_lga.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                selected_staff_lga = spinner_staff_lga.getSelectedItem().toString();
//                readSchoolPerSelectedTarabaLga(selected_staff_lga, selected_staff_schooltype);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        spinner_staff_schooltype.setAdapter(schooltypespinnerAdapter);
//        spinner_staff_schooltype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // your code here
//                if (position==1){
//                    //school list should be primary school
//                    populated_school.clear();
//                    selected_staff_schooltype = "Primary";
//                    readSchoolPerSelectedTarabaLga(selected_staff_lga, selected_staff_schooltype);
//                    checker2("Primary");
//                }else if (position==2){
//                    //school list should be secondary school
//                    populated_school.clear();
//                    selected_staff_schooltype = "Secondary";
//                    readSchoolPerSelectedTarabaLga(selected_staff_lga, selected_staff_schooltype);
//                    checker2("Secondary");
//                }else if (position==0){
//                    //school list should be secondary school
//                    populated_school.clear();
//                    checker2("");
//                }
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });
    }

//    private void checker2(String passed) {
//
//        if (passed.equals("Primary")){
//            schoolspinnerAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, populated_school);
//            spinner_staff_school.setAdapter(schoolspinnerAdapter);
//
//        }else if (passed.equals("Secondary")){
//            schoolspinnerAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, populated_school);
//            spinner_staff_school.setAdapter(schoolspinnerAdapter);
//
//        }else if (passed.equals("")){
//            String school[] = {"Select School"};
//            schoolspinnerAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, school);
//            spinner_staff_school.setAdapter(schoolspinnerAdapter);
//        }
//    }

    public void checkStaffLocationInputValidity() {
        staffBioData();
//        if (spinner_staff_lga.getSelectedItem().toString().equals("Select Local Government Area") || spinner_staff_lga.getSelectedItem().toString().equals("")){
//            staff_lga = "";
//        }else{
//            staff_lga = spinner_lga.getSelectedItem().toString();
//        }
//        if (spinner_staff_schooltype.getSelectedItem().toString().equals("Select School Type") || spinner_staff_schooltype.getSelectedItem().toString().equals("")){
//            staff_schoolType = "";
//        }else{
//            staff_schoolType = spinner_staff_schooltype.getSelectedItem().toString();
//        }
//        if (spinner_staff_school.getSelectedItem().toString().equals("Select School") || spinner_staff_school.getSelectedItem().toString().equals("")){
//            staff_school = "";
//        }else{
//            staff_school = spinner_staff_school.getSelectedItem().toString();
//        }

//        if (!staff_lga.equals("") && !staff_schoolType.equals("") && !staff_school.equals("")){
//            staffContainer1.setVisibility(View.GONE);
//            staff_bar2.setBackgroundResource(R.drawable.quote1);
//            staffContainer2.setVisibility(View.VISIBLE);
//
//
//        }else{
//            Toast.makeText(this, "Wrong Selection", Toast.LENGTH_SHORT).show();
//        }


    }

    private void staffBioData() {
        img_staff_surname = findViewById(R.id.img_staff_surname);
        img_staff_firstname = findViewById(R.id.img_staff_firstname);
        img_staff_gender = findViewById(R.id.img_staff_gender);
        img_staff_dob = findViewById(R.id.img_staff_dob);
        img_staff_phone = findViewById(R.id.img_staff_phone);
        img_staff_nin = findViewById(R.id.img_staff_nin);
        img_staff_email = findViewById(R.id.img_staff_email);

        til_staff_surname = findViewById(R.id.til_staff_surname);
        til_staff_firstname = findViewById(R.id.til_staff_firstname);
        til_staff_dob = findViewById(R.id.til_staff_dob);
        til_staff_phone = findViewById(R.id.til_staff_phone);
        til_staff_nin = findViewById(R.id.til_staff_nin);
        til_staff_email = findViewById(R.id.til_staff_email);

        edt_staff_surname = findViewById(R.id.edt_staff_surname);
        edt_staff_firstname = findViewById(R.id.edt_staff_firstname);
        spinner_staff_gender = findViewById(R.id.spinner_staff_gender);
        edt_staff_dob = findViewById(R.id.edt_staff_dob);
        edt_staff_phone = findViewById(R.id.edt_staff_phone);
        edt_staff_nin = findViewById(R.id.edt_staff_nin);
        edt_staff_email = findViewById(R.id.edt_staff_email);

        edt_staff_surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_staff_surname.getText().toString().length()>2){
                    img_staff_surname.setColorFilter(Color.parseColor("#006837"));
                    staff_surname = true;
                }else{
                    img_surname.setColorFilter(Color.parseColor("#ed1b24"));
                    staff_surname = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_staff_surname=edt_staff_surname.getText().toString();
            }
        });
        edt_staff_firstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_staff_firstname.getText().toString().length()>2){
                    img_staff_firstname.setColorFilter(Color.parseColor("#006837"));
                    staff_firstname = true;
                }else{
                    img_staff_firstname.setColorFilter(Color.parseColor("#ed1b24"));
                    staff_firstname = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_staff_firstname=edt_staff_firstname.getText().toString();
            }
        });
        spinner_staff_gender.setAdapter(genderAdapter);
        spinner_staff_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position==0){
                    img_staff_gender.setColorFilter(Color.parseColor("#ed1b24"));
                    staff_gender = false;
                }else {
                    img_staff_gender.setColorFilter(Color.parseColor("#006837"));
                    staff_gender = true;
                    str_staff_gender=spinner_staff_gender.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        edt_staff_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(SFingerActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                monthOfYear+=1;
                                // set day of month, month and year value in the edit text
                                String mt;
                                if(monthOfYear<10){
                                    mt = "0"+monthOfYear;
                                }
                                else mt = String.valueOf(monthOfYear);
                                String dy;
                                if(dayOfMonth<10)
                                    dy = "0"+dayOfMonth;
                                else dy = String.valueOf(dayOfMonth);

                                edt_staff_dob.setText(year + "-"
                                        + mt + "-" + dy);
                                staff_dob = true;
                                img_staff_dob.setColorFilter(Color.parseColor("#006837"));
                                str_staff_dob = edt_staff_dob.getText().toString();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        edt_staff_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_staff_phone.getText().toString().length()>10){
                    img_staff_phone.setColorFilter(Color.parseColor("#006837"));
                    staff_phone = true;
                }else{
                    img_staff_phone.setColorFilter(Color.parseColor("#ed1b24"));
                    staff_phone = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_staff_phone=edt_staff_phone.getText().toString();
            }
        });
        edt_staff_nin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_staff_nin.getText().toString().length()>10){
                    img_staff_nin.setColorFilter(Color.parseColor("#006837"));
                    staff_nin = true;
                }else{
                    img_staff_nin.setColorFilter(Color.parseColor("#ed1b24"));
                    staff_nin = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_staff_nin=edt_staff_nin.getText().toString();
            }
        });
        edt_staff_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Pattern pattern;
                Matcher matcher;
                String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                pattern = Pattern.compile(EMAIL_PATTERN);
                CharSequence cs = (CharSequence) editable;
                matcher = pattern.matcher(cs);
                if(!(matcher.matches()==true)){
                    img_staff_email.setColorFilter(Color.parseColor("#ed1b24"));
                    str_staff_email=edt_staff_email.getText().toString();
                }else{
                    img_staff_email.setColorFilter(Color.parseColor("#006837"));
                    str_staff_email=edt_staff_email.getText().toString();
                }
            }
        });

    }

    private void psnNumber() {
        edt_psnNum = findViewById(R.id.edt_staff_psnnumber);
        til_psnNum = findViewById(R.id.til_staff_psnnumber);
        img_psnNum = findViewById(R.id.img_staff_psnnumber);

        edt_psnNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_psnNum.getText().toString().length()>3){
                    img_psnNum.setColorFilter(Color.parseColor("#006837"));
                    staff_psnNum = true;
                }else{
                    img_psnNum.setColorFilter(Color.parseColor("#ed1b24"));
                    staff_psnNum = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_staff_psnNum=edt_psnNum.getText().toString();
            }
        });

    }

    private void staffAccountInput() {
        bank_list.add("Access Bank Plc");
        bank_list.add("Citibank Nigeria Limited");
        bank_list.add("Ecobank Nigeria Plc");
        bank_list.add("Fidelity Bank Plc");
        bank_list.add("First Bank Nigeria Limited");
        bank_list.add("First City Monument Bank Plc");
        bank_list.add("Globus Bank Limited");
        bank_list.add("Guaranty Trust Bank Plc");
        bank_list.add("Heritage Banking Company Ltd.");
        bank_list.add("Keystone Bank Limited");
        bank_list.add("Parallex Bank Ltd");
        bank_list.add("Polaris Bank Plc");
        bank_list.add("Premium Trust Bank");
        bank_list.add("Providus Bank");
        bank_list.add("Stanbic IBTC Bank Plc");
        bank_list.add("Standard Chartered Bank Nigeria Ltd.");
        bank_list.add("Sterling Bank Plc");
        bank_list.add("SunTrust Bank Nigeria Limited");
        bank_list.add("Titan Trust Bank Ltd");
        bank_list.add("Union Bank of Nigeria Plc");
        bank_list.add("United Bank For Africa Plc");
        bank_list.add("Unity  Bank Plc");
        bank_list.add("Wema Bank Plc");
        bank_list.add("Zenith Bank Plc");


        accNumber = findViewById(R.id.edt_staff_accountnumber);
        accName = findViewById(R.id.edt_staff_accountname);
        spinner_bank_list = findViewById(R.id.spinner_bank_list);

        bankAdapter = new ArrayAdapter<>(SFingerActivity.this, R.layout.simple_spinner_item, R.id.tx, bank_list);
        spinner_bank_list.setAdapter(bankAdapter);
        spinner_bank_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_staff_bankname=spinner_bank_list.getSelectedItem().toString();
                staffBankName = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                staffBankName = false;
            }
        });

        accNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (accNumber.getText().toString().length()>=10){
//                    img_psnNum.setColorFilter(Color.parseColor("#006837"));
                    staffAccountNumber = true;
                }else{
//                    img_psnNum.setColorFilter(Color.parseColor("#ed1b24"));
                    staffAccountNumber = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_staff_accnum=accNumber.getText().toString();
            }
        });
        accName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (accName.getText().toString().length()>=7){
//                    img_psnNum.setColorFilter(Color.parseColor("#006837"));
                    staffAccountName = true;
                }else{
//                    img_psnNum.setColorFilter(Color.parseColor("#ed1b24"));
                    staffAccountName = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_staff_accname=accName.getText().toString();
            }
        });


    }

    private void eduQualification() {
        spinner_postgraduate = findViewById(R.id.spinner_postgraduate);
        img_postgraduate = findViewById(R.id.img_postgraduate);
        spinner_tertiary = findViewById(R.id.spinner_tertiary);
        img_tertiary = findViewById(R.id.img_tertiary);
        spinner_secondary = findViewById(R.id.spinner_secondary_school);
        img_secondary = findViewById(R.id.img_secondary_school);
        edt_staff_experience = findViewById(R.id.edt_staff_teachingexperience);
        edt_subject = findViewById(R.id.edt_staff_subject_taught);
        til_staff_experience = findViewById(R.id.til_staff_teachingexperience);
        til_staff_subject = findViewById(R.id.til_staff_subject_taught);
        img_staff_experience = findViewById(R.id.img_staff_teachingexperience);
        img_staff_subject = findViewById(R.id.img_staff_subject_taught);

        ArrayAdapter<String> postgradAdapter;
        String postgrad[] = {"Select Postgraduate Qualification", "PhD", "MSc", "MTech", "MBA", "MPhil", "MMed"};
        postgradAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, postgrad);
        spinner_postgraduate.setAdapter(postgradAdapter);
        spinner_postgraduate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position==0){
                    img_postgraduate.setColorFilter(Color.parseColor("#ed1b24"));
                    str_postgrad = "";
                }else {
                    img_postgraduate.setColorFilter(Color.parseColor("#006837"));
                    str_postgrad=spinner_postgraduate.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        ArrayAdapter<String> tertiaryAdapter;
        String tert[] = {"Select Tertiary Qualification", "BSc", "BSc(Hons)", "B.Agric", "BBA", "BArch", "B.Tech", "BA", "BEd", "B.NSC", "B.V.Sc", "BEng", "BPhil", "BPharm", "BMLS", "BFA", "BFA", "BMed", "LLB", "MBBS", "National Diploma (ND)", "HND", "NCE"};
        tertiaryAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, tert);
        spinner_tertiary.setAdapter(tertiaryAdapter);
        spinner_tertiary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position==0){
                    img_tertiary.setColorFilter(Color.parseColor("#ed1b24"));
                    str_tertiary = "";
                }else {
                    img_tertiary.setColorFilter(Color.parseColor("#006837"));
                    str_tertiary=spinner_tertiary.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        ArrayAdapter<String> secondaryAdapter;
        String secon[] = {"Select Secondary Qualification", "WASSCE", "NECO"};
        secondaryAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, secon);
        spinner_secondary.setAdapter(secondaryAdapter);
        spinner_secondary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position==0){
                    img_secondary.setColorFilter(Color.parseColor("#ed1b24"));
                    str_secondary = "";
                }else {
                    img_secondary.setColorFilter(Color.parseColor("#006837"));
                    str_secondary=spinner_secondary.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        edt_staff_experience.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_staff_experience.getText().toString().length()>0){
                    img_staff_experience.setColorFilter(Color.parseColor("#006837"));
                }else{
                    img_staff_experience.setColorFilter(Color.parseColor("#ed1b24"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_staff_experience=edt_staff_experience.getText().toString();
            }
        });
        edt_subject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_subject.getText().toString().length()>2){
                    img_staff_subject.setColorFilter(Color.parseColor("#006837"));
                }else{
                    img_staff_subject.setColorFilter(Color.parseColor("#ed1b24"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_staff_subject=edt_subject.getText().toString();
            }
        });
    }

    private void nextOfKin() {
        edt_nok_fullname = findViewById(R.id.edt_staff_nok_fullname);
        edt_nok_phone = findViewById(R.id.edt_staff_nok_phone);
        edt_nok_address = findViewById(R.id.edt_staff_nok_address);
        edt_nok_relationship = findViewById(R.id.edt_staff_nok_relationship);

        img_nok_fullname = findViewById(R.id.img_staff_nok_fullname);
        img_nok_phone = findViewById(R.id.img_staff_nok_phone);
        img_nok_address = findViewById(R.id.img_staff_nok_address);
        img_nok_relationship = findViewById(R.id.img_staff_nok_relationship);

        edt_nok_fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_nok_fullname.getText().toString().length()>5){
                    img_nok_fullname.setColorFilter(Color.parseColor("#006837"));
                    nok_fullname = true;
                }else{
                    img_nok_fullname.setColorFilter(Color.parseColor("#ed1b24"));
                    nok_fullname = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_nok_fullname=edt_nok_fullname.getText().toString();
            }
        });
        edt_nok_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_nok_phone.getText().toString().length()>10){
                    img_nok_phone.setColorFilter(Color.parseColor("#006837"));
                    nok_phone = true;
                }else{
                    img_nok_phone.setColorFilter(Color.parseColor("#ed1b24"));
                    nok_phone = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_nok_phone=edt_nok_phone.getText().toString();
            }
        });
        edt_nok_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_nok_address.getText().toString().length()>5){
                    img_nok_address.setColorFilter(Color.parseColor("#006837"));
                    nok_address = true;
                }else{
                    img_nok_address.setColorFilter(Color.parseColor("#ed1b24"));
                    nok_address = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_nok_address=edt_nok_address.getText().toString();
            }
        });
        edt_nok_relationship.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_nok_relationship.getText().toString().length()>3){
                    img_nok_relationship.setColorFilter(Color.parseColor("#006837"));
                    nok_relationship = true;
                }else{
                    img_nok_relationship.setColorFilter(Color.parseColor("#ed1b24"));
                    nok_relationship = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_nok_relationship=edt_nok_relationship.getText().toString();
            }
        });
    }

    private void references() {
        edt_ref1_fullname = findViewById(R.id.edt_ref1_fullname);
        edt_ref1_organisation = findViewById(R.id.edt_ref1_organisation);
        edt_ref1_phone = findViewById(R.id.edt_ref1_phone);
        edt_ref1_position = findViewById(R.id.edt_ref1_position);
        img_ref1_fullname = findViewById(R.id.img_ref1_fullname);
        img_ref1_organisation = findViewById(R.id.img_ref1_organisation);
        img_ref1_phone = findViewById(R.id.img_ref1_phone);
        img_ref1_position = findViewById(R.id.img_ref1_position);

        edt_ref2_fullname = findViewById(R.id.edt_ref2_fullname);
        edt_ref2_organisation = findViewById(R.id.edt_ref2_organisation);
        edt_ref2_phone = findViewById(R.id.edt_ref2_phone);
        edt_ref2_position = findViewById(R.id.edt_ref2_position);
        img_ref2_fullname = findViewById(R.id.img_ref2_fullname);
        img_ref2_organisation = findViewById(R.id.img_ref2_organisation);
        img_ref2_phone = findViewById(R.id.img_ref2_phone);
        img_ref2_position = findViewById(R.id.img_ref2_position);

        edt_ref1_fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_ref1_fullname.getText().toString().length()>5){
                    img_ref1_fullname.setColorFilter(Color.parseColor("#006837"));
                }else{
                    img_ref1_fullname.setColorFilter(Color.parseColor("#ed1b24"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_ref1_fullname=edt_ref1_fullname.getText().toString();
            }
        });
        edt_ref1_organisation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_ref1_organisation.getText().toString().length()>5){
                    img_ref1_organisation.setColorFilter(Color.parseColor("#006837"));
                }else{
                    img_ref1_organisation.setColorFilter(Color.parseColor("#ed1b24"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_ref1_organisation=edt_ref1_organisation.getText().toString();
            }
        });
        edt_ref1_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_ref1_phone.getText().toString().length()>5){
                    img_ref1_phone.setColorFilter(Color.parseColor("#006837"));
                }else{
                    img_ref1_phone.setColorFilter(Color.parseColor("#ed1b24"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_ref1_phone=edt_ref1_phone.getText().toString();
            }
        });
        edt_ref1_position.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_ref1_position.getText().toString().length()>5){
                    img_ref1_position.setColorFilter(Color.parseColor("#006837"));
                }else{
                    img_ref1_position.setColorFilter(Color.parseColor("#ed1b24"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_ref1_position=edt_ref1_position.getText().toString();
            }
        });

        edt_ref2_fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_ref2_fullname.getText().toString().length()>5){
                    img_ref2_fullname.setColorFilter(Color.parseColor("#006837"));
                }else{
                    img_ref2_fullname.setColorFilter(Color.parseColor("#ed1b24"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_ref2_fullname=edt_ref2_fullname.getText().toString();
            }
        });
        edt_ref2_organisation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_ref2_organisation.getText().toString().length()>5){
                    img_ref2_organisation.setColorFilter(Color.parseColor("#006837"));
                }else{
                    img_ref2_organisation.setColorFilter(Color.parseColor("#ed1b24"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_ref2_organisation=edt_ref2_organisation.getText().toString();
            }
        });
        edt_ref2_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_ref2_phone.getText().toString().length()>5){
                    img_ref2_phone.setColorFilter(Color.parseColor("#006837"));
                }else{
                    img_ref2_phone.setColorFilter(Color.parseColor("#ed1b24"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_ref2_phone=edt_ref2_phone.getText().toString();
            }
        });
        edt_ref2_position.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_ref2_position.getText().toString().length()>5){
                    img_ref2_position.setColorFilter(Color.parseColor("#006837"));
                }else{
                    img_ref2_position.setColorFilter(Color.parseColor("#ed1b24"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_ref2_position=edt_ref2_position.getText().toString();
            }
        });

    }

    private void questionBank() {
        preferences2 = getSharedPreferences("TempStorage", Context.MODE_PRIVATE);
        String pref_lga = preferences2.getString("lga", "");
        String pref_schooltype = preferences2.getString("schooltype", "");
        String pref_school = preferences2.getString("school", "");

        counter = findViewById(R.id.counter);
        nextQuestion = findViewById(R.id.nextQuestion);
        question = findViewById(R.id.question);
        questionnumber = findViewById(R.id.questionnumber);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        txt_option1 = findViewById(R.id.txt_option1);
        txt_option2 = findViewById(R.id.txt_option2);
        txt_option3 = findViewById(R.id.txt_option3);
        txt_option4 = findViewById(R.id.txt_option4);
        radio_option1 = findViewById(R.id.radio_option1);
        radio_option2 = findViewById(R.id.radio_option2);
        radio_option3 = findViewById(R.id.radio_option3);
        radio_option4 = findViewById(R.id.radio_option4);


        //get primary questions in an array
        ArrayList<String> arr_question_primary = new ArrayList<>();
        ArrayList<String> arr_option1_primary = new ArrayList<>();
        ArrayList<String> arr_option2_primary = new ArrayList<>();
        ArrayList<String> arr_option3_primary = new ArrayList<>();
        ArrayList<String> arr_option4_primary = new ArrayList<>();

        arr_question_primary.add("What is the value of 5 x 8?");
        arr_question_primary.add("Which of the following is a prime number?");
        arr_question_primary.add("What is the perimeter of a rectangle with length 6 cm and width 4 cm?");
        arr_question_primary.add("What is the result of 15 - 7?");
        arr_question_primary.add("What is the next number in the sequence: 2, 4, 6, 8, ...?");
        arr_question_primary.add("Which of the following words is a synonym for \"happy\"?");
        arr_question_primary.add("Identify the correct spelling:");
        arr_question_primary.add("What is the plural form of \"child\"?");
        arr_question_primary.add("Which of the following sentences is grammatically correct?");
        arr_question_primary.add("What is a group of wolves called?");
        arr_question_primary.add("What is the chemical symbol for water?");
        arr_question_primary.add("Which planet is known as the \"Red Planet\"?");
        arr_question_primary.add("What is the powerhouse of the cell?");
        arr_question_primary.add("Which organ is responsible for filtering waste from the blood?");
        arr_question_primary.add("What is the process by which plants make their food?");

        arr_option1_primary.add("40");
        arr_option1_primary.add("1");
        arr_option1_primary.add("10cm");
        arr_option1_primary.add("9");
        arr_option1_primary.add("10");
        arr_option1_primary.add("Sad");
        arr_option1_primary.add("Recieve");
        arr_option1_primary.add("Childs");
        arr_option1_primary.add("He goes to the store yesterday.");
        arr_option1_primary.add("Flock");
        arr_option1_primary.add("H2O");
        arr_option1_primary.add("Venus");
        arr_option1_primary.add("Nucleus");
        arr_option1_primary.add("Liver");
        arr_option1_primary.add("Photosynthesis");

        arr_option2_primary.add("13");
        arr_option2_primary.add("4");
        arr_option2_primary.add("20cm");
        arr_option2_primary.add("8");
        arr_option2_primary.add("12");
        arr_option2_primary.add("Joyful");
        arr_option2_primary.add("Recieve");
        arr_option2_primary.add("Childrens");
        arr_option2_primary.add("She have been studying for hours.");
        arr_option2_primary.add("Herd");
        arr_option2_primary.add("CO2");
        arr_option2_primary.add("Mars");
        arr_option2_primary.add("Ribosome");
        arr_option2_primary.add("Kidneys");
        arr_option2_primary.add("Respiration");

        arr_option3_primary.add("20");
        arr_option3_primary.add("7");
        arr_option3_primary.add("24cm");
        arr_option3_primary.add("7");
        arr_option3_primary.add("14");
        arr_option3_primary.add("Angry");
        arr_option3_primary.add("Receive");
        arr_option3_primary.add("Childs'");
        arr_option3_primary.add("They are swimming in the pool.");
        arr_option3_primary.add("Pack");
        arr_option3_primary.add("NaCl");
        arr_option3_primary.add("Jupiter");
        arr_option3_primary.add("Mitochondria");
        arr_option3_primary.add("Lungs");
        arr_option3_primary.add("Fermentation");

        arr_option4_primary.add("56");
        arr_option4_primary.add("10");
        arr_option4_primary.add("14cm");
        arr_option4_primary.add("6");
        arr_option4_primary.add("16");
        arr_option4_primary.add("Upset");
        arr_option4_primary.add("Receeve");
        arr_option4_primary.add("Children");
        arr_option4_primary.add("We has lunch at noon.");
        arr_option4_primary.add("Pod");
        arr_option4_primary.add("O2");
        arr_option4_primary.add("Saturn");
        arr_option4_primary.add("Golgi apparatus");
        arr_option4_primary.add("Heart");
        arr_option4_primary.add("Digestion");

        //get secondary questions in an array
        ArrayList<String> arr_question_secondary = new ArrayList<>();
        ArrayList<String> arr_option1_secondary = new ArrayList<>();
        ArrayList<String> arr_option2_secondary = new ArrayList<>();
        ArrayList<String> arr_option3_secondary = new ArrayList<>();
        ArrayList<String> arr_option4_secondary = new ArrayList<>();


        arr_question_secondary.add("What is the sum of the interior angles of a triangle?");
        arr_question_secondary.add("Identify the acute angle in the triangle:");
        arr_question_secondary.add("What is the correct past tense form of \"run\"?");
        arr_question_secondary.add("Identify the correct usage of the word \"effect\".");
        arr_question_secondary.add("Which of the following is a compound sentence?");
        arr_question_secondary.add("In the sentence \"She sang beautifully,\" what part of speech is \"beautifully\"?");
        arr_question_secondary.add("What is the present perfect tense of the verb \"eat\"?");
        arr_question_secondary.add("What is the sum of the angles in a quadrilateral?");
        arr_question_secondary.add("What is the square root of 144?");
        arr_question_secondary.add("Identify the obtuse angle in the triangle:");
        arr_question_secondary.add("What is the comparative form of the adjective \"good\"?");
        arr_question_secondary.add("What is the plural form of \"radius\"?");
        arr_question_secondary.add("Identify the correct usage of the word \"its\".");
        arr_question_secondary.add("What type of sentence is this: \"Take out the trash.\"");
        arr_question_secondary.add("What is the past perfect tense of \"see\"?");
        arr_question_secondary.add("What is the area of a rectangle with length 8 cm and width 5 cm?");
        arr_question_secondary.add("What is the value of π to two decimal places?");
        arr_question_secondary.add("Which of the following is an example of an igneous rock?");
        arr_question_secondary.add("Who developed the theory of relativity?");
        arr_question_secondary.add("What is the chemical symbol for gold?");
        arr_question_secondary.add("What is the pH level of pure water?");
        arr_question_secondary.add("What is the process by which plants lose water through their leaves called?");
        arr_question_secondary.add("What is the function of the nucleus in a cell?");
        arr_question_secondary.add("Which gas is most abundant in Earth's atmosphere?");
        arr_question_secondary.add("What is the SI unit of force?");
        arr_question_secondary.add("What is the chemical formula for table salt?");
        arr_question_secondary.add("What is the name of the closest star to Earth?");
        arr_question_secondary.add("What is the unit of electrical resistance?");
        arr_question_secondary.add("What is the process of converting light energy into chemical energy in plants called?");

        arr_option1_secondary.add("A) 90 degrees");
        arr_option1_secondary.add("A) 45 degrees");
        arr_option1_secondary.add("A) Runned");
        arr_option1_secondary.add("A) The new law may effect positive change.");
        arr_option1_secondary.add("A) She studied for her test.");
        arr_option1_secondary.add("A) Adjective");
        arr_option1_secondary.add("A) Eat");
        arr_option1_secondary.add("A) 90 degrees");
        arr_option1_secondary.add("A) 12");
        arr_option1_secondary.add("A) 45 degrees");
        arr_option1_secondary.add("A) Gooder");
        arr_option1_secondary.add("A) Gooder");
        arr_option1_secondary.add("A) Its a beautiful day.");
        arr_option1_secondary.add("A) Declarative");
        arr_option1_secondary.add("A) See");
        arr_option1_secondary.add("A) 13 cm²");
        arr_option1_secondary.add("A) 3.14");
        arr_option1_secondary.add("A) Marble");
        arr_option1_secondary.add("A) Isaac Newton");
        arr_option1_secondary.add("A) Au");
        arr_option1_secondary.add("A) 7");
        arr_option1_secondary.add("A) Osmosis");
        arr_option1_secondary.add("A) Stores genetic material");
        arr_option1_secondary.add("A) Oxygen");
        arr_option1_secondary.add("A) Newton");
        arr_option1_secondary.add("A) NaCl");
        arr_option1_secondary.add("A) Proxima Centauri");
        arr_option1_secondary.add("A) Ampere");
        arr_option1_secondary.add("A) Respiration");

        arr_option2_secondary.add("B) 180 degrees");
        arr_option2_secondary.add("B) 90 degrees");
        arr_option2_secondary.add("B) Ran");
        arr_option2_secondary.add("B) The medicine had an adverse affect.");
        arr_option2_secondary.add("B) He went to the store, and he bought groceries.");
        arr_option2_secondary.add("B) Adverb");
        arr_option2_secondary.add("B) Eating");
        arr_option2_secondary.add("B) 180 degrees");
        arr_option2_secondary.add("B) 16");
        arr_option2_secondary.add("B) 90 degrees");
        arr_option2_secondary.add("B) Goodest");
        arr_option2_secondary.add("B) Radiuses");
        arr_option2_secondary.add("B) It's raining outside.");
        arr_option2_secondary.add("B) Interrogative");
        arr_option2_secondary.add("B) Saw");
        arr_option2_secondary.add("B) 30 cm²");
        arr_option2_secondary.add("B) 3.16");
        arr_option2_secondary.add("B) Sandstone");
        arr_option2_secondary.add("B) Albert Einstein");
        arr_option2_secondary.add("B) Ag");
        arr_option2_secondary.add("B) 5");
        arr_option2_secondary.add("B) Transpiration");
        arr_option2_secondary.add("B) Produces energy");
        arr_option2_secondary.add("B) Nitrogen");
        arr_option2_secondary.add("B) Watt");
        arr_option2_secondary.add("B) H2O");
        arr_option2_secondary.add("B) Alpha Centauri");
        arr_option2_secondary.add("B) Ohm");
        arr_option2_secondary.add("B) Fermentation");

        arr_option3_secondary.add("C) 270 degrees");
        arr_option3_secondary.add("C) 180 degrees");
        arr_option3_secondary.add("C) Runneded");
        arr_option3_secondary.add("C) The weather has no affect on me.");
        arr_option3_secondary.add("C) The cat sat on the mat.");
        arr_option3_secondary.add("C) Noun");
        arr_option3_secondary.add("C) Eaten");
        arr_option3_secondary.add("C) 270 degrees");
        arr_option3_secondary.add("C) 10");
        arr_option3_secondary.add("C) 150 degrees");
        arr_option3_secondary.add("C) Better");
        arr_option3_secondary.add("C) Radiuses");
        arr_option3_secondary.add("C) The dog chased it's tail.");
        arr_option3_secondary.add("C) Imperative");
        arr_option3_secondary.add("C) Seen");
        arr_option3_secondary.add("C) 40 cm²");
        arr_option3_secondary.add("C) 3.18");
        arr_option3_secondary.add("C) Granite");
        arr_option3_secondary.add("C) Stephen Hawking");
        arr_option3_secondary.add("C) Cu");
        arr_option3_secondary.add("C) 8");
        arr_option3_secondary.add("C) Photosynthesis");
        arr_option3_secondary.add("C) Digests food");
        arr_option3_secondary.add("C) Carbon dioxide");
        arr_option3_secondary.add("C) Joule");
        arr_option3_secondary.add("C) CO2");
        arr_option3_secondary.add("C) Betelgeuse");
        arr_option3_secondary.add("C) Volt");
        arr_option3_secondary.add("C) Photosynthesis");

        arr_option4_secondary.add("D) 360 degrees");
        arr_option4_secondary.add("D) 120 degrees");
        arr_option4_secondary.add("D) Runneding");
        arr_option4_secondary.add("D) The effect of the experiment was evident.");
        arr_option4_secondary.add("D) They played in the park.");
        arr_option4_secondary.add("D) Verb");
        arr_option4_secondary.add("D) Has eaten");
        arr_option4_secondary.add("D) 360 degrees");
        arr_option4_secondary.add("D) 14");
        arr_option4_secondary.add("D) 120 degrees");
        arr_option4_secondary.add("D) Best");
        arr_option4_secondary.add("D) Radiusses");
        arr_option4_secondary.add("D) The cat licked its paws.");
        arr_option4_secondary.add("D) Exclamatory");
        arr_option4_secondary.add("D) Had seen");
        arr_option4_secondary.add("D) 48 cm²");
        arr_option4_secondary.add("D) 3.12");
        arr_option4_secondary.add("D) Slate");
        arr_option4_secondary.add("D) Galileo Galilei");
        arr_option4_secondary.add("D) Fe");
        arr_option4_secondary.add("D) 9");
        arr_option4_secondary.add("D) Respiration");
        arr_option4_secondary.add("D) Controls cell movement");
        arr_option4_secondary.add("D) Hydrogen");
        arr_option4_secondary.add("D) Volt");
        arr_option4_secondary.add("D) HCl");
        arr_option4_secondary.add("D) Sirius");
        arr_option4_secondary.add("D) Watt");
        arr_option4_secondary.add("D) Digestion");

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_option1.setChecked(true);
                radio_option2.setChecked(false);
                radio_option3.setChecked(false);
                radio_option4.setChecked(false);
                answer = txt_option1.getText().toString();
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_option1.setChecked(false);
                radio_option2.setChecked(true);
                radio_option3.setChecked(false);
                radio_option4.setChecked(false);
                answer = txt_option2.getText().toString();
            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_option1.setChecked(false);
                radio_option2.setChecked(false);
                radio_option3.setChecked(true);
                radio_option4.setChecked(false);
                answer = txt_option3.getText().toString();
            }
        });
        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_option1.setChecked(false);
                radio_option2.setChecked(false);
                radio_option3.setChecked(false);
                radio_option4.setChecked(true);
                answer = txt_option4.getText().toString();
            }
        });

        Random myRand = new Random();

        if (pref_schooltype.equals("Primary")){
            randomNumber2 = myRand.nextInt(arr_option1_primary.size());
            question.setText(arr_question_primary.get(randomNumber2));
            txt_option1.setText(arr_option1_primary.get(randomNumber2));
            txt_option2.setText(arr_option2_primary.get(randomNumber2));
            txt_option3.setText(arr_option3_primary.get(randomNumber2));
            txt_option4.setText(arr_option4_primary.get(randomNumber2));
        }else if (pref_schooltype.equals("Secondary")){
            randomNumber2 = myRand.nextInt(arr_option1_secondary.size());
            question.setText(arr_question_secondary.get(randomNumber2));
            txt_option1.setText(arr_option1_secondary.get(randomNumber2));
            txt_option2.setText(arr_option2_secondary.get(randomNumber2));
            txt_option3.setText(arr_option3_secondary.get(randomNumber2));
            txt_option4.setText(arr_option4_secondary.get(randomNumber2));
        }


//        if (selected_staff_schooltype.equals("Primary")){
//            randomNumber2 = myRand.nextInt(arr_option1_primary.size());
//            question.setText(arr_question_primary.get(randomNumber2));
//            txt_option1.setText(arr_option1_primary.get(randomNumber2));
//            txt_option2.setText(arr_option2_primary.get(randomNumber2));
//            txt_option3.setText(arr_option3_primary.get(randomNumber2));
//            txt_option4.setText(arr_option4_primary.get(randomNumber2));
//        }else if (selected_staff_schooltype.equals("Secondary")){
//            randomNumber2 = myRand.nextInt(arr_option1_secondary.size());
//            question.setText(arr_question_secondary.get(randomNumber2));
//            txt_option1.setText(arr_option1_secondary.get(randomNumber2));
//            txt_option2.setText(arr_option2_secondary.get(randomNumber2));
//            txt_option3.setText(arr_option3_secondary.get(randomNumber2));
//            txt_option4.setText(arr_option4_secondary.get(randomNumber2));
//        }
        questionnumber.setText("1");
        arr_questions.add(question.getText().toString());
        arr_responses.add(answer);


        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index==4){

                    //go to next page
                    staffContainer7.setVisibility(View.GONE);
                    staff_bar8.setBackgroundResource(R.drawable.quote1);
                    staffContainer8.setVisibility(View.VISIBLE);

                    arr_responses.add(answer);
                    System.out.println("Questions = "+arr_questions);
                    System.out.println("Responses = "+arr_responses);

                    qualifications();
                    next2.setVisibility(View.VISIBLE);
                }else{
                    if (answer.equals("")){
                        Toast.makeText(SFingerActivity.this, "Please select one answer", Toast.LENGTH_SHORT).show();
                    }else{
                        arr_responses.add(answer);
                        radio_option1.setChecked(false);
                        radio_option2.setChecked(false);
                        radio_option3.setChecked(false);
                        radio_option4.setChecked(false);

                        if (pref_schooltype.equals("Primary")){
                            randomNumber1 = myRand.nextInt(arr_option1_primary.size());
                            question.setText(arr_question_primary.get(randomNumber1));
                            txt_option1.setText(arr_option1_primary.get(randomNumber1));
                            txt_option2.setText(arr_option2_primary.get(randomNumber1));
                            txt_option3.setText(arr_option3_primary.get(randomNumber1));
                            txt_option4.setText(arr_option4_primary.get(randomNumber1));
                        }else if (pref_schooltype.equals("Secondary")){
                            randomNumber1 = myRand.nextInt(arr_option1_secondary.size());
                            question.setText(arr_question_secondary.get(randomNumber1));
                            txt_option1.setText(arr_option1_secondary.get(randomNumber1));
                            txt_option2.setText(arr_option2_secondary.get(randomNumber1));
                            txt_option3.setText(arr_option3_secondary.get(randomNumber1));
                            txt_option4.setText(arr_option4_secondary.get(randomNumber1));
                        }

                        arr_questions.add(question.getText().toString());
                        questionnumber.setText(String.valueOf(index+1));
                        counter.setText(String.format("Question %d of 4", index + 1));

                        index++;
                        answer = "";
                    }
                }

            }
        });

    }

    private void qualifications() {
        employmentLetter = findViewById(R.id.employmentLetter);
        promotionLetter = findViewById(R.id.promotionLetter);
        idCard = findViewById(R.id.idCard);
        img_employmentLetter = findViewById(R.id.img_employment);
        img_promotionLetter = findViewById(R.id.img_promotion);
        img_idCard = findViewById(R.id.img_idcard);

        employmentLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(SFingerActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // here, Permission is not granted
                    ActivityCompat.requestPermissions(SFingerActivity.this, new String[] {android.Manifest.permission.CAMERA}, 50);
                }else{
                    // here, permission is granted
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 100);
                }
            }
        });
        promotionLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(SFingerActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // here, Permission is not granted
                    ActivityCompat.requestPermissions(SFingerActivity.this, new String[] {android.Manifest.permission.CAMERA}, 50);
                }else{
                    // here, permission is granted
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 200);
                }
            }
        });
        idCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(SFingerActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // here, Permission is not granted
                    ActivityCompat.requestPermissions(SFingerActivity.this, new String[] {android.Manifest.permission.CAMERA}, 50);
                }else{
                    // here, permission is granted
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 300);
                }
            }
        });
    }

    private void staffPortrait() {
        openCamera_staff = findViewById(R.id.openCamera_staff);
        surfaceView_staff = findViewById(R.id.surfaceView_staff);

        openCamera_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(SFingerActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // here, Permission is not granted
                    ActivityCompat.requestPermissions(SFingerActivity.this, new String[] {android.Manifest.permission.CAMERA}, 50);
                }else{
                    // here, permission is granted
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 400);
                }
            }
        });
    }

    private void staffFingerprint() {
        //指纹上电
        IoControl.getInstance().setIoStatus(IoControl.USBFP_PATH, IoControl.IOSTATUS.ENABLE);
        mApi = new SFingerManager(SFingerActivity.this);
        openDevice(true);
    }

    private void endStaffView() {
        endStaff = findViewById(R.id.endStaff);
        endStaff.setVisibility(View.VISIBLE);
        checkButtonClicked();
    }

    private void checkButtonClicked() {
        printslip_staff = findViewById(R.id.printslip_staff);
        btn_addstaff = findViewById(R.id.btn_addstaff);
        btn_end_staff = findViewById(R.id.btn_end_staff);

        printslip_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send to printer page
                Intent i = new Intent(SFingerActivity.this, PrintActivity.class);
                i.putExtra("Firstname", str_staff_firstname);
                i.putExtra("Surname", str_staff_surname);
                i.putExtra("Tesisnumber", str_staff_psnNum);
                i.putExtra("School", staff_school);
                i.putExtra("userid", uid);
                startActivity(i);
            }
        });
        btn_addstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadActivity();
            }
        });
        btn_end_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadActivity();
            }
        });
    }

    private void saveStaffToSQLite() {
        preferences2 = getSharedPreferences("TempStorage", Context.MODE_PRIVATE);
        String pref_lga = preferences2.getString("lga", "");
        String pref_schooltype = preferences2.getString("schooltype", "");
        String pref_school = preferences2.getString("school", "");

        myDB_Staff = new MyDatabaseHelperStaff(SFingerActivity.this);
        myDB_Staff.addToTable(str_staff_firstname, str_staff_surname, str_staff_surname, str_staff_dob, str_staff_email, str_staff_phone, str_staff_nin, edu_quality,
                arr_questions.get(0)+": "+arr_responses.get(1),
                arr_questions.get(1)+": "+arr_responses.get(2),
                arr_questions.get(2)+": "+arr_responses.get(3),
                arr_questions.get(3)+": "+arr_responses.get(4),
                str_staff_gender, str_staff_phone, str_staff_gender, str_nok_fullname,
                str_ref1_fullname+", "+str_ref2_fullname,
                str_staff_subject, str_staff_experience, pref_school, pref_lga, uid, str_staff_psnNum,
                b_staffPortrait, b_finger, b_staffPortrait, b_idCard, b_employment, b_promotion, selected_staff_type,
                str_staff_bankname, str_staff_accnum, str_staff_accname);
    }


    @Override
    public void onBackPressed() {
        //do nothing
    }
}