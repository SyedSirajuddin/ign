import java.io.*;
import java.math.*;

public class PingPong {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
			System.out.println("This program finds the number of ping pong balls that can " +
					"fit inside a rectangular box(bus) using two different types of" +
					"orientations- stacked balls and close fitted balls (read additional "+
					"documantation for further details\n");
			System.out.println("Please enter in the diameter of the ping Pong balls:");
		
			double d = Double.parseDouble(in.readLine());
			
			System.out.println("Please enter the width of the bus:");
			
			double w = Double.parseDouble(in.readLine());
			
			System.out.println("Please enter the lenght of the bus:");
			
			double l = Double.parseDouble(in.readLine());
			
			System.out.println("Please enter the height of the bus:");
			
			double h = Double.parseDouble(in.readLine());
			
			int simple = simpleCalc(d, w, l, h);
			
			int complex = complexCalc(d, w, l, h);
			
			System.out.println("Max number of balls is " + Math.max(simple, complex));

			
			System.out.println("Complex result: " + complex);
			System.out.println("Simplex result: " + simple);
			
			
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			System.err.println("Invalid number");
			System.exit(-1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static int simpleCalc(double d, double w, double l, double h) {
		
		return (int)(Math.floor(w/d)*Math.floor(l/d)*Math.floor(h/d));
	}
	
	private static int complexCalc(double d, double w, double l, double h) {
		
		int bh = (int)Math.floor( ((h-d)/(Math.sqrt(3)*d/2))+1 );
		
		int bw = (int)Math.floor(w/d);
		
		int bl = (int)Math.floor(l/d);
		
		int s = 0;
		
		if((w-(bw*d)) >= (d/2))
			s = bh*bw;
		else {
			s = ( ((bh+(bh%2))/2)*bw ) + ((bh-(bh%2))/2)*(bw-1);
		}
		
		if((l-(bl*d)) >= (d/2))
			return s*bl;
		else {
			return ( (s*(bl-1))+ ((bh+(bh%2))/2)*(bw) );
		}
		
	}

}
