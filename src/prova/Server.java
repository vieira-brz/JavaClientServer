package prova;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket; 

public class Server {

	public static void main(String[] args) throws IOException 
	{
		try (ServerSocket serverSocket = new ServerSocket(4000)) 
		{
			System.out.println("Aguardando envio...");
			Socket socket = serverSocket.accept();

	        byte[] objectAsByte = new byte[socket.getReceiveBufferSize()];
	        BufferedInputStream bufferIS = new BufferedInputStream(socket.getInputStream());
	        
	        bufferIS.read(objectAsByte);

	        Arquivo arquivo = (Arquivo) getObjectFromByte(objectAsByte);
	    
	        String diretorio = Server.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "dados/";

	        File dir = new File(diretorio);
	        
	        //Criar pasta dados se não existir.
	        if(!dir.exists()) 
	        {
	        	try 
	        	{
					dir.mkdir();
				} 
	        	catch (Exception e) 
	        	{
					e.getMessage();
				}
	        }
	
	        System.out.println("Escrevendo no diretório: " + diretorio + arquivo.getName());

	        FileOutputStream fileOS = new FileOutputStream(diretorio + arquivo.getName());
	        fileOS.write(arquivo.getContent());
	        fileOS.close();
	        socket.close();
	        serverSocket.close();
	        
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private static Arquivo getObjectFromByte(byte[] objByte) 
	{
	     Object object = null;
	     
	     ByteArrayInputStream byteIS = null;
	     ObjectInputStream objectInputStream = null;
	     
	     try 
	     {
	    	byteIS = new ByteArrayInputStream(objByte);
	    	objectInputStream = new ObjectInputStream(byteIS);
	        object = objectInputStream.readObject();
	        
	        byteIS.close();
	        objectInputStream.close();
	     } 
	     catch (IOException e) 
	     {
	        e.printStackTrace();
	     } 
	     catch (ClassNotFoundException e) 
	     {
	        e.printStackTrace();
	     }
	     
	     return (Arquivo) object;
	}
	
}