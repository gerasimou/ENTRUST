
/**
 * AlarmService2ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package soar.ws.fx.services.AL;

public class AlarmService2ExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1436366315671L;
    
    private soar.ws.fx.services.AL.AlarmService2Stub.AlarmService2Exception faultMessage;

    
        public AlarmService2ExceptionException() {
            super("AlarmService2ExceptionException");
        }

        public AlarmService2ExceptionException(java.lang.String s) {
           super(s);
        }

        public AlarmService2ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public AlarmService2ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(soar.ws.fx.services.AL.AlarmService2Stub.AlarmService2Exception msg){
       faultMessage = msg;
    }
    
    public soar.ws.fx.services.AL.AlarmService2Stub.AlarmService2Exception getFaultMessage(){
       return faultMessage;
    }
}
    