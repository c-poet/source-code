#
# Activiti 5.15 shipped with some broken DDL statements for MySQL 5.6+ concering timestamps with millisecond precision
# If you are currently running on <= 5.14, simply execute the 5.15 and 5.15.1 update scripts.
# They have some duplication, but your shema will be in a correct state afterwards
# If you are currently running 5.15 on MySQL 5.6+ (by a sheer of luck of having data that didn't clash with the DDL),
# execute the 5.15.1 upgrade script, and all bugs shipped in Activiti 5.15 around these timestamp problems have been solved.
#

# Following statements are a duplication of the 5.15 updates. See above explanation why they need to be repeated. 

#  
# ACT-1867: MySQL DATETIME and TIMESTAMP precision
# The way this is done, is by creating a new column, pumping over all data
# and then removing the old column.
#

# DEPLOY_TIME_ in ACT_RE_DEPLOYMENT

ALTER TABLE ACT_RE_DEPLOYMENT ADD DEPLOY_TIME_TEMP_ timestamp(3);
UPDATE ACT_RE_DEPLOYMENT SET DEPLOY_TIME_TEMP_ = DEPLOY_TIME_;
ALTER TABLE ACT_RE_DEPLOYMENT DROP COLUMN DEPLOY_TIME_;
ALTER TABLE ACT_RE_DEPLOYMENT CHANGE DEPLOY_TIME_TEMP_ DEPLOY_TIME_ timestamp(3);


# LOCK_EXP_TIME_ in ACT_RU_JOB

ALTER TABLE ACT_RU_JOB ADD LOCK_EXP_TIME_TEMP_ timestamp(3) null;
UPDATE ACT_RU_JOB SET LOCK_EXP_TIME_TEMP_ = LOCK_EXP_TIME_; 
ALTER TABLE ACT_RU_JOB DROP COLUMN LOCK_EXP_TIME_;
ALTER TABLE ACT_RU_JOB CHANGE LOCK_EXP_TIME_TEMP_ LOCK_EXP_TIME_ timestamp(3) null;


# DUEDATE_ in ACT_RU_JOB

ALTER TABLE ACT_RU_JOB ADD DUEDATE_TEMP_ timestamp(3) null;
UPDATE ACT_RU_JOB SET DUEDATE_TEMP_ = DUEDATE_; 
ALTER TABLE ACT_RU_JOB DROP COLUMN DUEDATE_;
ALTER TABLE ACT_RU_JOB CHANGE DUEDATE_TEMP_ DUEDATE_ timestamp(3) null;


# CREATE_TIME_ in ACT_RU_TASK

ALTER TABLE ACT_RU_TASK ADD CREATE_TIME_TEMP_ timestamp(3);
UPDATE ACT_RU_TASK SET CREATE_TIME_TEMP_ = CREATE_TIME_; 
ALTER TABLE ACT_RU_TASK DROP COLUMN CREATE_TIME_;
ALTER TABLE ACT_RU_TASK CHANGE CREATE_TIME_TEMP_ CREATE_TIME_ timestamp(3);


# DUE_DATE_ in ACT_RU_TASK

ALTER TABLE ACT_RU_TASK ADD DUE_DATE_TEMP_ datetime(3);
UPDATE ACT_RU_TASK SET DUE_DATE_TEMP_ = DUE_DATE_; 
ALTER TABLE ACT_RU_TASK DROP COLUMN DUE_DATE_;
ALTER TABLE ACT_RU_TASK CHANGE DUE_DATE_TEMP_ DUE_DATE_ datetime(3);


# CREATED_ in ACT_RU_EVENT_SUBSCR

ALTER TABLE ACT_RU_EVENT_SUBSCR ADD CREATED_TEMP_ timestamp(3) not null;
UPDATE ACT_RU_EVENT_SUBSCR SET CREATED_TEMP_ = CREATED_; 
ALTER TABLE ACT_RU_EVENT_SUBSCR DROP COLUMN CREATED_;
ALTER TABLE ACT_RU_EVENT_SUBSCR CHANGE CREATED_TEMP_ CREATED_ timestamp(3) not null DEFAULT CURRENT_TIMESTAMP(3);



update ACT_GE_PROPERTY set VALUE_ = '5.15.1' where NAME_ = 'schema.version';
