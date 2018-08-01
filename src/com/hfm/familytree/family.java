package com.hfm.familytree;

import java.util.ArrayList;

import org.andengine.util.mime.MIMEType;

import com.hfm.familytree.xml;
public class family {
	 int x_max;
	 int x_min;
	 int y_max;
	 int y_min;
		
	 
	 short index;
		short id;
		even position;
		even top_left_pos;
		even top_right_pos;		
		private int parent1=-1;
		private int parent2=-1;
		private ArrayList<Integer> childrens;
		
	public family(){
		//this(0,0,null,null,null);
		index=0;
		this.id=0;
		position=null;
		top_left_pos=null;
		top_right_pos=null;
		childrens=new ArrayList<Integer>();
		childrens.clear();
		
	}
	public family(short i,short id,even p,even tlp,even trp) {
		// TODO Auto-generated constructor stub
		index=i;
		this.id=id;
		position=p;
		top_left_pos=tlp;
		top_right_pos=trp;
		childrens=new ArrayList<Integer>();
		childrens.clear();
		min_max(top_left_pos);
		min_max(top_right_pos);
	}


	public void setParent(int parent) {
		if(parent1==-1){
		this.parent1 = parent;
		min_max(xml.individuals.get(parent).pos);
		}
		else if(parent2==-1){
			this.parent2=parent;
			min_max(xml.individuals.get(parent).pos);
		}
	}
	public int getParent1() {
		return parent1;
	}
	public int getParent2() {
		return parent2;
	}

	public void setChild(int id){
		if (childrens.indexOf(id)==-1){
		childrens.add(id);
		min_max(xml.individuals.get(id).pos);
		}
	}
	
	
	public ArrayList<Integer> getChildrens() {
		return childrens;
	}
	ArrayList<even> lines;
	public ArrayList<even> getLines() throws Exception {
		if (lines==null)
			throw new Exception();
		return lines;
	}
	public void setLines() throws Exception{
		if (parent1<0 ||parent2 <0) 
			throw new Exception();
		lines=new ArrayList<even>();
		lines.clear();
		
		generate_Line_p(xml.individuals.get(parent1));
		/*if(xml.individuals.get(parent1).as_parent_Point==null){
		even e0=xml.individuals.get(parent1).pos;
		even e1=new even(e0.x,top_left_pos.y);
		}*/
		generate_Line_p(xml.individuals.get(parent2));
/*		even e2=xml.individuals.get(parent2).pos;
		even e3=new even(e2.x,top_left_pos.y);		*/
		//lines.add(e0);lines.add(e1);lines.add(e2);lines.add(e3);
		lines.add(top_left_pos);
		lines.add(top_right_pos);
		min_max(top_left_pos); min_max(top_right_pos);
		for(int i=0;i<childrens.size();i++){
			generate_Line_c(xml.individuals.get(childrens.get(i)));
			/*even es=xml.individuals.get(childrens.get(i)).pos;
			min_max(es);
			even ee=new even(es.x,top_left_pos.y);
			lines.add(es);
			lines.add(ee);	*/		
		}		

		
	}

	
		private void min_max(even e){

			if(e.x>x_max)
				x_max=e.x;
			else if (e.x<x_min) {
				x_min=e.x;
			}
			if(e.y>y_max)
				y_max=e.y;
			else if (e.y<y_min) {
				y_min=e.y;
			}
		}
		
		private void generate_Line_p(individual ind){
			if(ind.as_parent_Point==null){
				even e0=ind.pos;
				even e1=new even(e0.x,top_left_pos.y);
				lines.add(e0);
				lines.add(e1);
				 min_max(e0);
				}
			else{
				even e0=ind.pos;
				even e1=new even(ind.as_parent_Point.x,e0.y);
				//even e2=new even(ind.as_parent_Point.x,e0.y);
				even e3=new even(ind.as_parent_Point.x,top_left_pos.y);//ind.as_parent_Point.y);
				lines.add(e0);
				lines.add(e1);
				lines.add(e1);
				lines.add(e3);
				min_max(e0);
			}
			
			
		}
	
		private void generate_Line_c(individual ind){
			if(ind.as_biological_Point==null){
				even es=ind.pos;
				min_max(es);
				even ee=new even(es.x,top_left_pos.y);
				lines.add(es);
				lines.add(ee);		
				}
			else{
				even e0=ind.pos;
				even e1=new even(ind.as_biological_Point.x,e0.y);
				
				even e3=new even(ind.as_biological_Point.x,top_left_pos.y);//ind.as_parent_Point.y);
				lines.add(e0);
				lines.add(e1);
				lines.add(e1);
				lines.add(e3);
				min_max(e0);
			}
			
			
		}


}
