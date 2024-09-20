package com.example.btl_android.Activity.admin.ThongKe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ThongKeViewAdapter extends FragmentStatePagerAdapter {
    public ThongKeViewAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Thongke1Fragment();
            case 1:
                return new Thongke2Fragment();
            case 2:
                return new Thongke3Fragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Thống kê 1";
                break;
            case 1:
                title = "Thống kê 2";
                break;
            case 2:
                title = "Thống kê 3";
                break;
        }
        return title;
    }
}
