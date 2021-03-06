package dataset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class csv_file_creator {
	PrintWriter writer ; 
	String filename ; 
	public csv_file_creator(String filename, Vector< Vector<Float> > data_to_be_written)
	{
		this.filename = filename ; 
		create_file(filename); 
		int size_of_header = data_to_be_written.get(0).size();
		add_header(size_of_header) ;
		write_data_to_csv(data_to_be_written) ; 
		writer.close() ; 
	}
	
	public void create_file(String filename)
	{
		try {
		    writer = new PrintWriter(filename, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Error : file not found");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.out.println("Error : Unsupported Encoding Exception");
		}
	}
	
	public void add_header(int size_of_header) 
	{
		String header_given = "" ; 
		for (int count_number = 0 ; count_number < size_of_header ; count_number++)
		{
			header_given = header_given + String.valueOf(count_number)+"," ; 
		}
		// strip last comma 
		header_given = header_given.replaceAll("[, ]+$", "");
		
		writer.println(header_given) ; 
	}
	
	public void write_data_to_csv(Vector<Vector<Float> > Data)
	{
		for (Vector<Float> datarow : Data)
		{
			String row_to_be_write = "" ; 
			for (float each_value : datarow)
			{
				row_to_be_write = row_to_be_write + String.valueOf(each_value) + "," ;  
			}
			row_to_be_write = row_to_be_write.replaceAll("[, ]+$", "") ;  // strip last ,(comma)
			writer.println(row_to_be_write) ; 
		}
	}
	
	public void delete_csv()
	{
		try{
			 
    		File file = new File(filename);
 
    		if(!file.delete()){
    			System.out.println("Error : Delete operation is failed.");
    		}
    	}catch(Exception e){
    		System.out.println("Error : csv file delete operation fail.");;
    	}
 
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//test
		/*

		Vector<Vector<Float>> a= new Vector<Vector<Float>>() ;
		Vector<Float> b = new Vector<Float>() ; 
		b.add((float)2.3); 
		b.add((float)3.3); 
		b.add((float)5.3);
		a.add(b); 
		
		b = new Vector<Float>() ;
		b.add((float)1.1); 
		b.add((float)9.1); 
		b.add((float)5.1);
		a.add(b) ; 
		csv_file_creator c1 = new csv_file_creator("bk.csv", a);
		//c1.delete_csv() ;
  
		*/
	}

}
