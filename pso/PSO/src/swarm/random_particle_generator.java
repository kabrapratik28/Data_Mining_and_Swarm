package swarm;



import java.util.Random;
import java.util.Vector;
//import dataset.Data_reader_from_file;

public class random_particle_generator {
	Vector<Float> min_of_dataset ; 
	Vector<Float> max_of_dataset ; 
	int no_of_cluster  ; 
	Random random ; 
	public random_particle_generator(int no_of_cluster , Vector< Vector<Float> > min_max_of_dataset)
	{
		this.no_of_cluster = no_of_cluster ; 
		min_of_dataset = new Vector<Float>(min_max_of_dataset.get(0)); // get all min elements
		max_of_dataset = new Vector<Float>(min_max_of_dataset.get(1)); // get all max elements 
		random = new Random();
	}
	
	public particle get_random_particle()
	{
		
		Vector< Vector<Float> > position_of_particle = new Vector< Vector<Float> >() ; 
		Vector< Vector<Float> >	velocity_of_particle = new Vector< Vector<Float> >() ; 
		
		for (int count_cluster = 0 ; count_cluster < no_of_cluster ; count_cluster++)
		{
			Vector<Float> curr_dime_position = new Vector<Float>() ; 
			Vector<Float> curr_dime_veloc = new Vector<Float>() ; 
			for (int dimension_counter = 0 ; dimension_counter < min_of_dataset.size() ; dimension_counter++)
			{
				float curr_min_value =  min_of_dataset.get(dimension_counter) ;
				float curr_max_value =   max_of_dataset.get(dimension_counter) ; 
				float curr_pos_coordinate_value = 
						random_no_giver_in_range( curr_min_value , curr_max_value ); 
				
				float curr_vel_coordinate_value = 
						random_no_giver_in_range( (curr_max_value-curr_min_value),(curr_min_value-curr_max_value) );
				
				curr_dime_position.add(curr_pos_coordinate_value);  // current coordinate position set
				curr_dime_veloc.add(curr_vel_coordinate_value);   // current coordinate velocity set
				
			}
			position_of_particle.add(curr_dime_position); 
			velocity_of_particle.add(curr_dime_veloc);
		}
		
		particle random_new_particle = new particle(position_of_particle, velocity_of_particle) ; 
		return random_new_particle ; 
	}
	
	float random_no_giver_in_range(float min_value , float max_value)
	{
		float new_random_number_in_range = random.nextFloat() * (max_value-min_value) + min_value ; 
		return new_random_number_in_range ; 
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Test data 
		/* 
		data_reader_from_file d1  = new data_reader_from_file("abc",",") ; 
		swarm_based_clustering.points = d1.get_dataset() ; 
		random_particle_generator r1 = new random_particle_generator(3, d1.get_min_and_max_value_of_dimensions()) ; 
		for (int a = 0 ; a<10 ; a++)
		{
			particle p1 = r1.get_random_particle() ; 
		}
		*/
	}
}
