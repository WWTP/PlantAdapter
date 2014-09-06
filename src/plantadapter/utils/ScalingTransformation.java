package plantadapter.utils;


public interface ScalingTransformation extends Transformation {

	public double scale(double value);
	
	public ScalingTransformation invert();
}