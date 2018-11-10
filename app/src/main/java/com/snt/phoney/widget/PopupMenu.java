package com.snt.phoney.widget;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PopupMenu extends Dialog {

    protected PopupMenu(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private PopupMenu(Context context, int theme) {
        super(context, theme);
        init();
    }

    public PopupMenu(Context context) {
        super(context);
        init();
    }

    private int height = LayoutParams.WRAP_CONTENT;
    private int width = LayoutParams.WRAP_CONTENT;

    private DropMenuAdapter adapter;
    private ListView list;
    private int itemHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
    private int textColor = 0xff000000;
    private ArrayList<MenuItem> items = new ArrayList<MenuItem>();
    private OnMenuItemClickListener onMenuItemClickListener;

    private int position = 0;

    private void init() {
        adapter = new DropMenuAdapter();

        list = new ListView(getContext());

        LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        list.setLayoutParams(p);
        list.setBackgroundColor(0xffffffff);
        list.setFooterDividersEnabled(false);
        list.setHeaderDividersEnabled(false);
        list.setDivider(new ColorDrawable(0xff9f9f9f));
        list.setDividerHeight(1);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                PopupMenu.this.position = position;
                if (onMenuItemClickListener != null) {
                    onMenuItemClickListener.onOptionsItemSelected(((MenuItemView) view).getMenuItem());
                }
            }
        });

        this.getWindow().setBackgroundDrawable(new ColorDrawable(0xffffffff));
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(list);
    }

    /**
     * 设置选择效果
     *
     * @param selector
     */
    public void setItemSelector(int selector) {
        list.setSelector(selector);
    }

    /**
     * 设置背景
     *
     * @param resid
     */
    public void setBackgroundResource(int resid) {
        list.setBackgroundResource(resid);
    }

    /**
     * 设置背景
     *
     * @param color default white
     */
    public void setBackgroundColor(int color) {
        list.setBackgroundColor(color);
    }

    /**
     * 设置Divider
     *
     * @param drawable 设置颜色可使用如下方法：setDivider(new ColorDrawable(color))
     */
    public void setDivider(Drawable drawable) {
        list.setDivider(drawable);
        list.setDividerHeight(1);
    }

    /**
     * 设置Divider
     *
     * @param drawable      设置颜色可使用如下方法：setDivider(new ColorDrawable(color))
     * @param dividerHeight 高度
     */
    public void setDivider(Drawable drawable, int dividerHeight) {
        list.setDivider(drawable);
        list.setDividerHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerHeight, getContext().getResources().getDisplayMetrics()));
    }

    /**
     * 设置Divider颜色
     *
     * @param color
     */
    public void setDividerColor(int color) {
        setDivider(new ColorDrawable(color));
    }

    /**
     * 设置是否显示 scrollBar
     *
     * @param scrollBarEnabled
     */
    public void setScrollBarEnabled(boolean scrollBarEnabled) {
        list.setVerticalScrollBarEnabled(scrollBarEnabled);
    }

    /**
     * @param position
     */
    public void setPosition(int position) {
        // list.setSelection(position);
        this.position = position;
    }

    /**
     * 根据显示内容设置position
     *
     * @param title
     */
    public void setPosition(String title) {
        if (items != null) {
            int position = 0;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getTitle() != null && items.get(i).getTitle().equals(title)) {
                    position = i;
                    break;
                }
            }
            // list.setSelection(position);
            this.position = position;
        }
    }

    /**
     * add menu item 之前设置，否则不会生效
     *
     * @param color default black
     */
    public void setTextColor(int color) {
        this.textColor = color;
        list.invalidate();
    }

    /**
     * uint dip
     *
     * @param dip
     */
    public void setHeight(int dip) {
        this.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getContext().getResources().getDisplayMetrics());
    }

    /**
     * @param dip    单位是否采用 TypedValue.COMPLEX_UNIT_DIP
     * @param height 高度
     */
    public void setHeight(boolean dip, int height) {
        if (dip) {
            this.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getContext().getResources().getDisplayMetrics());
        } else {
            this.height = height;
        }
    }

    /**
     * uint dip
     *
     * @param dip
     */
    public void setWidth(int dip) {
        this.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getContext().getResources().getDisplayMetrics());
    }

    /**
     * @param dip   单位是否采用 TypedValue.COMPLEX_UNIT_DIP
     * @param width 宽度
     */
    public void setWidth(boolean dip, int width) {
        if (dip) {
            this.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getContext().getResources().getDisplayMetrics());
        } else {
            this.width = width;
        }
    }

    /**
     * @param height   每一行的高度,单位dip
     * @param showSize 显示多少行
     */
    public void setItemHeight(int height, int showSize) {
        this.itemHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getContext().getResources().getDisplayMetrics());
        this.height = showSize * itemHeight + showSize - 1;
    }

    /**
     * Display the content view in a popup window anchored to the bottom-left corner of the anchor view offset by the specified x and y coordinates. If there is
     * not enough room on screen to show the popup in its entirety, this method tries to find a parent scroll view to scroll. If no parent scroll view can be
     * scrolled, the bottom-left corner of the popup is pinned at the top left corner of the anchor view.
     * <p>
     * If the view later scrolls to move anchor to a different location, the popup will be moved correspondingly.
     */
    @Override
    public void show() {
        LayoutParams lp = list.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(width, height);
        } else {
            lp.width = width;
            lp.height = height;
        }
        // getWindow().setLayout(width, height);
        list.setLayoutParams(lp);
        adapter.notifyDataSetChanged();
        list.setSelection(position);
        super.show();
    }

    public class MenuItem {
        int position = -1;
        int iconResId = -1;
        String title = null;

        public MenuItem(int iconResId, String title) {
            this.iconResId = iconResId;
            this.title = title;
        }

        public MenuItem(int iconResId) {
            this.iconResId = iconResId;
        }

        public MenuItem(String title) {
            this.title = title;
        }

        protected void setPosition(int id) {
            this.position = id;
        }

        /**
         * @return 该item显示在第几行
         */
        public int getPosition() {
            return position;
        }

        /**
         * @return menu item icon resource id
         */
        public int getIconResourceId() {
            return iconResId;
        }

        /**
         * @return menu item title
         */
        public String getTitle() {
            return title;
        }

    }

    private class MenuItemView extends LinearLayout {

        MenuItem menuItem;
        ImageView iconView;
        TextView titleView;
        int height = LayoutParams.WRAP_CONTENT;

        public MenuItemView(Context context, MenuItem menuItem, int height) {
            super(context);
            this.height = height;
            this.menuItem = menuItem;
            this.setOrientation(LinearLayout.HORIZONTAL);
            if (menuItem.getIconResourceId() == -1) { // 只有title
                this.removeAllViews();
                this.addView(createTitleView(height));
                this.setTitle(menuItem.getTitle());
                this.setGravity(Gravity.CENTER);
            } else if (menuItem.getTitle() == null) { // 只有Icon
                this.removeAllViews();
                this.addView(createIconView());
                this.setIcon(menuItem.getIconResourceId());
            } else if (menuItem.getIconResourceId() != -1 && menuItem.getTitle() != null) {
                this.removeAllViews();
                this.addView(createIconView());
                this.addView(createTitleView(height));
                this.setIcon(menuItem.getIconResourceId());
                this.setTitle(menuItem.getTitle());
            }
        }

        private ImageView createIconView() {
            iconView = new ImageView(getContext());
            LayoutParams p = new LayoutParams(dp2px(30), dp2px(30));
            p.setMargins(dp2px(15), 0, 0, 0);
            p.gravity = Gravity.CENTER_VERTICAL;
            iconView.setLayoutParams(p);
            iconView.setScaleType(ScaleType.CENTER_INSIDE);
            return iconView;
        }

        private TextView createTitleView(int height) {
            titleView = new TextView(getContext());
            titleView.setTextColor(textColor);
            titleView.setGravity(Gravity.CENTER_VERTICAL);
            LayoutParams p1 = new LayoutParams(LayoutParams.MATCH_PARENT, height);
            p1.weight = 1;
            p1.gravity = Gravity.CENTER_VERTICAL;
            titleView.setLayoutParams(p1);
            if (height == LayoutParams.WRAP_CONTENT) {
                // p1.setMargins(dp2px(15), dp2px(5), dp2px(15), dp2px(5));
                titleView.setPadding(dp2px(15), dp2px(10), dp2px(15), dp2px(10));
            } else {
                // p1.setMargins(dp2px(15), 0, dp2px(15), 0);
                titleView.setPadding(dp2px(15), 0, dp2px(15), 0);
            }
            return titleView;
        }

        public void setMenuItem(MenuItem menuItem) {
            this.menuItem = menuItem;
            if (menuItem.getIconResourceId() == -1) { // 只有title
                this.removeAllViews();
                this.addView(titleView == null ? createTitleView(height) : titleView);
                this.setTitle(menuItem.getTitle());
                this.setGravity(Gravity.CENTER);
            } else if (menuItem.getTitle() == null) { // 只有Icon
                this.removeAllViews();
                this.addView(iconView == null ? createIconView() : iconView);
                this.setIcon(menuItem.getIconResourceId());
            } else if (menuItem.getIconResourceId() != -1 && menuItem.getTitle() != null) {
                this.removeAllViews();
                this.addView(titleView == null ? createTitleView(height) : titleView);
                this.addView(iconView == null ? createIconView() : iconView);
                this.setIcon(menuItem.getIconResourceId());
                this.setTitle(menuItem.getTitle());
            }
        }

        private final int dp2px(int dp) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
        }

        public void setGravity(int gravity) {
            if (titleView != null) {
                titleView.setGravity(gravity);
            }
        }

        public void setIcon(int resId) {
            if (iconView != null) {
                iconView.setImageResource(resId);
            }
        }

        public void setTitle(String title) {
            if (titleView != null) {
                titleView.setText(title);
            }
        }

        public TextView getTitleView() {
            return titleView;
        }

        public ImageView getIconView() {
            return iconView;
        }

        public MenuItem getMenuItem() {
            return menuItem;
        }
    }

    /**
     * 添加菜单项
     */
    public void add(int resId, String title) {
        MenuItem item = new MenuItem(resId, title);
        item.setPosition(items.size());
        items.add(item);
    }

    public void add(String title) {
        MenuItem item = new MenuItem(title);
        item.setPosition(items.size());
        items.add(item);
    }

    public void add(int resId) {
        MenuItem item = new MenuItem(resId);
        item.setPosition(items.size());
        items.add(item);
    }

    /**
     * 设置菜单监听器
     *
     * @param onMenuItemClickListener
     */
    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public static interface OnMenuItemClickListener {
        public void onOptionsItemSelected(MenuItem item);
    }

    private class DropMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new MenuItemView(getContext(), items.get(position), itemHeight);
            } else {
                ((MenuItemView) convertView).setMenuItem(items.get(position));
            }
            return convertView;
        }
    }
}
