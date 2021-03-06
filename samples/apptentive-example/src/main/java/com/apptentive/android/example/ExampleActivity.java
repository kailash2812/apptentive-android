/*
 * Copyright (c) 2015, Apptentive, Inc. All Rights Reserved.
 * Please refer to the LICENSE file for the terms and conditions
 * under which redistribution and use of this file is permitted.
 */

package com.apptentive.android.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.apptentive.android.example.push.RegistrationIntentService;
import com.apptentive.android.sdk.Apptentive;

/**
 * This is an example integration of Apptentive.
 */
public class ExampleActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// GCM: Start IntentService to register this application.
		Intent intent = new Intent(this, RegistrationIntentService.class);
		startService(intent);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// Only engage if this window is gaining focus.
		if (hasFocus) {
			Apptentive.handleOpenedPushNotification(this);
		}
	}

	/**
	 * Provide a simple feedback button in your app.
	 */
	public void onMessageCenterButtonPressed(@SuppressWarnings("unused") View view) {
		Apptentive.showMessageCenter(this);
	}
}
