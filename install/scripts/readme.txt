This project is meant to be build using Maven v1.1.b2
Running "maven multiproject:site" from this directory will generate a central web 
site with all the projects included in the target/docs directory.

Product Documentation 
http://singularity.firstopen.org/confluence

Issue Management
http://singularity.firstopen.org/jira


Dependencies:
	JBoss 4.0.2
	JDK 1.5.x
	MySql 4.x (InnoDB)

Installation:

unzip <singularity-1.0-M1.zip>

directory structure
	/event-process-manager/singularity
	/event-process-manager/db
	/device-manager
      /doc

1. Install JBoss 4.0.2
2. Install MySql 4.x (create database "singularity", load /event-process-manager/db/singularity_mysql.sql)
3. copy /event-process-manager/singularity to JBOSS_HOME/server
4  Modify singularity-mysql-ds.xml to configure the DataSource for the MySQL installation to use.
5. place device-manager anywhere on any host on the LAN (start on the same host as jboss then migrate to a multi-host environment)
6. /device-manager/start-all.sh or start-all.cmd
7. JBOSS_HOME/server/singularity/bin/start.sh or start.cmd

http://localhost:8080/admin

logon is admin/admin

see doc/manual.doc for more information.

















-----------------------------

TODO (focus areas)

EPC-IS  (not started)

Event Process Manager
	Rule Service Installed as MBean for Complex Event Processing. Needs administrative configuration for subscription
      to ALE service, and non-epc event/alert framework.

	Integrate JBPM for workflow

      Integrate object instance security famework (Acegi)

      Admin Console (Web and RCP)
		ALE Configuration
            Reader Management 
            Alert Configuration
            Rule Editor/Configuration
      
      Custom Applications
            move custom application demo out of Admin Web App.

      Hibernate Services
         	Generic MBean Hibernate services to use in any Appserver to uncouple from JBoss impelementation
            of SAR or HAR. 

	ALE 1.0 implementation
		Finish converstion from SAX to XMLBeans
            Create ALE WebService endpoint     
            Finish Report components.
            
      Configuration Management
            Device Manager Provisioning from manual installed properties to dynamic provisioning.

Build
	Multiple application server targets.
      extend custom application framework.
      Maven Site Generation content.
      Automate Daily build and publish of Maven Build Reports.


Documentation
	Administration
      Custom Application Development Guide (how to utilize Singularity)
      Developer Extension Guide
      Deployment Guide

      
      


        