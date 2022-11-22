package prova;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import prova.Arquivo; 

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;
	private long sizeAllowed = 10240;
	
	private JPanel contentPane;
	private Arquivo arquivo;

	
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					Client cliente = new Client();
					cliente.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	
	public Client() 
	{
		setTitle("Prova - FTP");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 463, 253);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Caminho:");
		lblNewLabel.setBounds(36, 26, 105, 19);
		contentPane.add(lblNewLabel);
		
		JTextField file_path = new JTextField();
		file_path.setBounds(36, 46, 284, 20);
		contentPane.add(file_path);
		file_path.setColumns(10);
		
		
		// Nome e tamanho do arquivo
		JLabel lblNomeArquivo = new JLabel("Arquivo: ");
		lblNomeArquivo.setBounds(36, 77, 256, 14);
		contentPane.add(lblNomeArquivo);
		
		JLabel lblTamanhoArquivo = new JLabel("Tamanho: ");
		lblTamanhoArquivo.setBounds(36, 95, 256, 14);
		contentPane.add(lblTamanhoArquivo);
		
		
		//Botão de Enviar arquivo. 
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.setEnabled(false);
		btnEnviar.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				sendFileServer();
			}
		});
		btnEnviar.setBounds(173, 180, 89, 23);
		contentPane.add(btnEnviar);

		
		// Evento do botão		
		final JButton btnNewButton = new JButton("Buscar\r\n");
		btnNewButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				FileInputStream fileInputStream;
				
				try 
				{
					JFileChooser selectedFile = new JFileChooser();
					selectedFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
					selectedFile.setDialogTitle("Selecione um arquivo");
			        int i = selectedFile.showSaveDialog(null);
			   
				    if (i == 1)
				    {
				    	JOptionPane.showMessageDialog(contentPane, "Arquivo não selecionado, tente de novo!");
				    	file_path.setText("");
						btnEnviar.setEnabled(false);
				    }
				    else 
				    {
				    	File file = selectedFile.getSelectedFile();
				        file_path.setText(file.getPath());
				        btnEnviar.setEnabled(true);
					  	long kbSize = file.length() / 1024;
						lblNomeArquivo.setText("Arquivo: " + file.getName());
						lblTamanhoArquivo.setText("Tamanho: " + kbSize + " KB");

				        //Conversão do arquivo em bytes.
				        byte[] bArquivo = new byte[(int) file.length()];
				        fileInputStream = new FileInputStream(file);
				        fileInputStream.read(bArquivo);
				        fileInputStream.close();
					  	
					  	arquivo = new Arquivo();
					  	arquivo.setContent(bArquivo);
					  	arquivo.setName(file.getName());
					  	arquivo.setSizeKB(kbSize);
				    }
				} 
				catch (Exception e2) 
				{
					e2.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(330, 46, 80, 21);
		contentPane.add(btnNewButton);
		
		//Endereço remoto. 
		JLabel lblNewLabel_1 = new JLabel("Endereço remoto:");
		lblNewLabel_1.setBounds(36, 120, 131, 14);
		contentPane.add(lblNewLabel_1);
		
		JTextField tfEndereco = new JTextField();
		tfEndereco.setEditable(false);
		tfEndereco.setText("127.0.0.1");
		tfEndereco.setBounds(36, 136, 284, 20);
		contentPane.add(tfEndereco);
		tfEndereco.setColumns(10);
		
	}
	
	
	private void sendFileServer()
	{
		if (validationFile())
		{
		    try 
		    {
		        Socket socket = new Socket("localhost", 4000);
		        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
		
		        byte[] bytea = serializeFile();
		        bufferedOutputStream.write(bytea);
		        bufferedOutputStream.flush();
		        bufferedOutputStream.close();
		        socket.close();
		        
		        JOptionPane.showMessageDialog(contentPane, "Arquivo enviado!");
		        
		    } 
		    catch (UnknownHostException e) 
		    {
		        e.printStackTrace();
		    } 
		    catch (IOException e) 
		    {
		        e.printStackTrace();
		    }
		}
	}
	
	//Funções do Arquivo
	private boolean validationFile() 
	{
		if (arquivo.getSizeKB() > this.sizeAllowed) 
		{
			JOptionPane.showMessageDialog(this, "Tamanho do arquivo excedido!\n Tamanho máximo: " + (sizeAllowed / 1024) + "MB");
			return false;
		}
		
		return true;
	}
	
	private byte[] serializeFile() 
	{
		try 
		{
			ByteArrayOutputStream byteAOS = new ByteArrayOutputStream();
			ObjectOutputStream objAOS;
			
			objAOS = new ObjectOutputStream(byteAOS);
			objAOS.writeObject(arquivo);
			
			return byteAOS.toByteArray();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
}