package net.sourceforge.ondex.tools.tab.exporter.extractors;

import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXEntity;
import net.sourceforge.ondex.core.base.AbstractConcept;
import net.sourceforge.ondex.tools.tab.exporter.InvalidOndexEntityException;

/**
 * 
 * @author hindlem
 *
 */
public class CVAttributeExtractor implements AttributeExtractor{

	public String getValue(ONDEXEntity cOrr) throws InvalidOndexEntityException {
		if(AbstractConcept.class.isAssignableFrom(cOrr.getClass())){
			return ((ONDEXConcept)cOrr).getElementOf().getId();
		}
		throw new InvalidOndexEntityException(cOrr.getClass()+": is not an Ondex class for which cv is known");
	}

	@Override
	public String getHeaderName() {
		return "DataSource";
	}
	
}
