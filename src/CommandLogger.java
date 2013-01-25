import org.apache.commons.exec.LogOutputStream;


public class CommandLogger extends LogOutputStream {
	AppDeployerWindow window;

	public CommandLogger(AppDeployerWindow window) {
		this.window = window;
	}

	@Override
	protected void processLine(String arg0, int arg1) {
		window.printlnToConsole(arg0);
	}

}
