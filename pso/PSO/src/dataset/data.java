package dataset;

import java.util.Vector;


public class data {
	long identity ;   // identity of dataset
	Vector<Float> data ; // float for data
	public void add_data(long iden , Vector<Float> datac)
	{
		identity  = iden ;
		data = new Vector<>(datac); // float for data object created with data given
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//test
		/*
		*
		*
		*Vector<dataset> mydata = new Vector<>(); 
		*
		long a =123 ; 
		Vector<Float> vect = new Vector<>(); 
		vect.add((float) 2.3);
		vect.add((float)4.5);
		dataset s1 = new dataset();
		s1.add_data(a, vect);
		
		*
		*mydata.add(s1);
		*
		
		a=234; 
		vect = new Vector<>(); 
		vect.add((float) 8.3);
		vect.add((float)9.5);
		s1 = new dataset();
		s1.add_data(a, vect);
		mydata.add(s1);
		
		System.out.println(mydata.get(0).data.get(0));
		System.out.println(mydata.get(1).data.get(1));
		*/
	}

}
