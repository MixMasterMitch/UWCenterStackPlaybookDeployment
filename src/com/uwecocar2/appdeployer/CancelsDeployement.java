package com.uwecocar2.appdeployer;

public interface CancelsDeployement {
	/**
	 * Immediately halts the app deployment and prints the given message.
	 */
	public void cancelDeployement(String message);
}
