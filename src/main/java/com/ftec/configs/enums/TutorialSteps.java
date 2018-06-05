package com.ftec.configs.enums;

public enum TutorialSteps {
	
	FIRST(TutorialSteps.SECOND),SECOND(TutorialSteps.THIRD),THIRD(null);

	TutorialSteps nextStep;

	TutorialSteps(TutorialSteps nextStep) {
		this.nextStep = nextStep;
	}

	public TutorialSteps getNextStep(){
		return nextStep;
	}

}
