package com.hfm.familytree;

public class individual {	
	public static int X_MAX=0;
	public static int Y_MAX=0;
	public static int X_MIN=0;
	public static int Y_MIN=0;	
	public individual(short i,short id,even p,char g,String dn,String fn,String ln,String ln2) {
		index=i;
		this.id=id;
		pos=p;
		gender=g;
		display=dn;
		first=fn;
		last=ln;
		last2=ln2;
	}
	String Empty="";
	short index;
	short id=0;
	even pos;
	char gender;
	String display=Empty;
	String first=Empty;
	String last=Empty;
	String last2=Empty;
	boolean drawn=false;
	even as_biological_Point=null;
	even as_parent_Point=null;
	public even getAs_biological_Point() {
		return as_biological_Point;
	}
	public void setAs_biological_Point(even as_biological_Point) {
		this.as_biological_Point = as_biological_Point;
	}
	
	public even getAs_parent_Point() {
		return as_parent_Point;
	}
	public void setAs_parent_Point(even as_parent_Point) {
		this.as_parent_Point = as_parent_Point;
	}
	
}
