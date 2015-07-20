
/**
 * NotificationService1ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package fx.services.NOT;

public class NotificationService1ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1436366366814L;
    
    private fx.services.NOT.NotificationService1Stub.NotificationService1Exception faultMessage;

    
        public NotificationService1ExceptionException() {
            super("NotificationService1ExceptionException");
        }

        public NotificationService1ExceptionException(java.lang.String s) {
           super(s);
        }

        public NotificationService1ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public NotificationService1ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(fx.services.NOT.NotificationService1Stub.NotificationService1Exception msg){
       faultMessage = msg;
    }
    
    public fx.services.NOT.NotificationService1Stub.NotificationService1Exception getFaultMessage(){
       return faultMessage;
    }
}
    