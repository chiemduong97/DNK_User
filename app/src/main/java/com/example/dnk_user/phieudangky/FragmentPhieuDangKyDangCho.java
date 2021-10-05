package com.example.dnk_user.phieudangky;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
import com.example.dnk_user.dao.PhieuDangKyDAO;
import com.example.dnk_user.dao.SharedPreferencesDAO;
import com.example.dnk_user.hocvien.HocVien;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentPhieuDangKyDangCho extends Fragment implements PhieuDangKyDAO, SharedPreferencesDAO {
    ArrayList<PhieuDangKy> phieuDangKIES;
    RecyclerView recyclerView;
    HocVien hocVien;
    public static ProgressDialog pDialog;
    PhieuDangKyAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_phieu_dang_ky_dang_cho,null);
        recyclerView=view.findViewById(R.id.recyclerViewPDKDangCho);
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
        getAllDangChoTheoHV();
    }

    @Override
    public void getAll() {

    }

    @Override
    public void getAllDangCho() {

    }

    @Override
    public void getAllDaXacNhanTheoHV() {

    }

    @Override
    public void getAllDangChoTheoHV() {
        phieuDangKIES=new ArrayList<>();
        hocVien=restore();
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, config.URL_PhieuDK_GetAllDangChoTheoHV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jsonGetPDKDangChoTheoHV",response.toString());
                try {
                    JSONObject jsonOb=new JSONObject(response.trim());
                    if(jsonOb.getInt(config.ThanhCong)==1){
                        JSONArray list = (JSONArray) jsonOb.getJSONArray(config.Table_PhieuDK);
                        for(int i=0;i<list.length();i++){
                            PhieuDangKy x=new PhieuDangKy();
                            JSONObject jsonObject=(JSONObject)list.get(i);
                            x.setMapdk(jsonObject.getInt(config.Ma_PhieuDK));
                            x.setMahv(jsonObject.getInt(config.Ma_HV));
                            x.setMalop(jsonObject.getInt(config.Ma_Lop));
                            x.setBatdau(jsonObject.getString(config.BD_Lop));
                            x.setKetthuc(jsonObject.getString(config.KT_Lop));
                            x.setTrangthai(jsonObject.getInt(config.TrangThai_PhieuDK));
                            x.setCahoc(jsonObject.getString(config.CaHoc_Lop));
                            phieuDangKIES.add(x);
                        }

                    }
                    else {
                        Toast.makeText(getContext(),jsonOb.getString(config.ThongBao),Toast.LENGTH_SHORT).show();
                    }
                    adapter=new PhieuDangKyAdapter(getContext(),phieuDangKIES,recyclerView);
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

        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(config.Ma_HV,hocVien.getMahv()+"");
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    @Override
    public void add() {

    }

    @Override
    public void update() {

    }

    @Override
    public void save() {

    }

    @Override
    public HocVien restore() {
        hocVien=new HocVien();
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("HV_active.txt",getActivity().MODE_PRIVATE);
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
}
