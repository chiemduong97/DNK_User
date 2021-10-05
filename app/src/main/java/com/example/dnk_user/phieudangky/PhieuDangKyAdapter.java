package com.example.dnk_user.phieudangky;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.dnk_user.MainActivity;
import com.example.dnk_user.R;
import com.example.dnk_user.config;
import com.example.dnk_user.dao.PhieuDangKyDAO;
import com.example.dnk_user.dao.SharedPreferencesDAO;
import com.example.dnk_user.hocvien.HocVien;
import com.example.dnk_user.khoahoc.KhoaHocActivity;
import com.example.dnk_user.lop.Lop;
import com.example.dnk_user.lop.LopAdapter;
import com.example.dnk_user.lop.ThongTinLop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class PhieuDangKyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements PhieuDangKyDAO, SharedPreferencesDAO {
    Context context;
    ArrayList<PhieuDangKy> phieuDangKIES;
    RecyclerView recyclerView;
    HocVien hocVien;
    public PhieuDangKyAdapter(Context context, ArrayList<PhieuDangKy> phieuDangKIES, RecyclerView recyclerView){
        this.context=context;
        this.phieuDangKIES=phieuDangKIES;
        this.recyclerView=recyclerView;
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
        FragmentPhieuDangKyDangCho.pDialog.show();
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
                        Toast.makeText(context,jsonOb.getString(config.ThongBao),Toast.LENGTH_SHORT).show();
                    }
                    PhieuDangKyAdapter adapter=new PhieuDangKyAdapter(context,phieuDangKIES,recyclerView);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapter);
                    FragmentPhieuDangKyDangCho.pDialog.dismiss();


                } catch (JSONException ex) {
                    FragmentPhieuDangKyDangCho.pDialog.dismiss();
                    Log.d("Thất bại","");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FragmentPhieuDangKyDangCho.pDialog.dismiss();
                Toast.makeText(context, "Kết nối thất bại", Toast.LENGTH_SHORT).show();

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
        Volley.newRequestQueue(context).add(stringRequest);
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView anhminhoa;
        TextView tenlop;
        TextView batdau;
        TextView ketthuc;
        TextView trangthai;
        TextView cahoc;
        TextView huy;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            anhminhoa=itemView.findViewById(R.id.ivAnhMinhHoa_PDK);
            tenlop=itemView.findViewById(R.id.tvTenLop_PDK);
            batdau=itemView.findViewById(R.id.tvBatDau_PDK);
            ketthuc=itemView.findViewById(R.id.tvKetThuc_PDK);
            trangthai=itemView.findViewById(R.id.tvTrangThai_PDK);
            cahoc=itemView.findViewById(R.id.tvCaHoc_PDK);
            huy=itemView.findViewById(R.id.tvHuy_PDK);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.phieu_dang_ky,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PhieuDangKy phieuDangKy=phieuDangKIES.get(position);
        Lop lop=new Lop();
        StringRequest stringRequestLop=new StringRequest(Request.Method.POST, config.URL_Lop_Get, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jsonGetLopTheoKH",response.toString());
                try {
                    JSONObject jsonOb=new JSONObject(response.trim());
                    if(jsonOb.getInt(config.ThanhCong)==1){
                        JSONArray list = (JSONArray) jsonOb.getJSONArray(config.Table_Lop);
                        JSONObject jsonObject=(JSONObject)list.get(0);
                        lop.setMalop(jsonObject.getInt(config.Ma_Lop));
                        lop.setTenlop(jsonObject.getString(config.Ten_Lop));
                        lop.setMota(jsonObject.getString(config.Mota_Lop));
                        lop.setMakh(jsonObject.getInt(config.Ma_KH));
                        lop.setMagv(jsonObject.getInt(config.Ma_GV));
                        lop.setBatdau(jsonObject.getString(config.BD_Lop));
                        lop.setKetthuc(jsonObject.getString(config.KT_Lop));
                        lop.setAnhminhhoa(jsonObject.getString(config.Anh_Lop));
                        lop.setDanhgia(jsonObject.getDouble(config.DanhGia_Lop));
                        lop.setHocphi(jsonObject.getDouble(config.HocPhi_Lop));
                        lop.setCahoc(jsonObject.getString(config.CaHoc_Lop));
                        ((MyViewHolder)holder).tenlop.setText(lop.getTenlop());
                        Glide.with(context).asBitmap().load(config.URL_Lop_AnhMinhHoa+lop.getAnhminhhoa()).into(((MyViewHolder)holder).anhminhoa);
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
                params.put(config.Ma_Lop,phieuDangKy.getMalop()+"");
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequestLop);


        ((MyViewHolder)holder).batdau.setText(phieuDangKy.getBatdau());
        ((MyViewHolder)holder).ketthuc.setText(phieuDangKy.getKetthuc());
        ((MyViewHolder)holder).cahoc.setText(phieuDangKy.getCahoc());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String now=simpleDateFormat.format(calendar.getTime());
        if(now.compareTo(phieuDangKy.getBatdau())<0){
            ((MyViewHolder)holder).trangthai.setTextColor(Color.GRAY);
            ((MyViewHolder)holder).trangthai.setText("Chưa học");
        }
        else if(now.compareTo(phieuDangKy.getKetthuc())<0){
            ((MyViewHolder)holder).trangthai.setTextColor(Color.RED);
            ((MyViewHolder)holder).trangthai.setText("Đang học");
        }
        else {
            ((MyViewHolder)holder).trangthai.setTextColor(Color.GREEN);
            ((MyViewHolder)holder).trangthai.setText("Đã hoàn thành");
        }
        if(phieuDangKy.getTrangthai()==1){
            ((MyViewHolder)holder).huy.setVisibility(View.INVISIBLE);
        }
        else {
            ((MyViewHolder)holder).trangthai.setTextColor(Color.GRAY);
            ((MyViewHolder)holder).trangthai.setText("Đang chờ");
        }
        ((MyViewHolder)holder).huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                View view= LayoutInflater.from(context).inflate(R.layout.dialog_xac_nhan,null);
                TextView xacnhan=view.findViewById(R.id.tvXacNhan);
                xacnhan.setText("Bạn có muốn hủy đăng ký lớp học này?");
                Button yes=view.findViewById(R.id.yes);
                Button no=view.findViewById(R.id.no);
                builder.setView(view);
                builder.setCancelable(false);
                AlertDialog dialog=builder.create();
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringRequest stringRequest=new StringRequest(Request.Method.POST, config.URL_PhieuDK_Delete, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("jsonDeleteLop",response.toString());
                                try {
                                    JSONObject jsonObject = new JSONObject(response.trim());
                                    if (jsonObject.getInt(config.ThanhCong) == 1) {
                                        Toast.makeText(context, jsonObject.getString(config.ThongBao), Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(context, jsonObject.getString(config.ThongBao), Toast.LENGTH_LONG).show();
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
                                params.put(config.Ma_PhieuDK,phieuDangKy.getMapdk()+"");
                                return params;
                            }
                        };
                        Volley.newRequestQueue(context).add(stringRequest);
                        dialog.dismiss();
                        getAllDangChoTheoHV();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();




            }
        });
        ((MyViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenkh=((MainActivity)context).getIntent().getStringExtra(config.Ten_KH);
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
        return phieuDangKIES.size();
    }
}
