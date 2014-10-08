package graph_clustering;

import java.util.Vector;

public class fitness_function {
	float  Q_value_for_community ; 
	int no_of_communities ; 
	Vector<Float> A_vector_inter_cluster_values ; 
	Vector< Vector<Float> > community_linkage ; 
	//give community based linkage matrix 
	//symmetric in nature 
	//Matrix E in paper of Zhewen Shi 3.3 Section
	public fitness_function (Vector< Vector<Float> > community_linkage)
	{
		Q_value_for_community =  0 ; 
		no_of_communities = community_linkage.size() ; 
		A_vector_inter_cluster_values = new Vector<Float>() ; 
		this.community_linkage = community_linkage ; 
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
				// Dont consider intra community values
				if (community_counter != other_community_counter)
				{
					sum_of_all_inter_community_linkage = 
							sum_of_all_inter_community_linkage +
							community_linkage.get(community_counter).get(other_community_counter) ;
				}
			}
			A_vector_inter_cluster_values.add(sum_of_all_inter_community_linkage);
		}
	}
	
	public void calculate_Q_value ()
	{
		Q_value_for_community = 0 ; 
		for (int community_counter=0 ; community_counter < no_of_communities ; community_counter++)
		{
			Q_value_for_community = Q_value_for_community +
					(community_linkage.get(community_counter).get(community_counter) /* intra community fraction */
							-
					(float)Math.pow(A_vector_inter_cluster_values.get(community_counter), 2) /*inter community fraction*/
					) ; 
		}
	}
	
	public float get_Q_value() 
	{
		return Q_value_for_community ; 
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Vector <Vector<Float>> comun = new Vector<Vector<Float>>() ;  
		Vector<Float> onec = new Vector<Float>() ; 
		onec.add((float)(3.0/12.0));
		onec.add((float)(1.0/12.0));
		onec.add((float)(1.0/12.0));
		
		comun.add(onec) ; 
		
		onec = new Vector<Float>() ; 
		onec.add((float)(1.0/12.0));
		onec.add((float)(3.0/12.0));
		onec.add((float)(1.0/12.0));
		
		comun.add(onec) ; 
		
		onec = new Vector<Float>() ; 
		onec.add((float)(1.0/12.0));
		onec.add((float)(1.0/12.0));
		onec.add((float)(3.0/12.0));
		
		comun.add(onec) ; 
		
		fitness_function ff = new fitness_function(comun);
		
		System.out.println(ff.get_Q_value());

	}

}
