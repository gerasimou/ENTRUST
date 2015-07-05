
/**
 * WatchMarketService1ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package soar.ws.fx.services.WM;

public class WatchMarketService1ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1436047702009L;
    
    private soar.ws.fx.services.WM.WatchMarketService1Stub.WatchMarketService1Exception faultMessage;

    
        public WatchMarketService1ExceptionException() {
            super("WatchMarketService1ExceptionException");
        }

        public WatchMarketService1ExceptionException(java.lang.String s) {
           super(s);
        }

        public WatchMarketService1ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public WatchMarketService1ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(soar.ws.fx.services.WM.WatchMarketService1Stub.WatchMarketService1Exception msg){
       faultMessage = msg;
    }
    
    public soar.ws.fx.services.WM.WatchMarketService1Stub.WatchMarketService1Exception getFaultMessage(){
       return faultMessage;
    }
}
    