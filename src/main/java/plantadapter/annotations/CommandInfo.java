package plantadapter.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import plantadapter.commands.Command;
import plantadapter.communication.ISink;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
public @interface CommandInfo {
	
	
	// TODO Accetta in ingresso un Plant...
	
	public Class<? extends ISink<Command>> commandExecutorClass(); // TODO Marker (?)
}