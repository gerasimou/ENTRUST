
/**
 * TechnicalAnalysisService4ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package fx.services.TA;

public class TechnicalAnalysisService4ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1440380147103L;
    
    private fx.services.TA.TechnicalAnalysisService4Stub.TechnicalAnalysisService4Exception faultMessage;

    
        public TechnicalAnalysisService4ExceptionException() {
            super("TechnicalAnalysisService4ExceptionException");
        }

        public TechnicalAnalysisService4ExceptionException(java.lang.String s) {
           super(s);
        }

        public TechnicalAnalysisService4ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public TechnicalAnalysisService4ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(fx.services.TA.TechnicalAnalysisService4Stub.TechnicalAnalysisService4Exception msg){
       faultMessage = msg;
    }
    
    public fx.services.TA.TechnicalAnalysisService4Stub.TechnicalAnalysisService4Exception getFaultMessage(){
       return faultMessage;
    }
}
    