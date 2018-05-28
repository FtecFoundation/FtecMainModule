package com.ftec.configs.enums;

import com.ftec.entities.User;

public enum TutorialSteps {
	FIRST,SECOND,THIRD;
    private static TutorialSteps[] vals = values();

	public static void setNextStep(User u) {
		u.setStep(vals[(u.getStep().ordinal()+1) % vals.length]);
	}
	public static TutorialSteps getNextStep(TutorialSteps currentStep) {
		if(currentStep.ordinal() + 1 == vals.length) {
			System.out.println("NULL RETURNED");
			return null;
		}
		System.out.println(currentStep + "   currentStep ordinal is = " + currentStep.ordinal());
		System.out.println("vals size is  " + vals.length);
		return vals[(currentStep.ordinal()+1) % vals.length];
	}
}
