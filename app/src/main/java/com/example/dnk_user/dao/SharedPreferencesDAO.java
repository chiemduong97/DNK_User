package com.example.dnk_user.dao;

import com.example.dnk_user.hocvien.HocVien;

public interface SharedPreferencesDAO {
    public void save();
    public HocVien restore();
    public void delete();
}
