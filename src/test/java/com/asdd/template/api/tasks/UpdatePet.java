package com.asdd.template.api.tasks;

import com.asdd.template.api.model.Pet;
import com.asdd.template.api.util.ApiConstants;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Put;

/**
 * Task to update an existing Pet via the PetStore API.
 */
public class UpdatePet implements Task {

    private final Pet pet;

    public UpdatePet(Pet pet) {
        this.pet = pet;
    }

    public static UpdatePet with(Pet pet) {
        return new UpdatePet(pet);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Put.to(ApiConstants.PET_ENDPOINT)
               .with(requestSpecification -> requestSpecification
                   .body(pet)
                   .contentType(ContentType.JSON))
        );
    }
}
