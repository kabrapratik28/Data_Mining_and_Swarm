package swarm;

import java.util.Vector;

import dataset.Data_reader_from_file;

public class particle {	
	Vector< Vector<Float> > current_location ;  // now current velocity
	Vector< Vector<Float> > current_velocity ;  // now current velocity

	float pbest ;  
	Vector< Vector<Float> > pbest_position ;  // all dimension value for pbest value
	
	/*
	 * set after all particle value calculated
	 * initialization after all particle calculated
	 */
	static float gbest ; 
	static Vector< Vector<Float> > gbest_position ;  

	static settings setting_for_all ;  // setting for all particles (inertia, c1, c2 , no of cluster , dimension ,etc)
	
	
	//When object initializes position and velocity initial values set 
	public particle(Vector< Vector<Float> > intial_values ,Vector< Vector<Float> > intial_velocities  )
	{
		current_location = new Vector<Vector<Float> >();
		particle.copy_vector_inside_vector(intial_values, current_location);
		
		current_velocity = new Vector< Vector<Float> > ();
		particle.copy_vector_inside_vector(intial_velocities, current_velocity) ; 
		
		/*
		 * set pbest
		 */
		pbest_position = new Vector<Vector<Float> > (); //copy same dimension to pbest_position bz its for first time
		particle.copy_vector_inside_vector(current_location, pbest_position);
		
		swarm_based_clustering particle_cluster = new swarm_based_clustering(current_location);
		pbest = particle_cluster.get_quantization_error_value();
	}

	public float get_pbest_value()
	{
		return pbest ; 
	}
	
	public Vector<Vector<Float>> get_pbest_position()
	{
		return pbest_position ; 
	}
	
	public void set_velocity(float rando1 ,float rando2)
	{

		int vecsize_curr_velocity = current_velocity.size() ;
		for (int count_index = 0 ; count_index < vecsize_curr_velocity ; count_index++)
		{
			int dimension_size = current_velocity.get(count_index).size(); 
			for (int dimencount = 0 ; dimencount < dimension_size ; dimencount++)
			{
				float prev_vel_val = current_velocity.get(count_index).get(dimencount) ;  // this is old (below calculated is new) 
				float curr_posi_val = current_location.get(count_index).get(dimencount) ; 
				float pbest_val = pbest_position.get(count_index).get(dimencount);
				float gbest_val = gbest_position.get(count_index).get(dimencount);
				float random1 = rando1 ; 
				float random2 = rando2 ;
				float now_vel=velocity_cal_formula(prev_vel_val,curr_posi_val,pbest_val, gbest_val, random1 ,random2);
				current_velocity.get(count_index).set(dimencount, now_vel);  //this is new 
			}
		}
		
	}

	public void set_position()
	{
		int curr_position_size = current_location.size() ; 
		for (int count_position = 0 ; count_position < curr_position_size ; count_position++)
		{
			int get_dim_size = current_location.get(count_position).size() ; 
			for (int dime_counter = 0 ; dime_counter < get_dim_size ; dime_counter++)
			{
				float new_position = (
						current_location.get(count_position).get(dime_counter)+
						current_velocity.get(count_position).get(dime_counter));
				current_location.get(count_position).set(dime_counter, new_position);
			}
		}
	} 
	public void set_pbest()
	{
		float temp_pbest ; 
		swarm_based_clustering particle_cluster = new swarm_based_clustering(current_location);
		temp_pbest = particle_cluster.get_quantization_error_value();
		if (temp_pbest < pbest)
		{
			pbest = temp_pbest ; 
			pbest_position = new Vector<Vector<Float> > ();
			particle.copy_vector_inside_vector(current_location, pbest_position);
		}
	}
	public float velocity_cal_formula(float pre_vel,float curr_posi_val, float pbest_val , float gbest_val , float random1 , float random2)
	{
		float curr_dimension_velocity = (
		setting_for_all.w *  pre_vel + 
		setting_for_all.c1 * random1 * (gbest_val - curr_posi_val) +
		setting_for_all.c2 * random2* (pbest_val -  curr_posi_val)
		); 
		return curr_dimension_velocity ;
	}
	
	public float position_cal_formula(float pre_val , float curr_vel)
	{
		float curr_dimension_position = pre_val + curr_vel ; 
		return curr_dimension_position ; 
	}
	
	public static void copy_vector_inside_vector(Vector<Vector<Float>> src_vec ,Vector<Vector<Float>> des_vec )
	{
		int src_vec_size = src_vec.size();
		int count_vec_src = 0 ;
		for(count_vec_src=0 ; count_vec_src < src_vec_size ; count_vec_src++)
		{
			Vector<Float> clone = (Vector<Float>)src_vec.get(count_vec_src).clone();
			des_vec.add(clone);
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//algorithm based on 
		//Data Clustering using Particle Swarm Optimization DW van der Merwe 
		
		//settings exaplained in paper are used 
		/*
		 * SET NO OF DIMESIONS AND NO OF CLUSTER
		 * SET INFINITY VALUE
		 */
		settings.INFINITY = Float.MAX_VALUE ;  // max float value assigned 
		settings settings_for_now = new settings(3,4,(float)0.72,(float)1.49,(float)1.49);
		particle.setting_for_all = settings_for_now ; // set settings	

		Data_reader_from_file d1  = new Data_reader_from_file("iris.data",",") ; 
		swarm_based_clustering.points = d1.get_dataset() ; 
		random_particle_generator r1 = new random_particle_generator(settings_for_now.no_of_cluster, d1.get_min_and_max_value_of_dimensions()) ; 
	
		//particles information
		Vector<particle> particles_as_cen = new Vector<particle>() ;
		
		// 10 particle generating 
		for (int a =0  ; a< 10 ; a++)
		{
			particle p1 = r1.get_random_particle() ; 
			particles_as_cen.add(p1);
		}
		
		particle.gbest = -1 ; 
		for (particle partin : particles_as_cen)
		{
			if (particle.gbest== -1 || partin.get_pbest_value() < particle.gbest)
			{
				particle.gbest = partin.get_pbest_value() ; 
				particle.gbest_position = new Vector< Vector<Float> >(partin.get_pbest_position());
			}
		}
		System.out.println(particle.gbest);
		for (int a = 0 ; a<1000 ; a++)
		{
			float rando1 = (float)Math.random() ; 
			float rando2 = (float)Math.random() ; 
			for(particle partin : particles_as_cen)
			{
				partin.set_velocity(rando1, rando2) ; 
				partin.set_position();
				partin.set_pbest() ; 
			}
			for (particle partin : particles_as_cen)
			{
				if (partin.get_pbest_value() < particle.gbest)
				{
					particle.gbest = partin.get_pbest_value() ; 
					particle.gbest_position = new Vector< Vector<Float> >();
					particle.copy_vector_inside_vector(partin.get_pbest_position(), particle.gbest_position);
				}
			}
		}
		System.out.println(particle.gbest);
		System.out.println(particle.gbest_position);
}

}
