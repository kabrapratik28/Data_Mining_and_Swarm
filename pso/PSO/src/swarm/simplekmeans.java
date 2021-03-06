package swarm;

import java.util.Vector;

import dataset.csv_file_creator;
import dataset.csv_to_arff;

import weka.clusterers.SimpleKMeans ;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource ;  

public class simplekmeans {
	SimpleKMeans kmeans ; 
	public simplekmeans(Vector< Vector<Float> > data_to_be_clustered, int set_seed , int no_of_cluster)
	{
		//generating csv and arff 
		//arff required by weka (arff input is csv)
		csv_file_creator csv_temp = new csv_file_creator("temporaryfile.csv", data_to_be_clustered) ;
		csv_to_arff arff_temp = new csv_to_arff("temporaryfile.csv","temporaryfile.arff") ; 
		
		kmeans_calculations(set_seed , no_of_cluster);
		
		//deleting csv and arff file (weka work is done)
		csv_temp.delete_csv() ; 
		arff_temp.delete_arff_file() ; 
	}
	
	public void kmeans_calculations(int set_seed , int no_of_cluster)
	{
		Instances data = null;
		try {
			data = DataSource.read("temporaryfile.arff");  // data instances read from arff file
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error : Loading of arff file");
		}
		
		// kmeans class object 
		kmeans = new SimpleKMeans();
		//no of intial seed set // by default it is 10 
		kmeans.setSeed(set_seed);
		// This is the important parameter to set
		kmeans.setPreserveInstancesOrder(true);
		try {
			// no of cluster to be formed are set // by default 2
			kmeans.setNumClusters(no_of_cluster);
			// actualy building cluster on given data from arff with prev. settings
			kmeans.buildClusterer(data) ; 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Number of cluster cannot set");
		}
		
	}
	
	public Vector< Vector<Float> > get_centroids()
	{
		Vector< Vector<Float> > centroidsvector = new Vector< Vector<Float> >() ; 
		
		Instances centroids = kmeans.getClusterCentroids() ;
		for (int i = 0; i < centroids.numInstances(); i++) { 

			/*
			 * Data instance converted to string  
			 * ASK SIR WHAT IS RIGHT WAY 
			 */
			String centroid_comma_seprated = centroids.instance(i).toString() ; 
			String[] centroid_indivusal = centroid_comma_seprated.split(",") ; 
			Vector<Float> one_centroid_of_data  = new Vector<Float>();
			
			for (String indi_cen : centroid_indivusal )
			{
				one_centroid_of_data.add(Float.parseFloat(indi_cen)) ; 
			}
			centroidsvector.add(one_centroid_of_data) ; 
		}
		
		return centroidsvector ; 
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//test
		/*
		Vector< Vector<Float> > a = new Vector<Vector<Float>>() ; 
		Vector<Float> b = new Vector<Float>() ; 
		b.add((float)1.23); 
		b.add((float)3.43) ; 
		b.add((float)5.67) ;
		a.add(b) ; 
		
		b = new Vector<Float>() ; 
		b.add((float)7.21); 
		b.add((float)9.18) ; 
		b.add((float)8.41) ;
		a.add(b) ; 
		
		simplekmeans s1 = new simplekmeans(a, 10, 2) ; 
		System.out.println(s1.get_centroids()); 
		*/
	}

}
