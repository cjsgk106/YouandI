package com.example.andorid.youandi.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andorid.youandi.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class ChatFragment extends Fragment {

    TabLayout tabLayout;
    TabItem tabItemFriend;
    TabItem tabItemChat;
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.fragment_chat_tablayout);
        tabItemFriend = (TabItem) view.findViewById(R.id.fragment_chat_tabitem_friendlist);
        tabItemChat = (TabItem) view.findViewById(R.id.fragment_chat_tabitem_chatlist);
        viewPager = (ViewPager) view.findViewById(R.id.fragment_chat_viewpager);

        ChatPageAdapter chatPageAdapter = new ChatPageAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(chatPageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return view;
    }

}