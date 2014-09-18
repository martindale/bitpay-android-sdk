package com.bitpay.androidsdk.android.interfaces;

import com.bitpay.androidsdk.controller.BitPayException;

/**
 * Created by eordano on 9/11/14.
 */
public interface PromiseCallback<D> {
    public void onSuccess(D promised);
    public void onError(BitPayException e);
}
