#What we tried


##ARToolkit
ARToolkit was used on the ARToolkit_integration branch.

The results obtained were not very good. The OpenGL cube rendered was extremely jittery and often not rendered correctly at all.
This could be due to bad calibration of the camera (which was done using their tools/code).
ARToolkit also does not seem to have built in pose estimation.

Many compilation and setup issues were encountered. Both with missing dependencies and the porting to AndroidStudio.

ARToolkit was scraped and OpenCV/Aruco used instead.

##Prototyping environment
The purpose of developing a Prototyping environment was to minimize iteration time when developing and testing
the AR algorithms.

The Recording tool (on branch `sensor_tracking`) forms an integral part of this process.

Prototyping of algorithms is best done in cpp ,using cpp OpenCV, on a PC using a dedicated camera (eg webcam in laptop).

##Recording tool
An Android application for recording Video and simultaneous sensor data is on the `sensor_tracking` branch.

The video records at 30fps. The media recorder is set to 60fps but will default to the highest possible frame rate.
When using the live feed, from a camera view for example, you will only get up to approximately 16fps.

The sensor data is recorded into a text file (See format below, **Space seperated**).

\[Accel X\] \[Accel Y\] \[Accel Z\] \[Gyro X\] \[Gyro Y\] \[Gyro Z\] \[MagField X\] \[MagField y\] \[MagField z\] \[Delta time\]

* Accelerometer samples: For example <Accel X>, are `Acceleration minus Gx on the x-axis` .
* Gyroscope samples: For example <Gyro X>, are `Angular speed around the x-axis` .
* Magnetic field samples: `All values are in micro-Tesla (uT) and measure the ambient magnetic field in the X, Y and Z axis` .
* Delta Time: The time in milliseconds since the start of the recording of both the video and the sensor sample recordings.

See [SensorEvent](http://developer.android.com/reference/android/hardware/SensorEvent.html "SensorEvent API page on developer.android") for more information.

NOTE: The Delta Time may not be perfectly synced with the video.
