package dataset;

import java.util.Vector;

//xml file settings related to data 
public class dataset_and_parameter_settings {
	 public int option_of_file_or_database ; 
	 public String file_name ; 
	 public String database_name;
	 public String database_user_data_table_name ; 
	 public String database_max_data_table_name ; 
	 public String database_min_data_table_name ; 
	 public Vector<Integer> parameter_to_be_considered_pso ; 
	 public String liwc_feature_extraction_file ; 
	
	public dataset_and_parameter_settings(int option_of_file_or_database, String file_name, String database_name, String database_user_data_table_name,String database_min_data_table_name,String database_max_data_table_name , Vector<Integer> parameter_to_be_considered_pso, String liwc_feature_extraction_file )
	{
		this.option_of_file_or_database  = option_of_file_or_database ; 
		this.file_name = file_name ; 
		
		this.database_name = database_name ; 
		this.database_user_data_table_name = database_user_data_table_name ; 
		this.database_min_data_table_name = database_min_data_table_name ; 
		this.database_max_data_table_name = database_max_data_table_name ; 
		
		this.parameter_to_be_considered_pso = parameter_to_be_considered_pso ; 
		
		this.liwc_feature_extraction_file = liwc_feature_extraction_file ; 
	}
}
