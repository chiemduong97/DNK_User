package com.example.dnk_user.lop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.example.dnk_user.phieudanhgia.PhieuDanhGia;
import com.example.dnk_user.phieudanhgia.PhieuDanhGiaAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BinhLuanLop extends AppCompatActivity implements PhieuDanhGiaDAO {
    TextView tvTenLop;
    ImageView ivBack;
    RecyclerView recyclerView;
    ArrayList<PhieuDanhGia> phieuDanhGias;
    ProgressDialog pDialog;
    PhieuDanhGiaAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binh_luan_lop);
        tvTenLop=findViewById(R.id.tvTenLop_BinhLuanLop);
        ivBack=findViewById(R.id.ivBack_BinhLuanLop);
        recyclerView=findViewById(R.id.recyclerViewBinhLuanLop);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getAllTheoLop();
        tvTenLop.setText(getIntent().getStringExtra(config.Ten_Lop));
    }

    @Override
    public void getAll() {

    }

    @Override
    public void getAllTheoLop() {
        phieuDanhGias=new ArrayList<>();
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, config.URL_PhieuDG_GetAllTheoLop, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jsonGetAllPDGTheoLop",response.toString());
                try {
                    JSONObject jsonOb=new JSONObject(response.trim());
                    if(jsonOb.getInt(config.ThanhCong)==1){
                        JSONArray list = (JSONArray) jsonOb.getJSONArray(config.Table_PhieuDG);
                        for(int i=0;i<list.length();i++){
                            PhieuDanhGia x=new PhieuDanhGia();
                            JSONObject jsonObject=(JSONObject)list.get(i);
                            x.setMapdg(jsonObject.getInt(config.Ma_PhieuDG));
                            x.setMahv(jsonObject.getInt(config.Ma_HV));
                            x.setMalop(jsonObject.getInt(config.Ma_Lop));
                            x.setDiem(jsonObject.getDouble(config.Diem_PhieuDG));
                            x.setNgaydg(jsonObject.getString(config.NgayDG_PhieuDG));
                            x.setLuotthich(jsonObject.getInt(config.LuotThich_PhieuDG));
                            x.setBinhluan(jsonObject.getString(config.BinhLuan_PhieuDG));
                            phieuDanhGias.add(x);
                        }

                    }
                    else {
                        Toast.makeText(BinhLuanLop.this,jsonOb.getString(config.ThongBao),Toast.LENGTH_SHORT).show();
                    }
                    adapter=new PhieuDanhGiaAdapter(BinhLuanLop.this,phieuDanhGias,recyclerView);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(BinhLuanLop.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapter);
                    pDialog.dismiss();


                } catch (JSONException ex) {
                    pDialog.dismiss();
                    Log.d("Thất bại","");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(BinhLuanLop.this, "Kết nối thất bại", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(config.Ma_Lop,getIntent().getIntExtra(config.Ma_Lop,-1)+"");
                return params;
            }
        };
        Volley.newRequestQueue(BinhLuanLop.this).add(stringRequest);
    }

    @Override
    public void add() {

    }

    @Override
    public void updateDanhGia() {

    }

    @Override
    public void updateLuotThich() {

    }

    @Override
    public void delete() {

    }
}