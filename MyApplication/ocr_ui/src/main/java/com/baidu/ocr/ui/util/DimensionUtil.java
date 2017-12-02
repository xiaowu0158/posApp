/*
 * Copyright (C) 20手段防范17 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ocr.ui.util;

import android.content.res.Resources;

public class DimensionUtil {

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}
