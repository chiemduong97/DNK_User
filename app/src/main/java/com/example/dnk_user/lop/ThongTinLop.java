package com.example.dnk_user.lop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.dnk_user.R;
import com.example.dnk_user.config;
import com.example.dnk_user.dao.LopDAO;
import com.example.dnk_user.dao.PhieuDangKyDAO;
import com.example.dnk_user.dao.SharedPreferencesDAO;
import com.example.dnk_user.hocvien.HocVien;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class ThongTinLop extends AppCompatActivity implements LopDAO, SharedPreferencesDAO, PhieuDangKyDAO {
    EditText etTenLop,etMota,etHocPhi,etBatDau,etKetThuc,etGiangVien,etCaHoc;
    ImageView ivAnhMinhHoa,ivBack;
    Button btnDanhGia,btnDangKy;
    HocVien hocVien;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_lop);
        etTenLop=findViewById(R.id.etTenLop_TTLop);
        etMota=findViewById(R.id.etMoTa_TTLop);
        etHocPhi=findViewById(R.id.etHocPhi_TTLop);
        etBatDau=findViewById(R.id.etBatDau_TTLop);
        etKetThuc=findViewById(R.id.etKetThuc_TTLop);
        etGiangVien=findViewById(R.id.etGiangVien_TTLop);
        etCaHoc=findViewById(R.id.etCaHoc_TTLop);
        ivAnhMinhHoa=findViewById(R.id.ivAnhMinhHoa_TTLop);
        ivBack=findViewById(R.id.ivBack_TTLop);
        btnDangKy=findViewById(R.id.btnDangKy_TTLop);
        btnDanhGia=findViewById(R.id.btnDanhGia_TTLop);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etTenLop.setText(getIntent().getStringExtra(config.Ten_Lop));
        etMota.setText(getIntent().getStringExtra(config.Mota_Lop));
        etHocPhi.setText(getIntent().getDoubleExtra(config.HocPhi_Lop,-1.0)+"");
        etBatDau.setText(getIntent().getStringExtra(config.BD_Lop));
        etKetThuc.setText(getIntent().getStringExtra(config.KT_Lop));
        etCaHoc.setText(getIntent().getStringExtra(config.CaHoc_Lop));
        getGV();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);

        Glide.with(this).asBitmap().load(config.URL_Lop_AnhMinhHoa+getIntent().getStringExtra(config.Anh_Lop)).into(ivAnhMinhHoa);
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String now=simpleDateFormat.format(calendar.getTime());
                if(now.compareTo(getIntent().getStringExtra(config.KT_Lop))>0){
                    Toast.makeText(ThongTinLop.this,"Lớp học đã kết thúc, không thể đăng ký",Toast.LENGTH_SHORT).show();
                }
                else if(now.compareTo(getIntent().getStringExtra(config.BD_Lop))>=0){
                    Toast.makeText(ThongTinLop.this,"Lớp học đã bắt đầu, không thể đăng ký",Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder builder=new AlertDialog.Builder(ThongTinLop.this);
                    View view= LayoutInflater.from(ThongTinLop.this).inflate(R.layout.dialog_xac_nhan,null);
                    TextView xacnhan=view.findViewById(R.id.tvXacNhan);
                    xacnhan.setText("Bạn có muốn đăng ký lớp này không?");
                    Button yes=view.findViewById(R.id.yes);
                    Button no=view.findViewById(R.id.no);
                    builder.setView(view);
                    builder.setCancelable(false);
                    AlertDialog dialog=builder.create();
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            add();
                            dialog.dismiss();
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
            }
        });

        btnDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ThongTinLop.this, DanhGia.class);
                intent.putExtra(config.Ma_Lop,getIntent().getIntExtra(config.Ma_Lop,-1));
                intent.putExtra(config.Ten_Lop,getIntent().getStringExtra(config.Ten_Lop));
                startActivity(intent);
            }
        });
    }

    public void thongbao(){
        AlertDialog.Builder builder=new AlertDialog.Builder(ThongTinLop.this);
        View view= LayoutInflater.from(ThongTinLop.this).inflate(R.layout.dialog_xac_nhan,null);
        TextView xacnhan=view.findViewById(R.id.tvXacNhan);
        xacnhan.setGravity(Gravity.LEFT);
        xacnhan.setTextSize(14);
        xacnhan.setText(getResources().getString(R.string.regis_success));
        Button yes=view.findViewById(R.id.yes);
        Button no=view.findViewById(R.id.no);
        builder.setView(view);
        builder.setCancelable(false);
        AlertDialog dialog=builder.create();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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

    }

    @Override
    public void getAllTheoKH() {

    }

    @Override
    public void getAllTheoKHActive() {

    }

    @Override
    public void add() {
        hocVien=restore();
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, config.URL_PhieuDK_Add, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jsonAddPhieuDK",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.trim());
                    if (jsonObject.getInt(config.ThanhCong) == 1) {
                        thongbao();
                    }
                    else {
                        Toast.makeText(ThongTinLop.this, jsonObject.getString(config.ThongBao), Toast.LENGTH_LONG).show();
                    }
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(ThongTinLop.this, "Kết nối thất bại", Toast.LENGTH_SHORT).show();

            }

        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(config.Ma_HV,hocVien.getMahv()+"");
                params.put(config.Ma_Lop,getIntent().getIntExtra(config.Ma_Lop,-1)+"");
                params.put(config.TrangThai_PhieuDK,"0");
                params.put(config.BD_Lop,getIntent().getStringExtra(config.BD_Lop));
                params.put(config.KT_Lop,getIntent().getStringExtra(config.KT_Lop));
                params.put(config.CaHoc_Lop,getIntent().getStringExtra(config.CaHoc_Lop));
                return params;
            }
        };
        Volley.newRequestQueue(ThongTinLop.this).add(stringRequest);
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
    public void getGV() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, config.URL_GV_Get, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jsonGetGV",response.toString());
                try {
                    JSONObject jsonOb=new JSONObject(response.trim());
                    if(jsonOb.getInt(config.ThanhCong)==1){
                        JSONArray list = (JSONArray) jsonOb.getJSONArray(config.Table_GV);
                        for(int i=0;i<list.length();i++){
                            JSONObject jsonObject=(JSONObject)list.get(i);
                            etGiangVien.setText(jsonObject.getString(config.Ten_GV));
                        }
                    }
                    else {
                        Toast.makeText(ThongTinLop.this,jsonOb.getString(config.ThongBao),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                params.put(config.Ma_GV,getIntent().getIntExtra(config.Ma_GV,-1)+"");
                return params;
            }
        };
        Volley.newRequestQueue(ThongTinLop.this).add(stringRequest);
    }

    @Override
    public void getAllGV() {

    }

    @Override
    public void getDSLop() {

    }
}