
/**
 * NotificationService2ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package fx.services.NOT;

public class NotificationService2ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1436366350376L;
    
    private fx.services.NOT.NotificationService2Stub.NotificationService2Exception faultMessage;

    
        public NotificationService2ExceptionException() {
            super("NotificationService2ExceptionException");
        }

        public NotificationService2ExceptionException(java.lang.String s) {
           super(s);
        }

        public NotificationService2ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public NotificationService2ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(fx.services.NOT.NotificationService2Stub.NotificationService2Exception msg){
       faultMessage = msg;
    }
    
    public fx.services.NOT.NotificationService2Stub.NotificationService2Exception getFaultMessage(){
       return faultMessage;
    }
}
    