Here is what needs to be done:
* consider removing the registration out of the scope of the worker thread
* check to see if the channel is null -> this might prevent it from the weird nullptr 
* attach an object to show that the channel is being used
