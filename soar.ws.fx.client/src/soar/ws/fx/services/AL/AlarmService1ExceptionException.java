
/**
 * AlarmService1ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package soar.ws.fx.services.AL;

public class AlarmService1ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1436366300052L;
    
    private soar.ws.fx.services.AL.AlarmService1Stub.AlarmService1Exception faultMessage;

    
        public AlarmService1ExceptionException() {
            super("AlarmService1ExceptionException");
        }

        public AlarmService1ExceptionException(java.lang.String s) {
           super(s);
        }

        public AlarmService1ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public AlarmService1ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(soar.ws.fx.services.AL.AlarmService1Stub.AlarmService1Exception msg){
       faultMessage = msg;
    }
    
    public soar.ws.fx.services.AL.AlarmService1Stub.AlarmService1Exception getFaultMessage(){
       return faultMessage;
    }
}
    