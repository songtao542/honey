package expandable.widget;

import java.util.Collection;
import java.util.HashSet;

public class ExpandableCollection {

    private final Collection<ExpandableNestedScrollView> expansions = new HashSet<>();
    private boolean openOnlyOne = true;

    private final ExpandableNestedScrollView.IndicatorListener indicatorListener = new ExpandableNestedScrollView.IndicatorListener() {
        @Override
        public void onStartedExpand(ExpandableNestedScrollView expansionLayout, boolean willExpand) {
            if(willExpand && openOnlyOne){
                for (ExpandableNestedScrollView view : expansions) {
                    if(view != expansionLayout){
                        view.collapse(true);
                    }
                }
            }
        }
    };

    public ExpandableCollection add(ExpandableNestedScrollView expansionLayout) {
        expansions.add(expansionLayout);
        expansionLayout.addIndicatorListener(indicatorListener);
        return this;
    }

    public ExpandableCollection addAll(ExpandableNestedScrollView...expansionLayouts) {
        for (ExpandableNestedScrollView expansionLayout : expansionLayouts) {
            if (expansionLayout!=null){
                add(expansionLayout);
            }
        }
        return this;
    }

    public ExpandableCollection addAll(Collection<ExpandableNestedScrollView> expansionLayouts) {
        for (ExpandableNestedScrollView expansionLayout : expansionLayouts) {
            if (expansionLayout!=null){
                add(expansionLayout);
            }
        }
        return this;
    }

    public ExpandableCollection remove(ExpandableNestedScrollView expansionLayout) {
        if (expansionLayout != null) {
            expansions.remove(expansionLayout);
            expansionLayout.removeIndicatorListener(indicatorListener);
        }
        return this;
    }

    public ExpandableCollection openOnlyOne(boolean openOnlyOne){
        this.openOnlyOne = openOnlyOne;
        return this;
    }
}
