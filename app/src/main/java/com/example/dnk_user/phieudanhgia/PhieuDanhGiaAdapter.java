package com.example.dnk_user.phieudanhgia;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.dnk_user.R;
import com.example.dnk_user.config;
import com.example.dnk_user.dao.SharedPreferencesDAO;
import com.example.dnk_user.hocvien.HocVien;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhieuDanhGiaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SharedPreferencesDAO {
    Context context;
    ArrayList<PhieuDanhGia> phieuDanhGias;
    RecyclerView recyclerView;
    HocVien hocVien;
    public PhieuDanhGiaAdapter(Context context,ArrayList<PhieuDanhGia> phieuDanhGias,RecyclerView recyclerView){
        this.context=context;
        this.phieuDanhGias=phieuDanhGias;
        this.recyclerView=recyclerView;
    }

    @Override
    public void save() {

    }

    @Override
    public HocVien restore() {
        hocVien=new HocVien();
        SharedPreferences sharedPreferences=context.getSharedPreferences("HV_active.txt",context.MODE_PRIVATE);
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

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView ivAvatar;
        TextView tvDanhGia;
        TextView tvNgayDG;
        TextView tvBinhLuan;
        ToggleButton toggleButton;
        TextView tvLuotThich;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar=itemView.findViewById(R.id.ivAvatar_PDG);
            tvDanhGia=itemView.findViewById(R.id.tvDanhGia_PDG);
            tvNgayDG=itemView.findViewById(R.id.tvNgayDG_PDG);
            tvBinhLuan=itemView.findViewById(R.id.tvBinhLuan_PDG);
            toggleButton=itemView.findViewById(R.id.toggle_PDG);
            tvLuotThich=itemView.findViewById(R.id.tvLuotThich_PDG);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.phieu_danh_gia,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PhieuDanhGia phieuDanhGia=phieuDanhGias.get(position);

        StringRequest stringRequestHV=new StringRequest(Request.Method.POST, config.URL_HV_Get, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jsonGetHocVien",response.toString());
                try {
                    JSONObject jsonOb=new JSONObject(response.trim());
                    if(jsonOb.getInt(config.ThanhCong)==1){
                        JSONArray list = (JSONArray) jsonOb.getJSONArray(config.Table_HV);
                        for(int i=0;i<list.length();i++){
                            JSONObject jsonObject=(JSONObject)list.get(i);
                            if(jsonObject.getString(config.Avatar_HV).equalsIgnoreCase("null")==false){
                                Glide.with(context).asBitmap().load(config.URL_HV_Avatar+jsonObject.getString(config.Avatar_HV)).into(((MyViewHolder)holder).ivAvatar);
                            }
                            String tenTaiKhoan="<strong>"+jsonObject.getString(config.TaiKhoan_HV)+"</strong>";
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                ((MyViewHolder)holder).tvDanhGia.setText(Html.fromHtml(tenTaiKhoan,Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                ((MyViewHolder)holder).tvDanhGia.setText(Html.fromHtml(tenTaiKhoan));
                            }
                            ((MyViewHolder)holder).tvDanhGia.append(" đánh giá "+phieuDanhGia.getDiem()+"* ");
                            StringRequest stringRequestLop=new StringRequest(Request.Method.POST, config.URL_Lop_Get, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("jsonGetLop",response.toString());
                                    try {
                                        JSONObject jsonOb=new JSONObject(response.trim());
                                        if(jsonOb.getInt(config.ThanhCong)==1){
                                            JSONArray list = (JSONArray) jsonOb.getJSONArray(config.Table_Lop);
                                            JSONObject jsonObject=(JSONObject)list.get(0);
                                            String tenLop="<strong><font color='#000080'>"+jsonObject.getString(config.Ten_Lop)+"</font></strong>";
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                ((MyViewHolder)holder).tvDanhGia.append(Html.fromHtml(tenLop,Html.FROM_HTML_MODE_COMPACT));
                                            } else {
                                                ((MyViewHolder)holder).tvDanhGia.append(Html.fromHtml(tenLop));
                                            }
                                        }
                                        else {

                                            Toast.makeText(context,jsonOb.getString(config.ThongBao),Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException ex) {

                                        Log.d("Thất bại","");

                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }

                            })
                            {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put(config.Ma_Lop,phieuDanhGia.getMalop()+"");
                                    return params;
                                }
                            };
                            Volley.newRequestQueue(context).add(stringRequestLop);

                        }
                    }
                    else {
                        Toast.makeText(context,jsonOb.getString(config.ThongBao),Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException ex) {
                    Log.d("Thất bại","");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Kết nối thất bại", Toast.LENGTH_SHORT).show();

            }

        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(config.Ma_HV,phieuDanhGia.getMahv()+"");
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequestHV);
        hocVien=restore();
        StringRequest stringRequestThich=new StringRequest(Request.Method.POST, config.URL_Thich_GetAll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jsonGetAllThich",response.toString());
                try {
                    JSONObject jsonOb=new JSONObject(response.trim());
                    if(jsonOb.getInt(config.ThanhCong)==1){
                        JSONArray list = (JSONArray) jsonOb.getJSONArray(config.Table_Thich);
                        JSONObject jsonObject=(JSONObject)list.get(0);
                        if(jsonObject.getInt(config.TrangThai_Thich)==1){
                            ((MyViewHolder)holder).toggleButton.setChecked(true);
                            ((MyViewHolder)holder).tvLuotThich.setTextColor(Color.RED);
                        }
                        else {
                            ((MyViewHolder)holder).toggleButton.setChecked(false);
                            ((MyViewHolder)holder).tvLuotThich.setTextColor(Color.BLACK);
                        }
                    }
                    else {
                        ((MyViewHolder)holder).toggleButton.setChecked(false);
                        ((MyViewHolder)holder).tvLuotThich.setTextColor(Color.BLACK);
                    }

                } catch (JSONException ex) {

                    Log.d("Thất bại","");

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(config.Ma_PhieuDG,phieuDanhGia.getMapdg()+"");
                params.put(config.Ma_HV,hocVien.getMahv()+"");
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequestThich);




        ((MyViewHolder)holder).tvNgayDG.setText(phieuDanhGia.getNgaydg());
        ((MyViewHolder)holder).tvBinhLuan.setText(phieuDanhGia.getBinhluan());
        ((MyViewHolder)holder).tvLuotThich.setText(phieuDanhGia.getLuotthich()+" lượt thích");
        ((MyViewHolder)holder).toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((MyViewHolder)holder).toggleButton.isChecked()==true){
                    ((MyViewHolder)holder).tvLuotThich.setTextColor(Color.RED);
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, config.URL_PhieuDG_UpdateLuotThich, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("jsonLuotThich",response.toString());
                            try {
                                JSONObject jsonOb=new JSONObject(response.trim());
                                if(jsonOb.getInt(config.ThanhCong)==1){
                                    JSONArray list = (JSONArray) jsonOb.getJSONArray(config.Table_PhieuDG);
                                    JSONObject jsonObject=(JSONObject)list.get(0);
                                    ((MyViewHolder)holder).tvLuotThich.setText(jsonObject.getInt(config.LuotThich_PhieuDG)+" lượt thích");

                                }
                                else {

                                    Toast.makeText(context,jsonOb.getString(config.ThongBao),Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException ex) {

                                Log.d("Thất bại","");

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }

                    })
                    {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put(config.Ma_PhieuDG,phieuDanhGia.getMapdg()+"");
                            params.put(config.Ma_HV,hocVien.getMahv()+"");
                            params.put(config.TrangThai_Thich,"1");
                            return params;
                        }
                    };
                    Volley.newRequestQueue(context).add(stringRequest);
                }
                else {
                    ((MyViewHolder)holder).tvLuotThich.setTextColor(Color.BLACK);
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, config.URL_PhieuDG_UpdateLuotThich, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("jsonLuotThich",response.toString());
                            try {
                                JSONObject jsonOb=new JSONObject(response.trim());
                                if(jsonOb.getInt(config.ThanhCong)==1){
                                    JSONArray list = (JSONArray) jsonOb.getJSONArray(config.Table_PhieuDG);
                                    JSONObject jsonObject=(JSONObject)list.get(0);
                                    ((MyViewHolder)holder).tvLuotThich.setText(jsonObject.getInt(config.LuotThich_PhieuDG)+" lượt thích");

                                }
                                else {

                                    Toast.makeText(context,jsonOb.getString(config.ThongBao),Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException ex) {

                                Log.d("Thất bại","");

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }

                    })
                    {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put(config.Ma_PhieuDG,phieuDanhGia.getMapdg()+"");
                            params.put(config.Ma_HV,hocVien.getMahv()+"");
                            params.put(config.TrangThai_Thich,"0");
                            return params;
                        }
                    };
                    Volley.newRequestQueue(context).add(stringRequest);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return phieuDanhGias.size();
    }
}
