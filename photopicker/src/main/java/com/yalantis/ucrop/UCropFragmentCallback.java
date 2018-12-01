package com.yalantis.ucrop;

public interface UCropFragmentCallback {

    /**
     * Return loader status
     *
     * @param showLoader
     */
    @SuppressWarnings("JavaDoc")
    void loadingProgress(boolean showLoader);

    /**
     * Return cropping result or error
     *
     * @param result
     */
    @SuppressWarnings("JavaDoc")
    void onCropFinish(UCropFragment.UCropResult result);

}
