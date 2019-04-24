package moneytransfer;

import moneytransfer.controllers.AccountsController;
import moneytransfer.controllers.TransactionsController;
import moneytransfer.exceptions.ControllerExceptionMapper;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);
    private static int PORT = 8000;

    public static void main (String ... args) throws Exception {
        Server server = new Server(PORT);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        server.setHandler(context);

        ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitParameter(ServerProperties.PROVIDER_CLASSNAMES,
                String.join(",", new String[]{
                        AccountsController.class.getCanonicalName(),
                        TransactionsController.class.getCanonicalName(),
                        ControllerExceptionMapper.class.getCanonicalName()
                        }));

        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }



//        public static void main(String[] args) {
            logger.debug("Debug log message");
            logger.info("Info log message");
            logger.error("Error log message");

//        }

//        int serverPort = 8000;
//
//        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
//        server.createContext("/moneytransfer.Api/hello", (exchange -> {
//            try {
//                BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), "utf-8"));
//                String requestBody = br.lines().collect(Collectors.joining(System.lineSeparator()));
//
//                moneytransfer.models.Account account = new moneytransfer.models.Account("hello");
//                ObjectMapper obj = new ObjectMapper();
//                String json = requestBody; // obj.writeValueAsString(account);
//                moneytransfer.models.Account newAccount = obj.readValue(json, moneytransfer.models.Account.class);
//
//                exchange.sendResponseHeaders(200, obj.writeValueAsString(newAccount).getBytes().length);
//                OutputStream output = exchange.getResponseBody();
//                output.write(obj.writeValueAsString(newAccount).getBytes());
//                output.flush();
//                exchange.close();
//            } catch (Exception e) {
//                logger.debug(e.getMessage());
//            }
//
//        }));
//
//        server.setExecutor(null); // creates a default executor
//        server.start();
    }

}
