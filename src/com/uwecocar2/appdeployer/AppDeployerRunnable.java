package com.uwecocar2.appdeployer;
/**
 * Base {@link Runnable} implementation.
 */
abstract class AppDeployerRunnable implements Runnable {
	protected PrintsMessages console;
	protected CancelsDeployement canceler;

	protected void cancelDeployement(String message) {
		canceler.cancelDeployement(message);
	}
}
