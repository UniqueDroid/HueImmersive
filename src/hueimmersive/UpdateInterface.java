package hueimmersive;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Font;

import javax.swing.JButton;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UpdateInterface
{
	private JFrame frame;
	private JLabel label_LVersion;
	private JTextArea textarea_Changelog;
	private JButton button_Update;
	private JLabel label_Status;

	public UpdateInterface() throws Exception
	{
		Main.ui.setEnabled(false);
		initialize();
		checkForUpdates();
	}

	private void initialize()
	{
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("check for updates");
		frame.setBounds(100, 100, 435, 426);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) 
			{
				Main.ui.setEnabled(true);
			}
		});
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		frame.setLocation(screenWidth / 2 - frame.getSize().width / 2, screenHeight / 2 - frame.getSize().height / 2);
		
		JScrollPane scrollpane = new JScrollPane();
		scrollpane.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		scrollpane.setBounds(10, 61, 409, 288);
		frame.getContentPane().add(scrollpane);
		
		textarea_Changelog = new JTextArea();
		textarea_Changelog.setWrapStyleWord(true);
		textarea_Changelog.setFont(new Font("Monospaced", Font.PLAIN, 12));
		textarea_Changelog.setTabSize(4);
		textarea_Changelog.setEditable(false);
		scrollpane.setViewportView(textarea_Changelog);
		textarea_Changelog.setLineWrap(true);
		textarea_Changelog.setCaretPosition(0);
		
		JLabel lblThisVersion = new JLabel(" this version:");
		lblThisVersion.setBounds(10, 11, 75, 14);
		frame.getContentPane().add(lblThisVersion);
		
		JLabel lblLatestVersion = new JLabel(" latest version:");
		lblLatestVersion.setBounds(10, 36, 75, 14);
		frame.getContentPane().add(lblLatestVersion);
		
		JLabel label_TVersion = new JLabel(String.valueOf(Main.version));
		label_TVersion.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_TVersion.setBounds(95, 11, 46, 14);
		frame.getContentPane().add(label_TVersion);
		
		label_LVersion = new JLabel("-");
		label_LVersion.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_LVersion.setBounds(95, 36, 46, 14);
		frame.getContentPane().add(label_LVersion);
		
		button_Update = new JButton("UPDATE");
		button_Update.setToolTipText("");
		button_Update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				try
				{
					update();
				} catch (Exception e)
				{
					Debug.exception(e);
				}
			}
		});
		button_Update.setEnabled(false);
		button_Update.setBounds(10, 360, 409, 26);
		frame.getContentPane().add(button_Update);
		
		label_Status = new JLabel("up to date");
		label_Status.setHorizontalAlignment(SwingConstants.CENTER);
		label_Status.setFont(new Font("Tahoma", Font.PLAIN, 17));
		label_Status.setBounds(158, 11, 261, 39);
		frame.getContentPane().add(label_Status);
	}
	
	private void checkForUpdates() throws Exception
	{
        URL versionUrl = new URL("https://raw.githubusercontent.com/Blodjer/HueImmersive/master/VERSION"); // get version and build number from GitHub
        URL changelogUrl = new URL("https://raw.githubusercontent.com/Blodjer/HueImmersive/master/CHANGELOG.md"); // get the CHANGELOG.md from GitHub
        
        try // to get the latest version
        {
        	BufferedReader versionIn = new BufferedReader(new InputStreamReader(versionUrl.openStream()));
	        BufferedReader changelogIn = new BufferedReader(new InputStreamReader(changelogUrl.openStream()));
	        
	        String lBuild = versionIn.readLine();
	        String lVersion = versionIn.readLine();
	        label_LVersion.setText(lVersion);
	        versionIn.close();
	        
	        String changelog = "";
	        String line;
	        while((line = changelogIn.readLine()) != null)
	        {
	        	changelog = changelog + line + "\r\n";
	        }
	        changelog = changelog.replaceAll("##", "====");
	        textarea_Changelog.setText(changelog);
	        textarea_Changelog.setCaretPosition(0);    
	        changelogIn.close();
	        
	        if(Main.build < Integer.valueOf(lBuild))
	        {
	        	button_Update.setEnabled(true);
	        	label_Status.setText("update available");
	        	label_Status.setForeground(new Color(51, 153, 255));
	        	Main.ui.noteUpdate();
	        }
	        else
	        {
	        	label_Status.setText("up to date");
	        	button_Update.setEnabled(false);
	        }
        }
        catch (Exception e)
        {
        	Debug.exception(e);
        	label_Status.setText("no connection");
        	button_Update.setEnabled(false);
        }
	}
	
	private void update() throws Exception // open a link to download the latest version
	{
		Desktop.getDesktop().browse(new URL("https://github.com/Blodjer/HueImmersive/releases/latest").toURI());
	}
}
