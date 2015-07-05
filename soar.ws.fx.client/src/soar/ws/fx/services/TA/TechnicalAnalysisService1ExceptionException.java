
/**
 * TechnicalAnalysisService1ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package soar.ws.fx.services.TA;

public class TechnicalAnalysisService1ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1436006867248L;
    
    private soar.ws.fx.services.TA.TechnicalAnalysisService1Stub.TechnicalAnalysisService1Exception faultMessage;

    
        public TechnicalAnalysisService1ExceptionException() {
            super("TechnicalAnalysisService1ExceptionException");
        }

        public TechnicalAnalysisService1ExceptionException(java.lang.String s) {
           super(s);
        }

        public TechnicalAnalysisService1ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public TechnicalAnalysisService1ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(soar.ws.fx.services.TA.TechnicalAnalysisService1Stub.TechnicalAnalysisService1Exception msg){
       faultMessage = msg;
    }
    
    public soar.ws.fx.services.TA.TechnicalAnalysisService1Stub.TechnicalAnalysisService1Exception getFaultMessage(){
       return faultMessage;
    }
}
    