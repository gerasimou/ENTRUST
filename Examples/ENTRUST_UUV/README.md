Applying ENTRUST to a mission-critical UUV embedded system
=======
 A UUV equipped with n ≥ 1 on-board sensors (that can measure the same characteristic of the ocean environment, e.g., water current or salinity) is deployed to carry out a data gathering mission. When used, the sensors take measurements with different, variable rates  <i> r<sub>1</sub>, r<sub>2</sub>, . . . , r<sub>n</sub></i>. The probability that each sensor produces measurements that are sufficiently accurate for the purpose of the mission depends on the UUV speed <i>sp</i>, given by <i>p<sub>1</sub>, p<sub>2</sub>, . . . , p<sub>n</sub></i>. For each measurement taken, different amount of energy is consumed, given by <i>e<sub>1</sub>, e<sub>2</sub>, . . . , e<sub>n</sub></i>. Finally, the <i>n</i> sensors can be switched on and off individually (e.g., to save battery power when not required), but these operations consume an amount of energy given by <i>e<sub>1</sub><sup>on</sup>, e<sub>2</sub><sup>on</sup>, ..., e<sub>n</sub><sup>on</sup></i>, and <i>e<sub>1</sub><sup>off</sup>, e<sub>2</sub> <sup>off</sup>, ..., e<sub>n</sub><sup>off</sup></i>, respectively.˜

 The UUV is required to self-adapt to changes in the observed sensor measurement rates <i>r<sub>i</sub>, 1 ≤ i ≤ n</i>, and to sensor failures by dynamically adjusting:
 * the UUV speed <i>sp</i>
 * the sensor configuration <i>x<sub>1</sub>, x<sub>2</sub>, . . . , x<sub>n</sub></i> (where x<sub>i</sub> = 1 if the i-th sensor is on and x<sub>i</sub> = 0 otherwise)

so that the following QoS requirements are satisfied at all times:

| ID        | Description
| ------------- |:-------------|
| **R1**   | **(throughput)**: The UUV should take at least 20 measurements of sufficient accuracy for every 10 metres of mission distance|
| **R2**   | **(resource usage)**: The energy consumption of the sensors should not exceed 120 Joules per 10 surveyed metres|
| **R3**   | **(cost)**: If requirements R1 and R2 are satisfied by multiple configurations, the UUV should use one of these configurations that minimises energy consumption and maximises UUV speed.|
| **R4**   | **(failsafe)**: If a configuration that meets requirements R1 - R3 is not identified within 2 seconds after a sensor rate change, the UUV speed must be reduced to 0m/s, so that the UUV does not advance until its sensors recover or new instructions are provided by a human operator.

For additional information, please see the [ENTRUST webpage](http://www-users.cs.york.ac.uk/~simos/ENTRUST/#uuv)
***

**This is a Maven Eclipse project**. Simply import the project into Eclipse and run main.MainENTRUST.

--


Should you have any comments, suggestions or questions, please email us at simos-at-cs.york.ac.uk
