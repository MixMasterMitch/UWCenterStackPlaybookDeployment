package com.uwecocar2.appdeployer;
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

/**
 * The user interface for the AppDeployer.
 * 
 * Displays logs in the console.
 */
public class AppDeployerWindow implements PrintsMessages {
	private static final String DEFAULT_SOURCE_PATH = System.getProperty("user.home") + "/Documents/GitHub/UWCenterStack";
	private static final String DEFAULT_PLAYBOOK_IP = "169.254.0.1";
	private static final String DEFAULT_PLAYBOOK_PASSWORD = "playbook";
	private static final String[] PLAYBOOK_PINS = {"501138E7", "502CEE27", "50303968"};
	private static final String DEFAULT_TABLET_SKD = "/Developer/SDKs/Research In Motion/BlackBerry WebWorks SDK for TabletOS 2.2.0.5";
	private static final String DEFAULT_ROOT_HTML = "srcs/t2c/coord.html";
	private static final String MAIN_WINDOW_TITLE = "Deploy App";

	// UI elements
	private final JPanel fieldsPanel = new JPanel();
	private final JTextField playbookIpField = new JTextField();
	private final JLabel playbookIpLabel = new JLabel();
	private final JTextField playbookPasswordField = new JTextField();
	private final JLabel playbookPasswordLabel = new JLabel();
	private final JComboBox playbookPinComboBox = new JComboBox();
	private final JLabel playbookPinLabel = new JLabel();
	private final JTextField sdkPathField = new JTextField();
	private final JLabel sdkPathLabel = new JLabel();
	private final JTextField projectPathField = new JTextField();
	private final JLabel projectPathLabel = new JLabel();
	private final JTextField rootHtmlPathField = new JTextField();
	private final JLabel rootHtmlPathLabel = new JLabel();
	private final JButton deployButton = new JButton();

	private final JTextArea console = new JTextArea();

	public AppDeployerWindow() {
		// Create mainWindow to hold all other elements
		JFrame mainWindow = new JFrame(MAIN_WINDOW_TITLE);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create button panel to hold the deploy button
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(deployButton);

		// Setup UI fields
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
		rootHtmlPathField.setText(DEFAULT_ROOT_HTML);
		rootHtmlPathLabel.setText("Root Html File");

		// Setup deployButton
		deployButton.setText("Deploy");
		deployButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event)
			{
				try {
					console.setText("");
					new AppDeployer(AppDeployerWindow.this).deploy();
				} catch (Exception e) {
					console.setText(e.toString());
				}
			}
		});

		// Setup console
		console.setEditable(false);

		// Setup fiels and labels layout
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
																		.addComponent(sdkPathLabel))
																		.addGroup(layout.createSequentialGroup()
																				.addComponent(rootHtmlPathField)
																				.addPreferredGap(RELATED)
																				.addComponent(rootHtmlPathLabel)))
																				.addContainerGap(27, MAX_VALUE))
				);

		layout.linkSize(HORIZONTAL, new Component[] {playbookPasswordField, playbookIpField, playbookPinComboBox});
		layout.linkSize(HORIZONTAL, new Component[] {sdkPathField, projectPathField, rootHtmlPathField});

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
																.addGroup(layout.createParallelGroup(BASELINE)
																		.addComponent(rootHtmlPathField)
																		.addComponent(rootHtmlPathLabel))
																		.addPreferredGap(RELATED)
																		.addContainerGap(21, Short.MAX_VALUE))
				);

		// Make the console scrollable
		JScrollPane consoleScroller = new JScrollPane(console);
		consoleScroller.setPreferredSize(new Dimension(300, 300));
		consoleScroller.setAlignmentX(LEFT_ALIGNMENT);

		// Combine panels
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

	/**
	 * @return The IP address entered by the user.
	 */
	public String getPlaybookIp() {
		return playbookIpField.getText();
	}

	/**
	 * @return The Playbook PIN selected by the user.
	 */
	public String getPlaybookPin() {
		return (String) playbookPinComboBox.getSelectedItem();
	}

	/**
	 * @return The Playbook password entered by the user.
	 */
	public String getPlaybookPassword() {
		return playbookPasswordField.getText();
	}

	/**
	 * @return The Playbook SDK path entered by the user.
	 */
	public String getSdkPath() {
		return sdkPathField.getText();
	}

	/**
	 * @return The path to the root of the application entered by the user.
	 */
	public String getProjectPath() {
		return projectPathField.getText();
	}

	/**
	 * @return The path to the application entry point webpage entered by the user.
	 */
	public String getRootHtml() {
		return rootHtmlPathField.getText();
	}

	@Override
	public void printlnToConsole(String text) {
		console.setText(console.getText() + text + "\n");
	}

	@Override
	public void printlnToConsole() {
		printlnToConsole("");
	}

	/**
	 * Enables or disables the deployment button.
	 * @param enable true to enable the button.
	 */
	public void enableDeployment(boolean enable) {
		deployButton.setEnabled(enable);
	}
}
