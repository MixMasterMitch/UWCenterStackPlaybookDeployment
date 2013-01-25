package com.uwecocar2.appdeployer;
/**
 * When the {@link Command}s are run on a separate thread, a {@link DeploymentFinalizer} can be added
 * to the thread queue to notify the {@link AppDeployer} that all of the {@link Command}s have been
 * completed.
 */
public class DeploymentFinalizer extends AppDeployerRunnable {
	private final AppDeployer appDeployer;

	public DeploymentFinalizer(AppDeployer appDeployer) {
		this.appDeployer = appDeployer;
	}

	@Override
	public void run() {
		appDeployer.finalizeDepoyment();
	}
}
