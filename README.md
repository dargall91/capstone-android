# Setup

In order to make API calls to your local server instance, you will need to create file that contains your local IP
address. The file should be created in the `assets` folder. The assets folder must be created by right-clicking the
`main` folder and selecting `New -> Directory -> assets`. In this folder, create a file named `server_address.dat`.
This file will be ignored by git, DO NOT add it to the repo. The file should contain a single line of text that
contains your local IP and nothing else.

# Running on the Emulator

If you're unable to get the app to run in the built in emulator, try this:

1. Go to `File -> Settings -> Emulator`
2. Uncheck `Launch in a tool window`
3. Restart android studio and rerun

# Setting up location on the emulator

The default location in the emulator is somewhere in california. We need to be able to update this.

1. On the emulator, click the 3 dots "more" option.
2. Click Location
3. Search for desired location.
4. Save the point.
5. In the area on the left, ensure that the point you wish to use is selected, and press Set Location.

# Using the application

Assuming everything is correct server side, the only thing that is necessary here is to press the message button on the screen, this will result in the following:

1. An alert will be sent to your device.
2. The alert will be stored in the persistent database.
3. The alert will be temporarily stored in the local database.
4. The alert will be viewable in the History tab in a recycler view.
