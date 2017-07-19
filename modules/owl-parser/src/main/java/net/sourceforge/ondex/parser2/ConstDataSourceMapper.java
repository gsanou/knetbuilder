package net.sourceforge.ondex.parser2;

import net.sourceforge.ondex.core.DataSource;
import net.sourceforge.ondex.core.ONDEXGraph;
import net.sourceforge.ondex.core.utils.CachedGraphWrapper;
import net.sourceforge.ondex.core.utils.DataSourcePrototype;

/**
 * TODO: comment me!
 *
 * @author brandizi
 * <dl><dt>Date:</dt><dd>19 Jul 2017</dd></dl>
 *
 */
public class ConstDataSourceMapper<S> extends ConstMapper<S, DataSourcePrototype, DataSource>
{
	@Override
	public DataSource map ( S source, ONDEXGraph graph )
	{
		return CachedGraphWrapper
		.getInstance ( graph )
		.getDataSource ( this.getValue () );
	}
}
