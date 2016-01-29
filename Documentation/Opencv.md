#OpenCV
Download OpenCV from [the main download page](http://opencv.org/downloads.html).
##Version
[OpenCV4Android v2.4.10](http://sourceforge.net/projects/opencvlibrary/files/opencv-android/2.4.10/) is used in this project.
It was included as a module in the AndroidStudio project.
The java code is called directly and via the JNI (Java Native Interface).

##instalation
mkdir build

cd build

cmake ../

make -j8

sudo make -j8 install

##Usage

####Camera calibration
We used OpenCV to obtain the camera_matrix, distortion_coefficients, and the avg_reprojection_error.
We used the `sensor_tracking` branch app to record video of a calibration chessboard, copied the video to a laptop and then used the OpenCV calibration cpp program.

##Improvements & suggestions
Ideally the whole project should use Native OpenCV 3.1 as it uses the latest
Aruco 1.3 Contrib package.
