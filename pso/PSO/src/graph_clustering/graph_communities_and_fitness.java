package graph_clustering;

import java.util.Random;
import java.util.Vector;

public class graph_communities_and_fitness {
	Vector<Float> sorted_eigen_value ;
	Vector<Vector<Float>> sorted_eigen_vector ; 
	
	//this may update by this algorithm so PSO needs 2 things here
	Vector<Float> pso_results_at_every_iteration ; 
	
	//Instead of quantization error in data clustering here it requires Q value
	// ********** Q value is optimized to max instead where 
	// ********** PSO for data clustering quantization error is optimized to minimum value
	float Q_value_for_this_iteration ; 
	
	int n ; //number of communities center
	//pso based center for which flag greater than equal to 0.5
	// we will change this estimation we respect to eigen vector whose eigen value is more
	Vector<Float> center_estimated_by_pso ; 
	
	Vector<Vector<Float>> actual_center_selected_from_eigen_vector ; 
	
	//Random Number Generator
	Random random_number_giver_between_0_and_1 ; 
	
	public graph_communities_and_fitness(Vector<Float> sorted_eigen_value , Vector<Vector<Float>> sorted_eigen_vector) {
		// TODO Auto-generated constructor stub
		this.sorted_eigen_value = sorted_eigen_value ; 
		this.sorted_eigen_vector = sorted_eigen_vector ; 
		this.random_number_giver_between_0_and_1 = new Random();
	}
	
	public void graph_cluster_at_every_pso_iteration(Vector<Float> pso_results_at_every_iteration)
	{
		this.pso_results_at_every_iteration = pso_results_at_every_iteration ;
		//every step is one function
		pso_results_rounding_off() ; 
		centroids_size_greater_than_two_checking();
		assign_first_eigen_vector_values_to_estimated_near_centroids(); 
	}
	
	public float get_Q_value_for_this_iteration()
	{
		return Q_value_for_this_iteration ; 
	}
	
	// this is because step 2 in research paper of "PSO based graph clustering"
	// flag value are changed flag_p = 1- 0.5 * rand(0,1) flag_q = 1- 0.5 * rand(0,1)
	// this is may or may not happen so safe side take value from here
	public Vector<Float> get_particle_new_position()
	{
		return pso_results_at_every_iteration ; 
	}
	
	//step 1 
	public void pso_results_rounding_off()
	{
		// always even // half flag half centroids
		int size_of_particle_dimension = pso_results_at_every_iteration.size() ; // 1 ... n
		int flags_are_upto= (size_of_particle_dimension/2)-1 ; // 0 is starting index 0 ... n-1
		int actual_center_start_from = flags_are_upto ; // 0 is starting index n/2 ... n-1
		n = 0 ; // setting to zero
		center_estimated_by_pso = new Vector<Float>() ; //  center vector intialization
		// rounding off and center consideration
		for (int counter_flag_and_cen = 0 ; counter_flag_and_cen < (flags_are_upto+1) ; counter_flag_and_cen++)
		{
			// round
			if (Math.round(pso_results_at_every_iteration.get(counter_flag_and_cen))==1)
			{
				n = n +1 ; 
				//center added
				center_estimated_by_pso.
					add(pso_results_at_every_iteration.get(counter_flag_and_cen + actual_center_start_from));
			}
		}
	}
	
	// check if n > 2
	//step 2 of research paper
	public void centroids_size_greater_than_two_checking()
	{
		if (n<2)
		{
			//two max values and respective positions
			float max_value_first = 0 ;
			float max_value_second = 0 ; 
			int position_in_pso_results_of_max_value_first = 0 ; 
			int position_in_pso_results_of_max_value_second = 0 ; 

			
			int size_of_particle_dimension = pso_results_at_every_iteration.size() ; // 1 ... n
			int flags_are_upto= (size_of_particle_dimension/2)-1 ; // 0 is starting index 0 ... n-1
			int actual_center_start_from = flags_are_upto ; // 0 is starting index n/2 ... n-1
			n = 2 ; // setting to two
			center_estimated_by_pso = new Vector<Float>() ; //  center vector intialization
			// rounding off and center consideration
			for (int counter_flag_and_cen = 0 ; counter_flag_and_cen < (flags_are_upto+1) ; counter_flag_and_cen++)
			{
				//first two max value calculated
				if (pso_results_at_every_iteration.get(counter_flag_and_cen) > max_value_first)
				{
					//assign this value to lower
					position_in_pso_results_of_max_value_second = position_in_pso_results_of_max_value_first ;
					max_value_second = max_value_first ; 
					
					//assign new value here
					position_in_pso_results_of_max_value_first = counter_flag_and_cen ; 
					max_value_first = pso_results_at_every_iteration.get(counter_flag_and_cen);
				}
				
				else if (pso_results_at_every_iteration.get(counter_flag_and_cen) > max_value_second)
				{
					//assign value to second only
					position_in_pso_results_of_max_value_second = counter_flag_and_cen ; 
					max_value_second = pso_results_at_every_iteration.get(counter_flag_and_cen) ; 
				}
			}
			
			center_estimated_by_pso.add(pso_results_at_every_iteration.
					get((position_in_pso_results_of_max_value_first+actual_center_start_from)));
			center_estimated_by_pso.add(pso_results_at_every_iteration.
					get((position_in_pso_results_of_max_value_second+actual_center_start_from)));
			//upwards n set to two and center are added from second half of particle
			
			
			//now set flags value to (1-0.5*rand(0,1))
			pso_results_at_every_iteration.set(position_in_pso_results_of_max_value_first, 
				(1.0f- (0.5f *	random_number_giver_between_0_and_1.nextFloat())));
			
			pso_results_at_every_iteration.set(position_in_pso_results_of_max_value_second, 
					(1.0f- (0.5f *	random_number_giver_between_0_and_1.nextFloat())));
			
		}
						
	}
	
	//step 3
	public void assign_first_eigen_vector_values_to_estimated_near_centroids()
	{
		actual_center_selected_from_eigen_vector = new Vector<Vector<Float>>() ; 
		
		// actual near points finding from 1st eigen vector
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
