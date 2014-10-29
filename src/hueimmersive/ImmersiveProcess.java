package hueimmersive;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;


public class ImmersiveProcess
{
	private static Rectangle ScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[Settings.getInteger("screen")].getDefaultConfiguration().getBounds();
	public static Rectangle CaptureSize = new Rectangle(0, 0, ScreenSize.width, ScreenSize.height);
	public static int chunksNumX;
	public static int chunksNumY;
	public static final int algorithms = 4;
	
	static
	{
		setSettings();
	}
	
	public static void setStandbyOutput() throws Exception
	{
		setSettings();
		Main.ui.cpi.setStandbyIcon(CaptureSize, chunksNumX, chunksNumY);
	}
	
	private static void setSettings()
	{		
		// setup screen area
		double ratio;
		int x, y, w, h;
		ScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[Settings.getInteger("screen")].getDefaultConfiguration().getBounds();
		switch (Settings.getInteger("format"))
		{
			case 0: // Fullscreen
				ratio = 1.0;
				w = (int)ScreenSize.getWidth();
				h = (int)ScreenSize.getHeight();
				x = 0;
				y = 0;
				break;

			case 1: // 16:9
				ratio = 9.0 / 16.0;
				w = (int)ScreenSize.getWidth();
				h = (int)(ratio * ScreenSize.getWidth());
				x = 0;
				y = (int)((ScreenSize.getHeight() - h) / 2);
				break;
	
			case 2: // 21:9 (Cinema)
				ratio = 9.0 / 21.0;
				w = (int)(ScreenSize.getWidth());
				h = (int)(ratio * ScreenSize.getWidth());
				x = 0;
				y = (int)((ScreenSize.getHeight() - h) / 2);
				break;
	
			case 3: // Borderless (Windows)
				ratio = 1.0;
				w = (int)ScreenSize.getWidth();
				h = (int)(ScreenSize.getHeight() - 40 - 23);
				x = 0;
				y = 23;
				break;
	
			case 4: // 4:3
				ratio = 3.0 / 4.0;
				w = (int)(ratio * ScreenSize.getWidth());
				h = (int)ScreenSize.getHeight();
				x = (int)((ScreenSize.getWidth() - w) / 2);
				y = 0;
				break;
	
			default:
				ratio = 1.0;
				w = (int)ScreenSize.getWidth();
				h = (int)ScreenSize.getHeight();
				x = 0;
				y = 0;
				break;
		}
		CaptureSize = new Rectangle((int)ScreenSize.getX() + x, (int)ScreenSize.getY() + y, w, h);
		
		// calculate number of chunks
		double chunks = Settings.getInteger("chunks");
		chunks = 3 + 0.35 * Math.pow(chunks, 1.4);
		chunksNumX = (int) Math.round(chunks);
		chunksNumY = (int) Math.round(((double)CaptureSize.height / (double)CaptureSize.width) * chunksNumX); 
		//round -> exact | ceil (round up) -> less options, always transverse | nothing (round down) -> imprecise
	}
	
	public static void execute() throws Exception // execute the process to get immersive colors based on the captured area
	{
		applyChanges();
		capture();
	}
	
	private static int lFormat = Settings.getInteger("format");
	private static int lChunks = Settings.getInteger("chunks");
	private static int lScreen = Settings.getInteger("screen");
	private static boolean forceStandbyColorGrid = false;
	private static void applyChanges() throws Exception // apply changed settings
	{
		forceStandbyColorGrid = false;
		if (Settings.getInteger("format") != lFormat || Settings.getInteger("chunks") != lChunks || Settings.getInteger("screen") != lScreen)
		{
			lFormat = Settings.getInteger("format");
			lChunks = Settings.getInteger("chunks");
			lScreen = Settings.getInteger("screen");
			setSettings();
			Main.ui.cpi.setStandbyIcon(CaptureSize, chunksNumX, chunksNumY);
			forceStandbyColorGrid = true;
		}
	}

	private static void capture() throws Exception // capture a selected screen area
	{
		BufferedImage screenshot = new Robot().createScreenCapture(CaptureSize);
		chunking(screenshot);
	}
	
	private static void chunking(BufferedImage screenshot) throws Exception // split the image in serval chunks
	{		
		int chunkResX = screenshot.getWidth() / chunksNumX;
		int chunkResY = screenshot.getHeight() / chunksNumY;
		
		BufferedImage[] chunks = new BufferedImage[chunksNumX * chunksNumY];
		
		int id = 0;
		for (int x = 0; x < chunksNumX; x++)
		{
			for (int y = 0; y < chunksNumY; y++)
			{
				chunks[id] = screenshot.getSubimage(chunkResX * x, chunkResY * y, chunkResX, chunkResY);
				id++;
			}
		}
		
		avgChunks(chunks);
	}
	
	private static void avgChunks(BufferedImage[] chunks) throws Exception // get average color of each chunk
	{
		Color[] avgColors = new Color[chunks.length];
		
		for (int i = 0; i < chunks.length; i++)
		{
			int avgR = 0;
			int avgG = 0;
			int avgB = 0;
			
			for (int x = 0; x < chunks[i].getWidth(); x++)
			{
				for (int y = 0; y < chunks[i].getHeight(); y++)
				{
					Color pColor = new Color(chunks[i].getRGB(x, y));
					avgR += pColor.getRed();
					avgG += pColor.getGreen();
					avgB += pColor.getBlue();
				}
			}
			
			avgR = avgR / (chunks[i].getWidth() * chunks[i].getHeight());
			avgG = avgG / (chunks[i].getWidth() * chunks[i].getHeight());
			avgB = avgB / (chunks[i].getWidth() * chunks[i].getHeight());
			avgColors[i] = new Color(avgR, avgG, avgB);
		}
		
		if (Main.ui.cpi.frame.isVisible())
		{
			drawColorGrid(avgColors);
		}
		
		analyze(avgColors);
	}
	
	private static void drawColorGrid(Color[] ColorContainer) // draw the chunks in the color grid interface
	{
		if (forceStandbyColorGrid == false)
		{
			int ChunkResX = (int)(CaptureSize.getWidth() / 3) / chunksNumX;
			int ChunkResY = (int)(CaptureSize.getHeight() / 3) / chunksNumY;
			
			BufferedImage b = new BufferedImage(ChunkResX * chunksNumX, ChunkResY * chunksNumY, BufferedImage.TYPE_INT_RGB);
			Graphics g = b.createGraphics();
			
			int i = 0;
			for (int x = 0; x < chunksNumX; x++)
			{
				for (int y = 0; y < chunksNumY; y++)
				{
					g.setColor(ColorContainer[i]);
					g.drawRect(x * ChunkResX, y * ChunkResY, ChunkResX, ChunkResY);
					g.fillRect(x * ChunkResX, y * ChunkResY, ChunkResX, ChunkResY);
					i++;
				}
			}

			Main.ui.cpi.label_Colors.setIcon(new ImageIcon(b));
			Main.ui.cpi.frame.pack();
		}
	}

	private static void analyze(Color[] colorcontainer) throws Exception // analyze all chunks
	{
		float[] temp_color;
		
		float[] avg_color = Color.RGBtoHSB(0, 0, 0, null);
		float[] sat_color = Color.RGBtoHSB(0, 0, 0, null);
		float[] bri_color = Color.RGBtoHSB(0, 0, 0, null);
		float[] dar_color = Color.RGBtoHSB(0, 0, 0, null);
		
		float minSat = 1;
		float maxSat = 0;
		float minBri = 1;
		float maxBri = 0;
		
		int[] avg_rgb = new int[3];
		for (Color color : colorcontainer) // get average color
		{
			temp_color = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
			
			if (temp_color[1] <= minSat)
			{
				minSat = temp_color[1];
			}
			if (temp_color[1] >= maxSat)
			{
				maxSat = temp_color[1];
			}
			if (temp_color[2] <= minBri)
			{
				minBri = temp_color[2];
			}
			if (temp_color[2] >= maxBri)
			{
				maxBri = temp_color[2];
			}
			
			avg_rgb[0] += color.getRed();
			avg_rgb[1] += color.getGreen();
			avg_rgb[2] += color.getBlue();
		}		
		avg_rgb[0] = avg_rgb[0] / colorcontainer.length;
		avg_rgb[1] = avg_rgb[1] / colorcontainer.length;
		avg_rgb[2] = avg_rgb[2] / colorcontainer.length;
		avg_color = Color.RGBtoHSB(avg_rgb[0], avg_rgb[1], avg_rgb[2], null);

		for (Color color : colorcontainer) // get max/min sat/bri
		{
			temp_color = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
			
			if (temp_color[2] >= ((maxBri - minBri) / 2) + minBri)
			{
				bri_color[0] = (bri_color[0] + temp_color[0]) / 2;
				bri_color[1] = (bri_color[1] + temp_color[1]) / 2;
				bri_color[2] = (bri_color[2] + temp_color[2]) / 2;
			}

			if (temp_color[2] <= ((maxBri - minBri) / 2) + minBri)
			{
				dar_color[0] = (dar_color[0] + temp_color[0]) / 2;
				dar_color[1] = (dar_color[1] + temp_color[1]) / 2;
				dar_color[2] = (dar_color[2] + temp_color[2]) / 2;
			}
			
			
			if (temp_color[1] >= sat_color[1] && temp_color[2] >= ((maxBri - minBri) / 3 + minBri)) // ((maxBri - minBri) / 2 + minBri))
			{
				sat_color = temp_color;
			}
			/*if (temp_color[1] >= sat_color[1] && temp_color[2] >= avg_color[2])
			{
				sat_color = temp_color;
			}*/
			
		}

		Color[] extrColor = new Color[algorithms];
		extrColor[0] = Color.getHSBColor(sat_color[0], sat_color[1], sat_color[2]);
		extrColor[1] = Color.getHSBColor(bri_color[0], bri_color[1], bri_color[2]);
		extrColor[2] = Color.getHSBColor(dar_color[0], dar_color[1], dar_color[2]);
		extrColor[3] = Color.getHSBColor(avg_color[0], avg_color[1], avg_color[2]);
		
		setLightColor(extrColor);
	}
	
	private static void setLightColor(Color[] extrColor) throws Exception // distribute the colors to the lights
	{
		for (HLight light : HBridge.lights)
		{
			String uniqueid = light.uniqueid;
			boolean active = Settings.Light.getActive(uniqueid);
			int alg = Settings.Light.getAlgorithm(uniqueid);
			
			if(active == true)
			{
				Main.hueControl.setLight(light, extrColor[alg]);
			}
		}
	}
}
