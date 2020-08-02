//PACKAGE NAME
package DTNRouting;

//IMPORT PACKAGES
import java.util.*;

import RoutingProtocols.RoutingProtocol;

import java.awt.*;
import java.awt.geom.*;


//******************************************************************************
//START OF THE CLASS PLAYFIED, DISPLAYING MOVING NODES AND REGIONS/MAP

public class PlayField extends RoutingProtocol
{
	//Instance Variables
	static boolean hasDeliverCalled[][]=new boolean[dtnrouting.allNodes.size()][dtnrouting.allNodes.size()];
	static dtnrouting dtn=new dtnrouting();	

	//******************************************************************************
	//EMPTY CONSTRUCTOR

	public PlayField() {}


	//******************************************************************************
	//DRAW NODES ALONG WITH THEIR packetS IN THE PLAYFIELD OF APPLET

	public void drawNodesPackets(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g.setFont(new Font("Dialog",Font.PLAIN,12));

		//Displaying Nodes and the packet that they hold
		for (int k=0;k<dtnrouting.allNodes.size();k++)
		{
			//Access one node at a time from its array list
			Node n=new Node();
			n=dtnrouting.allNodes.get(k);
			int r=n.getRadioRange();  //set the size of nodes
			g2.setStroke(new BasicStroke(3));
			g.setColor(Color.black);

			//Drawing nodes of different names with different colors
			if(n.name.substring(0,1).equals("R"))         g2.setPaint(Color.YELLOW);
			else if(n.name.substring(0, 1).equals("D"))   g2.setPaint(Color.BLUE);
			else if(n.name.substring(0, 1).equals("S"))   g2.setPaint(Color.RED);
			else if(n.name.substring(0, 1).equals("U"))   g2.setPaint(Color.GRAY);

			Ellipse2D e = new Ellipse2D.Double(n.location.x, n.location.y, r, r);
			e.setFrame(n.location.x - r, n.location.y - r, 2*r, 2*r);
			g2.draw(e);
			//g.fillOval(n.location.x - r,  n.location.y - r, 2 * r, 2 * r);

			//Put name of node inside node circle
			g.setColor(Color.black);
			g.drawString(n.ID+"", (int)(n.location.x), (int)(n.location.y));

			//Show whether packet is present: for one packet only
			if(!n.DestNPacket.isEmpty())
			{
				Set<Packet> setPacket=n.DestNPacket.keySet();
				Iterator<Packet> it=setPacket.iterator();
				int x=(int)(n.location.x+r/2-6);
				int y=(int)(n.location.y+r/2-1);
				while(it.hasNext())
				{
					Packet packetObj=(Packet)it.next();
					if(packetObj.isTTLExpired==true)
						g.setColor(Color.RED);
					else if(packetObj.ispacketDelivered==true)
						g.setColor(Color.GREEN);
					else
						g.setColor(Color.BLUE);
					//g.drawString(packetObj.packetName.substring(1)+",", x-40, y+15);
					g.fillOval(x-40-5, y+15-5, 10, 10);

					
					//If node has more than one packet then next packet is displayed
					//near the earlier one in the same node
					x=x+11;
				} //End of while loop
			}

			g.setColor(Color.gray);
			//divide it by two so that the text comes in the mid of the node
		} //End of if statement
	}

	//******************************************************************************
	//FIND WHETHER A CONTACT IS PRESENT BETWEEN ANY PAIR OF NODES

	double FindIntersection(Node ni,Node nj) //to find the intersection between nodes
	{

		//******************************************************
		//mid point and radius of ni
		double x1 = ni.location.x;
		double y1 = ni.location.y;
		double r1 = ni.getRadioRange();//(ni.getRadioRange())/2;

		//mid point and radius of nj
		double x2 = nj.location.x;
		double y2 = nj.location.y;
		double r2 = nj.getRadioRange();//(nj.getRadioRange())/2;

		double distance_km = Math.sqrt(Math.pow((y2-y1),2) + Math.pow((x2-x1),2));
		double r = r1 + r2;

	
		if(distance_km <= r) { 
			double dist_min = 1.5, dist_max = 6.4345, range_min = 0 , range_max = r;
			distance_km = ((distance_km - range_min) / (range_max - range_min)) * (dist_max - dist_min) + dist_min;
			//System.out.println(distance_km+ "/"+ getLinkCapacity(distance_km));
			return getLinkCapacity(distance_km);}
	
		else return 0.0;	
	}
	
	//*********************************************************
	// Five phase reservation protocol	
	public void FivePhaseReservationProtocol()
	{	
	// 1. Identify nodes with messages---prob. (0.5)	
	// FINAL Result:0->IDLE, 1=BLOCK, 2=RECIEVE, 3=TRANSMIT
	// 2. Initially RR [] = (-1) for all nodes
	//Each node with a msg to transfer, should updates its value to "t=0" in its RR array and
	// updates it neighboring nodes with r=1, If a node receives multiple 'r',its RR >1
	// RR[i] = 
	 for (int a = 0; a < dtnrouting.allNodes.size(); a++) {
			Node ni = dtnrouting.allNodes.get(a);
	        int relay_packets = ni.packetIDHash.size()-ni.number_packet_arrived;
	        
	        double prob = new Random().nextDouble();
			if(relay_packets > 0 & prob>=0.5) {
				System.out.println(ni.ID+" PACKETS:"+ni.packetIDHash.size()+", ARRIVED:"+ni.number_packet_arrived);
				//Move forward only if it has not received
				//an RR from its neigbour node
				if(dtnrouting.RR[ni.ID-1]==-1) {
				dtnrouting.RR[ni.ID-1] = 0;
			    for(int p =0; p <  ni.n1_neighborhood.size(); p++) {
				Node nj = dtnrouting.allNodes.get(ni.n1_neighborhood.get(p));
				if(dtnrouting.RR[nj.ID-1]==(-1)) dtnrouting.RR[nj.ID-1] = 1;
				else							 dtnrouting.RR[nj.ID-1] += 1;}}}}
			
	// 3. Initially CR[] = (-1) for all nodes	
	// If a node's RR value > 1, it updates its CR  value  to t=0, and its neighbor CR value to r=1
	// Nodes with CR value > (-1) are considered IDLE=I
	 for (int a = 0; a < dtnrouting.allNodes.size(); a++) {
			Node ni = dtnrouting.allNodes.get(a);
			if(dtnrouting.RR[ni.ID-1] > 1) {
				dtnrouting.CR[ni.ID-1] = 0;
				dtnrouting.Result[ni.ID-1]=0; //INDICATION FOR IDLE STATE
			    for(int p =0; p <  ni.n1_neighborhood.size(); p++) {
				Node nj = dtnrouting.allNodes.get(ni.n1_neighborhood.get(p));
				dtnrouting.CR[nj.ID-1] = 1;
				dtnrouting.Result[nj.ID-1]=0; //INDICATION FOR IDLE STATE;
				}}}
	
	// *****IF A NODE HAS RR =0 AND CR=-1, IT BECOMES A TRANSMITTING NODE TN***********	
	// 4. Initially RC [] = (-1) for all nodes
	// If a node with RR value 0 and CR value = -1, it sets its RC to t=0, and its neighbor RC to r=1
	 for (int a = 0; a < dtnrouting.allNodes.size(); a++) {
			Node ni = dtnrouting.allNodes.get(a);
			if(dtnrouting.RR[ni.ID-1] == 0 & dtnrouting.CR[ni.ID-1] ==(-1)) {
				dtnrouting.TransferNodes.add(ni);
				dtnrouting.RC[ni.ID-1]=0;
			    for(int p =0; p <  ni.n1_neighborhood.size(); p++) {
					Node nj = dtnrouting.allNodes.get(ni.n1_neighborhood.get(p));
					dtnrouting.RC[nj.ID-1] = 1;}}}

	// 5. Initially RA [] = -1  for all nodes
	// If a node has its RC value = 1, it sets its RA value t=0 and neighbor RA to r=1
	 for (int a = 0; a < dtnrouting.allNodes.size(); a++) {
			Node ni = dtnrouting.allNodes.get(a);
			if(dtnrouting.RC[ni.ID-1] == 1) {
				dtnrouting.RA[ni.ID-1]=0;
			    for(int p =0; p <  ni.n1_neighborhood.size(); p++) {
					Node nj = dtnrouting.allNodes.get(ni.n1_neighborhood.get(p));
					dtnrouting.RA[nj.ID-1] = 1;}}}
	
	// 6. Initially PP[] = -1 for all nodes
	// A node with RA = 1 and RC = -1, updates its PP to t=0, and its neighbors PP to r=1
	 for (int a = 0; a < dtnrouting.allNodes.size(); a++) {
			Node ni = dtnrouting.allNodes.get(a);
			if(dtnrouting.RA[ni.ID-1] == 1 & dtnrouting.RC[ni.ID-1] == -1) {
				dtnrouting.PP[ni.ID-1]=0;
				dtnrouting.Result[ni.ID-1]=1; //INDICATION FOR BLOCK STATE;
			    for(int p =0; p <  ni.n1_neighborhood.size(); p++) {
					Node nj = dtnrouting.allNodes.get(ni.n1_neighborhood.get(p));
					dtnrouting.PP[nj.ID-1] = 1;
					dtnrouting.Result[nj.ID-1]=0; //INDICATION FOR IDLE STATE;
					}}}
	// 7. Initially EP = [-1]
	// EACH TN node sets its EP to t=0, with prob. 0.5, and sets its neighbors EP to r =0.
	// The node with EP = 0, GETS THE SLOT
	 for (int a = 0; a < dtnrouting.TransferNodes.size(); a++) {
			Node ni = dtnrouting.TransferNodes.get(a);
			double prob = new Random().nextDouble();
			if(dtnrouting.RA[ni.ID-1]==(-1))// THIS IS ISOLATED NODE and must not transmit
				;
			else
			if(prob>=0) {//not yet set it, since we filter out adjacent nodes early
			dtnrouting.EP[ni.ID-1] = 0;
			dtnrouting.Result[ni.ID-1]=3; //INDICATION FOR TRANSMIT STATE;
			
			for(int p =0; p <  ni.n1_neighborhood.size(); p++) {
					Node nj = dtnrouting.allNodes.get(ni.n1_neighborhood.get(p));
					dtnrouting.EP[nj.ID-1] = 1;
					dtnrouting.Result[nj.ID-1]=2; //INDICATION FOR RECIEVE STATE;
			}}}
	 TransferPackets();	
	}
	
	//*********************************************************
	double getLinkCapacity(double distance_km) {

		/* Random rand = new Random();
		double mean_dB = 0.0, sd_dB = 1.0, RandomFading_dB = rand.nextGaussian()*sd_dB + mean_dB;*/
		double Beta = 4.0, thisRate = 0.0, freq_Mhz = 2400;
		//double[] rates = {0.0, 7.2, 14.4, 21.7, 28.9, 43.3, 57.8, 65.0, 72.2};
		double[] rates = {3.0, 7.2, 14.4, 21.7, 28.9, 43.3, 57.8, 65.0, 72.2};
		double[] snrThreshold = {0.0, 2.0, 5.0, 9.0, 11.0, 15.0, 18.0, 20.0, 25.0}; 

		/*if (RandomFading_dB < 0.0) 
			RandomFading_dB = 0.0; // Do not make fading improve signal */

		double PathLoss_db = - 32.45 - Beta * 10 * Math.log10(freq_Mhz * distance_km) /*- RandomFading_dB*/;  
		double Radio_Pwr_dBm = 20.0;
		double rcdPower_dBm  = Radio_Pwr_dBm + PathLoss_db;
		double noise_cuttoff = -180.0;
		double sinr_dB = rcdPower_dBm - noise_cuttoff;	 

		for (int i = 0; i < snrThreshold.length; i++) {
			if (sinr_dB > snrThreshold[i]) 	thisRate = rates[i]; 
			else 							break;
		}	  

		return (thisRate);
	}

	//******************************************************************************
	//Find n1 and n2 neighbors
	
	/**
	 * 
	 */
	public void FindNeighborhoods()
	{
		double capacity;
		// Empty previous linked lists
		for (int i = 0; i < dtnrouting.allNodes.size(); i++) {
			dtnrouting.allNodes.get(i).link_capacity.clear();
			dtnrouting.allNodes.get(i).n1_neighborhood.clear();
			//dtnrouting.allNodes.get(i).n2_neighborhood.clear();
			dtnrouting.contactsTA.setText("");
		}
		
		// Generate n1 and n2_neiborhood
		for (int i = 0; i < (dtnrouting.allNodes.size()-1); i++) {
			dtnrouting.adjacencyMatrix[i][i]=0;
		    //System.out.print("\nNode ("+(i+1)+"): ");
			for(int j = i+1; j < dtnrouting.allNodes.size(); j++) 
				{
					Node ni = dtnrouting.allNodes.get(i);  //node i
					Node nj = dtnrouting.allNodes.get(j);  // node j				
					//If contact is present between nodes in current time stamp
					
					dtnrouting.adjacencyMatrix[i][j] = dtnrouting.adjacencyMatrix[j][i] = 0;
					capacity = FindIntersection(ni, nj);	
					// If two nodes are neighbor, then update the neighborhood information
					if(capacity > 0.0)
					{
						dtnrouting.adjacencyMatrix[i][j] = dtnrouting.adjacencyMatrix[j][i] = (double)(1/capacity);
						dtnrouting.contactsTA.insert(ni.ID+"<-->"+nj.ID+"\n", 0);
						// when new nodes comes into contact then deliver the message
						ni.link_capacity.add(capacity);
						nj.link_capacity.add(capacity);
						//System.out.print("-"+(j+1));
						ni.n1_neighborhood.add(j);
						nj.n1_neighborhood.add(i);
						
						//ni.n2_neighborhood.add(j);
						//nj.n2_neighborhood.add(i);
					}else 
					dtnrouting.adjacencyMatrix[i][j] = dtnrouting.adjacencyMatrix[j][i]=0;}
		}
		dtnrouting.adjacencyMatrix[dtnrouting.allNodes.size()-1][dtnrouting.allNodes.size()-1]=0;
		
		/*Generate n2_neighborhood from n1_neiborhood and
		 allocate time slots (link capacities) according
		 packets with neighboring n2 nodes
		*/
		// for each node in the network
		
		/*
		 * for (int i = 0; i < dtnrouting.allNodes.size(); i++) { Node ni =
		 * dtnrouting.allNodes.get(i);
		 * 
		 * // for each n1 neighbor for(int j = 0; j < ni.n1_neighborhood.size(); j++) {
		 * Node nj = dtnrouting.allNodes.get(ni.n1_neighborhood.get(j)); // find n2
		 * neighbors for(int k = 0; k < nj.n1_neighborhood.size(); k++) { Node nk =
		 * dtnrouting.allNodes.get(nj.n1_neighborhood.get(k)); // If nk is not in n2
		 * linked list yet if(ni.ID != nk.ID &
		 * !ni.n2_neighborhood.contains(nj.n1_neighborhood.get(k)) )
		 * ni.n2_neighborhood.add(nj.n1_neighborhood.get(k)); }} }
		 */			
	

}// Find neighbor hood ended
	
//******************************************************************************

public void TransferPackets()
{
	
	    for (int a = 0; a < dtnrouting.TransferNodes.size(); a++) {
	    
		Node ni = dtnrouting.TransferNodes.get(a);
		System.out.println("TN-all:"+ni.ID);
		if(dtnrouting.Result[ni.ID-1] ==3) {
	    for(int p =0; p <  ni.n1_neighborhood.size(); p++) {
			// Available capacity for link with k n1_neighbor
			ni.capacity= (double)(ni.link_capacity.get(p));
			Node nj = dtnrouting.allNodes.get(ni.n1_neighborhood.get(p));
			
			if((!nj.name.contains("S") & nj.queueSizeLeft > 0) ||
			  (ni.DestNPacket.containsValue(nj)))
				DeliverData(ni, nj);
			}}}
	    
	    
	    // After packets are transfered in the slice 
	    //toggle their packetTransfered to false for next slice
	    for(int d=0; d< dtnrouting.arePacketsDelivered.size(); d++)
	    	dtnrouting.arePacketsDelivered.get(d).packetTransferedinSlice=false;
	   
} //End of Method
// ********************************************************************************

public void DeliverData(Node nx, Node ny)
{
	
	    ArrayList<Packet> dummyDestNPacket = new ArrayList<Packet>();
		//Transfer only one packet to a receiver in a time stamp
	    for (Iterator<Map.Entry<Packet,Node>> i = nx.DestNPacket.entrySet().iterator(); i.hasNext(); )
	     {
	             Map.Entry<Packet,Node> entry = i.next();
	             Packet packetObj = entry.getKey();
	             Node   destNode = entry.getValue();

	             if((packetObj.packetSize >= nx.capacity) ||
	              (ny.queueSizeLeft == 0 & ny.name.contains("R"))) 
	              break;
	            
	             
	            //If destination has not enough size to receive packet
	            //OR if the next destination of packet is not ny
	            //OR if its TTL is expired,  it packet cannot be sent
	           	if(expiredTTL_LargeSize(nx,ny,packetObj)==true);
	           

	            //If destination has enough size to receive packet
	            //and if its TTL is not expired, , it packet can be sent
	            // if contact duration is enough to transfer the message
	            else
	                {
	                    //if ny is destination
	                    if(destNode.equals(ny))
	                    {	deliver_Destination(nx, ny, packetObj);
	                    	//System.out.println(packetObj.packetName+":"+nx.name+"->"+ny.name+" ("+destNode.name+")");
	                    	dummyDestNPacket.add(packetObj);
	                    }
	                    //if ny is not a destination
	                    else if(packetObj.packetSize <= ny.queueSizeLeft)
	                    { 
	                    	deliver_Relay(nx, ny,destNode, packetObj,true);
	                    	//System.out.println(packetObj.packetName+":"+nx.name+"->"+ny.name+" ("+destNode.name+")");
	                    	dummyDestNPacket.add(packetObj);   
	                    } 
						
	                     /*
						 * else {
						 * System.out.println("No space, packet size:"+packetObj.packetSize+", Qsize:"
						 * +ny.queueSizeLeft); }
						 */
	                   break; 
	                 }

	}
	    for(int i=0 ; i< dummyDestNPacket.size(); i++ )
	    nx.DestNPacket.remove(dummyDestNPacket.get(i));
}


//******************************************************************************

}//END OF PLAYFIELD CLASS
