package graph_clustering;

public class graph_settings {
	int no_of_particle ; 
	float w ; //inertia
	float c1 , c2 ; 
	int max_no_of_iterations ; 
	static float INFINITY ;  // assign max float value by Float.MAX_VALUE
	
	public graph_settings( float w, float c1 , float c2, int max_no_of_iter , int no_of_particle)
	{
		//this.no_of_dimension = no_of_dimension ; 
		this.w = w ; 
		this.c1 = c1 ; 
		this.c2 = c2 ;
		this.max_no_of_iterations = max_no_of_iter ; 
		this.no_of_particle = no_of_particle ; 
	}
}
