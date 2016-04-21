package gamelogic;

public class Constants {
	public static final float POWER = 350.0f;
	public static final float BRAKES = -450.0f;
	public static final float DRAG = 0.4f;
	public static final float FRICTION = 25f;
	public static final float TOP_SPEED_REVERSE = -100;
	public static final float MAX_TURN = (float) (Math.PI/6);
	public static final float MIN_TURN = (float) (Math.PI/30);
	public static final float TURN_SPEED = (float) (Math.PI*4);
	public static final float WHEELBASE = 40;
	public static final float TURN_REDUCTION_SPEED = 0.9f;
	public static final float TURN_REDUCTION_ACCEL = 0.1f;
	public static final float TURN_REDUCTION_BRAKE = 0.8f;
	public static final float TOP_TURN_SPEED = 400; //At this speed turn reduction is at full
	
	public static final int GRASS_UPPER = 10;
	public static final int GRASS_LOWER = 0;
	public static final float GRASS_SLOW = 0.5f;
	public static final float GRASS_TURN_REDUCTION = 0.2f;
}
