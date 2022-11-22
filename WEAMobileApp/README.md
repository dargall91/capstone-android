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