package hueimmersive;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.plaf.FontUIResource;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Component;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Cursor;

import javax.swing.Box;

import java.awt.Font;
import java.awt.Color;

import javax.swing.ImageIcon;


public class UserInterface
{
	private JFrame frame;
	private JLabel labelConnect;
	private JButton button_Stop;
	private JButton button_Start;
	private JButton button_Once;
	private JComboBox checkbox_Format;
	public JCheckBox checkbox_ShowColorGrid;
	public JSlider slider_Brightness;
	private JPanel panel_Brightness;
	private JLabel label_BrightnessPercentage;
	public JCheckBox checkbox_RestoreLight;
	public JButton button_On;
	public JButton button_Off;
	public JSlider sSetChunks;
	private JPanel panel_Chunks;
	private JLabel label_ChunksNumber;
	private JLabel label_AspectRatio;
	private JMenuBar menubar;
	private JMenu menu_Help;
	private JPanel panel;
	private JMenuItem menuitem_CheckForUpdates;
	private JMenuItem menuitem_About;
	private JLabel label_UpdateAvailable;
	private JMenu menu_Settings;
	private JMenuItem menuitem_Options;
	private JMenuItem menuitem_Reset;
	private Component rigidarea;
	public ColorGridInterface cpi = new ColorGridInterface();
	
	public UserInterface() throws Exception
	{
		setLookAndFeel();
		initialize();
	}
	
	public void setEnabled(Boolean b)
	{
		frame.setEnabled(b);
	}
	
	private void setLookAndFeel() // set style - Required to look identical on each platform
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			UIManager.put("Label.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("Button.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("MenuBar.font", new FontUIResource("Segoe UI", Font.PLAIN, 12));
			UIManager.put("MenuItem.font", new FontUIResource("Segoe UI", Font.PLAIN, 12));
			UIManager.put("Panel.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("ToggleButton.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("RadioButton.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("CheckBox.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("ColorChooser.font", new FontUIResource("Dialog.plain", Font.PLAIN, 12));
			UIManager.put("ComboBox.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("List.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("RadioButtonMenuItem.font", new FontUIResource("Segoe UI", Font.PLAIN, 12));
			UIManager.put("CheckBoxMenuItem.font", new FontUIResource("Segoe UI", Font.PLAIN, 12));
			UIManager.put("Menu.font", new FontUIResource("Segoe UI", Font.PLAIN, 12));
			UIManager.put("PopupMenu.font", new FontUIResource("Segoe UI", Font.PLAIN, 12));
			UIManager.put("OptionPane.font", new FontUIResource("Segoe UI", Font.PLAIN, 12));
			UIManager.put("ProgressBar.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("ScrollPane.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("Viewport.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("TabbedPane.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("Table.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("TableHeader.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("TextField.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("PasswordField.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("TextArea.font", new FontUIResource("Monospaced.plain", Font.PLAIN, 13));
			UIManager.put("TextPane.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("EditorPane.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("TitledBorder.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
			UIManager.put("ToolBar.font", new FontUIResource("Segoe UI", Font.PLAIN, 12));
			UIManager.put("ToolTip.font", new FontUIResource("Segoe UI", Font.PLAIN, 12));
			UIManager.put("Tree.font", new FontUIResource("Tahoma", Font.PLAIN, 11));
		} 
		catch(Exception e)
		{
			Debug.exception(e);
		}
	}
	
	private void initialize() throws Exception // pre init user interface
	{
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setResizable(false);
		frame.setTitle("Hue Immersive");
		frame.setBounds(100, 100, 240, 237);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(Settings.getInteger("ui_x"), Settings.getInteger("ui_y"));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				Debug.closeLog();
				Settings.set("ui_x", frame.getX());
				Settings.set("ui_y", frame.getY());
				cpi.hide();
			}
		});

		Debug.info(null, "interface initialized");
		
		//loadMainInterface(); // uncomment to edit in Window Builder
	}
	
	public void loadConnectionInterface() throws Exception // load the connection interface
	{
		// setup window
		frame.getContentPane().setLayout(null);
		labelConnect = new JLabel("");
		labelConnect.setHorizontalAlignment(SwingConstants.CENTER);
		labelConnect.setBounds(0, 0, 234, 208);
		frame.getContentPane().add(labelConnect);
		frame.setVisible(true);
		setConnectState(0);
		
		Debug.info(null, "connection-interface loaded");
	}
	
	public void setConnectState(int state) throws Exception // set different visual output
	{
		switch (state)
		{
			case 0: //blank
				labelConnect.setIcon(null);
				break;
			case 1:  //search and connect
				labelConnect.setIcon(new ImageIcon(UserInterface.class.getResource("/images/hue_connect.gif")));
				Thread.sleep(1500);
				break;
			case 2:  // successfully connected
				labelConnect.setIcon(new ImageIcon(UserInterface.class.getResource("/images/hue_connected.png")));
				Thread.sleep(500);
				loadMainInterface();
				break;
			case 3:  // press link button
				labelConnect.setIcon(new ImageIcon(UserInterface.class.getResource("/images/hue_presslinkbutton.gif")));
				break;
			case 4:  // timeout
				labelConnect.setIcon(new ImageIcon(UserInterface.class.getResource("/images/hue_timeout.png")));
				break;
		}
	}

	public void loadMainInterface() throws Exception // load the main user interface
	{
		// setup window
		frame.getContentPane().removeAll();
		frame.getContentPane().setBackground(new Color(240, 240, 240));
		
		frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("10dlu:grow(2)"),
				ColumnSpec.decode("5dlu:grow"),
				ColumnSpec.decode("5dlu:grow"),
				ColumnSpec.decode("10dlu:grow(2)"),},
			new RowSpec[] {
				RowSpec.decode("24px:grow"),
				RowSpec.decode("24px:grow"),
				RowSpec.decode("24px:grow"),
				RowSpec.decode("24px:grow"),
				RowSpec.decode("24px:grow"),
				RowSpec.decode("24px:grow"),
				RowSpec.decode("24px:grow"),}));
		
		// Button ON
		button_On = new JButton("ON");
		button_On.setToolTipText("turn all lights on");
		button_On.setEnabled(false);
		button_On.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					button_On.setEnabled(false);
					button_Off.setEnabled(true);
					Main.hueControl.turnAllLightsOn();
				} catch (Exception e)
				{
					Debug.exception(e);
				}
			}
		});
		frame.getContentPane().add(button_On, "1, 1, 2, 1, fill, center");
		
		// Button OFF
		button_Off = new JButton("OFF");
		button_Off.setToolTipText("turn all lights off");
		button_Off.setEnabled(false);
		button_Off.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					button_On.setEnabled(true);
					button_Off.setEnabled(false);
					Main.hueControl.turnAllLightsOff();
				} catch (Exception e)
				{
					Debug.exception(e);
				}
			}
		});
		frame.getContentPane().add(button_Off, "3, 1, 2, 1, fill, center");
			
		// Setup On/Off Buttons
		setupOnOffButton();
	
		// ComboBox that holds a list with different aspect ratios
		checkbox_Format = new JComboBox();
		checkbox_Format.setToolTipText("set the capture area");
		checkbox_Format.setModel(new DefaultComboBoxModel(new String[] {"Fullscreen", "16:9 Widescreen", "21:9 Cinema", "Borderless (Windows)", "4:3 Traditional television"}));
		checkbox_Format.setSelectedIndex(Settings.getInteger("format"));
		checkbox_Format.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				Settings.set("format", checkbox_Format.getSelectedIndex()); 
				try
				{
					ImmersiveProcess.setStandbyOutput();
					label_ChunksNumber.setText(String.valueOf(ImmersiveProcess.chunksNumX * ImmersiveProcess.chunksNumY));
				} 
				catch (Exception e)
				{
					Debug.exception(e);
				}
			}
		});
		
		// Label "aspect ratio"
		label_AspectRatio = new JLabel("aspect ratio");
		frame.getContentPane().add(label_AspectRatio, "1, 2, center, center");
		frame.getContentPane().add(checkbox_Format, "2, 2, 3, 1, fill, default");
		
		// CheckBox to show the color grid (debug feature)
		checkbox_ShowColorGrid = new JCheckBox("   show color grid");
		checkbox_ShowColorGrid.setToolTipText("show the color/chunks grid");
		checkbox_ShowColorGrid.setSelected(Settings.getBoolean("colorgrid"));
		if(checkbox_ShowColorGrid.isSelected() == true)
		{
			cpi.show();
		}
		else if (checkbox_ShowColorGrid.isSelected() == false)
		{
			cpi.hide();
		}
		checkbox_ShowColorGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				try
				{
					Settings.set("colorgrid", checkbox_ShowColorGrid.isSelected());
					if(checkbox_ShowColorGrid.isSelected() == true)
					{
						cpi.show(); 
					}
					else if (checkbox_ShowColorGrid.isSelected() == false)
					{
						cpi.hide();
					}
				} 
				catch (Exception e)
				{
					Debug.exception(e);
				}
			}
		});
		frame.getContentPane().add(checkbox_ShowColorGrid, "1, 5, 4, 1");
		
		// Label "chunks"
		JLabel label_Chunks = new JLabel("   chunks");
		frame.getContentPane().add(label_Chunks, "1, 3, left, center");
		
		// Panel to hold the chunks slider and chunks amount Label
		panel_Chunks = new JPanel();
		panel_Chunks.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("115px"),
				ColumnSpec.decode("right:32px:grow"),},
			new RowSpec[] {
				RowSpec.decode("23px:grow"),}));
		frame.getContentPane().add(panel_Chunks, "2, 3, 3, 1, fill, fill");
		
		// Label chunks amount
		label_ChunksNumber = new JLabel(String.valueOf(ImmersiveProcess.chunksNumX * ImmersiveProcess.chunksNumY));
		panel_Chunks.add(label_ChunksNumber, "2, 1, center, center");
		
		// Slider to set the numbers of chunks
		sSetChunks = new JSlider();
		sSetChunks.setSnapToTicks(true);
		sSetChunks.setToolTipText("set how detailed the grid should be");
		sSetChunks.setMinorTickSpacing(1);
		sSetChunks.setMajorTickSpacing(1);
		sSetChunks.setMaximum(30);
		sSetChunks.setValue(Settings.getInteger("chunks"));
		panel_Chunks.add(sSetChunks, "1, 1, center, center");

		sSetChunks.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) 
			{
				Settings.set("chunks", sSetChunks.getValue());
				try
				{
					ImmersiveProcess.setStandbyOutput();
				} catch (Exception e)
				{
					Debug.exception(e);
				}
				label_ChunksNumber.setText(String.valueOf(ImmersiveProcess.chunksNumX * ImmersiveProcess.chunksNumY));
			}
		});
		
		// Label "brightness"
		JLabel label_Brightness = new JLabel("   brightness");
		label_Brightness.setToolTipText("");
		label_Brightness.setHorizontalAlignment(SwingConstants.LEFT);
		frame.getContentPane().add(label_Brightness, "1, 4, left, center");
		
		// Panel to hold brightness Slider and brightness percentage Label
		panel_Brightness = new JPanel();
		panel_Brightness.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("115px"),
				ColumnSpec.decode("right:38px:grow"),},
			new RowSpec[] {
				RowSpec.decode("26px"),}));
		frame.getContentPane().add(panel_Brightness, "2, 4, 3, 1, fill, fill");	
		
		// Label brightness percentage
		label_BrightnessPercentage = new JLabel(Settings.getInteger("brightness") + " %");
		label_BrightnessPercentage.setIconTextGap(3);
		label_BrightnessPercentage.setAlignmentX(Component.CENTER_ALIGNMENT);
		label_BrightnessPercentage.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_Brightness.add(label_BrightnessPercentage, "2, 1, center, center");
		
		// Slider brightness
		slider_Brightness = new JSlider();
		slider_Brightness.setToolTipText("set how bright your lights should be");
		slider_Brightness.setMinorTickSpacing(5);
		slider_Brightness.setSnapToTicks(true);
		slider_Brightness.setMinimum(5);
		slider_Brightness.setValue(Settings.getInteger("brightness"));
		slider_Brightness.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0)
			{
				label_BrightnessPercentage.setText(String.valueOf(slider_Brightness.getValue()) + " %");
				Settings.set("brightness", slider_Brightness.getValue());
			}
		});
		panel_Brightness.add(slider_Brightness, "1, 1, center, center");
		
		// CheckBox restore light
		checkbox_RestoreLight = new JCheckBox("   restore light (experimental v1)");
		checkbox_RestoreLight.setToolTipText("restore the color/brightness from your lights when the program stopped");
		checkbox_RestoreLight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				Settings.set("restorelight", checkbox_RestoreLight.isSelected());
			}
		});
		checkbox_RestoreLight.setSelected(Settings.getBoolean("restorelight"));
		frame.getContentPane().add(checkbox_RestoreLight, "1, 6, 4, 1");
		
		// Button stop
		button_Stop = new JButton("STOP");
		button_Stop.setEnabled(false);
		button_Stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) 
			{
				try
				{
					Main.hueControl.stopImmersiveProcess();
					button_Stop.setEnabled(false);
					button_Start.setEnabled(true);
					button_Once.setEnabled(true);
				} 
				catch (Exception e)
				{
					Debug.exception(e);
				}
			}
		});
		frame.getContentPane().add(button_Stop, "1, 7, fill, center");
		
		// Button start
		button_Start = new JButton("START");
		button_Start.setToolTipText("start to ");
		button_Start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) 
			{
				try
				{
					Main.hueControl.startImmersiveProcess();
					button_Stop.setEnabled(true);
					button_Start.setEnabled(false);
					button_Once.setEnabled(false);
				} 
				catch (Exception e)
				{
					Debug.exception(e);
				}
			}
		});
		frame.getContentPane().add(button_Start, "2, 7, 2, 1, default, center");
		
		// Button once
		button_Once = new JButton("ONCE");
		button_Once.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) 
			{
				try
				{
					Main.hueControl.onceImmersiveProcess();
					button_Stop.setEnabled(false);
					button_Start.setEnabled(true);
					button_Once.setEnabled(true);
				} 
				catch (Exception e)
				{
					Debug.exception(e);
				}
			}
		});
		frame.getContentPane().add(button_Once, "4, 7, default, center");
		
		// MenuBar
		menubar = new JMenuBar();
		frame.setJMenuBar(menubar);
		
		// Menu settings
		menu_Settings = new JMenu(" settings ");
		menubar.add(menu_Settings);
		
		// MenuItem options
		menuitem_Options = new JMenuItem("options");
		menuitem_Options.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				new OptionInterface();
			}
		});
		menu_Settings.add(menuitem_Options);
		
		// MenuItem reset
		menuitem_Reset = new JMenuItem("reset");
		menuitem_Reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				try
				{
					// create a dialog window (ok or cancel)
					Object[] options = {"OK","Cancel"};
					int dialogResult = JOptionPane.showOptionDialog(frame, 
																	"After a reset, all previous settings are lost and can't be recovered.\nThe program will be closed after reset.",
																	"Warning", 
																	JOptionPane.OK_CANCEL_OPTION, 
																	JOptionPane.WARNING_MESSAGE, 
																	null, 
																	options, 
																	options[1]);
	                if(dialogResult == JOptionPane.YES_OPTION)
	                {
	                	Settings.reset(true);
	                }
				} 
				catch (Exception e)
				{
					Debug.exception(e);
				}
			}
		});
		menu_Settings.add(menuitem_Reset);
		
		// Menu help
		menu_Help = new JMenu(" help ");
		menu_Help.setHorizontalTextPosition(SwingConstants.CENTER);
		menu_Help.setHorizontalAlignment(SwingConstants.CENTER);
		menubar.add(menu_Help);
		
		// MenuItem check for updates
		menuitem_CheckForUpdates = new JMenuItem("check for updates");
		menuitem_CheckForUpdates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) 
			{
				try
				{
					new UpdateInterface();
				} catch (Exception e)
				{
					Debug.exception(e);
				}
			}
		});
		menu_Help.add(menuitem_CheckForUpdates);
		
		// MenuItem about
		menuitem_About = new JMenuItem("about");
		menuitem_About.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) 
			{
				try
				{
					new AboutInterface();
				} catch (Exception e)
				{
					Debug.exception(e);
				}
			}
		});
		menu_Help.add(menuitem_About);
		
		// Panel to hold MenuBar
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setHgap(4);
		flowLayout.setAlignment(FlowLayout.RIGHT);
		menubar.add(panel);
		
		// Label for the update note
		label_UpdateAvailable = new JLabel("update available");
		label_UpdateAvailable.setToolTipText("klick here to open the update menu");
		panel.add(label_UpdateAvailable);
		label_UpdateAvailable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				try
				{
					new UpdateInterface();
				} catch (Exception e)
				{
					Debug.exception(e);
				}
			}
		});
		label_UpdateAvailable.setForeground(new Color(51, 153, 255));
		label_UpdateAvailable.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		label_UpdateAvailable.setHorizontalAlignment(SwingConstants.CENTER);
		label_UpdateAvailable.setVisible(Main.updateAvailable);
		
		// RigidArea to keep distance between objects
		rigidarea = Box.createRigidArea(new Dimension(9, 5));
		panel.add(rigidarea);
		
		// Label version
		JLabel label_Version = new JLabel("v" + Main.version);
		label_Version.setEnabled(false);
		panel.add(label_Version);
		
		// complete main user interface loading and show the window
		frame.setVisible(true);
		Debug.info(null, "main-interface loaded");
	}
	
	public void noteUpdate() // note that an update is available
	{
		label_UpdateAvailable.setVisible(true);
	}
	
	public void setupOnOffButton() throws Exception // enable/disable on/off Buttons
	{
		boolean lightOn = false;
		boolean lightOff = false;
		
		for(HLight light : HBridge.lights)
		{
			if (light.isOn())
			{
				lightOn = true;
			}
			else if (!light.isOn())
			{
				lightOff = true;
			}
		}
		
		if (lightOn && lightOff)
		{
			button_On.setEnabled(true);
			button_Off.setEnabled(true);
		}
		else if (lightOn)
		{
			button_On.setEnabled(false);
			button_Off.setEnabled(true);
		}
		else if (lightOff)
		{
			button_On.setEnabled(true);
			button_Off.setEnabled(false);
		}
	}
}
