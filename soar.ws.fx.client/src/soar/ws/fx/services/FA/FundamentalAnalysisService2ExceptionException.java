
/**
 * FundamentalAnalysisService2ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package soar.ws.fx.services.FA;

public class FundamentalAnalysisService2ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1436366222388L;
    
    private soar.ws.fx.services.FA.FundamentalAnalysisService2Stub.FundamentalAnalysisService2Exception faultMessage;

    
        public FundamentalAnalysisService2ExceptionException() {
            super("FundamentalAnalysisService2ExceptionException");
        }

        public FundamentalAnalysisService2ExceptionException(java.lang.String s) {
           super(s);
        }

        public FundamentalAnalysisService2ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public FundamentalAnalysisService2ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(soar.ws.fx.services.FA.FundamentalAnalysisService2Stub.FundamentalAnalysisService2Exception msg){
       faultMessage = msg;
    }
    
    public soar.ws.fx.services.FA.FundamentalAnalysisService2Stub.FundamentalAnalysisService2Exception getFaultMessage(){
       return faultMessage;
    }
}
    