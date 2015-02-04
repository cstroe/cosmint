package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.SpendHawk;
import com.github.cstroe.spendhawk.testutil.web.AggregateFilter;
import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;

import java.io.File;

@ArquillianSuiteDeployment
public class SpendHawkDeployment {

    private static final String WEBAPP_SRC = "src/main/webapp";
    private static final String RESOURCES_SRC = "src/main/resources";
    private static final String TEST_RESOURCES_SRC = "src/test/resources";

    @Deployment
    @OverProtocol("Servlet 3.0")
    public static WebArchive deploy() {
        WebArchive war = ShrinkWrap.create(WebArchive.class);
        war.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                        .importDirectory(WEBAPP_SRC).as(GenericArchive.class),
                "/", Filters.includeAll());
        war.addPackages(true,
                new AggregateFilter(
                        Filters.exclude(".*/SpendHawkDeployment\\.class")
                ), SpendHawk.class.getPackage());


        WebArchive hibernateMappingFiles = ShrinkWrap.create(WebArchive.class)
                .as(ExplodedImporter.class)
                .importDirectory(RESOURCES_SRC, Filters.exclude(".*/hibernate.cfg.xml"))
                .as(WebArchive.class);

        war.merge(hibernateMappingFiles, "/WEB-INF/classes", Filters.includeAll());

        WebArchive hibernate = ShrinkWrap.create(WebArchive.class)
            .as(ExplodedImporter.class)
            .importDirectory(TEST_RESOURCES_SRC).as(WebArchive.class);

        war.merge(hibernate, "/WEB-INF/classes", Filters.includeAll());

        File[] files = Maven.resolver()
                .loadPomFromFile("pom.xml").resolve(
                        "org.freemarker:freemarker",
                        "org.hsqldb:hsqldb",
                        "org.hibernate:hibernate-core",
                        "org.apache.commons:commons-lang3",
                        "org.apache.commons:commons-csv",
                        "org.dbunit:dbunit"
                ).withTransitivity().asFile();

        war.addAsLibraries(files);

        return war;
    }

    @Test
    public void showDeploymentContents() {
        WebArchive archive = deploy();
        System.out.println(archive.toString(true));
    }
}
