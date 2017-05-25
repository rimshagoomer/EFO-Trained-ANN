
public class functions{
   public double[][] do_transpose(int rows,int cols,double matrix[][])
   {
      int m=rows,n=cols,c, d;
      double transpose[][] = new double[n][m];
      for ( c = 0 ; c < m ; c++ )
      {
         for ( d = 0 ; d < n ; d++ )               
            transpose[d][c] = matrix[c][d];
      }
      return transpose;
   }
   public double[][] add_one(int rows,int cols,double matrix[][])
   {
	   int j;
       double new_mat[][]=new double[1][cols+1];
	   for(j=0;j<cols;j++){
    		new_mat[0][j]=matrix[0][j];  
       }
	  new_mat[0][j]=1;
	  return new_mat;
   }
   public double[][] sigmoid(int rows,int cols,double x[][])
   {
	   for (int i=0;i<rows;i++){
		   for (int j=0;j<cols;j++){
			   x[i][j]=1 / (1 + Math.exp(-x[i][j]));
		   }
	   }
       return x;
   }
   
   public double[][] multiplication(int row1,int col1,int row2,int col2, double first[][],double second[][])
   {
      int m=row1, n=col1, p=row2, q=col2,  c, d, k;
      double sum=0;
      if ( n != p ){
         System.out.println("Matrices with entered orders can't be multiplied with each other.");
         return new double[1][1];
      }else
      {
         double multiply[][] = new double[m][q];
 
         for ( c = 0 ; c < m ; c++ )
         {
            for ( d = 0 ; d < q ; d++ )
            {   
               for ( k = 0 ; k < p ; k++ )
               {
                  sum = sum + first[c][k]*second[k][d];
               }
 
               multiply[c][d] = sum;
               sum = 0;
            }
         }
         return multiply;
      }
   }
}