         /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */     
     
package AdapterPackage;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Results.*;
import DTNRouting.*;
import java.util.Random;
/**
 *
 * @author nil
 */
public class MyActionAdapter implements ActionListener {
dtnrouting dtn;
UpdateInformation updateInfo;
Random rand;
public static String protocol="ContactOblivious", mobilityPattern;
public MyActionAdapter(dtnrouting dtn)
    {
    this.dtn=dtn;
    }


public void actionPerformed (ActionEvent ae)

{
 String buttonname;

 buttonname=ae.getActionCommand();
 
// CREATE PACKET 
 if(buttonname.equals("Packet"))
 {
         CreatePacket packetObj=new CreatePacket();
         packetObj.CreateMessageAtSource();
 }
    
// MOBILITY PATTERN
 if(buttonname.equals("Pseudorandom"))
     dtnrouting.movementtype="Pseudorandom";
 	
 if(buttonname.equals("Dataset"))
     dtnrouting.movementtype="Dataset";
   
// CREATE NODE   
    else if (buttonname.equals("Node"))
    {
   
	   CreateNode cnodeObj=new CreateNode();
	   cnodeObj.GenerateFrame();
    }
 
  
 // CLEAR SIMULATION
    else if (buttonname.equals("Clear"))
    {
      updateInfo=new UpdateInformation();
      updateInfo.clearSettings();
    }

 // RUN SIMULATION
  else if(buttonname.equals("Run"))
  {
   // SET THE SIMULATION DELAY
   dtnrouting.delay=0;
   dtnrouting.SIMULATION_RUNNING=true;
  }
 
 // PERFORMANCE TABLE AND CHART
 if(buttonname.equals("Performance Table"))
    {
      RP_Performance rp=new RP_Performance();
      rp.CreateGUI();
      rp.displayTable();
    }
 if(buttonname.equals("Bar Chart"))
    {
      RP_Performance rp=new RP_Performance();
      rp.CreateGUI();
      rp.displayChart();
    }

 }
}
