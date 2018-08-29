package net.sourceforge.ondex.rdf.convert;

import static java.lang.String.format;
import static uk.ac.ebi.utils.io.IOUtils.readResource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import info.marcobrandizi.rdfutils.namespaces.NamespaceUtils;
import net.sourceforge.ondex.rdf.convert.support.ItemConfiguration;
import net.sourceforge.ondex.rdf.convert.support.Resettable;
import net.sourceforge.ondex.rdf.convert.support.ResourceHandler;
import net.sourceforge.ondex.rdf.convert.support.ResourceProcessor;

/**
 * TODO: comment me!
 *
 * @author brandizi
 * <dl><dt>Date:</dt><dd>1 Aug 2018</dd></dl>
 *
 */
@Component
public class Rdf2OxlConverter
{
	@Autowired @Qualifier ( "itemConfigurations" )
	private List<ItemConfiguration> itemConfigurations;
	
	@Autowired @Qualifier ( "templateClassPath" )	
	private String templateClassPath;

	/**
	 * This is {@link ResourceProcessor} by default, {@link ItemConfiguration} can override it.
	 */
	@Autowired @Qualifier ( "resourceProcessor" )	
	private ResourceProcessor defaultResourceProcessor;
		
	/**
	 * Same as {@link #defaultResourceProcessor}
	 */
	@Autowired @Qualifier ( "resourceHandler" )	
	private ResourceHandler defaultResourceHandler;
	
	
	@Autowired ( required = false )
	private List<Resettable> toBeResetComponents;
	
	
	public void convert ( Writer oxlOut )
	{		
		if ( this.toBeResetComponents != null )
			toBeResetComponents.forEach ( Resettable::reset );
		
		for ( ItemConfiguration itemCfg: this.itemConfigurations )
		{
			try
			{
				ResourceProcessor processor = itemCfg.getResourceProcessor ();
				if ( processor == null ) processor = this.defaultResourceProcessor;

				ResourceHandler handler = itemCfg.getResourceHandler ();
				if ( handler == null ) handler = this.defaultResourceHandler;
					
				processor.setConsumer ( handler );				
				handler.setOutWriter ( oxlOut );

				String itemName = itemCfg.getName ();
				
				String constructTemplate = itemCfg.getConstructTemplateName ();
				if ( constructTemplate != null ) constructTemplate = 
					NamespaceUtils.asSPARQLProlog () 
					+ readResource ( this.templateClassPath + "/" + constructTemplate ); 
				handler.setConstructTemplate ( constructTemplate );

				processor.setHeader ( itemCfg.getHeader () );
				processor.setTrailer ( itemCfg.getTrailer () );
				handler.setOxlTemplateName ( itemCfg.getGraphTemplateName () );
				
				processor.setLogPrefix ( "[" + itemName + " Processor]" );
				handler.setLogPrefix ( "[" + itemName + " Handler]" );
				
				// Some processors get URIs from previously fetched data, so this might be null  
				String resourcesSparql = itemCfg.getResourcesQueryName ();
				if ( resourcesSparql != null ) resourcesSparql = 
						NamespaceUtils.asSPARQLProlog () + readResource ( this.templateClassPath + "/" + resourcesSparql );
							
				processor.process ( resourcesSparql );
			}
			catch ( IOException ex ) {
				throw new UncheckedIOException ( 
					format ( "I/O error while processing %s: %s", itemCfg.getName (), ex.getMessage () ),
					ex
				);
			}				
		}
	}
}
