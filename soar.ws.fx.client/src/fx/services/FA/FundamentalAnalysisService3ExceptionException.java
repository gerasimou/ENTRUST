
/**
 * FundamentalAnalysisService3ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package fx.services.FA;

public class FundamentalAnalysisService3ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1440379427635L;
    
    private fx.services.FA.FundamentalAnalysisService3Stub.FundamentalAnalysisService3Exception faultMessage;

    
        public FundamentalAnalysisService3ExceptionException() {
            super("FundamentalAnalysisService3ExceptionException");
        }

        public FundamentalAnalysisService3ExceptionException(java.lang.String s) {
           super(s);
        }

        public FundamentalAnalysisService3ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public FundamentalAnalysisService3ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(fx.services.FA.FundamentalAnalysisService3Stub.FundamentalAnalysisService3Exception msg){
       faultMessage = msg;
    }
    
    public fx.services.FA.FundamentalAnalysisService3Stub.FundamentalAnalysisService3Exception getFaultMessage(){
       return faultMessage;
    }
}
    