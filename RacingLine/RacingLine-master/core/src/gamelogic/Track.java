package gamelogic;

import java.awt.geom.Line2D;

public class Track {
	
	public static final int NO_CHECK = 0;
	public static final int CHECK = 1;
	public static final int LAP = 2;
	public static final int CUT = 3;
	
	private Line2D[] checkpoints;
	private boolean[] checkpointsReached;
	
	public Track(Line2D[] checkpoints) {
		this.checkpoints = new Line2D[checkpoints.length];
		this.checkpointsReached = new boolean[checkpoints.length];
		this.checkpoints = checkpoints;
	}
	
	public int carMove(Line2D movement) {
		if (movement.intersectsLine(checkpoints[0])) {
			if (checkpointsReached[1] && checkpointsReached[checkpoints.length-1]) { //only a valid lap if the last checkpoint was reached and at least the first checkpoint was reached
				checkpointsReached = new boolean[checkpoints.length];
				checkpointsReached[0] = true;
				return LAP;
			}
			else {
				return NO_CHECK;
			}
		}
		
		for (int i = 1; i < checkpoints.length; i++) {
			if (movement.intersectsLine(checkpoints[i])) {
				if (checkpointsReached[i-1]) {
					checkpointsReached[i] = true;
					return CHECK;
				}
				else {
					checkpointsReached[i] = true;
					return CUT;
				}
			}
		}
		return NO_CHECK;
	}
}
