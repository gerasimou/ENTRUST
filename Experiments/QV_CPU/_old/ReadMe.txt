#README

This is the old set of experiments for measuring the CPU time taken by QV for the UUV and FX systems.

These measurements make the following assumptions:
(1) each event is considered individually. Thus, for each event the JVM had to load itself (which takes time)
    and  also to initialise and run a new ENTRUST instance;
(2) the CPU times measures the total time required by the ENTRUST controller to reconfigure the system
     (i.e., Sensor --> Monitor --> ... --> Effector).

Hence, the times shown are not representative of the time taken by QV only. For this check the other folder.
