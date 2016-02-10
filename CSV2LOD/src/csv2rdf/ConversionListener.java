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
public interface ConversionListener {
    
    public void DidFinishConversion();
    public void DidFinishConversionWithError(String errDesc);
}
