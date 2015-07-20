
/**
 * MarketWatchService2ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package fx.services.MW;

public class MarketWatchService2ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1436283374105L;
    
    private fx.services.MW.MarketWatchService2Stub.MarketWatchService2Exception faultMessage;

    
        public MarketWatchService2ExceptionException() {
            super("MarketWatchService2ExceptionException");
        }

        public MarketWatchService2ExceptionException(java.lang.String s) {
           super(s);
        }

        public MarketWatchService2ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public MarketWatchService2ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(fx.services.MW.MarketWatchService2Stub.MarketWatchService2Exception msg){
       faultMessage = msg;
    }
    
    public fx.services.MW.MarketWatchService2Stub.MarketWatchService2Exception getFaultMessage(){
       return faultMessage;
    }
}
    