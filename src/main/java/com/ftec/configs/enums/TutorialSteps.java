package com.ftec.configs.enums;

import com.ftec.entities.User;

public enum TutorialSteps {
	FIRST,SECOND,THIRD;
    private static TutorialSteps[] vals = values();

	public static void setNextStep(User u) {
		u.setStep(vals[(u.getStep().ordinal()+1) % vals.length]);
	}
	public static TutorialSteps getNextStep(TutorialSteps currentStep) {
		return vals[(currentStep.ordinal()+1) % vals.length];
	}
}
