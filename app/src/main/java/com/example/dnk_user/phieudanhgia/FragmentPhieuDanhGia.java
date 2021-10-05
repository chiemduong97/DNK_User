package com.example.dnk_user.phieudanhgia;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dnk_user.MainActivity;
import com.example.dnk_user.R;
import com.example.dnk_user.config;
import com.example.dnk_user.dao.PhieuDanhGiaDAO;
import com.example.dnk_user.phieudangky.PhieuDangKy;
import com.example.dnk_user.phieudangky.PhieuDangKyAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentPhieuDanhGia extends Fragment implements PhieuDanhGiaDAO {
    ArrayList<PhieuDanhGia> phieuDanhGias;
    RecyclerView recyclerView;
    public static ProgressDialog pDialog;
    PhieuDanhGiaAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_phieu_danh_gia,null);
        recyclerView=view.findViewById(R.id.recyclerViewPDG);
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        Animation animation = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.rorate);

        MainActivity.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.refresh.startAnimation(animation);
                onResume();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAll();
    }

    @Override
    public void getAll() {
        phieuDanhGias=new ArrayList<>();
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, config.URL_PhieuDG_GetAll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jsonGetAllPDG",response.toString());
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
                        Toast.makeText(getContext(),jsonOb.getString(config.ThongBao),Toast.LENGTH_SHORT).show();
                    }
                    adapter=new PhieuDanhGiaAdapter(getContext(),phieuDanhGias,recyclerView);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
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
                Toast.makeText(getContext(), "Kết nối thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    @Override
    public void getAllTheoLop() {

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
