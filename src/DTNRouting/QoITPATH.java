//PACKAGE NAME
package DTNRouting;

//IMPORT PACKAGES
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


//------------------------------------------------------------------------------
//START OF CLASS packet
public class QoITPATH implements ActionListener
{

	public int num_packets,ttl_packets,size_packets;
	public PlayField pf = new PlayField();
	public shortestPath  sp;
	Random rand=new Random();

	//******************************************************************************

	public  QoITPATH(){}
  

	//******************************************************************************
	public void ShortestPathsSD() {
		int total_nodes = dtnrouting.allNodes.size();
		System.out.println("Nodes size: "+total_nodes +"Senario"+dtnrouting.SIMULATION_PART);
		
		
		// Initialize the adjacency table
		dtnrouting.adjacencyMatrix =new double[total_nodes][total_nodes];
		
		// Initialize the Dynamic TSA arrays
		dtnrouting.RR=new int[total_nodes];
		dtnrouting.CR=new int[total_nodes];
		dtnrouting.RC=new int[total_nodes];
		dtnrouting.RA=new int[total_nodes]; 
		dtnrouting.PP=new int[total_nodes];
		dtnrouting.EP=new int[total_nodes];
		dtnrouting.Result=new int[total_nodes];
		// Calculate Adjacency matrix
		dtnrouting.source_index = new int[dtnrouting.Sources.size()];
		dtnrouting.dest_index = new int[dtnrouting.Destinations.size()];
		int s_counter=0 , d_counter=0;

		for (int i = 0; i < (total_nodes-1); i++) {
			dtnrouting.adjacencyMatrix[i][i] = 0;
			if(dtnrouting.allNodes.get(i).name.substring(0,1).equals("S"))
				dtnrouting.source_index[s_counter++] = i;
			else if(dtnrouting.allNodes.get(i).name.substring(0,1).equals("D"))
				dtnrouting.dest_index[d_counter++] = i;
			   // System.out.println("DESTINATION ID IN ORDER:"+ dtnrouting.dest_index[d_counter-1]);
		}
      
		pf.FindNeighborhoods();

		// For last node
		dtnrouting.adjacencyMatrix[total_nodes-1][total_nodes-1]=0;
		if(dtnrouting.allNodes.get(total_nodes-1).name.substring(0,1).equals("S"))
			dtnrouting.source_index[s_counter++] = total_nodes-1;
		else if(dtnrouting.allNodes.get(total_nodes-1).name.substring(0,1).equals("D"))
			dtnrouting.dest_index[d_counter++] = total_nodes-1;

		// Find shortest path from each source to each destination
		System.out.println("Path from source nodes");
		for(int s=0; s < dtnrouting.source_index.length; s++) {
			sp = new shortestPath();
			dtnrouting.allNodes.get(dtnrouting.source_index[s]).ptD = new pathToDestination(dtnrouting.dest_index.length);
			sp.runDijkstra(dtnrouting.adjacencyMatrix, dtnrouting.dest_index , dtnrouting.source_index[s], dtnrouting.allNodes.get(dtnrouting.source_index[s]));
			System.out.println(dtnrouting.allNodes.get(dtnrouting.source_index[s]).ptD.paths);			
			sp=null;}
		
		
		
		BestPathsSD();
	}// End of Method
	
	//******************************************************************************
	
	
	
	 public void BestPathsSD() {
		
		//1. Destination-wise Source-Destination Paths i.e. s1:s2:s3..:sn-->d1, s1:s2:s3..:sn-->d2, ...s1:s2:s3..:sn-->dn
		ArrayList<ArrayList<ArrayList<Integer>>> srcDestPaths_All = new ArrayList<ArrayList<ArrayList<Integer>>>();
		ArrayList<ArrayList<Integer>> srcDestPaths = null;

		for(int dest_index = 0; dest_index < dtnrouting.dest_index.length; dest_index++) {
			srcDestPaths = new ArrayList<ArrayList<Integer>>();
			for(int s = 0; s < dtnrouting.source_index.length; s++) {
			if((dtnrouting.allNodes.get(dtnrouting.source_index[s]).ptD.paths.get(dest_index).size() - 1) != 0)
					srcDestPaths.add(dtnrouting.allNodes.get(dtnrouting.source_index[s]).ptD.paths.get(dest_index));
			}
			srcDestPaths_All.add(srcDestPaths);}
		    System.out.println("All: " + srcDestPaths_All);
		    

		
		    //2. Exploring Network Metric Values possessed by particular Src-Dst path AND 
		    // Evaluating goodness value of a metric i.e. percentage of user need met w.r.t a metric
		    // Choose source for each destination node
		    for(int dest_index = 0; dest_index < dtnrouting.dest_index.length; dest_index++) {
		    System.out.print("For destination:"+dtnrouting.dest_index[dest_index]+", ");
			if(srcDestPaths_All.get(dest_index).size() != 0) {
				
				// row: no. of available sources; [column: no. of network metrics; 0 = HC; 1 = BW; 2 = PI] 
				double networkMetricValues[][] = new double[srcDestPaths_All.get(dest_index).size()][3];
				double goodnessValue_wrtNetworkMetrics[][] = new double[srcDestPaths_All.get(dest_index).size()][3];


				for(int source_index = 0; source_index < srcDestPaths_All.get(dest_index).size(); source_index++) {
					int hopCount = srcDestPaths_All.get(dest_index).get(source_index).size() - 1;
					
					//dtnrouting.allNodes.get(srcDestPaths_All.get(dest_index).get(source_index).get(hopCount)).ID;
					
					//System.out.println("Destination Node1: " + destID);
					//System.out.println("Destination Node2: " + dtnrouting.dest_index[dest_index]);
					// Exploring Network Metric Values possessed by particular Src-Dst route
					if(hopCount != 0) {	
						networkMetricValues_AlongPath nmv = new networkMetricValues_AlongPath();
						networkMetricValues[source_index][0] = hopCount;
						networkMetricValues[source_index][1] = nmv.getPathBandwidth(srcDestPaths_All.get(dest_index).get(source_index));
						networkMetricValues[source_index][2] = nmv.getPathReliability(srcDestPaths_All.get(dest_index).get(source_index));
					}
			 
					int destID = dtnrouting.dest_index[dest_index];
					// Evaluating goodness value of a metric i.e. percentage of user need met w.r.t a metric
					/*
					 * System.out.println(); System.out.println("Network Metric Reqs.: " +
					 * dtnrouting.allNodes.get(destID).neworkMetricRequirements[0] + ", " +
					 * dtnrouting.allNodes.get(destID).neworkMetricRequirements[1] + ", " +
					 * dtnrouting.allNodes.get(destID).neworkMetricRequirements[2]);
					 */		
					//Hop-Count
					goodnessValue_wrtNetworkMetrics[source_index][0] = 
					(networkMetricValues[source_index][0] <= 
					dtnrouting.allNodes.get(destID).neworkMetricRequirements[0])? 
					1:dtnrouting.allNodes.get(destID).neworkMetricRequirements[0]/ 
					networkMetricValues[source_index][0];
				   
					// Bandwidth
					goodnessValue_wrtNetworkMetrics[source_index][1] =
					(networkMetricValues[source_index][1] >= 
					dtnrouting.allNodes.get(destID).neworkMetricRequirements[1])?
					1:networkMetricValues[source_index][1] / 
					dtnrouting.allNodes.get(destID).neworkMetricRequirements[1];
					
					// Path-Integrity or Reliability
					goodnessValue_wrtNetworkMetrics[source_index][2] = 
				    (networkMetricValues[source_index][2] >= 
				    dtnrouting.allNodes.get(destID).neworkMetricRequirements[2])?
					1: networkMetricValues[source_index][2] / 
					dtnrouting.allNodes.get(destID).neworkMetricRequirements[2];
					

					/*
					 * System.out.println("Source " +
					 * dtnrouting.allNodes.get(srcDestPaths_All.get(dest_index).get(source_index).
					 * get(0)).ID + ":"); System.out.println("Network Metric Values: " +
					 * networkMetricValues[source_index][0] + ", " +
					 * networkMetricValues[source_index][1] + ", " +
					 * networkMetricValues[source_index][2]); System.out.println("Goodness Value: "
					 * + goodnessValue_wrtNetworkMetrics[source_index][0] + ", " +
					 * goodnessValue_wrtNetworkMetrics[source_index][1] + ", " +
					 * goodnessValue_wrtNetworkMetrics[source_index][2]); System.out.println();
					 */		}
				// Choose best source for each user/destination
				//update source dest pair array
				SourceSelection ss = new SourceSelection();
			    dtnrouting.destsourcePair[dest_index] = ss.selectSource(goodnessValue_wrtNetworkMetrics, 
			    		             srcDestPaths_All.get(dest_index));

			    }}
} //End Method
			
//******************************************************************************	

public void SetSource() {
       
     // For each dest and source pair
	 for(int d = 0; d < dtnrouting.dest_index.length; d++) {
	
			//System.out.println("\nS->D:(" + (dtnrouting.destsourcePair[d]) + "->"+ 
			//(dtnrouting.dest_index[d])+") ");
			

			//Destination chooses number of packets randomly
			Node destNode = dtnrouting.allNodes.get(dtnrouting.dest_index[d]);
			Node sourceNode = dtnrouting.allNodes.get(dtnrouting.destsourcePair[d]);
			
			//stores the path a packet will take from the shortest path from its source to destination
			// Stores them in its selected source buffer
			for(int j=0; j< destNode.nodePackets.size(); j++) {//number of packets that each source will transmit..
				Packet packetObj = destNode.nodePackets.get(j);
				
				// Packet will follow the shortest path from source too destination
				for(int c=0; c < sourceNode.ptD.paths.get(d).size(); c ++) {
					if(sourceNode.ptD.paths.get(d).get(c)==(-1)) {
						break; // store no path for packet
					}
					else {
						packetObj.pathHops.add(dtnrouting.allNodes.get(sourceNode.ptD.paths.get(d).get(c)));}}
					

				// Store packet to inside source buffer
				if(sourceNode.queueSizeLeft > packetObj.packetSize)
				  {    
					sourceNode.queueSizeLeft-=packetObj.packetSize; //update queue space after putting packet in it
					sourceNode.packetIDHash.add(packetObj.packetName); //Store ID of packet in the source as Hash value
					//sourceNode.packetTimeSlots.put(p.packetName,0);
					sourceNode.packetCopies.put(packetObj.packetName,1);
					// these packets have no path
					if(packetObj.pathHops.size()==0) {
						sourceNode.DestNPacket.put(packetObj,null);
						sourceNode.number_packet_arrived+=1;}
					else
						sourceNode.DestNPacket.put(packetObj,destNode);
					//System.out.println("SOURCE: "+sourceNode.name+", DEST: "+destNode.name);
					dtnrouting.sdpTA.append("\n "+sourceNode.ID+"--"+destNode.ID+" ("+packetObj.packetName+")");}

			  else    //If queue of the packet has not enough space to store the new packet then
					dtnrouting.sdpTA.append("\nSource "+ sourceNode.ID+" has not enough space to occupy "+packetObj.packetName);  
			   }  //all packets of the destination assigned to the source of dest.     
		}  

	} // End of Method

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
    }

	//******************************************************************************
}//END OF packet CLASS
