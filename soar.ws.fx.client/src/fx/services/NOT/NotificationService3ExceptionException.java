
/**
 * NotificationService3ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package fx.services.NOT;

public class NotificationService3ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1440379594496L;
    
    private fx.services.NOT.NotificationService3Stub.NotificationService3Exception faultMessage;

    
        public NotificationService3ExceptionException() {
            super("NotificationService3ExceptionException");
        }

        public NotificationService3ExceptionException(java.lang.String s) {
           super(s);
        }

        public NotificationService3ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public NotificationService3ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(fx.services.NOT.NotificationService3Stub.NotificationService3Exception msg){
       faultMessage = msg;
    }
    
    public fx.services.NOT.NotificationService3Stub.NotificationService3Exception getFaultMessage(){
       return faultMessage;
    }
}
    