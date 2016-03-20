import java.io.*;
import java.awt.image.*;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

class Steganography extends JFrame implements ActionListener
{
	/*-------------GUI------------------*/
	JLabel l; // For encode window and decode window
	JTextArea ta,tb,password_textarea, decode_password_textarea;
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem menuItem,exit;
	JLabel lname,limage,dname,dimage;
	JFileChooser jfc;
	JFrame decode_password_frame;
	JButton decode_okbutton;

	// Behind the screen
	JFrame behindj;
	JTextArea bta,btb,btc;
	JLabel bl1,bl2,bl3,password_label;

	/*----------Data Members------------*/
		  String text,key, decodekey;
		  File f;
		  BufferedImage image_org,image,readImage;
		  Graphics2D graphics;
		  WritableRaster raster;
		  DataBufferByte buffer;		
	int unsetcode[]={0x01,0x02,0x04,0x08,0x10,0x20,0x40,0x80};
	int setcode[]={0xFE,0xFD,0xFB,0xF7,0xEF,0xDF,0xBF,0x7F};
	/*---------Constructor-------------*/
	Steganography()
	{
	    super("Steganography");
	    l=new JLabel("Decode window");
	    ta=new JTextArea("Your code"); 
		tb=new JTextArea("Enter your code");
		lname=new JLabel("Real image");
		limage=new JLabel("Image");
		lname.setBounds(20,20,100,20);
		limage.setBounds(20,40,200,150);
		dname=new JLabel("New image");
		dimage=new JLabel("Image");
		dname.setBounds(320,20,100,20);
		dimage.setBounds(320,40,200,150);
		menuBar=new JMenuBar();
		menu=new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_M);
		menuBar.add(menu);
		menuItem=new JMenuItem("Encode",KeyEvent.VK_E);
		menuItem.addActionListener(this);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,ActionEvent.ALT_MASK));
		menu.add(menuItem);
		menu.addSeparator();
		menuItem=new JMenuItem("Decode",KeyEvent.VK_D);
		menuItem.addActionListener(this);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,ActionEvent.ALT_MASK));
		menu.add(menuItem);
		menu.addSeparator();
		exit=new JMenuItem("exit");
		jfc=new JFileChooser();

		/*--------- For key ---------*/
		password_label=new JLabel("Enter the key");
		password_textarea=new JTextArea();
		password_label.setBounds(20,125,200,150);
		password_textarea.setBounds(20,210,200,100);
		add(password_label);	
		add(password_textarea);
		/*------------------------*/

		exit.addActionListener(this);
		menu.add(exit);

        add(lname);
        add(limage);
		add(dname);
        add(dimage);
        menuBar.add(menu);
		setJMenuBar(menuBar);
		l.setBounds(350,300,300,50); ta.setBounds(350,350,300,250);  // Decode window and text area
		add(l); add(ta);
		l=new JLabel("Encode window");
        l.setBounds(5,300,300,50); tb.setBounds(5,350,300,250);   // Encode window and text area
		add(l); add(tb);
        setLayout(null); setSize(700,700);
		setVisible(true);
    }

	/*--------------------------------------MAIN  METHOD-------------------------*/
	public static void main(String[] args) throws Exception
	{
		new Steganography();
	}
	static int c=0;
	/*-----------------------------Encode---------------------*/
	public void encode_text(byte[] img,byte[] msg,int start)
	{
		 System.out.println("Before");
		  for(int i=32;i<40;i++)
					System.out.print(img[i]);
            System.out.println();
		
         
         for(int i=0;i<msg.length;i++)
			{
				Random r=new Random();
				byte rand=(byte)r.nextInt(7);
				System.out.println("Rand :"+rand);
				 for(int j=7;j>=0;j--,start++)
			       {
	                    byte b=rand; 
						b&=unsetcode[j];
						 b>>>=j;
						img[start]=(byte)((img[start]&0xFE)|b); 
				   }
				  // System.out.println("Massage :"+msg[i]);
				 for(int j=7;j>=0;j--,start++)
			       {
	                    byte b=msg[i]; 
						b&=unsetcode[j];	
						if(j>=rand)
							b>>>=j-rand;
						else
							b<<=rand-j;
						System.out.println("msg : "+(char)msg[i]);	 
						img[start]=(byte)((img[start]&setcode[rand])|b);
						System.out.println("With code: "+img[start]);	  
				   }  
			}
			System.out.println(++c);
	}

	/*--------------DECODE PASSWORD------------------*/
	public byte[] decode_password(byte[] img)
	{
		int len=0;
		for(int j=0,i=0;i<4;i++)
		{
	        byte rand=0;
	    	len=0;    
	        for(int k=0;k<8;k++,j++)   
	        { 
	            byte bit=(byte)(img[j]&1);
				//System.out.print(bit);
				bit<<=(7-k);
				rand|=bit;
			}
			//System.out.println(rand);

			for(int k=0;k<8;k++,j++)   
	        { 
	            int bit=(byte)(img[j]&unsetcode[rand]);
	            if(k>=rand)
							bit<<=(7-k-rand);
				else
							bit>>>=-(7-k-rand);
				len|=bit;
			}

		}
		System.out.println("length :"+len);
       	byte msg[]=new byte[len];
		for(int i=64,k=0;k<len;k++)
		{
			byte rand=0;
	        for(int j=0;j<8;i++,j++)   
	        { 
	            byte bit=(byte)(img[i]&1);
				//System.out.print(bit);
				bit<<=(7-j);
				rand|=bit;
			}

			for(byte j=0;j<8;i++,j++)   
	        { 
	            byte bit=(byte)(img[i]&unsetcode[rand]);
	            if((7-j-rand)>=0)
							bit<<=7-j-rand;
				else
							bit>>=-(7-j-rand);
				msg[k]|=bit;
			}
			System.out.println("msg : "+(char)msg[k]);
		}
		return (msg);
	}

	//---------------------------------------------- Method need to be alter
	public byte[] decode_text(byte[] img)
	{

		int len=0;
		for(int i=32+decodekey.length()*8;i<32+32+decodekey.length()*8;i++)
		{
            int bit=(img[i]&1);
			//System.out.print(bit);
			bit<<=(31-i);
			len|=bit;
		}
		//System.out.println("length :"+len);
        byte msg[]=new byte[len];
		for(int i=32+32+decodekey.length()*8,k=0;k<len;k++)
		{
			for(int j=7;j>=0;j--,i++)
			{
				msg[k]=(byte)((msg[k] << 1) | (img[i] & 1));
				//result[b] = (byte)((result[b] << 1) | (image[offset] & 1));
			}
			//System.out.println(msg[k]);
		}



		//System.out.println("length :"+msg.length);*/
		String s=new String("");
				for(int i=0;i<msg.length;i++)
				{
						s+=(char)msg[i];
					/*	if(i%5==0)
							s+="\n";
					*/
				}
			try{	// decryption algorithm throws an exception
					ta.setText(AESencrp.decrypt(s,decodekey));  // Convert cipher text to plain text
				}
				catch(Exception encep){ System.out.println("Decode Exception"); }
				
		return msg;
	}

	/*-------Length of data into bytes-------------*/
	public byte[] msgLength(int msg)
	{
		byte[] b=new byte[4];
		b[0]=(byte)(msg >> 24);
		b[1]=(byte)(msg >> 16);
		b[2]=(byte)(msg >> 8);
		b[3]=(byte)(msg );
		return b;
	}

	byte img[];

	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Encode"))
		{

		  int x=jfc.showOpenDialog(null);
		  if(x==JFileChooser.APPROVE_OPTION)
			{
				f=jfc.getSelectedFile();
			      try{
						 image_org=ImageIO.read(f);   // Raw File
		              }
			  catch(IOException er)	{	 System.out.println(er); }
        		/*---------------*/                                     // added here
			}

          if(x==JFileChooser.CANCEL_OPTION)
			{
			  return ;
			}
		  
		  key=password_textarea.getText();   // GET key from the text area
		  AESencrp.inputKey(key);            // send key to the algorithm for encryption
		  System.out.println(key);
		  byte keyBytes[]=key.getBytes();
		  byte keylen[]=msgLength(key.length());

		  try{  // encryption algorithm throws an exception
		  		text=AESencrp.encrypt(tb.getText());             // Converted to cipher
		  	}
		  	catch(Exception excep) { System.out.println("Throwing exception");} 

		  /*------------User Space---------*/
		  image=new BufferedImage(image_org.getWidth(),image_org.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
		  graphics=image.createGraphics();
		  graphics.drawRenderedImage(image_org,null);
		  graphics.dispose();
		  raster=image.getRaster();
		  buffer=(DataBufferByte)raster.getDataBuffer();

	      img=buffer.getData();
		  byte msg[]=text.getBytes();                       // cipher to bytes
		  byte len[]=msgLength(text.length());				// cipher length

				//	before encoding
							initbehindframe();
							String s=new String("  ");
									for(int i=0;i<100;i++)
										{
											s+=img[i]+" ";
											if(i!=0 && i%20==0)
												s+="\n";
						
										}
									bta.setText(s);
							

		  encode_text(img,keylen,0); // Hiding key length
		  encode_text(img,keyBytes,64);   // Hiding key

		  //encode_text(img,len,64+key.length()*8*2);   // From 32th byte its key after that our cipher length
				//	after hiding the length of message
							s=new String("  ");
									for(int i=0;i<100;i++)
										{
											s+=img[i]+" ";
											if(i!=0 && i%20==0)
												s+="\n";
						
										}
									btb.setText(s);
				
		  //encode_text(img,msg,64+key.length()*8*2+64);  // After hiding cipher length which occupies 32 bytes then hide the cipher
				//	after hiding the message
							s=new String("  ");
									for(int i=0;i<100;i++)
										{
											s+=img[i]+" ";
											if(i!=0 && i%20==0)
												s+="\n";
						
										}
									btc.setText(s);
			
		  limage.setIcon(new ImageIcon("abc.jpg"));
          /*-----------File Output--------------*/
          try{
            ImageIO.write(image,"png",new File("out.png"));
          } catch(Exception er) {}
		  dimage.setIcon(new ImageIcon("out.png"));
		}                                                 ////////// encode
			
		if(e.getActionCommand().equals("Decode"))
		{
           
		   int x=jfc.showOpenDialog(null);
		  if(x==JFileChooser.APPROVE_OPTION)
			{
				f=jfc.getSelectedFile();
			      try{
						 readImage=ImageIO.read(f);   // Raw File
		              }
			  catch(IOException er)	{	 System.out.println(er); }
        
			}

          if(x==JFileChooser.CANCEL_OPTION)
			{
			  return ;
			}
		  
			// USER SPACE
			image=new BufferedImage(readImage.getWidth(),readImage.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
		    graphics=image.createGraphics();
		    graphics.drawRenderedImage(readImage,null);
		    graphics.dispose();
            raster=image.getRaster();
		    buffer=(DataBufferByte)raster.getDataBuffer();

	        img=buffer.getData();                

	      //  ---------------- Initializing Decode Frame ------------------
	        decode_password_frame=new JFrame("Enter Decrypt key");
	        decode_password_textarea=new JTextArea(100,50);
	        decode_password_textarea.setBounds(10,10,150,40);
	        decode_password_frame.add(decode_password_textarea);
	        decode_okbutton=new JButton("OK");
	        decode_okbutton.setBounds(40,60,60,40);
	        decode_okbutton.addActionListener(this);
	        decode_password_frame.add(decode_okbutton);
	        decode_password_frame.setSize(200,200);
	        decode_password_frame.setLayout(null);
	        decode_password_frame.setVisible(true);
		}
			
		if(e.getActionCommand().equals("exit"))
		{
			System.exit(0);
		}

		if(e.getSource()==decode_okbutton)
		{
			byte[] dkey=decode_password(img);   // new method has added 
				for(int i=0;i<dkey.length;i++)
					System.out.print((char)dkey[i]);

			decodekey=new String(dkey);

			if( decodekey.equals(decode_password_textarea.getText()) )
			{
				System.out.println("PAssword matched");   // ************************* now i will decode the extract the cipher length and cipher
				decode_text(img);
			}
			else
			{
				System.out.println("Not matched");
			}
		}
	}
	
	// Initialize behind framey
	void initbehindframe()
	{
		behindj=new JFrame("Behind the screen");
		bta=new JTextArea();
		btb=new JTextArea();
		btc=new JTextArea();
		bl1=new JLabel("Before encode");
		bl2=new JLabel("After hiding the length of message");
		bl3=new JLabel("After hiding the massage");
		
		bl1.setBounds(5,10,100,10);
		bl2.setBounds(5,135,300,15);
		bl3.setBounds(5,270,200,15);
		
		bta.setBounds(10,30,470,100);
		btb.setBounds(10,155,470,100);
		btc.setBounds(10,290,470,100);

		behindj.add(bl1);
		behindj.add(bta);
		behindj.add(bl2);
		behindj.add(btb);
		behindj.add(bl3);
		behindj.add(btc);

		behindj.setSize(500,500);
		behindj.setLayout(null);
		behindj.setVisible(true);
		
	}

}
