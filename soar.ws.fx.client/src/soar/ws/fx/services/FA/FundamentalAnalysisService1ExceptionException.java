
/**
 * FundamentalAnalysisService1ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package soar.ws.fx.services.FA;

public class FundamentalAnalysisService1ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1436366189677L;
    
    private soar.ws.fx.services.FA.FundamentalAnalysisService1Stub.FundamentalAnalysisService1Exception faultMessage;

    
        public FundamentalAnalysisService1ExceptionException() {
            super("FundamentalAnalysisService1ExceptionException");
        }

        public FundamentalAnalysisService1ExceptionException(java.lang.String s) {
           super(s);
        }

        public FundamentalAnalysisService1ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public FundamentalAnalysisService1ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(soar.ws.fx.services.FA.FundamentalAnalysisService1Stub.FundamentalAnalysisService1Exception msg){
       faultMessage = msg;
    }
    
    public soar.ws.fx.services.FA.FundamentalAnalysisService1Stub.FundamentalAnalysisService1Exception getFaultMessage(){
       return faultMessage;
    }
}
    