package com.hfm.familytree;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;

public class xml {
	public static ArrayList<individual> individuals;
	public static ArrayList<family> families;	
	public static void ParseXML(Context c) {
		individuals = new ArrayList<individual>();
		families=new ArrayList<family>();
		try {
			DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
			DocumentBuilder docbuild = df.newDocumentBuilder();
			Document doc = docbuild.parse(new BufferedInputStream(c.getAssets().open("source/final.xml"),50000));
			doc.getDocumentElement().normalize();
			NodeList nodelist = doc.getDocumentElement().getElementsByTagName("Individual");
			int size=Integer.valueOf(((Element)nodelist.item(nodelist.getLength()-1)).getAttribute("ID").substring(3));
			individuals.clear();
			for(int i=0;i<size+1;i++){
				individuals.add(i,null);
			}
			for (short i = 0; i < nodelist.getLength(); i++) {
				Element element = (Element) nodelist.item(i);
				short id = Short.valueOf(element.getAttribute("ID")
						.substring(3));
				Element Name = (Element) element.getElementsByTagName("Name")
						.item(0);
				Element Display = null;
				Element first = null;
				Element last = null;
				Element last2=null;
				try {
					Display = (Element) Name.getElementsByTagName("Display")
							.item(0);
					first = (Element) Name.getElementsByTagName("First")
							.item(0);
					last = (Element) Name.getElementsByTagName("Last").item(0);
					last2 = (Element) Name.getElementsByTagName("Last2").item(0);
				} catch (Exception e) {
					// TODO: handle exception

				}
				Element Position = (Element) element.getElementsByTagName(
						"Position").item(0);
				Element Gender = (Element) element.getElementsByTagName(
						"Gender").item(0);

				String dn = Display != null ? Display.getTextContent() : "";
				String fn = first != null ? first.getTextContent() : "";
				;
				String ln = last != null ? last.getTextContent() : "";
				String ln2= last2 != null ? last2.getTextContent() : "";
				String pos = Position.getTextContent();
				even xy=parse_xy(pos);
				char gender = Gender.getTextContent().charAt(0);
				 individual ind= new individual(i,id,xy, gender, dn, fn,ln,ln2);
				individuals.set(id,ind);				
			}
			//Individuals Parsing Process has been completed yet and now parsing families;
			nodelist = doc.getDocumentElement().getElementsByTagName("Family");
			size=Integer.valueOf(((Element)nodelist.item(nodelist.getLength()-1)).getAttribute("ID").substring(3));
			families.clear();
			for(int i=0;i<size+1;i++){
				families.add(i,null);
			}
			for (short i = 0; i < nodelist.getLength(); i++) {
				Element element = (Element) nodelist.item(i);
				short id = Short.valueOf(element.getAttribute("ID").substring(3));
				Element Position=(Element) element.getElementsByTagName("Position").item(0);
				String family_pos=Position.getFirstChild().getTextContent().replace(" ","");
				family_pos=family_pos.replace("\n","");
				Element top=(Element) Position.getElementsByTagName("Top").item(0);
				String top_left=((Element)top.getElementsByTagName("Left").item(0)).getTextContent();
				String top_right=((Element)top.getElementsByTagName("Right").item(0)).getTextContent();
				family fam=new family(i, id, parse_xy(family_pos),parse_xy(top_left), parse_xy(top_right));
				families.set(id, fam);
			}
			
			nodelist = doc.getDocumentElement().getElementsByTagName("PedigreeLink");
			for (int i = 0; i < nodelist.getLength(); i++) {
				
			Element element = (Element) nodelist.item(i);
			NodeList list =element.getElementsByTagName("Points");
			
			
			String PedigreeLink=element.getAttribute("PedigreeLink");
			String Family=element.getAttribute("Family");
			int family_id=Integer.valueOf(Family.substring(3));
			String Individual=element.getAttribute("Individual");
			int inv_id=Integer.valueOf(Individual.substring(3));

			if(PedigreeLink.equalsIgnoreCase("Parent")){
				families.get(family_id).setParent(inv_id);
				
				if(list.getLength()>0){
					String point=((Element)list.item(0)).getTextContent();
					even pointe=parse_xy(point);
					individuals.get(inv_id).as_parent_Point=pointe;
					}
			}
			else {
				families.get(family_id).setChild(inv_id);
				
				if(list.getLength()>0){
					String point=((Element)list.item(0)).getTextContent();
					even pointe=parse_xy(point);
					individuals.get(inv_id).as_biological_Point=pointe;
					}
				
			}
			}
			
			families.isEmpty();
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static even parse_xy(String pos){
		int comma_place = pos.indexOf(',');
		int len = pos.length();
		int xp = Integer.parseInt(pos.substring(0, comma_place));
		int yp = Integer.parseInt(pos.substring(comma_place + 1, len));
		even p=new even(xp,yp);
		min_max(p);
		return p;
	}
	
	private static void min_max(even e){

		if(e.x>individual.X_MAX)
			individual.X_MAX=e.x;
		else if (e.x<individual.X_MIN) {
			individual.X_MIN=e.x;
		}
		if(e.y>individual.Y_MAX)
			individual.Y_MAX=e.y;
		else if (e.y<individual.Y_MIN) {
			individual.Y_MIN=e.y;
		}
	}

}
