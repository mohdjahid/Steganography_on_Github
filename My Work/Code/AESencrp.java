import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.spec.*;
import javax.crypto.*;
import sun.misc.*;

class AESencrp 
{
	private static final String ALGO="AES";
	public static byte[] keyValue;

	
	public static void inputKey(String s)   /* ---------- Input by the user during encryption and decryption-----*/
	{
		keyValue=s.getBytes();
	}
           
	public static byte[] getKey()
	{
		return keyValue;
	}

	public static String encrypt(String Data) throws Exception
	{
		Key key=generateKey();
		Cipher c=Cipher.getInstance(ALGO); 
		System.out.println("Throw now");
		c.init(Cipher.ENCRYPT_MODE,key);
		byte[] encVal=c.doFinal(Data.getBytes());
		String encryptedValue=new BASE64Encoder().encode(encVal);
		return encryptedValue;
	}
	
	private static Key generateKey() throws Exception   /*Generate key on the basis of the user input password*/
	{
		Key key=new SecretKeySpec(getKey(),ALGO);
		return key;
	}

	public static String decrypt(String encryptedData,String Receivekey) throws Exception
	{
		inputKey(Receivekey);
		Key key=generateKey();
		Cipher c=Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE,key);
		byte[] decodedValue=new BASE64Decoder().decodeBuffer(encryptedData);
		byte[] decValue=c.doFinal(decodedValue);
		return new String(decValue);
	}
	
	public static void main(String[] args) throws Exception
	{
		
		String Data="JAHID";
		String DataEnc=encrypt(Data);
		String DataDec=decrypt(DataEnc,"key");

		System.out.println("Plain Text : "+Data);
		System.out.println("Cipher Text : "+DataEnc);
		System.out.println("Decypted Text : "+DataDec);		
	}
}
