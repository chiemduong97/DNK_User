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

public class ThongTinHocVien extends AppCompatActivity implements SharedPreferencesDAO, HocVienDAO {
    EditText etTenhv,etEmailhv,etDiaChihv,etSDThv;
    TextInputLayout inputLayoutTenhv,inputLayoutEmailhv,inputLayoutDiaChihv,inputLayoutSDThv;
    Button btnLuu;
    HocVien hocVien;
    ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_hoc_vien);
        etTenhv=findViewById(R.id.etHoTen_TThv);
        etEmailhv=findViewById(R.id.etEmail_TThv);
        etDiaChihv=findViewById(R.id.etDiaChi_TThv);
        etSDThv=findViewById(R.id.etSDT_TThv);
        btnLuu=findViewById(R.id.btnLuu_TThv);
        inputLayoutTenhv=findViewById(R.id.textInputHoTen_TThv);
        inputLayoutEmailhv=findViewById(R.id.textInputEmail_TThv);
        inputLayoutDiaChihv=findViewById(R.id.textInputDiaChi_TThv);
        inputLayoutSDThv=findViewById(R.id.textInputDiaChi_TThv);

        inputLayoutTenhv.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    inputLayoutTenhv.setError("Không được để trống");
                    inputLayoutTenhv.setErrorEnabled(true);
                }
                else
                    inputLayoutTenhv.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputLayoutEmailhv.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    inputLayoutEmailhv.setError("Không được để trống");
                    inputLayoutEmailhv.setErrorEnabled(true);
                }
                else
                    inputLayoutEmailhv.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputLayoutDiaChihv.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    inputLayoutDiaChihv.setError("Không được để trống");
                    inputLayoutDiaChihv.setErrorEnabled(true);
                }
                else
                    inputLayoutDiaChihv.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputLayoutSDThv.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    inputLayoutSDThv.setError("Không được để trống");
                    inputLayoutSDThv.setErrorEnabled(true);
                }
                else
                    inputLayoutSDThv.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ivBack=findViewById(R.id.ivBack_TThv);
        hocVien=restore();
        etTenhv.setText(hocVien.getTenhv());
        etEmailhv.setText(hocVien.getEmail());
        etDiaChihv.setText(hocVien.getDiachi());
        etSDThv.setText(hocVien.getSdt());

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etTenhv.getText().toString().equals("")||etEmailhv.getText().toString().equals("")||etDiaChihv.getText().toString().equals("")||etSDThv.getText().toString().equals(""))
                {
                    Toast.makeText(ThongTinHocVien.this,"Không được để trống",Toast.LENGTH_SHORT).show();
                }
                else {
                    save();
                    hocVien=restore();
                    update();
                    finish();
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
                        Toast.makeText(ThongTinHocVien.this, jsonObject.getString(config.ThongBao), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ThongTinHocVien.this, jsonObject.getString(config.ThongBao), Toast.LENGTH_LONG).show();
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
                Toast.makeText(ThongTinHocVien.this, "Kết nối thất bại", Toast.LENGTH_SHORT).show();
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
        Volley.newRequestQueue(ThongTinHocVien.this).add(stringRequest);
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
        editor.putString(config.Ten_HV,etTenhv.getText().toString());
        editor.putString(config.Email_HV,etEmailhv.getText().toString());
        editor.putString(config.SDT_HV,etSDThv.getText().toString());
        editor.putString(config.DiaChi_HV,etDiaChihv.getText().toString());
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