package graph_clustering;

import java.util.*;
import java.util.Vector;
import Jama.Matrix;
import Jama.EigenvalueDecomposition;

public class graphdata {
	
	Vector<Vector<Float>> nodes_linkage_graph ; 
    Vector<Float> eigen_value ; 
    Vector<Vector<Float>> eigen_vector ; 
	
    Vector<Float> sorted_eigen_value ; 
    Vector<Vector<Float>> sorted_eigen_vector ;
    
    Vector<Vector<Float>> graph_matrix ; 
    
    Matrix graph_matrix_jama ; 
    EigenvalueDecomposition eigen_values_of_matrix ; 
    double [] eigen_values_jama ; 
    double [][] eigen_vectors_jama ; 
    double[][] graph_matrix_array ; 
    double[][] digonal_matrix_array ;
    
	public graphdata(Vector<Vector<Float>> graph_matrix)
	{
		//apply some java eigen value package here 
		//to calculate eigen value and vector
		//pass to sort egen valu and vector
		//pass to extract positive eigen value and respective vector

		this.graph_matrix = graph_matrix ; 
		
		// create diagonal matrix
		//square matrix
		digonal_matrix_array = new double[graph_matrix.size()][graph_matrix.size()] ;
		for (int count_row = 0 ; count_row < graph_matrix.size() ; count_row++)
		{
			double sum_of_row = 0 ;  
			for(int count_clm=0; count_clm < graph_matrix.get(count_row).size() ; count_clm++)
			{
				sum_of_row = sum_of_row + Double.parseDouble(Float.toString(this.graph_matrix.get(count_row).get(count_clm)));
			}
			//digonal value setting
			digonal_matrix_array[count_row][count_row] = sum_of_row ; 
		}
		
		Matrix digonal_matrix_jama = new Matrix(digonal_matrix_array);
		//take inverse matrix 
		Matrix inv_digonal_matrix_jama = digonal_matrix_jama.inverse() ; 
		

		//convert vector to array of graph matrix values
		graph_matrix_array  = new double[this.graph_matrix.size()][] ; 
		
		for (int count_row_of_matrix=0; count_row_of_matrix < this.graph_matrix.size() ; count_row_of_matrix++)
		{
			double[] row_of_matrix = new double[this.graph_matrix.get(count_row_of_matrix).size()] ;
			for (int clm_of_matrix = 0 ; clm_of_matrix < this.graph_matrix.get(count_row_of_matrix).size() ; clm_of_matrix++)
			{
				
				row_of_matrix[clm_of_matrix] = Double.parseDouble(Float.toString(this.graph_matrix.get(count_row_of_matrix).get(clm_of_matrix)));
			}
			graph_matrix_array[count_row_of_matrix] = row_of_matrix ;
		}
			
		graph_matrix_jama = new Matrix(graph_matrix_array);

		//multiply inv(digonal) * weight matrix i.e. passed by user  ... paper ==> {inv(D)*W}  
		//eigen value of this
		Matrix normal_matrix = inv_digonal_matrix_jama.times(graph_matrix_jama);
		
		//normal matrix eigenvalue
		eigen_values_of_matrix = new EigenvalueDecomposition(normal_matrix) ;
		eigen_values_jama = eigen_values_of_matrix.getRealEigenvalues() ;
		eigen_vectors_jama = eigen_values_of_matrix.getV().getArray() ; 
		eigen_value = new Vector<Float>() ; 
		eigen_vector = new Vector<Vector<Float>>();
		
		//getting +ve eigen vectors
		for (int count_eigen_value = 0 ; count_eigen_value < eigen_values_jama.length ; count_eigen_value++)
		{
			if(eigen_values_jama[count_eigen_value] > 0)
			{
				eigen_value.add((float)eigen_values_jama[count_eigen_value]);
				Vector<Float> eigen_vector_for_eigen_value = new Vector<Float>();
				for(int count_row_eigen_vec=0 ; count_row_eigen_vec < eigen_vectors_jama.length ; count_row_eigen_vec++)
				{
					eigen_vector_for_eigen_value.add((float)eigen_vectors_jama[count_row_eigen_vec][count_eigen_value]);
				}
				eigen_vector.add(eigen_vector_for_eigen_value);
			}
		}
		
		//sorted eigen values and vectors
		sorted_eigen_value = new Vector<Float>() ; 
		sorted_eigen_vector = new Vector<Vector<Float>>();
		sort_eigen_value_eigen_vector_in_descending_order() ; 
	}

	public void sort_eigen_value_eigen_vector_in_descending_order()
	{
		int size_of_eigen_val = eigen_value.size() ; 
		for (int count_eigen_vec = 0 ; count_eigen_vec < size_of_eigen_val ; count_eigen_vec++)
		{
			 Object maximum_value_obj = Collections.max(eigen_value);
			 float maximum_value  = (float)maximum_value_obj ; 
			 int index_to_be_added_to_sorted_and_removed_from_nonsorted = eigen_value.indexOf(maximum_value_obj);
			 // max eigen value at every iteration added
			 // and that deleted so next time max value selected from remaining values
			 
			 //take only nontrivial vectors
			 if (maximum_value != 1.0f)
			 {
				 sorted_eigen_value.add(maximum_value);
				 //System.out.println(maximum_value);
				 sorted_eigen_vector.add(eigen_vector.get(index_to_be_added_to_sorted_and_removed_from_nonsorted));
			 }
			 
			 // now delete respective eigen value and vector
			 eigen_value.remove(index_to_be_added_to_sorted_and_removed_from_nonsorted);
			 eigen_vector.remove(index_to_be_added_to_sorted_and_removed_from_nonsorted);
		}
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
		/*
		Vector<Vector<Float>> aa = new Vector<Vector<Float>>();
		Vector<Float>  bb = new Vector<Float>();
		bb.add(0f);
		bb.add(0f);
		bb.add(1f);
		bb.add(1f);
		bb.add(0f);
		bb.add(1f);
		aa.add(bb);

		bb = new Vector<Float>();
		bb.add(0f);
		bb.add(0f);
		bb.add(0f);
		bb.add(1f);
		bb.add(1f);
		bb.add(0f);
		aa.add(bb);
		
		bb = new Vector<Float>();
		bb.add(1f);
		bb.add(0f);
		bb.add(0f);
		bb.add(0f);
		bb.add(0f);
		bb.add(1f);
		aa.add(bb);
		
		bb = new Vector<Float>();
		bb.add(1f);
		bb.add(1f);
		bb.add(0f);
		bb.add(0f);
		bb.add(1f);
		bb.add(0f);
		aa.add(bb);
		
		bb = new Vector<Float>();
		bb.add(0f);
		bb.add(1f);
		bb.add(0f);
		bb.add(1f);
		bb.add(0f);
		bb.add(0f);
		aa.add(bb);
		
		bb = new Vector<Float>();
		bb.add(1f);
		bb.add(0f);
		bb.add(1f);
		bb.add(0f);
		bb.add(0f);
		bb.add(0f);
		aa.add(bb);
		
		graphdata g1 = new graphdata(aa);
		System.out.println(g1.sorted_eigen_value);
		System.out.println(g1.sorted_eigen_vector);
		
		*/

	
	}
	
}
