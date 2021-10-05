package com.example.dnk_user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dnk_user.dao.HocVienDAO;
import com.example.dnk_user.dao.SharedPreferencesDAO;
import com.example.dnk_user.hocvien.HocVien;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class LoginActivity extends AppCompatActivity implements SharedPreferencesDAO, HocVienDAO {
    EditText etTaiKhoan,etMatKhau;
    CheckBox checkBoxGhiNho;
    TextView tvDangNhap,tvDangKy;
    HocVien hocVien;
    public static ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etTaiKhoan=findViewById(R.id.etTaiKhoan);
        etMatKhau=findViewById(R.id.etMatKhau);
        checkBoxGhiNho=findViewById(R.id.checkboxGhiNho);
        tvDangNhap=findViewById(R.id.tvDangNhap);
        tvDangKy=findViewById(R.id.tvDangKy);
        handleSSLHandshake();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        hocVien=restore();
        checkBoxGhiNho.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(etTaiKhoan.getText().toString().equals("")||etMatKhau.getText().toString().equals("")){
                        Toast.makeText(LoginActivity.this,"Không được để trống",Toast.LENGTH_SHORT).show();
                        checkBoxGhiNho.setChecked(false);

                    }
                }
            }
        });
        tvDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etTaiKhoan.getText().toString().equals("")||etMatKhau.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"Không được để trống",Toast.LENGTH_SHORT).show();
                }
                else {
                    login();

                }
            }
        });
        tvDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                View view= LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_xac_nhan,null);
                TextView xacnhan=view.findViewById(R.id.tvXacNhan);
                xacnhan.setGravity(Gravity.LEFT);
                xacnhan.setTextSize(14);
                xacnhan.setText(getResources().getString(R.string.contact));
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
        });
    }


    @Override
    public void save() {
        SharedPreferences sharedPreferences=getSharedPreferences("HV_active.txt",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        boolean check=checkBoxGhiNho.isChecked();
        editor.putInt(config.Ma_HV,hocVien.getMahv());
        editor.putString(config.Ten_HV,hocVien.getTenhv());
        editor.putString(config.TaiKhoan_HV,hocVien.getTaikhoan());
        editor.putString(config.MatKhau_HV,hocVien.getMatkhau());
        editor.putString(config.Email_HV,hocVien.getEmail());
        editor.putString(config.SDT_HV,hocVien.getSdt());
        editor.putString(config.DiaChi_HV,hocVien.getDiachi());
        editor.putString(config.Avatar_HV,hocVien.getAvatar());
        editor.putInt(config.TrangThai_HV,hocVien.getTrangthai());
        editor.putBoolean("GhiNho",check);
        editor.commit();
    }

    @Override
    public HocVien restore() {
        SharedPreferences sharedPreferences=getSharedPreferences("HV_active.txt",MODE_PRIVATE);
        boolean check=sharedPreferences.getBoolean("GhiNho",false);
        if(check){
            hocVien=new HocVien();
            hocVien.setMahv(sharedPreferences.getInt(config.Ma_HV,-1));
            hocVien.setTenhv(sharedPreferences.getString(config.Ten_HV,""));
            hocVien.setTaikhoan(sharedPreferences.getString(config.TaiKhoan_HV,""));
            hocVien.setMatkhau(sharedPreferences.getString(config.MatKhau_HV,""));
            hocVien.setEmail(sharedPreferences.getString(config.Email_HV,""));
            hocVien.setSdt(sharedPreferences.getString(config.SDT_HV,""));
            hocVien.setDiachi(sharedPreferences.getString(config.DiaChi_HV,""));
            hocVien.setAvatar(sharedPreferences.getString(config.Avatar_HV,""));
            hocVien.setTrangthai(sharedPreferences.getInt(config.TrangThai_HV,-1));
            etTaiKhoan.setText(hocVien.getTaikhoan());
            etMatKhau.setText(hocVien.getMatkhau());
            checkBoxGhiNho.setChecked(check);
            login();
        }
        return hocVien;
    }

    @Override
    public void delete() {

    }

    @Override
    public void reset() {

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

    }

    @Override
    public void updateAnh() {

    }

    @Override
    public void login() {
        pDialog.show();
        hocVien=new HocVien();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, config.URL_HV_Login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jsonLogin",response.toString());
                try {
                    JSONObject jsonOb=new JSONObject(response.trim());
                    if(jsonOb.getInt(config.ThanhCong)==1){
                        JSONArray list = (JSONArray) jsonOb.getJSONArray(config.Table_HV);
                        JSONObject jsonObject=(JSONObject)list.get(0);
                        hocVien.setMahv(jsonObject.getInt(config.Ma_HV));
                        hocVien.setTenhv(jsonObject.getString(config.Ten_HV));
                        hocVien.setTaikhoan(jsonObject.getString(config.TaiKhoan_HV));
                        hocVien.setMatkhau(jsonObject.getString(config.MatKhau_HV));
                        hocVien.setEmail(jsonObject.getString(config.Email_HV));
                        hocVien.setSdt(jsonObject.getString(config.SDT_HV));
                        hocVien.setDiachi(jsonObject.getString(config.DiaChi_HV));
                        hocVien.setAvatar(jsonObject.getString(config.Avatar_HV));
                        hocVien.setTrangthai(jsonObject.getInt(config.TrangThai_HV));
                        if(hocVien.getTrangthai()==0){
                            pDialog.dismiss();
                            AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                            View view= LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_xac_nhan,null);
                            TextView xacnhan=view.findViewById(R.id.tvXacNhan);
                            xacnhan.setGravity(Gravity.LEFT);
                            xacnhan.setTextSize(14);
                            xacnhan.setText(getResources().getString(R.string.lock));
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
                        else {
                            save();
                            Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            etTaiKhoan.setText("");
                            etMatKhau.setText("");
                            checkBoxGhiNho.setChecked(false);
                        }
                    }
                    else {
                        pDialog.dismiss();
                        checkBoxGhiNho.setChecked(false);
                        Toast.makeText(LoginActivity.this,jsonOb.getString(config.ThongBao),Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException ex) {
                    Log.d("Thất bại","");
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,"Kết nối thất bại",Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }

        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(config.TaiKhoan_HV,etTaiKhoan.getText().toString());
                params.put(config.MatKhau_HV,etMatKhau.getText().toString());
                return params;
            }
        };
        Volley.newRequestQueue(LoginActivity.this).add(stringRequest);
    }
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
}