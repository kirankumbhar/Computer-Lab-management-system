import java.io.*;  
import java.net.*;
import java.util.*;
import java.sql.Timestamp;
public class client {  
public static void main(String[] args) {
	String str = "";
	if(args.length == 0){
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter PC name : ");
		str = sc.nextLine();
	}
Script t = new Script();
try{
t.runscript(); 
Timestamp timestamp = new Timestamp(System.currentTimeMillis());
Socket s=new Socket("localhost",6666);
String contents = new Scanner(new File("info.txt")).useDelimiter("\\Z").next();
int active=1;	
DataOutputStream dout=new DataOutputStream(s.getOutputStream());
	if(args.length == 0){
		dout.writeUTF(str);
	}else{
		dout.writeUTF(args[0]);
	}
dout.writeLong(timestamp.getTime());
dout.writeUTF(contents);
dout.write(active);  
dout.flush();  
dout.close();  
s.close();  
}catch(Exception e){System.out.println(e);}  
}  
}  
