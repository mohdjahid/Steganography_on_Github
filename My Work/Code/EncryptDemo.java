class EncryptDemo
{
	int pbkey,pvkey;
	int P=5,Q=13,Z,N=P*Q,E=7,D=7;

	int encrypt(int m)
	{
		//N=P*Q;
		Z=(P-1)*(Q-1);
		int C=((int)Math.pow(m,E))%(int)N;
		System.out.println(C);
		return C;
	}

	public static void main(String[] args)
	{
		EncryptDemo e=new EncryptDemo();
		float x=e.encrypt(22);
		//System.out.println(e.N);
		System.out.println( ( Math.pow(x,e.D))%65 );

	}
}
