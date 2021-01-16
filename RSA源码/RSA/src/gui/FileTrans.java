package gui;

import java.awt.*;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;

import com.rhg.rsa.RSAKey;
import com.rhg.rsa.RSAKeyGenerator;
import com.rhg.rsa.RSAPrivateKey;
import com.rhg.rsa.RSAPublicKey;
import com.rhg.rsa.RSATest;

import javax.crypto.Cipher;
import gui.Main.FileTransListener;
import gui.Main.StrTransListener;
import sun.misc.BASE64Encoder;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.*;

public class FileTrans extends JFrame {
	private Container c;
	private JButton fileInButton = new JButton("输入文件");
	private JButton fileOutButton = new JButton("输出文件");
	private JButton encrypFileButton = new JButton("加密");
	private JButton encrypButton = new JButton("自动加密");
	private JButton decrypButton = new JButton("解密");
	private JButton priKeyFileButton = new JButton("打开私钥文件");
	private JButton pubKeyFileButton = new JButton("打开公钥文件");
	private File priKeyFile, pubKeyFile;
	private FileDialog openDia, saveDia;// 定义“打开、保存”对话框
	private File fileIn, fileOut;// 定义文件
	RSAPrivateKey privateKey = null;
	RSAPublicKey publicKey = null;
	private JPanel fileInPanel = new JPanel();
	private JPanel fileOutPanel = new JPanel();
	private JPanel encrypPanel = new JPanel();
	private JPanel encrypFilePanel = new JPanel();
	private JPanel decrypPanel = new JPanel();
	private JPanel filePanel = new JPanel();
	private JPanel keyPanel = new JPanel();

	FileTrans() {
		super("RSA保密系统");

		JMenuBar menuBar = new JMenuBar();
		JMenuItem newMenuItem = new JMenuItem("返回主页面");
		newMenuItem.addActionListener(new MenuActionListener());
		menuBar.add(newMenuItem);
		this.setJMenuBar(menuBar);
		
		c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(filePanel);
		filePanel.setLayout(new GridLayout(2, 1));
		filePanel.add(fileInButton);
		filePanel.add(fileOutButton);

		c.add(keyPanel, BorderLayout.EAST);
		keyPanel.setLayout(new BorderLayout());
		keyPanel.add(encrypPanel, BorderLayout.NORTH);
		keyPanel.add(decrypPanel, BorderLayout.SOUTH);

		encrypPanel.setLayout(new BorderLayout());
		encrypPanel.add(encrypFilePanel, BorderLayout.NORTH);

		encrypFilePanel.setLayout(new FlowLayout());
		encrypFilePanel.add(pubKeyFileButton);
		encrypFilePanel.add(encrypFileButton);
		encrypPanel.add(encrypButton, BorderLayout.SOUTH);

		keyPanel.add(decrypPanel, BorderLayout.SOUTH);
		decrypPanel.add(priKeyFileButton);
		decrypPanel.add(decrypButton);

		priKeyFileButton.addActionListener(new OpenPriKeyListener());
		pubKeyFileButton.addActionListener(new OpenPubKeyListener());
		fileInButton.addActionListener(new FileInListener());
		fileOutButton.addActionListener(new FileOutListener());
		encrypFileButton.addActionListener(new EncrypFileListener());
		encrypButton.addActionListener(new EncrypListener());
		decrypButton.addActionListener(new DecrypListener());
		openDia = new FileDialog(this, "打开", FileDialog.LOAD);
		saveDia = new FileDialog(this, "保存", FileDialog.SAVE);

		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 退出关闭窗口
		setVisible(true);
	}

	class MenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			FileTrans.this.dispose();
			new Main();
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
			openDia.setVisible(true);
			String dirpath = openDia.getDirectory();
			String fileName = openDia.getFile();
			if (dirpath == null || fileName == null) {
				return;
			} else {
				priKeyFile = new File(dirpath, fileName);
			}
			ObjectInputStream s = null;
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
				privateKey = (RSAPrivateKey) s.readObject();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(privateKey);
			System.out.println("n:" + privateKey.getModulus());
			System.out.println("d:" + privateKey.getPriExp());
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
			openDia.setVisible(true);
			String dirpath = openDia.getDirectory();
			String fileName = openDia.getFile();
			System.out.println(dirpath + fileName);
			if (dirpath == null || fileName == null) {
				return;
			} else {
				pubKeyFile = new File(dirpath, fileName);
			}
			ObjectInputStream s = null;
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
				publicKey = (RSAPublicKey) s.readObject();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(publicKey);
			System.out.println("n:" + publicKey.getModulus());
		}
	}

	class FileInListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*
			 * //选择文件，得到文件路径 JFileChooser jf = new JFileChooser();
			 * jf.showOpenDialog(FileTrans.this);//显示打开的文件对话框 File f =
			 * jf.getSelectedFile();//使用文件类获取选择器选择的文件 String s = f.getAbsolutePath();//返回路径名
			 * //JOptionPane弹出对话框类，显示绝对路径名 //JOptionPane.showMessageDialog(FileTrans.this,
			 * s, "标题",JOptionPane.WARNING_MESSAGE); inPath=s;
			 */
			openDia.setVisible(true);
			String dirpath = openDia.getDirectory();
			String fileName = openDia.getFile();
			if (dirpath == null || fileName == null) {
				return;
			} else {
				fileIn = new File(dirpath, fileName);
			}
		}
	}

	class FileOutListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			saveDia.setVisible(true);
			String dirpath = saveDia.getDirectory();
			String fileName = saveDia.getFile();
			if (dirpath == null || fileName == null) {
				return;
			} else {
				fileOut = new File(dirpath, fileName);
			}
		}
	}

	class EncrypFileListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!publicKey.use(fileIn.toString(), fileOut.toString())) {
				System.out.println("Public key encryption failed.");
				System.out.println(String.format("plaintext from: %s", fileIn.getAbsolutePath()));
				// System.exit(RSATest.TEST_ERROR);
			}
		}
	}

	class EncrypListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			RSAKeyGenerator keygen = new RSAKeyGenerator();
			publicKey = (RSAPublicKey) keygen.makeKey(RSAKey.PUBLIC_KEY);
			privateKey = (RSAPrivateKey) keygen.makeKey(RSAKey.PRIVATE_KEY);
			System.out.println("n:" + privateKey.getModulus());
			System.out.println("d:" + privateKey.getPriExp());
			if (!publicKey.use(fileIn.toString(), fileOut.toString())) {
				System.out.println("Public key encryption failed.");
				System.out.println(String.format("plaintext from: %s", fileIn.getAbsolutePath()));
				// System.exit(RSATest.TEST_ERROR);
			} else {
				ObjectOutputStream s = null;
				try {
					s = new ObjectOutputStream(new FileOutputStream(fileOut + "-public"));
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
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					s = new ObjectOutputStream(new FileOutputStream(fileOut + "-private"));
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
			}
			JOptionPane.showMessageDialog(null, "秘钥对存放于：" + fileIn.getParent());
		}
	}

	class DecrypListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!privateKey.use(fileIn.toString(), fileOut.toString())) {
				System.out.println("Public key encryption failed.");
				System.out.println(String.format("plaintext from: %s", fileIn.getAbsolutePath()));
				// System.exit(RSATest.TEST_ERROR);
			}
		}
	}
}
