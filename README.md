# Event scheduler

## User Interface
### The app provides two activities:

1. One activity shows the itinerary in a ListView (e.g. items for “Leave Home,” 0:00 and “Stop for gas at your favorite station,” 0:15).
2. The other activity allows the user to edit the MET(Mission Elapsed Time) time and description for an individual event in the itinerary.
* The itinerary activity has three items in its options menu:
1. New Event
2. Set Alarms
3. Cancel Alarms
“New Event” will be displayed on the action bar if there is room.
* The event activity shows the MET time and description for the event. Use the TimePicker widget to set the time. Three buttons should allow you 
1. Saving the activity being edited
2. Discarding changes without saving them
3. Deleting the event entirely

### Operation
* Choosing “New Event” from the itinerary view will add a new event to the itinerary and start the event activity to set the new event’s details
* Clicking an item in the itinerary view will start the event activity to edit that event’s details
* Choosing “Set Alarms” from the itinerary view will use a Calendar object to get the current time, then use AlarmClock intents to set alarms at the times specified by the MET offsets in the itinerary.
* Choosing “Cancel Alarms” will use AlarmClock intents to dismiss all alarms that have been set.



# Note:
	> Cancel Alarms will only work once due to the bug present in the AlarmClock class.
