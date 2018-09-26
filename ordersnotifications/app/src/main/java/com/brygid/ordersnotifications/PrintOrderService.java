package com.brygid.ordersnotifications;

import android.accounts.Account;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.printer.job.PrintJob;
import com.clover.sdk.v1.printer.job.StaticOrderPrintJob;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderConnector;

/**
 * Created by joshparkes on 2018-02-01.
 */

public class PrintOrderService extends IntentService {
    private Account mAccount;
    private OrderConnector mOrderConnector;
    private final static String TAG = "PrintOrderService";

    public PrintOrderService() {
        super("PrintOrderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String orderId = intent.getStringExtra(MainActivity.EXTRA_PAYLOAD);
        if (mAccount == null) {
            mAccount = CloverAccount.getAccount(this);
            if (mAccount == null) {
                Log.d(TAG, "cannot get merchant account");
                return;
            }
        }
        connect();
        try {
            Order order = mOrderConnector.getOrder(orderId);
            if (order != null) {
                PrintJob pj = new StaticOrderPrintJob.Builder().order(order).build();
                pj.print(getApplicationContext(), mAccount);
            }
        } catch (Exception e) {
            Log.d(TAG, "cannot get order " + orderId);
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    private void connect() {
        disconnect();
        if (mAccount != null) {
            mOrderConnector = new OrderConnector(this, mAccount, null);
            mOrderConnector.connect();
        }
    }

    private void disconnect() {
        if (mOrderConnector != null) {
            mOrderConnector.disconnect();
            mOrderConnector = null;
        }
    }

}