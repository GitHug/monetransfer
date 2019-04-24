package moneytransfer.integration;

import moneytransfer.controllers.AccountsController;
import moneytransfer.models.Account;
import moneytransfer.requests.AccountRequest;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountsIntegrationTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(AccountsController.class)
                .register(JacksonFeature.class);
    }

    @Test
    public void listAccounts_should_return_empty_list_of_accounts() {
        Response response = target("/accounts").request().get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));

        //noinspection Convert2Diamond
        List<Account> accounts = response.readEntity(new GenericType<List<Account>>() {});

        assertFalse(accounts.size() > 0);
    }

    @Test
    public void listAccounts_should_return_only_active_accounts() {
        // given
        AccountRequest request = new AccountRequest();
        request.setName("test");

        Response response1 = target("/accounts").request().post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
        Response response2 = target("/accounts").request().post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
        Response response3 = target("/accounts").request().post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));

        Account account1 = response1.readEntity(Account.class);
        Account account2 = response2.readEntity(Account.class);
        Account account3 = response3.readEntity(Account.class);

        // when
        target(String.format("/accounts/%s", account2.getId())).request().delete();

        Response accountsResponse = target("/accounts").request().get();

        //noinspection Convert2Diamond
        List<Account> accounts = accountsResponse.readEntity(new GenericType<List<Account>>() {});

        assertEquals(List.of(account1, account3), accounts);
    }
}
