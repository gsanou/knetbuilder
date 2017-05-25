package net.sourceforge.ondex.parser;

import java.util.stream.Stream;

import net.sourceforge.ondex.core.ConceptAccession;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.utils.ONDEXElemWrapper;

/**
 * TODO: comment me!
 *
 * @author brandizi
 * <dl><dt>Date:</dt><dd>28 Apr 2017</dd></dl>
 *
 */
public interface AccessionsMapper<S> extends ONDEXMapper<Stream<ConceptAccession>, S, ONDEXElemWrapper<ONDEXConcept>>
{

}