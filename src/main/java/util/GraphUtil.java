package util;

import org.neo4j.driver.*;

import static org.neo4j.driver.Values.parameters;

public class GraphUtil implements AutoCloseable {
    private final Driver driver;

    public GraphUtil(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public void addLink(String originURL, String targetURL) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx ->
                    tx.run( "CREATE (o:Page {url: $origin})-[rel:LINKS_TO]->(t:Page {url: $target})",
                    parameters("origin", originURL, "target", targetURL))
            );
        }
    }
}
