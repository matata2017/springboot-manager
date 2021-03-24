package com.company.project.common.spider;

import java.util.ArrayList;
import java.util.List;

public class PaChong {
	private String name;
	private String path;
	private String dir;
	private boolean isParent;
	private List<PaChong> sons;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isParent() {
		return isParent;
	}
	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}
	public List<PaChong> getSons() {
		return sons;
	}
	public void setSons(List<PaChong> sons) {
		this.sons = sons;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public PaChong() {
		super();
	}
	public PaChong(String name, String path, String dir, boolean isParent) {
		super();
		this.name = name;
		this.path = path;
		this.dir = dir;
		this.isParent = isParent;
	}
	public void addSon(PaChong pc) {
		if (this.sons == null) {
			this.sons = new ArrayList<PaChong>();
		}
		this.sons.add(pc);
	}
}
