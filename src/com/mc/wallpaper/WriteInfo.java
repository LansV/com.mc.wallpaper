package com.mc.wallpaper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class WriteInfo {
	static String filename="wallpaperinfo/wp.lan";
	static FileOutputStream out = null;
	public static void main(String[] args){
		WriteInfo.getWriteInfo(null);
		//WriteInfo.writeInfo(null, "test");
	}
	public static ArrayList<String> getWriteInfo(JFrame JOP){
		ArrayList<String> ls = new ArrayList<String>();
		File file = new File(filename);
		if (file.exists() == true) {
			InputStream in = null;
			try {
				in = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(JOP, "get lockfile error");
			}
			byte b1[] = new byte[1024];
			int i = 0;
			try {
				i = in.read(b1);
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(JOP, "read lockfile error");
			}
			String s = new String(b1, 0, i);
			String[] cs=s.split("\n");
			//System.out.println("i");
			String wp="";
			String wps="";
			String tw="";
			int count=0;
			for(String st:cs){
				if(count==0){
					wp=st;
				}
				if(count==1){
					wps=st;
				}
				if(count==2){
					tw=st;
				}
				count++;
				
			}
			wp=wp.substring(11, wp.length()-2);
			wps=wps.substring(16, wps.length()-2);
			tw=tw.substring(15, tw.length()-1);
			count=0;
			ls.add(wp);
			ls.add(wps);
			ls.add(tw);
			//System.out.println(s+"\n"+wp+"\n"+wps+"\n"+tw);
		}else{
			ls.add("");
		}
		return ls;
	}
	public static boolean writeInfo(JFrame JOP,String info) {
		File file = new File(filename);
		if (file.exists() == true) {
			try {
				out = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(JOP, "get lockfile error");
				return false;
			}
			byte b[] = info.getBytes();
			try {
				out.write(b);
				out.close();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(JOP, "write lockfile error");
				return false;
			}
			
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(JOP, "create lockfile error");
				return false;
			}
			try {
				out = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(JOP, "get lockfile error");
				return false;
			}
			byte b[] = info.getBytes();
			try {
				out.write(b);
				out.close();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(JOP, "write lockfile error");
				return false;
			}
		}
	}
}
