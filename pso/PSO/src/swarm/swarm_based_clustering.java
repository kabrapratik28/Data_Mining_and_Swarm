package swarm;

import java.util.Vector;

public class swarm_based_clustering {
	Vector< cluster > ClusterFormation ; // all cluster are store of current Cluster
	public float cal_euclidean_distance(Vector<Float> data_position , Vector<Float> centroid_position)
	{
		float distance=0 ; 
		float sumofsquare = 0  ; 
		int data_position_vec_size = data_position.size() ; 
		for (int counter_position_size = 0 ; counter_position_size < data_position_vec_size ; counter_position_size++)
		{
			float diffrence_of_position = data_position.get(counter_position_size) - centroid_position.get(counter_position_size);
			sumofsquare = sumofsquare + (float)Math.pow(diffrence_of_position, 2);
		}
		distance = (float)Math.sqrt(sumofsquare);
		return distance ; 
	}
	
	public float cal_quantization_error(Vector<cluster> whole_cluster)
	{
		/*  *NOT HANDLED YET HANDLE THIS *
		 * BE CAUTIONS : about any cluster can get zero elements 
		 * suppose one particle near to all points
		 * another particle is not near to any so 0/0 can occur
		 */
		float je_quantization_error = 0 ; 
		float je_quantization_error_denometer = 0 ; 
		int no_of_cluster = whole_cluster.size() ; 
		for (int count_cluster=0 ; count_cluster < no_of_cluster ; count_cluster++ )
		{
			float distance_total_by_one_cluster = 0 ; 
			int total_no_of_point_in_cluster = whole_cluster.get(count_cluster).near_points.size() ; 
			for (int point_counter = 0 ; point_counter < total_no_of_point_in_cluster; point_counter++)
			{
				distance_total_by_one_cluster  = 
						distance_total_by_one_cluster  + 
						cal_euclidean_distance(whole_cluster.get(count_cluster).near_points.get(point_counter),whole_cluster.get(count_cluster).centroid);
			}
			je_quantization_error_denometer  = je_quantization_error_denometer + (distance_total_by_one_cluster/total_no_of_point_in_cluster); 
		}
		je_quantization_error = je_quantization_error_denometer  / no_of_cluster ; 
		return je_quantization_error ; 
	}
	
	public void assign_point_to_cluster(Vector< Vector <Float> > position_of_centroids, Vector< Vector<Float> > points)
	{
		int size_of_centroids = position_of_centroids.size() ; 
		//Cluster objects made and added to object vector
		for (int count_centroids=0 ; count_centroids < size_of_centroids ; count_centroids++)
		{
			cluster c1 = new cluster(position_of_centroids.get(count_centroids));
			ClusterFormation.add(c1);
		}
		
		// each point now calculate distance and assigned to cluster object respectively
		int size_of_points = points.size() ; 
		for (int count_on_point = 0 ; count_on_point < size_of_points; count_on_point++)
		{
			float closest_distance_from_cluster = -1 ; 
			int index_of_closest_centroid = -1 ; 
			int size_of_centroi = ClusterFormation.size() ;
			// look for which is closest centroid 
			for (int counter_on_centroi = 0 ; counter_on_centroi < size_of_centroi ; counter_on_centroi++)
			{
				float current_dis_from_curr_centroid = 
						cal_euclidean_distance(points.get(count_on_point), ClusterFormation.get(counter_on_centroi).centroid); 
				if (closest_distance_from_cluster == -1 )
				{
					closest_distance_from_cluster = current_dis_from_curr_centroid ; 
					index_of_closest_centroid = counter_on_centroi ; 
				}
				else if (closest_distance_from_cluster > current_dis_from_curr_centroid)  // if distance is smaller then only update 
				{
					closest_distance_from_cluster = current_dis_from_curr_centroid ; 
					index_of_closest_centroid = counter_on_centroi ; 
				}
			}
			
			//assign point to cluster according to closest centroid and its resp. index
			ClusterFormation.get(index_of_closest_centroid).add_point(points.get(count_on_point)) ; 
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
