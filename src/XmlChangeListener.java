/**
 * Used to notify the AppDeployer in the main thread when the config.xml is changed.
 */
public interface XmlChangeListener {
	public void onXmlChange(String oldValue);
}
