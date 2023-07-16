# site

Panopset website source code.


# Development PC Requirements

## Linux

Linux Mint is the recommended OS.  You can easily set up nginx there and run things locally much like in production.

## Windows

It might be easier to use Apache on Windows, than nginx.

Install Apache here, for deploystatic.cmd and runStaticApache.cmd to work without modification.

    %USERPROFILE%\Documents\apps\servers\apache\Apache24


Make these modifications to C:/Users/karldi/Documents/apps/servers/apache/Apache24/conf/httpd.conf:

    Define SRVROOT "%USERPROFILE%/Documents/apps/servers/apache/Apache24"
    Listen 8089

Replace %USERPROFILE% with your home directory, unfortunately httpd.conf won't recognize your windows variables.

## Environment Variables

Set up these environment variables on your development PC.

* on Linux Mint it can be done in ~/.profile.
* on Macs it can be done in ~/.zshrc.
* on Windows you'd make them profile environment variables.


Adjust for your target environment:


    export PANOPSET_SITE_DN=your site deployment domain name, ie: panopset.com.
    export PANOPSET_SITE_NAME=corresponds to the Host entry for your deployment site Host entry
    export PANOPSET_SITE_USR=user name you would like setup on your deployment site
    export PANOPSET_SITE_PWD=deployment site user's password
    export PANOPSET_SITE_REDIS_URL=your redis cache URL
    export PANOPSET_SITE_REDIS_PWD=your redis cache password



## Build Requirements

* Java 17

This project maintains (for now) both gradle and maven build configurations.

* Gradle 7.5.1 

and/or

* Maven 3.8.6


## Dependencies

Build the Panopset [src](https://github.com/panopset/src) project on your development PC.


# [Deploy](docs/deploy.md)ment instructions


## Desk version

DeskPageController
