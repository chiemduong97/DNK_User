package com.example.dnk_user.hocvien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dnk_user.R;
import com.example.dnk_user.config;
import com.example.dnk_user.dao.HocVienDAO;
import com.example.dnk_user.dao.SharedPreferencesDAO;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DoiMatKhauHocVien extends AppCompatActivity implements SharedPreferencesDAO, HocVienDAO {
    EditText etMKCu,etMKMoi,etXacNhanMK;
    TextInputLayout inputLayoutMKCu,inputLayoutMKMoi,inputLayoutXacNhanMK;
    Button btnLuu;
    ImageView ivBack;
    HocVien hocVien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau_hoc_vien);
        etMKCu=findViewById(R.id.etMKCu_DoiMK);
        etMKMoi=findViewById(R.id.etMKMoi_DoiMK);
        etXacNhanMK=findViewById(R.id.etXacNhan_DoiMK);
        btnLuu=findViewById(R.id.btnLuu_DoiMK);
        inputLayoutMKCu=findViewById(R.id.textInputMKCu_DoiMK);
        inputLayoutMKMoi=findViewById(R.id.textInputMKMoi_DoiMK);
        inputLayoutXacNhanMK=findViewById(R.id.textInputXacNhan_DoiMK);
        ivBack=findViewById(R.id.ivBack_DoiMK);
        inputLayoutMKCu.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    inputLayoutMKCu.setError("Không được để trống");
                    inputLayoutMKCu.setErrorEnabled(true);
                }
                else
                    inputLayoutMKCu.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputLayoutMKMoi.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    inputLayoutMKMoi.setError("Không được để trống");
                    inputLayoutMKMoi.setErrorEnabled(true);
                }
                else
                    inputLayoutMKMoi.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputLayoutXacNhanMK.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etXacNhanMK.getText().toString().equals(etMKMoi.getText().toString())==false){
                    inputLayoutXacNhanMK.setError("Xác nhận chưa chính xác");
                    inputLayoutXacNhanMK.setErrorEnabled(true);
                }
                else
                    inputLayoutXacNhanMK.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMKCu.getText().toString().equals("")||etMKMoi.getText().toString().equals("")||etXacNhanMK.getText().toString().equals(""))
                {
                    Toast.makeText(DoiMatKhauHocVien.this,"Không được để trống",Toast.LENGTH_SHORT).show();
                }
                else if(!etXacNhanMK.getText().toString().equals(etMKMoi.getText().toString())){
                    Toast.makeText(DoiMatKhauHocVien.this,"Xác nhận mật khẩu chưa chính xác",Toast.LENGTH_SHORT).show();
                }
                else {
                    hocVien=restore();
                    if(!etMKCu.getText().toString().equals(hocVien.getMatkhau())){
                        Toast.makeText(DoiMatKhauHocVien.this,"Mật khẩu cũ chưa chính xác",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        save();
                        hocVien=restore();
                        update();
                        finish();
                    }
                }
            }
        });

    }

    @Override
    public void getHocVien() {

    }

    @Override
    public void getAll() {

    }

    @Override
    public void add() {

    }

    @Override
    public void update() {
        FragmentHocVien.pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, config.URL_HV_Update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jsonUpdateHV",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.trim());
                    if (jsonObject.getInt(config.ThanhCong) == 1) {
                        Toast.makeText(DoiMatKhauHocVien.this, jsonObject.getString(config.ThongBao), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(DoiMatKhauHocVien.this, jsonObject.getString(config.ThongBao), Toast.LENGTH_LONG).show();
                    }
                    FragmentHocVien.pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FragmentHocVien.pDialog.dismiss();
                Toast.makeText(DoiMatKhauHocVien.this, "Kết nối thất bại", Toast.LENGTH_SHORT).show();
            }

        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(config.Ma_HV,hocVien.getMahv()+"");
                params.put(config.Ten_HV,hocVien.getTenhv());
                params.put(config.TaiKhoan_HV,hocVien.getTaikhoan());
                params.put(config.MatKhau_HV,hocVien.getMatkhau());
                params.put(config.Email_HV,hocVien.getEmail());
                params.put(config.SDT_HV,hocVien.getSdt());
                params.put(config.DiaChi_HV,hocVien.getDiachi());
                params.put(config.TrangThai_HV,hocVien.getTrangthai()+"");
                return params;
            }
        };
        Volley.newRequestQueue(DoiMatKhauHocVien.this).add(stringRequest);
    }

    @Override
    public void updateAnh() {

    }

    @Override
    public void login() {

    }

    @Override
    public void save() {
        SharedPreferences sharedPreferences=getSharedPreferences("HV_active.txt",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(config.MatKhau_HV,etMKMoi.getText().toString());
        editor.commit();
    }

    @Override
    public HocVien restore() {
        hocVien=new HocVien();
        SharedPreferences sharedPreferences=getSharedPreferences("HV_active.txt",MODE_PRIVATE);
        hocVien.setMahv(sharedPreferences.getInt(config.Ma_HV,-1));
        hocVien.setTenhv(sharedPreferences.getString(config.Ten_HV,""));
        hocVien.setTaikhoan(sharedPreferences.getString(config.TaiKhoan_HV,""));
        hocVien.setMatkhau(sharedPreferences.getString(config.MatKhau_HV,""));
        hocVien.setEmail(sharedPreferences.getString(config.Email_HV,""));
        hocVien.setSdt(sharedPreferences.getString(config.SDT_HV,""));
        hocVien.setDiachi(sharedPreferences.getString(config.DiaChi_HV,""));
        hocVien.setAvatar(sharedPreferences.getString(config.Avatar_HV,""));
        hocVien.setTrangthai(sharedPreferences.getInt(config.TrangThai_HV,-1));
        return hocVien;
    }

    @Override
    public void delete() {

    }

    @Override
    public void reset() {

    }
}