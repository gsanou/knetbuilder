package net.sourceforge.ondex.rdf.convert.support;

import java.io.Writer;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import info.marcobrandizi.rdfutils.jena.SparqlEndPointHelper;
import info.marcobrandizi.rdfutils.jena.SparqlUtils;
import net.sourceforge.ondex.rdf.convert.support.freemarker.FreeMarkerHelper;

/**
 * TODO: comment me!
 *
 * @author brandizi
 * <dl><dt>Date:</dt><dd>25 Jul 2018</dd></dl>
 *
 */
@Component ( "resourceHandler" )
public class ResourceHandler implements Consumer<Set<Resource>>
{
	private Writer outWriter;
	
	private String oxlTemplateName;
	private String constructTemplate;
	
	private SparqlEndPointHelper sparqlHelper;
	private FreeMarkerHelper templateHelper;
	
	private Function<Model, Map<String, Object>> dataPreProcessor;
	
	
	private String logPrefix = "[RDF Hanlder]";
	
	
	@Override
	public void accept ( Set<Resource> res )
	{
		// Get a VALUES-compliant representation of all these URIs
		if ( res.size () == 0 ) return;
		
		String valuesStr = res.stream ()
		.map ( Resource::getURI )
		.map ( uri -> "( <" + uri + "> )" )
		.collect ( Collectors.joining ( "\n" ) );
		
		// And use it in the SPARQL template
		String sparqlConstruct = constructTemplate.replace ( "$resourceIris", valuesStr );
		
		// Then, run it and process the OXL-generating template
		synchronized ( this.outWriter ) 
		{
			sparqlHelper.processConstruct (
				logPrefix,
				sparqlConstruct,
				model ->  {
					Map<String, Object> initialData = dataPreProcessor == null ? null: dataPreProcessor.apply ( model );
					templateHelper.processJenaModel ( model, oxlTemplateName, this.outWriter, initialData ); 
				}
			);
		}
	}


	public Writer getOutWriter ()
	{
		return outWriter;
	}


	public void setOutWriter ( Writer outWriter )
	{
		this.outWriter = outWriter;
	}


	public String getOxlTemplateName ()
	{
		return oxlTemplateName;
	}


	public void setOxlTemplateName ( String oxlTemplateName )
	{
		this.oxlTemplateName = oxlTemplateName;
	}


	public String getConstructTemplate ()
	{
		return constructTemplate;
	}


	public void setConstructTemplate ( String constructTemplate )
	{
		this.constructTemplate = constructTemplate;
	}


	public SparqlEndPointHelper getSparqlHelper ()
	{
		return sparqlHelper;
	}

	@Autowired
	public void setSparqlHelper ( SparqlEndPointHelper sparqlHelper )
	{
		this.sparqlHelper = sparqlHelper;
	}


	public FreeMarkerHelper getTemplateHelper ()
	{
		return templateHelper;
	}

	@Autowired	
	public void setTemplateHelper ( FreeMarkerHelper templateHelper )
	{
		this.templateHelper = templateHelper;
	}

	
	public Function<Model, Map<String, Object>> getDataPreProcessor ()
	{
		return dataPreProcessor;
	}

	public void setDataPreProcessor ( Function<Model, Map<String, Object>> dataPreProcessor )
	{
		this.dataPreProcessor = dataPreProcessor;
	}


	public String getLogPrefix ()
	{
		return logPrefix;
	}


	@Autowired ( required = false ) @Qualifier ( "logPrefix" )		
	public void setLogPrefix ( String logPrefix )
	{
		this.logPrefix = logPrefix;
	}
	
}
