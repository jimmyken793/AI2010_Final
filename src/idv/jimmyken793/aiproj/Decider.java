package idv.jimmyken793.aiproj;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import idv.jimmyken793.aiproj.database.Database;

public class Decider {
	public static String decide(Database db,String pre1,String pre2,String input) throws ClassNotFoundException, SQLException, FileNotFoundException{
		String path="key_to_word"+File.separator;
		String list1 = "";
		String list2 = "";
		String list3 = "";
		String output = "";
		
		if(pre1.length()>0){
			pre1=pre1.trim();
			pre1="_"+pre1+"_";
		}
		if(pre2.length()>0){
			pre2=pre2.trim();
			pre2="_"+pre2+"_";
		}
		input=input.trim();
		input="_"+input+"_";
		
		if(pre1.length()==0 && pre2.length()==0){
			try{
				FileInputStream fis=new FileInputStream(new File(path+input));
				BufferedReader br=new BufferedReader(new InputStreamReader(fis,"UTF-8"));
				list3=br.readLine();
				br.close();
				fis.close();
				output=list3.substring(0,1);
			}
			catch(Exception e){
				output=e.toString();
				throw new FileNotFoundException();
			}
		}
		else if(pre1.length()==0 && pre2.length()!=0){
			try{
				FileInputStream fis=new FileInputStream(new File(path+pre2));
				BufferedReader br=new BufferedReader(new InputStreamReader(fis,"UTF-8"));
				list2=br.readLine();
				br.close();
				fis.close();
				fis=new FileInputStream(new File(path+input));
				br=new BufferedReader(new InputStreamReader(fis,"UTF-8"));
				list3=br.readLine();
				br.close();
				fis.close();
				
				int best=-1;
				for(int i=0;i<list2.length();i++)
					for(int j=0;j<list3.length();j++){
						String s=list2.substring(i,i+1)+list3.substring(j,j+1);
						int score=db.getData2(s);
						if(score>best){
							best=score;
							output=s;
						}
					}
			}
			catch(Exception e){
				output=e.toString();
				throw new FileNotFoundException();
			}
		}
		else{
			try{
				FileInputStream fis=new FileInputStream(new File(path+pre1));
				BufferedReader br=new BufferedReader(new InputStreamReader(fis,"UTF-8"));
				list1=br.readLine();
				br.close();
				fis.close();
				fis=new FileInputStream(new File(path+pre2));
				br=new BufferedReader(new InputStreamReader(fis,"UTF-8"));
				list2=br.readLine();
				br.close();
				fis.close();
				fis=new FileInputStream(new File(path+input));
				br=new BufferedReader(new InputStreamReader(fis,"UTF-8"));
				list3=br.readLine();
				br.close();
				fis.close();
				
				int best=-1;
				for(int i=0;i<list1.length();i++)
					for(int j=0;j<list2.length();j++){
						String s=list1.substring(i,i+1)+list2.substring(j,j+1);
						int score=db.getData2(s);
						if(score>best){
							best=score;
							output=s+list3.substring(0,1);
						}
					}
				for(int i=0;i<list2.length();i++)
					for(int j=0;j<list3.length();j++){
						String s=list2.substring(i,i+1)+list3.substring(j,j+1);
						int score=db.getData2(s);
						if(score>best){
							best=score;
							output=s;
						}
					}
				for(int i=0;i<list1.length();i++)
					for(int j=0;j<list2.length();j++)
						for(int k=0;k<list3.length();k++){
							String s=list1.substring(i,i+1)+list2.substring(j,j+1)+list3.substring(k,k+1);
							int score=db.getData3(s);
							if(score>10){
								best=5000;
								output=s;
							}
						}
			}
			catch(Exception e){
				output=e.toString();
				throw new FileNotFoundException();
			}
		}
		return output;
	}
}
