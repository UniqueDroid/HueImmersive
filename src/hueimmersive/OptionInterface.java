package hueimmersive;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JSlider;
import javax.swing.ListSelectionModel;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Font;

import javax.swing.SwingConstants;


public class OptionInterface
{
	private JFrame frame;
	private JCheckBox checkbox_Autoswitch;
	private JCheckBox checkbox_Gammacorrection;
	private JPanel panel_Lights;
	private JComboBox checkbox_Screen;

	public OptionInterface()
	{
		Main.ui.setEnabled(false);
		initialize();
		getOptions();
	}
	
	private void initialize()
	{
		frame = new JFrame();
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				Settings.set("oi_x", frame.getX());
				Settings.set("oi_y", frame.getY());
				Main.ui.setEnabled(true);
			}
		});
		frame.setTitle("options");
		frame.setBounds(100, 100, 408, 334);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.setLocation(Settings.getInteger("oi_x"), Settings.getInteger("oi_y"));
		frame.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:20dlu:grow"),
				ColumnSpec.decode("left:20dlu:grow"),
				ColumnSpec.decode("left:20dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("16dlu"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("16dlu"),
				RowSpec.decode("10dlu"),
				RowSpec.decode("16dlu:grow"),
				RowSpec.decode("10dlu"),
				RowSpec.decode("105dlu"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("bottom:16dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		checkbox_Autoswitch = new JCheckBox("auto. turn off lights (experimental v2)    ");
		checkbox_Autoswitch.setHorizontalTextPosition(SwingConstants.LEADING);
		checkbox_Autoswitch.setToolTipText("turns the lights automatically off when the screen is near black");
		frame.getContentPane().add(checkbox_Autoswitch, "2, 2, 2, 1, right, center");
		
		checkbox_Gammacorrection = new JCheckBox("use gamma correction    ");
		checkbox_Gammacorrection.setHorizontalAlignment(SwingConstants.TRAILING);
		checkbox_Gammacorrection.setHorizontalTextPosition(SwingConstants.LEADING);
		checkbox_Gammacorrection.setToolTipText("makes the color more like the color on your screen");
		frame.getContentPane().add(checkbox_Gammacorrection, "2, 4, 2, 1, right, center");
		
		JSeparator separator_1 = new JSeparator();
		frame.getContentPane().add(separator_1, "2, 5, 3, 1, fill, center");
		
		JButton button_Ok = new JButton("ok");
		button_Ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				saveOptions();
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				frame.dispose();
			}
		});
		frame.getContentPane().add(button_Ok, "2, 10, fill, fill");
		
		JLabel label_Screen = new JLabel("capture screen    ");
		label_Screen.setToolTipText("select the screen to capture");
		frame.getContentPane().add(label_Screen, "2, 6, right, center");
		
		ArrayList<String> screens = new ArrayList<String>();
		for (int i = 0; i < GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length; i++)
		{
			screens.add("Display " + (i + 1));
		}
		checkbox_Screen = new JComboBox();
		checkbox_Screen.setToolTipText("select the screen to capture");
		checkbox_Screen.setModel(new DefaultComboBoxModel(screens.toArray()));
		((JLabel)checkbox_Screen.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		if (screens.size() <= 1)
		{
			checkbox_Screen.setEnabled(false);
		}
		frame.getContentPane().add(checkbox_Screen, "3, 6, fill, center");
				
		JButton button_Cancel = new JButton("cancel");
		button_Cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				frame.dispose();
			}
		});
		frame.getContentPane().add(button_Cancel, "3, 10, fill, fill");
		
		JButton button_Apply = new JButton("apply");
		button_Apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				saveOptions();
			}
		});
		frame.getContentPane().add(button_Apply, "4, 10, fill, fill");
		
		JSeparator separator_2 = new JSeparator();
		frame.getContentPane().add(separator_2, "2, 7, 3, 1, fill, center");
		
		JScrollPane scrollpane = new JScrollPane();
		scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scrollpane, "2, 8, 3, 1, fill, fill");
		
		
		// setup list with options for all lights
		
			panel_Lights = new JPanel();
			scrollpane.setViewportView(panel_Lights);
			int rows = HBridge.countLights();
			if (rows < 4)
			{
				rows = 4;
			}
			panel_Lights.setLayout(new GridLayout(rows, 1, 5, 4));
	
			JLabel label_Listheader = new JLabel("   active          name                    \r\n color algorithm                   brightness\r\n");
			scrollpane.setColumnHeaderView(label_Listheader);
			
			// create the list
			for (final HLight light : HBridge.lights)
			{
				final JPanel panel_options = new JPanel();
				panel_Lights.add(panel_options, light.id - 1);
				
				JLabel label_Name = new JLabel(light.name);
				label_Name.setPreferredSize(new Dimension(80, 15));
				
				final JList list_Algorithms = new JList();
				list_Algorithms.setLayoutOrientation(JList.HORIZONTAL_WRAP);
				list_Algorithms.setVisibleRowCount(1);
				list_Algorithms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				list_Algorithms.setModel(new AbstractListModel() 
				{
					String[] values = new String[] {"   A   ", "   B   ", "   C   ", "   D   "};
					public int getSize() {
						return values.length;
					}
					public Object getElementAt(int index) {
						return values[index];
					}
				});
				list_Algorithms.setSelectedIndex(Settings.Light.getAlgorithm(light.id));
				list_Algorithms.addMouseMotionListener(new MouseMotionAdapter() {
					@Override
					public void mouseMoved(MouseEvent arg0) 
					{
						int algorithm = list_Algorithms.locationToIndex(arg0.getPoint());
						if (algorithm == 0)
						{
							list_Algorithms.setToolTipText("saturated color");
						}
						else if (algorithm == 1)
						{
							list_Algorithms.setToolTipText("bright color");
						}
						else if (algorithm == 2)
						{
							list_Algorithms.setToolTipText("dark color");
						}
						else if (algorithm == 3)
						{
							list_Algorithms.setToolTipText("average color");
						}
					}
				});
	
				JPanel panel_Brightness = new JPanel();
				panel_Brightness.setLayout(new FormLayout(new ColumnSpec[] {
						ColumnSpec.decode("fill:93px"),
						ColumnSpec.decode("right:29px:grow"),},
					new RowSpec[] {
						RowSpec.decode("24px"),}));
				
				final JSlider slider_Brightness = new JSlider();
				slider_Brightness.setSnapToTicks(true);
				slider_Brightness.setMinorTickSpacing(5);
				slider_Brightness.setMinimum(10);
				slider_Brightness.setMaximum(100);
				slider_Brightness.setValue(Settings.Light.getBrightness(light.id));			
				panel_Brightness.add(slider_Brightness, "1, 1, center, center");
				
				final JLabel label_Brightness = new JLabel("100%");
				label_Brightness.setText(slider_Brightness.getValue() + "%");
				label_Brightness.setFont(new Font("Tahoma", Font.PLAIN, 10));
				panel_Brightness.add(label_Brightness, "2, 1, right, center");
				slider_Brightness.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent arg0)
					{
						label_Brightness.setText(slider_Brightness.getValue() + "%");
					}
				});
				
				final JCheckBox checkbox_Active = new JCheckBox();
				checkbox_Active.setSelected(Settings.Light.getActive(light.id));
				checkbox_Active.setToolTipText("allow the program to change this lights color and brightness");
				if (checkbox_Active.isSelected() == false)
				{
					label_Name.setEnabled(false);
					list_Algorithms.setEnabled(false);
					slider_Brightness.setEnabled(false);
					label_Brightness.setEnabled(false);
				}
				checkbox_Active.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) 
					{
						try
						{
							if (checkbox_Active.isSelected())
							{
								panel_options.getComponent(1).setEnabled(true);
								panel_options.getComponent(2).setEnabled(true);
								JPanel panel = (JPanel) panel_options.getComponent(3);
								panel.getComponent(0).setEnabled(true);
								panel.getComponent(1).setEnabled(true);
							}
							else
							{
								panel_options.getComponent(1).setEnabled(false);
								panel_options.getComponent(2).setEnabled(false);
								panel_options.getComponent(3).setEnabled(false);
								JPanel panel = (JPanel) panel_options.getComponent(3);
								panel.getComponent(0).setEnabled(false);
								panel.getComponent(1).setEnabled(false);
							}
						} catch (Exception e)
						{
							Debug.exception(e);
						}
					}
				});
				
				FlowLayout flowlayout_options = new FlowLayout(FlowLayout.LEFT, 12, 4);
				panel_options.setLayout(flowlayout_options);
				panel_options.add(checkbox_Active,0);
				panel_options.add(label_Name,1);
				panel_options.add(list_Algorithms,2);
				panel_options.add(panel_Brightness,3);
				
				frame.pack();
				frame.setVisible(true);
			}
	}
	
	private void getOptions() // get saved options and setup window elements
	{
		checkbox_Autoswitch.setSelected(Settings.getBoolean("autoswitch"));
		checkbox_Gammacorrection.setSelected(Settings.getBoolean("gammacorrection"));
		checkbox_Screen.setSelectedIndex(Settings.getInteger("screen"));
	}
	
	private void saveOptions() // save all settings
	{
		Settings.set("autoswitch", checkbox_Autoswitch.isSelected());
		Settings.set("gammacorrection", checkbox_Gammacorrection.isSelected());
		
		Settings.set("screen", checkbox_Screen.getSelectedIndex());
		
		for (HLight light : HBridge.lights)
		{
			JPanel panel_Light = (JPanel) panel_Lights.getComponent(light.id - 1);
			
			JCheckBox checkbox_Active = (JCheckBox) panel_Light.getComponent(0);
			Settings.Light.setActive(light.id, checkbox_Active.isSelected());
			
			JList list_Algorithms = (JList) panel_Light.getComponent(2);
			Settings.Light.setAlgorithm(light.id, list_Algorithms.getSelectedIndex());
			
			JPanel panel_Brightness = (JPanel) panel_Light.getComponent(3);
			JSlider slider_Brightness = (JSlider) panel_Brightness.getComponent(0);
			Settings.Light.setBrightness(light.id, slider_Brightness.getValue());
		}
	}
}
