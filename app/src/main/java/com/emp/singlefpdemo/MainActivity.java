package com.emp.singlefpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    TextInputLayout til_uid, til_pass;
    EditText edt_uid, edt_pass;
    String uid, pass;
    Button login;
    ArrayList<String> array_users = new ArrayList<>();
    Dialog loading_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        til_uid = findViewById(R.id.til_uid);
        til_pass = findViewById(R.id.til_pass);
        edt_uid = findViewById(R.id.edt_uid);
        edt_pass = findViewById(R.id.edt_pass);
        login = (Button) findViewById(R.id.login);

        edt_uid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                uid = edt_uid.getText().toString();
            }
        });
        edt_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pass = edt_pass.getText().toString();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check match from the excel file
                loading_dialog = new Dialog(MainActivity.this);
                loading_dialog.setContentView(R.layout.custom_popup_loading);
                Window window = loading_dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                TextView text = loading_dialog.findViewById(R.id.text);
                text.setText("Logging in, please wait...");
                loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                loading_dialog.setCanceledOnTouchOutside(true);
                loading_dialog.show();

                checkMatch();

            }
        });
    }

    private void checkMatch() {
        try{
            InputStream myInput;
            // initialize asset manager
            AssetManager assetManager = getAssets();
            //  open excel sheet
            myInput = assetManager.open("user_info.xls");
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
                    String user="";
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno==0){
                            user = myCell.toString();
                            if (array_users.contains(user)){
                                //do not add
                            }else{
                                array_users.add(user);
                            }
                        }
                        colno++;
                    }

                }
                rowno++;
            }

        }catch (Exception e) {
            System.out.println("error "+ e.toString());
        }

        //check if the inputted username is in the list of users
        if(array_users.contains(uid)){
            //track and confirm the password match
            int match_count = 0;
            try{
                InputStream myInput = null;
                // initialize asset manager
                AssetManager assetManager = getAssets();
                //  open excel sheet
                myInput = assetManager.open("user_info.xls");
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
                        String id="", password="";
                        while (cellIter.hasNext()) {
                            HSSFCell myCell = (HSSFCell) cellIter.next();
                            if (colno==0){
                                id = myCell.toString();
                                if (id.equals(uid)){
                                    //increase the colno by 1
                                    colno=colno+1;
                                    password = cellIter.next().toString();
                                    //pick the cell, convert to string
                                    if (password.equals(pass)){
                                        match_count = match_count+1;
                                    }else{
                                        //add nothing
                                    }
                                }else{
                                    //do nothing
                                }
                            }
                            colno++;
                        }
                    }
                    rowno++;
                }

            }catch (Exception e) {
//            Log.e(TAG, "error "+ e.toString());
                System.out.println("error "+ e.toString());
            }
            if (match_count==0){
                //toast password does not match
                loading_dialog.dismiss();
                Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                edt_pass.setError("Wrong password");
            }else{
                //validate login
                loading_dialog.dismiss();
                Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, SFingerActivity.class);
                i.putExtra("userid", uid);
                i.putExtra("from", "login");
                startActivity(i);
            }

        }else{
            //Stop dialog and show toast of user does not exist
            loading_dialog.dismiss();
            Toast.makeText(MainActivity.this, "This user does not exist!", Toast.LENGTH_SHORT).show();
            edt_uid.setError("Wrong user ID");
        }
    }
}