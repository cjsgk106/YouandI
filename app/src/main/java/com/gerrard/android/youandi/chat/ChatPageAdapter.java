package com.gerrard.android.youandi.chat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ChatPageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    private String[] tabTitles = new String[]{"PARTNER", "CHATROOM"};

    public ChatPageAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FriendListFragment();
            case 1:
                return new ChatroomFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
