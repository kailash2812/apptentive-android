//
//  DispatchQueue.java
//
//  Lunar Unity Mobile Console
//  https://github.com/SpaceMadness/lunar-unity-console
//
//  Copyright 2017 Alex Lementuev, SpaceMadness.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package com.apptentive.android.sdk.util.threading;

import android.os.Looper;

/**
 * A class representing dispatch queue where <code>{@link DispatchTask}</code> tasks can be executed
 * serially
 */
public abstract class DispatchQueue {

	/**
	 * Dispatch task implementation
	 */
	protected abstract void dispatch(DispatchTask task, long delayMillis);

	/**
	 * Add <code>{@link DispatchTask}</code> to the queue
	 */
	public void dispatchAsync(DispatchTask task) {
		dispatchAsync(task, 0L);
	}

	/**
	 * Add <code>{@link DispatchTask}</code> to the queue
	 */
	public void dispatchAsync(DispatchTask task, long delayMillis) {
		task.setScheduled(true);
		dispatch(task, delayMillis);
	}

	/**
	 * Add <code>{@link DispatchTask}</code> to the queue if it's not already on the queue (this
	 * way you can ensure only one instance of the task is scheduled at a time). After the task is
	 * executed you can schedule it again.
	 *
	 * @return true if task was scheduled
	 */
	public boolean dispatchAsyncOnce(DispatchTask task) {
		return dispatchAsyncOnce(task, 0L);
	}

	/**
	 * Add <code>{@link DispatchTask}</code> to the queue if it's not already on the queue (this
	 * way you can ensure only one instance of the task is scheduled at a time). After the task is
	 * executed you can schedule it again.
	 *
	 * @return true if task was scheduled
	 */
	public boolean dispatchAsyncOnce(DispatchTask task, long delayMillis) {
		if (!task.isScheduled()) {
			dispatchAsync(task, delayMillis);
			return true;
		}
		return false;
	}

	/**
	 * Stops queue execution and cancels all scheduled tasks
	 */
	public abstract void stop();

	/**
	 * A global dispatch queue associated with main thread
	 */
	public static DispatchQueue mainQueue() {
		return Holder.INSTANCE;
	}

	/**
	 * Creates a background queue with specified <code>name</code> and dispatch type.
	 */
	public static DispatchQueue createBackgroundQueue(String name, DispatchQueueType type) {
		if (type == DispatchQueueType.Serial) {
			return new SerialDispatchQueue(name);
		}
		if (type == DispatchQueueType.Concurrent) {
			return new ConcurrentDispatchQueue(name);
		}

		throw new IllegalArgumentException("Unexpected queue type: " + type);
	}

	/**
	 * Thread safe singleton trick
	 */
	private static class Holder {
		private static final DispatchQueue INSTANCE = createMainQueue();

		private static DispatchQueue createMainQueue() {
			try {
				// this call will fail when running a unit test
				// we would allow that and make test responsible for setting the implementation
				return new SerialDispatchQueue(Looper.getMainLooper());
			} catch (Exception e) {
				return null;
			}
		}
	}
}