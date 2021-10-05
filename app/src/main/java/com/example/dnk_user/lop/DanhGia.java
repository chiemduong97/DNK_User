package com.example.dnk_user.lop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dnk_user.R;
import com.example.dnk_user.config;
import com.example.dnk_user.dao.PhieuDanhGiaDAO;
import com.example.dnk_user.dao.SharedPreferencesDAO;
import com.example.dnk_user.hocvien.HocVien;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DanhGia extends AppCompatActivity implements SharedPreferencesDAO, PhieuDanhGiaDAO {
    RatingBar ratingBarDanhGia;
    EditText etBinhLuan;
    Button btnLuuDanhGia;
    ImageView ivBack;
    TextView tvXemDanhGia;
    TextView tvTenLop;
    int diem;
    HocVien hocVien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_gia);
        ratingBarDanhGia=findViewById(R.id.ratingBarDanhGia);
        etBinhLuan=findViewById(R.id.etBinhLuan);
        btnLuuDanhGia=findViewById(R.id.btnLuuDanhGia);
        ivBack=findViewById(R.id.ivBack_DanhGia);
        tvXemDanhGia=findViewById(R.id.tvXemDanhGia);
        tvTenLop=findViewById(R.id.tvTenLop_DanhGia);
        tvTenLop.setText(getIntent().getStringExtra(config.Ten_Lop));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ratingBarDanhGia.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                diem=(int)rating;
            }
        });

        btnLuuDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ratingBarDanhGia.getRating()==0){
                    Toast.makeText(DanhGia.this,"Bạn chưa đánh giá",Toast.LENGTH_SHORT).show();
                }
                else if(etBinhLuan.getText().toString().equals("")){
                    Toast.makeText(DanhGia.this,"Bạn chưa bình luận",Toast.LENGTH_SHORT).show();
                }
                else{
                    add();
                }
            }
        });

        tvXemDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DanhGia.this,BinhLuanLop.class);
                intent.putExtra(config.Ma_Lop,getIntent().getIntExtra(config.Ma_Lop,-1));
                intent.putExtra(config.Ten_Lop,getIntent().getStringExtra(config.Ten_Lop));
                startActivity(intent);
            }
        });


    }

    @Override
    public void save() {

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
    public void getAll() {

    }

    @Override
    public void getAllTheoLop() {

    }

    @Override
    public void add() {
        hocVien=restore();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, config.URL_PhieuDG_Add, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jsonAddPhieuDG",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.trim());
                    if (jsonObject.getInt(config.ThanhCong) == 1) {
                        Toast.makeText(DanhGia.this, "Đánh giá thành công", Toast.LENGTH_SHORT).show();
                        updateDanhGia();
                        finish();
                    }
                    else {
                        Toast.makeText(DanhGia.this, jsonObject.getString(config.ThongBao), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DanhGia.this, "Kết nối thất bại", Toast.LENGTH_SHORT).show();

            }

        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(config.Ma_HV,hocVien.getMahv()+"");
                params.put(config.Ma_Lop,getIntent().getIntExtra(config.Ma_Lop,-1)+"");
                params.put(config.Diem_PhieuDG,diem+"");
                params.put(config.BinhLuan_PhieuDG,etBinhLuan.getText().toString());
                return params;
            }
        };
        Volley.newRequestQueue(DanhGia.this).add(stringRequest);
    }

    @Override
    public void updateDanhGia() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, config.URL_PhieuDG_UpdateDG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jsonUpdateDG",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.trim());
                    if (jsonObject.getInt(config.ThanhCong) == 1) {

                    }
                    else {
                        Toast.makeText(DanhGia.this, jsonObject.getString(config.ThongBao), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DanhGia.this, "Kết nối thất bại", Toast.LENGTH_SHORT).show();

            }

        });
        Volley.newRequestQueue(DanhGia.this).add(stringRequest);
    }

    @Override
    public void updateLuotThich() {

    }
}