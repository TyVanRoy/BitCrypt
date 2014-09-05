import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class BitCrypt implements ActionListener{
	boolean encrypt = true;
	boolean fileS = false;
	boolean kS = false;
	File file, k;
	JFileChooser fileChooser = new JFileChooser();
	JFrame window = new JFrame("BitCrypter");
	JPanel frame = new JPanel();
	JLabel fileLabel = new JLabel("File");
	JLabel kLabel = new JLabel("K");
	JTextField fileField = new JTextField(38);
	JTextField kField = new JTextField(38);
	JTextArea outField = new JTextArea();
	JButton browseFile = new JButton("Browse");
	JButton browseK = new JButton("Browse");
	JButton go = new JButton("START");
	JRadioButton en = new JRadioButton("ENCRYPT");
	JRadioButton de = new JRadioButton("DECRYPT");
	ButtonGroup buttonGroup = new ButtonGroup();
	
	public File getHome(){
		FileSystemView filesys = FileSystemView.getFileSystemView();
		return filesys.getHomeDirectory();
	}
	public BitCrypt(){
		formatGUI();
	}
	public void start(){
		window.setVisible(true);
		say("Will encrypt");
	}
	public void formatGUI(){
		window.setSize(600,300);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.setLayout(new BorderLayout());
		buttonGroup.add(en);
		buttonGroup.add(de);
		en.setSelected(true);
		outField.setBackground(Color.LIGHT_GRAY);
		outField.setForeground(Color.red);
		outField.setEditable(false);
		fileField.setBackground(Color.LIGHT_GRAY);
		fileField.setEditable(false);
		kField.setEditable(false);
		kField.setBackground(Color.LIGHT_GRAY);
		browseFile.addActionListener(this);
		browseK.addActionListener(this);
		en.addActionListener(this);
		de.addActionListener(this);
		go.addActionListener(this);
		frame.setPreferredSize(new Dimension(600,120));
		frame.setBackground(Color.LIGHT_GRAY);
		frame.add(fileLabel);
		frame.add(fileField);
		frame.add(browseFile);
		frame.add(kLabel);
		frame.add(kField);
		frame.add(browseK);
		frame.add(en);
		frame.add(de);
		frame.add(go);
		window.add(frame,BorderLayout.NORTH);
		window.add(new JScrollPane(outField),BorderLayout.CENTER);
	}
	public void actionPerformed(ActionEvent e){
		Object o = e.getSource();
		if(o == browseFile){
			file = open();
			if(file != null){
				fileS = true;
				fileField.setText(file.getPath());
				say("File selected");
			}else{
				fileS = false;
			}
		}else if(o == browseK){
			k = open();
			if(k != null){
				kS = true;
				kField.setText(k.getPath());
				say("KStub selected");
			}else{
				kS = false;
			}
		}else if(o == en){
			say("Will encrypt");
			encrypt = true;
		}else if(o == de){
			say("Will decrypt");
			encrypt = false;
		}else if(o == go){
			if(fileS && kS){
				if(encrypt){
					post("Now encrypting---");
					new Thread(new Runnable(){
						public void run(){
							encrypt();
						}
					}).start();
				}else{
					post("Now decrypting---");
					decrypt();
				}
			}else if(fileS){
				if(encrypt){
					post("Now encrypting---");
					new Thread(new Runnable(){
						public void run(){
							encrypt(1);
						}
					}).start();
				}else{
					post("Now decrypting---");
					decrypt(1);
				}
			}else{
				say("You must implement your files");
			}
		}
	}
	public void say(final String s){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					outField.append("\t*|  " + s + "\n");
                }
            }
	    );
	}
	public void post(final String s){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					outField.append(s + "\n");
				}
			}
	    );
	}
	public void write(final String s){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						outField.append(s);
					}
				}
		    );
	}
	public void encrypt(){
		new Thread(new Runnable(){
			public void run(){
				Path input = Paths.get(file.getPath());
				Path kPath = Paths.get(k.getPath());
				try{
					Thread.sleep(2000);
					byte[] kBytes = Files.readAllBytes(kPath);
					int kSize = kBytes.length;
					Path output = Paths.get(file.getPath());
					byte[] iBytes = Files.readAllBytes(input);
					byte[] oBytes = new byte[iBytes.length];
					for(int i = 0; i < iBytes.length; i++){
						oBytes[i] = (byte) (iBytes[i] + kBytes[i % kSize]);
					}
					Files.write(output,oBytes);
					say("Encrypted");
				}catch(Exception e){
					say("ERROR **");
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	public void encrypt(int i){
		new Thread(new Runnable(){
			public void run(){
				Path input = Paths.get(file.getPath());
				try{
					Thread.sleep(2000);
					Path output = Paths.get(new File(getHome().getPath() + "//desktop//file").getPath());
					byte[] iBytes = Files.readAllBytes(input);
					byte[] oBytes = new byte[iBytes.length];
					for(int i = 0; i < iBytes.length; i++){
						oBytes[i] = (byte) (iBytes[i] + 100000);
					}
					Files.write(output,oBytes);
					say("Encrypted");
				}catch(Exception e){
					say("ERROR **");
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void decrypt(int i){
		new Thread(new Runnable(){
			public void run(){
				Path input = Paths.get(file.getPath());
				try{
					Thread.sleep(2000);
					Path output = Paths.get(file.getPath());
					byte[] iBytes = Files.readAllBytes(input);
					byte[] oBytes = new byte[iBytes.length];
					for(int i = 0; i < iBytes.length; i++){
						oBytes[i] = (byte) (iBytes[i] - 100000);
					}
					Files.write(output,oBytes);
					say("Decrypted");
				}catch(Exception e){
				}
			}
		}).start();
	}
	public void decrypt(){
		new Thread(new Runnable(){
			public void run(){
				Path input = Paths.get(file.getPath());
				Path kPath = Paths.get(k.getPath());
				try{
					Thread.sleep(2000);
					byte[] kBytes = Files.readAllBytes(kPath);
					int kSize = kBytes.length;
					Path output = Paths.get(file.getPath());
					byte[] iBytes = Files.readAllBytes(input);
					byte[] oBytes = new byte[iBytes.length];
					for(int i = 0; i < iBytes.length; i++){
						oBytes[i] = (byte) (iBytes[i] - kBytes[i % kSize]);
					}
					Files.write(output,oBytes);
					say("Decrypted");
				}catch(Exception e){
					say("ERROR **");
				}
			}
		}).start();
	}
	public File open(){
        int go = fileChooser.showOpenDialog(null);
        if(go == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            return file;
        }else{
            return null;
		}
	}
	public static void main(String[] args){
		BitCrypt b = new BitCrypt();
		b.start();
	}
}
