
/**
 * AlarmService3ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package fx.services.AL;

public class AlarmService3ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1440379503937L;
    
    private fx.services.AL.AlarmService3Stub.AlarmService3Exception faultMessage;

    
        public AlarmService3ExceptionException() {
            super("AlarmService3ExceptionException");
        }

        public AlarmService3ExceptionException(java.lang.String s) {
           super(s);
        }

        public AlarmService3ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public AlarmService3ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(fx.services.AL.AlarmService3Stub.AlarmService3Exception msg){
       faultMessage = msg;
    }
    
    public fx.services.AL.AlarmService3Stub.AlarmService3Exception getFaultMessage(){
       return faultMessage;
    }
}
    