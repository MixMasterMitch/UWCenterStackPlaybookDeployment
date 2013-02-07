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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
	private static String default_source_path;
	private static String default_playbook_ip;
	private static String default_playbook_password;
	private static String[] playbook_pins;
	private static int playbook_pins_index;
	private static String default_tablet_sdk;
	private static String default_root_html;
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
		Scanner s = null;
		ArrayList<String> settings = new ArrayList<String>();
		try {
			s = new Scanner(new File("text.txt"));
		} catch (FileNotFoundException e1) {
			// do nothing for now
		}
		while(s.hasNextLine()){
			settings.add(s.nextLine());
		}
		
		default_source_path = System.getProperty("user.home") + settings.get(0);
		default_playbook_ip = settings.get(1);
		default_playbook_password = settings.get(2);
		playbook_pins = new String[] {settings.get(3), settings.get(4), settings.get(5)};
		playbook_pins_index = Integer.parseInt(settings.get(6));
		default_tablet_sdk = settings.get(7);
		default_root_html = settings.get(8);
		
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
		playbookIpField.setText(default_playbook_ip);
		playbookIpLabel.setText("Playbook IP Address");
		playbookPasswordField.setText(default_playbook_password);
		playbookPasswordLabel.setText("Playbook Password");
		for (String pin : playbook_pins) {
			playbookPinComboBox.addItem(pin);
		}
		playbookPinComboBox.setSelectedIndex(playbook_pins_index);
		playbookPinLabel.setText("Playbook PIN");

		sdkPathField.setText(default_tablet_sdk);
		sdkPathLabel.setText("SDK Path");
		projectPathField.setText(default_source_path);
		projectPathLabel.setText("Project Path");
		rootHtmlPathField.setText(default_root_html);
		rootHtmlPathLabel.setText("Root Html File");

		// Setup deployButton
		deployButton.setText("Deploy");
		deployButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event)
			{
				FileWriter writer = null;
				String settings = projectPathField.getText().substring(System.getProperty("user.home").length()) + '\n' +
								  playbookIpField.getText() + '\n' + 
								  playbookPasswordField.getText() + '\n' + 
								  playbook_pins[0] + '\n' +
								  playbook_pins[1] + '\n' +
								  playbook_pins[2] + '\n' +
								  playbookPinComboBox.getSelectedIndex() + '\n' +
								  sdkPathField.getText() + '\n' +
								  rootHtmlPathField.getText();
				System.out.println(settings);
				try {
					writer = new FileWriter("text.txt", false);
				} catch (IOException e1) {
					System.out.println("SHIT!");
				}
				try {
					writer.write(settings);
					writer.flush();
				} catch (IOException e1) {
					// do nothing now
				}
				
				
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
