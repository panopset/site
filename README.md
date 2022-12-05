# site

Panopset website source code.


# Development PC Requirements


Tested on a recent Linux mint system.


## Environment Variables

Set up these environment variables on your development PC.

* on Linux Mint it can be done in ~/.profile.
* on Macs it can be done in ~/.zshrc.
* on Windows you'd make them profile environment variables.



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
