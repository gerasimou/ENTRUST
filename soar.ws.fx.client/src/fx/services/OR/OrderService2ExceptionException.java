
/**
 * OrderService2ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package fx.services.OR;

public class OrderService2ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1436366277746L;
    
    private fx.services.OR.OrderService2Stub.OrderService2Exception faultMessage;

    
        public OrderService2ExceptionException() {
            super("OrderService2ExceptionException");
        }

        public OrderService2ExceptionException(java.lang.String s) {
           super(s);
        }

        public OrderService2ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public OrderService2ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(fx.services.OR.OrderService2Stub.OrderService2Exception msg){
       faultMessage = msg;
    }
    
    public fx.services.OR.OrderService2Stub.OrderService2Exception getFaultMessage(){
       return faultMessage;
    }
}
    