package com.uwecocar2.appdeployer;
import static java.util.concurrent.Executors.newFixedThreadPool;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

//# Debug Token Request
//	"$TABLET_SDK_BIN"/blackberry-debugtokenrequest -storepass $PLAYBOOK_PASSWORD -devicepin $PLAYBOOK_PIN "$DEBUG_TOKEN"
//
//	# Install Debug Token
//	"$TABLET_SDK_BIN"/blackberry-deploy -installDebugToken "$DEBUG_TOKEN" -device $PLAYBOOK_IP -password $PLAYBOOK_PASSWORD
//
//	# Zip App
//	cd "$SOURCE_PATH"
//	zip -r "$SOURCE_PATH" *
//
//	# Package App
//	"$BBWP" $ZIPPED_SOURCE_PATH -d -o $PACKAGED_SOURCE_PATH
//
//	# Install App
//	"$TABLET_SDK_BIN"/blackberry-deploy -installApp -password $PLAYBOOK_PASSWORD -device $PLAYBOOK_IP -package "$PACKAGED_SOURCE_FILE"

public class AppDeployer implements CancelsDeployement {
	private final AppDeployerWindow window;
	private String originalRootHtml;
	private final ExecutorService executorService = newFixedThreadPool(1);

	//	private static void runCommand(String[] command) throws IOException, InterruptedException {
	//		runCommand(command, new File(""));
	//	}
	//
	//	private static void runCommand(String[] command, File dir) throws IOException, InterruptedException {
	//		window.printlnToConsole("Running Command: " + Joiner.on(" ").join(command));
	//
	//		CommandLine cmdLine = new CommandLine(dir);
	//		for (String s : command) {
	//			cmdLine.addArgument(s);
	//		}
	//
	//		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler() {
	//
	//		};
	//
	//		PumpStreamHandler psh = new PumpStreamHandler(new CommandLogger(window), new CommandLogger(window));
	//
	//		ExecuteWatchdog watchdog = new ExecuteWatchdog(60*1000);
	//		Executor executor = new DefaultExecutor();
	//		executor.setStreamHandler(psh);
	//		executor.setWatchdog(watchdog);
	//		executor.execute(cmdLine);
	//
	//		// some time later the result handler callback was invoked so we
	//		// can safely request the exit value
	//		resultHandler.waitFor();
	//
	//
	//	}

	public AppDeployer(AppDeployerWindow window) {
		this.window = window;
	}
	public void deploy() throws IOException, InterruptedException {
		window.enableDeployment(false);

		Command debugTokenRequest 	= new Command(this, window, getDebugTokenRequestPath(), "-storepass", window.getPlaybookPassword(), "-devicepin", window.getPlaybookPin(), getDebugTokenPath());
		Command installDebugToken 	= new Command(this, window, getDeployCommandPath(), "-installDebugToken", getDebugTokenPath(), "-device", window.getPlaybookIp(), "-password", window.getPlaybookPassword());
		Command zip 				= new Command(this, window, new File(window.getProjectPath()), "zip", "-r", window.getProjectPath(), ".", "*", "-x", ".*", "testing*", "DeployApp.app*", "PlayBookSigner.app*");
		Command packageApp 			= new Command(this, window, getBbwpPath(), window.getProjectPath() + ".zip", "-d", "-o", window.getProjectPath());
		Command deployApp 			= new Command(this, window, getDeployCommandPath(), "-installApp", "-password", window.getPlaybookPassword(), "-device", window.getPlaybookIp(), "-package", getPackagePath());
		Command cleanUp 			= new Command(this, window, "rm", "-f", window.getProjectPath() + ".zip", getPackagePath());

		try {
			executorService.execute(setRootHtmlFile());
			executorService.execute(cleanUp);
			executorService.execute(debugTokenRequest);
			executorService.execute(installDebugToken);
			executorService.execute(zip);
			executorService.execute(packageApp);
			executorService.execute(deployApp);
			executorService.execute(cleanUp);
			executorService.execute(new DeploymentFinalizer(this));
		} catch (Exception e) {
			cancelDeployement(e.getMessage());
		}
	}

	private XmlModifier setRootHtmlFile() {
		return new XmlModifier.Builder()
		.withFile(window.getProjectPath() + "/config.xml")
		.withElement("content")
		.withAttribute("src")
		.withValue(window.getRootHtml())
		.withCanceler(this)
		.withConsole(window)
		.withXmlChangeListener(new XmlChangeListener() {

			@Override
			public void onXmlChange(String oldValue) {
				originalRootHtml = oldValue;
			}
		}).build();
	}

	private XmlModifier revertRootHtmlFile() {
		return new XmlModifier.Builder()
		.withFile(window.getProjectPath() + "/config.xml")
		.withElement("content")
		.withAttribute("src")
		.withValue(originalRootHtml)
		.withCanceler(this)
		.withConsole(window)
		.build();
	}

	private String getSdkBin() {
		return window.getSdkPath() + "/bbwp/blackberry-tablet-sdk/bin";
	}

	private String getDebugTokenRequestPath() {
		return getSdkBin() + "/blackberry-debugtokenrequest";
	}

	private String getDebugTokenPath() {
		return window.getSdkPath() + "/bbwp/blackberry-tablet-sdk/debug-tokens/playbook_debug_token.bar";
	}

	private String getPackagePath() {
		return window.getProjectPath() + window.getProjectPath().substring(window.getProjectPath().lastIndexOf("/")) + ".bar";
	}

	private String getDeployCommandPath() {
		return getSdkBin() + "/blackberry-deploy";
	}

	private String getBbwpPath() {
		return window.getSdkPath() + "/bbwp/bbwp";
	}

	@Override
	public void cancelDeployement(String message) {
		executorService.shutdownNow();
		window.enableDeployment(true);
		if (originalRootHtml != null) {
			revertRootHtmlFile().run();
		}
		window.printlnToConsole("ERROR: Deployement failed: " + message);
	}

	public void finalizeDepoyment() {
		window.enableDeployment(true);
		if (originalRootHtml != null) {
			revertRootHtmlFile().run();
		}
		window.printlnToConsole("Finished All Commands");
	}
}
