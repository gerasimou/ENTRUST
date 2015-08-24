
/**
 * OrderService3ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package fx.services.OR;

public class OrderService3ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1440379554570L;
    
    private fx.services.OR.OrderService3Stub.OrderService3Exception faultMessage;

    
        public OrderService3ExceptionException() {
            super("OrderService3ExceptionException");
        }

        public OrderService3ExceptionException(java.lang.String s) {
           super(s);
        }

        public OrderService3ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public OrderService3ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(fx.services.OR.OrderService3Stub.OrderService3Exception msg){
       faultMessage = msg;
    }
    
    public fx.services.OR.OrderService3Stub.OrderService3Exception getFaultMessage(){
       return faultMessage;
    }
}
    