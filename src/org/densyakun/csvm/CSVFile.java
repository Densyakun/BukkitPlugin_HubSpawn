package org.densyakun.csvm;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class CSVFile implements Runnable{
	File f;
	public CSVFile(File file){
		this.f=file;
		new Thread(this).start();
	}
	public void setFile(File file){
		this.f=file;
	}
	public File getFile(){
		return f;
	}
	public void AllWrite(List<List<String>>datas)throws IOException{
		if(!f.exists())throw new FileNotFoundException();
		if(!f.isFile())throw new IOException("Not File.");
		BufferedWriter w=new BufferedWriter(new FileWriter(f));
		for(int a=0;a<datas.size();a++){
			List<String>l=datas.get(a);
			for(int b=0;b<l.size();b++){
				w.write(l.get(b));
				if(b!=l.size()-1)w.write(",");
			}
			w.write("\n");
		}
		w.close();
	}
	public List<List<String>>AllRead()throws IOException{
		if(!f.exists())throw new FileNotFoundException();
		if(!f.isFile())throw new IOException("Not File.");
		List<List<String>>d=new ArrayList<List<String>>();
		BufferedReader r=new BufferedReader(new FileReader(f));
		String line="";
		for(int a=0;(line=r.readLine())!=null;a++){
			d.add(new ArrayList<String>());
			String[]lsp=line.split(",");
			for(int b=0;b<lsp.length;b++)d.get(a).add(lsp[b]);
		}
		r.close();
		return d;
	}
	public void LineWrite(List<String>line,int l)throws LineNotFoundException,IOException{
		if(!f.exists())throw new FileNotFoundException();
		if(!f.isFile())throw new IOException("Not File.");
		List<List<String>>d=AllRead();
		if(d.size()<l+1)throw new LineNotFoundException(l);
		d.set(l,line);
		AllWrite(d);
	}
	public List<String>LineRead(int l)throws LineNotFoundException,IOException{
		if(!f.exists())throw new FileNotFoundException();
		if(!f.isFile())throw new IOException("Not File.");
		List<List<String>>d=AllRead();
		if(d.size()<l+1)throw new LineNotFoundException(l);
		return d.get(l);
	}
	public void DataWrite(String data,int l,int c)throws LineNotFoundException,ColumnNotFoundException,IOException{
		if(!f.exists())throw new FileNotFoundException();
		if(!f.isFile())throw new IOException("Not File.");
		List<String>d=LineRead(l);
		if(d.size()<c+1)throw new ColumnNotFoundException(l,c);
		d.set(c,data);
		LineWrite(d,l);
	}
	public String DataRead(int l,int c)throws LineNotFoundException,ColumnNotFoundException,IOException{
		if(!f.exists())throw new FileNotFoundException();
		if(!f.isFile())throw new IOException("Not File.");
		List<String>d=LineRead(l);
		if(d.size()<c+1)throw new ColumnNotFoundException(l,c);
		return d.get(c);
	}
	public void addLine(List<String>line)throws IOException{
		if(!f.exists())throw new FileNotFoundException();
		if(!f.isFile())throw new IOException("Not File.");
		List<List<String>>d=AllRead();
		d.add(line);
		AllWrite(d);
	}
	public void addData(String data,int l)throws LineNotFoundException,IOException{
		if(!f.exists())throw new FileNotFoundException();
		if(!f.isFile())throw new IOException("Not File.");
		List<String>d=LineRead(l);
		d.add(data);
		LineWrite(d,l);
	}
	public List<Point>getDatas(String data)throws IOException{
		if(!f.exists())throw new FileNotFoundException();
		if(!f.isFile())throw new IOException("Not File.");
		List<Point>d=new ArrayList<Point>();
		List<List<String>>a=AllRead();
		for(int b=0;b<a.size();b++)for(int c=0;c<a.get(b).size();c++)if(a.get(b).get(c).equalsIgnoreCase(data))d.add(new Point(c,b));
		return d;
	}
	public boolean isCSVFile()throws IOException{
		if(!f.exists())throw new FileNotFoundException();
		if(!f.isFile())throw new IOException("Not File.");
		String[]n=f.getName().split("\\.");
		if(n[n.length-1].equalsIgnoreCase("csv"))return true;
		else return false;
	}
	@Override
	public void run(){
	}
}