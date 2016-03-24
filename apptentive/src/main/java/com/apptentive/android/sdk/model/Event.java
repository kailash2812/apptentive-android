/*
 * Copyright (c) 2015, Apptentive, Inc. All Rights Reserved.
 * Please refer to the LICENSE file for the terms and conditions
 * under which redistribution and use of this file is permitted.
 */

package com.apptentive.android.sdk.model;

import com.apptentive.android.sdk.ApptentiveLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sky Kelsey
 */
public class Event extends ConversationItem {

	private static final String KEY_LABEL = "label";
	private static final String KEY_INTERACTION_ID = "interaction_id";
	private static final String KEY_DATA = "data";
	private static final String KEY_TRIGGER = "trigger";
	private static final String KEY_CUSTOM_DATA = "custom_data";

	public Event(String json) throws JSONException {
		super(json);
	}

	public Event(String label, JSONObject data) {
		super();
		try {
			put(KEY_LABEL, label);
			if (data != null) {
				put(KEY_DATA, data);
			}
		} catch (JSONException e) {
			ApptentiveLog.e("Unable to construct Event.", e);
		}
	}

	public Event(String label, Map<String, String> data) {
		super();
		try {
			put(KEY_LABEL, label);
			if (data != null && !data.isEmpty()) {
				JSONObject dataObject = new JSONObject();
				for (String key : data.keySet()) {
					dataObject.put(key, data.get(key));
				}
				put(KEY_DATA, dataObject);
			}
		} catch (JSONException e) {
			ApptentiveLog.e("Unable to construct Event.", e);
		}
	}

	public Event(String label, String interactionId, String data, Map<String, Object> customData, ExtendedData... extendedData) {
		super();
		try {
			put(KEY_LABEL, label);
			if (interactionId != null) {
				put(KEY_INTERACTION_ID, interactionId);
			}
			if (data != null) {
				put(KEY_DATA, new JSONObject(data));
			}

			if (customData != null && !customData.isEmpty()) {
				JSONObject customDataJson = generateCustomDataJson(customData);
				put(KEY_CUSTOM_DATA, customDataJson);
			}

			if (extendedData != null && extendedData.length != 0) {
				for (ExtendedData currentExtendedData : extendedData) {
					if (currentExtendedData != null) {
						put(currentExtendedData.getTypeName(), currentExtendedData);
					}
				}
			}
		} catch (JSONException e) {
			ApptentiveLog.e("Unable to construct Event.", e);
		}
	}

	private JSONObject generateCustomDataJson(Map<String, Object> customData) {
		JSONObject ret = new JSONObject();
		for (String key : customData.keySet()) {
			Object value = customData.get(key);
			if (value != null) {
				try {
					ret.put(key, value);
				} catch (Exception e) {
					ApptentiveLog.w("Error adding custom data to Event: \"%s\" = \"%s\"", key, value.toString(), e);
				}
			}
		}
		return ret;
	}

	public Event(String label, String trigger) {
		this(label, (Map<String, String>) null);
		Map<String, String> data = new HashMap<String, String>();
		data.put(KEY_TRIGGER, trigger);
		putData(data);
	}

	@Override
	protected void initBaseType() {
		setBaseType(BaseType.event);
	}

	public void putData(Map<String, String> data) {
		if (data == null || data.isEmpty()) {
			return;
		}
		try {
			JSONObject dataObject;
			if (isNull(KEY_DATA)) {
				dataObject = new JSONObject();
				put(KEY_DATA, dataObject);
			} else {
				dataObject = getJSONObject(KEY_DATA);
			}
			for (String key : data.keySet()) {
				dataObject.put(key, data.get(key));
			}
		} catch (JSONException e) {
			ApptentiveLog.e("Unable to add data to Event.", e);
		}
	}

	public enum EventLabel {

		app__launch("launch"),
		app__exit("exit"),
		error("error");

		private final String labelName;

		EventLabel(String labelName) {
			this.labelName = labelName;
		}

		public String getLabelName() {
			return labelName;
		}
	}
}