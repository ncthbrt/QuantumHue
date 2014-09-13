package co.za.cuthbert.three.components.interfaces;

import com.badlogic.ashley.core.Component;

/** Copyright Nick Cuthbert, 2014.
 * This interface is to assist serialisation and deserialisation
 * so that levels do not get broken between different version of the game
 *
 */
public abstract class ANamedComponent extends Component{
   public abstract String getComponentName();
}
