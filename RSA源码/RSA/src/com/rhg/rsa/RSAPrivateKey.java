package com.rhg.rsa;

import java.math.BigInteger;
import java.util.Base64;
import java.io.*;

/**
 * Class representing a private RSA key.
 * 
 * @author Rob
 * @version 05/31/2010
 */
public class RSAPrivateKey extends RSAKey {
	private static final long serialVersionUID = 666L;
	/** The private exponent. */
	private BigInteger d;

	/** Default constructor. */
	public RSAPrivateKey() {
		super();
		setPriExp(null);
		return;
	}

	/** Main constructor. */
	public RSAPrivateKey(BigInteger modulus, BigInteger priExp) {
		super(modulus);
		setPriExp(priExp);
		return;
	}

	/** Performs the classical RSA computation. */
	protected BigInteger decrypt(BigInteger c) {
		return c.modPow(getPriExp(), getModulus());
	}

	/** Extracts the data portion of the byte array. */
	protected byte[] extractData(byte[] EB) {
		if (EB.length < 12 || EB[0] != 0x00 || EB[1] != 0x02) {
			return null;
		}
		int index = 2;
		do {
		} while (EB[index++] != 0x00);

		return getSubArray(EB, index, EB.length);
	}

	/** Returns the private exponent. */
	public BigInteger getPriExp() {
		return d;
	}

	/** Sets the private exponent. */
	public void setPriExp(BigInteger priExp) {
		d = weedOut(priExp);
		return;
	}

	/** Uses key and returns true if decryption was successful. */
	public boolean use(String source, String destination) {
		System.out.println("源文件：" + source);
		System.out.println("目标文件:" + destination);
		
		byte[] sourceBytes = getBytes(source);
		System.out.println("源文件长度:" + sourceBytes.length);
		if (isNull(sourceBytes)) {
			return false;
		}

		int k = getModulusByteSize();
		BigInteger c, m;
		byte[] EB, M;
		byte[][] C = reshape(sourceBytes, k);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(destination);
			for (int i = 0; i < C.length; i++) {
				if (C[i].length != k)
					return false;
				c = new BigInteger(C[i]);
				m = decrypt(c);
				System.out.println("c:" + c);
				System.out.println("m:" + m);
				EB = toByteArray(m, k);
				System.out.println("EB:" + EB);
				System.out.println("长度：" + EB.length);
				M = extractData(EB);
				System.out.println("M:" + M);
				out.write(M);
			}
			out.close();
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (isNull(out))
					out.close();
			} catch (IOException e) {
				return false;
			}
		}

		return true;
	}

	public String use(String text) {
		// TODO Auto-generated method stub
		byte[] sourceBytes = null;

		sourceBytes = Base64.getDecoder().decode(text);
		/*
		System.out.println("打印解密数组");
		for (int i = 0; i < sourceBytes.length; ++i) {
			System.out.println(sourceBytes[i]);
		}
		*/
		System.out.println("byteLen:" + sourceBytes.length);
		int k = getModulusByteSize();
		BigInteger c, m;
		byte[] EB, M;
		byte[][] C = reshape(sourceBytes, k);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for (int i = 0; i < C.length; i++) {
			if (C[i].length != k)
				break;
			c = new BigInteger(C[i]);
			m = decrypt(c);
			System.out.println("c:" + c);
			System.out.println("m:" + m);
			EB = toByteArray(m, k);
			System.out.println("EB:" + EB);
			System.out.println("长度：" + EB.length);
			M = extractData(EB);
			System.out.println(M);
			try {
				out.write(M);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return out.toString();
	}
}
