package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.CommonMethods;
import utils.Constants;

import java.util.*;

public class HackerRankSteps extends CommonMethods {
    public static RequestSpecification debitRequestForPage;
    public static RequestSpecification creditRequestForPage;

    public static Response debitResponseForPage;
    public static Response creditResponseForPage;

    Map<Integer, Integer> debitTotalMap;
    Map<Integer, Integer> creditTotalMap;

    @Given("a request is prepared to collect debit and credit amount of bank customers transactions")
    public void aRequestIsPreparedToCollectDebitAndCreditAmountOfBankCustomersTransactions() {
        debitRequestForPage = getDebitOrCreditRequest(Constants.DEBIT, Constants.GET_INFO_PAGE);
        creditRequestForPage = getDebitOrCreditRequest(Constants.CREDIT, Constants.GET_INFO_PAGE);
    }

    @When("a GET call is made to collect debit and credit amount of bank customers transactions")
    public void aGETCallIsMadeToCollectDebitAndCreditAmountOfBankCustomersTransactions() {
        debitResponseForPage = getDebitOrCreditResponse(debitRequestForPage);
        creditResponseForPage = getDebitOrCreditResponse(creditRequestForPage);
    }

    @And("the total debit and credit transactions collected from bank customers transactions")
    public void theTotalDebitAndCreditTransactionsCollectedFromBankCustomersTransactions() {
        debitTotalMap = totalDebitOrCreditTransactions(Constants.DEBIT, debitResponseForPage);
        creditTotalMap = totalDebitOrCreditTransactions(Constants.CREDIT, creditResponseForPage);
    }

    @Then("the result of bank customer total transaction summary displayed in the console code")
    public void theResultOfBankCustomerTotalTransactionSummaryDisplayedInTheConsoleCode() {
        getSummaryOfCustomer(debitTotalMap, creditTotalMap);
    }
}
