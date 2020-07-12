package DTNRouting;

import java.util.*;;

public class SourceSelection {
	Random rand=new Random();
	
	SourceSelection() { }

	public int selectSource(double[][] goodnessValue_wrtNetworkMetrics, 
			ArrayList<ArrayList<Integer>> sourcePaths_toDestination) {

		double average_goodness_Measure[] = new double[sourcePaths_toDestination.size()];

		// Take average of the summed individual goodness measure
		for(int i = 0; i < sourcePaths_toDestination.size(); i++) {
			for(int no_metrics = 0; no_metrics < 3; no_metrics++)
				average_goodness_Measure[i] += goodnessValue_wrtNetworkMetrics[i][no_metrics];
			average_goodness_Measure[i] = (double) average_goodness_Measure[i] / 3;
		}
		
		//for(int i = 0; i < sourcePaths_toDestination.size(); i++)
		//	System.out.println(average_goodness_Measure[i]);

		// Select source based on highest average goodness measure
		double max_goodnessMeasure = 0.0;
		int selectedSource = -1, index = -1;
		for(int i = 0; i < sourcePaths_toDestination.size(); i++) {
			if(average_goodness_Measure[i] > max_goodnessMeasure) {
				max_goodnessMeasure = average_goodness_Measure[i];
				selectedSource = sourcePaths_toDestination.get(i).get(0);
				index = i;
			}
			else if(average_goodness_Measure[i] == max_goodnessMeasure && rand.nextBoolean() == true) {
				max_goodnessMeasure = average_goodness_Measure[i];
				selectedSource = sourcePaths_toDestination.get(i).get(0);
				index = i;
			}		
		}
		
		System.out.println(" selected source: " + selectedSource + " has a score of: " 
		+ average_goodness_Measure[index]);


		return selectedSource;
	}
	/*public  LinkedList<Object> sourceSelection_QoIT(int noofcriteria, int noofsources, int totalNetworkNodes, int[] informationSourceList, int destinationNode, Graph graph, classNode[] nodes, classLink[] links, int[] nodeKeys, 
			WriteFile SrcDstPath_Data_QoIT, String current_time, int runStep, LinkedList<LinkedList<List<classNode>>> shortestPathsArray_forSelectedSources_toAllDestinations, 	int fileNumber, HashMap<String, classLink> link_CAU_QoIT, 
			HashMap<Integer, ApplicationDataRates> applicationDataRate, HashMap<String, NetworkMetricThresholdValues> networkMetricThresholdValues, double[] relationshipWeights, double[] QoIMetricPairwiseComparisons, 
			WriteFile computationOverhead_QoIT_Data, HashMap<Integer, selectedSourceInformation> selectedSourceList_QoIT, boolean flag_updatedInfo, Map<Integer, LinkedHashSet<Integer>> srcDstRouteMap_QoIT_updated, 
			Map<Integer, LinkedHashSet<Integer>> srcDstRouteMap_QoIT, HashMap<Integer, QualityMetricScores> QMS_QoITwrtQoIT_temp, HashMap<Integer, networkData_HashMaps> pnp_SelectedSource_QoITwrtQoIT, 
			HashMap<Integer, networkData_HashMaps> qms_SelectedSource_QoITwrtQoIT, double[] QoICriteriaWeights, double[] priority_of_QualityMetrics, int iteration, WriteFile QoITSelectedSource_QoIScore_Data,
			WriteFile QoITSelectedSource_QoITScore_Data) throws Exception {

		int[] infSourceList = new int[informationSourceList.length]; 
		int sourceIndex=-10, selectedSource_QoIT=-10, hopCount, Noof_QualityMetrics_Met[] = new int[noofsources]; // Number of metrics met
		double networkMetricValues_PercentageFulfilled[][] = new double[noofsources][noofcriteria], qualityMetricValues[][] = new double[noofsources][noofcriteria], SourceQoITScore[] = new double[noofsources], PriorityScore[] = new double[noofsources];
		double qualityMetricScores[][] = new double[noofsources][noofcriteria], communicationOverheadTime = 0.0, NetworkMetricValues[][] = new double[noofcriteria][noofsources], SourceComparisonMatrixWRTNetworkMetric[][] = new double[noofsources][noofsources];
		NetworkMetricThresholdValues nmtv = networkMetricThresholdValues.get(Integer.toString(destinationNode));
		LinkedList<Object> obj = new LinkedList<Object>();
		LinkedHashSet<Integer> srcDstRouteNodes = new LinkedHashSet<Integer>();

		for (int i = 0; i < noofsources; i++) { PriorityScore[i] = 0;  Noof_QualityMetrics_Met[i] = 0; SourceQoITScore[i] = 0;} 
		communicationOverheadTime = networkMetrics.CommunicationOverheadTime(current_time, noofsources, totalNetworkNodes, informationSourceList, destinationNode, graph, nodes, links, runStep, link_CAU_QoIT, applicationDataRate);
	 *//******************************************************************************************************************************//*
		// Re-arranging to make infSourceList, because sources are not in order in informationSourceList
		for(int l = 0; l < informationSourceList.length; l++) {
			int pathToSource_Size = shortestPathsArray_forSelectedSources_toAllDestinations.get(destinationNode).get(l).size()-1;
			infSourceList[l] = shortestPathsArray_forSelectedSources_toAllDestinations.get(destinationNode).get(l).get(pathToSource_Size).getSourceLabel(); }	
	  *//******************************************************************************************************************************//*

		//QoIT wrt QoI
	   *//******************************************************************************************************************************//*	
		// "Network Metrics Values" and "Normalized Network Metric Values"
		// Information-Source Network Metric Values Calculation
		NetworkMetricValues = ahp.sourceNetworkMetricValues(NetworkMetricValues, noofcriteria, noofsources, totalNetworkNodes, infSourceList, destinationNode, graph, nodes, links, nodeKeys, runStep, fileNumber, link_CAU_QoIT, applicationDataRate);	
		// Network_Metric/Range to get relative values
		double networkMetricNormalized[][] = ahp.networkMetricNormalization(NetworkMetricValues, noofsources, noofcriteria, graph, nodes, link_CAU_QoIT, links, runStep, totalNetworkNodes, applicationDataRate, destinationNode, infSourceList);

		// Part(B): Selecting optimal QoI Information-Source
		double[] SourceQoIScores  = ss.QoIscore_ofSelectedSourcewrtQoITScheme(networkMetricNormalized, SourceComparisonMatrixWRTNetworkMetric, relationshipWeights, QoICriteriaWeights, noofsources, noofcriteria, 
				infSourceList, destinationNode, current_time);

		// QoIT wrt QoIT
	    *//******************************************************************************************************************************//*	
		long startTime_QoIT = System.nanoTime(); 

		for(int i = 0; i < infSourceList.length; i++) {
			double Bandwidth = Double.MAX_VALUE, informationUtility = 0.0, linkIntegrity = 0.0, bandwidth_and_communicationOverhead[];
			Dijkstra dijkstra = new Dijkstra(graph, nodes[infSourceList[i]].getLabel(), link_CAU_QoIT);
			HashMap<String,String> predecessors =  dijkstra.predecessorsList(graph, nodes[infSourceList[i]].getLabel(), link_CAU_QoIT).get(0);

			// Exploring Network Metric Values possessed by particular Src-Dst route
			bandwidth_and_communicationOverhead = networkMetrics.getBandwidth(dijkstra.getPathTo(Integer.toString(destinationNode)), Integer.toString(infSourceList[i]),
					nodes[infSourceList[i]], links, Bandwidth, graph, nodes[destinationNode].getLabel(), predecessors, runStep, totalNetworkNodes, applicationDataRate, link_CAU_QoIT);
			hopCount = networkMetrics.hopCount(Integer.toString(destinationNode), graph, nodes, infSourceList[i], link_CAU_QoIT);
			linkIntegrity = networkMetrics.getLinkIntegrity(nodeKeys, Integer.toString(infSourceList[i]), linkIntegrity, graph, nodes[destinationNode], predecessors);
			informationUtility = networkMetrics.getInformationUtility(informationUtility, Integer.toString(infSourceList[i]));
			Bandwidth = bandwidth_and_communicationOverhead[0];

			double BW = 0.0, HC = 0.0, LI = 0.0, IU = 0.0;

			if((Double.compare(Bandwidth, nmtv.get_bandwidth()) >= 0)) BW = 1; 	                      else BW = Bandwidth/nmtv.get_bandwidth();
			if(hopCount <= nmtv.get_hopCount()) HC = 1; 			                                  else HC = (double) nmtv.get_hopCount()/hopCount;
			if((Double.compare(linkIntegrity, nmtv.get_linkIntegrity()) >= 0)) LI = 1;   			  else LI = linkIntegrity / nmtv.get_linkIntegrity(); 
			if((Double.compare(informationUtility, nmtv.get_informationUtility()) >= 0)) IU = 1; 	  else IU = informationUtility/nmtv.get_informationUtility();  

			networkMetricValues_PercentageFulfilled[i][0] = HC; networkMetricValues_PercentageFulfilled[i][1] = IU;
			networkMetricValues_PercentageFulfilled[i][2] = BW; networkMetricValues_PercentageFulfilled[i][3] = LI;
		}

		// Individual Quality Metric VALUES and SCORES w.r.t column: A, C, T, R
		qualityMetricValues =  ahp.sourceVsNetworkMetricRelationshipWeights(qualityMetricValues, networkMetricValues_PercentageFulfilled, relationshipWeights, noofsources, noofcriteria);
		qualityMetricScores =  ahp.qualityMetricScores_QoIT(qualityMetricValues, QoICriteriaWeights, noofsources, noofcriteria, infSourceList, destinationNode, qualityMetricScores, QMS_QoITwrtQoIT_temp);

		for (int i = 0; i < noofsources; i++){	
			for (int j = 0; j < noofcriteria; j++){
				if(Double.compare(qualityMetricScores[i][j],QoICriteriaWeights[j]) == 0) { // Sum of Priorities of metrics met, Number of metric met, Sum of all -> QM_Score/QM_Threshold					
					PriorityScore[i] += priority_of_QualityMetrics[j]; 	Noof_QualityMetrics_Met[i] += 1;}
				SourceQoITScore[i] += qualityMetricScores[i][j];					
			}
		}

		// Selecting Source w.r.t QoIT Scheme
		selectedSource_QoIT = ahp.data_selectedSource_QoIT(qualityMetricScores, QoICriteriaWeights, noofsources, infSourceList, destinationNode, PriorityScore, Noof_QualityMetrics_Met, 
				SourceQoITScore, selectedSourceList_QoIT, runStep, flag_updatedInfo);

		long endTime_QoIT = System.nanoTime(), elapsedTime_QoIT  = (endTime_QoIT - startTime_QoIT);
		double seconds = ((double)elapsedTime_QoIT + communicationOverheadTime) / 1000000000.0;
		computationOverhead_QoIT_Data.writeToFile(seconds);

	     *//*********************************************************************************************************************************************************************************//*
		// Storing updated info wrt "pnp_SelectedSource_QoIwrtQoIT"
		for(int i = 0; i < infSourceList.length; i++) { if(selectedSource_QoIT == infSourceList[i]) { sourceIndex = i; break; } }

		if(selectedSource_QoIT != -1) {
			HashMap<String,String> predecessors = new HashMap<String,String>();
			Dijkstra dijkstra = new Dijkstra(graph, Integer.toString(selectedSource_QoIT), link_CAU_QoIT);
			predecessors =  dijkstra.predecessorsList(graph, Integer.toString(selectedSource_QoIT), link_CAU_QoIT).get(0);
			double Bandwidth = Double.MAX_VALUE, informationUtility = 0.0, linkIntegrity = 0.0, bandwidth_and_communicationOverhead[];
			double accuracy = 0.0, completeness = 0.0, timeliness = 0.0, reliability = 0.0;
			boolean flag_sameAs_previousPath = false;
			List<classNode> path = shortestPathsArray_forSelectedSources_toAllDestinations.get(destinationNode).get(sourceIndex);

			// Exploring Network Metric Values possessed by Src-Dst route
			double assigned_bandwidth = networkMetrics.allotedBandwidth(Integer.toString(selectedSource_QoIT), Bandwidth, graph, nodes[destinationNode].getLabel(), predecessors, applicationDataRate, link_CAU_QoIT, 
					networkMetricThresholdValues);	
			hopCount = networkMetrics.hopCount(Integer.toString(destinationNode), graph, nodes, selectedSource_QoIT, link_CAU_QoIT);
			linkIntegrity = networkMetrics.getLinkIntegrity(nodeKeys, Integer.toString(selectedSource_QoIT), linkIntegrity, graph, nodes[destinationNode], predecessors);
			informationUtility = networkMetrics.getInformationUtility(informationUtility, Integer.toString(selectedSource_QoIT));

	      *//*********************************************************************************************************************************************************************************//*
			// New implementation			
			//populating srcDstRouteNodes with src-dst path nodes
			for(int pathLength = 0; pathLength <= hopCount; pathLength++)  srcDstRouteNodes.add(Integer.parseInt(path.get(pathLength).getLabel()));		

			// Storing selected src-dst route into "srcDstRouteMap_QoI_updated" (for each iteration) and "srcDstRouteMap_QoI" (only when n/w info is propagated)
			srcDstRouteMap_QoIT_updated.put(destinationNode, srcDstRouteNodes);
			if(flag_updatedInfo == true) srcDstRouteMap_QoIT.put(destinationNode, srcDstRouteNodes);

			// To decide what to write to the "SrcDstPath_Data_QoI" file i.e. either -1 or the path (if )
			LinkedHashSet<Integer> srcDstRouteNodes_updated  = srcDstRouteMap_QoIT_updated.get(destinationNode), srcDstRouteNodes_previous = srcDstRouteMap_QoIT.get(destinationNode);
			Iterator<Integer> srcDstUpdatedRoute_Iterator = srcDstRouteNodes_updated.iterator(), srcDstPreviousRoute_Iterator = srcDstRouteNodes_previous.iterator();

			if(srcDstRouteNodes_updated.size() == srcDstRouteNodes_previous.size()) {
				for(int i = 0; i < srcDstRouteNodes_previous.size(); i++) {				
					if(srcDstUpdatedRoute_Iterator.next() == srcDstPreviousRoute_Iterator.next())  flag_sameAs_previousPath = true;
					else                                                                         { flag_sameAs_previousPath = false; break; }
				}
			}

			if(flag_sameAs_previousPath == true) { 
				for(int pathLength = 0; pathLength <= hopCount; pathLength++) 
					SrcDstPath_Data_QoIT.writeToFile(iteration, path.get(pathLength).getLabel(), hopCount, pathLength);
			}
			else SrcDstPath_Data_QoIT.writeToFile(iteration, destinationNode, -3);
	       *//*********************************************************************************************************************************************************************************//*
			Object KeySet[] = QMS_QoITwrtQoIT_temp.keySet().toArray();
			for(int i = 0; i < KeySet.length; i++) {
				QualityMetricScores qms = QMS_QoITwrtQoIT_temp.get((int) KeySet[i]);
				if(qms.get_sourceNode() == selectedSource_QoIT) {
					accuracy = qms.get_Accuracy(); completeness = qms.get_Completeness(); timeliness = qms.get_Timeliness(); reliability = qms.get_Reliability();
					break;   					
				}
			}	
	        *//*********************************************************************************************************************************************************************************//*	
			// Storing non-updated info wrt "pnp_SelectedSource_QoITwrtQoIT"
			if (flag_updatedInfo == true) {
				pnp_SelectedSource_QoITwrtQoIT.put(destinationNode, new networkData_HashMaps(iteration, destinationNode, selectedSource_QoIT, PriorityScore[sourceIndex], Noof_QualityMetrics_Met[sourceIndex], SourceQoITScore[sourceIndex]));					
				qms_SelectedSource_QoITwrtQoIT.put(destinationNode, new networkData_HashMaps(iteration, destinationNode, selectedSource_QoIT, accuracy, completeness, timeliness, reliability));
				QoITSelectedSource_QoITScore_Data.writeToFile(runStep, destinationNode, selectedSource_QoIT, SourceQoITScore[sourceIndex]);
				QoITSelectedSource_QoIScore_Data.writeToFile(runStep, destinationNode, selectedSource_QoIT, SourceQoIScores[sourceIndex]);
			}
			else {
				srcDstRouteNodes_updated  = srcDstRouteMap_QoIT_updated.get(destinationNode); srcDstRouteNodes_previous = srcDstRouteMap_QoIT.get(destinationNode); 
				srcDstUpdatedRoute_Iterator  = srcDstRouteNodes_updated.iterator(); srcDstPreviousRoute_Iterator = srcDstRouteNodes_previous.iterator();
				flag_sameAs_previousPath = false;

				if(srcDstRouteNodes_updated.size() == srcDstRouteNodes_previous.size()) {
					for(int i = 0; i < srcDstRouteNodes_previous.size(); i++) {
						if( srcDstUpdatedRoute_Iterator.next() == srcDstPreviousRoute_Iterator.next())  flag_sameAs_previousPath = true;
						else 									                                      { flag_sameAs_previousPath = false; break; }
					}

					if (flag_sameAs_previousPath == true) {
						pnp_SelectedSource_QoITwrtQoIT.put(destinationNode, new networkData_HashMaps(iteration, destinationNode, selectedSource_QoIT, PriorityScore[sourceIndex], Noof_QualityMetrics_Met[sourceIndex], SourceQoITScore[sourceIndex]));
						qms_SelectedSource_QoITwrtQoIT.put(destinationNode, new networkData_HashMaps(iteration, destinationNode, selectedSource_QoIT, accuracy, completeness, timeliness, reliability));
					}
					else {
						pnp_SelectedSource_QoITwrtQoIT.put(destinationNode, new networkData_HashMaps(iteration, destinationNode, selectedSource_QoIT, 0, 0, 0));	
						qms_SelectedSource_QoITwrtQoIT.put(destinationNode, new networkData_HashMaps(iteration, destinationNode, selectedSource_QoIT, 0.0, 0.0, 0.0, 0.0));
					}
				}
				else {
					pnp_SelectedSource_QoITwrtQoIT.put(destinationNode, new networkData_HashMaps(iteration, destinationNode, selectedSource_QoIT, 0, 0, 0));	
					qms_SelectedSource_QoITwrtQoIT.put(destinationNode, new networkData_HashMaps(iteration, destinationNode, selectedSource_QoIT, 0.0, 0.0, 0.0, 0.0));
				}
			}

	         *//**********************************************************************************************************************************************************************************//*			
			obj.add(selectedSource_QoIT);
			obj.add(predecessors);
			obj.add(assigned_bandwidth);
			obj.add(hopCount);
			obj.add(linkIntegrity);
			obj.add(informationUtility);
			return obj;
		}

		else {	
			SrcDstPath_Data_QoIT.writeToFile(iteration, destinationNode, -1);	
			pnp_SelectedSource_QoITwrtQoIT.put(destinationNode, new networkData_HashMaps(iteration, destinationNode, -1, 0, 0, 0));
			qms_SelectedSource_QoITwrtQoIT.put(destinationNode, new networkData_HashMaps(iteration, destinationNode, -1, 0.0, 0.0, 0.0, 0.0));
			obj.add(selectedSource_QoIT);
			return obj;
		}
	}*/

}
