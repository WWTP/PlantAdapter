package plantadapter.utils;


public class LinearScalingTransformation implements ScalingTransformation {
	
	public static LinearScalingTransformation IDENTITY = new LinearScalingTransformation(1.0, 0.0);
	
	private double factor, offset;
	
	public LinearScalingTransformation(double factor, double offset) {
		this.factor = factor;
		this.offset = offset;
	}

	@Override
	public double scale(double value) {
		return value * factor + offset;
	}

	@Override
	public ScalingTransformation invert() {
		return new ScalingTransformation() {

			@Override
			public double scale(double value) {
				return (value - offset) / factor;
			}

			@Override
			public ScalingTransformation invert() {
				return new LinearScalingTransformation(factor, offset);
			}
		};
	}
	
	// Object
	
	@Override
	public String toString() {
		return "y = " + this.factor + " * x " + (this.offset > 0 ? "+ " : "") + this.offset;
	}
}