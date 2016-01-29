# Login
The login handels all comunication with the login and pull servers and passes
the server response to the 'ARActivity'.

## onCreate
Creates a location listener that updates the latitude and longitude in shared
preferences. Create an `onClickListener` for the login button that gets the
username and password from the entered fields and then executes
`SendHTTPLoginTask`, the cookie that is recieved from that is then sent to
`sendGPSLocationTask`, the json responce is then sent to `ARActivity`.

##SendHTTPLoginTask

