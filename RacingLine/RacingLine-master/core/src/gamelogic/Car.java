package gamelogic;

public class Car {
	
	public static final int STRAIGHT = 0;
	public static final int TURN_LEFT = 1;
	public static final int TURN_RIGHT = 2;
	public static final int NO_POWER = 0;
	public static final int ACCELERATE = 1;
	public static final int BRAKE = 2;
	
	//Control layer
	private int turning;
	private int acceleration;
	private int grassWheels;
	
	//Physics
	private float speed;
	private float angle;
	private float xPos;
	private float yPos;
	private float turnAngle;
	private float rearX;
	private float rearY;
	private float frontX;
	private float frontY;
	private float tempTurn;
	private float understeer;
	private float drag;
	
	public Car(float xInit, float yInit) {
		speed = 0;
		angle = 0;
		xPos = xInit;
		yPos = yInit;
		
		turning = 0;
		acceleration = 0;
	}
	
	public void turn(int direction) {
		turning = direction;
	}
	
	public void accelerate(int direction) {
		acceleration = direction;
	}
	
	public void grass(int num) {
		grassWheels = num;
	}
	
	public void move(float delta) {
		understeer = 0;
		drag = Constants.DRAG;
		
		
		//Acceleration
		drag += Constants.GRASS_SLOW*grassWheels;
		understeer += Constants.GRASS_TURN_REDUCTION*grassWheels;
		
		if (drag > 1) {
			drag = 1;
		}
		
		if (acceleration == ACCELERATE) {
			speed += (Constants.POWER) * delta;
			understeer += Constants.TURN_REDUCTION_ACCEL;
		}
		else if (acceleration == BRAKE) {
			speed += (Constants.BRAKES) * delta;
			understeer += Constants.TURN_REDUCTION_BRAKE;
		}
		
		int speedDir = (int) (speed/Math.abs(speed));
		speed -= (speedDir*(Math.abs(speed*drag) + Constants.FRICTION))*delta;
		//The car has crossed the 0 speed line, set the speed to 0
		if (speedDir+(speed/Math.abs(speed))==0) {
			speed = 0;
		}
		
		if (speed < Constants.TOP_SPEED_REVERSE) {
			speed = Constants.TOP_SPEED_REVERSE;
		}
		
		//Turning
		tempTurn = Constants.MAX_TURN - Constants.MIN_TURN;
		understeer += (speed/Constants.TOP_TURN_SPEED)*Constants.TURN_REDUCTION_SPEED;
		if (understeer>1) {
			understeer = 1;
		}
		tempTurn *= (1-understeer);
		tempTurn += Constants.MIN_TURN;
		
		if (turning == TURN_LEFT) {
			if (turnAngle < 0){
				turnAngle += Constants.TURN_SPEED*delta;
			}
			turnAngle += Constants.TURN_SPEED*delta;
		}
		else if (turning == TURN_RIGHT) {
			if (turnAngle > 0) {
				turnAngle -= Constants.TURN_SPEED*delta;
			}
			turnAngle -= Constants.TURN_SPEED*delta;
		}
		else {
			int turnDir = (int) (turnAngle/Math.abs(turnAngle));
			turnAngle -= (turnDir*Constants.TURN_SPEED)*delta;
			//The car has crossed the 0 angle line, set the angle to 0
			if (turnDir+(turnAngle/Math.abs(turnAngle))==0) {
				turnAngle = 0;
			}
		}
		
		if (turnAngle < -tempTurn) {
			turnAngle = -tempTurn;
		}
		else if (turnAngle > tempTurn) {
			turnAngle = tempTurn;
		}
		
		//Speed
		rearX = (float) (xPos - (Constants.WHEELBASE/2) * Math.cos(angle));
		rearY = (float) (yPos - (Constants.WHEELBASE/2) * Math.sin(angle));
		frontX = (float) (xPos + (Constants.WHEELBASE/2) * Math.cos(angle));
		frontY = (float) (yPos + (Constants.WHEELBASE/2) * Math.sin(angle));
		
		rearX += (speed * Math.cos(angle))*delta;
		rearY += (speed * Math.sin(angle))*delta;
		
		frontX += (speed * Math.cos(angle+turnAngle))*delta;
		frontY += (speed * Math.sin(angle+turnAngle))*delta;
	
		xPos = (float) ((rearX+frontX)/2.0);
		yPos = (float) ((rearY+frontY)/2.0);
		
		angle = (float) Math.atan2(frontY-rearY, frontX-rearX);
		
		//TEMP
		if (xPos < 0) {
			xPos = 1600;
		}
		if (xPos > 1600) {
			xPos = 0;
		}
		if (yPos < 0) {
			yPos = 900;
		}
		if (yPos > 900) {
			yPos = 0;
		}
	}
	
	public float getX() {
		return xPos;
	}
	
	public float getY() {
		return yPos;
	}
	
	public float getAngle() {
		return angle;
	}
	
	public float getTurnAngle() {
		return turnAngle;
	}
	
	public float getSpeed(){
		return speed;
	}
	
}
