package DTNRouting;

public class Location {

	public double x;
	public double y;

public Location	getMidpoint(Location l2)
	{
	
	Location midpoint = new Location();
	midpoint.x= (this.x + l2.x)/2;
	midpoint.y= (this.x + l2.y)/2;		
		return midpoint;
	}
}
