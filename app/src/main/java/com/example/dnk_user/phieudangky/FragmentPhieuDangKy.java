package com.example.dnk_user.phieudangky;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dnk_user.MainActivity;
import com.example.dnk_user.R;

public class FragmentPhieuDangKy extends Fragment {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    TextView tvDaXacNhan,tvDangCho;
    int flag=1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_phieu_dang_ky,null);
        tvDaXacNhan=view.findViewById(R.id.tvDaXacNhan_PDK);
        tvDangCho=view.findViewById(R.id.tvDangCho_PDK);

        fragmentManager=getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutPDK,new FragmentPhieuDangKyDaXacNhan()).commit();

        tvDaXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag!=1){
                    fragmentManager=getFragmentManager();
                    fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayoutPDK,new FragmentPhieuDangKyDaXacNhan()).commit();
                    tvDaXacNhan.setBackgroundColor(Color.RED);
                    tvDangCho.setBackgroundColor(Color.GRAY);

                    flag=1;
                }
            }
        });
        tvDangCho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag!=0){
                    fragmentManager=getFragmentManager();
                    fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayoutPDK,new FragmentPhieuDangKyDangCho()).commit();
                    tvDangCho.setBackgroundColor(Color.RED);
                    tvDaXacNhan.setBackgroundColor(Color.GRAY);
                    flag=0;
                }
            }
        });


        return view;
    }
}
