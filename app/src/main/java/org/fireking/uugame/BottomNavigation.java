package org.fireking.uugame;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class BottomNavigation extends LinearLayout {

    private int position = -1;

    public BottomNavigation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(final List<NavigationItem> dataSets, final OnBottomNavigationListener onBottomNavigationListener) {
        removeAllViews();
        for (int i = 0; i < dataSets.size(); i++) {

            NavigationItem navigationItem = dataSets.get(i);

            View layout = LayoutInflater.from(getContext())
                    .inflate(R.layout.bottom_navigation_item, this, false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            ImageView ivTabberLogo = layout.findViewById(R.id.ivTabberLogo);
            TextView tvTabberName = layout.findViewById(R.id.tvTabberName);

            final int finalI = i;
            layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (position != -1) {
                        ImageView ivTabberLogo = getChildAt(position).findViewById(R.id.ivTabberLogo);
                        ivTabberLogo.setImageResource(dataSets.get(position).localImageId);
                    }
                    ImageView ivTabberLogo = v.findViewById(R.id.ivTabberLogo);
                    ivTabberLogo.setImageResource(dataSets.get(finalI).localImageCheckedId);

                    if (onBottomNavigationListener != null) {
                        onBottomNavigationListener.onBottomNavigation(finalI);
                    }

                    position = finalI;
                }
            });

            ivTabberLogo.setImageResource(navigationItem.localImageId);
            tvTabberName.setText(navigationItem.localItemName);

            params.weight = 1;
            addView(layout, params);
        }
    }

    /**
     * 点击指定item
     */
    public void performClick(int position) {
        if (position < getChildCount()) {
            getChildAt(position).performClick();
        }
    }

    public interface OnBottomNavigationListener {

        void onBottomNavigation(int position);
    }

    public static class NavigationItem {

        public int localImageId;
        public int localImageCheckedId;
        public String localItemName;
        public boolean isChecked;

        public NavigationItem(int localImageId, int localImageCheckedId, String localItemName) {
            this.localImageId = localImageId;
            this.localImageCheckedId = localImageCheckedId;
            this.localItemName = localItemName;
        }
    }
}
