#What we tried


##ARToolkit
ARToolkit was used on the ARToolkit_integration branch.



##Recording tool
An Android application for recording Video and simultaneous sensor data is on the sensor_tracking branch.

The video records at 30fps. The media recorder is set to 60fps but will default to the highest possible frame rate.
When using the live feed, from a camera view for example, you will only get up to approximately 16fps.

The sensor data is recorded into a text file (See format below, **Space seperated**).
\[Accel X\] \[Accel Y\] \[Accel Z\] \[Gyro X\] \[Gyro Y\] \[Gyro Z\] \[MagField X\] \[MagField y\] \[MagField z\] \[Delta time\]

Accelerometer samples: For example <Accel X>, are `Acceleration minus Gx on the x-axis` .
Gyrometer samples: For example <Gyro X>, are `Angular speed around the x-axis` .
Magnetic field samples: `All values are in micro-Tesla (uT) and measure the ambient magnetic field in the X, Y and Z axis` .
Delta Time: The time in ms since the start of the recording of both the video and the sensor sample recordings.

See [SensorEvent](http://developer.android.com/reference/android/hardware/SensorEvent.html "SensorEvent API page on developer.andoroid") for more information.

NOTE: The Delta Time may not be perfectly synced with the video.
