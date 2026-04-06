package com.asdd.template.api.tasks;

import com.asdd.template.api.util.ApiConstants;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

/**
 * Task to retrieve a Pet by its ID from the PetStore API.
 */
public class GetPet implements Task {

    private final long petId;

    public GetPet(long petId) {
        this.petId = petId;
    }

    public static GetPet withId(long petId) {
        return new GetPet(petId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Get.resource(ApiConstants.PET_BY_ID_ENDPOINT)
               .with(request -> request.pathParam("id", petId))
        );
    }
}
