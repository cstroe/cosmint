package com.github.cstroe.spendhawk;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("target/graphDb");
        graphDb.shutdown();
        System.out.println( "Hello World after Neo4J!" );
    }
}
