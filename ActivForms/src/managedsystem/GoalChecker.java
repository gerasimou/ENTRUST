package managedsystem;

import activforms.goalmanagement.Goal;
import activforms.goalmanagement.GoalClient;

public class GoalChecker implements GoalClient {

    @Override
    public void GoalStatusChanged(Goal goal) {
		System.out.println("Goal violated!");
		System.err.println(goal);
    }


}
