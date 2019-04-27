package BiasedMF;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Random;

public class BMF {
    
	public static void main(String[] args) {
		int i, j, x, k, K, n, m, step = 5000;
		double eij = 0, e;
		Scanner s = new Scanner(System.in);
		Random rd = new Random();
		double beta = 0.0002, lamda = 0.002;
		System.out.println("So User: ");
		n = s.nextInt();
		System.out.println("So Item: ");
		m =  s.nextInt();
		System.out.println("So nhan to: ");
		K = s.nextInt();
		
		double A[][] = new double[n][m];
		System.out.println("Ma tran User-Item: ");
		for(i = 0; i < n; i++) {
			for(j = 0; j < m; j++) {
				A[i][j] = s.nextDouble();
			}
		}
		double u = 0.00;
		for(i = 0; i < n; i++) {
			for(j = 0; j < m; j++) {
				u += A[i][j];
			}
		}
		u = u/n;
		//System.out.println(u);
		
		double bs[] = new double[n];
		for(i = 0; i < n; i++) {
			for(j = 0; j < m; j++) {
				bs[i] += A[i][j]-u;
			}
			bs[i] = bs[i]/n;
			//System.out.println(bs[i]+"\t");
		}
		
		double bi[] = new double[m];
		for(i = 0; i < m; i++) {
			for(j = 0; j < n; j++) {
				bi[i] += A[j][i]-u;
			}
			bi[i] = bi[i]/n;
			//System.out.println(bi[i]+"\t");
		}
		
		double[][] W= new double[n][K];
		for(i = 0; i < n; i++) {
			for(j = 0; j < K; j ++) {
				W[i][j] = rd.nextDouble();
			}
		}
		
		double H[][] = new double[K][m];
		for(i = 0; i < K; i++) {
			for(j = 0; j < m; j++) {
				H[i][j] = rd.nextDouble();
			}
		}
		double M[][] = new double[n][m];
		
		//MF
		e=0;
		for(x = 0; x < step; x++) {
			for(i = 0; i < n; i++) {
				for(j = 0; j < m; j++) {
					if(A[i][j] > 0) {
						M[i][j]=0;
						for(k = 0; k < K; k++) {
							M[i][j] += W[i][k]*H[k][j];
						}
						eij = A[i][j] - u - bs[i] - bi[j] - M[i][j];
						u = u + beta*eij;
						bs[i] = bs[i] + beta*(eij - lamda*bs[i]);
						bi[j] = bi[j] + beta*(eij - lamda*bi[j]);
						for(k = 0; k < K; k++) {
							W[i][k] = W[i][k] + beta*(2*eij*H[k][j]-lamda*W[i][k]);
							H[k][j] = H[k][j] + beta*(2*eij*W[i][k]-lamda*H[k][j]);
						}
					}
				}
			}
			for(i = 0; i < n; i++) {
				for(j = 0; j < m; j++) {
					if(A[i][j] > 0) {
						M[i][j]=0;
						for(k = 0; k < K; k++) {
							M[i][j] += W[i][k]*H[k][j];
						}
						e = e + Math.pow(eij , 2);
						for(k = 0; k < K; k++) {
							e = e + (lamda)*(Math.pow(W[i][k], 2)+Math.pow(H[k][j], 2)+Math.pow(bs[i], 2)+Math.pow(bi[j], 2));
						}
					}
				}
			}
			if (e < 0.001) break;
		}
		
		
		for(i = 0; i < n; i++) {
			for(j = 0; j < m; j++) {
				M[i][j] = 0;
					for(k = 0; k < K; k++) {
						M[i][j] += W[i][k]*H[k][j];
					}
				}
			}
		System.out.println();
		System.out.println("Ket qua: ");
		for(i = 0; i < n; i++) {
			System.out.println();
			for(j = 0; j < m; j++) {
				M[i][j] += u + bs[i] + bi[j];
				M[i][j] = Math.round(M[i][j]*1000.00)/1000.00;
				System.out.print(M[i][j]+"\t"); 
			}
		}
		
	}
}

//so user 5
//so item 4
//so nhan to 2
/*
5 3 0 1
4 0 0 1
1 1 0 5
1 0 0 4
0 1 5 4
*/