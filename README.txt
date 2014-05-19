bacnet4J.jar, as of v2.0.1 no longer requires seroUtils.jar to be on the classpath.

A discussion forum for this package can be found at http://mango.serotoninsoftware.com/forum/forums/show/12.page.

*Version 2.0.1 release notes*

-seroUtils.jar (and its proprietary licensing) are no longer a dependency. Relevant functionality has been coded from scratch in org.free.bacnet4j.util package, 
and imported throughout project where needed.

-Building the jar with ant should be close to one-click right from start of cloning.


*Version 2.0 release notes*

The networking package of this product has been pretty much entirely rewritten to support MS/TP. These changes implied
many changes to the LocalDevice public interface, so if you were using version 1.x you will need to port some code to
upgrade.

Commercial licensers can pay an upgrade fee to use this new version (2.x) commercially.
 