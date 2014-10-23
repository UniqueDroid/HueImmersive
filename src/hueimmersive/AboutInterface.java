package hueimmersive;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.JButton;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.net.URI;

import javax.swing.JTextPane;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Cursor;
import java.awt.Color;


public class AboutInterface
{
	private JFrame frame;

	public AboutInterface()
	{
		Main.ui.setEnabled(false);
		initialize();
	}
	
	private void initialize()
	{
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("about");
		frame.setResizable(false);
		frame.setBounds(100, 100, 504, 343);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				Main.ui.setEnabled(true);
			}
		});
		
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		frame.setLocation(screenWidth / 2 - frame.getSize().width / 2, screenHeight / 2 - frame.getSize().height / 2);
		
		JButton button_Close = new JButton("close");
		button_Close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				frame.dispose();
			}
		});
		button_Close.setBounds(381, 281, 107, 24);
		frame.getContentPane().add(button_Close);
		
		JLabel lblHueimmersive = new JLabel("HueImmersive");
		lblHueimmersive.setVerticalAlignment(SwingConstants.TOP);
		lblHueimmersive.setHorizontalAlignment(SwingConstants.CENTER);
		lblHueimmersive.setFont(new Font("Tahoma", Font.PLAIN, 26));
		lblHueimmersive.setBounds(10, 9, 478, 32);
		frame.getContentPane().add(lblHueimmersive);
		
		JLabel label_Version = new JLabel("v" + String.valueOf(Main.version));
		label_Version.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_Version.setHorizontalAlignment(SwingConstants.CENTER);
		label_Version.setBounds(10, 44, 478, 14);
		frame.getContentPane().add(label_Version);
		
		JScrollPane scrollpane_About = new JScrollPane();
		scrollpane_About.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		scrollpane_About.setBounds(10, 67, 478, 203);
		frame.getContentPane().add(scrollpane_About);
		
		JTextPane txtpnHueImmersiveIs = new JTextPane();
		txtpnHueImmersiveIs.setFocusable(false);
		scrollpane_About.setViewportView(txtpnHueImmersiveIs);
		txtpnHueImmersiveIs.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent he) 
			{
	            if(he.getEventType() == HyperlinkEvent.EventType.ACTIVATED) 
	            {
	            	try
					{
	            		if (he.getDescription().indexOf("@") != -1)
	            		{
	            			Desktop.getDesktop().mail(new URI("mailto:" + he.getDescription()));
	            		}
	            		else
	            		{
	            			Desktop.getDesktop().browse(he.getURL().toURI());
	            		}
					} catch (Exception e)
					{
						Debug.exception(e);
					}
	            }
			}
		});
		txtpnHueImmersiveIs.setBackground(new Color(204, 204, 204));
		txtpnHueImmersiveIs.setEditable(false);
		txtpnHueImmersiveIs.setContentType("text/html");
		txtpnHueImmersiveIs.setText("<font style=\"font-family: Tahoma; font-size:13\">\r\n\r\nHue Immersive is a program written and developed by Jannik Szwaczka (Blodjer).<br>\r\nIf you need support or have suggestions/feedback please send me an email or write it in the reddit comments. You can also create an issue on GitHub.<br>\r\n<p>\r\nGitHub: <a href='https://github.com/Blodjer/HueImmersive'>https://github.com/Blodjer/HueImmersive</a><br>\r\nReddit: <a href='http://redd.it/2e3vq9'>http://redd.it/2e3vq9</a><br>\r\nEmail: <a href='blodjer@live.de'>blodjer@live.de</a><br>\r\n</p><br>\r\n\r\n<b>\r\nIf you you want to support me with a donation: <a href='https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=BVVY8L9TTPQFJ'>Thank You!</a>\r\n</b>\r\n\r\n<p align=\"center\">\r\nCopyright (c) 2014 Jannik Szwaczka <br>\r\n<a href='https://github.com/Blodjer/HueImmersive/blob/master/LICENSE'>License</a>\r\n</p>\r\n\r\n</font>");

		frame.setVisible(true);
	}
}
