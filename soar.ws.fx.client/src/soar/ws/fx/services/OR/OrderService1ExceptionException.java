
/**
 * OrderService1ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package soar.ws.fx.services.OR;

public class OrderService1ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1436366262452L;
    
    private soar.ws.fx.services.OR.OrderService1Stub.OrderService1Exception faultMessage;

    
        public OrderService1ExceptionException() {
            super("OrderService1ExceptionException");
        }

        public OrderService1ExceptionException(java.lang.String s) {
           super(s);
        }

        public OrderService1ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public OrderService1ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(soar.ws.fx.services.OR.OrderService1Stub.OrderService1Exception msg){
       faultMessage = msg;
    }
    
    public soar.ws.fx.services.OR.OrderService1Stub.OrderService1Exception getFaultMessage(){
       return faultMessage;
    }
}
    