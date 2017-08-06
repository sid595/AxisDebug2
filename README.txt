# AxisDebug2
Simple accelemeter data collection and analysis
Activity 1 will start on app startup. Here when you tap the logging button it will start logging the sensor data and when you tap again
the logging will stop. Each time you log some data that is a record number. 
When you tap Analyze button, it will go to new activity where you will have to first Read data by pressing the button with the same name.
After this there is the edit text view to enter the record number and clicking on Mean Filter will apply mean filter. Then you can either
plot data or click on SGOLAY filter to apply sgolay fitering on the data which is read and mean filtered and then plot. Either way a new
activity will open and show you a graph of data that was analyzed and plotted

NOTE: Exact algorithm of sGolay is not implemented. There are two coefficient sets available in the code:
1. Order 4 with 21 coefficients[currently used] 2. Order 3 with 31 coefficients
Note that ordere here means the order of polynomial that is being fit the frame data, not the order of the filter.
