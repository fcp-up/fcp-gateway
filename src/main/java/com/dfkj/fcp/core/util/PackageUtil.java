package com.dfkj.fcp.core.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

//	参考 http://blog.csdn.net/wangpeng047/article/details/8206427

public class PackageUtil {
	public static List<String> getClassName(ClassLoader loader, String packageName) {
		return getClassName(loader, packageName, true);
	}

	public static List<String> getClassName(String packageName) {
		return getClassName(null, packageName, true);
	}

	public static List<String> getClassName(String packageName, boolean childPackage) {
		return getClassName(null, packageName, childPackage);
	}

	public static List<String> getClassName(ClassLoader loader, String packageName, boolean childPackage) {
		List<String> fileNames = null;
		if (loader == null) {
			loader = Thread.currentThread().getContextClassLoader();
		}

		String packagePath = packageName.replace(".", "/");
		URL url = loader.getResource(packagePath);
		
		if (url != null) {
			String type = url.getProtocol();
			if (type.equals("file")) {
				fileNames = getClassNameByFile(urlPathHelper(url), null, childPackage);
			} else if (type.equals("jar")) {
				fileNames = getClassNameByJar(urlPathHelper(url), childPackage);
			}
		} else {
			//fileNames = getClassNameByJars(((URLClassLoader)loader).getURLs(), packagePath, childPackage);
			try {
				/**
				 * 加载当前jar包
				 */
				File directory = new File("");
				File jarFile = new File(directory.getAbsolutePath(), System.getProperty("java.class.path"));
				String jarPath = jarFile.getPath();
				jarPath = String.format("file:%s", jarPath);
				
				fileNames = getClassNameByJars(new URL[] {new URL(jarPath)}, packagePath, childPackage);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return fileNames;
	}

	private static List<String> getClassNameByFile(String filePath, List<String> className, boolean childPackage) {
		List<String> myClassName = new ArrayList<String>();
		File file = new File(filePath);
		File[] childFiles = file.listFiles();
		for (File childFile : childFiles) {
			if (childFile.isDirectory()) {
				if (childPackage) {
					myClassName.addAll(getClassNameByFile(childFile.getPath(), myClassName, childPackage));
				}
			} else {
				String childFilePath = childFile.getPath();
				if (childFilePath.endsWith(".class")) {
					childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9,
							childFilePath.lastIndexOf("."));
					childFilePath = childFilePath.replace("\\", ".");
					myClassName.add(childFilePath);
				}
			}
		}

		return myClassName;
	}

	public static List<String> getClassNameByJar(String jarPath, boolean childPackage) {
		List<String> myClassName = new ArrayList<String>();
		String[] jarInfo = jarPath.split("!");
		String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
		String packagePath = jarInfo[1].substring(1);
		try {
			JarFile jarFile = new JarFile(jarFilePath);
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();
				String entryName = jarEntry.getName();
				if (entryName.endsWith(".class")) {
					if (childPackage) {
						if (entryName.startsWith(packagePath)) {
							entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
							myClassName.add(entryName);
						}
					} else {
						int index = entryName.lastIndexOf("/");
						String myPackagePath;
						if (index != -1) {
							myPackagePath = entryName.substring(0, index);
						} else {
							myPackagePath = entryName;
						}
						if (myPackagePath.equals(packagePath)) {
							entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
							myClassName.add(entryName);
						}
					}
				}
			}
			jarFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return myClassName;
	}

	public static List<String> getClassNameByJars(URL[] urls, String packagePath, boolean childPackage) {
		List<String> myClassName = new ArrayList<String>();
		if (urls != null) {
			for (int i = 0; i < urls.length; i++) {
				URL url = urls[i];
				String urlPath = urlPathHelper(url);
				if (urlPath.endsWith("classes/")) {
					continue;
				}
				/*
				String path = PackageUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
				try {
					path = URLDecoder.decode(path, Charset.defaultCharset().displayName());
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				
				//urlPath = System.getProperty("java.class.path");
				packagePath = packagePath.replace(".", "/");
				String jarPath = urlPath + "!/" + packagePath;
				//System.out.println("************    jarPath:" + jarPath);
				//System.out.println("************    path:" + System.getProperty("java.class.path"));
				myClassName.addAll(getClassNameByJar(jarPath, childPackage));
			}
		}
		return myClassName;
	}
	
	/**
	 * 对url中出现的中文进行解码
	 * @param url
	 * @return
	 */
	public static String urlPathHelper(URL url) {
		String path = "";
		try {
			path = URLDecoder.decode(url.getPath(), Charset.defaultCharset().displayName());
			System.out.println("URL Path:" + path);
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
		}
		return path;
	}
}
