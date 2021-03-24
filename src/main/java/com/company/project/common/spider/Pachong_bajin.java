package com.company.project.common.spider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Pachong_bajin {
	static String writer = "巴金";
	static String charset = "gb2312";
	static String urlBasePath = "http://www.readers365.com/bajin/";
	static String fileDir = "E:\\project\\简体转繁体(java、python)\\net_pachong\\" + writer + "文集";
	static String fileSep = "/";
	static PaChong root = new PaChong(writer, urlBasePath, "", true);
	
	public static void fileWriter(String path, String content) {
		try {
			if (path.contains("javascript")) {
				return;
			}
			File file = createFile(path);
			PrintWriter pw = new PrintWriter(new FileWriter(file), true);
			pw.write(content);
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static File createFile(String path) {
		try {
			System.out.println(path);
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			return file;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void mkDir(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	public static void parseDoc(PaChong parent, String regex, int start, int end) { 
        String html = pachong(parent.getPath());
        Document doc = Jsoup.parse(html);
        
        Elements selectAll = doc.select(regex);
        for (int i = 0; i < start; i++) {
        	selectAll.remove(0);
        }
        for (int i = 0; i < end; i++) {
        	selectAll.remove(selectAll.size() - 1);
        }

        for (Element ele : selectAll) {
        	String href = ele.attr("href");
        	String dir = href.substring(0, href.indexOf("/"));
        	href = urlBasePath + href;
        	System.out.println(href);

        	dir = parent.getDir() + fileSep + dir;

        	PaChong pc = new PaChong(href, href, dir, true);
        	parent.addSon(pc);
        	parseDoc2(pc, "div[class=\"content\"] a", 0, 0);
        	
//        	break;
        }
        System.out.println(root);
	}
	
	public static void parseDoc2(PaChong parent, String regex, int start, int end) { 
        String html = pachong(parent.getPath());
        Document doc = Jsoup.parse(html);
        
        Elements selectAll = doc.select(regex);
        if (selectAll.size() < 3) {
        	return;
        }
        
        for (int i = 0; i < start; i++) {
        	selectAll.remove(0);
        }
        for (int i = 0; i < end; i++) {
        	selectAll.remove(selectAll.size() - 1);
        }

        for (Element ele : selectAll) {
        	String href = ele.attr("href");
        	
        	System.out.println(href);
        	
        	if (!href.startsWith("http")) {
            	String hrefPar = parent.getPath();
            	hrefPar = hrefPar.replace(urlBasePath, "");
            	hrefPar = hrefPar.substring(0, hrefPar.lastIndexOf("/"));
        		href = urlBasePath + hrefPar + fileSep + href;
        	}
        	String text = ele.text();
        	String dir = parent.getDir() + fileSep + text + ".txt";

        	System.out.println(href);
        	System.out.println(text);
        	System.out.println(dir);
        	
        	PaChong pc = new PaChong(text, href, dir, false);
        	parent.addSon(pc);
        }
	}
	
	public static void writeFile(PaChong pc, String regex) {
		if (pc == null) {
			return;
		}
		System.out.println(pc.getPath());
		String html = pachong(pc.getPath());
		if (html == null) {
			return;
		}
		Document doc = Jsoup.parse(html);
		
		Elements selectContent = doc.select(regex);
		selectContent.remove(0);
		selectContent.remove(0);
		
		StringBuilder sb = new StringBuilder();
		for (Element ele : selectContent) {
			String text = ele.text();
			sb.append(text);
		}
		
		fileWriter(fileDir + pc.getDir(), sb.toString());
	}
	
	public static void extract(PaChong pc, String regex) {
		if (pc.isParent()) {
			mkDir(fileDir + pc.getDir());
			if (pc.getSons() != null) {
				for (PaChong pachong : pc.getSons()) {
					extract(pachong, regex);
//					return;
				}
			}
		} else {
			writeFile(pc, regex);
//			return;
		}
	}
	
	public static void test(String urlPath) {
		URL url = null;
        URLConnection urlconn = null;
        BufferedReader br = null;

        try {
            url = new URL(urlPath);
            urlconn = url.openConnection();

            br = new BufferedReader(new InputStreamReader(
                    urlconn.getInputStream(), Charset.forName(charset)));
            String buf = null;
            while ((buf = br.readLine()) != null) {
            	System.out.println(buf);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            sleep(500);
        }
	}
	
	public static String pachong(String urlPath) {
		URL url = null;
        URLConnection urlconn = null;
        BufferedReader br = null;

        try {
            url = new URL(urlPath);
            urlconn = url.openConnection();

            br = new BufferedReader(new InputStreamReader(
                    urlconn.getInputStream(), Charset.forName(charset)));
            String buf = null;
            StringBuilder sb = new StringBuilder();
            while ((buf = br.readLine()) != null) {
            	sb.append(buf).append("\n");
            }
            
            return sb.toString();
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
            	if (br != null) {
            		br.close();
            	}
            } catch (IOException e) {
                e.printStackTrace();
            }
            sleep(500);
        }
		return null;
	}
	
	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		parseDoc(root, "map[name=\"FPMap1\"] area", 2, 0);
		extract(root, "font[color=\"#000000\"]");
	}
}
