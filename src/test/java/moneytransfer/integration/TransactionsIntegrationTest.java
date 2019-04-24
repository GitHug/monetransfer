package moneytransfer.integration;

import moneytransfer.controllers.AccountsController;
import moneytransfer.controllers.TransactionsController;
import moneytransfer.models.Account;
import moneytransfer.models.Transaction;
import moneytransfer.requests.AccountRequest;
import moneytransfer.requests.TransactionRequest;
import moneytransfer.requests.UpdateAccountRequest;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TransactionsIntegrationTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(TransactionsController.class)
                .register(AccountsController.class)
                .register(JacksonFeature.class);
    }

    @Test
    public void createTransaction_should_transfer_amount_from_source_to_target_account () {
        // given
        AccountRequest request = new AccountRequest();
        request.setName("test");

        Supplier<Account> createAccount = () -> target("/accounts")
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Account.class);

        Account source = createAccount.get();
        Account target = createAccount.get();

        UpdateAccountRequest updateRequest = new UpdateAccountRequest();
        updateRequest.setBalance(BigDecimal.valueOf(6000));

        Response updateResponse = target(String.format("/accounts/%s", source.getId()))
                .request()
                .build("PATCH", Entity.entity(updateRequest, MediaType.APPLICATION_JSON_TYPE))
                .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
                .invoke();

        assertEquals(200, updateResponse.getStatus(), "The update request was successful");

        // when
        BigDecimal amount = BigDecimal.valueOf(450);
        TransactionRequest transactionRequest = new TransactionRequest(source.getId(), target.getId(), amount);

        Response response = target("/transactions")
                .request()
                .post(Entity.entity(transactionRequest, MediaType.APPLICATION_JSON_TYPE));

        // then
        assertEquals(200, response.getStatus(), "Response should be 200");

        Transaction transaction = response.readEntity(Transaction.class);

        source.setBalance(BigDecimal.valueOf(5550));
        assertEquals(source, transaction.getSourceAccount());

        target.setBalance(amount);
        assertEquals(target, transaction.getTargetAccount());

        assertEquals(amount, transaction.getAmount());

        // when
        amount = BigDecimal.valueOf(5);
        transactionRequest = new TransactionRequest(target.getId(), source.getId(), amount);

        Transaction transaction2 = target("/transactions")
                .request()
                .post(Entity.entity(transactionRequest, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Transaction.class);

        // then
        source.setBalance(BigDecimal.valueOf(5555));
        assertEquals(source, transaction2.getTargetAccount());

        target.setBalance(BigDecimal.valueOf(445));
        assertEquals(target, transaction2.getSourceAccount());

        assertEquals(amount, transaction2.getAmount());

        //noinspection Convert2Diamond
        List<Transaction> transactions = target("/transactions")
                .request()
                .get()
                .readEntity(new GenericType<List<Transaction>>() {});

        assertEquals(List.of(transaction, transaction2), transactions);
    }


}
