package dataset;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

public class data_reader_from_file {

	/**
	 * @param args
	 */
	Vector < Vector<Float> > dataset ;
	String filename ;
	String delimiter ; 
	public data_reader_from_file(String filename, String delimiter) {
		dataset =  new Vector<Vector<Float> >();
		this.filename = filename ; 
		this.delimiter = delimiter ; 
		read_file_complete();
	}
	
	public void read_file_complete()
	{
		BufferedReader buffer_reader = null;
		try {
			buffer_reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Error : File Not Found");
		}
		
		String line_by_line_read;
		
		try {
			while ((line_by_line_read = buffer_reader.readLine()) != null) {
			   // process the line.
				add_data_from_line(line_by_line_read); 
			}
			buffer_reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error : IO Exception");
		}
		
	}
	
	
	public void add_data_from_line(String line_of_data)
	{
		String[] tokens = line_of_data.split(delimiter); 
		Vector<Float> data_element = new Vector<Float>();
		
		/* CAUTIONS :  
		 * Take care when parse because other things like string
		 * throw exception while parse if they are not numbers. 
		 */
		for (String each_value : tokens)
		{
			try{
			float value_in_float = Float.parseFloat(each_value);
			data_element.add(value_in_float);
			}catch (NumberFormatException e) {
			    //System.out.println("String is not a float");
			}
		}
		dataset.add(data_element);
	}
	
	public Vector < Vector<Float> > get_dataset()
	{
		return dataset ; 
	}
	
	int get_dimension_of_data_set()
	{
		int dimension_of_data_size = dataset.get(0).size() ;  // considering all element size is same
		return dimension_of_data_size ; 
	}
	
	public Vector< Vector<Float> > get_min_and_max_value_of_dimensions()
	{
		Vector<Float> min_values = new Vector<Float>();
		Vector<Float> max_values = new Vector<Float>();
		Vector< Vector<Float> > min_max = new Vector< Vector<Float> >();
		
		int total_data_set_instances = dataset.size();
		
		int total_dimension_size = this.get_dimension_of_data_set() ; 
		int counter_of_dimension= 0 ; 
		for(counter_of_dimension=0 ; counter_of_dimension < total_dimension_size ; counter_of_dimension++)
		{
			Vector <Float> curr_dimension_data_from_all_instances = new Vector<Float>(); 
			for (int count_data_size=0 ; count_data_size < total_data_set_instances ; count_data_size++)
			{
				// get 1st element from all vector
				// then 2nd from all and so on .. 
				curr_dimension_data_from_all_instances.add(dataset.get(count_data_size).get(counter_of_dimension));
			}
			
			// get minimum and maximum along curr_dimension_data_from_all_instance
			float minimum_along_all = Collections.min(curr_dimension_data_from_all_instances);
			float maximum_along_all = Collections.max(curr_dimension_data_from_all_instances);
			min_values.add(minimum_along_all);
			max_values.add(maximum_along_all);
			
		}
		
		min_max.add(min_values);
		min_max.add(max_values);
		return min_max ; 
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//test
		/*
		data_reader_from_file dr = new data_reader_from_file("wine.data", ",");
		System.out.println(dr.get_dimension_of_data_set());
		System.out.println("=====");
		
		for (Vector<Float>ss : dr.get_dataset())
		{
			for (float a : ss)
			{
				System.out.println(a);
			}
		}
	
		for (Vector<Float> dd : dr.get_min_and_max_value_of_dimensions()) 
		{
			for (float ee : dd){
			System.out.println(ee);
			}
			System.out.println("======= max");
		}
		*/
	}

}
