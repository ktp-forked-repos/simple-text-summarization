/*************************************/
/* Ver.081105                        */
/*-----------------------------------*/
/* Program by Fu,Yu-Hsiang in Aizu   */
/*************************************/
//Import
import java.lang.*;
import java.io.*;
import java.util.*;
import java.text.*;

//import AWT
import java.awt.*;
import java.awt.font.*;
import java.awt.event.*;

//import Swing
import javax.swing.*;

//Sentence
class Sentence{
	//Parameter
 	public String sentence;
 	public int value;
 	public int no;

 	//Sentence
 	public Sentence(String x){
	 	sentence=x;
	 	value=0;
	 	no=-1;
	}
}

//Project3
public class Project3{
	//Pathe of input and outpt
    private String pathin;
    private String pathout;

    //Query
    private ArrayList<String> query;

	//Output list
	private ArrayList<Sentence> op;

    //GUI Component
    private JFrame jframe;
    private JTextArea jtarea;
    private JScrollPane jspane;
    private Container ccontrolpanel;
    private JButton jbquery,jbload,jbrun,jbsave,jbclear,jbexit;
    private JLabel jlquery;
    private JTextField jtfquery;

	//Project3
    public Project3(){
	    //Initialization
	    Initialization();

	    //GUI Interface
	    GUIInterface();
    }

    //Initialization
    private void Initialization(){
    	pathin="";
		pathout="";
		query = new ArrayList<String>();
        op = new ArrayList<Sentence>();
	}

    //GUI Interface
    private void GUIInterface(){
	    //Main frame window,JFrame
	    //Title Information
	    jframe = new JFrame("Aizu - Intelligent Information Retrieval and Text Mining - Project 3");
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    jframe.setSize(640,480);

	    //BorderLayout
	    jframe.setLayout(new BorderLayout(2,2));

	    //Display Panel
	    //JPanel
	    jtarea = new JTextArea("Aizu - Intelligent Information Retrieval and Text Mining - Project 3");
	    jtarea.append("\n--");
	    jtarea.append("\nProgram by Fu, Yu-Hsiang (m5128110), 11/05/2008 in Aizu.");
        jtarea.append("\n--");
	    jtarea.append("\nPlease load the query file.");
	    jtarea.setEditable(false);
	    jtarea.setDisabledTextColor(Color.BLACK);

	    //JScrollPane
		jspane = new JScrollPane(jtarea);
		jframe.add(jspane,BorderLayout.CENTER);

		//Control Panel
		//Container
		ccontrolpanel = new Container();

		//FlowLayout
		ccontrolpanel.setLayout(new FlowLayout());

	    //JButton
	    jbquery = new JButton("Query");
	    jbload = new JButton("Load");
		jbrun = new JButton("Run");
		jbsave = new JButton("Save");
		jbclear = new JButton("Clear");
		jbexit = new JButton("Exit");
		jbload.setEnabled(false);
		jbrun.setEnabled(false);
		jbsave.setEnabled(false);

		//query
	    jbquery.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					query.clear();

					//JfileChooser
					JFileChooser jfchooser = new JFileChooser();
					jfchooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

					int result = jfchooser.showOpenDialog(null);
					if(result==JFileChooser.CANCEL_OPTION){
						return;
					}

					//FileName
					File filename = jfchooser.getSelectedFile();
					if((filename==null)||(filename.getName().equals(""))){
                    	JOptionPane.showMessageDialog(null,"Invalid File Name","Invalid File Name",JOptionPane.ERROR_MESSAGE);
                    	return;
					}

					if(filename.exists()){
						try{
				    		BufferedReader br = new BufferedReader(new FileReader(filename.getAbsolutePath()));
                            String s;

				            while((s=br.readLine())!=null){
								//PorterStemmingAlgorithm
								jtarea.append("\n--\n0.PorterStemmingAlgorithm: ");

								StringTokenizer st = new StringTokenizer(s);

                                jtarea.append("\n--\nQuery: ");
					            while(st.hasMoreTokens()){
						            String sTemp=st.nextToken();
						            sTemp.trim();
						            jtarea.append(sTemp+"  ");
                                	query.add(sTemp);
								}

								query = new PorterStemmingAlgorithm(query).getStems();
							}

							jtarea.append("\nStem: ");
							for(int a=0;a<query.size();a++){
                                jtarea.append(query.get(a)+"  ");
							}

							jtarea.append("\n--\nThe Query is loaded. Please execute 'Load'.");
							jbload.setEnabled(true);
						}
						catch(FileNotFoundException e){
							jtarea.append("\n -Load Query:Can't find the file!!");
						}
						catch(IOException e){
							jtarea.append("\n -Load Query:Error of I/O!!");
						}
				        catch(Exception e){
				            jtarea.append("\n -Load Query:Exception!!");
				        }
					}
					else{
                    	JOptionPane.showMessageDialog(null,filename+" does not exit.","ERROR",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		);

		//Load
	    jbload.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					//JfileChooser
					JFileChooser jfchooser = new JFileChooser();
					jfchooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

					int result = jfchooser.showOpenDialog(null);
					if(result==JFileChooser.CANCEL_OPTION){
						return;
					}

					//FileName
					File filename = jfchooser.getSelectedFile();
					if((filename==null)||(filename.getName().equals(""))){
                    	JOptionPane.showMessageDialog(null,"Invalid File Name","Invalid File Name",JOptionPane.ERROR_MESSAGE);
                    	return;
					}

					if(filename.exists()){
						String finpath=filename.getAbsolutePath();

                		StringTokenizer st = new StringTokenizer(finpath,".");
						String foutpath=st.nextToken()+"_result."+st.nextToken();

						setPathin(finpath);
						setPathout(foutpath);

						jtarea.append("\n--\nThe File is loaded. Please execute 'Run'.");
						jbrun.setEnabled(true);
					}
					else{
                    	JOptionPane.showMessageDialog(null,filename+" does not exit.","ERROR",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		);

		//Run
	    jbrun.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					jtarea.append("\n--");
				    //Reconstruct
				    jtarea.append("\n -1.Reconstruction");
				    Reconstruction();

				    //Sentence Boundary Detection
					jtarea.append("\n -2.Sentence Boundary Dection");
				    SentenceBoundaryDetection();
				    jbsave.setEnabled(true);

				    //Summarization
					jtarea.append("\n -3.Summarization");
					jtarea.append("\n--");
				    Summarization();
				}
			}
		);

		//Save
	    jbsave.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					jtarea.append("\n--\nsave");

					//JfileChooser
					JFileChooser jfchooser = new JFileChooser();

					int result = jfchooser.showSaveDialog(null);
					if(result==JFileChooser.APPROVE_OPTION){
						File filename = jfchooser.getSelectedFile();
						String foutname = filename.getAbsolutePath();
						foutname.endsWith(".txt");

				        //Set the path of output
				        setPathout(foutname);

				    	try{
							BufferedWriter wr = new BufferedWriter(new FileWriter(pathout));

							//Wrihte op
							for(int a=0;a<op.size();a++){
				            	wr.write(op.get(a).sentence);
				            	wr.newLine();
							}
							//Close writer
				            wr.close();
				        }
						catch(FileNotFoundException e){
							jtarea.append("\n -FileWriter:Can't find the file!!");
						}
						catch(IOException e){
							jtarea.append("\n -FileWriter:Error of I/O!!");
						}
				        catch(Exception e){
				            jtarea.append("\n -FileWriter:Exception!!");
				        }

				        jtarea.append("\nThe result is saved.");
					}
				}
			}
		);

		//Clear
	    jbclear.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
				    jtarea.setText("Aizu - Intelligent Information Retrieval and Text Mining - Project 3");
				    jtarea.append("\n--");
				    jtarea.append("\nProgram by Fu, Yu-Hsiang (m5128110), 11/05/2008 in Aizu.");
			        jtarea.append("\n--");
				    jtarea.append("\nPlease load the query file.");

					pathin="";
					pathout="";

				    jbload.setEnabled(false);
					jbrun.setEnabled(false);
					jbsave.setEnabled(false);
				}
			}
		);

		//Exit
	    jbexit.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					JOptionPane.showMessageDialog(null,"Have a nice day! Bye~ : )");
					System.exit(0);
				}
			}
		);

        ccontrolpanel.add(jbquery);
        ccontrolpanel.add(jbload);
		ccontrolpanel.add(jbrun);
        ccontrolpanel.add(jbsave);
        ccontrolpanel.add(jbclear);
        ccontrolpanel.add(jbexit);
		jframe.add(ccontrolpanel,BorderLayout.SOUTH);

	    //Show jframe
	    jframe.setVisible(true);
	}

	//Detect the End-Of-Sentence
    //Reconstruct the text
    private void Reconstruction(){
        //Set the path of input and output
        setPathin(pathin);
        setPathout(pathin);

        //Load text
        try{
    		BufferedReader br = new BufferedReader(new FileReader(pathin));

            String sin;//One string of each time
            String sout="";

            while((sin=br.readLine())!=null){
	            sin=sin.trim();
	            if(sin.length()>0){
                	sout+=" "+sin;
				}
            }

			//Prune the result
            sout=sout.trim();

            //Close reader
            br.close();

			BufferedWriter wr = new BufferedWriter(new FileWriter(pathout));

			//Write the result
            wr.write(sout);

            //Close writer
            wr.close();
        }
		catch(FileNotFoundException e){
			jtarea.append("\n -Reconstruction:Can't find the file!!");
		}
		catch(IOException e){
			jtarea.append("\n -Reconstruction:Error of I/O!!");
		}
        catch(Exception e){
            jtarea.append("\n -Reconstruction:Exception!!");
        }
	}

    //Sentence Boundary Detection
    private void SentenceBoundaryDetection(){
        //Set the path of input
        setPathin(pathin);

        //Load text
        try{
    		BufferedReader br = new BufferedReader(new FileReader(pathin));

            String s;//One string of each time

            //Position list
			ArrayList<Integer> ps = new ArrayList<Integer>();//arraylist of position
			ps.add(0);//Start position

			//Output list
			op.clear();//arraylist of position

            while((s=br.readLine())!=null){
	            for(int a=0;a<s.length();a++){
		            int current=a;

		            //Right context
		            boolean checkrightcontext=false;

		            if(((int)s.charAt(a)==46) && (a<(s.length()-4))){
			            //Check rules
			            //Rule 1: . + space + A~Z
			            if((int)s.charAt(a+1)==32 && ((int)s.charAt(a+2)>=65 && (int)s.charAt(a+2)<=90)){
                        	checkrightcontext=true;
						}
						//Rule 2: . + space + " + A~Z
			            if((int)s.charAt(a+1)==32 && (int)s.charAt(a+2)==34 && ((int)s.charAt(a+3)>=65 && (int)s.charAt(a+3)<=90)){
                        	checkrightcontext=true;
						}
						//Rule 3: . + space + ' + A~Z
			            if((int)s.charAt(a+1)==32 && (int)s.charAt(a+2)==39 && ((int)s.charAt(a+3)>=65 && (int)s.charAt(a+3)<=90)){
                        	checkrightcontext=true;
						}
						//Rule 4: . + " + space + "
			            if((int)s.charAt(a+1)==34 && (int)s.charAt(a+2)==32 && (int)s.charAt(a+3)==34){
                        	checkrightcontext=true;
                        	current+=1;
						}
						//Rule 5: . + " + space + A~Z
			            if((int)s.charAt(a+1)==34 && (int)s.charAt(a+2)==32 && ((int)s.charAt(a+3)>=65 && (int)s.charAt(a+3)<=90)){
                        	checkrightcontext=true;
                        	current+=1;
						}
						//Rule 6: " + . + space + A~Z
			            if((int)s.charAt(a-1)==34 && ((int)s.charAt(a+1)==32) && ((int)s.charAt(a+2)>=65 && (int)s.charAt(a+2)<=90)){
                        	checkrightcontext=true;
						}
					}
					else{
						//Others: should be the last one of .
						if(((int)s.charAt(a)==46) && (a>=(s.length()-4))){
	                    	checkrightcontext=true;
						}
					}

					//Left context
                    boolean checkleftcontext=false;

		            if(checkrightcontext){
                     	String ss=s.substring((ps.get(ps.size()-1)),(a+1));
                     	ss=ss.trim();

                     	int p1=ss.lastIndexOf(" ");
                     	int p2=ss.lastIndexOf(".");

                     	if(p1<0){
							p1=0;
						}

						ss=ss.substring(p1,(p2+1));
						ss=ss.trim();

						char[] css=ss.toCharArray();

						//Check abbreviation
						boolean firstCapital=false;
						boolean size=false;
						boolean twomorefullstop=false;
						boolean number=false;

						//Check the First character is capital
						if((css[0]>=65 && css[0]<=90) && !(css[1]>=65 && css[1]<=90)){
							firstCapital=true;
						}
						//Check the size of the word
						if(css.length>0 && css.length<=5){
                        	size=true;
						}
						//Check two more period
		                //StringTokenizer
		                StringTokenizer st = new StringTokenizer(ss,".");
		                if(st.countTokens()>=2){
                        	twomorefullstop=true;

                        	//Check number
							if(css[0]>=30 && css[0]<=39){
								number=true;
							}
						}

						//Check all of the conditions
						if((firstCapital && size) || number || twomorefullstop){
                        	checkleftcontext=true;
						}
					}

					//Add index
					if(checkrightcontext && !checkleftcontext){
                    	ps.add(current+1);
					}
				}

				//Break the text and crate the sentences
				for(int a=0;a<(ps.size()-1);a++){
					String ss=s.substring((ps.get(a)),(ps.get(a+1)));

					op.add(new Sentence(ss.trim()));
					op.get(a).no=(a+1);//Give the number of sentences

					//jtarea.append("\nS"+(a+1)+": "+ss);
				}
            }

            //Close reader
            br.close();
        }
		catch(FileNotFoundException e){
			jtarea.append("\n -CheckRightContext:Can't find the file!!");
		}
		catch(IOException e){
			jtarea.append("\n -CheckRightContext:Error of I/O!!");
		}
        catch(Exception e){
            jtarea.append("\n -CheckRightContext:Exception!!");
        }
	}

	//Summarization
	private void Summarization(){
		for(int a=(op.size()-1);a>=0;a--){
			boolean check=false;
			int tempValue=0;
			String s1Temp=op.get(a).sentence.toLowerCase();

        	for(int b=0;b<query.size();b++){
	        	String s2Temp=query.get(b).toLowerCase();

				if(s1Temp.indexOf(s2Temp)!=-1){
                	check=true;
                	tempValue++;
				}
			}

			if(!check){
             	op.remove(a);
			}
			else{
				op.get(a).value=tempValue;
			}
		}

		//Sort
		Sort(op);

		jtarea.append("\nQuery(Stem): ");
		for(int a=0;a<query.size();a++){
            jtarea.append(query.get(a)+"  ");
		}

        jtarea.append("\n--\nSummary: ");
		for(int a=0;a<(op.size()-1);a++){
			jtarea.append("\nS"+op.get(a).no+"("+op.get(a).value+"): "+op.get(a).sentence);
		}
	}

	//Sort
	private void Sort(ArrayList<Sentence> x){
    	QuickSort(x,0,x.size()-1);
	}

    //QuickSort
    private void QuickSort(ArrayList<Sentence> al,int left,int right){
	    int k;

	    if(left<right){
        	k=Partition(al,left,right);
        	QuickSort(al,left,k-1);
        	QuickSort(al,k+1,right);
		}
    }

	//Partition
    private int Partition(ArrayList<Sentence> al,int left,int right){
    	int i;
		double tempValue;

    	tempValue=al.get(right).value;
    	i=left-1;

    	for(int a=left;a<right;a++){
	    	if(al.get(a).value>tempValue){//Find Maximun or Minimun
            	i++;
            	Swap(al.get(i),al.get(a));
			}
		}
		Swap(al.get(i+1),al.get(right));
		return i+1;
	}

    //Swap
    private void Swap(Sentence x,Sentence y){
	    String tempSentence;
	    int tempValue;
	    int tempNo;

		//SentenceX -> tempSentence
        tempSentence=x.sentence;
        tempValue=x.value;
        tempNo=x.no;

		//SentenceY -> SentenceX
    	x.sentence=y.sentence;
    	x.value=y.value;
    	x.no=y.no;

		//tempSentence -> SentenceY
    	y.sentence=tempSentence;
    	y.value=tempValue;
    	y.no=tempNo;
    }

	//Set the path of input
	private void setPathin(String x){
    	pathin=x;
	}

	//Set the path of output
	private void setPathout(String x){
		pathout=x;
	}

    public static void main(String args[]){
	    Project3 P3 = new Project3();
    }
}