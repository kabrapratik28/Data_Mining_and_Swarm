package graph_clustering;

import java.util.Random;
import java.util.Vector;

public class random_particle_giver {
	
	//only 2 values 
	//min max values in between particle intiated 
	Vector<Float> min_max_value ; 
	
	//min max velocity of particle
	Vector<Float> min_max_velocity ; 
		
	//eigen value and vectors
	Vector<Float> sorted_eigen_value ; 
	Vector<Vector<Float>> sorted_eigen_vector ; 
	
	//particle dimension = 2 * (+ve eigen values)
	int half_size_particle_dimension ; 
	
	//Random Float generator
	Random random ; 
	
	public random_particle_giver(Vector<Float> sorted_eigen_value, Vector<Vector<Float>> sorted_eigen_vector)
	{
		// random no generator between 0 and 1
		random = new Random() ;
		
		this.sorted_eigen_value = sorted_eigen_value ;
		this.sorted_eigen_vector  = sorted_eigen_vector ; 
		
		//particle dimension set 
		//particle centroid is (+ve eigen value + 1 ) no of centroids
		half_size_particle_dimension = sorted_eigen_value.size()+1 ; 
		
		//get min max limitation of first eigen vector
		min_max_value_for_particle(sorted_eigen_vector.get(0));
		min_max_velocity_for_particle() ;
	}
	
	public void min_max_value_for_particle(Vector<Float> first_eigen_vector)
	{
		min_max_value = new Vector<Float>() ;
		float min_value = Float.MAX_VALUE; 
		float max_value = Float.MIN_VALUE; 
		for (int counter_eigen_vector_values =0 ; counter_eigen_vector_values < first_eigen_vector.size() ; counter_eigen_vector_values++)
		{
			if (max_value < first_eigen_vector.get(counter_eigen_vector_values))
			{
				max_value = first_eigen_vector.get(counter_eigen_vector_values);
			}
			if (min_value > first_eigen_vector.get(counter_eigen_vector_values))
			{
				min_value = first_eigen_vector.get(counter_eigen_vector_values);
			}
		}
		min_max_value.add(min_value);
		min_max_value.add(max_value);
		
	}
	
	public void min_max_velocity_for_particle()
	{
		min_max_velocity = new Vector<Float>(); 
		
		float min_velocity =  min_max_value.get(0) - min_max_value.get(1);
		float max_velocity = min_max_value.get(1) - min_max_value.get(0) ; 
		
		min_max_velocity.add(min_velocity) ; 
		min_max_velocity.add(max_velocity);
	}
	
	//max_number_of_communities == number of +ve eigen vector
	//no of +ve eigen vector passed above 
	public graph_particle give_random_particle()
	{
		Vector<Float> particle_of_swarm = new Vector<Float>() ; 
		Vector<Float> vel_of_particle = new Vector<Float>(); 
		//dimension centroid consideration between 0 and 1
		for (int dimension_counter= 0 ; dimension_counter < half_size_particle_dimension ; dimension_counter++)
		{
			particle_of_swarm.add(random.nextFloat()) ; //between 0 and 1 given
		}
		
		// actual centroids randomly initiated
		for (int dimension_counter= 0 ; dimension_counter < half_size_particle_dimension ; dimension_counter++)
		{
			particle_of_swarm.
				add(random_no_giver_in_range(min_max_value.get(0), min_max_value.get(1))) ; //particle centroids randomly initated
		}
		
		//Velocity initilization
		for (int dimension_counter= 0 ; dimension_counter < half_size_particle_dimension ; dimension_counter++)
		{
			vel_of_particle.add(random.nextFloat()) ; //between 0 and 1 given
		}
		
		// actual centroids randomly initiated
		for (int dimension_counter= 0 ; dimension_counter < half_size_particle_dimension ; dimension_counter++)
		{
			vel_of_particle.
				add(random_no_giver_in_range(min_max_velocity.get(0), min_max_velocity.get(1))) ; //particle centroids randomly initated
		}
		
		graph_particle g1 = new graph_particle(particle_of_swarm, vel_of_particle);
		
		return g1 ; 
	}

	// same as swarm.random_particle generator
	public float random_no_giver_in_range(float min_value , float max_value)
	{
		float new_random_number_in_range = random.nextFloat() * (max_value-min_value) + min_value ; 
		return new_random_number_in_range ; 
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * TESTING WILL TROW EXCEPTION BZ IT IS CALLING PARTICLE
		Vector<Float> sorted_eigen_va = new  Vector<Float>();
		Vector<Vector<Float>> sorted_eigen_ve = new Vector<Vector<Float>>();
		
		sorted_eigen_va.add(0.8677277f);
		sorted_eigen_va.add(0.712951f);
		
		Vector<Float> sorted_eigen_tem = new Vector<Float>();
		sorted_eigen_tem.add(0.14249386f);
		sorted_eigen_tem.add(0.072698124f);
		sorted_eigen_tem.add(-0.005455382f);
		sorted_eigen_ve.add(sorted_eigen_tem);
		
		sorted_eigen_tem = new Vector<Float>();
		sorted_eigen_tem.add(0.07090773f);
		sorted_eigen_tem.add(0.22916597f);
		sorted_eigen_tem.add(0.1311889f);
		sorted_eigen_ve.add(sorted_eigen_tem);
		
		random_particle_giver r1 = new random_particle_giver(sorted_eigen_va, sorted_eigen_ve);
		for (int i = 0 ; i<5 ; i++)
		{
		   r1.give_random_particle() ; 
		}
		*/
	}

}
