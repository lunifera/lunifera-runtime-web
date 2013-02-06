package org.lunifera.runtime.web.http.tests.context;

import java.io.IOException;
import java.sql.Connection;
import java.util.Calendar;

import javax.naming.Context;
import javax.sql.DataSource;

import org.eclipse.jetty.http.HttpException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RestIntegrationTest {

	private WebApplicationContext applicationContext;
	private SolrServer solrServer;
	private Connection dbConnection;
	private ArticleController articleController;
	private Server server;

	private RestletFrameworkServlet restlet;
	private final HttpClient httpClient = new HttpClient();
	private static final String ROOT_URL = "http://localhost:8181/rpoRest/";

	@BeforeClass
	public static void importAndInit() throws Exception {
		ImporterFixtureTestcase fixture = new ImporterFixtureTestcase() {
		};
		fixture.setUp();
		fixture.importTestData();
		fixture.tearDown();
		System.out.println("BEFORE OK");
	}

	@Before
	public void setUp() throws Exception {

		// load the restlet servlet
		ClassPathXmlApplicationContext tempContext = new ClassPathXmlApplicationContext(
				new String[] { "rest-interface.xml" });

		restlet = (RestletFrameworkServlet) tempContext.getBean("restlet");

		server = new Server(8181);
		Context rootServer = new Context(server, "/", Context.SESSIONS);
		rootServer.addServlet(new ServletHolder(restlet), "/rpoRest/*");
		server.start();

		// now get the "real" appContext from the servlet
		applicationContext = restlet.getWebApplicationContext();

		DataSource dataSource = (DataSource) applicationContext
				.getBean("dataSource");

		dbConnection = dataSource.getConnection();

		solrServer = (SolrServer) applicationContext
				.getBean(ImporterFixtureTestcase.getSolrType());

		articleController = (ArticleController) applicationContext
				.getBean("articleController");

		calculateArticleCounts();
		solrServer.commit(true, true);
	}

	@After
	public void tearDown() throws Exception {
		dbConnection.close();
		server.stop();
	}

	private void calculateArticleCounts() {
		System.out
				.println("Starting calculation of article counts for all tags...");

		TagscoreIndexerJob tagscoreIndexer = (TagscoreIndexerJob) applicationContext
				.getBean("rpoTagscoreIndexer");
		tagscoreIndexer.setDisabled(false);
		tagscoreIndexer.indexTagscore();

		System.out.println("Calculation of article counts finished.");
	}

	/**
	 * Many tests in one, so we dont have to wait for 60 secs all the time
	 */
	@Test
	public void testGeneralFunctionality() throws Exception {

		// Check slideshow
		Article newArticle = articleController
				.getArticle("rpo_slideshow_32890");
		assertEquals(1, newArticle.getPictures().size());
		assertEquals("slideshow", newArticle.getType());
		assertEquals("rpo", newArticle.getOrigin());
		assertEquals(2008, newArticle.getUpdated().get(Calendar.YEAR));

		QueryResponse qr = solrServer.query(new SolrQuery(
				"id:rpo_slideshow_32890"));
		SolrDocument doc = qr.getResults().get(0);
		assertNotNull(doc);

		assertEquals(03, newArticle.getUpdated().get(Calendar.MONTH));
		assertEquals(19, newArticle.getUpdated().get(Calendar.DAY_OF_MONTH));
		assertEquals(18, newArticle.getUpdated().get(Calendar.HOUR_OF_DAY));
		assertEquals(27, newArticle.getUpdated().get(Calendar.MINUTE));
		assertEquals(11, newArticle.getUpdated().get(Calendar.SECOND));

		assertEquals(
				"http://www.rp-online.de/public/bildershowinline/aktuelles/sport/fussball/32890",
				newArticle.getUrl().toString());
		assertEquals("aktuelles/sport/fussball", newArticle.getChannel());
		assertEquals("Lingor nach Brutalo-Foul schwer verletzt", newArticle
				.getTitle().trim());
		assertEquals("Frauen-Fußball", newArticle.getSubTitle().trim());
		assertEquals(1, newArticle.getPictures().size());
		assertEquals(
				"http://static.rp-online.de/layout/showbilder/32890-170519_Germany_Soccer_Cup_Final_CBE107.jpg",
				newArticle.getPictures().get(0).getUrl().toString());
		assertTrue(newArticle
				.getContent()
				.indexOf(
						"Weltmeisterin Renate Lingor droht eine wochenlange Pause. Die Mittelfeldspielerin musste "
								+ "beim DFB-Pokalfinale der Frauen zwischen ihrem Klub 1. FFC Fran") != -1);

		// Test updating slideshow
		String docXML = "<documents>" + "<document>"
				+ "<id>rpo_slideshow_32890</id>" + "<type>slideshow</type>"
				+ "<origin>rpo</origin>"
				+ "<updated>2008-12-24T18:27:11+02:00</updated>"
				+ "<url>http://www.rp-online.de/public/334</url>"
				+ "<channel name=\"deutschland\">"
				+ "<channel name=\"sport\" />" + "</channel>"
				+ "<title><![CDATA[verletzt]]></title>"
				+ "<subtitle><![CDATA[Fußball]]></subtitle>"
				+ "<body><![CDATA[Weltmeisterin]]>" + "</body>" + "</document>"
				+ "</documents>";

		PostMethod method = new PostMethod(ROOT_URL + "documents/");
		RequestEntity re = new StringRequestEntity(docXML, "text/xml", "UTF-8");
		method.setRequestEntity(re);
		int updateStatusCode = httpClient.executeMethod(method);
		assertEquals(201, updateStatusCode);
		System.out.println("Sleeping for 10 secs for update");

		Thread.sleep(10000);
		solrServer.commit();

		Article updatedSlideshow = articleController
				.getArticle("rpo_slideshow_32890");
		assertEquals(1, updatedSlideshow.getPictures().size());
		assertEquals("slideshow", updatedSlideshow.getType());
		assertEquals("rpo", updatedSlideshow.getOrigin());
		assertEquals(2008, updatedSlideshow.getUpdated().get(Calendar.YEAR));
		assertEquals(3, updatedSlideshow.getUpdated().get(Calendar.MONTH));
		assertEquals(19,
				updatedSlideshow.getUpdated().get(Calendar.DAY_OF_MONTH));
		assertEquals(
				"http://www.rp-online.de/public/bildershowinline/aktuelles/sport/fussball/32890",
				updatedSlideshow.getUrl().toString());
		assertEquals("aktuelles/sport/fussball", updatedSlideshow.getChannel());
		// assertEquals("verletzt", updatedSlideshow.getTitle().trim());
		// assertEquals("Fußball", updatedSlideshow.getSubTitle().trim());
		assertTrue(updatedSlideshow.getContent().indexOf("Weltmeisterin") != -1);

		// Test deleting slideshow
		// DeleteMethod deleteMethod = new DeleteMethod(this.rootURL +
		// "document/rpo_slideshow_32890");
		// int statusCode = this.httpClient.executeMethod(deleteMethod);
		// assertEquals(200, statusCode);
		//
		// System.out.println("Sleeping for 10 secs for delete");
		// Thread.sleep(10000);
		//
		// this.solrServer.commit();
		// QueryResponse qr1 = this.solrServer.query(new
		// SolrQuery("id:rpo_slideshow_32890"));
		// assertEquals(0, qr1.getResults().getNumFound());

	}

	@Test
	public void testGetDossierWithTimefilter() throws Exception {
		GetMethod method = new GetMethod(
				ROOT_URL
						+ "dossiers/Berlin?timefilter=%5B2008-01-01T00%3A00%3A00%20TO%202008-12-31T23%3A59%3A59%5D");
		int statusCode = httpClient.executeMethod(method);
		assertEquals(200, statusCode);

		String responseBodyAsString = method.getResponseBodyAsString();
		System.out.println(responseBodyAsString);
		method.releaseConnection();
	}

	@Test
	public void testCount() throws HttpException, IOException {
		GetMethod method = new GetMethod(ROOT_URL + "dossiers/Berlin/count");
		assertEquals(200, httpClient.executeMethod(method));
		assertEquals(23, Integer.parseInt(new String(method.getResponseBody())));
		method.releaseConnection();

		method = new GetMethod(ROOT_URL
				+ "dossiers/ThisDossierDoesNotExist/count");
		assertEquals(404, httpClient.executeMethod(method));
		method.releaseConnection();
	}
}
