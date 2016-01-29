#What we tried

##Prototyping environment
The purpose of developing a Prototyping environment was to minimize iteration time when developing and testing
the AR algorithms.

The [Recording tool](#RecordingT) (on branch `sensor_tracking`) forms an integral part of this process.

Prototyping of algorithms is best done in c++ ,using c++ OpenCV, on a PC using a dedicated camera (eg webcam in laptop).

##ARToolkit
ARToolkit was used on the `ARToolkit_integration` branch.

Do not ever use ARToolkit

The results obtained were not very good. The OpenGL cube rendered was extremely jittery and often not rendered correctly at all.
This could be due to bad calibration of the camera (which was done using their tools/code).
ARToolkit also does not seem to have built in pose estimation.

We encountered many compilation and setup issues. Both with missing dependencies and the porting to AndroidStudio.

We scraped ARToolkit and used OpenCV/Aruco instead.


##<a name=RecordingT>Recording tool</a>
An Android application for recording Video and simultaneous sensor data is on the `sensor_tracking` branch.

The video records to an MP4 file. The media recorder is set to 60fps but will default to the highest possible frame rate, which is 30fps on the tablets we used.
When using the live feed, from a camera view for example, you will only get up to approximately 16fps.

The sensor data is recorded into a text file (See format below, **Space seperated**).

Both the text file and the video filenames are formated by the date and start time of the recording.

The files appear to record to the /storage/emulated/0/pictures and not the pictures folder of the SD card.

FORMAT:

```Accel_X Accel_Y Accel_Z Gyro_X Gyro_Y Gyro_Z MagField_X MagField_Y MagField_Z Delta time ```

* Accelerometer samples: For example <Accel X>, are `Acceleration minus Gx on the x-axis` .
* Gyroscope samples: For example <Gyro X>, are `Angular speed around the x-axis` .
* Magnetic field samples: `All values are in micro-Tesla (uT) and measure the ambient magnetic field in the X, Y and Z axis` .
* Delta Time: The time in milliseconds since the start of the recording of both the video and the sensor sample recordings.

See [SensorEvent](http://developer.android.com/reference/android/hardware/SensorEvent.html "SensorEvent API page on developer.android") for more information.

NOTE: The Delta Time may not be perfectly synced with the video.

##The glorious rotation vector
The rotation that is recieved from the OpenCV is a Euler angle while OpenGL extects a Quaternion as the argument for glRotate. There were issues with the conversion from Euler angles to Quaternions. One option is to use the translation and rotation vectors to manually create the view matrix for OpenGL. We tried this...and failed. Have fun

