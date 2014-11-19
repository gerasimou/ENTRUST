//This file was generated from (Academic) UPPAAL 4.1.14 (rev. 5212), March 2013

/*

*/
Executor.ChangeSpeed and newSpeed != uuvSpeed --> UUV.SpeedChanged && uuvSpeed == newSpeed

/*

*/
Executor.OffSensor and sensorId == 2 --> Sensor(2).Off

/*

*/
Executor.OffSensor and sensorId == 3 --> Sensor(3).Off

/*

*/
Executor.OffSensor and sensorId == 2 --> Sensor(2).On

/*

*/
Executor.OffSensor and sensorId == 3 --> Sensor(3).On

/*

*/
Executor.OnSensor and sensorId == 1 --> Sensor(1).On

/*

*/
Executor.OffSensor and sensorId == 1 --> Sensor(1).Off

/*

*/
A[]Analyzer.NoAdaptationNeeded imply currentConfiguration == bestConfiguration

/*

*/
A[]Executor.PlanExecuted imply currentConfiguration.req2Result < 120

/*

*/
A[] currentConfiguration.req2Result < 120

/*

*/
A[] currentConfiguration.req1Result > 20

/*

*/
A[]Executor.PlanExecuted imply currentConfiguration.req1Result > 20

/*

*/
Planner.PlanCreated --> Executor.PlanExecuted

/*

*/
Analyzer.AdaptationNeeded-->Executor.PlanExecuted

/*

*/
A[] not deadlock
