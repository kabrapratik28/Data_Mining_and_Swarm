package dataset;

import java.util.Vector;

public class dataset {
	Vector<data> alldata ; 
	public dataset()
	{
		alldata = new Vector<>() ;
	}
	public void add_data_instance(long ident , Vector<Float> dataval)
	{
		data data_ins = new data();
		data_ins.add_data(ident, dataval);
		alldata.add(data_ins);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	/*TEST CASE
	 * 	dataset d1 = new dataset() ; 
		Vector<Float> c1 =  new Vector<>();
		c1.add((float) 2.33);
		c1.add((float) 4.55);
		d1.add_data_instance(11, c1) ; 
		
		Vector<Float> c2 =  new Vector<>();
		c2.add((float) 9.33);
		c2.add((float) 11.55);
		d1.add_data_instance(16, c2) ; 
		System.out.println(d1.alldata.get(0).identity);
		System.out.println(d1.alldata.get(1).identity);
		System.out.println(d1.alldata.get(1).data.get(1));
	*/
	}

}
