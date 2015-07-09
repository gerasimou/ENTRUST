
/**
 * MarketWatchService1ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package soar.ws.fx.services.MW;

public class MarketWatchService1ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1436282479418L;
    
    private soar.ws.fx.services.MW.MarketWatchService1Stub.MarketWatchService1Exception faultMessage;

    
        public MarketWatchService1ExceptionException() {
            super("MarketWatchService1ExceptionException");
        }

        public MarketWatchService1ExceptionException(java.lang.String s) {
           super(s);
        }

        public MarketWatchService1ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public MarketWatchService1ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(soar.ws.fx.services.MW.MarketWatchService1Stub.MarketWatchService1Exception msg){
       faultMessage = msg;
    }
    
    public soar.ws.fx.services.MW.MarketWatchService1Stub.MarketWatchService1Exception getFaultMessage(){
       return faultMessage;
    }
}
    