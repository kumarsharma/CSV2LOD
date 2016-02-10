/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csv2rdf;

/**
 *
 * @author ksharma
 */
public class StringUtil {
    
    public static boolean hasDuplicates(Object[] data)
    {
        if(data == null || data.length <= 0)
            return false;
        
        boolean hasDuplicate = false;
        for(int i=0;i<data.length;i++)
        {
            for(int j= 0; j<data.length;j++)
            {
                if(i!=j)
                {
                    if(data[i].equals(data[j]))
                    {
                        hasDuplicate = true;
                        break;
                    }
                }
            }
            if(hasDuplicate)
                break;
        }
        
        return hasDuplicate;
    }
}
