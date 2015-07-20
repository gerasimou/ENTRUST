
/**
 * MarketWatchService1CallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package fx.services.MW;

    /**
     *  MarketWatchService1CallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class MarketWatchService1CallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public MarketWatchService1CallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public MarketWatchService1CallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getNominalReliability method
            * override this method for handling normal response from getNominalReliability operation
            */
           public void receiveResultgetNominalReliability(
                    fx.services.MW.MarketWatchService1Stub.GetNominalReliabilityResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getNominalReliability operation
           */
            public void receiveErrorgetNominalReliability(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for run method
            * override this method for handling normal response from run operation
            */
           public void receiveResultrun(
                    fx.services.MW.MarketWatchService1Stub.RunResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from run operation
           */
            public void receiveErrorrun(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                


    }
    