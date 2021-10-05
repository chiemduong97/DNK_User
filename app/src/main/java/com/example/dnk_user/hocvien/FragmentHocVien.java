package com.example.dnk_user.hocvien;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.dnk_user.MainActivity;
import com.example.dnk_user.R;
import com.example.dnk_user.config;
import com.example.dnk_user.dao.HocVienDAO;
import com.example.dnk_user.dao.SharedPreferencesDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class FragmentHocVien extends Fragment implements HocVienDAO, SharedPreferencesDAO {
    HocVien hocVien;
    ImageView ivAvatar,ivChange_avatar;
    TextView tvMahv,tvTaiKhoan,tvUpdatehv,tvTenhv,tvEmailhv,tvDiaChihv,tvSDThv,tvChange_matkhau,tvDangXuat,tvThoat;
    Bitmap bm;
    public static ProgressDialog pDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_hoc_vien,null);
        handleSSLHandshake();
        ivAvatar=view.findViewById(R.id.ivAvatar_HV);
        ivChange_avatar=view.findViewById(R.id.ivChange_avatar);
        tvMahv=view.findViewById(R.id.tvMa_HV);
        tvTaiKhoan=view.findViewById(R.id.tvTaiKhoan_HV);
        tvUpdatehv=view.findViewById(R.id.tvUpdate_HV);
        tvTenhv=view.findViewById(R.id.tvTen_HV);
        tvEmailhv=view.findViewById(R.id.tvEmail_HV);
        tvDiaChihv=view.findViewById(R.id.tvDiaChi_HV);
        tvSDThv=view.findViewById(R.id.tvSDT_HV);
        tvChange_matkhau=view.findViewById(R.id.tvChange_MatKhau);
        tvDangXuat=view.findViewById(R.id.tvDangXuat);
        tvThoat=view.findViewById(R.id.tvThoat);

        bm= BitmapFactory.decodeResource(getResources(),R.drawable.user);
        hocVien=restore();
        loadAvatar();
        tvMahv.setText("#"+hocVien.getMahv());
        tvTaiKhoan.setText(hocVien.getTaikhoan());
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        ivChange_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent take=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Intent pick=new Intent(Intent.ACTION_GET_CONTENT);
                pick.setType("image/*");
                Intent intent=Intent.createChooser(pick,"Chọn");
                intent.putExtra(Intent.EXTRA_INITIAL_INTENTS,new Intent[]{take});
                startActivityForResult(intent,999);
            }
        });
        tvUpdatehv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),ThongTinHocVien.class);
                startActivity(intent);
            }
        });
        tvChange_matkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),DoiMatKhauHocVien.class);
                startActivity(intent);
            }
        });
        tvDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                View view= LayoutInflater.from(getContext()).inflate(R.layout.dialog_xac_nhan,null);
                TextView xacnhan=view.findViewById(R.id.tvXacNhan);
                xacnhan.setText("Bạn có muốn đăng xuất tài khoản này không?");
                Button yes=view.findViewById(R.id.yes);
                Button no=view.findViewById(R.id.no);
                builder.setView(view);
                builder.setCancelable(false);
                AlertDialog dialog=builder.create();
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete();
                        dialog.dismiss();
                        getActivity().finish();
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
        tvThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                View view= LayoutInflater.from(getContext()).inflate(R.layout.dialog_xac_nhan,null);
                TextView xacnhan=view.findViewById(R.id.tvXacNhan);
                xacnhan.setText("Bạn có chắc chắn muốn thoát không?");
                Button yes=view.findViewById(R.id.yes);
                Button no=view.findViewById(R.id.no);
                builder.setView(view);
                builder.setCancelable(false);
                AlertDialog dialog=builder.create();
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        getActivity().finishAffinity();
                        System.exit(0);
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
        return view;
    }
    public void loadAvatar(){
        if(!hocVien.getAvatar().equalsIgnoreCase("null")){
            Glide.with(getContext()).asBitmap().load(config.URL_HV_Avatar+hocVien.getAvatar()).into(ivAvatar);
            Glide.with(getContext()).asBitmap().load(config.URL_HV_Avatar+hocVien.getAvatar()).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    bm=resource;
                    return false;
                }
            }).submit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.refresh.setVisibility(View.INVISIBLE);
        hocVien=restore();
        tvTenhv.setText("\t"+hocVien.getTenhv());
        tvEmailhv.setText("\t"+hocVien.getEmail());
        tvDiaChihv.setText("\t"+hocVien.getDiachi());
        tvSDThv.setText("\t"+hocVien.getSdt());
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.refresh.setVisibility(View.VISIBLE);

    }

    @Override
    public void getHocVien() {
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, config.URL_HV_Get, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jsonGetHocVien",response.toString());
                try {
                    JSONObject jsonOb=new JSONObject(response.trim());
                    if(jsonOb.getInt(config.ThanhCong)==1){
                        JSONArray list = (JSONArray) jsonOb.getJSONArray(config.Table_KH);
                        for(int i=0;i<list.length();i++){
                            JSONObject jsonObject=(JSONObject)list.get(i);
                            hocVien.setMahv(jsonObject.getInt(config.Ma_HV));
                            hocVien.setTenhv(jsonObject.getString(config.Ten_HV));
                            hocVien.setTaikhoan(jsonObject.getString(config.TaiKhoan_HV));
                            hocVien.setMatkhau(jsonObject.getString(config.MatKhau_HV));
                            hocVien.setEmail(jsonObject.getString(config.Email_HV));
                            hocVien.setSdt(jsonObject.getString(config.SDT_HV));
                            hocVien.setDiachi(jsonObject.getString(config.DiaChi_HV));
                            hocVien.setAvatar(jsonObject.getString(config.Avatar_HV));
                            hocVien.setTrangthai(jsonObject.getInt(config.TrangThai_HV));
                        }
                    }
                    else {
                        Toast.makeText(getContext(),jsonOb.getString(config.ThongBao),Toast.LENGTH_SHORT).show();
                    }
                    save();
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
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, config.URL_HV_UpdateAvatar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jsonUpdateHV",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.trim());
                    if (jsonObject.getInt(config.ThanhCong) == 1) {
                        hocVien.setAvatar(jsonObject.getString(config.Avatar_HV));
                        loadAvatar();
                        save();
                        Toast.makeText(getContext(), jsonObject.getString(config.ThongBao), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), jsonObject.getString(config.ThongBao), Toast.LENGTH_LONG).show();
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
                Toast.makeText(getContext(), "Kết nối thất bại", Toast.LENGTH_SHORT).show();
            }

        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(config.Ma_HV,hocVien.getMahv()+"");
                params.put(config.Avatar_HV,imageStore());
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    @Override
    public void login() {

    }

    @Override
    public void save() {
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("HV_active.txt",getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(config.Ma_HV,hocVien.getMahv());
        editor.putString(config.Ten_HV,hocVien.getTenhv());
        editor.putString(config.TaiKhoan_HV,hocVien.getTaikhoan());
        editor.putString(config.MatKhau_HV,hocVien.getMatkhau());
        editor.putString(config.Email_HV,hocVien.getEmail());
        editor.putString(config.SDT_HV,hocVien.getSdt());
        editor.putString(config.DiaChi_HV,hocVien.getDiachi());
        editor.putString(config.Avatar_HV,hocVien.getAvatar());
        editor.putInt(config.TrangThai_HV,hocVien.getTrangthai());
        editor.commit();
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
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("HV_active.txt",getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(config.Ma_HV,-1);
        editor.putString(config.Ten_HV,"");
        editor.putString(config.TaiKhoan_HV,"");
        editor.putString(config.MatKhau_HV,"");
        editor.putString(config.Email_HV,"");
        editor.putString(config.SDT_HV,"");
        editor.putInt(config.TrangThai_HV,-1);
        editor.putBoolean("GhiNho",false);
        editor.commit();
    }

    @Override
    public void reset() {

    }

    public String imageStore(){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        Log.d("bitmap",bm.toString());
        return android.util.Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==999){
            Log.d("OK","OK");
            try{
                if(data.getExtras()!=null){
                    Bundle bundle=data.getExtras();
                    Bitmap bitmap=(Bitmap)bundle.get("data");
                    ivAvatar.setImageBitmap(bitmap);
                    bm=bitmap;
                    updateAnh();
                    Log.d("bitmap",bitmap.toString());
                }
                else {
                    Uri uri=data.getData();
                    Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                    ivAvatar.setImageBitmap(bitmap);
                    bm=bitmap;
                    updateAnh();
                    Log.d("Uri",bitmap.toString());
                }
            }catch (Exception ex){

            }
        }
        else
            Log.d("FAIL","FAIL");

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
