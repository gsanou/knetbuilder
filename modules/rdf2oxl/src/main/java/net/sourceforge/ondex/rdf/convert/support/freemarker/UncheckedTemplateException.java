package net.sourceforge.ondex.rdf.convert.support.freemarker;

import freemarker.template.TemplateException;

/**
 * TODO: comment me!
 *
 * @author brandizi
 * <dl><dt>Date:</dt><dd>25 Jul 2018</dd></dl>
 *
 */
@SuppressWarnings ( "serial" )
public class UncheckedTemplateException extends RuntimeException
{
	public UncheckedTemplateException ( String message, TemplateException cause )
	{
		super ( message, cause );
	}	
}
