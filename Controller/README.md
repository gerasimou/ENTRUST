ENTRUST Generic Controller
=======


<p align="center">
  <img src="http://www-users.cs.york.ac.uk/~simos/ENTRUST/images/ENTRUST/entrustInstance.svg">
  <br />
  Architecture of an ENTRUST self-adaptive system.
  </p>
<br />

An ENTRUST controller implements the monitor-analyse- plan-execute (MAPE) steps of the [autonomic computing loop] (http://ieeexplore.ieee.org/xpl/articleDetails.jsp?arnumber=1160055) using verifiable models, i.e., a network of interacting timed automata. These executable models are assessed for their correctness at design-time verifying a set of controller properties and executed at runtime by a [reusable virtual machine](https://people.cs.kuleuven.be/~danny.weyns/software/ActivFORMS/).

The ENTRUST controller executes the monitor model to obtain information related to the managed system and its environment through sensors. This information is used to instantiate parametric stochastic models capturing the behaviour of the managed system and its environment. These models along with the system goals (formalised in probabilistic temporal logic) are kept in a knowledge repository. When a change occurs, the analyser model uses a runtime probabilistic verification engine to verify the system compliance with its goals. If this does not hold, the analyser determines a new configuration that restores goal compliance. A stepwise reconfiguration plan is assembled by executing a planner model, and this plan is implemented by the executor model through effectors.

###ENTRUST System Development Stages
1. Development of Verifiable Models (controller and parametric stochastic models)
2. Verification of Controller Models
3. Partial Instantiation of Assurance Argument Pattern
4. Enactment of the Controller
5. Deployment of the Self-Adaptive System
6. Self-adaptation
7. Synthesis of Dynamic Assurance Argument

For additional information regarding the ENTRUST stages, please see the [ENTRUST webpage] (http://www-users.cs.york.ac.uk/~simos/ENTRUST).


--

Should you have any comments, suggestions or questions, please email us at simos-at-cs.york.ac.uk
