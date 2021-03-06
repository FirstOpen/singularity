alter table ECBoundarySpec drop foreign key FK9B8B8EC3432DEAC0;
alter table ECBoundarySpec drop foreign key FK9B8B8EC3B4A6DCD0;
alter table ECBoundarySpec drop foreign key FK9B8B8EC399EB6748;
alter table ECSpec drop foreign key FK7995F5794F8A7534;
alter table ECSpec_IIds drop foreign key FK22B294B59187DB68;
alter table ECSpec_Subscribers drop foreign key FK53C04F259187DB68;
alter table Reader drop foreign key FK91AA44E323F821B6;
alter table ReaderEvent drop foreign key FK797FD3775CD7E9BE;
alter table ReaderEventTags drop foreign key FK4612E83077E68C69;
alter table ReaderEventTags drop foreign key FK4612E830236A712A;
alter table Sensor drop foreign key FK93653FDA4F125924;
alter table Sensor drop foreign key FK93653FDA23F821B6;
alter table ecspecs drop foreign key FK8E4E263A8B788562;
alter table readerSet drop foreign key FKBDF5909F911E23BA;
alter table readerSet drop foreign key FKBDF5909F72B6F0F6;
drop table if exists DeviceProfile;
drop table if exists ECBoundarySpec;
drop table if exists ECSpec;
drop table if exists ECSpec_IIds;
drop table if exists ECSpec_Subscribers;
drop table if exists ECTime;
drop table if exists LogicalDevice;
drop table if exists Reader;
drop table if exists ReaderEvent;
drop table if exists ReaderEventTags;
drop table if exists RuleBase;
drop table if exists Sensor;
drop table if exists ecspecs;
drop table if exists readerSet;
drop table if exists tag;
create table DeviceProfile (id varchar(128) not null, manufacturer varchar(255), model varchar(255), name varchar(255), serialNumber varchar(255), vendor varchar(255), version varchar(255), ipAddress varchar(255), port varchar(255), interrogatorClassName varchar(255), deviceProfileID varchar(255), baud varchar(255), deviceManagerID varchar(255), primary key (id));
create table ECBoundarySpec (boundarySpecId integer not null auto_increment, repeatPeriod integer unique, duration integer unique, stableSetInterval integer unique, primary key (boundarySpecId));
create table ECSpec (specName varchar(255) not null, boundaries integer unique, includeSpecInReports bit, xml text, currentState integer, primary key (specName));
create table ECSpec_IIds (specName varchar(255) not null, deviceManagerId varchar(255));
create table ECSpec_Subscribers (specName varchar(255) not null, subscribers varchar(255));
create table ECTime (timeId integer not null auto_increment, duration bigint, primary key (timeId));
create table LogicalDevice (id varchar(64) not null, name varchar(255), primary key (id));
create table Reader (id varchar(128) not null, name varchar(255), deviceManagerId varchar(255), device_profile_id varchar(128), primary key (id));
create table ReaderEvent (id varchar(128) not null, sensor varchar(128), date datetime, timestamp bigint, readerName varchar(255), primary key (id));
create table ReaderEventTags (reader_event_id varchar(128) not null, tag varchar(128) not null, position integer not null, primary key (reader_event_id, position));
create table RuleBase (id varchar(128) not null, ruleBaseURI varchar(255), primary key (id));
create table Sensor (id varchar(128) not null, deviceManagerId varchar(255), name varchar(255), device_profile_id varchar(128), reader_id varchar(128), primary key (id));
create table ecspecs (ecspecid varchar(128) not null, ecspecname varchar(255));
create table readerSet (logical_device_id varchar(64) not null, elt varchar(128) not null, primary key (logical_device_id, elt));
create table tag (id varchar(128) not null, tampered bit, value varchar(255), printedId varchar(255), state integer, primary key (id));
alter table ECBoundarySpec add index FK9B8B8EC3432DEAC0 (stableSetInterval), add constraint FK9B8B8EC3432DEAC0 foreign key (stableSetInterval) references ECTime (timeId);
alter table ECBoundarySpec add index FK9B8B8EC3B4A6DCD0 (repeatPeriod), add constraint FK9B8B8EC3B4A6DCD0 foreign key (repeatPeriod) references ECTime (timeId);
alter table ECBoundarySpec add index FK9B8B8EC399EB6748 (duration), add constraint FK9B8B8EC399EB6748 foreign key (duration) references ECTime (timeId);
alter table ECSpec add index FK7995F5794F8A7534 (boundaries), add constraint FK7995F5794F8A7534 foreign key (boundaries) references ECBoundarySpec (boundarySpecId);
alter table ECSpec_IIds add index FK22B294B59187DB68 (specName), add constraint FK22B294B59187DB68 foreign key (specName) references ECSpec (specName);
alter table ECSpec_Subscribers add index FK53C04F259187DB68 (specName), add constraint FK53C04F259187DB68 foreign key (specName) references ECSpec (specName);
alter table Reader add index FK91AA44E323F821B6 (device_profile_id), add constraint FK91AA44E323F821B6 foreign key (device_profile_id) references DeviceProfile (id);
alter table ReaderEvent add index FK797FD3775CD7E9BE (sensor), add constraint FK797FD3775CD7E9BE foreign key (sensor) references Sensor (id);
alter table ReaderEventTags add index FK4612E83077E68C69 (reader_event_id), add constraint FK4612E83077E68C69 foreign key (reader_event_id) references ReaderEvent (id);
alter table ReaderEventTags add index FK4612E830236A712A (tag), add constraint FK4612E830236A712A foreign key (tag) references tag (id);
alter table Sensor add index FK93653FDA4F125924 (reader_id), add constraint FK93653FDA4F125924 foreign key (reader_id) references Reader (id);
alter table Sensor add index FK93653FDA23F821B6 (device_profile_id), add constraint FK93653FDA23F821B6 foreign key (device_profile_id) references DeviceProfile (id);
alter table ecspecs add index FK8E4E263A8B788562 (ecspecid), add constraint FK8E4E263A8B788562 foreign key (ecspecid) references RuleBase (id);
alter table readerSet add index FKBDF5909F911E23BA (elt), add constraint FKBDF5909F911E23BA foreign key (elt) references Reader (id);
alter table readerSet add index FKBDF5909F72B6F0F6 (logical_device_id), add constraint FKBDF5909F72B6F0F6 foreign key (logical_device_id) references LogicalDevice (id);
