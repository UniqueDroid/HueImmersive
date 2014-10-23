package hueimmersive;

import java.awt.Color;


public class HColor
{
	public static double[] translate(Color color, Boolean useGammaCorrection) // translate in CIE 1931 colorspace for hue
	{
		// code created with help: https://github.com/PhilipsHue/PhilipsHueSDK-iOS-OSX/commit/f41091cf671e13fe8c32fcced12604cd31cceaf3

	    double[] nColor = new double[3];
	    float red, green, blue;
	    
	    nColor[0] = ((float)color.getRed() / 255);
	    nColor[1] = ((float)color.getGreen() / 255);
	    nColor[2] = ((float)color.getBlue() / 255);
	    
	    if (useGammaCorrection)
	    {
		    if (nColor[0] > 0.04045) 
		    {
		        red = (float) Math.pow((nColor[0] + 0.055) / (1.0 + 0.055), 2.4);
		    } 
		    else 
		    {
		        red = (float) (nColor[0] / 12.92);
		    }

		    if (nColor[1] > 0.04045) 
		    {
		        green = (float) Math.pow((nColor[1] + 0.055) / (1.0 + 0.055), 2.4);
		    } 
		    else 
		    {
		        green = (float) (nColor[1] / 12.92);
		    }

		    if (nColor[2] > 0.04045) 
		    {
		        blue = (float) Math.pow((nColor[2] + 0.055) / (1.0 + 0.055), 2.4);
		    }
		    else
		    {
		        blue = (float) (nColor[2] / 12.92);
		    }
	    }
	    else
	    {
	    	red = (float) (nColor[0] / 12.92);
	    	green = (float) (nColor[1] / 12.92);
	    	blue = (float) (nColor[2] / 12.92);
	    }
	    
	    float X = (float) (red * 0.649926 + green * 0.103455 + blue * 0.197109);
	    float Y = (float) (red * 0.234327 + green * 0.743075 + blue * 0.022598);
	    float Z = (float) (red * 0.0000000 + green * 0.053077 + blue * 1.035763);

	    float x = X / (X + Y + Z);
	    float y = Y / (X + Y + Z);

	    // set default color if float/color is NaN
	    if (Float.isNaN(x) || Float.isNaN(y))
	    {
		    x = 0.31f;
		    y = 0.32f;
	    }
	    
	    double[] xy = new double[2];
	    xy[0] = x;
	    xy[1] = y;
	    
	    return xy;
	}
}
