
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
