/*
 * Copyright (C) 20防范17 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ocr.ui.util;

import android.content.res.Resources;

public class DimensionUtil {

    public static int dpToPx(int dp) {
        System.out.println("测试");
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}
