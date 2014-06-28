package swarm;

import weka.clusterers.SimpleKMeans ;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource ;  

public class simplekmeans {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Instances data = null;
		try {
			data = DataSource.read("iris.arff");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error : Loading of arff file");
		}
		
		
		SimpleKMeans kmeans = new SimpleKMeans();
		kmeans.setSeed(10);
		// This is the important parameter to set
		kmeans.setPreserveInstancesOrder(true);
		try {
			kmeans.setNumClusters(3);
			kmeans.buildClusterer(data) ; 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Number of cluster cannot set");
		}
		
		Instances centroids = kmeans.getClusterCentroids() ;
		for (int i = 0; i < centroids.numInstances(); i++) { 
			System.out.print("Centroid ");
			System.out.print (i + 1) ;
			System.out.print( ": ") ;
			System.out.println(centroids.instance(i) );
		} 
		
	}

}
