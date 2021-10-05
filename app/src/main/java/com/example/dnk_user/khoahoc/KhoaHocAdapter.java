package com.example.dnk_user.khoahoc;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dnk_user.R;
import com.example.dnk_user.config;
import com.example.dnk_user.lop.Lop;
import com.example.dnk_user.lop.LopAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KhoaHocAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    Context context;
    ArrayList<KhoaHoc> khoaHocs;
    ArrayList<KhoaHoc> khoaHocsOld;
    RecyclerView recyclerView;

    public KhoaHocAdapter(Context context, ArrayList<KhoaHoc> khoaHocs, RecyclerView recyclerView){
        this.context=context;
        this.khoaHocs=khoaHocs;
        this.recyclerView=recyclerView;
        this.khoaHocsOld=khoaHocs;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch=constraint.toString();
                if(strSearch.isEmpty())
                    khoaHocs=khoaHocsOld;
                else {
                    ArrayList<KhoaHoc> arrayList=new ArrayList<>();
                    for(int i=0;i<khoaHocsOld.size();i++){
                        if(khoaHocsOld.get(i).getTenkh().toLowerCase().contains(strSearch.toLowerCase()))
                            arrayList.add(khoaHocsOld.get(i));
                    }
                    khoaHocs=arrayList;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=khoaHocs;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                khoaHocs=(ArrayList<KhoaHoc>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tenkh;
        TextView solop;
        RecyclerView recyclerViewLop;
        public MyViewHolder(@NonNull View KhoaHocView) {
            super(KhoaHocView);
            tenkh=KhoaHocView.findViewById(R.id.kh_ten);
            solop=KhoaHocView.findViewById(R.id.kh_solop);
            recyclerViewLop=KhoaHocView.findViewById(R.id.recyclerViewLop);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.khoa_hoc,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        KhoaHoc khoaHoc=khoaHocs.get(position);
        ((MyViewHolder)holder).tenkh.setText(khoaHoc.getTenkh());
        ArrayList<Lop> lops=new ArrayList<>();

        StringRequest stringRequestSoLop=new StringRequest(Request.Method.POST, config.URL_KH_GetSoLopActive, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jsonGetSoLop",response.toString());
                try {
                    JSONObject jsonOb = new JSONObject(response.trim());
                    if (jsonOb.getInt(config.ThanhCong) == 1) {
                        JSONArray list = (JSONArray) jsonOb.getJSONArray(config.Table_KH);
                        JSONObject jsonObject=(JSONObject)list.get(0);
                        ((MyViewHolder)holder).solop.setText(jsonObject.getInt(config.SoLop_KH)+" lớp");
                        KhoaHocActivity.pDialog.dismiss();
                    }
                    else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                KhoaHocActivity.pDialog.show();
                Toast.makeText(context, "Kết nối thất bại", Toast.LENGTH_LONG).show();

            }

        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(config.Ma_KH,khoaHoc.getMakh()+"");
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequestSoLop);

        StringRequest stringRequestGetLop=new StringRequest(Request.Method.POST, config.URL_Lop_GetAllTheoKHActive, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jsonGetLopTheoKH",response.toString());
                try {
                    JSONObject jsonOb=new JSONObject(response.trim());
                    if(jsonOb.getInt(config.ThanhCong)==1){
                        JSONArray list = (JSONArray) jsonOb.getJSONArray(config.Table_Lop);
                        for(int i=0;i<list.length();i++){
                            Lop x=new Lop();
                            JSONObject jsonObject=(JSONObject)list.get(i);
                            x.setMalop(jsonObject.getInt(config.Ma_Lop));
                            x.setTenlop(jsonObject.getString(config.Ten_Lop));
                            x.setMota(jsonObject.getString(config.Mota_Lop));
                            x.setMakh(jsonObject.getInt(config.Ma_KH));
                            x.setMagv(jsonObject.getInt(config.Ma_GV));
                            x.setBatdau(jsonObject.getString(config.BD_Lop));
                            x.setKetthuc(jsonObject.getString(config.KT_Lop));
                            x.setAnhminhhoa(jsonObject.getString(config.Anh_Lop));
                            x.setDanhgia(jsonObject.getDouble(config.DanhGia_Lop));
                            x.setHocphi(jsonObject.getDouble(config.HocPhi_Lop));
                            x.setCahoc(jsonObject.getString(config.CaHoc_Lop));
                            lops.add(x);
                        }

                    }
                    setAdapter(((MyViewHolder)holder).recyclerViewLop,lops);

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
                params.put(config.Ma_KH,khoaHoc.getMakh()+"");
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequestGetLop);

    }

    public void setAdapter(RecyclerView recyclerView,ArrayList<Lop> lops){
        LopAdapter adapter=new LopAdapter(context,lops,recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }


    
    @Override
    public int getItemCount() {
        return khoaHocs.size();
    }


}
