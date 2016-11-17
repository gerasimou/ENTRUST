Applying ENTRUST to a business-critical service-based system
=======
 An FX customer (called a trader) can use the system in two operation modes. In the *expert*  mode, FX executes a loop that analyses market activity, identifies patterns that satisfy the trader's objectives, and automatically carries out trades. Thus, the *Market watch* service extracts real-time exchange rates (bid/ask price) of selected currency pairs. This data is used by a *Technical analysis* service that evaluates the current trading conditions, predicts future price movement, and decides if the trader's objectives are: (i) "satisfied" (causing the invocation of an *Order* service to carry out a trade); (ii) "unsatisfied" (resulting in a new *Market watch* invocation); or (iii) "unsatisfied with high variance" (triggering an *Alarm* service invocation to notify the trader about discrepancies/opportunities not covered by the trading objectives). In the *normal* mode, FX assesses the economic outlook of a country using a *Fundamental analysis* service that collects, analyses and evaluates information such as news reports, economic data and political events, and provides an assessment on the country's currency. If satisfied with this assessment, the trader can use the *Order* service to sell or buy currency, in which case a *Notification* service confirms the completion of the trade.

Multiple service-providers could support concrete implementations that provide the functionality required by the *n=6* services in the FX system. Each  service comprises an interface with which it can be invoked at runtime as well as a service-level-agreement (SLA), a  contract that defines various QoS characteristics, e.g., reliability, performance (response time), and price, that both parties (service provider and FX system, as service consumer) are obliged to comply with.

The FX system should dynamically select third-party implementations for each of the *n &#8805; 1* services for which in-house implementations are not available, in order to meet the following QoS requirements:


| ID        | Description
| ------------- |:-------------|
| **R1**   | **(reliability)**: Workflow executions must complete successfully with probability at least 0.9|
| **R2**   | **(response time)**: The total response time per workflow execution should be at maximum 5s|
| **R3**   | **(cost)**: If requirements R1 and R2 are satisfied by multiple configurations, the system should use one of these configurations that minimises both price and response time.|
| **R4**   | **(failsafe)**: If a configuration that meets requirements R1 - R3 cannot be identified within 2 seconds after a change in service characteristics is signalled by the sensors of the self-adaptive FX system, the Order service invocation is bypassed, so that FX cannot make any order using obsolete data (e.g., old exchange rates).|  

For additional information, please see the [ENTRUST webpage](http://www-users.cs.york.ac.uk/~simos/ENTRUST/#FX)
***

**This is a Maven Eclipse project**. Simply import the project into Eclipse and run main.MainENTRUST

--


Should you have any comments, suggestions or questions, please email us at simos-at-cs.york.ac.uk
