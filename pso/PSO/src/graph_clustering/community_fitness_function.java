package graph_clustering;

import java.util.Map ; 
import java.util.HashMap;
import java.util.Vector;

public class community_fitness_function {
	float  Q_value_for_community ; 
	int no_of_communities ; 
	Vector<Float> A_vector_inter_cluster_values ; 
	
	//community linkage cost
	Vector<Vector<Float>> community_inter_intra_linkage ;
	
	//sum of all linkage
	float sum_of_all_linkage_in_graph  ;
	
	//Graph data matrix 
	Vector<Vector<Float>> graph_data ; 
	
	//Hash map center and community
	HashMap<Integer, Vector<Integer>> centroid_no_to_element ; 
	
	public community_fitness_function(Vector<Vector<Float>> graph_data)
	{
		this.graph_data = graph_data ; 
	}
	
	//give community based linkage matrix 
	//symmetric in nature 
	//Matrix E in paper of Zhewen Shi 3.3 Section
	public void fitness_function_calculate_for_community ( HashMap<Integer, Vector<Integer>> centroid_no_to_element)
	{
		this.centroid_no_to_element = centroid_no_to_element ; 
		linkage_matrix_making() ; 
		
		Q_value_for_community =  0 ; 
		no_of_communities = community_inter_intra_linkage.size() ; 
		A_vector_inter_cluster_values = new Vector<Float>() ; 
		
		calculate_A_vector_values() ; 
		calculate_Q_value() ; 
	}
	
	public void calculate_A_vector_values ()
	{
		for (int community_counter=0; community_counter<no_of_communities ; community_counter++ )
		{
			float sum_of_all_inter_community_linkage = 0  ;
			for (int other_community_counter=0 ; other_community_counter<no_of_communities; other_community_counter++ )
			{
				//intra is added 2 times 
				if (community_counter == other_community_counter)
				{
					sum_of_all_inter_community_linkage = 
							sum_of_all_inter_community_linkage + 2 * community_inter_intra_linkage.get(community_counter).get(other_community_counter) ;			
				}
				//inter added just 1 time
				else
				{
					sum_of_all_inter_community_linkage = 
							sum_of_all_inter_community_linkage +
							community_inter_intra_linkage.get(community_counter).get(other_community_counter) ;
				}
			}
			//divide this by 2 * total no. of edges we have
			A_vector_inter_cluster_values.add((sum_of_all_inter_community_linkage/(2*sum_of_all_linkage_in_graph)));
		}
	}
	
	public void calculate_Q_value ()
	{
		Q_value_for_community = 0 ; 
		for (int community_counter=0 ; community_counter < no_of_communities ; community_counter++)
		{
			Q_value_for_community = Q_value_for_community +
					// fraction of edges in intra
					((community_inter_intra_linkage.get(community_counter).get(community_counter)/sum_of_all_linkage_in_graph )/* intra community fraction */
							-
					(float)Math.pow(A_vector_inter_cluster_values.get(community_counter), 2) /*inter community fraction*/
					) ; 
		}
	}
	
	public float get_Q_value() 
	{
		return Q_value_for_community ; 
	}
	
	//graph data = whole graph linkage 
	//centroid_no_to_element : centroid and respected element
	public void linkage_matrix_making( )
	{
		int no_of_communities = centroid_no_to_element.size() ; 
		//n*n matrix initilization // community* community matrix
		community_inter_intra_linkage = new Vector<Vector<Float>>(); 
		for (int count_community= 0  ; count_community < no_of_communities ; count_community++)
		{
			Vector<Float> community_row_in_matrix = new Vector<Float>();
			//intialize with 0's
			for (int count_clm=0; count_clm < no_of_communities ; count_clm++)
			{
				community_row_in_matrix.add(0.0f);
			}
			community_inter_intra_linkage.add(community_row_in_matrix);
		}
		
		//map to vector change
		Vector<Vector<Integer>> communities_in_graph =  new Vector<Vector<Integer>>(); 
		
		//iterate over map and all vector of community
		for (Map.Entry<Integer, Vector<Integer>> entry : centroid_no_to_element.entrySet()) {
			communities_in_graph.add(entry.getValue());
		}
		
		//sum of all linkage in graph 
		//every matrix element of n*n should be divided by this
		sum_of_all_linkage_in_graph = 0 ; 
		
		//all community iterator
		for (int counter_to_community_no=0; counter_to_community_no < no_of_communities ; counter_to_community_no++)
		{
			Vector<Integer> nodes_in_one_community = communities_in_graph.get(counter_to_community_no) ; 
			float intra_cluster_sum = 0.0f ;
			
			//all internal added first (intra cluster)
			//nodes checks with forward node
			for (int node_intra_counter = 0 ; node_intra_counter < nodes_in_one_community.size(); node_intra_counter++)
			{
				for (int node_intra_counter_two = node_intra_counter ; node_intra_counter_two < nodes_in_one_community.size(); node_intra_counter_two++)
				{
					intra_cluster_sum = intra_cluster_sum + 
							graph_data.get(nodes_in_one_community.get(node_intra_counter)).get(nodes_in_one_community.get(node_intra_counter_two));
					
					//for whole graph linkage total
					sum_of_all_linkage_in_graph = sum_of_all_linkage_in_graph+
							graph_data.get(nodes_in_one_community.get(node_intra_counter)).get(nodes_in_one_community.get(node_intra_counter_two));
				}
			}
			
			//set diagonal matrix element which represent intra cluster value
			community_inter_intra_linkage.get(counter_to_community_no).set(counter_to_community_no, intra_cluster_sum);
			
			//inter cluster finding 
			//with other other community 
			for (int another_community_counter= counter_to_community_no+1 ; another_community_counter < no_of_communities ; another_community_counter++ )
			{
				Vector<Integer> nodes_in_another_community = communities_in_graph.get(another_community_counter) ; 
				
				float community_one_to_another_total = 0 ; 
				for (int node_counter_of_first_community = 0 ; node_counter_of_first_community < nodes_in_one_community.size() ; node_counter_of_first_community++)
				{	
					int node_number_first_community = nodes_in_one_community.get(node_counter_of_first_community) ; 
					for(int node_counter_of_second_community=0 ; node_counter_of_second_community < nodes_in_another_community.size() ; node_counter_of_second_community++ )
					{
						int node_number_second_community  = nodes_in_another_community.get(node_counter_of_second_community);
						// inter community count 
						community_one_to_another_total = community_one_to_another_total +
								graph_data.get(node_number_first_community).get(node_number_second_community) ; 
						
						sum_of_all_linkage_in_graph = sum_of_all_linkage_in_graph + 
								graph_data.get(node_number_first_community).get(node_number_second_community) ; 
					}
					
				}
				// m row n column
				community_inter_intra_linkage.get(counter_to_community_no).set(another_community_counter, community_one_to_another_total);
				// bz of symmetry set for n row m clm
				community_inter_intra_linkage.get(another_community_counter).set(counter_to_community_no, community_one_to_another_total);
			}
		}
		//System.out.println(community_inter_intra_linkage);
		/*
		for (int count_community= 0 ; count_community < community_inter_intra_linkage.size() ; count_community++)
		{
			for(int count_column =0 ; count_column < community_inter_intra_linkage.get(0).size(); count_column++)
			{
				//divide all elements by total linkage
				float fraction_linkage = community_inter_intra_linkage.get(count_community).get(count_column) / sum_of_all_linkage_in_graph;
				community_inter_intra_linkage.get(count_community).set(count_column, fraction_linkage ) ; 
			}
		}
		
		System.out.println(community_inter_intra_linkage);
		*/
		//System.out.println("all nodes total weight "+sum_of_all_linkage_in_graph);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		Vector<Vector<Float>> graph_data = new Vector<Vector<Float>>();
		
		Vector<Float> row = new Vector<Float>();
		row.add(0.0f);
		row.add(0.0f);
		row.add(0.0f);
		row.add(7.0f);
		row.add(0.0f);
		row.add(4.0f);
		row.add(0.0f);
		row.add(0.0f);
		graph_data.add(row);
		
		row = new Vector<Float>();
		row.add(0.0f);
		row.add(0.0f);
		row.add(0.0f);
		row.add(2.0f);
		row.add(0.0f);
		row.add(0.0f);
		row.add(1.0f);
		row.add(0.0f);
		graph_data.add(row);
		
		row = new Vector<Float>();
		row.add(0.0f);
		row.add(0.0f);
		row.add(0.0f);
		row.add(0.0f);
		row.add(5.0f);
		row.add(0.0f);
		row.add(0.0f);
		row.add(0.0f);
		graph_data.add(row);
		
		row = new Vector<Float>();
		row.add(7.0f);
		row.add(2.0f);
		row.add(0.0f);
		row.add(0.0f);
		row.add(8.0f);
		row.add(2.0f);
		row.add(0.0f);
		row.add(0.0f);
		graph_data.add(row);
		
		row = new Vector<Float>();
		row.add(0.0f);
		row.add(0.0f);
		row.add(5.0f);
		row.add(8.0f);
		row.add(0.0f);
		row.add(0.0f);
		row.add(6.0f);
		row.add(2.0f);
		graph_data.add(row);
		
		
		row = new Vector<Float>();
		row.add(4.0f);
		row.add(0.0f);
		row.add(0.0f);
		row.add(2.0f);
		row.add(0.0f);
		row.add(0.0f);
		row.add(0.0f);
		row.add(0.0f);
		graph_data.add(row);
		
		row = new Vector<Float>();
		row.add(0.0f);
		row.add(1.0f);
		row.add(0.0f);
		row.add(0.0f);
		row.add(6.0f);
		row.add(0.0f);
		row.add(0.0f);
		row.add(0.0f);
		graph_data.add(row);
		
		
		row = new Vector<Float>();
		row.add(0.0f);
		row.add(0.0f);
		row.add(0.0f);
		row.add(0.0f);
		row.add(2.0f);
		row.add(0.0f);
		row.add(0.0f);
		row.add(0.0f);
		graph_data.add(row);
		
		 HashMap<Integer, Vector<Integer>> m1 = new HashMap<Integer, Vector<Integer>>();
		 
		 Vector<Integer> r1 = new Vector<Integer>();
		 r1.add(0);
		 r1.add(3);
		 r1.add(5);
		 m1.put(0, r1);
		 
		 r1 = new Vector<Integer>();
		 r1.add(1);
		 r1.add(6);
		 m1.put(6, r1);
		 
		 r1 = new Vector<Integer>();
		 r1.add(2);
		 r1.add(4);
		 r1.add(7);
		 m1.put(7, r1);
		 
		 
		 fitness_function ff1 = new fitness_function(graph_data) ;
		 ff1.fitness_function_calculate_for_community(m1);
		 System.out.println(ff1.get_Q_value());
		 */
		
		/*     KARATE CLUB
		//====================== DELETE THIS ======================= */
				HashMap<Integer, Vector<Integer>> asa = new HashMap<Integer,Vector<Integer>>();
				Vector<Integer> pp = new Vector<Integer>();
				pp.add(6);
				pp.add(10);
				pp.add(5);
				pp.add(4);
				pp.add(16);
				asa.put(5, pp);
				
				pp = new Vector<Integer>();
				pp.add(2);
				pp.add(13);
				pp.add(19);
				pp.add(7);
				pp.add(3);
				pp.add(1);
				pp.add(12);
				pp.add(0);
				pp.add(17);
				pp.add(11);
				pp.add(21);
				asa.put(7, pp);
				
				pp = new Vector<Integer>();
				pp.add(26);
				pp.add(22);
				pp.add(14);
				pp.add(20);
				pp.add(15);
				pp.add(18);
				pp.add(9);
				pp.add(33);
				pp.add(32);
				pp.add(30);
				pp.add(8);
				pp.add(29);
				asa.put(30, pp);
				
				pp = new Vector<Integer>();
				pp.add(23);
				pp.add(24);
				pp.add(25);
				pp.add(27);
				pp.add(28);
				pp.add(31);
				asa.put(25, pp);
				
				System.out.println(asa);
				float [][] asd = {{0,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0,1,0,0}, {1,0,1,1,0,0,0,1,0,0,0,0,0,1,0,0,0,1,0,1,0,1,0,0,0,0,0,0,0,0,1,0,0,0}, {1,1,0,1,0,0,0,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1,0}, {1,1,1,0,0,0,0,1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,1}, {0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, {1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1}, {0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1}, {1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1}, {1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,1,0,0,1,1}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,1,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1}, {0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1}, {0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,1,1}, {0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1}, {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,1,0,0,0,1,1}, {0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,1,0,0,1,0,1,0,1,1,0,0,0,0,0,1,1,1,0,1}, {0,0,0,0,0,0,0,0,1,1,0,0,0,1,1,1,0,0,1,1,1,0,1,1,0,0,1,1,1,1,1,1,1,0} };
				//float[][] asd  = {{0,1,1,0,0,1},{1,0,1,0,0,0},{1,1,0,0,0,0},{0,0,0,0,1,1},{0,0,0,1,0,1},{1,0,0,1,1,0}};
				
				Vector<Vector<Float>> aa = new Vector<Vector<Float>>() ; 
				for (int k=0 ; k< asd.length ; k++)
				{
					Vector<Float> pps = new Vector<Float>(); 
					for(int j=0 ; j<asd[k].length ; j++)
					{
						pps.add(asd[k][j]);
					}
					aa.add(pps);
				}
				
				community_fitness_function community_fitness_function_obj = new community_fitness_function(aa);
				community_fitness_function_obj.fitness_function_calculate_for_community(asa);
				System.out.println("Manual checking ... "+community_fitness_function_obj.get_Q_value());
				//===================== DELETE THIS ENDS ==================== */
				/*
				 * FOOTBALL CLUB
		HashMap<Integer, Vector<Integer>> asa = new HashMap<Integer,Vector<Integer>>();
		Vector<Integer> pp = new Vector<Integer>();
		pp.add(7);
		pp.add(8);
		pp.add(21);
		pp.add(22);
		pp.add(51);
		pp.add(68);
		pp.add(77);
		pp.add(78);
		pp.add(108);
		pp.add(111);
		asa.put(111, pp);
		
		pp = new Vector<Integer>();
		pp.add(1);
		pp.add(25);
		pp.add(33);
		pp.add(37);
		pp.add(45);
		pp.add(89);
		pp.add(103);
		pp.add(105);
		pp.add(109);
		asa.put(105, pp);
		
		pp = new Vector<Integer>();
		pp.add(12);
		pp.add(14);
		pp.add(18);
		pp.add(26);
		pp.add(31);
		pp.add(34);
		pp.add(38);
		pp.add(43);
		pp.add(54);
		pp.add(61);
		pp.add(71);
		pp.add(85);
		pp.add(99);
		pp.add(36);
		pp.add(42);
		asa.put(99, pp);
		
		pp = new Vector<Integer>();
		pp.add(44);
		pp.add(48);
		pp.add(57);
		pp.add(66);
		pp.add(75);
		pp.add(86);
		pp.add(91);
		pp.add(92);
		pp.add(112);
		asa.put(92, pp);
		
		pp = new Vector<Integer>();
		pp.add(0);
		pp.add(4);
		pp.add(9);
		pp.add(16);
		pp.add(23);
		pp.add(41);
		pp.add(93);
		pp.add(104);
		pp.add(11);
		pp.add(24);
		pp.add(50);
		pp.add(69);
		pp.add(28);
		pp.add(90);
		asa.put(90, pp);
		
		
		pp = new Vector<Integer>();
		pp.add(3);
		pp.add(5);
		pp.add(10);
		pp.add(40);
		pp.add(52);
		pp.add(72);
		pp.add(74);
		pp.add(81);
		pp.add(84);
		pp.add(98);
		pp.add(102);
		pp.add(107);
		asa.put(102, pp);
		
		pp = new Vector<Integer>();
		pp.add(2);
		pp.add(6);
		pp.add(13);
		pp.add(15);
		pp.add(32);
		pp.add(39);
		pp.add(47);
		pp.add(60);
		pp.add(64);
		pp.add(100);
		pp.add(106);
		asa.put(2, pp);
		
		pp = new Vector<Integer>();
		pp.add(46);
		pp.add(49);
		pp.add(53);
		pp.add(67);
		pp.add(73);
		pp.add(83);
		pp.add(88);
		pp.add(114);
		pp.add(110);
		asa.put(46, pp);
		
		pp = new Vector<Integer>();
		pp.add(19);
		pp.add(29);
		pp.add(30);
		pp.add(35);
		pp.add(55);
		pp.add(79);
		pp.add(94);
		pp.add(101);
		pp.add(80);
		pp.add(82);
		asa.put(80, pp);
		
		pp = new Vector<Integer>();
		pp.add(17);
		pp.add(20);
		pp.add(27);
		pp.add(56);
		pp.add(62);
		pp.add(65);
		pp.add(70);
		pp.add(76);
		pp.add(87);
		pp.add(95);
		pp.add(96);
		pp.add(110);
		pp.add(59);
		pp.add(63);
		pp.add(97);
		pp.add(58);
		asa.put(20, pp);
		Vector<Vector<Float>> aa = null ;
		try (BufferedReader br = new BufferedReader(new FileReader("testing.txt")))
		{
 
			String sCurrentLine;
		    aa= new Vector<Vector<Float>>() ; 
			Vector<Float> pps = new Vector<Float>(); 
			
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.matches("----") )
				{
					aa.add(pps);
					pps = new Vector<Float>(); 
				}
				else
				{
					pps.add((float)Integer.parseInt(sCurrentLine));
				}
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		System.out.println(aa);

		community_fitness_function community_fitness_function_obj = new community_fitness_function(aa);
		community_fitness_function_obj.fitness_function_calculate_for_community(asa);
		System.out.println("Manual checking ... "+community_fitness_function_obj.get_Q_value());
	
		*/
	}

}
