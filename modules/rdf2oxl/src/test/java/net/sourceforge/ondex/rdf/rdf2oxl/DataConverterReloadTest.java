package net.sourceforge.ondex.rdf.rdf2oxl;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.io.Resources;

import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.parser.oxl.Parser;

/**
 * TODO: comment me!
 *
 * @author brandizi
 * <dl><dt>Date:</dt><dd>17 Oct 2018</dd></dl>
 *
 */
public class DataConverterReloadTest extends AbstractConverterReloadTest
{
	@BeforeClass
	public static void initData () throws IOException
	{
		resultGraph = loadOxl (
			"target/concepts_reload_test.xml", 
			"target/concepts_reload_test_tdb",
			Pair.of ( Resources.getResource ( "bioknet.owl" ).openStream (), "RDF/XML" ),
			Pair.of ( Resources.getResource ( "oxl_templates_test/bk_ondex.owl" ).openStream (), "RDF/XML" ),
			Pair.of ( Resources.getResource ( "support_test/publications.ttl" ).openStream (), "TURTLE" )
		);
	}
	
	@Test
	public void testConceptsReload ()
	{
		ConceptClass cc = resultGraph.getMetaData ().getConceptClass ( "Publication" );
		assertNotNull ( "Test Class not found!", cc );
		
		ONDEXConcept concept = resultGraph
		.getConceptsOfConceptClass ( cc )
		.stream ()
		.filter ( c -> "26396590".equals ( c.getPID () ) )
		.findFirst ()
		.orElse ( null );
		
		assertNotNull ( "Test Concept not found!", concept );
	}

}