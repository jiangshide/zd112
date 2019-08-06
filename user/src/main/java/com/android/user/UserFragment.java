package com.android.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.base.BaseFragment;
import com.android.widget.ZdTabLayout;
import com.android.widget.ZdViewPager;
import com.android.widget.refresh.interfaces.RefreshLayout;

/**
 * created by jiangshide on 2019-07-24.
 * email:18311271399@163.com
 */
public class UserFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_user, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ZdTabLayout tabTitle = view.findViewById(R.id.tabTitle);
        ZdViewPager tabView = view.findViewById(R.id.tabView);
        tabView.setAdapter(tabView.create(getChildFragmentManager())
            .setTitles(
                    getString(R.string.me),
                    getString(R.string.app_settings)
            ).setFragment(
                ).initTabs(getActivity(),tabTitle,tabView)
                .setLinePagerIndicator(getColor(R.color.colorAccent))
        );
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
        cancelRefresh();
    }
}
