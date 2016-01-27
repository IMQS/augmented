# Settings
Settings handels preset values that might want to be changed during runtime.

## onCreate
onCreate starts by calling the 'load_defaults' method. An 'OnClickListener' is
set to the save button. When save is clicked all values from the different
settings are stored in shared preferences, 'PREFERENCE, and the activity
finishes.

## load_defaults
load_defaults sets the values for 'marker_tracker', 'gps_spoof', 'latitude',
'longitude', 'angle_offset', 'login_server' and 'pull_server' to there
corrisponding values in shared preferences, 'PREFERENCE'. Some of those might
not have altered values and thus there default value will be set.

