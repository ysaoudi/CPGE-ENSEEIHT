package Display;

import java.util.ArrayList;
import My_game.*;
import My_game.Object;

public interface Display {

	/** display your location
	 * 
	 * @param place
	 */
	public void displaylocation(Place place);
	
	/** display objects, people and knowledge in your location
	 * which are visible
	 * @param place
	 */
	public void locationdetails(Place place);
	
	/** display available paths from the current place.
	 * 
	 * @param place
	 */
	public void availablepaths(Place place);
	
	/** informs the player of the path he's taking
	 * 
	 * @param path
	 */
	public void pathTaken(Path path);
		
	/** displays the possessed objects, their respective sizes, the cummulated weight
	* and also the available weight
	* 
	* @param explorator
	*/
	public void possessedObjects(Explorer explorer, Game game);
	
	/** Display the player's knowledge
	 * 
	 * @param explorator
	 */
	public void possessedKnowledge(Explorer explorer);
	
	/** Informs the player of a newly acquired knowledge
	 * 
	 * @param knowledge
	 */
	public void newKnowledge(Knowledge knowledge);
	
	/** Informs the player of the chosen object
	 * 
	 * @param object 
	 */
	public void chosenObject(Object object);
	
	/** Informs the player of a newly acquired object
	 * 
	 * @param object
	 */
	public void newObject(Object object);
	
	/** Informs the player of not having enough place for the object he's trying to acquire.
	 * 
	 * @param object the object he wants to acquire
	 */
	public void notEnoughPlace(Object object);
	
	/** Informs the player of the unability to drop an item in this location.
	 */
	public void impossibleDrop();
	
	/** Informs the player of the dropped object.
	 * 
	 * @param object
	 */
	public void droppedObject(Object object);
	
	/** Informs the player of the consumed object.
	 * 
	 * @param object
	 */
	public void consommumedObject(Object object);
	
	/** Display the interation with a person
	 * 
	 * @param person
	 */
	public void interaction(Person person);
	
	/** Display the available actions for the player
	 * if there are none, it's mentioned 
	 * 
	 * @param choice contains the list of actions 
	 * @return number of actions
	 */
	public int displayActions(Choice choice);

	/** Display the available objects for the player
	 *  if there are none, it's mentioned 
	 *  
	 * @param objects list of objects
	 * @return number of available objects
	 */
	public int displayObjects(ArrayList<Object> objects);


	/** Display the available objects for the player
	 *  if there are none, it's mentioned 
	 *  
	 * @param explorer list of objects
	 * @return number of available objects
	 */
	public int displayPossessedObjects(Explorer explorer);
	
    /** Display the available choices for the player
	 *  if there are none, it's mentioned 
	 *  
	 * @param choices list of choices
	 * @return number of available objects
	 */
	public int displayChoices(ArrayList<Choice> choices);


	/** Displays a set of knowledge
	 *  if there are none, it's mentioned
	 *  
	 * @param knowledge
	 * @return number of knowledge
	 */
	public int displayKnowledge(ArrayList<Knowledge>knowledge);
	
	/** Displays a set of people
	 *  if there are none, it's mentioned
	 *  
	 * @param people
	 * @return number of people
	 */
	public int displayPeople(ArrayList<Person> people);
	
	/** Displays a set of paths
	 *  if there are none, it's mentioned
	 *  
	 * @param paths
	 * @return number of paths
	 */
	public int displayPaths(ArrayList<Path> paths);
	
	/** end of the game
	 */
	public void endGame();
}
