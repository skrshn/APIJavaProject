package utils;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import static io.restassured.RestAssured.given;

public class CommonMethods {
    public static RequestSpecification getDebitOrCreditRequest(String transactionType, int pageNumber) {
        return given().header(Constants.HEADER_CONTENT_TYPE, Constants.HEADER_CONTENT_TYPE_VALUE)
                .queryParam("txnType", transactionType).queryParam("page", pageNumber);
    }

    public static Response getDebitOrCreditResponse(RequestSpecification requestSpecification) {
        return requestSpecification.when().get(Constants.BaseURI);
    }

    public static Map<Integer, Integer> totalDebitOrCreditTransactions(String transactionType, Response debitOrCreditResponseForPage) {
        Map<Integer, Integer> lineMap = new TreeMap<>();

        int debitOrCreditTotalPages = Integer.parseInt(debitOrCreditResponseForPage.jsonPath().getString("total_pages"));
        System.out.println(transactionType+" "+debitOrCreditTotalPages);

        if (debitOrCreditTotalPages == 0) {
            System.out.println("Sorry There is no either Debit or Credit Transaction");
            System.exit(0);
        }

        for (int pages = 1; pages <= debitOrCreditTotalPages; pages++) {
            debitOrCreditResponseForPage = getDebitOrCreditResponse(getDebitOrCreditRequest(transactionType, pages));

            String[] userIdArrayComplete = debitOrCreditResponseForPage.jsonPath().getString("data.userId")
                    .replaceAll("[\\[,\\]]", "").split(" ");

            String[] amountArrayComplete = debitOrCreditResponseForPage.jsonPath().getString("data.amount")
                    .replaceAll("[\\[$,\\]]", "").split(" ");

//            System.out.println(Arrays.asList(userIdArrayComplete));
//            System.out.println(Arrays.asList(amountArrayComplete));

            for (int i = 0; i < userIdArrayComplete.length; i++) {
                if (!lineMap.containsKey(Integer.valueOf(userIdArrayComplete[i]))) {
                    lineMap.put(Integer.parseInt(userIdArrayComplete[i]), (int) Double.parseDouble(amountArrayComplete[i]));
                } else {
                    lineMap.replace(Integer.valueOf(userIdArrayComplete[i]), lineMap.get(Integer.valueOf(userIdArrayComplete[i])) + (int) Double.parseDouble(amountArrayComplete[i]));
                }
            }
            System.out.println(pages+" "+lineMap);
        }
        System.out.println(transactionType+" "+lineMap);

        return lineMap;
    }

    public static void getSummaryOfCustomer(Map<Integer, Integer> debitTotalMap, Map<Integer, Integer> creditTotalMap) {
        Map<Integer, Integer> summaryMap = new TreeMap<>();
        for (Map.Entry<Integer, Integer> entryDebitTotalMap : debitTotalMap.entrySet()) {
            for (Map.Entry<Integer, Integer> entryCreditTotalMap : creditTotalMap.entrySet()) {
                if (entryDebitTotalMap.getKey().equals(entryCreditTotalMap.getKey())) {
                    summaryMap.put(entryDebitTotalMap.getKey(), entryDebitTotalMap.getValue() - entryCreditTotalMap.getValue());
                }
            }
        }
        summaryMap.forEach((key, value) -> System.out.println("User Id: " + key + " - Total amount in the account: "
                + NumberFormat.getCurrencyInstance(Locale.US).format(value)));
    }
}
