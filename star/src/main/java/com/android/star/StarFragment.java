package com.android.star;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.base.BaseFragment;
import com.android.star.vm.data.StarData;
import com.android.utils.DimenUtil;
import com.android.utils.StringUtil;
import com.android.widget.ZdStar;
import com.android.widget.ZdToast;
import com.android.widget.refresh.interfaces.RefreshLayout;
import com.android.widget.star.StarView;
import com.android.widget.star.adapter.StarAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * created by jiangshide on 2019-08-04.
 * email:18311271399@163.com
 */
public class StarFragment extends BaseFragment implements ZdStar.OnTagClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setView(R.layout.fragment_star, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ZdStar zdStar = view.findViewById(R.id.zdStar);

        zdStar.setAdapter(new StarAdapter<StarData>(getList()) {
            @Override
            protected View getView(Context context, int position, ViewGroup parent, StarData starData) {
                StarView starView = new StarView(context);
                starView.setSign(starData.getName());
                int starColor = position % 2 == 0 ? StarView.COLOR_FEMALE : StarView.COLOR_MALE;
                boolean hasShadow = false;

                String str = "";
                if (position % 12 == 0) {
                    str = "老铁";
                    starColor = StarView.COLOR_MOST_ACTIVE;
                } else if (position % 20 == 0) {
                    str = "好友";
                    starColor = StarView.COLOR_BEST_MATCH;
                } else if (position % 33 == 0) {
                    str = "潜力股";
                    starColor = StarView.COLOR_MOST_NEW;
                } else if (position % 18 == 0) {
                    hasShadow = true;
                } else {
                    str = "陌生人";
                }
                starView.setStarColor(starColor);
                starView.setHasShadow(hasShadow);
                starView.setMatch(position * 2 + "%", str);
                if (hasShadow) {
                    starView.setMatchColor(starColor);
                } else {
                    starView.setMatchColor(StarView.COLOR_MOST_ACTIVE);
                }

                int starWidth = DimenUtil.dp2px(context, 50.0f);
                int starHeight = DimenUtil.dp2px(context, 85.0f);
                int starPaddingTop = DimenUtil.dp2px(context, 20.0f);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(starWidth, starHeight);
                starView.setPadding(0, starPaddingTop, 0, 0);
                starView.setLayoutParams(layoutParams);
                return starView;
            }
        });
        zdStar.setOnTagClickListener(this);
    }

    private List<StarData> getList() {
        List<StarData> starData = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            starData.add(new StarData(StringUtil.getRandomNick(), i));
        }
        return starData;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
        cancelRefresh();
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, int position) {
        ZdToast.txt(String.valueOf(position));
    }
}
