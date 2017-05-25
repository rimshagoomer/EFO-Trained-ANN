import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class data_arrange {
 
    public double[][] make_dataset() throws IOException{
    	
        BufferedReader br = null;
        String line = "";
        String splitBy = "	";
        int k=0;        
        br = new BufferedReader(new FileReader(start_file.filename));
        
        double array[]=new double[start_file.training_examples];
        while ((line = br.readLine()) != null) {
                // use comma as separator
        		String[] one_example = line.split(splitBy);
                array[k]=Double.parseDouble(one_example[1]);
                k++;
        }
        //NORMALISATION
        double xmax=getmax(array),xmin=getmin(array);
		for (int i=0;i<array.length;i++){
				array[i]=start_file.a+((array[i]-xmin)*(start_file.b-start_file.a)/(xmax-xmin));
		}
        br.close();
        
        double matrix[][]=new double[start_file.x_dim][start_file.param+1];
        for (int i=0;i<start_file.x_dim;i++){
    		for (int j=0;j<=start_file.param;j++){
    			matrix[i][j]=array[i+j];
    		}
    	}
//        System.out.println(matrix.length+" x "+matrix[0].length);
        return matrix;
    }
    
    public static double getmax(double array[]){
    	double max=-99999;
    	for(int i=0;i<array.length;i++){
    		if (array[i]>=max){
    			max=array[i];
    		}
    	}
    	return max;
    }
    public static double getmin(double array[]){
    	double min=99999;
    	for(int i=0;i<array.length;i++){
    		if (array[i]<=min){
    			min=array[i];
    		}
    	}
    	return min;
    }
}
        