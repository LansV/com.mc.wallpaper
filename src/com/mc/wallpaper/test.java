package com.mc.wallpaper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.win32.StdCallLibrary;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class test {
	static int tcount = 0;
	static int count = 0;

	public static void main(String[] args) {
		String[] st = { "E:\\屏保\\1.jpg", "E:\\屏保\\2.jpg", "E:\\屏保\\3.jpg", "E:\\屏保\\4.jpg", "E:\\屏保\\5.jpg" };
		Properties props = System.getProperties(); // 获得系统属性集
		String osName = props.getProperty("os.name"); // 操作系统名称
		String osArch = props.getProperty("os.arch"); // 操作系统构架
		String osVersion = props.getProperty("os.version"); // 操作系统版本
		JOptionPane.showMessageDialog(null, "系统名称: " + osName + "\n" + "系统架构: " + osArch + "\n" + "系统版本: " + osVersion);
		String wallPaper = Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, "Control Panel\\Desktop",
				"Wallpaper");
		String wallPaperStyle = Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, "Control Panel\\Desktop",
				"WallpaperStyle");
		String tileWall = Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, "Control Panel\\Desktop",
				"TileWallpaper");
		WriteInfo.writeInfo(null, "Wallpaper:'" + wallPaper + "'\r\n" + "WallpaperStyle:'" + wallPaperStyle + "'\r\n"
				+ "TileWallpaper:'" + tileWall + "'");
		File oldf = new File("E:\\屏保\\img0.jpg");
		if (oldf.exists()) {
			String fileName = oldf.getParent() + File.separator + "6.jpg";
			System.out.println(fileName);
			if (!oldf.renameTo(new File(fileName))) {
				JOptionPane.showMessageDialog(null, "修改名称失败");
			} else {
				String sysPath = "E:\\testPath\\test";
				File pf = new File(sysPath);
				if (!pf.exists()) {
					JOptionPane.showMessageDialog(null, "no exists");
					pf.mkdirs();
					try {
						FileInputStream in = new FileInputStream(fileName);
						FileOutputStream out = new FileOutputStream("E:\\testPath\\test\\6.jpg");
						@SuppressWarnings("unused")
						int len = 0;
						byte[] b = new byte[1024];
						try {
							while ((len = in.read(b)) != -1) {
								out.write(b, 0, b.length);
							}
							in.close();
							out.close();
						} catch (IOException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					} catch (FileNotFoundException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				} else {
					if (pf.isDirectory()) {
						try {
							FileInputStream in = new FileInputStream(fileName);
							FileOutputStream out = new FileOutputStream("E:\\testPath\\test\\6.jpg");
							@SuppressWarnings("unused")
							int len = 0;
							byte[] b = new byte[1024];
							try {
								while ((len = in.read(b)) != -1) {
									out.write(b, 0, b.length);
								}
								in.close();
								out.close();
							} catch (IOException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
						} catch (FileNotFoundException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					} else {
						JOptionPane.showMessageDialog(null, "非文件夹，请核查");
					}
				}
			}
		} else {
			System.out.println("no");
		}
		/*
		 * Runnable runnable = new Runnable() { public void run() { // task to
		 * run goes here installWallpaper(st[count]); count++; tcount++;
		 * if(count>=4){ count=0; } System.out.println("第"+tcount+"次"); } };
		 * ScheduledExecutorService service = Executors
		 * .newSingleThreadScheduledExecutor(); //
		 * 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间 service.scheduleAtFixedRate(runnable,
		 * 3, 3, TimeUnit.SECONDS);
		 */

	}

	private static void installWallpaper(String fnm)
	/*
	 * Wallpaper installation requires three changes to thw Win32 registry, and
	 * a desktop refresh. The basic idea (using Visual C# and VB) is explained
	 * in "Setting Wallpaper" by Sean Campbell:
	 * http://blogs.msdn.com/coding4fun/archive/2006/10/31/912569.aspx
	 */
	{
		File f = new File(fnm);
		if (f.exists()) {
			String fullFnm = fnm;
			// JOptionPane.showMessageDialog(null, "文件存在，将更换桌面背景");
			// System.out.println("Full fnm: " + fullFnm);
			/*
			 * 3 registry key changes to HKEY_CURRENT_USER\Control Panel\Desktop
			 * These three keys (and many others) are explained at
			 * http://www.virtualplastic.net/html/desk_reg.html List of registry
			 * functions at MSDN:
			 * http://msdn.microsoft.com/en-us/library/ms724875(v=VS.85).aspx
			 */

			Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "Control Panel\\Desktop", "Wallpaper",
					fullFnm);
			Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "Control Panel\\Desktop", "WallpaperStyle",
					"10"); // win10:0-tile 0-center 2-stretch 6-fit 10-fill
							// 22-like as fill
			// win7:
			// winxp:just use 0
			Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "Control Panel\\Desktop", "TileWallpaper",
					"0");
			// refresh the desktop using User32.SystemParametersInfo(), so
			// avoiding an OS reboot
			int SPI_SETDESKWALLPAPER = 0x14;
			int SPIF_UPDATEINIFILE = 0x01;
			int SPIF_SENDWININICHANGE = 0x02;

			boolean result = MyUser32.INSTANCE.SystemParametersInfoA(SPI_SETDESKWALLPAPER, 0, fullFnm,
					SPIF_UPDATEINIFILE | SPIF_SENDWININICHANGE);
			if (result) {
				// JOptionPane.showMessageDialog(null, "更换背景成功,如背景未变更请联系服务商");
			} else {
				JOptionPane.showMessageDialog(null, "更换背景失败,请联系服务商");
			}
			// System.out.println("Refresh desktop result: " + result);
		} else {
			JOptionPane.showMessageDialog(null, "文件不存在");
		}

	} // end of installWallpaper()

	// ---------------------------------------------

	private interface MyUser32 extends StdCallLibrary
	/*
	 * JNA Win32 extensions includes a User32 class, but it doesn't contain
	 * SystemParametersInfo(), so it must be defined here.
	 * 
	 * MSDN libary docs on SystemParametersInfo() are at:
	 * http://msdn.microsoft.com/en-us/library/ms724947(VS.85).aspx
	 * 
	 * BOOL WINAPI SystemParametersInfo( __in UINT uiAction, __in UINT uiParam,
	 * __inout PVOID pvParam, __in UINT fWinIni );
	 * 
	 * When uiAction == SPI_SETDESKWALLPAPER, SystemParametersInfo() sets the
	 * desktop wallpaper. The value of the pvParam parameter determines the new
	 * wallpaper.
	 */
	{
		MyUser32 INSTANCE = (MyUser32) Native.loadLibrary("user32", MyUser32.class);

		boolean SystemParametersInfoA(int uiAction, int uiParam, String fnm, int fWinIni);
		// SystemParametersInfoA() is the ANSI name used in User32.dll
	} // end of MyUser32 interface
}
