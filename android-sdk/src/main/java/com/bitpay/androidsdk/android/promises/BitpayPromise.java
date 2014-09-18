package com.bitpay.androidsdk.android.promises;

import com.bitpay.androidsdk.android.interfaces.PromiseCallback;

/**
 * Created by eordano on 9/11/14.
 */
public abstract class BitpayPromise<Promised> {
    public abstract void then(PromiseCallback<Promised> callback);
}
