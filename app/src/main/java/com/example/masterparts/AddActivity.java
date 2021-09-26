package com.example.masterparts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddActivity<uri> extends AppCompatActivity {

    //view
    EditText mTitleEt, mDescriptionEt,mBrandEt,mEnginecEt,mFueluseEt,mAddressEt;
    Button mSaveBtn,mListBtn;

    //dialog
    ProgressDialog pd;

    //firebase instance
    FirebaseFirestore db;

    String pId,pTitle,pDescription,pBrand,pEnginec,pFueluse,pAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ActionBar actionBar = getSupportActionBar();


        mTitleEt = findViewById(R.id.titleEt);
        mDescriptionEt = findViewById(R.id.descriptionEt);
        mBrandEt = findViewById(R.id.brandEt);
        mEnginecEt = findViewById(R.id.enginecEt);
        mFueluseEt = findViewById(R.id.fueluseEt);
        mAddressEt = findViewById(R.id.addressEt);
        mSaveBtn = findViewById(R.id.saveBtn);
        mListBtn = findViewById(R.id.listBtn);

                Bundle bundle = getIntent().getExtras();
                if(bundle != null){
                    //actionBar.setTitle("Update Data ");
                    mSaveBtn.setText("Update Data");
                    pId = bundle.getString("pId");
                    pTitle = bundle.getString("pTitle");
                    pDescription = bundle.getString("pDescription");
                    pBrand = bundle.getString("pBrand");
                    pEnginec = bundle.getString("pEnginec");
                    pFueluse = bundle.getString("pFueluse");
                    pAddress = bundle.getString("pAddress");

                    mTitleEt.setText(pTitle);
                    mDescriptionEt.setText(pDescription);
                    mBrandEt.setText(pBrand);
                    mEnginecEt.setText(pEnginec);
                    mFueluseEt.setText(pFueluse);
                    mAddressEt.setText(pAddress);

                }
                else{

                    mSaveBtn.setText("Save");
                }

        //progress
        pd = new ProgressDialog(this);

        //firestore
        db = FirebaseFirestore.getInstance();

        //click button to send the data
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = getIntent().getExtras();
                if (bundle != null){
                     String id = pId;
                    String title = mTitleEt.getText().toString().trim();
                    String description = mDescriptionEt.getText().toString().trim();
                    String brand = mBrandEt.getText().toString().trim();
                    String enginec = mEnginecEt.getText().toString().trim();
                    String fueluse = mFueluseEt.getText().toString().trim();
                    String address = mAddressEt.getText().toString().trim();

                    updateData(id,title,description,brand,enginec,fueluse,address);
                }
                else{
                    String title = mTitleEt.getText().toString().trim();
                    String description = mDescriptionEt.getText().toString().trim();
                    String brand = mBrandEt.getText().toString().trim();
                    String enginec = mEnginecEt.getText().toString().trim();
                    String fueluse = mFueluseEt.getText().toString().trim();
                    String address = mAddressEt.getText().toString().trim();

                    if (TextUtils.isDigitsOnly(title)) {
                        mTitleEt.setError("Plz enter the value");
                        return;
                    }
                    else if (TextUtils.isEmpty(description)) {
                        mDescriptionEt.setError("Fieled is Empty");
                        return;
                    }
                    else if (TextUtils.isEmpty(brand)) {
                        mBrandEt.setError("Fieled is Empty");
                        return;
                    }
                    else if (TextUtils.isEmpty(enginec)) {
                        mEnginecEt.setError("Field is Empty");
                        return;
                    }
                    else if (TextUtils.isEmpty(fueluse)) {
                        mFueluseEt.setError("Field is Empty");
                    }
                    else if (TextUtils.isEmpty(address)) {
                        mAddressEt.setError("Field is Empty");
                    }
                    else if(title.isEmpty() && description.isEmpty() && brand.isEmpty() && enginec.isEmpty() && fueluse.isEmpty() && address.isEmpty()) {
                        new SweetAlertDialog(AddActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("All fields are empty!")
                                .show();
                    }
                    uploadData(title, description,brand,enginec,fueluse,address);
                }

                //input data
                String title = mTitleEt.getText().toString();
                String description = mDescriptionEt.getText().toString();
                String brand = mBrandEt.getText().toString();
                String enginec = mEnginecEt.getText().toString();
                String fueluse = mFueluseEt.getText().toString();
                String address = mAddressEt.getText().toString();

                //function call to upload data
                uploadData(title, description ,brand ,enginec ,fueluse ,address);


            }

        });
            mListBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(AddActivity.this ,ListActivity.class));
                    finish();
                }
            });
    }

    private void updateData(String id, String title, String description, String brand, String enginec, String fueluse, String address) {
        db.collection("Documents").document(id)
                .update("title",title,"description",description,"brand",brand,"enginec",enginec,"fueluse",fueluse,"address",address)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        new SweetAlertDialog(AddActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Update successfully !")
                                .show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                new SweetAlertDialog(AddActivity.this,SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Try Again!")
                        .show();
            }
        });
    }

    private void uploadData(String title, String description,String brand, String enginec, String fueluse, String address) {

        String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);
        doc.put("title", title);
        doc.put("description", description);
       doc.put("brand", brand);
        doc.put("enginec",enginec);
        doc.put("fueluse",fueluse);
       doc.put("address",address);

        db.collection("Documents").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        new SweetAlertDialog(AddActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Successefully!")
                                .show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        pd.dismiss();
                            new SweetAlertDialog(AddActivity.this,SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Something went wrong")
                                    .show();

                    }
                });
    }
}