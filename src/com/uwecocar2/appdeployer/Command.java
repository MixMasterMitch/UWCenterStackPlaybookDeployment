package com.uwecocar2.appdeployer;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.common.base.Joiner;

/**
 * A Command that will run as a new process.
 */
public class Command extends AppDeployerRunnable {
	private final String[] args;
	private final File workingDir;

	public Command(CancelsDeployement canceler, PrintsMessages console, String...args) {
		this(canceler, console, null, args);
	}

	public Command(CancelsDeployement canceler, PrintsMessages console, File workingDir, String...args) {
		this.canceler = canceler;
		this.console = console;
		this.workingDir = workingDir;
		this.args = args;
	}

	/**
	 * Runs the args as a new process. When the process is complete, the output is displayed in the
	 * console.
	 */
	@Override
	public void run() {
		console.printlnToConsole("Running Command: " + Joiner.on(" ").join(args));
		try {
			Process p = Runtime.getRuntime().exec(args, new String[0], workingDir);
			p.waitFor();
			printToConsole(p.getInputStream());
			printToConsole(p.getErrorStream());
			if (p.exitValue() == 0) {
				console.printlnToConsole("Finished Command");
			} else {
				cancelDeployement("Exit Value: " + p.exitValue());
			}
		} catch (Exception e) {
			cancelDeployement(e.getMessage());
		}
		console.printlnToConsole();
	}

	/**
	 * Prints every line of the given {@link InputStream} to the UI console.
	 * @param is The {@link InputStream} to print to the UI console.
	 * @throws IOException If is is closed.
	 */
	private void printToConsole(InputStream is) throws IOException {
		String line;
		BufferedReader input = new BufferedReader(new InputStreamReader(is));
		while ((line = input.readLine()) != null) {
			console.printlnToConsole(line);
		}
		input.close();
	}

}
