package com.example.dnk_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dnk_user.dao.SharedPreferencesDAO;
import com.example.dnk_user.hocvien.FragmentHocVien;
import com.example.dnk_user.hocvien.HocVien;
import com.example.dnk_user.loaikhoahoc.FragmentLoaiKhoaHoc;
import com.example.dnk_user.phieudangky.FragmentPhieuDangKy;
import com.example.dnk_user.phieudanhgia.FragmentPhieuDanhGia;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements SharedPreferencesDAO {
    public static Toolbar toolbar;
    public static ImageView refresh;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        refresh=findViewById(R.id.ivRefresh);
        bottomNavigationView=findViewById(R.id.naviga);
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,new FragmentLoaiKhoaHoc()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.menu_home){
                    fragmentManager=getSupportFragmentManager();
                    fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout,new FragmentLoaiKhoaHoc()).commit();
                }
                else if(item.getItemId()==R.id.menu_phieu){
                    fragmentManager=getSupportFragmentManager();
                    fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout,new FragmentPhieuDangKy()).commit();
                }
                else if(item.getItemId()==R.id.menu_congdong){
                    fragmentManager=getSupportFragmentManager();
                    fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout,new FragmentPhieuDanhGia()).commit();
                }
                else if(item.getItemId()==R.id.menu_person){
                    fragmentManager=getSupportFragmentManager();
                    fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout,new FragmentHocVien()).commit();
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_xac_nhan,null);
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
                finish();
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
    public void save() {

    }

    @Override
    public HocVien restore() {
        return null;
    }

    @Override
    public void delete() {
        SharedPreferences sharedPreferences=getSharedPreferences("HV_active.txt",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(config.Ma_HV,-1);
        editor.putString(config.Ten_HV,"");
        editor.putString(config.TaiKhoan_HV,"");
        editor.putString(config.MatKhau_HV,"");
        editor.putString(config.Email_HV,"");
        editor.putString(config.DiaChi_HV,"");
        editor.putString(config.Avatar_HV,"");
        editor.putString(config.SDT_HV,"");
        editor.putInt(config.TrangThai_HV,-1);
        editor.putBoolean("GhiNho",false);
        editor.commit();
    }
}