package com.bitpay.androidsdk.android.interfaces;

import com.bitpay.androidsdk.controller.BitPayException;
import com.bitpay.androidsdk.model.Invoice;

/**
 * Created by eordano on 9/11/14.
 */
public interface InvoiceCreationCallback {
    public void onSuccess(Invoice invoice);
    public void onError(BitPayException exception);
}
