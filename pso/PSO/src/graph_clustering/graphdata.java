package graph_clustering;

import java.util.Vector;

public class graphdata {
	
	Vector<Vector<Float>> nodes_linkage_graph ; 
    Vector<Float> eigen_value ; 
    Vector<Vector<Float>> eigen_vector ; 
	
    Vector<Float> sorted_eigen_value ; 
    Vector<Vector<Float>> sorted_eigen_vector ;
    
    Vector<Vector<Float>> graph_matrix ; 
    
	public graphdata(Vector<Vector<Float>> graph_matrix)
	{
		this.graph_matrix = graph_matrix ; 
		//apply some java eigen value package here 
		//to calculate eigen value and vector
		//pass to sort egen valu and vector
		//pass to extract positive eigen value and respective vector
	}
	
	public void extract_positive_eigen_value_and_related_vector()
	{
		
	}
	
	public void sort_eigen_value_eigen_vector_in_descending_order()
	{
		
	}
	
	public Vector<Float> get_sorted_eigen_value()
	{
		return sorted_eigen_value ; 
	}
	
	public Vector<Vector<Float>> get_sorted_eigen_vector()
	{
		return sorted_eigen_vector ; 
	}
		
	public Vector<Vector<Float>> get_graph_matrix()
	{
		return graph_matrix ; 
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
