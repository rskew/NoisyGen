package com.rowanskewes.filters;

import java.lang.Math;

/**
 * Simple low pass forgetful integrator
 * @author Rowan Skewes <rowan.skewes@gmail.com>
 */
public class Lpf implements Filter {

	// Memory of filter
	private float state;

	// Filter response ('memory')
	// High values <-> longer memory <-> larger averaging window <-> lower cutoff
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
