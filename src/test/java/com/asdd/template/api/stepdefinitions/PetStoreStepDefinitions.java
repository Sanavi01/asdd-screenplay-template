package com.asdd.template.api.stepdefinitions;

import com.asdd.template.api.model.Pet;
import com.asdd.template.api.questions.ResponseStatusCode;
import com.asdd.template.api.tasks.CreatePet;
import com.asdd.template.api.tasks.DeletePet;
import com.asdd.template.api.tasks.GetPet;
import com.asdd.template.api.tasks.UpdatePet;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;

import java.util.List;

import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Cucumber step definitions for the PetStore API feature.
 */
public class PetStoreStepDefinitions {

    private Pet currentPet;

    @Given("the API client is configured")
    public void theApiClientIsConfigured() {
        // Actor and ability are set up in ApiHooks
        theActorCalled("API User");
    }

    @When("I create a pet with name {string} and status {string}")
    public void iCreateAPetWithNameAndStatus(String name, String status) {
        Actor actor = theActorInTheSpotlight();
        currentPet = new Pet()
            .setName(name)
            .setStatus(status)
            .setPhotoUrls(List.of("https://example.com/photo.jpg"));

        actor.attemptsTo(CreatePet.with(currentPet));

        // Capture the generated ID from the response
        Long responseId = SerenityRest.lastResponse().body().jsonPath().getLong("id");
        if (responseId != null) {
            currentPet.setId(responseId);
        }
    }

    @Given("I have created a pet with name {string} and status {string}")
    public void iHaveCreatedAPetWithNameAndStatus(String name, String status) {
        iCreateAPetWithNameAndStatus(name, status);
    }

    @When("I retrieve the pet by id")
    public void iRetrieveThePetById() {
        Actor actor = theActorInTheSpotlight();
        actor.attemptsTo(GetPet.withId(currentPet.getId()));
    }

    @When("I update the pet status to {string}")
    public void iUpdateThePetStatusTo(String newStatus) {
        Actor actor = theActorInTheSpotlight();
        currentPet.setStatus(newStatus);
        actor.attemptsTo(UpdatePet.with(currentPet));
    }

    @When("I delete the pet by id")
    public void iDeleteThePetById() {
        Actor actor = theActorInTheSpotlight();
        actor.attemptsTo(DeletePet.withId(currentPet.getId()));
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        Actor actor = theActorInTheSpotlight();
        Integer actualStatusCode = actor.asksFor(ResponseStatusCode.ofTheLastResponse());
        assertThat(actualStatusCode)
            .as("Expected response status code to be %d but got %d", expectedStatusCode, actualStatusCode)
            .isEqualTo(expectedStatusCode);
    }
}
