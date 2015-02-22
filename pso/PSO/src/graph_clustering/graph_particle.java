package graph_clustering;

import dataset.database_connectivity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class graph_particle {
	
	Vector< Float > current_location ;  // now current velocity
	Vector< Float > current_velocity ;  // now current velocity
	
	float pbest ;  
	Vector< Float > pbest_position ;  // all dimension value for pbest value
	
	/*
	 * set after all particle value calculated
	 * initialization after all particle calculated
	 */
	static float gbest ; 
	static Vector< Float > gbest_position ;  
	
	//for every particle make community out of graph centroids and fitness function for this particle 
	static graph_make_communities graph_make_communities_obj ; 
	static community_fitness_function community_fitness_function_obj ; 
	
	static graph_settings setting_for_all_graph ;  // setting for all particles (inertia, c1, c2 , no of cluster , dimension ,etc)
	static Vector< Float > min_max_values_for_particle ; // requires at boundary wrapping for all particle 
	
	
	//When object initializes position and velocity initial values set 
	public graph_particle(Vector< Float > intial_values ,Vector< Float > intial_velocities  )
	{
		current_location = new Vector<Float>();
		graph_particle.copy_vector_inside_vector(intial_values, current_location);
		
		current_velocity = new Vector< Float > ();
		graph_particle.copy_vector_inside_vector(intial_velocities, current_velocity) ; 
		
		/*
		 * set pbest
		 */
		pbest_position = new Vector<Float > (); //copy same dimension to pbest_position bz its for first time
		
		// make communities out of current pbest position
		graph_make_communities_obj.graph_cluster_at_every_pso_iteration(current_location) ; 
		graph_particle.copy_vector_inside_vector(graph_make_communities_obj.get_particle_new_position(),pbest_position) ;
		
		current_location = new Vector<Float>();
		graph_particle.copy_vector_inside_vector(graph_make_communities_obj.get_particle_new_position(),current_location);
		
		//fitness function passed value of centroids and elements
		community_fitness_function_obj.fitness_function_calculate_for_community(graph_make_communities_obj.get_centroid_no_to_element_vec());
		pbest = community_fitness_function_obj.get_Q_value() ; 
	}
	
	public float get_pbest_value()
	{
		return pbest ; 
	}
	
	public Vector<Float> get_pbest_position()
	{
		return pbest_position ; 
	}
	
	public void set_velocity(float rando1 ,float rando2)
	{
		for (int counter_dimension=0; counter_dimension < current_velocity.size() ; counter_dimension++)
		{
			float prev_vel_val = current_velocity.get(counter_dimension);
			float curr_posi_val = current_location.get(counter_dimension);
			float pbest_val = pbest_position.get(counter_dimension);
			float gbest_val = gbest_position.get(counter_dimension);
			float random1 = rando1 ; 
			float random2 = rando2 ;
			float now_vel=velocity_cal_formula(prev_vel_val,curr_posi_val,pbest_val, gbest_val, random1 ,random2);
			current_velocity.set(counter_dimension, now_vel);
		}
	}
	
	public void set_position()
	{
		int half_size_of_curr_position_size = (current_location.size()/2) ; 
		for (int half_dime_count=0; half_dime_count < half_size_of_curr_position_size ; half_dime_count++)
		{
			float new_position = 
					position_cal_formula
					(current_location.get(half_dime_count), 
					 current_velocity.get(half_dime_count),
					 0.0f,  //get min value for that dimension //this all lower half always between 0 and 1
					 1.0f  // get max value for that dimension 
					 ) ; 
			current_location.set(half_dime_count, new_position);
		}
		for (int half_dime_count = half_size_of_curr_position_size ; half_dime_count < current_location.size() ; half_dime_count++)
		{
			float new_position = 
					position_cal_formula
					(current_location.get(half_dime_count), 
					 current_velocity.get(half_dime_count),
					 min_max_values_for_particle.get(0),  //get min value for that dimension //this all lower half always between 0 and 1
					 min_max_values_for_particle.get(1)  // get max value for that dimension 
					 ) ; 
			current_location.set(half_dime_count, new_position);
		}
	}
	
	public void set_pbest()
	{
		float temp_pbest ; 
		graph_make_communities_obj.graph_cluster_at_every_pso_iteration(current_location) ; 
		current_location = new Vector<Float>(); 
		graph_particle.copy_vector_inside_vector(graph_make_communities_obj.get_particle_new_position(),current_location) ;
		
		community_fitness_function_obj.fitness_function_calculate_for_community(graph_make_communities_obj.get_centroid_no_to_element_vec());
		temp_pbest = community_fitness_function_obj.get_Q_value() ; 
		
		if (temp_pbest > pbest)   //here we require bigger value as possible as
		{
			pbest = temp_pbest ; 
			pbest_position = new Vector<Float> ();
			graph_particle.copy_vector_inside_vector(current_location, pbest_position);
		}
	}
	
	//taken from swarm.particle last function in same project another package
	
	public float velocity_cal_formula(float pre_vel,float curr_posi_val, float pbest_val , float gbest_val , float random1 , float random2)
	{
		float curr_dimension_velocity = (
		setting_for_all_graph.w *  pre_vel + 
		setting_for_all_graph.c1 * random1 * (gbest_val - curr_posi_val) +
		setting_for_all_graph.c2 * random2* (pbest_val -  curr_posi_val)
		); 
		return curr_dimension_velocity ;
	}
	
	public float position_cal_formula(float pre_val , float curr_vel, float min_value, float max_value)
	{
		float curr_dimension_position = pre_val + curr_vel ; 
		// sometime particle is 2,3 .. times so take modulo 
		// and subtract so it is placed in boundary again
		float interval_between_max_and_min = max_value - min_value ; 
		//If particle go out of bound, wrap it around boundary
		//less than minimum
		if (curr_dimension_position < min_value)
		{
			float temp_value_reduced_from_max = min_value - curr_dimension_position ; 
			// modulo taken because if its magnitude is high wraps takes that much time
			curr_dimension_position = max_value - (temp_value_reduced_from_max % interval_between_max_and_min) ; 
			//System.out.println("wrapping "+ pre_val+" " + curr_vel +" " + min_value +" " + max_value + " => "+ curr_dimension_position);
		}
		//greater than maximum
		else if (curr_dimension_position > max_value)
		{
			float temp_value_increased_from_min = curr_dimension_position -max_value ; 
			curr_dimension_position  = min_value + (temp_value_increased_from_min % interval_between_max_and_min ) ;
			//System.out.println("wrapping "+ pre_val+" " + curr_vel +" " + min_value +" " + max_value + " => "+ curr_dimension_position);
		}
		return curr_dimension_position ; 
	}
	
	public static void copy_vector_inside_vector(Vector<Float> src_vec ,Vector<Float> des_vec )
	{
			for(int count_dime=0 ; count_dime < src_vec.size() ; count_dime++)
			{
				des_vec.add(src_vec.get(count_dime));
			}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		graph_settings.INFINITY = Float.MAX_VALUE ;  
		
		// as explained in paper expermiment section 
		graph_settings g1 = new graph_settings(0.65f, 2.05f , 2.05f , 1000 , 20);
		
		//for all this setting will be applied
		graph_particle.setting_for_all_graph = g1 ; 
		
		//read graph matrix from some where and pass here
		Vector<Vector<Float>> graph_matrix =   new Vector<Vector<Float>>() ; 
		
		// big facebook id to small int mapping for matrix assignment
		HashMap<String, Integer> big_facebook_id_to_small_id = new HashMap<String, Integer>() ; 
		
		//back mapping after community forms to get user belongs to which comminty
		HashMap<Integer, String> small_id_to_big_facebook_id = new HashMap<Integer, String>(); 
		
		int mapping_count = 0 ; 
		
		database_connectivity d1 = new database_connectivity("localhost", "3306", "facebook_database", "root", "pratik");
		d1.query_set_to_prepare_statement("select user_id,friend_id from FriendConnection ; ");
		
		ResultSet r1 = d1.execute_set_query() ; 
		try {
			while(r1.next())
			{
				String user = r1.getString("user_id") ;
				String frnd_id = r1.getString("friend_id") ;
				//add to hash table if entry is not there
				if (!big_facebook_id_to_small_id.containsKey(user))
				{
					big_facebook_id_to_small_id.put(user, mapping_count);
					small_id_to_big_facebook_id.put(mapping_count, user);
					mapping_count++ ; 
				}
				//same way add for friend
				if (!big_facebook_id_to_small_id.containsKey(frnd_id))
				{
					big_facebook_id_to_small_id.put(frnd_id, mapping_count);
					small_id_to_big_facebook_id.put(mapping_count, frnd_id);
					mapping_count++ ; 
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//matrix of connections of friends in Social n/w
		float asd[][]= new float[mapping_count][mapping_count];
		
		//d1.query_set_to_prepare_statement("select user_id,friend_id from FriendConnection ; ");
		r1 = d1.execute_set_query() ;
		try {
			while(r1.next())
			{
				String user = r1.getString("user_id") ;
				String frnd_id = r1.getString("friend_id") ;
				//add to hash table if entry is not there
				if (big_facebook_id_to_small_id.containsKey(user) && big_facebook_id_to_small_id.containsKey(frnd_id)  )
				{
					asd[big_facebook_id_to_small_id.get(user)][big_facebook_id_to_small_id.get(frnd_id)] = 1 ; 
					asd[big_facebook_id_to_small_id.get(frnd_id)][big_facebook_id_to_small_id.get(user)] = 1 ; 
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		d1.closedatabaseconnection() ; 
		
		/*
		 *For now add nodes here 
		 *after all development please read here from some file 
		 */
		//matrix

		//karate data set
		//****** make following eigen value vector 3 *********
		//float [][] asd = {{0,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,0,1,0,1,0,1,0,0,0,0,0,0,0,0,0,1,0,0}, {1,0,1,1,0,0,0,1,0,0,0,0,0,1,0,0,0,1,0,1,0,1,0,0,0,0,0,0,0,0,1,0,0,0}, {1,1,0,1,0,0,0,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1,0}, {1,1,1,0,0,0,0,1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,1}, {0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, {1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1}, {0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1}, {1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1}, {1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,1,0,0,1,1}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,1,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1}, {0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1}, {0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,1,1}, {0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1}, {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,1,0,0,0,1,1}, {0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,1,0,0,1,0,1,0,1,1,0,0,0,0,0,1,1,1,0,1}, {0,0,0,0,0,0,0,0,1,1,0,0,0,1,1,1,0,0,1,1,1,0,1,1,0,0,1,1,1,1,1,1,1,0} };

		//dolphin dataset
		//********** make following eigen value vector 4  **********
		//float [][] asd ={{0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, {0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0}, {0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,1,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0}, {0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0}, {0,0,0,0,0,1,1,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0}, {1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,1,1,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0}, {1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,1,0,0,1,1,0,1,0,0,1,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0}, {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0}, {0,1,0,0,0,0,1,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,1,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,1,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0}, {0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,1,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0}, {0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,0,1,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0}, {0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1,0,0,0,1,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0}, {1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0}, {0,1,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0}, {1,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0}, {0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,1,0,0,1,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,1,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0}, {1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,1,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, {0,1,0,0,0,0,1,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,1,1,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0} };
		
		
		//small example
		//float[][] asd  = {{0,1,1,0,0,1},{1,0,1,0,0,0},{1,1,0,0,0,0},{0,0,0,0,1,1},{0,0,0,1,0,1},{1,0,0,1,1,0}};
		
		Vector<Vector<Float>> aa = new Vector<Vector<Float>>() ; 
		for (int k=0 ; k< asd.length ; k++)
		{
			Vector<Float> pp = new Vector<Float>(); 
			for(int j=0 ; j<asd[k].length ; j++)
			{
				pp.add(asd[k][j]);
			}
			aa.add(pp);
		}
		//===================================

		/*
		 * ENDS HERE 
		 */
		 
		graphdata graphdata_obj = new graphdata(aa) ; 
		/*
		//====================  *** DELETE THIS *** =====================================
		while(graphdata_obj.get_sorted_eigen_value().size() > 3)
		{
			graphdata_obj.sorted_eigen_value.remove(3);
			graphdata_obj.sorted_eigen_vector.remove(3);
		}
		//=================== DELETE THIS ENDS ==================================
		*/
		// graph make community obj making
		graph_make_communities graph_make_communities_obj = new graph_make_communities
					(graphdata_obj.get_sorted_eigen_value(), graphdata_obj.get_sorted_eigen_vector());
		
		System.out.println(graphdata_obj.get_sorted_eigen_value());
		System.out.println(graphdata_obj.get_sorted_eigen_vector());
		
		// assign this for all particle (eigen value vector same for all particle)
		graph_particle.graph_make_communities_obj = graph_make_communities_obj ; 
		
		//graph fitness obj making 
		community_fitness_function community_fitness_function_obj = new community_fitness_function
					(graphdata_obj.get_graph_matrix());
	
		// assigen this fitness obj for all particle (Graph will be same for all particle)
		graph_particle.community_fitness_function_obj = community_fitness_function_obj ; 
		
		
		//random particle giver object
		random_particle_giver random_particle_giver_obj = new random_particle_giver(graphdata_obj.get_sorted_eigen_value(), graphdata_obj.get_sorted_eigen_vector());
		
		// assigen min max value
		graph_particle.min_max_values_for_particle =  random_particle_giver_obj.min_max_value ; 
		
		System.out.println("Min Max Value" + graph_particle.min_max_values_for_particle);
		
		//particle value assignment 
		Vector<graph_particle> particles_as_cen = new Vector<graph_particle>(); 
		for(int particle_counter= 0 ; particle_counter < graph_particle.setting_for_all_graph.no_of_particle ; particle_counter++)
		{
			particles_as_cen.add(random_particle_giver_obj.give_random_particle());
		}
	
		// we want now max value
		graph_particle.gbest = Float.MIN_VALUE ;
		for(graph_particle partin : particles_as_cen)
		{
			if (partin.get_pbest_value() > graph_particle.gbest)
			{
				graph_particle.gbest = partin.get_pbest_value() ; 
				graph_particle.gbest_position = new Vector<Float>(partin.get_pbest_position());
			}
		}
		
		for (int count_iteration=0 ; count_iteration < graph_particle.setting_for_all_graph.max_no_of_iterations  ; count_iteration++)
		{
			float rando1 = (float)Math.random() ; 
			float rando2 = (float)Math.random() ; 
			for(graph_particle partin : particles_as_cen)
			{
				partin.set_velocity(rando1, rando2) ; 
				partin.set_position();
				partin.set_pbest() ; 
			}
			
			for (graph_particle partin : particles_as_cen)
			{
				if (partin.get_pbest_value() > graph_particle.gbest) // we wenat value to be maximize
				{
					graph_particle.gbest = partin.get_pbest_value() ; 
					graph_particle.gbest_position = new Vector< Float >();
					graph_particle.copy_vector_inside_vector(partin.get_pbest_position(), graph_particle.gbest_position);
				}
			}
		}
		
		System.out.println("Graph community error : "+graph_particle.gbest);
		System.out.println(graph_particle.gbest_position);
		//gbest position passed
		
		graph_particle.graph_make_communities_obj.graph_cluster_at_every_pso_iteration(graph_particle.gbest_position);
		//print centroids and respective grouping
		System.out.println(graph_particle.graph_make_communities_obj.get_centroid_no_to_element_vec());
		
		database_connectivity dd1 = new database_connectivity("localhost", "3306", "facebook_database", "root", "pratik");
				
		dd1.query_set_to_prepare_statement("truncate table CommunityConnection ; ");
		dd1.execute_update_query() ; 
		
		int community_no_counter = 0 ; 
		for (Vector<Integer> every_community : graph_particle.graph_make_communities_obj.get_centroid_no_to_element_vec().values())
		{
			//for every community
			for (int one_man : every_community)
			{
				dd1.query_set_to_prepare_statement("insert into CommunityConnection values ( '" + community_no_counter + "' , '" + small_id_to_big_facebook_id.get(one_man) +"' ) ; " );
				dd1.execute_update_query();
			}
			community_no_counter++ ; 
		}
		
		dd1.closedatabaseconnection() ; 
	}
		
	
}
