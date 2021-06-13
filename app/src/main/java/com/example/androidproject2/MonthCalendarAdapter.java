package com.example.androidproject2;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MonthCalendarAdapter extends FragmentStateAdapter {
    private static int NUM_ITEMS=100;
    View rootView2;

    public MonthCalendarAdapter(@NonNull Fragment fragment) {
        super(fragment);
        System.out.println(("hhh"));

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        System.out.println(("iii"));



        System.out.println("position : " + position);


        int year = position; // 여러분이 수정 해야 함
        int month = position+1; // 여러분이 수정 해야 함



        return MonthCalendarFragment.newInstance(year, month);
    }

    @Override
    public int getItemCount()  {
        return NUM_ITEMS;
    }
}
