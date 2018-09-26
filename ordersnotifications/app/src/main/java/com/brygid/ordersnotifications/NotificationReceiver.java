package com.brygid.ordersnotifications;

import android.content.Context;
import android.content.Intent;

import com.clover.sdk.v1.app.AppNotification;
import com.clover.sdk.v1.app.AppNotificationReceiver;

/**
 * Created by joshparkes on 2018-02-01.
 */
public class NotificationReceiver extends AppNotificationReceiver {
    public final static String NEW_ORDER = "new_order_received";

    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, AppNotification notification) {
        if (notification.appEvent.equals(NEW_ORDER)) {
            Intent intent = new Intent(context, PrintOrderService.class);
            intent.putExtra(MainActivity.EXTRA_PAYLOAD, notification.payload);
            context.startService(intent);
        }
    }
}