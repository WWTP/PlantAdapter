package plantadapter.inputs;

/* TODO Eredita da RuntimeExceptione e non da PlantAdapterException
 * in quanto si tratta di un tipo di eccezione che ha senso solo internamente
 * al componente (fuori non vi è il concetto di "maschera") e che peraltro
 * non dovrebbe mai verificarsi in quanto un IResponseParser è restituito
 * sempre da ResponseParserFactory e quindi necessariamente corretto se gli
 * viene passato il dato per cui è stato richiesto.
 * 
 * Questo genere di eccezioni, quando incontrate, dovrebbero essere trasformate
 * in eccezioni facenti parte del modello (o in "informazioni di errore" facenti
 * eventualmente parte dell'ontologia o del "contratto" con l'utilizzatore).
 */

public class MaskFormatException extends RuntimeException {

}