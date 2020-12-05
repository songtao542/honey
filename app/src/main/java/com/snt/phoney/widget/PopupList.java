package com.snt.phoney.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class PopupList {

    private final Context context;
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
    private View maskView;
    private FrameLayout root;
    private int itemHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
    private ColorStateList textColor = ColorStateList.valueOf(0xff000000);
    private int textSize = 15;
    @SuppressLint("RtlHardcoded")
    private int textGravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
    private final ArrayList<MenuItem> menuItems = new ArrayList<>();
    private OnMenuItemClickListener onMenuItemClickListener;

    private int checkedPosition = -1;
    private String checkedTitle = null;
    private boolean showMask = false;

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

        root = new FrameLayout(context);
        LayoutParams pr = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        root.setLayoutParams(pr);

        maskView = new View(context);
        LayoutParams pb = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        maskView.setLayoutParams(pb);

        maskView.setBackgroundColor(0x9f333333);

        root.addView(maskView);

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
            this.checkedPosition = position;
            MenuItem menuItem = ((MenuItemLayout) view).getMenuItem();
            menuItem.setChecked(true);
            for (MenuItem item : menuItems) {
                if (menuItem == item) {
                    continue;
                }
                item.setChecked(false);
            }
            adapter.notifyDataSetChanged();
            if (onMenuItemClickListener != null) {
                onMenuItemClickListener.onOptionsItemSelected(menuItem);
            }
        });

        root.addView(list);
    }

    /**
     * 设置选择效果
     *
     * @param selector the selector
     */
    public void setItemSelector(int selector) {
        list.setSelector(selector);
    }

    /**
     * 设置选择效果
     *
     * @param textColor the text color
     */
    public void setItemTextColor(int textColor) {
        this.textColor = AppCompatResources.getColorStateList(context, textColor);
    }

    /**
     * 设置背景
     *
     * @param resId the resource id
     */
    public void setBackgroundResource(int resId) {
        list.setBackgroundResource(resId);
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
        list.setDividerHeight(dp2px(dividerHeight));
    }

    /**
     * 设置Divider颜色
     *
     * @param color the divider color
     */
    public void setDividerColor(int color) {
        setDivider(new ColorDrawable(color));
    }

    /**
     * 设置是否显示 scrollBar
     *
     * @param scrollBarEnabled scroll bar enabled
     */
    public void setScrollBarEnabled(boolean scrollBarEnabled) {
        list.setVerticalScrollBarEnabled(scrollBarEnabled);
    }

    /**
     * @param position the checked position
     */
    public void setCheckedPosition(int position) {
        this.checkedPosition = position;
        setupMenuCheck();
    }

    /**
     * 根据显示内容设置position
     *
     * @param title the checked title
     */
    public void setCheckedPosition(String title) {
        this.checkedTitle = title;
        setupMenuCheck();
    }

    /**
     * 添加菜单项
     */
    public void setMenu(List<MenuItem> menuItems) {
        if (menuItems == null || menuItems.isEmpty()) {
            return;
        }
        this.menuItems.clear();
        this.menuItems.addAll(menuItems);
        setupMenuCheck();
    }

    private void setupMenuCheck() {
        if (menuItems == null) {
            return;
        }
        int position = -1;
        if (checkedPosition != -1 && checkedPosition < menuItems.size()) {
            position = checkedPosition;
        } else if (checkedTitle != null) {
            for (int i = 0; i < menuItems.size(); i++) {
                if (checkedTitle.equals(menuItems.get(i).title)) {
                    position = i;
                    break;
                }
            }
        }
        if (position != -1) {
            for (int i = 0; i < menuItems.size(); i++) {
                MenuItem item = menuItems.get(i);
                item.setChecked(i == position);
            }
        } else {
            // 确保只有一个被选中
            MenuItem checkedItem = null;
            for (int i = 0; i < menuItems.size(); i++) {
                MenuItem item = menuItems.get(i);
                if (checkedItem == null && item.isChecked()) {
                    checkedItem = item;
                } else {
                    item.setChecked(false);
                }
            }
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
     * add menu item 之前设置，否则不会生效
     *
     * @param color default black
     */
    public void setTextColor(int color) {
        this.textColor = ColorStateList.valueOf(color);
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * @param gravity Gravity.CENTER,Gravity.CENTER_HORIZONTAL,Gravity.LEFT,Gravity.RIGHT
     */
    @SuppressLint("RtlHardcoded")
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
     * @param dip the unit is dip
     */
    public void setHeight(int dip) {
        this.height = dp2px(dip);
    }

    /**
     * @param dip    单位是否采用 TypedValue.COMPLEX_UNIT_DIP
     * @param height 高度
     */
    public void setHeight(boolean dip, int height) {
        if (dip) {
            this.height = dp2px(height);
        } else {
            this.height = height;
        }
    }

    public void setFullScreenWidth() {
        setWidth(false, context.getResources().getDisplayMetrics().widthPixels);
    }

    /**
     * @param dip the unit is dip
     */
    public void setWidth(int dip) {
        this.width = dp2px(dip);
        this.widthMode = 0;
    }

    /**
     * @param dip   单位是否采用 TypedValue.COMPLEX_UNIT_DIP
     * @param width 宽度
     */
    public void setWidth(boolean dip, int width) {
        if (dip) {
            this.width = dp2px(width);
        } else {
            this.width = width;
        }
        this.widthMode = 0;
    }

    public void setShowMask(boolean showMask) {
        this.showMask = showMask;
    }

    /**
     * @param height   单位dp， 每一行的高度
     * @param showSize 显示多少行
     */
    public void setItemHeight(int height, int showSize) {
        this.itemHeight = dp2px(height);
        this.height = showSize * itemHeight + 5;
    }

    private void createPopupWindow() {
        if (widthMode == LayoutParams.WRAP_CONTENT) {
            TextPaint paint = new TextPaint();
            paint.setTextSize(sp2px(textSize));
            for (MenuItem menuItem : menuItems) {
                int twidth = dp2px(60);
                if (menuItem.getTitle() != null) {
                    twidth += paint.measureText(menuItem.getTitle());
                }
                if (twidth > width) {
                    width = twidth;
                }
            }
        }

        popupWindow = new PopupWindow(root, width, height, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00ffffff));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(0);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOnDismissListener(() -> {
            if (onDismissListener != null) {
                onDismissListener.onDismiss();
            }
        });

        if (!showMask) {
            maskView.setVisibility(View.GONE);
        }

        maskView.setOnClickListener(v -> popupWindow.dismiss());
    }

    /**
     * @param anchor  the view on which to pin the DripDownListView
     * @param gravity {@link Gravity} just Gravity.LEFT,Gravity.RIGHT has effect ,other will be as Gravity.CENTER;
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
    @SuppressLint("RtlHardcoded")
    public void show(View anchor, int gravity, int yoff) {
        adapter.notifyDataSetChanged();
        list.setSelection(checkedPosition);
        float xx = anchor.getWidth();
        if (popupWindow == null) {
            createPopupWindow();
        }
        yoff = dp2px(yoff);
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
        list.setSelection(checkedPosition);
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
     * @param parent  a parent view to get the {@link android.view.View#getWindowToken()} token from
     * @param gravity the gravity which controls the placement of the popup window
     * @param x       the popup's x location offset
     * @param y       the popup's y location offset
     */
    public void showAtLocation(View parent, int gravity, int x, int y) {
        adapter.notifyDataSetChanged();
        list.setSelection(checkedPosition);
        if (popupWindow == null) {
            createPopupWindow();
        }
        popupWindow.showAtLocation(parent, gravity, x, y);
    }

    public static class MenuItem {
        private int position = 0;
        private int iconResId = 0;
        private int tailIconResId = 0;
        private boolean checked = false;
        private String title = null;

        public MenuItem(int iconResId, String title) {
            this.iconResId = iconResId;
            this.title = title;
        }

        public MenuItem(int iconResId, String title, int tailIconResId) {
            this.iconResId = iconResId;
            this.title = title;
            this.tailIconResId = tailIconResId;
        }

        public MenuItem(int iconResId) {
            this.iconResId = iconResId;
        }

        public MenuItem(String title) {
            this.title = title;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public int getPosition() {
            return position;
        }

        /**
         * @return menu item title
         */
        public String getTitle() {
            return title;
        }

        protected void setPosition(int id) {
            this.position = id;
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    private class MenuItemLayout extends LinearLayout {

        private final MenuItemView mMenuItemView;

        public MenuItemLayout(Context context, MenuItem menuItem, int height) {
            super(context);
            setOrientation(VERTICAL);
            setGravity(Gravity.CENTER);
            mMenuItemView = new MenuItemView(getContext(), menuItem, height);
            addView(mMenuItemView, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        }

        public MenuItem getMenuItem() {
            return mMenuItemView.getMenuItem();
        }

        public void setMenuItem(MenuItem menuItem) {
            mMenuItemView.setMenuItem(menuItem);
        }
    }

    private class MenuItemView extends LinearLayout {
        private MenuItem menuItem;
        private ImageView iconView;
        private ImageView tailIconView;
        private TextView titleView;
        private final int height;

        public MenuItemView(Context context, MenuItem menuItem, int height) {
            super(context);
            this.height = height;
            this.menuItem = menuItem;
            this.setOrientation(LinearLayout.HORIZONTAL);
            this.removeAllViews();
            if (menuItem.iconResId == 0) { // 只有title
                addTitleView(height, menuItem.getTitle(), 25);
                this.setGravity(Gravity.CENTER_VERTICAL);
            } else if (menuItem.getTitle() == null) { // 只有Icon
                addIconView(menuItem.iconResId);
            } else if (menuItem.iconResId != 0 && menuItem.getTitle() != null) {
                addIconView(menuItem.iconResId);
                addTitleView(height, menuItem.getTitle(), 0);
            }
            if (menuItem.tailIconResId != 0) {
                addTailIconView(menuItem.tailIconResId);
            }
            if (iconView != null) {
                iconView.setSelected(menuItem.isChecked());
            }
            if (titleView != null) {
                titleView.setSelected(menuItem.isChecked());
            }
            if (tailIconView != null) {
                tailIconView.setSelected(menuItem.isChecked());
            }
        }

        private void addIconView(int resId) {
            if (iconView == null) {
                iconView = new ImageView(context);
                LayoutParams p = new LayoutParams(dp2px(30), dp2px(30));
                p.setMargins(dp2px(8), 0, 0, 0);
                p.gravity = Gravity.CENTER_VERTICAL;
                iconView.setLayoutParams(p);
                iconView.setScaleType(ScaleType.CENTER_INSIDE);
            }
            addView(iconView);
            iconView.setImageResource(resId);
        }

        private void addTailIconView(int resId) {
            if (tailIconView == null) {
                tailIconView = new ImageView(context);
                LayoutParams p = new LayoutParams(dp2px(30), dp2px(30));
                p.setMargins(dp2px(8), 0, dp2px(8), 0);
                p.gravity = Gravity.CENTER_VERTICAL;
                tailIconView.setLayoutParams(p);
                tailIconView.setScaleType(ScaleType.CENTER_INSIDE);
            }
            addView(tailIconView);
            tailIconView.setImageResource(resId);
        }

        @SuppressLint("RtlHardcoded")
        private void addTitleView(int height, String text, int paddingLeft) {
            if (titleView == null) {
                paddingLeft = dp2px(paddingLeft);
                titleView = new TextView(context);
                titleView.setMinHeight(dp2px(42));
                titleView.setTextColor(textColor);
                titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                titleView.setGravity(textGravity);
                LayoutParams p1 = new LayoutParams(LayoutParams.WRAP_CONTENT, height);
                p1.gravity = Gravity.CENTER_VERTICAL;
                if (menuItem.tailIconResId == 0) {
                    p1.weight = 1;
                }
                titleView.setLayoutParams(p1);
                if (textGravity == (Gravity.CENTER_VERTICAL | Gravity.LEFT)) {
                    titleView.setPadding(paddingLeft == 0 ? dp2px(8) : paddingLeft, 0, dp2px(8), 0);
                }
            }
            addView(titleView);
            titleView.setText(text);
        }

        public void setMenuItem(MenuItem menuItem) {
            this.menuItem = menuItem;
            this.removeAllViews();
            if (menuItem.iconResId == 0) { // 只有title
                addTitleView(height, menuItem.getTitle(), 25);
            } else if (menuItem.getTitle() == null) { // 只有Icon
                addIconView(menuItem.iconResId);
            } else if (menuItem.iconResId != 0 && menuItem.getTitle() != null) {
                addIconView(menuItem.iconResId);
                addTitleView(height, menuItem.getTitle(), 0);
            }
            if (menuItem.tailIconResId != 0) {
                addTailIconView(menuItem.tailIconResId);
            }
            if (iconView != null) {
                iconView.setSelected(menuItem.isChecked());
            }
            if (titleView != null) {
                titleView.setSelected(menuItem.isChecked());
            }
            if (tailIconView != null) {
                tailIconView.setSelected(menuItem.isChecked());
            }
        }

        public MenuItem getMenuItem() {
            return menuItem;
        }
    }

    /**
     * 设置菜单监听器
     *
     * @param onMenuItemClickListener 菜单监听器
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
            MenuItem menuItem = menuItems.get(position);
            if (convertView == null) {
                convertView = new MenuItemLayout(context, menuItem, itemHeight);
                if (parent.getWidth() > 0) {
                    convertView.setLayoutParams(new ListView.LayoutParams(parent.getWidth(), itemHeight));
                } else {
                    parent.getViewTreeObserver().addOnGlobalLayoutListener(new ListViewLayoutListener(parent, convertView, itemHeight));
                }
            } else {
                ((MenuItemLayout) convertView).setMenuItem(menuItem);
            }
            return convertView;
        }


        class ListViewLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

            private final View mView;
            private final View mParent;
            private final int mHeight;

            public ListViewLayoutListener(View parent, View view, int height) {
                mParent = parent;
                mView = view;
                mHeight = height;
            }

            @Override
            public void onGlobalLayout() {
                mView.setLayoutParams(new ListView.LayoutParams(mParent.getWidth(), mHeight));
                mParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    }
}
