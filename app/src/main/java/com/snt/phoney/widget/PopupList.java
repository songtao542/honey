package com.snt.phoney.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PopupList {

    private Context context;
    private PopupWindow popupWindow;

    private int height = LayoutParams.WRAP_CONTENT;
    private int width = LayoutParams.WRAP_CONTENT;

    /**
     * <ul>
     * <li>-2 == LayoutParams.WRAP_CONTENT;</li>
     * <li>-1 == LayoutParams.MATCH_PARENT;</li>
     * <li>0 == Absolutely dpi</li>
     * </ul>
     */
    private int widthMode = -2;

    private DropMenuAdapter adapter;
    private ListView list;
    private int itemHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
    private int textColor = 0xff000000;
    private int textSize = 15;
    private int textGravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
    private ArrayList<MenuItem> menuItems = new ArrayList<>();
    private OnMenuItemClickListener onMenuItemClickListener;

    private int position = 0;

    public PopupList(Context context) {
        super();
        this.context = context;
        init();
    }

    public PopupList(Context context, int arrayResId) {
        super();
        this.context = context;
        init();
        initItems(Arrays.asList(context.getResources().getStringArray(arrayResId)));
    }

    public PopupList(Context context, List<String> strings) {
        super();
        this.context = context;
        init();
        initItems(strings);
    }

    public void setList(List<String> strings) {
        initItems(strings);
    }

    public void clear() {
        if (menuItems != null) {
            menuItems.clear();
        }
    }

    private void initItems(List<String> items) {
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                add(items.get(i));
            }
        }
    }

    private void init() {
        adapter = new DropMenuAdapter();

        list = new ListView(context);
        LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        list.setLayoutParams(p);

        list.setBackgroundColor(0xffffffff);
        list.setFooterDividersEnabled(false);
        list.setHeaderDividersEnabled(false);
        list.setDivider(new ColorDrawable(0xffe6e6e6));
        list.setDividerHeight(1);

        list.setAdapter(adapter);

        list.setOnItemClickListener((parent, view, position, id) -> {
            popupWindow.dismiss();
            PopupList.this.position = position;
            if (onMenuItemClickListener != null) {
                onMenuItemClickListener.onOptionsItemSelected(((MenuItemView) view).getMenuItem());
            }
        });
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

    public interface OnDismissListener {
        void onDismiss();
    }

    private OnDismissListener onDismissListener;

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
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
        list.setDividerHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerHeight, context.getResources().getDisplayMetrics()));
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
        this.position = position;
    }

    /**
     * 根据显示内容设置position
     *
     * @param title
     */
    public void setPosition(String title) {
        if (menuItems != null) {
            int position = 0;
            for (int i = 0; i < menuItems.size(); i++) {
                if (menuItems.get(i).getTitle() != null && menuItems.get(i).getTitle().equals(title)) {
                    position = i;
                    break;
                }
            }
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
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * @param gravity Gravity.CENTER,Gravity.CENTER_HORIZONTAL,Gravity.LEFT,Gravity.RIGHT
     */
    public void setTextGravity(int gravity) {
        if (gravity == Gravity.CENTER) {
            textGravity = Gravity.CENTER;
        } else if (gravity == Gravity.CENTER_HORIZONTAL) {
            textGravity = Gravity.CENTER;
        } else if (gravity == Gravity.LEFT) {
            textGravity = Gravity.CENTER_HORIZONTAL | Gravity.LEFT;
        } else if (gravity == Gravity.RIGHT) {
            textGravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        }
    }

    /**
     * uint dip
     *
     * @param dip
     */
    public void setHeight(int dip) {
        this.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
    }

    /**
     * @param dip    单位是否采用 TypedValue.COMPLEX_UNIT_DIP
     * @param height 高度
     */
    public void setHeight(boolean dip, int height) {
        if (dip) {
            this.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics());
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
        this.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
        this.widthMode = 0;
    }

    /**
     * @param dip   单位是否采用 TypedValue.COMPLEX_UNIT_DIP
     * @param width 宽度
     */
    public void setWidth(boolean dip, int width) {
        if (dip) {
            this.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, context.getResources().getDisplayMetrics());
        } else {
            this.width = width;
        }
        this.widthMode = 0;
    }

    /**
     * @param height   单位dp， 每一行的高度
     * @param showSize 显示多少行
     */
    public void setItemHeight(int height, int showSize) {
        this.itemHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics());
        this.height = showSize * itemHeight + 5;
    }

    private void createPopupWindow() {
        if (widthMode == -2) {
            TextPaint paint = new TextPaint();
            paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, context.getResources().getDisplayMetrics()));
            for (MenuItem menuItem : menuItems) {
                int twidth = (int) dp2px(60);
                if (menuItem.getTitle() != null) {
                    twidth += paint.measureText(menuItem.getTitle());
                }
                if (twidth > width) {
                    width = twidth;
                }
            }
        }

        popupWindow = new PopupWindow(list, width, height, true);

        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00ffffff));
        popupWindow.setOutsideTouchable(true);
        //popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOnDismissListener(() -> {
            if (onDismissListener != null) {
                onDismissListener.onDismiss();
            }
        });
    }

    /**
     * @param anchor
     * @param gravity
     * @see PopupList#show(View anchor, int gravity, int yoff)
     */
    public void show(View anchor, int gravity) {
        show(anchor, gravity, 0);
    }

    /**
     * Display the content view in a popup window anchored to the bottom-left corner of the anchor view offset by the specified x and y coordinates. If there is
     * not enough room on screen to show the popup in its entirety, this method tries to find a parent scroll view to scroll. If no parent scroll view can be
     * scrolled, the bottom-left corner of the popup is pinned at the top left corner of the anchor view.
     * <p>
     * If the view later scrolls to move anchor to a different location, the popup will be moved correspondingly.
     *
     * @param anchor  the view on which to pin the DripDownListView
     * @param gravity {@link Gravity} just Gravity.LEFT,Gravity.RIGHT has effect ,other will be as Gravity.CENTER;
     * @param yoff    dip
     */
    public void show(View anchor, int gravity, int yoff) {
        adapter.notifyDataSetChanged();
        list.setSelection(position);
        float xx = anchor.getWidth();
        if (popupWindow == null) {
            createPopupWindow();
        }

        yoff = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, yoff, context.getResources().getDisplayMetrics());

        if (gravity == Gravity.LEFT) {
            popupWindow.showAsDropDown(anchor, 0, yoff);
        } else if (gravity == Gravity.RIGHT) {
            popupWindow.showAsDropDown(anchor, (int) (xx - width), yoff);
        } else {
            popupWindow.showAsDropDown(anchor, (int) (xx - width) / 2, yoff);
        }
    }

    /**
     * Display the content view in a popup window anchored to the bottom-left corner of the anchor view. If there is not enough room on screen to show the popup
     * in its entirety, this method tries to find a parent scroll view to scroll. If no parent scroll view can be scrolled, the bottom-left corner of the popup
     * is pinned at the top left corner of the anchor view.
     *
     * @param anchor the view on which to pin the DripDownListView
     */
    public void show(View anchor) {
        adapter.notifyDataSetChanged();
        list.setSelection(position);
        if (popupWindow == null) {
            createPopupWindow();
        }
        popupWindow.showAsDropDown(anchor);
    }

    /**
     * Display the content view in a popup window at the specified location. If the popup window cannot fit on screen, it will be clipped. See
     * android.view.WindowManager.LayoutParams for more information on how gravity and the x and y parameters are related. Specifying a gravity of
     * android.view.Gravity.NO_GRAVITY is similar to specifying Gravity.LEFT | Gravity.TOP.
     *
     * @param parent
     * @param gravity
     * @param x
     * @param y
     */
    public void showAtLocation(View parent, int gravity, int x, int y) {
        adapter.notifyDataSetChanged();
        list.setSelection(position);
        if (popupWindow == null) {
            createPopupWindow();
        }
        popupWindow.showAtLocation(parent, gravity, x, y);
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

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
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
                createAndAddTitleView(height, menuItem.getTitle(), 25);
                this.setGravity(Gravity.CENTER_VERTICAL);
            } else if (menuItem.getTitle() == null) { // 只有Icon
                this.removeAllViews();
                createAndAddIconView(menuItem.getIconResourceId());
            } else if (menuItem.getIconResourceId() != -1 && menuItem.getTitle() != null) {
                this.removeAllViews();
                createAndAddIconView(menuItem.getIconResourceId());
                createAndAddTitleView(height, menuItem.getTitle(), 0);
            }
        }

        private ImageView createAndAddIconView(int resId) {
            iconView = new ImageView(context);
            LayoutParams p = new LayoutParams(dp2px(30), dp2px(30));
            p.setMargins(dp2px(8), 0, 0, 0);
            p.gravity = Gravity.CENTER_VERTICAL;
            iconView.setLayoutParams(p);
            iconView.setScaleType(ScaleType.CENTER_INSIDE);
            addView(iconView);
            iconView.setImageResource(resId);
            return iconView;
        }

        private TextView createAndAddTitleView(int height, String text, int paddingLeft) {
            paddingLeft = dp2px(paddingLeft);
            titleView = new TextView(context);
            titleView.setMinHeight(dp2px(42));
            titleView.setTextColor(textColor);
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            titleView.setGravity(textGravity);
            LayoutParams p1 = new LayoutParams(LayoutParams.WRAP_CONTENT, height);
            p1.gravity = Gravity.CENTER_VERTICAL;
            p1.weight = 1;
            titleView.setLayoutParams(p1);
            if (textGravity == (Gravity.CENTER_VERTICAL | Gravity.LEFT)) {
                titleView.setPadding(paddingLeft == 0 ? dp2px(8) : paddingLeft, 0, dp2px(8), 0);
            }
            addView(titleView);
            titleView.setText(text);
            return titleView;
        }

        public void setMenuItem(MenuItem menuItem) {
            this.menuItem = menuItem;
            if (menuItem.getIconResourceId() == -1) { // 只有title
                this.removeAllViews();
                if (titleView == null) {
                    createAndAddTitleView(height, menuItem.getTitle(), 25);
                } else {
                    addView(titleView);
                    titleView.setText(menuItem.getTitle());
                }
                this.setGravity(Gravity.CENTER_VERTICAL);
            } else if (menuItem.getTitle() == null) { // 只有Icon
                this.removeAllViews();
                if (iconView == null) {
                    createAndAddIconView(menuItem.getIconResourceId());
                } else {
                    addView(iconView);
                    iconView.setImageResource(menuItem.getIconResourceId());
                }
            } else if (menuItem.getIconResourceId() != -1 && menuItem.getTitle() != null) {
                this.removeAllViews();
                if (iconView == null) {
                    createAndAddIconView(menuItem.getIconResourceId());
                } else {
                    addView(iconView);
                    iconView.setImageResource(menuItem.getIconResourceId());
                }
                if (titleView == null) {
                    createAndAddTitleView(height, menuItem.getTitle(), 0);
                } else {
                    addView(titleView);
                    titleView.setText(menuItem.getTitle());
                }
            }
        }

        public MenuItem getMenuItem() {
            return menuItem;
        }
    }

    /**
     * 添加菜单项
     */
    public void add(int resId, String title) {
        MenuItem menuItem = new MenuItem(resId, title);
        menuItem.setPosition(menuItems.size());
        menuItems.add(menuItem);
    }

    public void add(String title) {
        MenuItem menuItem = new MenuItem(title);
        menuItem.setPosition(menuItems.size());
        menuItems.add(menuItem);
    }

    public void add(int resId) {
        MenuItem menuItem = new MenuItem(resId);
        menuItem.setPosition(menuItems.size());
        menuItems.add(menuItem);
    }

    /**
     * 设置菜单监听器
     *
     * @param onMenuItemClickListener
     */
    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public interface OnMenuItemClickListener {
        void onOptionsItemSelected(MenuItem item);
    }

    private class DropMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return menuItems.size();
        }

        @Override
        public Object getItem(int position) {
            return menuItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new MenuItemView(context, menuItems.get(position), itemHeight);
            } else {
                ((MenuItemView) convertView).setMenuItem(menuItems.get(position));
            }

            return convertView;
        }
    }
}
