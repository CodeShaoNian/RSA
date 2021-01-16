package gui;

import java.awt.Color;
import java.awt.*;
import java.util.Vector;
import javax.swing.*;

import com.rhg.rsa.RSAKey;
import com.rhg.rsa.RSAKeyGenerator;
import com.rhg.rsa.RSAPrivateKey;
import com.rhg.rsa.RSAPublicKey;

import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

public class Main extends JFrame {

	private Container c;
	private JButton fileTransButton = new JButton("文件转换");
	private JButton strTransButton = new JButton("字符串转换");
	private JButton fileCheckButton = new JButton("文件校验");
	private JButton keyGenButton = new JButton("秘钥对生成");
	private JPanel keyPanel = new JPanel();
	private JPanel otherPanel = new JPanel();
	public Main() {
		super("RSA保密系统");
		c = getContentPane();
		c.setLayout(new GridLayout(2, 1));
		c.add(otherPanel);
		c.add(keyPanel);
		
		otherPanel.setLayout(new FlowLayout());
		otherPanel.add(fileTransButton);
		otherPanel.add(strTransButton);
		otherPanel.add(fileCheckButton);
		
		keyPanel.setLayout(new BorderLayout());
		keyPanel.add(keyGenButton, BorderLayout.CENTER);
		
		keyGenButton.addActionListener(new KeyGenListener());
		fileTransButton.addActionListener(new FileTransListener());
		strTransButton.addActionListener(new StrTransListener());
		fileCheckButton.addActionListener(new FileCheckListener());
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 退出关闭窗口
		setVisible(true);
	}
	
	class KeyGenListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			RSAKeyGenerator keygen= new RSAKeyGenerator();
			RSAPublicKey publicKey = (RSAPublicKey) keygen.makeKey(RSAKey.PUBLIC_KEY);
			RSAPrivateKey privateKey = (RSAPrivateKey) keygen.makeKey(RSAKey.PRIVATE_KEY);
			ObjectOutputStream s=null;
			try {
				s = new ObjectOutputStream(new FileOutputStream(System.getProperty("user.dir")+"/publicKey"));
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
			JOptionPane.showMessageDialog(null, "公钥存放于："+System.getProperty("user.dir")+"/publicKey");
			try {
				s = new ObjectOutputStream(new FileOutputStream(System.getProperty("user.dir")+"/privateKey"));
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
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "私钥存放于："+System.getProperty("user.dir")+"/privateKey");
		}
	}
	
	class StrTransListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// 加密 解密 两个文本框，秘钥输入
			Main.this.dispose();
			new StrTrans();

		}
	}

	class FileTransListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// 跳转到新页面， 包含 加密功能， 解密功能 输入文件，输出文件夹
			Main.this.dispose();
			new FileTrans();
		}
	}
	
	class FileCheckListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			Main.this.dispose();
			new CheckTwoFile();
		}
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
