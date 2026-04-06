package com.asdd.template.api.tasks;

import com.asdd.template.api.model.Pet;
import com.asdd.template.api.util.ApiConstants;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

/**
 * Task to create a new Pet via the PetStore API.
 */
public class CreatePet implements Task {

    private final Pet pet;

    public CreatePet(Pet pet) {
        this.pet = pet;
    }

    public static CreatePet with(Pet pet) {
        return new CreatePet(pet);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Post.to(ApiConstants.PET_ENDPOINT)
                .with(requestSpecification -> requestSpecification
                    .body(pet)
                    .contentType(ContentType.JSON))
        );
    }
}
