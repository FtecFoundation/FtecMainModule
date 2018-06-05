package com.ftec.resources.enums;

public enum TutorialSteps {

    FIRST, SECOND, THIRD;

    static {
        FIRST.nextStep=SECOND;
        SECOND.nextStep=THIRD;
        THIRD.nextStep=null;
    }

	TutorialSteps nextStep;

    TutorialSteps() {
    }

    TutorialSteps(TutorialSteps nextStep) {
		this.nextStep = nextStep;
	}

	public TutorialSteps getNextStep(){
		return nextStep;
	}

}
