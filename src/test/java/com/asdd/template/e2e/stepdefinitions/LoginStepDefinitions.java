package com.asdd.template.e2e.stepdefinitions;

import com.asdd.template.e2e.pages.DashboardPage;
import com.asdd.template.e2e.questions.CurrentUrl;
import com.asdd.template.e2e.tasks.Login;
import com.asdd.template.e2e.tasks.NavigateTo;
import com.asdd.template.e2e.util.E2eConstants;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.questions.Text;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Cucumber step definitions for the Login E2E feature.
 */
public class LoginStepDefinitions {

    private String enteredUsername;
    private String enteredPassword;

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        Actor user = theActorCalled("User");
        user.attemptsTo(
            NavigateTo.thePageAt(E2eConstants.BASE_URL + E2eConstants.LOGIN_PATH)
        );
    }

    @When("I enter username {string} and password {string}")
    public void iEnterUsernameAndPassword(String username, String password) {
        this.enteredUsername = username;
        this.enteredPassword = password;
    }

    @And("I click the login button")
    public void iClickTheLoginButton() {
        Actor user = theActorInTheSpotlight();
        user.attemptsTo(
            Login.withCredentials(enteredUsername, enteredPassword)
        );
    }

    @Then("I should be redirected to the secure area")
    public void iShouldBeRedirectedToTheSecureArea() {
        Actor user = theActorInTheSpotlight();
        String currentUrl = user.asksFor(CurrentUrl.ofTheBrowser());
        assertThat(currentUrl)
            .as("Expected to be on secure area page")
            .contains("/secure");
    }

    @Then("I should see an error message")
    public void iShouldSeeAnErrorMessage() {
        Actor user = theActorInTheSpotlight();
        String flashMessage = user.asksFor(Text.of(DashboardPage.FLASH_MESSAGE));
        assertThat(flashMessage)
            .as("Expected an error flash message")
            .isNotBlank();
    }
}
