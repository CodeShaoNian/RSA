package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.*;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.rhg.rsa.RSAKey;
import com.rhg.rsa.RSAKeyGenerator;
import com.rhg.rsa.RSAPrivateKey;
import com.rhg.rsa.RSAPublicKey;

import gui.FileTrans.EncrypListener;
import gui.FileTrans.FileInListener;
import gui.FileTrans.FileOutListener;
import gui.FileTrans.MenuActionListener;
import gui.FileTrans.OpenPriKeyListener;
import sun.misc.BASE64Encoder;

public class StrTrans extends JFrame {
	private Container c;
	private JTextField inputText = new JTextField(50);
	private JTextField outputText = new JTextField(50);
	private JButton encrypButton = new JButton("自动加密");
	private JButton pubKeyFileButton = new JButton("打开公钥文件");
	private JButton encrypFileButton = new JButton("加密");
	private JButton decrypButton = new JButton("解密");
	private JButton priKeyFileButton = new JButton("打开私钥文件");
	RSAPrivateKey privateKey = null;
	RSAPublicKey publicKey=null;
	KeyPair keyPair;
	PrivateKey priKey=null;
	PublicKey pubKey=null;
	private FileDialog openDia;
	private JPanel textPanel = new JPanel();
	private JPanel inTextPanel = new JPanel();
	private JPanel outTextPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JPanel encrypPanel = new JPanel();
	private JPanel decrypPanel = new JPanel();
	private JPanel encrypFilePanel = new JPanel();
	StrTrans() {
		super("RSA保密系统");
		
		JMenuBar menuBar = new JMenuBar();
		JMenuItem newMenuItem = new JMenuItem("返回主页面");
		newMenuItem.addActionListener(new MenuActionListener());
		menuBar.add(newMenuItem);
		this.setJMenuBar(menuBar);
		
		c = getContentPane();
		c.setLayout(new GridLayout(2,1));
		c.add(textPanel);
		c.add(buttonPanel);
		
		textPanel.add(inTextPanel);
		inTextPanel.setLayout(new FlowLayout());
		textPanel.add(outTextPanel);
		outTextPanel.setLayout(new FlowLayout());

		inTextPanel.add(new JLabel("输入字符串"));
		inTextPanel.add(inputText);
		outTextPanel.add(new JLabel("加密字符串"));
		outTextPanel.add(outputText);
		
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(encrypPanel);
		buttonPanel.add(decrypPanel);
		
		encrypPanel.setLayout(new GridLayout(2,1));
		encrypPanel.add(encrypFilePanel);
		encrypFilePanel.setLayout(new FlowLayout());
		encrypFilePanel.add(pubKeyFileButton);
		encrypFilePanel.add(encrypFileButton);
		encrypPanel.add(encrypButton);
		
		
		decrypPanel.setLayout(new FlowLayout());
		decrypPanel.add(priKeyFileButton);
		decrypPanel.add(decrypButton);
		priKeyFileButton.addActionListener(new OpenPriKeyListener());
		pubKeyFileButton.addActionListener(new OpenPubKeyListener());
		openDia = new FileDialog(this, "打开秘钥文件", FileDialog.LOAD);
		encrypFileButton.addActionListener(new EncrypFileListener());
		encrypButton.addActionListener(new EncrypListener());
		decrypButton.addActionListener(new DecrypListener());
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 退出关闭窗口
		setVisible(true);
	}
	
	class MenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			StrTrans.this.dispose();
			new Main();
		}
	}
	
	class DecrypListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			inputText.setText(outputText.getText());
			System.out.println("inputText:"+inputText);
			String outText = privateKey.use(inputText.getText());
			System.out.println("outText:"+outText);
			outputText.setText(outText);
		}
	}
	
	class OpenPriKeyListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*
			 * //选择文件，得到文件路径 JFileChooser jf = new JFileChooser();
			 * jf.showOpenDialog(FileTrans.this);//显示打开的文件对话框 File f =
			 * jf.getSelectedFile();//使用文件类获取选择器选择的文件 String s = f.getAbsolutePath();//返回路径名
			 * //JOptionPane弹出对话框类，显示绝对路径名 //JOptionPane.showMessageDialog(FileTrans.this,
			 * s, "标题",JOptionPane.WARNING_MESSAGE); inPath=s;
			 */
			File priKeyFile=null;
			openDia.setVisible(true);
			String dirpath = openDia.getDirectory();
			String fileName = openDia.getFile();
			if (dirpath == null || fileName == null) {
				return;
			} else {
				priKeyFile = new File(dirpath, fileName);
			}
			ObjectInputStream s=null;
			try {
				s = new ObjectInputStream(new FileInputStream(priKeyFile));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				privateKey = (RSAPrivateKey)s.readObject();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(privateKey);
			System.out.println("n:"+privateKey.getModulus());
			System.out.println("d:"+privateKey.getPriExp());
		}
	}
	
	class OpenPubKeyListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*
			 * //选择文件，得到文件路径 JFileChooser jf = new JFileChooser();
			 * jf.showOpenDialog(FileTrans.this);//显示打开的文件对话框 File f =
			 * jf.getSelectedFile();//使用文件类获取选择器选择的文件 String s = f.getAbsolutePath();//返回路径名
			 * //JOptionPane弹出对话框类，显示绝对路径名 //JOptionPane.showMessageDialog(FileTrans.this,
			 * s, "标题",JOptionPane.WARNING_MESSAGE); inPath=s;
			 */
			File pubKeyFile=null;
			openDia.setVisible(true);
			String dirpath = openDia.getDirectory();
			String fileName = openDia.getFile();
			if (dirpath == null || fileName == null) {
				return;
			} else {
				pubKeyFile = new File(dirpath, fileName);
			}
			ObjectInputStream s=null;
			try {
				s = new ObjectInputStream(new FileInputStream(pubKeyFile));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				publicKey = (RSAPublicKey)s.readObject();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(publicKey);
			System.out.println("n:"+publicKey.getModulus());
		}
	}
	
	class EncrypListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String text = inputText.getText();
			System.out.println("原文"+text);
			RSAKeyGenerator keygen= new RSAKeyGenerator();
			publicKey = (RSAPublicKey) keygen.makeKey(RSAKey.PUBLIC_KEY);
			privateKey = (RSAPrivateKey) keygen.makeKey(RSAKey.PRIVATE_KEY);
			System.out.println("公钥n:"+publicKey.getModulus());
			System.out.println("私钥d:"+privateKey.getPriExp());
			String encrpyText = null;
			try {
				encrpyText = publicKey.use(text);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			System.out.println("密文"+encrpyText);
			outputText.setText(encrpyText);
			ObjectOutputStream s=null;
			try {
				System.out.println("文件路径:"+System.getProperty("user.dir"));
				s = new ObjectOutputStream(new FileOutputStream(System.getProperty("user.dir")+"/str-public"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				s.writeObject(publicKey);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				s.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				s = new ObjectOutputStream(new FileOutputStream(System.getProperty("user.dir")+"/str-private"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				s.writeObject(privateKey);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				s.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "秘钥对存放于："+System.getProperty("user.dir"));
		}
	}	
	
	class EncrypFileListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String text = inputText.getText();
			System.out.println("原文"+text);
			String encrpyText = null;
			try {
				encrpyText = publicKey.use(text);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			System.out.println("密文"+encrpyText);
			outputText.setText(encrpyText);
			
		}
	}	
}
