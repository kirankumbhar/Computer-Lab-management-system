import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.text.*;
import java.sql.Timestamp;
public class Mtserver extends JFrame implements ActionListener{
	static int online=0;
	static int a[] = new int[50];
	static long tstamp[] = new long[50];
	static String s[] = new String[50];
	static String pcno[] = new String[50];
	static String IP[] = new String[50];
	static int i=1,flag=0,j,temp;
	static String str;
	JPanel pan;
	static ImageIcon icon , dcicon;
	
	static JButton pcbtn[];
	public Mtserver(String title){
		super(title);
		pan = new JPanel();
		GridLayout layout = new GridLayout(4, 3);
		layout.setVgap(50);
		layout.setHgap(50);
		pan.setLayout(layout);

		icon = new ImageIcon("symbol.png");
		
		dcicon = new ImageIcon("dc.png");
		pcbtn = new JButton[15];
		
		for(j=1;j<=12;j++){
			pcbtn[j] = new JButton();
		}
		
		add(pan);
		for(j=1;j<=12;j++){
			pan.add(pcbtn[j]);
		}
		
		for(j=1;j<=12;j++){
			pcbtn[j].addActionListener(this);
		}

		
		
		
	}
	
	public void actionPerformed(ActionEvent ae){
		for(j=1;j<=12;j++){
			if(ae.getSource()==pcbtn[j]){
				if(a[j]==1){
					try{
						showFile(j);
					}
					catch(IOException e){
						e.printStackTrace();
					}
				}
			}	
		}
		
		
	}
	public static String process(String str , String ip){
		String[] lines = str.split("\\n");
		String tmp= "";
		if(str.contains("mouse")){
			tmp += "Mouse : Connected \n";
			int lineno = 0;
			for(;lineno<lines.length;lineno++){
				if(lines[lineno].equals("mouse:")){
					break;
				}
			}
		tmp += "Mouse model :" + (lines[lineno + 1] + "\n").replaceAll("\\s+", " ") + "\n";
			
		}
		if(str.contains("keyboard")){
			tmp += "Keyboard : Connected \n";
			int lineno = 0;
			for(;lineno<lines.length;lineno++){
				if(lines[lineno].equals("keyboard:")){
					break;
				}
			}
		tmp += "Keyboard model :" + (lines[lineno + 1] + "\n").replaceAll("\\s+", " ") + "\n";	
		}
		if(str.contains("graphics card")){
//			tmp += "Graphics card: Connected \n";
			int lineno = 0;
			for(;lineno<lines.length;lineno++){
				if(lines[lineno].equals("graphics card:")){
					break;
				}
			}
		tmp += "Graphics card:" + (lines[lineno + 1] + "\n").replaceAll("\\s+", " ") + "\n";
			
		}
		if(str.contains("sound")){
//			tmp += "sound card : Connected \n";
			int lineno = 0;
			for(;lineno<lines.length;lineno++){
				if(lines[lineno].equals("sound:")){
					break;
				}
			}
		tmp += "Sound card :" + (lines[lineno + 1] + "\n").replaceAll("\\s+", " ") + "\n";	
		}
		tmp += "IP : " + ip + "\n";
		tmp += "CPU " + (lines[lines.length - 1] + "\n").replaceAll("\\s+", " ") + "\n";
		tmp += "OS " + (lines[lines.length - 2] + "\n").replaceAll("\\s+", " ") + "\n";
		tmp += "Total Memory : " + lines[lines.length - 3] + "GB" + "\n";
		
		return tmp;
	}
	public static void showFile(int j)throws IOException{
		Date date = new Date(tstamp[j]);
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String dateFormatted = "Last connected : " + formatter.format(date);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		long currenttime = timestamp.getTime();
		long timediff = currenttime - tstamp[j];
		JTextArea textArea;
		System.out.println(timediff + " diff");
		if(timediff > 20*60*1000){
			//1*60*1000 = 1 minute
		textArea = new JTextArea("PC : " + pcno[j] + "\n" + process(s[j] , IP[j]) + dateFormatted + "\nSystem is no longer connected");
			pcbtn[j].setIcon(dcicon);
			pcbtn[j].setText("PC "+pcno[j]+" disconnected");
			pcbtn[j].setForeground(Color.RED);

			pcbtn[j].setVerticalTextPosition(SwingConstants.BOTTOM);

            		pcbtn[j].setHorizontalTextPosition(SwingConstants.CENTER);
		}else{
		textArea = new JTextArea("PC : " + pcno[j] + "\n" + process(s[j] , IP[j]) + dateFormatted);
			pcbtn[j].setForeground(Color.BLACK);
		}
		JScrollPane scrollPane = new JScrollPane(textArea);  
		textArea.setLineWrap(true);  
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false); 
		scrollPane.setPreferredSize( new Dimension( 400, 400 ) );
		JOptionPane.showMessageDialog(null, scrollPane, "PC HARDWARE INFORMATION", JOptionPane.PLAIN_MESSAGE);
		return;
	}

		

	
	public static void main(String args[]){		
		try{
			Mtserver mt= new Mtserver("Lab Management");
		mt.setVisible(true);
		mt.setSize(700,800);
		mt.setDefaultCloseOperation(EXIT_ON_CLOSE);
		ServerSocket ss=new ServerSocket(6666 );
		
			//  for(int k;k<14;k++){
			 // }
			while(online!=13){
			Socket s=ss.accept();
			ServerThread st=new ServerThread(s);				
			}			
			ss.close();  
		}
		catch(Exception e){System.out.println(e);}    
	}

static class ServerThread extends Thread{
	String tempstr;
	Socket sock;
	int index = 0;
	DataInputStream dis;
	public ServerThread(Socket s){
		sock = s;
		try{
		dis=new DataInputStream(s.getInputStream());
		this.start();
		}
		catch(Exception e){
		System.out.println(e);
		}
	}
	public void run(){
	try{
		tempstr=dis.readUTF();
		if(Arrays.asList(pcno).contains(tempstr)){
			for(index = 1;index < 13;index++){
				if(pcno[index].equals(tempstr)){
					break;
			}
		}
		}else{
				index = online+1;
			online++;
			}
		
		for(int t = 0; t<13; t++){
		}
		tstamp[index] = dis.readLong();
		pcno[index] = tempstr;
		s[index]=(String)dis.readUTF();
		IP[index]= sock.getRemoteSocketAddress().toString();
//		System.out.println(i);
		a[index]=dis.read();
		dis.close();
//		System.out.println(a[temp]);
//		if(a[index]==1){
		
			pcbtn[index].setIcon(icon);
			pcbtn[index].setText("PC "+pcno[index]+" connected");
			pcbtn[index].setVerticalTextPosition(SwingConstants.BOTTOM);
			pcbtn[index].setForeground(Color.BLACK);
            pcbtn[index].setHorizontalTextPosition(SwingConstants.CENTER);

			
//		}

	}
	catch(Exception e){
		System.out.println(e);
		e.printStackTrace();
	}
	}	
}
}


 
