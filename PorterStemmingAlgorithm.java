/*************************************/
/* Porter Stemming Algorithm         */
/*-----------------------------------*/
/* Ver.081111                        */
/*-----------------------------------*/
/* Program by Fu,Yu-Hsiang in Aizu   */
/*************************************/
//Import
import java.lang.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class PorterStemmingAlgorithm{
	private ArrayList<String> stems;

	public PorterStemmingAlgorithm(ArrayList<String> query){
    	//LowerCase
    	for(int a=0;a<query.size();a++){
	    	query.set(a,query.get(a).toLowerCase());
		}

		//Steps
    	for(int a=0;a<query.size();a++){
	    	String str=query.get(a);

	    	str=Step1a(str);//Step1a
	    	str=Step1b(str);//Step1b
	    	str=Step1c(str);//Step1c
	    	str=Step2(str);//Step2
	    	str=Step3(str);//Step3
	    	str=Step4(str);//Step4
	    	str=Step5a(str);//Step5a
	    	str=Step5b(str);//Step5b

			query.set(a,str);
		}

		stems=query;
	}

	//Step1a
	private String Step1a(String x){
		if(x.endsWith("sses") || x.endsWith("ies")){
			x=Modify(x,2);
		}
		else if(x.endsWith("ss")){
			x=Modify(x,0);
		}
		else if(x.endsWith("s")){
			x=Modify(x,1);
		}

		return x;
	}

	//Modify
	private String Modify(String x,int y){
		x=x.substring(0,(x.length()-y));
		return x;
	}

	//Step1b
	private String Step1b(String x){
		boolean second=false;
		boolean third=false;

		if(Measure(Stem(x,"eed"))>0 && x.endsWith("eed")){
			x=Modify(x,1);
		}
		else if(VowelInStem(Stem(x,"ed")) && x.endsWith("ed")){
			x=Modify(x,2);
			second=true;
		}
		else if(VowelInStem(Stem(x,"ing")) && x.endsWith("ing")){
			x=Modify(x,3);
			third=true;
		}
		if(second || third){
			if(x.endsWith("at") || x.endsWith("bl") || x.endsWith("iz")){
				x=Append(x,"e");
			}
			else if(DoubleConsonant(x.toCharArray()) && !(x.endsWith("l") || x.endsWith("s") || x.endsWith("z"))){
             	x=Modify(x,1);
			}
			else if(Measure(x)==1 && CVC(x)){
				x=Append(x,"e");
			}
		}

		return x;
	}

	//Stem
	private String Stem(String x,String y){
		if(x.length()>y.length()){
        	String str=x.substring(0,x.length()-y.length());
        	return str;
		}

    	return x;
	}

	//Measure
	private int Measure(String x){
		return VC(Consonant(x.toCharArray()));//VC
	}

	//Consonant
	private char[] Consonant(char[] x){
		for(int a=0;a<x.length;a++){
			switch(x[a]){
				case 'a':
				case 'e':
				case 'i':
				case 'o':
				case 'u':
					x[a]='v';
					break;
				case 'y':
					if(a==0){
                    	x[a]='c';
					}
					else if(x[a-1]=='c'){
                        x[a]='v';
					}
					else{
                    	x[a]='c';
					}
					break;
				default:
					x[a]='c';
					break;
			}
		}

		return x;
	}

	//VC
	private int VC(char[] x){
		int m=0;
		String VC="";

		for(int a=1;a<x.length;a++){
			if(x[a]!=x[a-1]){
				if(x[a-1]=='c'){
					VC+="C";
				}
				else{
					VC+="V";
				}

			}
			if(a==x.length-1){
				if(x[a]=='c'){
					VC+="C";
				}
				else{
					VC+="V";
				}
			}
		}

		//Pick the center part of [C](VC)^m[V]
		if(VC.startsWith("C")){
        	VC=VC.substring((VC.indexOf("C")+1),VC.length());
		}
		if(VC.endsWith("V")){
        	VC=VC.substring(0,VC.length()-1);
		}

		VC=VC.replaceAll("VC","1");//Replace

		if(VC.indexOf("1")!=-1 && VC.lastIndexOf("1")!=-1){
			VC=VC.substring(VC.indexOf("1"),VC.lastIndexOf("1")+1);

			for(int a=0;a<VC.length();a++){
	        	m++;
			}
		}

		return m;
	}

	//VowelInStem
	private boolean VowelInStem(String x){
		if(x.length()>=3){
			char[] ca=Consonant(x.toCharArray());

			String str="";
			for(int a=0;a<ca.length;a++){
				str+=String.valueOf(ca[a]);
			}

	    	if(str.indexOf("v")!=-1){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}

	//Append
	private String Append(String x,String y){
    	return x+y;
	}

	//DoubleConsonant
	private boolean DoubleConsonant(char[] x){
    	if(x[x.length-1]==x[x.length-2]){
			return true;
		}
		else{
			return false;
		}
	}

	//CVC
	private boolean CVC(String x){
		char[] ca=Consonant(x.toCharArray());
		String str="";
		for(int a=0;a<ca.length;a++){
        	str+=String.valueOf(ca[a]);
		}

		if(str.endsWith("cvc") && !(x.endsWith("w") || x.endsWith("x") || x.endsWith("y"))){
			return true;
		}
		else{
			return false;
		}
	}

	//Step1c
	private String Step1c(String x){
    	if(VowelInStem(Stem(x,"y")) && x.endsWith("y")){
        	x=Modify(x,1);
        	x=Append(x,"i");
		}

		return x;
	}

	//Step2
	private String Step2(String x){
		if(Measure(Stem(x,"ational"))>0 && x.endsWith("ational")){
			x=Modify(x,5);
			x=Append(x,"e");
		}
		else if(Measure(Stem(x,"tional"))>0 && x.endsWith("tional")){
			x=Modify(x,2);
		}
		else if(Measure(Stem(x,"enci"))>0 && x.endsWith("enci")){
			x=Modify(x,1);
			x=Append(x,"e");
		}
		else if(Measure(Stem(x,"anci"))>0 && x.endsWith("anci")){
			x=Modify(x,1);
			x=Append(x,"e");
		}
		else if(Measure(Stem(x,"izer"))>0 && x.endsWith("izer")){
			x=Modify(x,1);
		}
		else if(Measure(Stem(x,"abli"))>0 && x.endsWith("abli")){
			x=Modify(x,1);
			x=Append(x,"e");
		}
		else if(Measure(Stem(x,"alli"))>0 && x.endsWith("alli")){
			Modify(x,2);
		}
		else if(Measure(Stem(x,"entli"))>0 && x.endsWith("entli")){
			x=Modify(x,2);
		}
		else if(Measure(Stem(x,"eli"))>0 && x.endsWith("eli")){
			x=Modify(x,2);
		}
		else if(Measure(Stem(x,"ousli"))>0 && x.endsWith("ousli")){
			x=Modify(x,2);
		}
		else if(Measure(Stem(x,"ization"))>0 && x.endsWith("ization")){
			x=Modify(x,5);
			x=Append(x,"e");
		}
		else if(Measure(Stem(x,"ation"))>0 && x.endsWith("ation")){
			x=Modify(x,3);
			x=Append(x,"e");
		}
		else if(Measure(Stem(x,"ator"))>0 && x.endsWith("ator")){
			x=Modify(x,2);
			x=Append(x,"e");
		}
		else if(Measure(Stem(x,"alism"))>0 && x.endsWith("alism")){
			x=Modify(x,3);
		}
		else if(Measure(Stem(x,"iveness"))>0 && x.endsWith("iveness")){
			x=Modify(x,4);
		}
		else if(Measure(Stem(x,"fulness"))>0 && x.endsWith("fulness")){
			x=Modify(x,4);
		}
		else if(Measure(Stem(x,"ousness"))>0 && x.endsWith("ousness")){
			x=Modify(x,4);
		}
		else if(Measure(Stem(x,"alism"))>0 && x.endsWith("alism")){
			x=Modify(x,3);
		}
		else if(Measure(Stem(x,"iviti"))>0 && x.endsWith("iviti")){
			x=Modify(x,3);
			x=Append(x,"e");
		}
		else if(Measure(Stem(x,"biliti"))>0 && x.endsWith("biliti")){
			x=Modify(x,5);
			x=Append(x,"le");
		}

		return x;
	}

	//Step3
	private String Step3(String x){
		if(Measure(Stem(x,"icate"))>0 && x.endsWith("icate")){
			x=Modify(x,3);
		}
		else if(Measure(Stem(x,"ative"))>0 && x.endsWith("ative")){
			x=Modify(x,5);
		}
		else if(Measure(Stem(x,"alize"))>0 && x.endsWith("alize")){
			x=Modify(x,3);
		}
		else if(Measure(Stem(x,"iciti"))>0 && x.endsWith("iciti")){
			x=Modify(x,3);
		}
		else if(Measure(Stem(x,"ical"))>0 && x.endsWith("ical")){
			x=Modify(x,2);
		}
		else if(Measure(Stem(x,"ful"))>0 && x.endsWith("ful")){
			x=Modify(x,3);
		}
		else if(Measure(Stem(x,"ness"))>0 && x.endsWith("ness")){
			x=Modify(x,4);
		}

		return x;
	}

	//Step4
	private String Step4(String x){
		if(Measure(Stem(x,"al"))>1 && x.endsWith("al")){
			x=Modify(x,2);
		}
		else if(Measure(Stem(x,"ance"))>1 && x.endsWith("ance")){
			x=Modify(x,4);
		}
		else if(Measure(Stem(x,"ence"))>1 && x.endsWith("ence")){
			x=Modify(x,4);
		}
		else if(Measure(Stem(x,"er"))>1 && x.endsWith("er")){
			x=Modify(x,2);
		}
		else if(Measure(Stem(x,"ic"))>1 && x.endsWith("ic")){
			x=Modify(x,2);
		}
		else if(Measure(Stem(x,"able"))>1 && x.endsWith("able")){
			x=Modify(x,4);
		}
		else if(Measure(Stem(x,"ible"))>1 && x.endsWith("ible")){
			x=Modify(x,4);
		}
		else if(Measure(Stem(x,"ant"))>1 && x.endsWith("ant")){
			x=Modify(x,3);
		}
		else if(Measure(Stem(x,"ement"))>1 && x.endsWith("ement")){
			x=Modify(x,5);
		}
		else if(Measure(Stem(x,"ment"))>1 && x.endsWith("ment")){
			x=Modify(x,4);
		}
		else if(Measure(Stem(x,"ent"))>1 && x.endsWith("ent")){
			x=Modify(x,3);
		}
		else if(Measure(Stem(x,"ion"))>1 && x.endsWith("ion")){
			x=Modify(x,3);
		}
 		else if(Measure(Stem(x,"ou"))>1 && x.endsWith("ou")){
			x=Modify(x,2);
		}
		else if(Measure(Stem(x,"ism"))>1 && x.endsWith("ism")){
			x=Modify(x,3);
		}
		else if(Measure(Stem(x,"ate"))>1 && x.endsWith("ate")){
			x=Modify(x,3);
		}
		else if(Measure(Stem(x,"iti"))>1 && x.endsWith("iti")){
			x=Modify(x,3);
		}
		else if(Measure(Stem(x,"ous"))>1 && x.endsWith("ous")){
			x=Modify(x,3);
		}
		else if(Measure(Stem(x,"ive"))>1 && x.endsWith("ive")){
			x=Modify(x,3);
		}
		else if(Measure(Stem(x,"ize"))>1 && x.endsWith("ize")){
			x=Modify(x,3);
		}

		return x;
	}

	//Step5a
	private String Step5a(String x){
		if(Measure(Stem(x,"e"))>1 && x.endsWith("e")){
			x=Modify(x,1);
		}
		else if(Measure(Stem(x,"e"))==1 && !CVC(Stem(x,"e")) && x.endsWith("e")){
			x=Modify(x,1);
		}

		return x;
	}

	//Step5b
	private String Step5b(String x){
		if(Measure(Stem(x,"l"))>1 && DoubleConsonant(x.toCharArray()) && x.endsWith("l")){
         	x=Modify(x,1);
		}

		return x;
	}

	public ArrayList<String> getStems(){
		return stems;
	}
}