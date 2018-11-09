package expandable.widget;

import android.view.View;
import android.view.ViewGroup;

class ExpandableManager {
    private final ViewGroup viewGroup;
    private ExpandableCollection expandableCollection = new ExpandableCollection();

    public ExpandableManager(ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
    }

    public void onViewAdded() {
        findExpansionsViews(viewGroup);
    }

    private void findExpansionsViews(View view) {
        if (view instanceof ExpandableNestedScrollView) {
            expandableCollection.add((ExpandableNestedScrollView) view);
        } else if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                findExpansionsViews(((ViewGroup) view).getChildAt(i));
            }
        }
    }

    public void setOpenOnlyOne(boolean openOnlyOne) {
        this.expandableCollection.openOnlyOne(openOnlyOne);
    }
}
