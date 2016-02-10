/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csv2rdf.DataType;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
/**
 *
 * @author ksharma
 */
public class TypeGuesser {
    
    public static boolean isBoolean(String val)
    {
         if(val == null)
            return false;
        return (val.equalsIgnoreCase("true") || val.equalsIgnoreCase("false") || val.equalsIgnoreCase("yes") || val.equalsIgnoreCase("no") || val.equalsIgnoreCase("1") || val.equalsIgnoreCase("0"));
    }
    
    public static boolean isNumeric(String val) 
    {
         if(val == null)
            return false;
         
        boolean result1, result2;
        java.text.NumberFormat formatter =  java.text.NumberFormat.getInstance();
        java.text.ParsePosition p = new java.text.ParsePosition(0);
        formatter.parse(val, p);
        result1 = val.length() == p.getIndex();

        java.util.Scanner sc = new java.util.Scanner(val);
        result2 = sc.hasNextInt();

        return result1 && result2 && val.matches("^-?[0-9]+(\\\\.[0-9]+)?$");
    }
    
    
    public static boolean isInteger(String val)
    {
        if(!TypeGuesser.isNumeric(val))
            return false;
      
       try{
           int p = Integer.parseInt(val);
           return true;
       }
       catch(Exception ex){}
       
       return false;
    }
    
    public static boolean isFloat(String val)
    {
        if(val == null)
            return false;
        try{
           float p = Float.parseFloat(val);
           return true;
        }
        catch(Exception ex){ }
       
        return false;
    }
     
    public static boolean isDouble(String val)
    {
        if(val == null)
            return false;
        
       try{
           double p = Double.parseDouble(val);
           return true;
       }
       catch(Exception ex){ }
       
       return false;
    }
    
    public static boolean isdDate(String val)
    {
       if(val == null)
            return false;
        
       List<String> dfs = TypeGuesser.getRequiredDateFormats();
       boolean result = false;
       for(String sf : dfs)
       {
        try {
            
             SimpleDateFormat df = new SimpleDateFormat(sf);
             java.util.Date date = df.parse(val);
             result = true;
             break;
         } catch (Exception e) {}
       }
       return result;
    }
    
    private static List<String> getRequiredDateFormats()
    {
            List dateFormats = new ArrayList<String>() {{
                add("M/dd/yyyy");
                add("dd.M.yyyy");
                add("M/dd/yyyy hh:mm:ss a");
                add("dd.M.yyyy hh:mm:ss a");
                add("dd.MMM.yyyy");
                add("dd-MMM-yyyy");
                add("dd/MM/yyyy");
            }
        };
            
           return dateFormats;
    }
    
    public static boolean isDate(String date) {
 
    // some regular expression
    String time = "(\\s(([01]?\\d)|(2[0123]))[:](([012345]\\d)|(60))"
        + "[:](([012345]\\d)|(60)))?"; // with a space before, zero or one time
 
    // no check for leap years (Schaltjahr)
    // and 31.02.2006 will also be correct
    String day = "(([12]\\d)|(3[01])|(0?[1-9]))"; // 01 up to 31
    String month = "((1[012])|(0\\d))"; // 01 up to 12
    String year = "\\d{4}";
 
    // define here all date format
    ArrayList<Pattern> patterns = new ArrayList<Pattern>();
    patterns.add(Pattern.compile(day + "[-.]" + month + "[-.]" + year + time));
    patterns.add(Pattern.compile(year + "-" + month + "-" + day + time));
    // here you can add more date formats if you want
 
    // check dates
    for (Pattern p : patterns)
      if (p.matcher(date).matches())
        return true;
 
    return false;
 
  }
}
