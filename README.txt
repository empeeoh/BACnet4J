*Version 2.0.4 release notes*
-Project now generates javadoc and source via maven in package phase.
*Version 2.0.3 release notes*
-Project is now test-build-deploy lifecycle now managed by maven(3) rather than ant. Dependent libraries removed from repo tracking.

*Version 2.0.2 release notes*
-Non-blocking incoming message processor added. 
-Local device creation params improved. Changed to BRT from Serotonin. WIP need to make truly general from Vendor ID list.
-Many compiler warnings fixed (that in fairness, weren't warnings until v1.7 & new features.)

*Version 2.0.1 release notes*

-bacnet4J.jar dependency added. 

-seroUtils.jar (and its proprietary licensing) are no longer a dependency. Relevant functionality has been coded from scratch in org.free.bacnet4j.util package, 
and imported throughout project where needed. A discussion forum for this package can be found at http://mango.serotoninsoftware.com/forum/forums/show/12.page.

-Building the jar with ant should be close to one-click right from start of cloning.


*Version 2.0 release notes*

The networking package of this product has been pretty much entirely rewritten to support MS/TP. These changes implied
many changes to the LocalDevice public interface, so if you were using version 1.x you will need to port some code to
upgrade.
