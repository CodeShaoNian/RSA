package gui;

import java.awt.Container;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.rhg.rsa.RSAPrivateKey;
import com.rhg.rsa.RSAPublicKey;

import gui.FileTrans.DecrypListener;
import gui.FileTrans.EncrypListener;
import gui.FileTrans.FileInListener;
import gui.FileTrans.FileOutListener;
import gui.FileTrans.MenuActionListener;
import gui.FileTrans.OpenPriKeyListener;

public class CheckTwoFile extends JFrame {
	private Container c;
	private JButton fileInButton = new JButton("输入原文件");
	private JButton fileOutButton = new JButton("输入解密文件");
	private JButton encrypButton = new JButton("验证");

	private FileDialog openDia1, openDia2;// 定义“打开、保存”对话框
	private File fileIn, fileOut;// 定义文件

	CheckTwoFile() {
		super("RSA保密系统");
		
		JMenuBar menuBar = new JMenuBar();
		JMenuItem newMenuItem = new JMenuItem("返回主页面");
		newMenuItem.addActionListener(new MenuActionListener());
		menuBar.add(newMenuItem);
		this.setJMenuBar(menuBar);
		
		c = getContentPane();
		c.setLayout(new FlowLayout(FlowLayout.CENTER));
		c.add(fileInButton);
		c.add(fileOutButton);
		c.add(encrypButton);

		fileInButton.addActionListener(new FileInListener());
		fileOutButton.addActionListener(new FileOutListener());
		encrypButton.addActionListener(new EncrypListener());
		openDia1 = new FileDialog(this, "打开", FileDialog.LOAD);
		openDia2 = new FileDialog(this, "保存", FileDialog.LOAD);
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 退出关闭窗口
		setVisible(true);
	}

	class MenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			CheckTwoFile.this.dispose();
			new Main();
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
			//System.out.println(((JButton)(e.getSource())).getText());
			openDia1.setVisible(true);
			String dirpath = openDia1.getDirectory();
			String fileName = openDia1.getFile();
			if (dirpath == null || fileName == null) {
				return;
			} else {
				fileIn = new File(dirpath, fileName);
			}
		}
	}
	
	class FileOutListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*
			 * //选择文件，得到文件路径 JFileChooser jf = new JFileChooser();
			 * jf.showOpenDialog(FileTrans.this);//显示打开的文件对话框 File f =
			 * jf.getSelectedFile();//使用文件类获取选择器选择的文件 String s = f.getAbsolutePath();//返回路径名
			 * //JOptionPane弹出对话框类，显示绝对路径名 //JOptionPane.showMessageDialog(FileTrans.this,
			 * s, "标题",JOptionPane.WARNING_MESSAGE); inPath=s;
			 */
			//System.out.println(((JButton)(e.getSource())).getText());
			openDia2.setVisible(true);
			String dirpath = openDia2.getDirectory();
			String fileName = openDia2.getFile();
			if (dirpath == null || fileName == null) {
				return;
			} else {
				fileOut = new File(dirpath, fileName);
			}
		}
	}
	
	class EncrypListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*
			 * //选择文件，得到文件路径 JFileChooser jf = new JFileChooser();
			 * jf.showOpenDialog(FileTrans.this);//显示打开的文件对话框 File f =
			 * jf.getSelectedFile();//使用文件类获取选择器选择的文件 String s = f.getAbsolutePath();//返回路径名
			 * //JOptionPane弹出对话框类，显示绝对路径名 //JOptionPane.showMessageDialog(FileTrans.this,
			 * s, "标题",JOptionPane.WARNING_MESSAGE); inPath=s;
			 */
			//System.out.println(fileIn.getPath());
			//System.out.println(fileOut.getPath());
			if(fileIn.getPath().equals(fileOut.getPath())) {
				JOptionPane.showMessageDialog(null, "输入文件相同，重新输入", "Title",JOptionPane.WARNING_MESSAGE);
				return;
			}
			FileInputStream s1=null;
			try {
				 s1 = new FileInputStream(fileIn);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			FileInputStream s2=null;
			try {
				 s2 = new FileInputStream(fileOut);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if((long)(fileIn.length())!=(long)(fileOut.length())) {
				//弹出错误信息
				JOptionPane.showMessageDialog(null, "两个文件不同", "Title",JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			byte[] inBytes = null;
			inBytes = new byte[(int) fileIn.length()];
			byte[] outBytes = null;
			outBytes = new byte[(int) fileOut.length()];
			int offset = 0;
			int numRead = 0;
			try {
				while (offset < inBytes.length && (numRead = s1.read(inBytes, offset, inBytes.length - offset)) >= 0) {
					offset += numRead;
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			offset = numRead=0;
			try {
				while (offset < outBytes.length && (numRead = s2.read(outBytes, offset, outBytes.length - offset)) >= 0) {
					offset += numRead;
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for(int i=0;i<inBytes.length;++i) {
				if(inBytes[i] != outBytes[i]) {
					//弹出错误信息
					JOptionPane.showMessageDialog(null, "两个文件不同", "Title",JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
			//弹出正确信息
			JOptionPane.showMessageDialog(null, "两个文件一致");
		}
	}
}
