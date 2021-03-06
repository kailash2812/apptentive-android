package com.apptentive.android.sdk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.apptentive.android.sdk.notifications.ApptentiveNotification;
import com.apptentive.android.sdk.notifications.ApptentiveNotificationCenter;
import com.apptentive.android.sdk.notifications.ApptentiveNotificationObserver;

import static com.apptentive.android.sdk.ApptentiveInternal.NOTIFICATION_INTERACTIONS_SHOULD_DISMISS;

/** A base class for any SDK activity */
public class ApptentiveBaseActivity extends AppCompatActivity implements ApptentiveNotificationObserver {

	//region Activity life cycle

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerNotifications();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterNotification();
	}

	//endregion

	//region Notifications

	private void registerNotifications() {
		ApptentiveNotificationCenter.defaultCenter().addObserver(NOTIFICATION_INTERACTIONS_SHOULD_DISMISS, this);
	}

	private void unregisterNotification() {
		ApptentiveNotificationCenter.defaultCenter().removeObserver(this);
	}

	//endregion

	//region ApptentiveNotificationObserver

	@Override
	public void onReceiveNotification(ApptentiveNotification notification) {
		// handle notification in subclasses
	}

	//endregion
}
