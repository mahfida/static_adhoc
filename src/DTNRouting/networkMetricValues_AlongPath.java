package DTNRouting;

import java.util.*;

public class networkMetricValues_AlongPath {
	
	networkMetricValues_AlongPath() { }

	public int getPathReliability(ArrayList<Integer> srcDst_pathNodes) {
		int pathReliability = Integer.MAX_VALUE;
		
		for(int i = 0; i < srcDst_pathNodes.size(); i++) {
			Node node = dtnrouting.allNodes.get(srcDst_pathNodes.get(i));
			//System.out.println(node.ID + ": " + node.reliability);
			
			if(node.reliability < pathReliability)
				pathReliability = node.reliability;
		}

		//System.out.println("Path Reliability = " + pathReliability);
		return pathReliability;		
	}
	
	public double getPathBandwidth(ArrayList<Integer> srcDst_pathNodes) {
	
		double pathBandwidth = Double.MAX_VALUE, bandwidth = 0.0;
		
		for(int i = 0; i < srcDst_pathNodes.size()-1; i++) {
			Node from_Node = dtnrouting.allNodes.get(srcDst_pathNodes.get(i)); 
			Node to_Node   = dtnrouting.allNodes.get(srcDst_pathNodes.get(i+1)); 
			bandwidth = 1/dtnrouting.adjacencyMatrix[from_Node.ID-1][to_Node.ID-1];
			//System.out.println(from_Node.ID-1 + "-" + (to_Node.ID-1) + ": "+ bandwidth);
			
			if(bandwidth < pathBandwidth)
				pathBandwidth = bandwidth;
		}

		//System.out.println("Path Bandwidth = " + pathBandwidth);
		return pathBandwidth;
	}
}
