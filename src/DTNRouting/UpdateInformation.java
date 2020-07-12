// PACKAGE NAME
package DTNRouting;

//IMPORT PACKAGES
import Results.*;
import RoutingProtocols.RoutingProtocol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.io.IOException;
import java.util.Random;




//******************************************************************************
// CLASS MADE FOR UPDATING THE INFORMATION DURING SIMULATION

public class UpdateInformation {
    //Instance Variables
	RoutingProtocol rp;
    RP_Performance rpp=new RP_Performance();
    Random rand;
    PrintWriter writer=null;
    private StringBuilder sb = new StringBuilder();
    private static DecimalFormat df2 = new DecimalFormat("#.##");

//******************************************************************************
//Constructor
public UpdateInformation(){
}


//******************************************************************************
//Update TTL and packet Latency
public void UpdateTTLandLatency()
{   
	  //1. Update simulation timer
      df2.setRoundingMode(RoundingMode.DOWN);
	  dtnrouting.timer+=1;
      
	  for(int h=0;h < dtnrouting.arePacketsDelivered.size();h++)
      {   Packet packetObj=dtnrouting.arePacketsDelivered.get(h);
          packetObj.packetTTL-=1;
          
          if(packetObj.isTTLExpired == false && packetObj.ispacketDelivered==false) {
                  packetObj.packetLatency=(int) dtnrouting.timer;      
          if(packetObj.packetTTL==0){
            //if packet's TTL expires, it cannot be delivered else if
            packetObj.isTTLExpired=true;
            dtnrouting.total_packetsDeliveredExpired += 1;}}}
      
     //2. When packets are delivered or expired
	 //   store summary of results
	 //   stop simulation temporarily
     if(dtnrouting.total_packetsDeliveredExpired==dtnrouting.arePacketsDelivered.size() 
     & dtnrouting.arePacketsDelivered.size()!=0)
     {
         dtnrouting.deliveryTA.setText("");
    	 dtnrouting.deliveryTA.setText("PACKET DEDLIVER || EXPIRED");
	      // STOP SIMULATION 

	      for(int h=0;h<dtnrouting.Destinations.size();h++)
	      {
	    	  Node destNode = dtnrouting.Destinations.get(h);
	    	  double expired = 0;
	    	  for(int m=0; m < destNode.nodePackets.size(); m++)
	    	  {
	    		  destNode.msg_latency+=destNode.nodePackets.get(m).packetLatency;
	    		  destNode.msg_hops+=destNode.nodePackets.get(m).packetHops;  
	    		  destNode.msg_relibility+=destNode.nodePackets.get(m).packetReliability;
	    		  if(destNode.nodePackets.get(m).ispacketDelivered) 
	    		    destNode.msg_dl+=1;
	    		  if(destNode.nodePackets.get(m).isTTLExpired)
	    			  expired +=1;
	    	  }
	    	  
	    	  destNode.msg_latency = (double)destNode.msg_latency / destNode.num_packets;
	    	  destNode.msg_hops = (int)destNode.msg_hops / destNode.num_packets;
	    	  destNode.msg_dl =  (double)destNode.msg_dl / destNode.num_packets;
	    	  destNode.msg_relibility = (double)destNode.msg_relibility / destNode.num_packets;
	  	      
	    	  if(sb.length()==0)
	    	  sb.append("simulation_run, type, dest,  latency, hop , delivery, failure, reliability\n");
			  
	  	      sb.append(
	  	      (dtnrouting.TOTAL_SIMULATION_RUNS-dtnrouting.SIMULATION_N0 +1) + ", " +
			  dtnrouting.SIMULATION_PART + "," + (h+1) +  ", " +
	  	      df2.format(destNode.msg_latency)+ ", " +
			  destNode.msg_hops + ", " +
			  df2.format(destNode.msg_dl)+  ", " +
			  df2.format(expired/destNode.nodePackets.size())+  ", " +
			  df2.format(destNode.msg_relibility)+"\n");
	  	      dtnrouting.deliveryTA.append("\n"+destNode.name+", dl: "+destNode.msg_dl);
	  	   
	      }
	      
	        dtnrouting.THIS_SIMULATION_ENDED=true;
	    
	        // Add to file 
	    	//First simulation run
	    	if(dtnrouting.SIMULATION_N0==dtnrouting.TOTAL_SIMULATION_RUNS) {
	    	     try {
	    	     writer = new PrintWriter(new File("/Users/mahrukh/manet_results.csv"));
	    	      } catch (FileNotFoundException e) {
	    	            e.printStackTrace();}}

	    	//Last simulation run 
	    	if(dtnrouting.SIMULATION_N0==1) {
	    		writer.write(sb.toString());
	    		writer.flush();
	    		writer.close();}}
}

//******************************************************************************

public void nextPositionForMovement() throws IOException
{
	    //NODE MOVEMENT
		if(dtnrouting.movementtype.equals("Random"))
	    for(int i=0; i< dtnrouting.allNodes.size();i++)
	    	     dtnrouting.allNodes.get(i).node_nm.RandomMovement(dtnrouting.allNodes.get(i));
	
	    else if(dtnrouting.movementtype.equals("Pseudorandom"))
	    	 for(int i=0; i< dtnrouting.allNodes.size();i++)
	    		 dtnrouting.allNodes.get(i).node_nm.Follow_PseudoRandomPath(dtnrouting.allNodes.get(i));
	   
	    else if(dtnrouting.movementtype.equals("Dataset"))
	    	 for(int i=0; i< dtnrouting.allNodes.size();i++)
	    		 dtnrouting.allNodes.get(i).node_nm.Follow_DatasetPath(dtnrouting.allNodes.get(i));
	
}

//******************************************************************************
//Clear all the settings when clear (eraser) button is clicked

public void clearSettings()
{
	 
        dtnrouting.allNodes.clear();
        Node.ID_INCREMENTER=0;
        dtnrouting.timer=0;
        dtnrouting.total_packetsDeliveredExpired=0;
        
        //Clearings the array lists of source, destination, their packets and their parameter
        dtnrouting.Sources.clear();     
        dtnrouting.Destinations.clear();
        //Set movement model to null
        dtnrouting.movementtype="Random";
        dtnrouting.arePacketsDelivered.clear();
        dtnrouting.SIMULATION_N0 = dtnrouting.TOTAL_SIMULATION_RUNS;
        Packet.packetID=0; 
               
        //Empty Text areas
        dtnrouting.sdpTA.setText("Source    Dest.    packet");
        dtnrouting.contactsTA.setText("");
        dtnrouting.transferTA.setText("");
        dtnrouting.deliveryTA.setText("");
        rpp.clearData(); //clear data from table and charts
        dtnrouting.THIS_SIMULATION_ENDED=false;
        dtnrouting.SIMULATION_RUNNING=false;
        
}

//******************************************************************************
//When a simulation run completes
public void simulationSettings(dtnrouting dtn)
{
    if(dtnrouting.SIMULATION_PART==2) {
    	dtnrouting.SIMULATION_N0=dtnrouting.SIMULATION_N0-1;
    	dtnrouting.SIMULATION_PART=1;}
    else
    	dtnrouting.SIMULATION_PART=2;
    

 
    dtnrouting.sdpTA.setText("Source    Dest.    packet");
    dtnrouting.contactsTA.setText("");
    dtnrouting.transferTA.setText("");
    
    
    //...Display the result when all SIMULATIONS END
    if(dtnrouting.SIMULATION_N0==0)  
    	dtnrouting.SIMULATION_RUNNING=false;

   
    //...When a simulation run ends, update the average results
    else if(dtnrouting.SIMULATION_N0>0)  
    {
    	CreateNode cnObj = new CreateNode();
    	QoITPATH pathObj = new QoITPATH ();
    	
    	// WITHOUT UAV PART-----------------
    	if(dtnrouting.SIMULATION_PART==1) {
        	
    	    //Remove packets and destination information from nodes
        	//And change the number of packets and requirements of destinations
        	dtnrouting.arePacketsDelivered.clear();
        	Packet.packetID=0; 
        	for(int g=0;g<dtnrouting.allNodes.size();g++) {
        		dtnrouting.allNodes.get(g).clearNodeSettings();
        		if(dtnrouting.allNodes.get(g).name.substring(0,1).equals("D"))
        		{cnObj.RequirementsofDestination(dtnrouting.allNodes.get(g));
        	     cnObj.PacketsforDestination(dtnrouting.allNodes.get(g));}}
        	
        	//Remove UAV Nodes
        	dtnrouting.allNodes.subList(dtnrouting.uav_index[0], 
        	dtnrouting.uav_index[dtnrouting.uav_index.length]).clear();
	       
           //Take a break of one second
	       try
	         { Thread.sleep(1000);
	         } catch (InterruptedException ex) {}
	
	        // Re-define path for nodes, if pseudo-random and mobile
	        if(dtnrouting.movementtype.equals("Pseudorandom"))
	            for(int i=0; i< dtnrouting.allNodes.size(); i++)
	            if(dtnrouting.allNodes.get(i).name.substring(0,1).equals("R"))
	           	dtnrouting.allNodes.get(i).node_nm.InitializePsuedoPath(dtnrouting.allNodes.get(i));
	        
    	}
    	
    	 // WITH UAV PART-----------------------
         else {  
        	     for(int n=0;n<dtnrouting.allNodes.size();n++)
            		dtnrouting.allNodes.get(n).refreshNodeSettings();
         		 for(int p=0;p<dtnrouting.arePacketsDelivered.size();p++)
         			dtnrouting.arePacketsDelivered.get(p).refreshPacketSettings();
         		 //Add UAV nodes
         		cnObj.CreateUAV();
 
         	}
    			   
		pathObj.ShortestPathsSD();
    	pathObj.SetSource();
	    runSimulation();
    }
}//end of the method

//******************************************************************************

public void runSimulation()
{  
	dtnrouting.total_packetsDeliveredExpired=0;
    dtnrouting.timer=0;
    dtnrouting.THIS_SIMULATION_ENDED=false;
    dtnrouting.SIMULATION_RUNNING=true;
}
//******************************************************************************

} // END OF CLASS
