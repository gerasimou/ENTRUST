
/**
 * MarketWatchService3ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package fx.services.MW;

public class MarketWatchService3ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1440378689416L;
    
    private fx.services.MW.MarketWatchService3Stub.MarketWatchService3Exception faultMessage;

    
        public MarketWatchService3ExceptionException() {
            super("MarketWatchService3ExceptionException");
        }

        public MarketWatchService3ExceptionException(java.lang.String s) {
           super(s);
        }

        public MarketWatchService3ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public MarketWatchService3ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(fx.services.MW.MarketWatchService3Stub.MarketWatchService3Exception msg){
       faultMessage = msg;
    }
    
    public fx.services.MW.MarketWatchService3Stub.MarketWatchService3Exception getFaultMessage(){
       return faultMessage;
    }
}
    