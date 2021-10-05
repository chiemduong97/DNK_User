package com.example.dnk_user.lop;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.dnk_user.khoahoc.KhoaHocActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<Lop> lops;
    ArrayList<Lop> lopsOld;
    RecyclerView recyclerView;

    public LopAdapter(Context context, ArrayList<Lop> lops, RecyclerView recyclerView){
        this.context=context;
        this.lops=lops;
        this.recyclerView=recyclerView;
        this.lopsOld=lops;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView anhlop;
        TextView tenlop;
        TextView danhgia;
        TextView sohv;

        public MyViewHolder(@NonNull View LopView) {
            super(LopView);
            anhlop=LopView.findViewById(R.id.lop_anh);
            tenlop=LopView.findViewById(R.id.lop_ten);
            danhgia=LopView.findViewById(R.id.lop_danhgia);
            sohv=LopView.findViewById(R.id.lop_sohv);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.lop,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Lop lop=lops.get(position);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, config.URL_Lop_GetSoHV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jsonGetSoHV",response.toString());
                try {
                    JSONObject jsonOb = new JSONObject(response.trim());
                    if (jsonOb.getInt(config.ThanhCong) == 1) {
                        JSONArray list = (JSONArray) jsonOb.getJSONArray(config.Table_Lop);
                        JSONObject jsonObject=(JSONObject)list.get(0);
                        ((MyViewHolder)holder).sohv.setText(" "+jsonObject.getInt(config.SoHV_Lop)+" học viên");
                    }
                    else {
                        Toast.makeText(context, jsonOb.getString(config.ThongBao), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                params.put(config.Ma_Lop,lop.getMalop()+"");
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
        Glide.with(context).asBitmap().load(config.URL_Lop_AnhMinhHoa+lop.getAnhminhhoa()).into(((MyViewHolder)holder).anhlop);
        ((MyViewHolder)holder).tenlop.setText(lop.getTenlop());
        ((MyViewHolder)holder).danhgia.setText(" "+lop.getDanhgia()+"/5");
        ((MyViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenkh=((KhoaHocActivity)context).getIntent().getStringExtra(config.Ten_KH);
                Intent intent=new Intent(context, ThongTinLop.class);
                intent.putExtra(config.Ma_Lop,lop.getMalop());
                intent.putExtra(config.Ten_Lop,lop.getTenlop());
                intent.putExtra(config.Mota_Lop,lop.getMota());
                intent.putExtra(config.Ma_KH,lop.getMakh());
                intent.putExtra(config.Ma_GV,lop.getMagv());
                intent.putExtra(config.BD_Lop,lop.getBatdau());
                intent.putExtra(config.KT_Lop,lop.getKetthuc());
                intent.putExtra(config.CaHoc_Lop,lop.getCahoc());
                intent.putExtra(config.Anh_Lop,lop.getAnhminhhoa());
                intent.putExtra(config.DanhGia_Lop,lop.getDanhgia());
                intent.putExtra(config.HocPhi_Lop,lop.getHocphi());
                intent.putExtra(config.CaHoc_Lop,lop.getCahoc());
                intent.putExtra(config.Ten_KH,tenkh);
                context.startActivity(intent);
            }
        });


    }


    
    @Override
    public int getItemCount() {
        return lops.size();
    }


}
