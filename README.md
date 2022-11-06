# site

Panopset site source code.


# Development PC Requirements


Panopset recommends doing your web development on a Linux system.

These instructions will likely have to be extensively modified on Windows systems, but should work okay on macs.

Recommended ide is intellij.  I loved eclipse, but it just couldn't keep up with Kotlin I guess. You'll need:

* Java 17
* Kotlin

## Environment Variables

Names should be self-explanatory:

* PANOPSET_SITE_IP
* PANOPSET_SITE_NAME
* PANOPSET_SITE_USR
* PANOPSET_SITE_PWD
* PANOPSET_SITE_REDIS_URL
* PANOPSET_SITE_REDIS_PWD

## Build Requirements

This project maintains (for now) both gradle and maven build configurations.

* Gradle 7.5.1 

and/or

* Maven 3.8.6



# [Deploy](docs/deploy.md)ment instructions