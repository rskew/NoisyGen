package com.rowanskewes.filters;

import java.lang.Math;

public class Lpf implements Filter {
	/* Rowan Skewes
	 * Low Pass Filter object for Command Line Filter.class
	 * Simple low pass forgetful integrator,
	 * Geometric impulse response
	 */

	// Memory of filter
	private float state;

	// Filter response ('forgetfulness')
	// High values <-> less forgetfulness, lower cutoff, more filtering
	private final float response;

	// Constant to normalise volume for different cutoffs
	private final float gain;

	public Lpf(float state, float response){
		this.state = state;
		this.response = response;
		this.gain = - (float)Math.log((double)response);
	}

	public float processSample(float inputSample){
		// Calculate new state
		state = gain * inputSample +
			response * state;

		// Limit output (saturation)
		if(state > 128) state = 128;
		if(state < -128) state = -128;

		return state;
	}
}
