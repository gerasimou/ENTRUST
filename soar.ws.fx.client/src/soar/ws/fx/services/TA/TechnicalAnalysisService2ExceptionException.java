
/**
 * TechnicalAnalysisService2ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package soar.ws.fx.services.TA;

public class TechnicalAnalysisService2ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1436006874569L;
    
    private soar.ws.fx.services.TA.TechnicalAnalysisService2Stub.TechnicalAnalysisService2Exception faultMessage;

    
        public TechnicalAnalysisService2ExceptionException() {
            super("TechnicalAnalysisService2ExceptionException");
        }

        public TechnicalAnalysisService2ExceptionException(java.lang.String s) {
           super(s);
        }

        public TechnicalAnalysisService2ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public TechnicalAnalysisService2ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(soar.ws.fx.services.TA.TechnicalAnalysisService2Stub.TechnicalAnalysisService2Exception msg){
       faultMessage = msg;
    }
    
    public soar.ws.fx.services.TA.TechnicalAnalysisService2Stub.TechnicalAnalysisService2Exception getFaultMessage(){
       return faultMessage;
    }
}
    