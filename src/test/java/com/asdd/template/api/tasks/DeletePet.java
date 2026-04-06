package com.asdd.template.api.tasks;

import com.asdd.template.api.util.ApiConstants;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Delete;

/**
 * Task to delete a Pet by its ID from the PetStore API.
 */
public class DeletePet implements Task {

    private final long petId;

    public DeletePet(long petId) {
        this.petId = petId;
    }

    public static DeletePet withId(long petId) {
        return new DeletePet(petId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Delete.from(ApiConstants.PET_BY_ID_ENDPOINT)
                  .with(request -> request.pathParam("id", petId))
        );
    }
}
