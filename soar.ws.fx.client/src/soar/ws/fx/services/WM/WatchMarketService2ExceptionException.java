
/**
 * WatchMarketService2ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package soar.ws.fx.services.WM;

public class WatchMarketService2ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1436047820180L;
    
    private soar.ws.fx.services.WM.WatchMarketService2Stub.WatchMarketService2Exception faultMessage;

    
        public WatchMarketService2ExceptionException() {
            super("WatchMarketService2ExceptionException");
        }

        public WatchMarketService2ExceptionException(java.lang.String s) {
           super(s);
        }

        public WatchMarketService2ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public WatchMarketService2ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(soar.ws.fx.services.WM.WatchMarketService2Stub.WatchMarketService2Exception msg){
       faultMessage = msg;
    }
    
    public soar.ws.fx.services.WM.WatchMarketService2Stub.WatchMarketService2Exception getFaultMessage(){
       return faultMessage;
    }
}
    