package com.wipro.filebatchDemo;

import org.springframework.batch.item.ItemProcessor;

public class FileItemProcessor  implements ItemProcessor<LineModel, LineModel> {
    public LineModel process(LineModel line) throws Exception {
    	LineModel processedLine=new LineModel();
    	processedLine.setLineToProcess(runCaesar(line.getLineToProcess(),3));
    	return processedLine;
    }
    
    public static String runCaesar(String line, int s) 
    { 
        StringBuffer result= new StringBuffer(); 
  
        for (int i=0; i<line.length(); i++) 
        { 
            if (Character.isUpperCase(line.charAt(i))) 
            { 
               
                result.append((char)(((int)line.charAt(i) + 
                        s - 65) % 26 + 65)); 
            } 
            else
            { 
                result.append((char)(((int)line.charAt(i) + 
                        s - 97) % 26 + 97)); 
            } 
        } 
        return result.toString(); 
    } 
    
  /*  public static void main(String[] args) {
		//To test
    	String str="ABC";
    	System.out.println(encrypt(str, 2));
    	
    	//Caesar ciphered by 2 alphabets
    	//Result CDE
	}*/
}
