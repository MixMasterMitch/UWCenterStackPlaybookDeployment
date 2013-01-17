import static java.awt.Component.LEFT_ALIGNMENT;
import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;
import static javax.swing.SwingConstants.HORIZONTAL;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AppDeployerWindow {
	private static final String DEFAULT_SOURCE_PATH=System.getProperty("user.home") + "/Documents/GitHub/UWCenterStack";
	private static final String ZIPPED_SOURCE_PATH=DEFAULT_SOURCE_PATH + ".zip";
	private static final String PACKAGED_SOURCE_PATH=DEFAULT_SOURCE_PATH;
	private static final String PACKAGED_SOURCE_FILE=DEFAULT_SOURCE_PATH + "/UWCenterStack.bar";
	private static final String DEFAULT_PLAYBOOK_IP="69.91.176.132";
	private static final String DEFAULT_PLAYBOOK_PASSWORD="playbook";
	private static final String[] PLAYBOOK_PINS={"501138e7"};
	private static final String DEFAULT_TABLET_SKD ="/Developer/SDKs/Research In Motion/BlackBerry WebWorks SDK for TabletOS 2.2.0.5";
	private static final String TABLET_SDK_BIN ="/bbwp/blackberry-tablet-sdk/bin";
	private static final String BBWP="/bbwp/bbwp";
	private static final String DEBUG_TOKEN = "/bbwp/blackberry-tablet-sdk/debug-tokens/playbook_debug_token.bar";

	JPanel fieldsPanel = new JPanel();
	JTextField playbookIpField = new JTextField();
	JLabel playbookIpLabel = new JLabel();
	JTextField playbookPasswordField = new JTextField();
	JLabel playbookPasswordLabel = new JLabel();
	JComboBox playbookPinComboBox = new JComboBox();
	JLabel playbookPinLabel = new JLabel();
	JPanel panel2 = new JPanel();
	JTextField sdkPathField = new JTextField();
	JLabel sdkPathLabel = new JLabel();
	JTextField projectPathField = new JTextField();
	JLabel projectPathLabel = new JLabel();

	JTextArea console = new JTextArea();

	public AppDeployerWindow() {
		JFrame mainWindow = new JFrame("Deploy App");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel buttonPanel = new JPanel();
		JButton deployButton = new JButton();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(deployButton);

		playbookIpField.setText(DEFAULT_PLAYBOOK_IP);
		playbookIpLabel.setText("Playbook IP Address");
		playbookPasswordField.setText(DEFAULT_PLAYBOOK_PASSWORD);
		playbookPasswordLabel.setText("Playbook Password");
		for (String pin : PLAYBOOK_PINS) {
			playbookPinComboBox.addItem(pin);
		}
		playbookPinLabel.setText("Playbook PIN");

		sdkPathField.setText(DEFAULT_TABLET_SKD);
		sdkPathLabel.setText("SDK Path");
		projectPathField.setText(DEFAULT_SOURCE_PATH);
		projectPathLabel.setText("Project Path");

		deployButton.setText("Deploy");
		deployButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event)
			{
				try {
					AppDeployer.deploy();
				} catch (Exception e) {
					console.setText(e.toString());
				}
			}
		});

		console.setEditable(false);


		GroupLayout layout = new GroupLayout(fieldsPanel);
		fieldsPanel.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(LEADING)
								.addGroup(layout.createSequentialGroup()
										.addComponent(playbookIpField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
										.addPreferredGap(RELATED)
										.addComponent(playbookIpLabel))
										.addGroup(layout.createSequentialGroup()
												.addComponent(playbookPasswordField)
												.addPreferredGap(RELATED)
												.addComponent(playbookPinLabel))
												.addGroup(layout.createSequentialGroup()
														.addComponent(playbookPinComboBox)
														.addPreferredGap(RELATED)
														.addComponent(playbookPasswordLabel))
														.addGroup(layout.createSequentialGroup()
																.addComponent(projectPathField)
																.addPreferredGap(RELATED)
																.addComponent(projectPathLabel))
																.addGroup(layout.createSequentialGroup()
																		.addComponent(sdkPathField)
																		.addPreferredGap(RELATED)
																		.addComponent(sdkPathLabel)))
																		.addContainerGap(27, MAX_VALUE))
				);

		layout.linkSize(HORIZONTAL, new Component[] {playbookPasswordField, playbookIpField, playbookPinComboBox});
		layout.linkSize(HORIZONTAL, new Component[] {sdkPathField, projectPathField});

		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(BASELINE)
								.addComponent(playbookIpField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
								.addComponent(playbookIpLabel))
								.addPreferredGap(RELATED)
								.addGroup(layout.createParallelGroup(BASELINE)
										.addComponent(playbookPasswordField)
										.addComponent(playbookPasswordLabel))
										.addPreferredGap(RELATED)
										.addGroup(layout.createParallelGroup(BASELINE)
												.addComponent(playbookPinComboBox)
												.addComponent(playbookPinLabel))
												.addPreferredGap(RELATED)
												.addGroup(layout.createParallelGroup(BASELINE)
														.addComponent(projectPathField)
														.addComponent(projectPathLabel))
														.addPreferredGap(RELATED)
														.addGroup(layout.createParallelGroup(BASELINE)
																.addComponent(sdkPathField)
																.addComponent(sdkPathLabel))
																.addPreferredGap(RELATED)
																.addContainerGap(21, Short.MAX_VALUE))
				);

		JScrollPane consoleScroller = new JScrollPane(console);
		consoleScroller.setPreferredSize(new Dimension(300, 300));
		consoleScroller.setAlignmentX(LEFT_ALIGNMENT);

		JPanel bodyPanel = new JPanel();
		bodyPanel.setLayout(new BorderLayout());
		bodyPanel.add(fieldsPanel, BorderLayout.CENTER);
		bodyPanel.add(buttonPanel, BorderLayout.PAGE_END);
		bodyPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		//Display the window.
		mainWindow.getContentPane().add(bodyPanel, BorderLayout.CENTER);
		mainWindow.getContentPane().add(consoleScroller, BorderLayout.PAGE_END);
		mainWindow.pack();
		mainWindow.setVisible(true);

	}

	public String getPlaybookIp() {
		return playbookIpField.getText();
	}

	public String getPlaybookPin() {
		return (String) playbookPinComboBox.getSelectedItem();
	}

	public String getPlaybookPassword() {
		return playbookPasswordField.getText();
	}

	public void printlnToConsole(String text) {
		console.setText(console.getText() + text + "\n");
	}

	public String getSdkPath() {
		return sdkPathField.getText();
	}

	public String getProjectPath() {
		return projectPathField.getText();
	}
}
