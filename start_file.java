import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class start_file{
	public static int hidden_units=5,training_examples,N_var,N_emp=10,param=5,a=0,b=1,x_dim;
	public static String filename="C:/eclipse/NASA.txt";
	public static void main(String args[]) throws IOException{
				
		N_var=(param+1)*hidden_units+(hidden_units+1); //electromagnets
		
		double R_rate=0.5;
		double Ps_rate=0.5;
		double P_field=0.3; //portion of +ve field-good soln
		double N_field=0.3; //portion of -ve field-bad soln
		int min=0; 
		int max=5; 
		double phi=1.6180339887498948;
		
		int i,j;
		double fit[]=new double[N_emp]; // fitness array; 1 fitness for 1 e-particle
		double new_emp[]=new double[N_var]; // 1 new_emp is 1 e-particle
		double[][] em_pop=new double[N_emp][N_var]; //2D array of any e-particle of further e-magnet
		
		//Initializing field
		for (i=0;i<N_emp;i++){
			for (j=0;j<N_var;j++){

				em_pop[i][j]=getRandomNumberInRange(0,20);
				new_emp[j]=em_pop[i][j];
				//System.out.println(em_pop[i][j]);
			}
			
			fit[i]=fitness(new_emp); //according to problem
			//System.out.println("Fitness# "+i+" "+fit[i]);
		}
		//System.out.println(fit.length);
		
		sortPopulation(em_pop,fit); //sorts 2D em_pop in order of fitness value
		
		//for (i=0;i<10;i++){
		//	System.out.print(fit[i]+"  ");
		//}
		int RI=0,I_pos,I_neg,I_neu,iteration=10;
		while(iteration!=0/*condition for stopping loop*/){
			double force=Math.random();

			for (i=0;i<N_var;i++){

				I_pos=getRandomNumberInRange(0,(int)Math.floor(N_emp*P_field)-1); //0 to 3
				I_neg=getRandomNumberInRange((int)Math.floor((1-N_field)*N_emp)-1,N_emp-1); //6 to 9
				I_neu=getRandomNumberInRange((int)Math.ceil((int)Math.ceil(N_emp*P_field)-1),(int)((1-N_field)*N_emp)-1); //2 to 6
				
				if(Math.random()<Ps_rate){
					new_emp[i]=em_pop[I_pos][i];
				}
				else{
					new_emp[i]=em_pop[I_neu][i]+phi*force*(em_pop[I_pos][i]-em_pop[I_neu][i])-force*(em_pop[I_neg][i]-em_pop[I_neu][i]);
				}
				if (new_emp[i]>max||new_emp[i]<min){
					new_emp[i]=min+(int)(Math.random())*(max-min);
				}
			}
			if ((int)(Math.random())<R_rate){
				new_emp[RI]=min+Math.random()*(max-min);
				RI++;
				if(RI>N_var-1){
					RI=0;
				}
			}
			double new_fit=fitness(new_emp);
			if (new_fit<worst(fit,N_emp)){
				insertIntoSortedPopulation(new_emp,em_pop,new_fit,fit);
			}

			iteration--;
		}
		System.out.println("RESULT: ");
		System.out.println("Minimum Error-Fitness Value# "+fit[0]);
		System.out.println("Optimal Weights # ");
		for (int m=0;m<N_var;m++){
			System.out.print(em_pop[0][m]+" ");
		}
	}
	
	public static double fitness(double[] new_emp) throws IOException{
			
		BufferedReader br1 = null;
        String line = "";
        br1 = new BufferedReader(new FileReader(start_file.filename));
        while ((line = br1.readLine()) != null) {
        	training_examples++;
        }
        br1.close();
        
		x_dim=training_examples-param;
		double matrix[][]=new double[x_dim][param+1],h[]=new double[x_dim];
		matrix=(new data_arrange()).make_dataset();
		double x_dataset[][]=new double[x_dim][param],y_dataset[]=new double[x_dim]; //4 features, 1372 examples
		
		//System.out.println(x_dim+" "+matrix.length);
		for(int i=0;i<x_dim;i++){
			for (int j=0;j<param;j++){
				x_dataset[i][j]=matrix[i][j];
			}
		}
		for(int i=0;i<x_dim;i++){
			y_dataset[i]=matrix[i][param];
		}
		
		
		int i,end,k=0; //predicted y
		//calculate h from equation of NN
		//System.out.println(new_emp.length);
		
		end=(param+1)*hidden_units;
		//System.out.println(end);
		
		double weight1[][]=new double[param+1][hidden_units],weight2[][]=new double[hidden_units+1][1];
		
		for (i=0;i<end;i++){
			if (i%(hidden_units)==0&&i!=0){
				k++;
			}
			weight1[k][i%(hidden_units)]=new_emp[i];
			//System.out.print(new_emp[i]+" ");
			
		}
		//System.out.println(weight1.length+" "+weight1[0].length);
		
		k=0;
		//System.out.println(new_emp.length-end);
		for (i=end;i<new_emp.length;i++){
			weight2[k][0]=new_emp[i];
			//System.out.print(weight2[0][k]+" ");
			k++;
		}
		for (i=0;i<x_dim;i++){
			double ip_mat[][]=new double[1][param+1],act_matrix[][]=new double[1][hidden_units],op_matrix[][]=new double[1][1];
			ip_mat[0]=x_dataset[i];
			
			functions obj=new functions();
			ip_mat=obj.add_one(1,param, ip_mat);
			
			act_matrix=obj.multiplication(1,param+1,param+1,hidden_units,ip_mat,weight1);
			
			act_matrix=obj.sigmoid(1,hidden_units,act_matrix);
			
			act_matrix=obj.add_one(1,hidden_units, act_matrix);
			
			op_matrix=obj.multiplication(1,hidden_units+1,hidden_units+1,1,act_matrix,weight2);
			
			op_matrix=obj.sigmoid(1,1,op_matrix);
			
			h[i]=op_matrix[0][0];
		}
		
		double sumf=0;
		for (i=0;i<x_dim;i++){
			sumf=sumf+(h[i]*h[i]+y_dataset[i]*y_dataset[i]-2*h[i]*y_dataset[i]);
		}
		
		double f_value=sumf/x_dim;
		return f_value;
	}
	
	public static void sortPopulation(double[][] em_pop, double[] fit){
		int i,j,pos,flag;
		double min,temp;
		double[][] temp1=new double[1][10];
		for (i=0;i<fit.length-1;i++){
			min=fit[i];
			flag=0;
			pos=i;
			for(j=i+1;j<fit.length;j++){
				if(fit[j]<min){
					min=fit[j];
					pos=j;
					flag=1;
				}
				if (flag==1){
					temp=fit[i];
					temp1[0]=em_pop[i];
					fit[i]=fit[pos];
					em_pop[i]=em_pop[pos];
					fit[pos]=temp;
					em_pop[pos]=temp1[0];
				}
			}
		}
	}
	
	public static void insertIntoSortedPopulation(double[] new_emp,double[][] em_pop,double new_fit,double[] fit){
		int i;
		for(i=0;i<fit.length-1;i++){
			if(fit[i]>new_fit)
				break;
		}
		for(int k=fit.length-2; k>=i; k--){
			fit[k+1]=fit[k];  
			em_pop[k+1]=em_pop[k];
		}
		fit[i]=new_fit;
		em_pop[i]=new_emp;
		
	}
	
	
	public static double worst(double[] fit,int N_emp){
		return fit[N_emp-1];
	}
	
	public static int getRandomNumberInRange(int min, int max) {

		if (min>=max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
}